package com.faris165.mobilecomponentslab.roomdb


import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RoomScreen() {
    val app = LocalContext.current.applicationContext as Application

    // ✅ gunakan AndroidViewModelFactory bawaan (tanpa DSL initializer)
    val vm: NoteViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(app)
    )

    var text by remember { mutableStateOf("") }
    val notes by vm.notes.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Tulis catatan...") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { if (text.isNotBlank()) { vm.add(text); text = "" } },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Tambah") }

        Divider()

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize()) {
            items(notes, key = { it.id }) { n ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Text("${n.id}. ${n.text}", Modifier.padding(12.dp))
                }
            }
        }
    }
}