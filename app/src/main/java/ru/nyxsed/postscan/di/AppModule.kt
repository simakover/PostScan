package ru.nyxsed.postscan.di


import com.vk.api.sdk.VKKeyValueStorage
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.presentation.PostsScreenViewModel
import ru.nyxsed.postscan.presentation.screens.addgroupscreen.AddGroupScreenViewModel
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupsScreenViewModel

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
    viewModel<PostsScreenViewModel> {
        PostsScreenViewModel(
            dbRepository = get(),
            vkRepository = get(),
            storage = get()
        )
    }

    viewModel<GroupsScreenViewModel> {
        GroupsScreenViewModel(
            dbRepository = get()
        )
    }

    viewModel<AddGroupScreenViewModel> {
        AddGroupScreenViewModel(
            dbRepository = get(),
            vkRepository = get()
        )
    }
}