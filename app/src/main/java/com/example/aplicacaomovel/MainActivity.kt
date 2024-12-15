package com.example.aplicacaomovel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacaomovel.ui.theme.AplicacaoMovelTheme
import com.example.aplicacaomovel.ui.theme.screens.HomeScreen
import com.example.aplicacaomovel.ui.theme.screens.UserLoginScreen
import com.example.aplicacaomovel.ui.theme.screens.UserRegistrationScreen
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AplicacaoMovelTheme {
                NavigationView()
            }
        }
    }
}

@Composable
fun NavigationView() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login" ){
        composable("login"){ UserLoginScreen(navController) }
        composable("signup"){ UserRegistrationScreen(navController)}
        composable("homepage"){ HomeScreen(navController)}
    }
}

