package com.example.aplicacaomovel


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.*
import com.example.aplicacaomovel.ui.theme.screens.HomeScreen
import com.example.aplicacaomovel.ui.theme.screens.UserLoginScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                NavigationView()
            }
        }
    }
}

@Composable
fun NavigationView() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            UserLoginScreen(navController = navController)
        }
        composable("homepage") {
            HomeScreen(navController)
        }
    }
}

