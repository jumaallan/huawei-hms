package com.androidstudy.huaweihms.di

import android.content.Context
import androidx.room.Room
import com.androidstudy.huaweihms.data.Database
import com.androidstudy.huaweihms.data.settings.Settings
import com.androidstudy.huaweihms.repository.UserRepository
import com.androidstudy.huaweihms.views.viewmodel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * This is responsible for providing the following across the app
 *      - Room DB and DAO's
 *      - Settings
 *      - Repositories
 *      - ViewModels
 * */

fun injectFeature() = loadFeature

private val loadFeature by lazy {

    loadKoinModules(
        listOf(
            databaseModule,
            daoModule,
            repositoriesModule,
            viewModelsModule,
            settingsModule
        )
    )
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            Database::class.java,
            "huawei_db"
        ).build()
    }
}

val daoModule = module {
    single { get<Database>().userDao() }
}

val repositoriesModule = module {
    single { UserRepository(get()) }
}

val viewModelsModule = module {
    viewModel { UserViewModel(get()) }
}

val settingsModule = module {
    single {
        androidContext().getSharedPreferences(
            "huawei_settings",
            Context.MODE_PRIVATE
        )
    }
    single {
        Settings(get())
    }
}