package ru.nyxsed.postscan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.domain.repository.DbRepository

class MainViewModel(
    private val dbRepository: DbRepository,
) : ViewModel() {
    val posts = dbRepository.getAllPosts()

    fun addPost(postEntity: PostEntity) {
        viewModelScope.launch {
            dbRepository.addPost(postEntity)
        }
    }
}