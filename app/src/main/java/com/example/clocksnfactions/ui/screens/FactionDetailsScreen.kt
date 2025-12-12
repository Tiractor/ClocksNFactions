package com.example.clocksnfactions.ui.screens

import android.app.Application
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clocksnfactions.ui.components.AddClockDialog
import com.example.clocksnfactions.ui.components.ClockItem
import com.example.clocksnfactions.ui.viewmodel.ClockViewModel
import com.example.clocksnfactions.ui.viewmodel.ClockViewModelFactory

@Composable
fun FactionDetailsScreen(
    factionId: Long,
    factionName: String,
    onBack: () -> Unit
) {
    val application = LocalContext.current.applicationContext as Application
    val factory = ClockViewModelFactory(application, factionId)
    val vm: ClockViewModel = viewModel(
        key = "ClockVM-$factionId",
        factory = factory
    )

    val clocks by vm.clocks.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(factionName) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) { Text("+") }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (clocks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("У этой фракции ещё нет счётчиков. Нажмите +")
                }
            } else {
                LaunchedEffect(clocks) {
                    android.util.Log.d("DEBUG_CLOCKS", "clocks ids = ${clocks.map { it.id }}")
                }
                LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    items(clocks, key = { it.id }) { c ->
                        ClockItem(clock = c,
                            onInc = { vm.incrementById(c.id) },
                            onDec = { vm.decrementById(c.id) },
                            onDelete = { vm.delete(clock = c) },
                            onUpdate = { updatedClock ->
                                vm.updateClock(updatedClock)
                            }

                        )
                    }
                }
            }

            if (showAdd) {
                AddClockDialog(
                    onDismiss = { showAdd = false },
                    onCreate = { name, segments ->
                        vm.createClock(name = name, segments = segments)
                        showAdd = false
                    }
                )
            }
        }
    }
}