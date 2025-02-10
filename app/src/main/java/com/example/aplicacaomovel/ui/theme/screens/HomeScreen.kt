package com.example.aplicacaomovel.ui.theme.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.aplicacaomovel.Contact
import com.example.aplicacaomovel.ContactApiClient
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.util.Log
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

@Composable
fun HomeScreen(navController: NavController) {
    val api = ContactApiClient()
    val coroutineScope = rememberCoroutineScope()
    var contacts by remember { mutableStateOf(listOf<Contact>()) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedContact by remember { mutableStateOf<Contact?>(null) }

    LaunchedEffect(Unit) {
        try {
            contacts = api.getContacts()
        } catch (e: Exception) {
            Log.e("HomeScreen", "Erro ao buscar contatos: ${e.message}")
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "GESTÃO DE CONTACTOS", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Pesquisar...") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = { showDialog = true }) {
                Text("Adicionar Contacto")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                coroutineScope.launch {
                    contacts = api.getContacts()
                }
            }) {
                Text("Visualizar Contactos")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(contacts.filter { it.name.contains(searchQuery.text, ignoreCase = true) }) { contact ->
                ContactItem(contact = contact, onDelete = {
                    coroutineScope.launch {
                        if (api.deleteContact(contact.id)) {
                            contacts = contacts.filter { it.id != contact.id }
                        }
                    }
                }, onUpdate = {
                    selectedContact = contact
                    showDialog = true
                })
            }
        }
    }
    if (showDialog) {
        AddContactDialog(onDismiss = { showDialog = false }, onSave = { newContact ->
            coroutineScope.launch {
                if (selectedContact == null) {
                    val addedContact = api.addContact(newContact)
                    if (addedContact != null) {
                        contacts = contacts + addedContact
                    } else {
                        Log.e("HomeScreen", "Erro ao adicionar contato")
                    }
                } else {
                    val updatedContact = api.updateContact(newContact.copy(id = selectedContact!!.id))
                    contacts = contacts.map { if (it.id == updatedContact.id) updatedContact else it }
                    selectedContact = null
                }
                showDialog = false
            }
        }, contact = selectedContact)
    }
}

@Composable
fun ContactItem(contact: Contact, onDelete: () -> Unit, onUpdate: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nome: ${contact.name}")
            Text(text = "Email: ${contact.email}")
            Text(text = "Telefone: ${contact.telefone}")
            Text(text = "Endereço: ${contact.endereco}")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = onUpdate) {
                    Text("Atualizar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
fun AddContactDialog(onDismiss: () -> Unit, onSave: (Contact) -> Unit, contact: Contact? = null) {
    var name by remember { mutableStateOf(contact?.name ?: "") }
    var email by remember { mutableStateOf(contact?.email ?: "") }
    var telefone by remember { mutableStateOf(contact?.telefone ?: "") }
    var endereco by remember { mutableStateOf(contact?.endereco ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (contact == null) "Adicionar Contacto" else "Atualizar Contacto") },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Nome") })
                TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                TextField(value = telefone, onValueChange = { telefone = it }, label = { Text("Telefone") })
                TextField(value = endereco, onValueChange = { endereco = it }, label = { Text("Endereço") })
            }
        },
        confirmButton = {
            Button(onClick = { onSave(Contact(contact?.id ?: 0, name, email, telefone, endereco)) }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}













