package com.example.learnstack.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnstack.roomdata.AppDatabase
import com.example.learnstack.roomdata.BoxEntity
import com.example.learnstack.roomdata.ResourceEntity
import com.example.learnstack.roomdata.RoadmapEntity
import com.example.learnstack.roomdata.TopicEntity
import com.example.learnstack.utils.SharedPreferenceHelper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone


@Suppress("LABEL_NAME_CLASH")
class MainViewModel(
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val appDatabase: AppDatabase

) : ViewModel() {

    private val tag = "MainViewModel"

    private val _isLoggedIn = MutableStateFlow(sharedPreferenceHelper.isUserLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _username = MutableStateFlow(sharedPreferenceHelper.getStoredUsername())
    val username: StateFlow<String> get() = _username

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _roadmaps = MutableStateFlow<List<RoadmapWithBoxes>>(emptyList())
    val roadmaps: StateFlow<List<RoadmapWithBoxes>> get() = _roadmaps

    private val firestore = FirebaseFirestore.getInstance()

    private val _isFirstLaunch = MutableStateFlow(sharedPreferenceHelper.isFirstLaunch())
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch

    // Inside MainViewModel
    private val _isDrawerDisabled = mutableStateOf(false)
    val isDrawerDisabled: State<Boolean> = _isDrawerDisabled

    fun setDisableDrawer(isDisabled: Boolean) {
        _isDrawerDisabled.value = isDisabled
    }


    init {
        viewModelScope.launch {
            // First, fetch data if it's the first launch, then set the flag to false
            if (sharedPreferenceHelper.isFirstLaunch()) {
                Log.d(tag, "First launch, fetching roadmaps from Firebase.")
                try {
                    fetchRoadmapsFromFirebase() // Fetch roadmaps first
                    Log.d(tag, "First launch, setting first launch flag to false.")
                    sharedPreferenceHelper.setFirstLaunch(false) // Then set the flag to false
                } catch (e: Exception) {
                    Log.e(tag, "Error fetching roadmaps from Firebase", e)
                    // Optionally, handle the error or fallback to Room
                }
            } else {
                Log.d(tag, "Not first launch, checking for updates in Firebase.")
                checkForUpdatesInFirebase() // If it's not the first launch, check for updates
            }
            Log.d(tag, "Initializing ViewModel and syncing data.")
        }
    }


    // Assuming appDatabase is already initialized
    private val topicDao = appDatabase.topicDao()

    val bookmarkedTopics: LiveData<List<TopicEntity>> = topicDao.getBookmarkedTopics()

    // Update the bookmark state for a specific topic
    fun toggleBookmark(topicTitle: String, currentState: Boolean) {
        Log.d(
            "MainViewModel",
            "Toggling bookmark for topic: $topicTitle. Current state: $currentState"
        )

        viewModelScope.launch {
            try {
                // Log before updating the bookmark state
                Log.d(
                    "MainViewModel",
                    "Updating bookmark state for topic: $topicTitle to ${!currentState}"
                )

                // Update the bookmark state in the database
                topicDao.updateBookmarkState(topicTitle, !currentState)

                // Log success
                Log.d("MainViewModel", "Successfully updated bookmark state for topic: $topicTitle")
            } catch (e: Exception) {
                // Log any errors
                Log.e("MainViewModel", "Error updating bookmark state for topic: $topicTitle", e)
            }
        }
    }


    private fun checkForUpdatesInFirebase() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        viewModelScope.launch {
            try {
                Log.d(tag, "Checking for updates in Firebase.")
                val roadmapsSnapshot = firestore.collection("Roadmaps").get().await()
                var shouldFetchFromFirebase = false

                roadmapsSnapshot.documents.forEach { roadmapDoc ->
                    val roadmapName = roadmapDoc.getString("roadmapName") ?: return@forEach
                    val updatedAtTimestamp = roadmapDoc.getTimestamp("updatedAt")

                    // Use parseTimestamp method here to convert the timestamp string
                    val roadmapUpdatedDate = updatedAtTimestamp?.let { timestamp ->
                        convertTimestampToDate(timestamp, dateFormat)
                    } ?: return@forEach

                    val savedUpdatedAt = sharedPreferenceHelper.getUpdatedAtForRoadmap(roadmapName)

                    Log.d(tag, "Comparing timestamps for roadmap: $roadmapName")
                    Log.d(
                        tag,
                        "Saved Timestamp (UTC): $savedUpdatedAt, Firebase Timestamp (UTC): $roadmapUpdatedDate"
                    )

                    // If savedUpdatedAt is null or Firebase has a more recent timestamp, we need to fetch from Firebase
                    if (savedUpdatedAt == null || parseTimestamp(savedUpdatedAt).isBefore(
                            parseTimestamp(roadmapUpdatedDate)
                        )
                    ) {
                        sharedPreferenceHelper.setUpdatedAtForRoadmap(
                            roadmapName,
                            roadmapUpdatedDate
                        )
                        shouldFetchFromFirebase = true
                    }
                }

                if (shouldFetchFromFirebase) {
                    fetchRoadmapsFromFirebase()
                } else {
                    fetchRoadmapsFromRoom()
                }

            } catch (e: Exception) {
                Log.e(tag, "Error checking updates from Firebase", e)
                fetchRoadmapsFromRoom()  // Fallback to Room
            }
        }
    }


    private fun parseTimestamp(timestamp: String): Instant {
        // Define the format for the timestamp string
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // Parse the timestamp string into LocalDateTime
        val localDateTime = LocalDateTime.parse(timestamp, formatter)

        // Convert LocalDateTime to Instant (UTC)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant()
    }


    private suspend fun insertRoadmapsIntoRoom(fetchedRoadmaps: List<RoadmapWithBoxes>) {
        withContext(Dispatchers.IO) {
            try {
                val startTime = System.currentTimeMillis()
                Log.d(tag, "Inserting fetched roadmaps into Room.")
                fetchedRoadmaps.forEach { roadmap ->
                    val roadmapEntity = RoadmapEntity(
                        roadmapId = roadmap.roadmapName.hashCode().toString(),
                        roadmapName = roadmap.roadmapName,
                        lastUpdated = System.currentTimeMillis().toString()
                    )
                    appDatabase.roadmapDao().insertRoadmaps(listOf(roadmapEntity))

                    roadmap.boxes.forEach { box ->
                        val boxEntity = BoxEntity(
                            boxId = box?.name.hashCode().toString(),
                            roadmapId = roadmapEntity.roadmapId,
                            name = box?.name ?: "",

                            lastUpdated = System.currentTimeMillis().toString()
                        )
                        appDatabase.boxDao().insertBoxes(listOf(boxEntity))

                        box?.topics?.forEach { topic ->
                            val topicEntity = TopicEntity(
                                topicId = topic.title.hashCode().toString(),
                                boxId = boxEntity.boxId,
                                title = topic.title,
                                description = topic.description,
                                lastUpdated = System.currentTimeMillis().toString(),
                                isBookmarked = topic.isBookmarked
                            )
                            appDatabase.topicDao().insertTopics(listOf(topicEntity))

                            topic.resources.forEach { resource ->
                                val resourceEntity = ResourceEntity(
                                    resourceId = resource.title.hashCode().toString(),
                                    topicId = topicEntity.topicId,
                                    type = resource.type,
                                    title = resource.title,
                                    link = resource.link,
                                    lastUpdated = System.currentTimeMillis().toString()
                                )
                                appDatabase.resourceDao().insertResources(listOf(resourceEntity))
                            }
                        }
                    }
                }
                val endTime = System.currentTimeMillis()
                val durationInSeconds = (endTime - startTime) / 1000.0
                Log.d(tag, "Roadmaps insertion completed in $durationInSeconds seconds.")
            } catch (e: Exception) {
                Log.e(tag, "Error inserting roadmaps into Room", e)
            }
        }
    }


    private fun fetchRoadmapsFromRoom() {
        Log.d(tag, "Fetching roadmaps from Room database.")
        _isLoading.value = false
        viewModelScope.launch {
            try {
                val roadmapsFromRoom = withContext(Dispatchers.IO) {
                    appDatabase.roadmapDao().getAllRoadmaps()
                }

                if (roadmapsFromRoom.isNotEmpty()) {
                    _roadmaps.value = roadmapsFromRoom.map { roadmapEntity ->
                        val boxes = appDatabase.boxDao().getBoxesForRoadmap(roadmapEntity.roadmapId)
                            .map { boxEntity ->
                                BoxModel(
                                    name = boxEntity.name,

                                    topics = appDatabase.topicDao().getTopicsForBox(boxEntity.boxId)
                                        .map { topicEntity ->
                                            TopicModel(
                                                title = topicEntity.title,
                                                description = topicEntity.description,
                                                resources = appDatabase.resourceDao()
                                                    .getResourcesForTopic(topicEntity.topicId)
                                                    .map { resourceEntity ->
                                                        ResourceItem(
                                                            type = resourceEntity.type,
                                                            title = resourceEntity.title,
                                                            link = resourceEntity.link,
                                                            updatedAt = resourceEntity.lastUpdated
                                                        )
                                                    },
                                                updatedAt = topicEntity.lastUpdated,
                                                isBookmarked = topicEntity.isBookmarked
                                            )
                                        },
                                    updatedAt = boxEntity.lastUpdated
                                )
                            }

                        RoadmapWithBoxes(
                            roadmapName = roadmapEntity.roadmapName,
                            boxes = boxes,
                            updatedAt = roadmapEntity.lastUpdated
                        )
                    }
                } else {
                    Log.d(tag, "No roadmaps found in Room database.")
                }
            } catch (e: Exception) {
                Log.e(tag, "Error fetching roadmaps from Room", e)
            }
        }
    }

    private fun fetchRoadmapsFromFirebase() {
        Log.d(tag, "Fetching roadmaps from Firebase.")
        _isLoading.value = true

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        viewModelScope.launch {
            val startTime = System.currentTimeMillis() // Start tracking total time

            try {
                val firebaseFetchStart =
                    System.currentTimeMillis() // Start tracking Firebase fetch time

                // Fetch all roadmaps at once
                Log.d(tag, "Fetching roadmaps from Firestore...")
                val roadmapsSnapshot = firestore.collection("Roadmaps").get().await()

                val roadmapsDeferred = roadmapsSnapshot.documents.map { roadmapDoc ->
                    async {
                        val roadmapName = roadmapDoc.getString("roadmapName") ?: return@async null
                        val updatedAtTimestamp = roadmapDoc.getTimestamp("updatedAt")
                        val roadmapUpdatedDate =
                            convertTimestampToDate(updatedAtTimestamp, dateFormat)

                        val savedUpdatedAt =
                            sharedPreferenceHelper.getUpdatedAtForRoadmap(roadmapName)
                        if (savedUpdatedAt != roadmapUpdatedDate) {
                            sharedPreferenceHelper.setUpdatedAtForRoadmap(
                                roadmapName,
                                roadmapUpdatedDate
                            )
                        }

                        // Fetch boxes concurrently
                        val boxSnapshot = firestore.collection("Roadmaps")
                            .document(roadmapDoc.id)
                            .collection("Boxes")
                            .get()
                            .await()

                        val boxesDeferred = boxSnapshot.documents.map { boxDoc ->
                            async {
                                val boxName = boxDoc.getString("name") ?: return@async null
                                val boxUpdatedAtTimestamp = boxDoc.getTimestamp("updatedAt")
                                val boxUpdatedDate =
                                    convertTimestampToDate(boxUpdatedAtTimestamp, dateFormat)

                                // Fetch topics concurrently
                                val topicsSnapshot = firestore.collection("Roadmaps")
                                    .document(roadmapDoc.id)
                                    .collection("Boxes")
                                    .document(boxDoc.id)
                                    .collection("Topics")
                                    .get()
                                    .await()

                                val topicsDeferred = topicsSnapshot.documents.map { topicDoc ->
                                    async {
                                        val topicUpdatedAtTimestamp =
                                            topicDoc.getTimestamp("updatedAt")
                                        val topicUpdatedDate = convertTimestampToDate(
                                            topicUpdatedAtTimestamp,
                                            dateFormat
                                        )

                                        // Fetch resources concurrently
                                        val resourcesSnapshot = firestore.collection("Roadmaps")
                                            .document(roadmapDoc.id)
                                            .collection("Boxes")
                                            .document(boxDoc.id)
                                            .collection("Topics")
                                            .document(topicDoc.id)
                                            .collection("Resources")
                                            .get()
                                            .await()

                                        val resources =
                                            resourcesSnapshot.documents.map { resourceDoc ->
                                                val resourceUpdatedAtTimestamp =
                                                    resourceDoc.getTimestamp("updatedAt")
                                                val resourceUpdatedDate = convertTimestampToDate(
                                                    resourceUpdatedAtTimestamp,
                                                    dateFormat
                                                )

                                                ResourceItem(
                                                    type = resourceDoc.getString("type") ?: "",
                                                    title = resourceDoc.getString("title") ?: "",
                                                    link = resourceDoc.getString("link") ?: "",
                                                    updatedAt = resourceUpdatedDate
                                                )
                                            }

                                        TopicModel(
                                            title = topicDoc.getString("title") ?: "",
                                            description = topicDoc.getString("description") ?: "",
                                            resources = resources,
                                            updatedAt = topicUpdatedDate,
                                            isBookmarked = topicDoc.getBoolean("isBookmarked")
                                                ?: false
                                        )
                                    }
                                }

                                val topics = topicsDeferred.awaitAll()

                                BoxModel(
                                    name = boxName,
                                    topics = topics,
                                    updatedAt = boxUpdatedDate
                                )
                            }
                        }

                        val boxes = boxesDeferred.awaitAll()

                        RoadmapWithBoxes(
                            roadmapName = roadmapName,
                            boxes = boxes,
                            updatedAt = roadmapUpdatedDate
                        )
                    }
                }

                val fetchedRoadmaps = roadmapsDeferred.awaitAll().filterNotNull()

                val firebaseFetchEnd = System.currentTimeMillis() // End Firebase fetch time
                Log.d(
                    "FirebaseFetchTime",
                    "Firebase fetch time: ${(firebaseFetchEnd - firebaseFetchStart) / 1000}s"
                )

                if (fetchedRoadmaps.isNotEmpty()) {
                    insertRoadmapsIntoRoom(fetchedRoadmaps)
                    _roadmaps.value = fetchedRoadmaps
                }

            } catch (e: Exception) {
                Log.e(tag, "Error fetching roadmaps from Firebase", e)
            } finally {
                _isLoading.value = false
                val endTime = System.currentTimeMillis() // End total execution time
                Log.d(tag, "Total execution time: ${(endTime - startTime) / 1000}s")
            }
        }
    }


    private fun convertTimestampToDate(
        timestamp: com.google.firebase.Timestamp?,
        dateFormat: SimpleDateFormat
    ): String {
        return try {
            timestamp?.toDate()?.let { dateFormat.format(it) } ?: "Invalid Date"
        } catch (e: Exception) {
            Log.e(tag, "Error converting timestamp to date", e)
            "Invalid Date"
        }
    }


    fun onLogout() {
        Log.d(tag, "User logging out.")
        sharedPreferenceHelper.clearLoginInfo()
        _isLoggedIn.value = false
        _username.value = "Guest"
    }

    fun onLoginSuccess(newUsername: String) {
        Log.d(tag, "User logged in with username: $newUsername.")
        sharedPreferenceHelper.storeUsername(newUsername)
        _isLoggedIn.value = true
        _username.value = newUsername
    }
}
