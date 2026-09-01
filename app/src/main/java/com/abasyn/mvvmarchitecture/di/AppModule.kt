package com.abasyn.mvvmarchitecture.di


import com.abasyn.mvvmarchitecture.data.remote.RecipeApiService
import com.abasyn.mvvmarchitecture.data.remote.RecipeRepositoryImpl
import com.abasyn.mvvmarchitecture.domain.RecipeRepository
import com.abasyn.mvvmarchitecture.presentation.home.HomeViewModel
import com.abasyn.mvvmarchitecture.presentation.preview.PreviewViewModel
import com.abasyn.mvvmarchitecture.presentation.splash.SplashViewModel
import com.abasyn.mvvmarchitecture.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(RecipeApiService::class.java) }

    single {
        androidx.room.Room.databaseBuilder(
            get(),
            com.abasyn.mvvmarchitecture.data.local.AppDatabase::class.java,
            "recipe_database"
        ).build()
    }

    single { get<com.abasyn.mvvmarchitecture.data.local.AppDatabase>().favoriteRecipeDao() }

    single<RecipeRepository> { RecipeRepositoryImpl(get()) }

    single<com.abasyn.mvvmarchitecture.domain.FavoriteRecipeRepository> {
        com.abasyn.mvvmarchitecture.data.local.FavoriteRecipeRepositoryImpl(get())
    }

    viewModel {
        SplashViewModel()
    }

    viewModel {
        HomeViewModel(get(), get())
    }

    viewModel {
        PreviewViewModel(get(), get())
    }

    viewModel {
        com.abasyn.mvvmarchitecture.presentation.favorite.FavoriteViewModel(get())
    }
}
