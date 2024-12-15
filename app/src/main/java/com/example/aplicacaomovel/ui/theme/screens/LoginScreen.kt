package com.example.aplicacaomovel.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.aplicacaomovel.authentication.FirebaseAuthRepository
import com.example.aplicacaomovel.components.DontHaveAccountRow
import com.example.aplicacaomovel.ui.theme.AlegreyaFontFamily
import com.example.aplicacaomovel.ui.theme.AlegreyaSansFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@Composable
fun UserLoginScreen(
    navController: NavController,
    firebaseAuthRepository: FirebaseAuthRepository = FirebaseAuthRepository()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Surface(
        color = Color.DarkGray,
        modifier = Modifier.fillMaxSize(),
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(150.dp))

            Text(text = "Login",
                style = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = AlegreyaFontFamily,
                    fontWeight = FontWeight(800),
                    color = Color.White
                ),
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Todos os campos são obrigatórios"
                        successMessage = ""
                        coroutineScope.launch {
                            delay(3000)
                            errorMessage = ""
                        }
                    } else {
                        coroutineScope.launch {
                            try {
                                firebaseAuthRepository.loginUser(email, password)
                                successMessage = "Login efetuado com êxito!"
                                errorMessage = ""
                                delay(3000)
                                navController.navigate("homepage")
                                successMessage = ""
                            } catch (e: Exception) {
                                errorMessage = "Email ou Senha errada!"
                                successMessage = ""
                                delay(3000)
                                email = ""
                                password = ""
                                errorMessage = ""
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = AlegreyaSansFontFamily,
                        fontWeight = FontWeight(500),
                        color = Color.White
                    )
                )
            }
            DontHaveAccountRow(
                onSignUpTap = {
                    navController.navigate("signup")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotBlank()) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }

            if (successMessage.isNotBlank()) {
                Text(text = successMessage, color = MaterialTheme.colorScheme.primary)
            }
        }
    }

}

suspend fun <T> com.google.android.gms.tasks.Task<T>.toSuspendResult(): T {
    return suspendCoroutine { cont ->
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                cont.resume(task.result)
            } else {
                cont.resumeWithException(task.exception ?: Exception("Unknown error occurred"))
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun LoginScreenPreview() {
    UserLoginScreen(rememberNavController())
}






