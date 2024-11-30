package ru.nyxsed.postscan.di


import com.vk.api.sdk.VKKeyValueStorage
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.nyxsed.postscan.SharedViewModel
import ru.nyxsed.postscan.data.models.entity.PostEntity
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.presentation.screens.addgroupscreen.AddGroupScreenViewModel
import ru.nyxsed.postscan.presentation.screens.commentsscreen.CommentsScreenViewModel
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupsScreenViewModel
import ru.nyxsed.postscan.presentation.screens.imagepagerscreen.ImagePagerViewModel
import ru.nyxsed.postscan.presentation.screens.postsscreen.PostsScreenViewModel
import ru.nyxsed.postscan.presentation.screens.preferencesscreen.PreferencesScreenViewModel

val appModule = module {
    // repositories
    single {
        VkRepository(
            apiService = get(),
            mapper = get(),
            storage = get(),
            dataStore = get()
        )
    }

    single {
        DbRepository(
            dbDao = get()
        )
    }

    single<VKKeyValueStorage> {
        VKPreferencesKeyValueStorage(
            context = get()
        )
    }

    // viewmodels
    single<SharedViewModel> {
        SharedViewModel(
            storage = get()
        )
    }

    viewModel<PostsScreenViewModel> {
        PostsScreenViewModel(
            dbRepository = get(),
            vkRepository = get(),
            dataStore = get()
        )
    }

    viewModel<GroupsScreenViewModel> {
        GroupsScreenViewModel(
            dbRepository = get(),
            vkRepository = get(),
        )
    }

    viewModel<AddGroupScreenViewModel> {
        AddGroupScreenViewModel(
            dbRepository = get(),
            vkRepository = get()
        )
    }

    viewModel<ImagePagerViewModel> {
        ImagePagerViewModel(
            vkRepository = get()
        )
    }

    viewModel<CommentsScreenViewModel> { (post: PostEntity) ->
        CommentsScreenViewModel(
            vkRepository = get(),
            post = post,
            dataStore = get()
        )
    }

    viewModel<PreferencesScreenViewModel> {
        PreferencesScreenViewModel(
            dataStore = get()
        )
    }
}