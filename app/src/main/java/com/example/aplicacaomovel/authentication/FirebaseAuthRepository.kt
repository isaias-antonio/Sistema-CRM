package com.example.aplicacaomovel.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthRepository(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

    suspend fun registerUser(email: String, password: String): AuthResult {
        return suspendCoroutine { cont ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        cont.resume(task.result)
                    } else {
                        cont.resumeWithException(task.exception ?: Exception("Registration failed"))
                    }
                }
        }
    }

    suspend fun loginUser(email: String, password: String): AuthResult {
        return suspendCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        cont.resume(task.result)
                    } else {
                        cont.resumeWithException(task.exception ?: Exception("Login failed"))
                    }
                }
        }
    }
}


