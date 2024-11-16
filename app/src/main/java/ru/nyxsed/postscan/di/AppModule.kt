package ru.nyxsed.postscan.di


import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.presentation.PostScreenViewModel

val appModule = module {
    // repositories
    single {
        VkRepository(
            apiService = get(),
            mapper = get()
        )
    }

    single {
        DbRepository(
            dbDao = get()
        )
    }

    // viewmodel
    viewModel {
        PostScreenViewModel(
            dbRepository = get(),
            vkRepository = get()
        )
    }
}