package com.example.aplicacaomovel

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Contact(val id: Int, val name: String, val email: String, val telefone: String, val endereco: String)

class ContactApiClient {

    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    private val baseUrl = "http://192.168.183.1:8080"

    suspend fun getContacts(): List<Contact> {
        return withContext(Dispatchers.IO) {
            client.get("$baseUrl/contacts").body()
        }
    }

    suspend fun addContact(contact: Contact): Contact {
        return withContext(Dispatchers.IO) {
            client.post("$baseUrl/contacts") {
                contentType(ContentType.Application.Json)
                setBody(contact)
            }.body()
        }
    }

    suspend fun updateContact(contact: Contact): Contact {
        return withContext(Dispatchers.IO) {
            client.put("$baseUrl/contacts/${contact.id}") {
                contentType(ContentType.Application.Json)
                setBody(contact)
            }.body()
        }
    }

    suspend fun deleteContact(id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val response: HttpResponse = client.delete("$baseUrl/contacts/$id")
            response.status == HttpStatusCode.NoContent
        }
    }
}
