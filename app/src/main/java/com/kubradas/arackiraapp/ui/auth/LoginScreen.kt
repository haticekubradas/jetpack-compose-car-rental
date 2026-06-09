package com.kubradas.arackiraapp.ui.auth



// [Temel Importlar] - CustomTextField.kt dosyasındaki importları kopyala
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kubradas.arackiraapp.ui.components.CustomTextField // Yazdığımız componenti import ettik
// 🌟 DOSYANIN EN ÜSTÜNE, MEVCUT İMPORTLARININ OLDUĞU YERE BUNLARI DA EKLE:
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (String) -> Unit
) {
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    // Klavyedeki Tab ve Enter geçişlerini yöneten odak kumandası
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Üst Logo/İkon Alanı
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = "Logo",
                    modifier = Modifier.size(44.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "Premium Araç Kiralama",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Devam etmek için hesabınıza giriş yapın",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // --- E-POSTA ALANI (Tab Tuşu Entegrasyonlu) ---
            OutlinedTextField(
                value = emailInput,
                onValueChange = { emailInput = it },
                label = { Text("E-posta Adresi") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next // Klavyede "İleri/Tab" ikonunu gösterir
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        // Tab'a veya İleri'ye basınca odağı aşağıdaki şifre alanına kaydırır
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            // --- ŞİFRE ALANI (Enter Tuşu Entegrasyonlu) ---
            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text("Şifre") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done // Klavyede "Onay/Enter" ikonunu gösterir
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // Klavyeyi gizler
                        focusManager.clearFocus()
                        // Enter'a basıldığında, alanlar boş değilse direkt giriş yapar
                        if (emailInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                            onLoginSuccess(emailInput)
                        }
                    }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- GİRİŞ YAP BUTONU ---
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onLoginSuccess(emailInput)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = emailInput.isNotEmpty() && passwordInput.isNotEmpty()
            ) {
                Text(
                    text = "Giriş Yap",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // --- KAYIT OL YÖNLENDİRMESİ ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Hesabınız yok mu?", color = Color.Gray)
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        text = "Hemen Kaydolun",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}