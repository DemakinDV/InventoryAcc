package com.example.inventoryacc.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val usernameState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Авторизация",
            modifier = Modifier.padding(bottom = 32.dp)
        )
        TextField(
            value = usernameState.value,
            onValueChange = { usernameState.value = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                if (usernameState.value == "admin" && passwordState.value == "12345") {
                    onLoginSuccess()
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Войти")
        }
    }
}
