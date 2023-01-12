package br.com.zup.ezuppers.di

import br.com.zup.ezuppers.data.datasource.AuthenticationDataSource
import br.com.zup.ezuppers.data.datasource.UserDataSource
import br.com.zup.ezuppers.data.datasource.local.UserDataBase
import br.com.zup.ezuppers.data.repository.*
import br.com.zup.ezuppers.domain.usecase.*
import br.com.zup.ezuppers.ui.editregister.viewmodel.EditRegisterViewModel
import br.com.zup.ezuppers.ui.favorite.viewmodel.FavoriteViewModel
import br.com.zup.ezuppers.ui.feed.viewmodel.FeedViewModel
import br.com.zup.ezuppers.ui.login.viewmodel.LoginViewModel
import br.com.zup.ezuppers.ui.register.viewmodel.RegisterLoginViewModel
import br.com.zup.ezuppers.ui.register.viewmodel.RegisterOptionalViewModel
import br.com.zup.ezuppers.ui.userprofile.viewmodel.UserProfileViewModel
import br.com.zup.ezuppers.ui.zupperprofile.viewmodel.ZupperProfileViewModel
import br.com.zup.ezuppers.ui.zuppers.viewmodel.ZuppersViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { UserProfileViewModel(get(), get()) }
    viewModel { FavoriteViewModel(get(), get()) }
    viewModel { RegisterLoginViewModel(get(), get(), get()) }
    viewModel { FeedViewModel(get(), get(), get(), get()) }
    viewModel { RegisterOptionalViewModel(get(), get(), get()) }
    viewModel { EditRegisterViewModel(get(), get(), get()) }
    viewModel { ZupperProfileViewModel(get()) }
    viewModel { ZuppersViewModel(get(), get(), get(), get()) }
}

val useCaseModules = module {
    single { GetCepUseCase(get()) }
    single { GetUfUseCase(get()) }
    single { GetCitiesUseCase(get()) }
    single { GetZuppersUseCase(get()) }
    single { GetFavoriteUseCase(get()) }
    single { GetZuppersQuantityUseCase(get()) }
    single { UserUseCase(get()) }
}

val repositoryModules = module {
    single { CepRepository() }
    single { UfRepository() }
    single { ZuppersRepository() }
    single { PostRepository() }
    single { AuthenticationRepository(get()) }
    single { UserRepository(get())}
}

val daoModules = module {
    single { UserDataBase.getDatabase(context = androidContext()).userDao() }
}

val dataSourceModules = module{
    single { AuthenticationDataSource(get()) }
    single { UserDataSource(get(), get(), Firebase.database.reference) }
}

val firebaseModules = module {
     single { Firebase.auth }
 }
