package com.abasyn.mvvmarchitecture.di


import com.abasyn.mvvmarchitecture.presentation.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        SplashViewModel()
    }
}