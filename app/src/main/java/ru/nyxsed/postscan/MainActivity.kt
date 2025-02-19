package ru.nyxsed.postscan

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.rememberNavController
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import ru.nyxsed.postscan.presentation.screens.changegroupscreen.ChangeGroupScreen
import ru.nyxsed.postscan.presentation.screens.commentsscreen.CommentsScreen
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupsScreen
import ru.nyxsed.postscan.presentation.screens.imagepagerscreen.ImagePagerScreen
import ru.nyxsed.postscan.presentation.screens.loginscreen.LoginScreen
import ru.nyxsed.postscan.presentation.screens.pickgroupscreen.PickGroupScreen
import ru.nyxsed.postscan.presentation.screens.postsscreen.PostsScreen
import ru.nyxsed.postscan.presentation.screens.preferencesscreen.PreferencesScreen
import ru.nyxsed.postscan.presentation.ui.theme.PostScanTheme
import ru.nyxsed.postscan.util.DataStoreInteraction
import ru.nyxsed.postscan.util.DataStoreInteraction.Companion.NOTIFICATION_PERMISSION_REQUESTED

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val datastore: DataStoreInteraction by inject()
        runBlocking {
            val isNotificationPermissionRequested = datastore.getSettingBooleanFromDataStore(NOTIFICATION_PERMISSION_REQUESTED)

            if (!isNotificationPermissionRequested && !NotificationManagerCompat.from(this@MainActivity).areNotificationsEnabled()) {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle(getString(R.string.notification_permission))
                    .setMessage(getString(R.string.notification_permission_desc))
                    .setPositiveButton(getString(R.string.notification_permission_desc_settings)) { _, _ ->
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                        startActivity(intent)

                        runBlocking {
                            datastore.saveSettingBooleanToDataStore(NOTIFICATION_PERMISSION_REQUESTED, true)
                        }
                    }
                    .setNegativeButton(getString(R.string.notification_permission_desc_cancel)) { _, _ ->
                        runBlocking {
                            datastore.saveSettingBooleanToDataStore(NOTIFICATION_PERMISSION_REQUESTED, true)
                        }
                    }
                    .show()
            }
        }

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