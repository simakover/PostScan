package ru.nyxsed.postscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.presentation.MainViewModel
import ru.nyxsed.postscan.presentation.PostCard
import ru.nyxsed.postscan.presentation.TopBar
import ru.nyxsed.postscan.presentation.ui.theme.PostScanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm = koinViewModel<MainViewModel>()
            val postListState = vm.posts.collectAsState()

            PostScanTheme {
                val postEntity = PostEntity()
                Scaffold(
                    topBar = {
                        TopBar(
                            onRefreshClicked = {
                                vm.addPost(postEntity)
                            }
                        )
                    }
                ) { paddings ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(paddings),
                        contentPadding = PaddingValues(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        postListState.value.forEach {
                            item {
                                PostCard(it)
                            }
                        }
                    }
                }
            }
        }
    }
}