package com.majesty.minifleettracker.presentation.history.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.majesty.minifleettracker.presentation.history.data.TripLog
import com.majesty.minifleettracker.presentation.history.viewmodel.TripLogViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TripLogActivity : ComponentActivity() {
    private val viewModel by viewModels<TripLogViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripLogScreen(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripLogScreen(viewModel: TripLogViewModel) {
    val tripLogs by viewModel.tripLogs.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("📍 Historical Trip Log") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            if (tripLogs.isEmpty()) {
                Text(text = "No trip logs available.", fontSize = 16.sp, color = Color.Gray)
            } else {
                LazyColumn {
                    items(tripLogs) { log ->
                        TripLogItem(log)
                    }
                }
            }
        }
    }
}

@Composable
fun TripLogItem(tripLog: TripLog) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "🕒 ${formatDate(tripLog.timestamp)}", fontWeight = FontWeight.Bold)
            Text(text = "📌 Location: ${tripLog.latitude}, ${tripLog.longitude}")
            Text(text = "🚗 Speed: ${tripLog.speed} km/h")
            Text(text = if (tripLog.engineOn) "🔧 Engine: ON" else "⛔ Engine: OFF")
            Text(text = if (tripLog.doorOpen) "🚪 Door: OPEN" else "✅ Door: CLOSED")
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

