package com.faris165.mobilecomponentslab

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.faris165.mobilecomponentslab.alarm.AlarmScreen
import com.faris165.mobilecomponentslab.datastore.DataStoreScreen
import com.faris165.mobilecomponentslab.list.ListScreen
import com.faris165.mobilecomponentslab.roomdb.RoomScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val reqPostNotif = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _ -> }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            reqPostNotif.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            MaterialTheme { AppNav() }
        }
    }
}

object Routes {
    const val Home = "home"
    const val Prefs = "prefs"
    const val List = "list"
    const val Room = "room"
    const val Alarm = "alarm"
}

@OptIn(ExperimentalMaterial3Api::class) // ✅ opt-in untuk TopAppBar
@Composable
fun AppNav() {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val current = backStack?.destination?.route ?: Routes.Home

    Scaffold(topBar = { TopAppBar(title = { Text("Mobile Components Lab 2025") }) }) { padding ->
        NavHost(navController = nav, startDestination = Routes.Home, modifier = Modifier.padding(padding)) {
            composable(Routes.Home) {
                HomeMenu(
                    onOpenPrefs = { nav.navigate(Routes.Prefs) },
                    onOpenList  = { nav.navigate(Routes.List) },
                    onOpenRoom  = { nav.navigate(Routes.Room) },
                    onOpenAlarm = { nav.navigate(Routes.Alarm) }
                )
            }
            composable(Routes.Prefs) { DataStoreScreen() }
            composable(Routes.List)  { ListScreen() }
            composable(Routes.Room)  { RoomScreen() }
            composable(Routes.Alarm) { AlarmScreen() }
        }
    }
}

@Composable
fun HomeMenu(
    onOpenPrefs: () -> Unit,
    onOpenList: () -> Unit,
    onOpenRoom: () -> Unit,
    onOpenAlarm: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = onOpenPrefs, modifier = Modifier.fillMaxWidth()) { Text("1 DataStore (Prefs)") }
        Button(onClick = onOpenList,  modifier = Modifier.fillMaxWidth()) { Text("2 LazyColumn List") }
        Button(onClick = onOpenRoom,  modifier = Modifier.fillMaxWidth()) { Text("3 Room Database") }
        Button(onClick = onOpenAlarm, modifier = Modifier.fillMaxWidth()) { Text("4 Alarm & Notification") }
    }
}