package com.faris165.mobilecomponentslab.datastore


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun DataStoreScreen() {
    val ctx = LocalContext.current
    val repo = remember { UserPrefsRepo(ctx) }
    val scope = rememberCoroutineScope()

    val pair by repo.data.collectAsState(initial = "" to "")

    var name by remember { mutableStateOf(pair.first) }
    var note by remember { mutableStateOf(pair.second) }

    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Catatan") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { scope.launch { repo.save(name, note) } }, modifier = Modifier.fillMaxWidth()) { Text("Simpan") }
        Text("Tersimpan: ${pair.first}: ${pair.second}")
    }
}