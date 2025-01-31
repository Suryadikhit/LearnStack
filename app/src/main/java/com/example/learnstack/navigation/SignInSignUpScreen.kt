package com.example.learnstack.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.learnstack.R
import com.example.learnstack.utils.SharedPreferenceHelper
import com.example.learnstack.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

@Composable
fun SignInSignUpScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    var isSignUp by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF283048), Color(0xFF859398))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            isSignUp = isSignUp,
            onToggle = { isSignUp = it },
            navController = navController,
            viewModel = viewModel // Pass the ViewModel
        )
    }
}


@Composable
fun AnimatedContent(
    isSignUp: Boolean,
    onToggle: (Boolean) -> Unit,
    navController: NavController,
    viewModel: MainViewModel
) {
    val transition = updateTransition(targetState = isSignUp, label = "formTransition")

    // Define animation for horizontal translation
    val translationX by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000, easing = FastOutSlowInEasing) },
        label = "translationXAnimation"
    ) { state -> if (state) 0f else 600f }

    // Define animation for tilt effect (rotation)
    val rotation by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000, easing = FastOutSlowInEasing) },
        label = "rotationAnimation"
    ) { state -> if (state) 0f else 15f } // Tilt effect when the card is behind

    // Wrapper for forms
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Behind card (Sign In when Sign Up is active, and vice versa)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .graphicsLayer(
                    translationX = if (isSignUp) -translationX else 0f,
                    rotationZ = if (isSignUp) rotation else 10f // Tilt applied to behind card
                ),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            if (isSignUp) {
                SignInForm(
                    onToggle = onToggle, navController = navController, viewModel = viewModel
                )

            } else {
                SignUpForm(navController = navController, viewModel = viewModel)
            }
        }

        // Front card (Sign Up when Sign In is active, and vice versa)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .graphicsLayer(
                    translationX = if (isSignUp) translationX else 0f,
                    rotationZ = 0f // Front card stays normal without tilt
                ),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            if (isSignUp) {
                SignUpForm(navController = navController, viewModel = viewModel)
            } else {
                SignInForm(
                    onToggle = onToggle,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}


@Composable
fun SignUpForm(navController: NavController, viewModel: MainViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp) // Global padding for all items
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .align(Alignment.TopCenter) // Align the form content to the top
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.Black),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    Log.d("SignUpForm", "Username changed: $username")
                },
                label = { Text("Username") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Username Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                shape = MaterialTheme.shapes.medium,
                textStyle = TextStyle(color = Color.Black)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                shape = MaterialTheme.shapes.medium,
                textStyle = TextStyle(color = Color.Black)
            )

            Spacer(modifier = Modifier.height(10.dp))

            var passwordVisible by rememberSaveable { mutableStateOf(false) }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) painterResource(R.drawable.visible) else painterResource(
                            R.drawable.hide
                        )
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = image,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                shape = MaterialTheme.shapes.medium,
                textStyle = TextStyle(color = Color.Black)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank() && username.isNotBlank()) {
                        isLoading = true
                        Log.d(
                            "SignUpForm",
                            "Sign-up attempt for username: $username, email: $email"
                        )

                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            errorMessage = "Please enter a valid email address"
                            isLoading = false
                            return@Button
                        }

                        val auth = FirebaseAuth.getInstance()
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    user?.updateProfile(
                                        UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .build()
                                    )?.addOnCompleteListener { profileTask ->
                                        if (profileTask.isSuccessful) {

                                            val sharedPrefHelper =
                                                SharedPreferenceHelper.getInstance(context)
                                            sharedPrefHelper.storeUsername(username)

                                            // Update the ViewModel for login status
                                            viewModel.onLoginSuccess(username)

                                            Log.d(
                                                "SignUpForm",
                                                "Profile updated successfully with username: $username"
                                            )
                                            Toast.makeText(
                                                context,
                                                "Welcome, $username!",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            // Clear input fields
                                            email = ""
                                            password = ""
                                            username = ""

                                            navController.navigate("roadmapList") {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = true
                                                }
                                            }

                                        } else {
                                            Log.e(
                                                "SignUpForm",
                                                "Failed to update profile: ${profileTask.exception?.localizedMessage}"
                                            )
                                            errorMessage =
                                                "Failed to update profile: ${profileTask.exception?.localizedMessage}"
                                        }
                                    }
                                } else {
                                    Log.e(
                                        "SignUpForm",
                                        "Sign-up failed: ${task.exception?.localizedMessage}"
                                    )
                                    errorMessage =
                                        task.exception?.localizedMessage ?: "Sign Up Failed"
                                }
                            }
                    } else {
                        errorMessage = "Please fill in all fields"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF283048), Color(0xFF859398))
                        )
                    )
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Sign Up", color = Color.White, textAlign = TextAlign.Center)
                }
            }
        }

        // Error message displayed at the bottom center
        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.BottomCenter), // Align it to bottom center

                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
fun SignInForm(
    onToggle: (Boolean) -> Unit,
    viewModel: MainViewModel,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp) // Global padding for all items
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            Text(
                text = "Sign In",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = "" // Clear error message on input change
                },
                label = { Text("Email") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Email Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = MaterialTheme.shapes.medium,
                textStyle = TextStyle(color = Color.Black)
            )

            Spacer(modifier = Modifier.height(15.dp))

            var passwordVisible by rememberSaveable { mutableStateOf(false) }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) painterResource(R.drawable.visible) else painterResource(
                            R.drawable.hide
                        )
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = image,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = MaterialTheme.shapes.medium,
                textStyle = TextStyle(color = Color.Black)
            )

            Spacer(modifier = Modifier.height(15.dp))


            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        isLoading = true

                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            errorMessage = "Please enter a valid email address"
                            isLoading = false
                            return@Button
                        }

                        val auth = FirebaseAuth.getInstance()
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    val userName = user?.displayName ?: email.substringBefore("@")

                                    val sharedPrefHelper =
                                        SharedPreferenceHelper.getInstance(context)
                                    sharedPrefHelper.storeUsername(userName)
                                    viewModel.onLoginSuccess(userName)

                                    Toast.makeText(
                                        context,
                                        "Welcome, $userName!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.navigate("roadmapList") {
                                        popUpTo(navController.graph.startDestinationId) {
                                            inclusive = true
                                        }
                                    }

                                    // Clear input fields
                                    email = ""
                                    password = ""
                                } else {
                                    errorMessage =
                                        task.exception?.localizedMessage ?: "Login Failed"
                                }
                            }
                    } else {
                        errorMessage = "Please fill in all fields"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF283048), Color(0xFF859398))
                        )
                    )
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Sign In", color = Color.White, textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { onToggle(true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Don't have an account? Sign Up",
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Error message displayed at the bottom center
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Align it to bottom center inside the Box
                    .padding(bottom = 20.dp), // Add padding from the bottom
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
