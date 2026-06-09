package com.kubradas.arackiraapp.ui.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kubradas.arackiraapp.ui.components.CustomTextField

@Composable
fun OwnerDashboardScreen() {
    var brand by remember { mutableStateOf("") }
    var km by remember { mutableStateOf("") }
    var minDays by remember { mutableStateOf("") }
    var dailyPrice by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Yeni Araç İlanı Ekle",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Fotoğraf Yükleme Kutusu
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Araç Fotoğraflarını Yükleyin",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Form Alanları (Açık parametre isimleri ile 'it' hatası engellendi)
        CustomTextField(
            value = brand,
            onValueChange = { text -> brand = text },
            label = "Araç Marka / Model",
            icon = Icons.Default.Edit
        )

        CustomTextField(
            value = km,
            onValueChange = { text -> km = text },
            label = "Mevcut Kilometre (KM)",
            icon = Icons.Default.Edit
        )

        CustomTextField(
            value = minDays,
            onValueChange = { text -> minDays = text },
            label = "En Az Kaç Günlük Kiralanabilir?",
            icon = Icons.Default.Edit
        )

        CustomTextField(
            value = dailyPrice,
            onValueChange = { text -> dailyPrice = text },
            label = "Günlük Kiralama Ücreti (TL)",
            icon = Icons.Default.Edit
        )

        Spacer(modifier = Modifier.height(32.dp))

        // İlanı Yayına Al Butonu
        Button(
            onClick = { /* TODO: Veritabanı işlemleri */ },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "İlanı Yayına Al",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}