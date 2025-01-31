package com.example.learnstack.navigation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.learnstack.animations.getPopSlideInOutAnimation
import com.example.learnstack.animations.getSlideInOutAnimation
import com.example.learnstack.notes.HomeScreen
import com.example.learnstack.notes.ShowPdfScreen
import com.example.learnstack.quiz.QuizScreen
import com.example.learnstack.quiz.QuizTopicsScreen
import com.example.learnstack.screens.BookmarkScreen
import com.example.learnstack.screens.HelpScreen
import com.example.learnstack.screens.RoadmapList
import com.example.learnstack.screens.SlidingScreen
import com.example.learnstack.topics.SquareBoxDetailScreen
import com.example.learnstack.topics.TopicDetailScreen
import com.example.learnstack.viewmodels.MainViewModel

@SuppressLint("NewApi")
@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: MainViewModel,
    onMenuClicked: () -> Unit,

    ) {

    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()


    val startDestination = if (isFirstLaunch) "sliding_screen" else "roadmapList"
    val speedFactor = 50 // You can adjust this value to change the speed
    val context = LocalContext.current

    viewModel.roadmaps.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Sliding Screen (no animation)
        composable("sliding_screen") {
            SlidingScreen(navController = navController)
        }

        composable(
            "roadmapList",
            enterTransition = { getSlideInOutAnimation(speedFactor).first },
            exitTransition = { getSlideInOutAnimation(speedFactor).second },
            popEnterTransition = { getPopSlideInOutAnimation(speedFactor).first },
            popExitTransition = { getPopSlideInOutAnimation(speedFactor).second }
        ) {
            RoadmapList(
                navController = navController,
                onMenuClicked = onMenuClicked,
                onSearchClicked = { println("Search clicked!") },
                mainViewModel = viewModel
            )
        }


        composable(
            "help",
            enterTransition = { getSlideInOutAnimation(speedFactor).first },
            exitTransition = { getSlideInOutAnimation(speedFactor).second },
            popEnterTransition = { getPopSlideInOutAnimation(speedFactor).first },
            popExitTransition = { getPopSlideInOutAnimation(speedFactor).second }
        ) {
            HelpScreen()
        }

        composable("bookmark",
            enterTransition = { getSlideInOutAnimation(speedFactor).first },
            exitTransition = { getSlideInOutAnimation(speedFactor).second },
            popEnterTransition = { getPopSlideInOutAnimation(speedFactor).first },
            popExitTransition = { getPopSlideInOutAnimation(speedFactor).second }
        ) {
            BookmarkScreen(
                navController = navController,
                viewModel = viewModel
            ) // Pass navController here
        }

        composable(
            route = "login",
            enterTransition = { getSlideInOutAnimation(speedFactor).first },
            exitTransition = { getSlideInOutAnimation(speedFactor).second },
            popEnterTransition = { getPopSlideInOutAnimation(speedFactor).first },
            popExitTransition = { getPopSlideInOutAnimation(speedFactor).second }
        ) {
            SignInSignUpScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            "quizzes",
            enterTransition = { getSlideInOutAnimation(speedFactor).first },
            exitTransition = { getSlideInOutAnimation(speedFactor).second },
            popEnterTransition = { getPopSlideInOutAnimation(speedFactor).first },
            popExitTransition = { getPopSlideInOutAnimation(speedFactor).second }
        ) {
            QuizTopicsScreen(navController)
        }
        composable(
            "quizScreen/{topic}",
            enterTransition = { getSlideInOutAnimation(speedFactor).first },
            exitTransition = { getSlideInOutAnimation(speedFactor).second },
            popEnterTransition = { getPopSlideInOutAnimation(speedFactor).first },
            popExitTransition = { getPopSlideInOutAnimation(speedFactor).second }
        ) { backStackEntry ->
            val topic = backStackEntry.arguments?.getString("topic") ?: "DefaultTopic"
            QuizScreen(navController, topic)

        }


        composable("Notes",
            enterTransition = { getSlideInOutAnimation(speedFactor).first },
            exitTransition = { getSlideInOutAnimation(speedFactor).second },
            popEnterTransition = { getPopSlideInOutAnimation(speedFactor).first },
            popExitTransition = { getPopSlideInOutAnimation(speedFactor).second }) {
            // Home screen with navigation to PDF screen
            HomeScreen(navController = navController, viewModel = viewModel,
                onCardClick = { fileName ->
                    // Navigate to ShowPdfScreen with the selected file name
                    navController.navigate("pdfScreen/$fileName")
                }
            )
        }
        composable("pdfScreen/{fileName}",
            enterTransition = { getSlideInOutAnimation(speedFactor).first },
            exitTransition = { getSlideInOutAnimation(speedFactor).second },
            popEnterTransition = { getPopSlideInOutAnimation(speedFactor).first },
            popExitTransition = { getPopSlideInOutAnimation(speedFactor).second }) { backStackEntry ->
            // Retrieve the file name from the arguments and display the PDF
            val fileName = backStackEntry.arguments?.getString("fileName") ?: ""
            ShowPdfScreen(
                fileName = fileName,
                navController = navController,
                context = context,
                viewModel = viewModel
            )
        }







        composable(
            "box_detail/{roadmapName}/{boxName}",
            arguments = listOf(
                navArgument("roadmapName") { type = NavType.StringType },
                navArgument("boxName") { type = NavType.StringType }
            ),
            enterTransition = { getSlideInOutAnimation(speedFactor).first },
            exitTransition = { getSlideInOutAnimation(speedFactor).second },
            popEnterTransition = { getPopSlideInOutAnimation(speedFactor).first },
            popExitTransition = { getPopSlideInOutAnimation(speedFactor).second }

        ) { backStackEntry ->
            val encodedRoadmapName = backStackEntry.arguments?.getString("roadmapName")
            val encodedBoxName = backStackEntry.arguments?.getString("boxName")

            val decodedRoadmapName = Uri.decode(encodedRoadmapName)
            val decodedBoxName = Uri.decode(encodedBoxName)

            val roadmaps = viewModel.roadmaps.collectAsState().value
            val roadmap = roadmaps.firstOrNull { it.roadmapName == decodedRoadmapName }
            val box = roadmap?.boxes?.firstOrNull { it?.name == decodedBoxName }


            if (box != null) {
                SquareBoxDetailScreen(
                    box = box,
                    onBackClick = {
                        // Use launchSingleTop to ensure we don't re-create the screen
                        navController.popBackStack("roadmapList", inclusive = false)

                    },
                    navController = navController
                )
            } else {
                Text(
                    "Box not found or loading...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        composable(
            "topic_detail/{topicTitle}",
            arguments = listOf(
                navArgument("topicTitle") { type = NavType.StringType }
            ),
            enterTransition = { getSlideInOutAnimation(speedFactor).first },
            exitTransition = { getSlideInOutAnimation(speedFactor).second },
            popEnterTransition = { getPopSlideInOutAnimation(speedFactor).first },
            popExitTransition = { getPopSlideInOutAnimation(speedFactor).second }

        ) { backStackEntry ->
            val encodedTopicTitle = backStackEntry.arguments?.getString("topicTitle")
            val decodedTopicTitle = Uri.decode(encodedTopicTitle)

            val roadmaps = viewModel.roadmaps.collectAsState().value
            val topic = roadmaps
                .flatMap { it.boxes }
                .firstOrNull { box -> box!!.topics.any { it.title == decodedTopicTitle } }
                ?.topics?.firstOrNull { it.title == decodedTopicTitle }

            if (topic != null) {
                TopicDetailScreen(
                    topic = topic,
                    onBackClick = { navController.popBackStack() },
                    viewModel = viewModel
                    // Pass instance here
                )
            } else {
                Text(
                    "Topic not found or loading...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
