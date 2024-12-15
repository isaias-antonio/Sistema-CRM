package com.example.aplicacaomovel.di

//import androidx.room.Room
//import com.example.aplicacaomovel.database.MinhasTarefasDatabase
//import com.example.aplicacaomovel.repositories.TasksRepository
//import com.example.aplicacaomovel.repositories.UsersRepository
//import com.example.aplicacaomovel.ui.viewmodels.SignInViewModel
//import com.example.aplicacaomovel.ui.viewmodels.SignUpViewModel
//import com.example.aplicacaomovel.ui.viewmodels.TaskFormViewModel
//import com.example.aplicacaomovel.ui.viewmodels.TasksListViewModel
//import com.example.aplicacaomovel.authentication.FirebaseAuthRepository
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//import org.koin.android.ext.koin.androidContext
//import org.koin.androidx.viewmodel.dsl.viewModelOf
//import org.koin.core.module.dsl.singleOf
//import org.koin.dsl.module

//val appModule = module {
//    viewModelOf(::TaskFormViewModel)
//    viewModelOf(::TasksListViewModel)
//    viewModelOf(::SignInViewModel)
//    viewModelOf(::SignUpViewModel)
//}
//
//val storageModule = module {
//    singleOf(::TasksRepository)
//    singleOf(::UsersRepository)
//    singleOf(::FirebaseAuthRepository)
//    single {
//        Room.databaseBuilder(
//            androidContext(),
//            MinhasTarefasDatabase::class.java, "minhas-tarefas.db"
//        ).build()
//    }
//    single {
//        get<MinhasTarefasDatabase>().taskDao()
//    }
//}
//
//val firebaseModule = module {
//    single {
//        Firebase.auth
//    }
//}