package ru.nyxsed.postscan.util

import com.composegears.tiamat.NavDestination
import ru.nyxsed.postscan.data.models.entity.PostEntity

sealed class UiEvent {
    class ShowToast(val messageResId: Int) : UiEvent()
    class LaunchActivity() : UiEvent()
    class OpenUrl(val url: String) : UiEvent()
    class Navigate(val destination: NavDestination<Unit>) : UiEvent()
    class NavigateToPost(val destination: NavDestination<PostEntity>, val navArgs: PostEntity? = null) : UiEvent()
    class Scroll() : UiEvent()
}