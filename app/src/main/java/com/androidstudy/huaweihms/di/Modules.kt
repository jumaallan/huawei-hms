package com.androidstudy.huaweihms.di

import android.content.Context
import androidx.room.Room
import com.androidstudy.huaweihms.BuildConfig
import com.androidstudy.huaweihms.data.HuaweiDatabase
import com.androidstudy.huaweihms.data.network.AuthInterceptor
import com.androidstudy.huaweihms.data.network.MapAPI
import com.androidstudy.huaweihms.data.settings.Settings
import com.androidstudy.huaweihms.repository.MapRepository
import com.androidstudy.huaweihms.views.viewmodel.MapViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

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
            retrofitModule,
            networkingModule,
            databaseModule,
            daoModule,
            repositoriesModule,
            viewModelsModule,
            settingsModule
        )
    )
}

val retrofitModule = module(override = true) {
    single {

        val baseUrl = "https://siteapi.cloud.huawei.com/"

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = when (BuildConfig.BUILD_TYPE) {
            "release" -> HttpLoggingInterceptor.Level.NONE
            else -> HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(AuthInterceptor())
            .build()

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}

val networkingModule = module {
    single { get<Retrofit>().create<MapAPI>() }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            HuaweiDatabase::class.java,
            "huawei_db"
        ).build()
    }
}

val daoModule = module {
    single { get<HuaweiDatabase>().mapDao() }
}

val repositoriesModule = module {
    single { MapRepository(get(), get()) }
}

val viewModelsModule = module {
    viewModel { MapViewModel(get()) }
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