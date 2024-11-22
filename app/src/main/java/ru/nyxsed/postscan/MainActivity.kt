package ru.nyxsed.postscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.rememberNavController
import ru.nyxsed.postscan.presentation.screens.addgroupscreen.AddGroupScreen
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupsScreen
import ru.nyxsed.postscan.presentation.screens.loginscreen.LoginScreen
import ru.nyxsed.postscan.presentation.screens.postsscreen.PostsScreen
import ru.nyxsed.postscan.presentation.ui.theme.PostScanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController(
                startDestination = PostsScreen,
                destinations = arrayOf(
                    PostsScreen,
                    LoginScreen,
                    GroupsScreen,
                    AddGroupScreen,
                )
            )
            PostScanTheme {
                Navigation(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}