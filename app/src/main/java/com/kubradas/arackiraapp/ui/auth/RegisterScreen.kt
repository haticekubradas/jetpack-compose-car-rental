package com.kubradas.arackiraapp.ui.auth

// [Temel Importlar]
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.kubradas.arackiraapp.ui.components.CustomTextField

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit // Giriş sayfasına geri dönmek için
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Müşteri") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Aramıza Katılın", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(value = fullName, onValueChange = { fullName = it }, label = "Ad Soyad", icon = Icons.Default.Person)
        CustomTextField(value = email, onValueChange = { email = it }, label = "E-posta", icon = Icons.Default.Email)
        CustomTextField(value = password, onValueChange = { password = it }, label = "Şifre", icon = Icons.Default.Lock, isPassword = true)

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Hesap Türü Seçin:", style = MaterialTheme.typography.titleMedium)

        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { selectedRole = "Müşteri" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedRole == "Müşteri") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                )
            ) { Text("Müşteri") }

            OutlinedButton(
                onClick = { selectedRole = "Araç Sahibi" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedRole == "Araç Sahibi") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                )
            ) { Text("Araç Sahibi") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Kullanıcı başarıyla kayıt oldu simulasyonu yapıp giriş ekranına geri gönderiyoruz
                onNavigateToLogin()
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Hesap Oluştur")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Giriş sayfasına geri dönme linki
        Row {
            Text(text = "Zaten hesabın var mı? ")
            Text(
                text = "Giriş Yap",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }
    }
}