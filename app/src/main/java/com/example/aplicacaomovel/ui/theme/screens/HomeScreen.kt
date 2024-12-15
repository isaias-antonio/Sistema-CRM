package com.example.aplicacaomovel.ui.theme.screens


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter

data class Item(val uri: String, val description: String)

@Composable
fun HomeScreen(navController: NavController) {
    var items by remember {
        mutableStateOf(
            listOf(
                Item("https://picsum.photos/200/300", "Paisagem."),
                Item("https://picsum.photos/200", "Paisagem.")
            )
        )
    }
    var newItemDescription by remember { mutableStateOf(TextFieldValue()) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isAddingNewItem by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Item?>(null) }
    var itemToEditIndex by remember { mutableStateOf<Int?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    Spacer(modifier = Modifier.height(16.dp))

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start

    ){
        Button(onClick = { navController.navigate("login") }) {
            Text("Sair")
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        if (isAddingNewItem) {
            TextField(
                value = newItemDescription,
                onValueChange = { newItemDescription = it },
                label = { Text("Descrição da Imagem") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text("Selecionar Imagem")
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (isAddingNewItem) {
                    if (selectedImageUri != null && newItemDescription.text.isNotBlank()) {
                        if (itemToEditIndex == null) {
                            items = items + Item(uri = selectedImageUri.toString(), description = newItemDescription.text)
                        } else {
                            items = items.mapIndexed { index, item ->
                                if (index == itemToEditIndex) item.copy(description = newItemDescription.text, uri = selectedImageUri.toString()) else item
                            }
                            itemToEditIndex = null
                        }
                        selectedImageUri = null
                        newItemDescription = TextFieldValue()
                        isAddingNewItem = false
                    }
                } else {
                    isAddingNewItem = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isAddingNewItem) "Guardar" else "Adicionar Item")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items.size) { index ->
                val item = items[index]
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            selectedItem = item
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(item.uri),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = item.description,
                            fontSize = 16.sp,
                            color = Color.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = {
                                items = items.filterIndexed { i, _ -> i != index }
                            }) {
                                Text("Eliminar")
                            }
                            Button(onClick = {
                                newItemDescription = TextFieldValue(item.description)
                                selectedImageUri = Uri.parse(item.uri)
                                itemToEditIndex = index
                                isAddingNewItem = true
                            }) {
                                Text("Editar")
                            }
                        }
                    }
                }
            }
        }

        selectedItem?.let { item ->
            ShowDetailedView(item = item) {
                selectedItem = null
            }
        }
    }
}

@Composable
fun ShowDetailedView(item: Item, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Column {
                Image(
                    painter = rememberAsyncImagePainter(item.uri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = item.description, fontSize = 16.sp)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}



@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}







