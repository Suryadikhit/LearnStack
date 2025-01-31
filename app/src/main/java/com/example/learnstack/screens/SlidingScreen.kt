package com.example.learnstack.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learnstack.R
import com.example.learnstack.utils.SharedPreferenceHelper
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

// Define the com.example.learnstack.PageContent data class
data class PageContent(
    val image: Int, // Changed image type to resource ID
    val title: String,
    val description: String
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SlidingScreen(navController: NavController) {


    val context = LocalContext.current
    val pages = listOf(
        PageContent(
            image = R.drawable.goals, // Add your image resource here
            title = "Discover Your Path",
            description = "Explore the initial steps of your journey. Understand your goals and the roadmap ahead to begin your path to personal growth."
        ),
        PageContent(
            image = R.drawable.knowledge, // Add your image resource here
            title = "Build Knowledge",
            description = "Gather the knowledge and tools necessary to move forward. Dive deeper into topics that support your roadmap journey."
        ),
        PageContent(
            image = R.drawable.action, // Add your image resource here
            title = "Take Action",
            description = "Implement what you’ve learned, start taking real action, and progress towards your goals with each milestone."
        )
    )

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // HorizontalPager for sliding screens
            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f) // Makes the pager take most of the available height
            ) { page ->
                val content = pages[page]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,  // Align horizontally
                    verticalArrangement = Arrangement.Center // Align vertically in the center
                ) {
                    // Image above description
                    Image(
                        painter = painterResource(id = content.image),
                        contentDescription = content.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // Adjust height of the image as needed
                            .padding(bottom = 16.dp)
                    )

                    // Title (bold)
                    Text(
                        text = content.title,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold, // Make the title bold
                        textAlign = TextAlign.Center
                    )

                    // Spacer between title and description
                    Spacer(modifier = Modifier.padding(vertical = 8.dp)) // Adds spacing between title and description

                    // Description
                    Text(
                        text = content.description,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Page Indicator (dots) at the bottom center
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                activeColor = Color.Black,
                inactiveColor = Color.Gray
            )

            // Navigation button ("Next") at the bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 65.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (pagerState.currentPage < pages.lastIndex) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            // Mark the first launch as completed
                            SharedPreferenceHelper.getInstance(context).setFirstLaunch(false)
                            // Navigate to the roadmap list
                            navController.navigate("roadmapList") {
                                popUpTo("sliding_screen") { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    shape = CircleShape,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = if (pagerState.currentPage == pages.lastIndex) "Let's Go" else "Next",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
