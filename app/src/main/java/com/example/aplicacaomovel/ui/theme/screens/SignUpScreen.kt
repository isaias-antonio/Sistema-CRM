package com.example.aplicacaomovel.ui.theme.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
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
import com.example.aplicacaomovel.components.AlreadyHaveAccountRow
import com.example.aplicacaomovel.ui.theme.AlegreyaFontFamily
import com.example.aplicacaomovel.ui.theme.AlegreyaSansFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@Composable
fun UserRegistrationScreen(
    navController: NavController,
    firebaseAuthRepository: FirebaseAuthRepository = FirebaseAuthRepository()
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

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

            Text(text = "Crie uma conta",
                style = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = AlegreyaFontFamily,
                    fontWeight = FontWeight(800),
                    color = Color.White
                ),
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = name,
                onValueChange = { name = it; nameError = "" },
                label = { Text("Nome") },
                isError = nameError.isNotEmpty(),
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
            if (nameError.isNotEmpty()) {
                Text(text = nameError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = email,
                onValueChange = { email = it; emailError = "" },
                label = { Text("Email") },
                isError = emailError.isNotEmpty(),
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
            if (emailError.isNotEmpty()) {
                Text(text = emailError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it; passwordError = "" },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError.isNotEmpty(),
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
            if (passwordError.isNotEmpty()) {
                Text(text = passwordError, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    var hasError = false
                    if (name.isBlank()) {
                        nameError = "Campo nome obrigatório."
                        hasError = true
                    }
                    if (email.isBlank()) {
                        emailError = "Campo Email obrigatório."
                        hasError = true
                    }
                    if (password.isBlank()) {
                        passwordError = "Campo Senha obrigatório."
                        hasError = true
                    }
                    if (!hasError) {
                        coroutineScope.launch {
                            try {
                                firebaseAuthRepository.registerUser(email, password)
                                successMessage = "Conta registada com êxito!"
                                errorMessage = ""
                                delay(3000)
                                successMessage = "Redirecionando para Login"
                                delay(3000)
                                navController.navigate("login")
                            } catch (e: Exception) {
                                errorMessage = "Falha ao adicionar conta\nVerifique os dados e tente novamente"
                                successMessage = ""
                                delay(3000)
                                errorMessage = ""
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registar",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = AlegreyaSansFontFamily,
                        fontWeight = FontWeight(500),
                        color = Color.White
                    )
                )
            }
            AlreadyHaveAccountRow(
                onSignInTap = {
                    navController.navigate("login")
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

suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T {
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
fun SignUpScreenPreview() {
    UserRegistrationScreen(rememberNavController())
}






