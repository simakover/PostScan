package ru.nyxsed.postscan

import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.nyxsed.postscan.data.repository.VkRepositoryImpl
import ru.nyxsed.postscan.domain.models.GroupEntity
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.domain.repository.DbRepository
import ru.nyxsed.postscan.domain.repository.VkRepository
import ru.nyxsed.postscan.presentation.PostCard
import ru.nyxsed.postscan.presentation.TopBar
import ru.nyxsed.postscan.presentation.ui.theme.PostScanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vkRepository : VkRepository by inject()
            val dbRepository : DbRepository by inject()
            var postListState = MutableStateFlow<List<PostEntity>>(listOf())
            PostScanTheme {

                val items = postListState.collectAsState(listOf())

                val postEntity = PostEntity()
                Scaffold(
                    topBar = {
                        TopBar(
                            onRefreshClicked = {
                                lifecycleScope.launch {
                                    val group = GroupEntity()
//                                    val posts = vkRepository.getPostsForGroup(group)
                                    val post = PostEntity()
                                    dbRepository.addPost(post)
                                    postListState.value = dbRepository.getAllPosts()
//                                    val posts = dbRepository.getAllPosts()
//                                    Log.d("onRefreshClicked", posts.first().id.toString())
                                }
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

                        items.value.forEach {
                            item {
                                PostCard(it)
                            }
                        }
//                        postListState.value.forEach {
//                            item{
//                                PostCard(postEntity)
//                            }
//                        }
//                        repeat(2) {
//                            item{
//                                PostCard(postEntity)
//                            }
//                        }
                    }
                }
            }
        }
    }
}