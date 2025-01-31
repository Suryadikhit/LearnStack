package com.example.learnstack.roomdata


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

// Entities
@Entity(tableName = "roadmaps")
data class RoadmapEntity(
    @PrimaryKey val roadmapId: String,
    val roadmapName: String,
    val lastUpdated: String,

    )

@Entity(tableName = "boxes")
data class BoxEntity(
    @PrimaryKey val boxId: String,
    val roadmapId: String,
    val name: String,
    val lastUpdated: String,
)

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey val topicId: String,
    val boxId: String,
    val title: String,
    val description: String,
    val lastUpdated: String,
    val isBookmarked: Boolean

)

@Entity(tableName = "resources")
data class ResourceEntity(
    @PrimaryKey val resourceId: String,
    val topicId: String,
    val type: String,
    val title: String,
    val link: String,
    val lastUpdated: String
)


// DAOs
@Dao
interface RoadmapDao {
    @Query("SELECT * FROM roadmaps")
    fun getAllRoadmaps(): List<RoadmapEntity>

    @Query("SELECT MAX(lastUpdated) FROM roadmaps")
    suspend fun getLatestUpdateTime(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoadmaps(roadmaps: List<RoadmapEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(roadmap: RoadmapEntity)

}

@Dao
interface BoxDao {
    @Query("SELECT * FROM boxes WHERE roadmapId = :roadmapId")
    suspend fun getBoxesForRoadmap(roadmapId: String): List<BoxEntity>

    @Query("SELECT MAX(lastUpdated) FROM boxes WHERE roadmapId = :roadmapId")
    suspend fun getLatestUpdateTime(roadmapId: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoxes(boxes: List<BoxEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(box: BoxEntity)

}

@Dao
interface TopicDao {
    @Query("SELECT * FROM topics WHERE boxId = :boxId")
    suspend fun getTopicsForBox(boxId: String): List<TopicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topics: List<TopicEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(topic: TopicEntity)

    // Updated to return LiveData for UI observation
    @Query("SELECT * FROM topics WHERE isBookmarked = 1")
    fun getBookmarkedTopics(): LiveData<List<TopicEntity>>

    // Renamed to use topicTitle instead of topicId
    @Query("UPDATE topics SET isBookmarked = :isBookmarked WHERE title = :topicTitle")
    suspend fun updateBookmarkState(topicTitle: String, isBookmarked: Boolean)

}


@Dao
interface ResourceDao {
    @Query("SELECT * FROM resources WHERE topicId = :topicId")
    suspend fun getResourcesForTopic(topicId: String): List<ResourceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResources(resources: List<ResourceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(resource: ResourceEntity)

}


// Room Database
@Database(
    entities = [RoadmapEntity::class, BoxEntity::class, TopicEntity::class, ResourceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roadmapDao(): RoadmapDao
    abstract fun boxDao(): BoxDao
    abstract fun topicDao(): TopicDao
    abstract fun resourceDao(): ResourceDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
