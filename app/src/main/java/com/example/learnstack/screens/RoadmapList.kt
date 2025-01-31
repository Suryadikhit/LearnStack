package com.example.learnstack.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.learnstack.R
import com.example.learnstack.components.GreetingSection
import com.example.learnstack.components.RoadmapBox
import com.example.learnstack.components.SearchFieldWithCloseButton
import com.example.learnstack.components.SquareCardGrid
import com.example.learnstack.topics.topicIcons
import com.example.learnstack.ui.theme.TopBar
import com.example.learnstack.viewmodels.MainViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce


@OptIn(FlowPreview::class)
@SuppressLint("StateFlowValueCalledInComposition", "MutableCollectionMutableState")
@Composable
fun RoadmapList(
    navController: NavHostController,
    onMenuClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("prefs", Context.MODE_PRIVATE) }


    val roadmaps = mainViewModel.roadmaps.collectAsState(initial = emptyList()).value
    val isLoading = mainViewModel.isLoading.value
    val searchQuery = remember { mutableStateOf("") }
    val isSearchActive = remember { mutableStateOf(false) }
    val expandedRoadmapIndices = remember { mutableStateOf(mutableSetOf<Int>()) }
    val hasFetchedData = remember { mutableStateOf(false) }

    // Initialize LazyListState with saved scroll position
    val scrollState = rememberLazyListState()

    LaunchedEffect(roadmaps) {
        if (roadmaps.isNotEmpty()) {
            val restoredPosition = prefs.getInt("scroll_position", 0)
            Log.d("ScrollDebug", "Restoring Scroll Position: $restoredPosition")

            // Ensure smooth transition by using animateScrollToItem
            // Adding a small delay to give time for the list to be laid out
            delay(100) // Short delay to avoid abrupt scrolling
            scrollState.animateScrollToItem(restoredPosition, scrollOffset = 0)
        }
    }

// ✅ Save Scroll Position Efficiently with Coroutine Optimization
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.firstVisibleItemIndex }
            .debounce(500L) // Reduce unnecessary saves
            .collectLatest { index ->
                prefs.edit().putInt("scroll_position", index).apply()
                Log.d("ScrollDebug", "Saving Scroll Position: $index")
            }
    }





    if (roadmaps.isNotEmpty() && !hasFetchedData.value) {
        hasFetchedData.value = true
        expandedRoadmapIndices.value.addAll(roadmaps.indices)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "LearnStack",
                onMenuClicked = onMenuClicked,
                onSearchClicked = {
                    isSearchActive.value = !isSearchActive.value
                    onSearchClicked()
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
            ) {
                if (isLoading && !hasFetchedData.value) {
                    // Use Lottie Animation for loading
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
                    LottieAnimation(
                        modifier = Modifier.align(Alignment.Center),
                        composition = composition,
                        iterations = LottieConstants.IterateForever
                    )
                } else {
                    val filteredRoadmaps = if (searchQuery.value.isNotEmpty()) {
                        roadmaps.filter { it.roadmapName.contains(searchQuery.value, true) }
                    } else {
                        roadmaps
                    }

                    LazyColumn(
                        state = scrollState,  // ✅ Scroll state persists
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 64.dp)
                    ) {
                        item {
                            GreetingSection(navController)
                        }

                        if (isSearchActive.value) {
                            item {
                                SearchFieldWithCloseButton(
                                    searchQuery = searchQuery,
                                    onSearchClose = {
                                        isSearchActive.value = false
                                        searchQuery.value = ""
                                    }
                                )
                            }
                        }

                        items(filteredRoadmaps) { roadmapWithBoxes ->
                            val currentIndex = filteredRoadmaps.indexOf(roadmapWithBoxes)
                            val isExpanded = currentIndex in expandedRoadmapIndices.value

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        expandedRoadmapIndices.value =
                                            expandedRoadmapIndices.value.toMutableSet().apply {
                                                if (isExpanded) remove(currentIndex) else add(
                                                    currentIndex
                                                )
                                            }
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column {
                                    RoadmapBox(
                                        name = roadmapWithBoxes.roadmapName,
                                        onExpandToggle = {
                                            expandedRoadmapIndices.value =
                                                expandedRoadmapIndices.value.toMutableSet().apply {
                                                    if (isExpanded) remove(currentIndex) else add(
                                                        currentIndex
                                                    )
                                                }
                                        }
                                    )

                                    if (roadmapWithBoxes.boxes.isEmpty()) {
                                        Text(
                                            text = "No details available",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    } else if (isExpanded) {
                                        SquareCardGrid(
                                            roadmapName = roadmapWithBoxes.roadmapName,
                                            roadmapTopics = roadmapWithBoxes.boxes.mapNotNull { boxModel ->
                                                val iconResId = topicIcons[boxModel?.name]
                                                boxModel?.copy(iconResId = iconResId)
                                            },
                                            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                                            navController = navController
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
