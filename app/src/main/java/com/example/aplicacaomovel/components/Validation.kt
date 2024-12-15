package com.example.aplicacaomovel.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth


object AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Login realizado com sucesso!")
                } else {
                    onResult(false, task.exception?.message ?: "Erro desconhecido")
                }
            }
    }
}

@Composable
fun LoginWithEmailAndPassword(
    email: String,
    password: String,
    onResult: (Boolean, String) -> Unit // Callback para tratar sucesso ou erro
) {
    val auth = FirebaseAuth.getInstance() // Obtém a instância do FirebaseAuth
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Login bem-sucedido
                onResult(true, "Login realizado com sucesso!")
            } else {
                // Erro no login
                onResult(false, task.exception?.message ?: "Erro desconhecido")
            }
        }
}

