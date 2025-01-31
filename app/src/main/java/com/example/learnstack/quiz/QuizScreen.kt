package com.example.learnstack.quiz

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.learnstack.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(navController: NavController, topicName: String) {
    val topicQuestions = quizzes[topicName]?.shuffled() // Shuffle questions once
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var showResult by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableIntStateOf(0) }
    var selectedAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }

    // Shuffle options and track the correct index for each question
    val shuffledQuestions = remember(topicQuestions) {
        topicQuestions?.map { question ->
            val shuffledOptions = question.options.shuffled()
            val newCorrectAnswerIndex =
                shuffledOptions.indexOf(question.options[question.correctAnswerIndex])
            question.copy(options = shuffledOptions, correctAnswerIndex = newCorrectAnswerIndex)
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TopAppBar with a back button and title
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .padding(end = 25.dp) // Offset to prevent overlap with back icon
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = topicName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .clickable {
                                navController.popBackStack() // Navigate back
                            }
                            .padding(8.dp, top = 15.dp)
                            .size(24.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF5C57) // Dynamic background color
                ),
                modifier = Modifier.height(85.dp)
            )

            if (showResult) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize() // Fills the available space
                            .offset(y = (-100).dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Lottie Animation
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.con_animation))
                        LottieAnimation(composition = composition, modifier = Modifier.size(200.dp))

                        Spacer(modifier = Modifier.height(16.dp)) // Add space between the animation and the text

                        // Score Text
                        Text(
                            text = "You scored $correctAnswers/${shuffledQuestions?.size ?: 0}!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else if (shuffledQuestions != null) {
                val currentQuestion = shuffledQuestions[currentQuestionIndex]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = Color.White,
                        shape = MaterialTheme.shapes.medium,
                        shadowElevation = 5.dp
                    ) {
                        Text(
                            text = currentQuestion.question,
                            fontSize = 17.sp,
                            color = Color(0xFFFF5C57),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Display shuffled options for each question
                    currentQuestion.options.forEachIndexed { index, option ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable(
                                    onClick = {
                                        if (selectedOptionIndex == -1) { // Allow selection only if no option is selected
                                            selectedOptionIndex = index
                                            // Compare with the correct index in the shuffled list
                                            selectedAnswerCorrect =
                                                index == currentQuestion.correctAnswerIndex
                                        }
                                    },
                                    indication = null, // This removes the ripple effect
                                    interactionSource = remember { MutableInteractionSource() }
                                )
                                .border(
                                    width = 2.dp,
                                    color = when {
                                        selectedOptionIndex == index && selectedAnswerCorrect == true -> Color.Green
                                        selectedOptionIndex == index && selectedAnswerCorrect == false -> Color.Red
                                        else -> Color.Transparent
                                    },
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .padding(0.dp), // No padding inside the Card
                            shape = RoundedCornerShape(15.dp),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Option Number
                                Text(
                                    text = "${index + 1}.",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                // Option Text
                                Text(
                                    text = option,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                    }

                    // Next or Finish Button
                    Button(
                        onClick = {
                            if (selectedAnswerCorrect == true) correctAnswers++
                            if (currentQuestionIndex < shuffledQuestions.size - 1) {
                                currentQuestionIndex++
                                selectedOptionIndex = -1
                                selectedAnswerCorrect = null
                            } else {
                                showResult = true
                            }
                        },
                        enabled = selectedOptionIndex != -1,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = if (currentQuestionIndex == shuffledQuestions.size - 1) "Finish" else "Next")
                    }
                }
            }
        }
    }
}
