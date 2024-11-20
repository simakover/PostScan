package ru.nyxsed.postscan.presentation.screens.loginscreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

val LoginScreen by navDestination<Unit> {

    val sharedViewModel = koinViewModel<SharedViewModel>()

    val navController = navController()
    val launcher = rememberLauncherForActivityResult(
        contract = VK.getVKAuthActivityResultContract()
    ) {
        sharedViewModel.checkState()
        navController.back()
    }

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
                launcher.launch(listOf(VKScope.WALL, VKScope.FRIENDS))
            }
        ) {
            Text(text = stringResource(R.string.LogIn))
        }
    }
}