package ru.nyxsed.postscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.rememberNavController
import ru.nyxsed.postscan.presentation.screens.changegroupscreen.ChangeGroupScreen
import ru.nyxsed.postscan.presentation.screens.commentsscreen.CommentsScreen
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupsScreen
import ru.nyxsed.postscan.presentation.screens.imagepagerscreen.ImagePagerScreen
import ru.nyxsed.postscan.presentation.screens.loginscreen.LoginScreen
import ru.nyxsed.postscan.presentation.screens.pickgroupscreen.PickGroupScreen
import ru.nyxsed.postscan.presentation.screens.postsscreen.PostsScreen
import ru.nyxsed.postscan.presentation.screens.preferencesscreen.PreferencesScreen
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
                    ChangeGroupScreen,
                    PickGroupScreen,
                    ImagePagerScreen,
                    CommentsScreen,
                    PreferencesScreen,
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