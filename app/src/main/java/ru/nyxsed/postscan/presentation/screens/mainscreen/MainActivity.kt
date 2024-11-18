package ru.nyxsed.postscan.presentation.screens.mainscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.rememberNavController
import org.koin.android.ext.android.inject
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupsScreen
import ru.nyxsed.postscan.presentation.screens.loginscreen.LoginScreen
import ru.nyxsed.postscan.presentation.screens.postsscreen.PostScreen
import ru.nyxsed.postscan.presentation.ui.theme.PostScanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController(
                startDestination = PostScreen,
                destinations = arrayOf(
                    PostScreen,
                    LoginScreen,
                    GroupsScreen,
                )
            )
            val mainViewModel: MainViewModel by inject()
            val authState = mainViewModel.authStateFlow.collectAsState()

            PostScanTheme {
                Navigation(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )

                LaunchedEffect(authState.value) {
                    when (authState.value) {
                        is AuthState.Authorized -> {
                            navController.popToTop(PostScreen)
                        }

                        is AuthState.NotAuthorized -> {
                            navController.navigate(LoginScreen)
                        }
                    }
                }
            }
        }
    }
}