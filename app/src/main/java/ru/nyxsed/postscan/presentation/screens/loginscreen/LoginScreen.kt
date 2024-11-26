package ru.nyxsed.postscan.presentation.screens.loginscreen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.SharedViewModel
import ru.nyxsed.postscan.presentation.ui.theme.VkBlue
import ru.nyxsed.postscan.util.Constants.isInternetAvailable

val LoginScreen by navDestination<Unit> {

    val context = LocalContext.current
    val sharedViewModel = koinViewModel<SharedViewModel>()

    val navController = navController()
    val launcher = rememberLauncherForActivityResult(
        contract = VK.getVKAuthActivityResultContract()
    ) {
        sharedViewModel.checkState()
        navController.back()
    }

    Scaffold { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 200.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                modifier = Modifier
                    .size(150.dp),
                painter = painterResource(id = R.drawable.vk_logo),
                contentDescription = null
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = VkBlue,
                    contentColor = Color.White
                ),
                onClick = {
                    if (!isInternetAvailable(context)) {
                        Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }
                    launcher.launch(listOf(VKScope.WALL, VKScope.FRIENDS))
                }
            ) {
                Text(text = stringResource(R.string.LogIn))
            }
        }
    }
}