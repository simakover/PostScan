package ru.nyxsed.postscan.di


import com.vk.api.sdk.VKKeyValueStorage
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.presentation.PostsScreenViewModel
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupsScreenViewModel
import ru.nyxsed.postscan.presentation.screens.mainscreen.MainViewModel

val appModule = module {
    // repositories
    single {
        VkRepository(
            apiService = get(),
            mapper = get(),
            storage = get()
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
    single < MainViewModel> {
        MainViewModel(
            storage = get()
        )
    }

    viewModel<PostsScreenViewModel> {
        PostsScreenViewModel(
            dbRepository = get(),
            vkRepository = get()
        )
    }

    viewModel<GroupsScreenViewModel> {
        GroupsScreenViewModel(
            dbRepository = get()
        )
    }

}