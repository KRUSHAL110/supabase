package com.example.supabase

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DialerScreen(
    onStartCall: (String) -> Unit
) {
    var inputNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = inputNumber,
            onValueChange = { inputNumber = it },
            label = { Text("Enter SIP number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (inputNumber.isNotBlank()) {
                    onStartCall("sip:$inputNumber@192.168.7.120")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Call")
        }
    }
}
