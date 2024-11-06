package ru.nyxsed.postscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.nyxsed.postscan.presentation.PostCard
import ru.nyxsed.postscan.presentation.models.PostUiModel
import ru.nyxsed.postscan.presentation.ui.theme.PostScanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostScanTheme {
                val TEST_GROUP_IMG_URL = "https://sun9-51.userapi.com/s/v1/ig2/ox1k9csdJ9v6nJqhDGtwAb-jDcRcv2RtZlTe3emRHILprQveGVaYeMUxktUKTRi-Y-AP-odX9GzFGenjXTy4SEB2.jpg?quality=95&crop=135,0,530,530&as=32x32,48x48,72x72,108x108,160x160,240x240,360x360,480x480&ava=1&cs=50x50"
                val TEST_POST_IMG_URL = "https://sun9-14.userapi.com/impg/F4ATtq58Pm5bpSPl6jlGv0EVK07vP3Z-dX8WCA/rGBB4skflLE.jpg?size=720x960&quality=96&sign=bff4cded0a5d3ed984985d28c443322d&type=album"
                val postUiModel = PostUiModel(
                    id = 1,
                    ownerId = 1,
                    ownerName = "Some Group",
                    ownerImageUrl = TEST_GROUP_IMG_URL,
                    publicationDate = "23.11.2024",
                    contentText = "some text",
                    contentImageUrl = TEST_POST_IMG_URL,
                    isLiked = false
                )
                PostCard(postUiModel)
            }
        }
    }
}