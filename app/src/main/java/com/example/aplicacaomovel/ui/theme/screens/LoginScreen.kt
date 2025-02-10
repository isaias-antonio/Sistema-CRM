package com.example.aplicacaomovel.ui.theme.screens


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.aplicacaomovel.database.AppDatabase
import kotlinx.coroutines.CoroutineScope

@Composable
fun UserLoginScreen(navController: NavController) {
    val context = LocalContext.current
    val database = remember {
        try {
            // inicializar o banco de dados
            AppDatabase.getDatabase(context)
        } catch (e: Exception) {
            Log.e("UserLoginScreen", "Erro ao inicializar o banco de dados: ${e.message}")
            null
        }
    }
    val userDao = remember {
        database?.userDao()
    }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    fun validateCredentials(username: String, password: String) {
        // Verificando se o userDao é nulo
        if (userDao == null) {
            Log.e("UserLoginScreen", "Erro: userDao é nulo")
            errorMessage = "Erro ao acessar o banco de dados"
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("UserLoginScreen", "Validando usuário: $username")
                val user = userDao.getUserByUsername(username)
                if (user == null) {
                    Log.d("UserLoginScreen", "Usuário não encontrado")
                    withContext(Dispatchers.Main) {
                        errorMessage = "Usuário não encontrado"
                    }
                    return@launch
                }

                val isPasswordValid = user.password == password
                Log.d("UserLoginScreen", "Senha válida: $isPasswordValid")

                withContext(Dispatchers.Main) {
                    if (isPasswordValid) {
                        errorMessage = "Credenciais válidas!"
                        try {
                            // Navegação dentro de try-catch
                            navController.navigate("homepage")
                        } catch (e: Exception) {
                            Log.e("UserLoginScreen", "Erro na navegação: ${e.message}")
                            errorMessage = "Erro ao navegar para a homepage"
                        }
                    } else {
                        errorMessage = "Usuário ou senha incorretos!"
                    }
                }
            } catch (e: Exception) {
                Log.e("UserLoginScreen", "Erro ao acessar a base de dados: ${e.message}")
                withContext(Dispatchers.Main) {
                    errorMessage = "Erro ao acessar a base de dados: ${e.message}"
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuário") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Senha") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (username.isBlank() || password.isBlank()) {
                errorMessage = "Preencha todos os campos"
            } else {
                validateCredentials(username, password)
            }
        }) {
            Text("Entrar")
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    UserLoginScreen(navController = navController)
}










































