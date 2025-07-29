package com.example.supabase

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Boolean,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign Up", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            authViewModel.signUp(email, password) { success, error ->
                if (success) {
                    Toast.makeText(context, "Signup successful", Toast.LENGTH_SHORT).show()
                    onNavigateToLogin()
                } else {
                    Toast.makeText(context, "Signup failed: $error", Toast.LENGTH_LONG).show()
                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Sign Up")
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = { onNavigateToLogin() }) {
            Text("Already have an account? Login")
        }
    }
}