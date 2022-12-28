package br.com.zup.ezuppers

import android.app.Application
import br.com.zup.ezuppers.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidContext(this@MyApp)

            modules(useCaseModules)
            modules(repositoryModules)
            modules(viewModelModules)
            modules(daoModules)
            modules(dataSourceModules)
            modules(firebaseModules)
        }
    }
}

//registerViewModelModules,
//favoriteViewModelModules,
//feedViewModelModules,
//editRegisterViewModelModules,
//RegisterViewModelModules,
//registerOptionalViewModelModules,
//userProfileViewModelModule,
//zupperProfileViewModelModules,
//zupperViewModelModules,
//---------------------

//getUfUseCaseModules,
//getCitiesUseCaseModules,
//getZuppersUseCaseModules,
//getZuppersQuantityUseCaseModules,
//userUseCaseModules,
//getFavoriteUseCaseModules
//----------------------------

//ufRepositoryModules,
//postRepositorymodule,
//zupperRepositorymodule,
//authRepositoryModules,