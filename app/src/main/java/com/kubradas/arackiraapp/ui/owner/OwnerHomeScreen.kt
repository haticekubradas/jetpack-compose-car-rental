package com.kubradas.arackiraapp.ui.owner


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kubradas.arackiraapp.Car

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerHomeScreen(
    allCars: SnapshotStateList<Car>, // MainActivity'den gelen canlı ortak havuz
    onLogout: () -> Unit
) {
    // Form Input Durumları
    var brandInput by remember { mutableStateOf("") }
    var modelInput by remember { mutableStateOf("") }
    var cityInput by remember { mutableStateOf("") }
    var districtInput by remember { mutableStateOf("") }
    var kmInput by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Galeriden fotoğraf seçme motoru (Activity Result API)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Owner Dashboard", fontWeight = FontWeight.Black) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Çıkış Yap", tint = Color.Red)
                    }
                }
            )
        }
    ) { paddingValues ->
        // Güvenli dikey kaydırma için tüm yapıyı LazyColumn içinde topladık (Scroll çakışmaz)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // --- 1. BÖLÜM: FİNANSAL ANALİZ KARTLARI ---
            item {
                Text(
                    text = "Finansal Özet",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Kazanç Kartı
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Aylık Kazanç", style = MaterialTheme.typography.bodyMedium)
                            Text("45.000 TL", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        }
                    }
                    // Aktif Araç Kartı
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Aktif İlanlar", style = MaterialTheme.typography.bodyMedium)
                            Text("${allCars.size} Araç", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            // --- 2. BÖLÜM: YENİ İLAN EKLEME FORMU ---
            item {
                Text(
                    text = "Yeni Araç İlanı Oluştur",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Form Alanları
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = brandInput, onValueChange = { brandInput = it },
                        label = { Text("Marka (Önji: Porsche)") }, modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = modelInput, onValueChange = { modelInput = it },
                        label = { Text("Model (Örn: 911 Carrera)") }, modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = cityInput, onValueChange = { cityInput = it },
                            label = { Text("İl") }, modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = districtInput, onValueChange = { districtInput = it },
                            label = { Text("İlçe") }, modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = kmInput, onValueChange = { kmInput = it },
                        label = { Text("Kilometre") }, modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = priceInput, onValueChange = { priceInput = it },
                        label = { Text("Günlük Fiyat (Örn: 5000 TL / Gün)") }, modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // --- 3. BÖLÜM: DİNAMİK FOTOĞRAF SEÇİCİ ---
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .border(1.dp, Color.Gray, RoundedCornerShape(14.dp))
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        // Seçilen resmi canlı önizleme olarak basar
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Araç Fotoğrafı Seçin (Zorunlu)", color = Color.Gray)
                        }
                    }
                }
            }

            // --- 4. BÖLÜM: İLANI YAYINLA BUTONU ---
            item {
                val isFormValid = brandInput.isNotEmpty() && modelInput.isNotEmpty() &&
                        cityInput.isNotEmpty() && districtInput.isNotEmpty() &&
                        kmInput.isNotEmpty() && priceInput.isNotEmpty() && selectedImageUri != null

                Button(
                    onClick = {
                        // Yeni araç customnesnesini oluşturup ortak canlı listeye ekliyoruz
                        val newCar = Car(
                            brand = brandInput,
                            model = modelInput,
                            city = cityInput,
                            district = districtInput,
                            km = "${kmInput} km",
                            price = "${priceInput} TL / Gün",
                            imageUrl = selectedImageUri.toString() // Uri'yi stringe çevirip modele gömdük
                        )

                        allCars.add(newCar)

                        // Formu temizle ve yeni ilan için hazırla
                        brandInput = ""
                        modelInput = ""
                        cityInput = ""
                        districtInput = ""
                        kmInput = ""
                        priceInput = ""
                        selectedImageUri = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = isFormValid // Tüm alanlar dolmadan buton aktif olmaz
                ) {
                    Text("İlanı Yayınla ve Garaja Ekle", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}