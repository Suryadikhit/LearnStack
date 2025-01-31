package com.example.learnstack.components

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.learnstack.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerLayout(
    navController: NavHostController,
    isLoggedIn: Boolean,
    username: String,
    disableDrawer: Boolean,
    darkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onLogout: () -> Unit,
    content: @Composable (onDrawerToggle: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Define the onDrawerToggle function, ensuring that it's disabled if drawer is not enabled
    val onDrawerToggle: () -> Unit = {
        if (!disableDrawer) { // Only allow toggling if drawer is not disabled
            scope.launch {
                if (drawerState.isOpen) {
                    drawerState.close()
                } else {
                    drawerState.open()
                }
            }
        }
    }

    // BackHandler to close the drawer when back is pressed, disable it if disableDrawer is true
    BackHandler(enabled = drawerState.isOpen && !disableDrawer) {
        scope.launch {
            drawerState.close()
        }
    }

    // Only display the drawer if it's not disabled
    if (!disableDrawer) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                Column(
                    modifier = Modifier
                        .width(240.dp)
                        .background(if (darkTheme) Color(0xFF303030) else Color.White)
                        .fillMaxHeight()
                ) {
                    Spacer(modifier = Modifier.height(22.dp))

                    // User Info Section
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.loginlogo),
                            contentDescription = "Login",
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isLoggedIn) username else "Login",
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (darkTheme) Color.White else Color.Black,
                            modifier = Modifier.clickable(enabled = !isLoggedIn) {
                                if (!isLoggedIn) {
                                    scope.launch { drawerState.close() }
                                    navController.navigate("login")
                                }
                            }
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = Color.Gray
                    )

                    DrawerContent(
                        navController = navController,
                        drawerState = drawerState,
                        scope = scope,
                        isLoggedIn = isLoggedIn,
                        darkTheme = darkTheme,
                        onThemeToggle = onThemeToggle,
                        onLogout = onLogout
                    )
                }
            }
        ) {
            content(onDrawerToggle)
        }
    } else {
        // If drawer is disabled, just show the content without the drawer
        content(onDrawerToggle)
    }
}


@Composable
fun DrawerContent(
    navController: NavHostController,
    drawerState: DrawerState,

    scope: CoroutineScope,
    isLoggedIn: Boolean,
    darkTheme: Boolean, // Add darkTheme parameter
    onThemeToggle: () -> Unit, // Add onThemeToggle parameter
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (darkTheme) Color(0xFF303030) else Color.White) // Dynamic background
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        val menuItems = listOf(
            DrawerMenuItem(
                icon = Icons.Default.Home,
                title = "Home",
                onClick = {
                    navController.navigate("roadmapList")
                }
            ),
            DrawerMenuItem(
                icon = painterResource(id = R.drawable.bookmarked_icon), // Use painterResource for PNG
                title = "Bookmarks",
                onClick = {
                    navController.navigate("bookmark")
                }
            ),
            DrawerMenuItem(
                icon = Icons.Default.Share,
                title = "Share",
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                }
            ),
            DrawerMenuItem(
                icon = Icons.Default.Warning,
                title = "Contact Us",
                onClick = {
                    navController.navigate("help")
                }
            )
        )

        // Render menu items
        menuItems.forEach { item ->
            DrawerItem(icon = item.icon, title = item.title) {
                scope.launch { drawerState.close() }
                item.onClick()
            }
        }

        // Theme Toggle Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon: Painter = painterResource(
                id = if (darkTheme) R.drawable.moon else R.drawable.sun
            )

            // Display the selected icon
            Image(
                painter = icon,
                contentDescription = "Theme Toggle",
                modifier = Modifier.size(30.dp) // Increased size to make it bigger
            )

            Spacer(modifier = Modifier.width(13.dp))

            Text(
                text = "Dark Mode",
                style = MaterialTheme.typography.bodyLarge,
                color = if (darkTheme) Color.White else Color.Black,
                modifier = Modifier.weight(1f) // Push the switch to the right
            )

            androidx.compose.material.Switch(
                checked = darkTheme,
                onCheckedChange = { onThemeToggle() },
                colors = androidx.compose.material.SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color.Gray,
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.LightGray
                )
            )
        }

        // Logout button (visible only when logged in)
        if (isLoggedIn) {
            DrawerItem(
                icon = Icons.Default.Close,
                title = "Logout",
                onClick = {
                    scope.launch { drawerState.close() }
                    onLogout()
                }
            )
        }
    }
}


// Drawer Menu Item Data Class
data class DrawerMenuItem(
    val icon: Any,
    val title: String,
    val onClick: () -> Unit
)

@Composable
fun DrawerItem(icon: Any, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (icon) {
            is ImageVector -> Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp), // Set a fixed size for the icon
                tint = Color.Gray // Set gray color for the icon
            )

            is Painter -> Image(
                painter = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp), // Set a fixed size for the icon
                colorFilter = ColorFilter.tint(Color.Gray) // Set gray color for the image icon
            )

            else -> throw IllegalArgumentException("Unsupported icon type")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
