package com.kubradas.arackiraapp.ui.customer

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.kubradas.arackiraapp.Car

// --- DATA MODELLERİ ---
data class CarItem(val brand: String, val model: String, val price: String, val imageUrl: String, val km: String, val city: String, val district: String)
data class StoryItem(val title: String, val imageUrl: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen(allCars: List<Car>, // 🌟 MainActivity'den gelen listeyi buraya parametre olarak ekledik!
                       onLogout: () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedBottomTab by remember { mutableIntStateOf(0) }
    var isSearchPageOpen by remember { mutableStateOf(false) }

    // Seçilen Filtre Değerleri
    var selectedCity by remember { mutableStateOf("") }
    var selectedDistrict by remember { mutableStateOf("") }
    var selectedModel by remember { mutableStateOf("") }
    var selectedDates by remember { mutableStateOf("") }



    val filteredCars = allCars.filter { car ->
        (selectedCity.isEmpty() || car.city.equals(selectedCity, ignoreCase = true)) &&
                (selectedDistrict.isEmpty() || car.district.equals(selectedDistrict, ignoreCase = true)) &&
                (selectedModel.isEmpty() || car.brand.contains(selectedModel, ignoreCase = true) || car.model.contains(selectedModel, ignoreCase = true))
    }

    if (isSearchPageOpen) {
        AdvancedSearchScreen(
            onClose = { isSearchPageOpen = false },
            onApplyFilters = { city, district, model, dates ->
                selectedCity = city
                selectedDistrict = district
                selectedModel = model
                selectedDates = dates
                isSearchPageOpen = false
            }
        )
    } else {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                    Box(modifier = Modifier.fillMaxWidth().height(160.dp).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.CenterStart) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "Hatice Kübra DAŞ",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Premium Üye",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                            NavigationDrawerItem(label = { Text("Hesabım") }, selected = false, onClick = { /* TODO */ }, icon = { Icon(Icons.Default.Person, null) })
                            NavigationDrawerItem(label = { Text("Geçmiş Kiralamalar") }, selected = false, onClick = { /* TODO */ }, icon = { Icon(Icons.Default.List, null) })
                            NavigationDrawerItem(label = { Text("Ayarlar") }, selected = false, onClick = { /* TODO */ }, icon = { Icon(Icons.Default.Settings, null) })

                            Spacer(modifier = Modifier.weight(1f)) // Çıkış yap butonunu en alta iter
                            NavigationDrawerItem(
                                label = { Text("Çıkış Yap", color = Color.Red, fontWeight = FontWeight.Bold) },
                                selected = false,
                                icon = { Icon(Icons.Default.Logout, null, tint = Color.Red) },
                                onClick = { onLogout() } // MainActivity'e geri atar
                            )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("AraçKira", fontWeight = FontWeight.Black) },
                        actions = {
                            IconButton(onClick = { isSearchPageOpen = true }) { Icon(Icons.Default.Search, "Arama") }
                            IconButton(onClick = { scope.launch { drawerState.open() } }) { Icon(Icons.Default.Menu, "Menü") }
                        }
                    )
                },
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(selected = selectedBottomTab == 0, onClick = { selectedBottomTab = 0 }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Keşfet") })
                        NavigationBarItem(selected = selectedBottomTab == 1, onClick = { selectedBottomTab = 1 }, icon = { Icon(Icons.Default.DateRange, null) }, label = { Text("Rezervasyonlar") })
                        NavigationBarItem(selected = selectedBottomTab == 2, onClick = { selectedBottomTab = 2 }, icon = { Icon(Icons.Default.Favorite, null) }, label = { Text("Favoriler") })
                    }
                }
            ) { paddingValues ->
                // ÇÖZÜM: Tüm bileşenleri tek bir üst gövdede (LazyVerticalGrid) topladık, sonsuz aşağı kayma sağlandı!
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = paddingValues,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                ) {
                    // 1. Satır: Kampanya Bannerı
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        PremiumBannerSection()
                    }

                    // 2. Satır: Sola Doğru Otomatik Akan Hikayeler
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Sizden Gelenler ve Duyurular",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            AutoScrollingStories()
                        }
                    }
                    // 3. Satır: İlan Başlığı ve Filtre Durumu
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Mevcut Araçlar", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            if (selectedCity.isNotEmpty() || selectedModel.isNotEmpty() || selectedDistrict.isNotEmpty()) {
                                Text("Temizle", color = Color.Red, modifier = Modifier.clickable { selectedCity = ""; selectedDistrict = ""; selectedModel = ""; selectedDates = "" })
                            }
                        }
                    }

                    // Arabalar Listesi (Alt alta sonsuza kadar uzanabilir)
                    items(filteredCars) { car ->
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                            modifier = Modifier.fillMaxWidth().height(240.dp)
                        ) {
                            Column {
                                AsyncImage(
                                    model = car.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxWidth().height(120.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(car.brand, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text(car.model, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 1)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                                        Text(" ${car.city}, ${car.district} • ${car.km}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(car.price, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumBannerSection() {
    Box(modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(24.dp))) {
        AsyncImage(
            model = "https://images.unsplash.com/photo-1503376780353-7e6692767b70?q=80&w=600",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)))))
        Text("YAZ FIRSATI\nPremium Araçlarda %30 İndirim", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.BottomStart).padding(16.dp))
    }
}

// --- ÇÖZÜM: OTOMATİK SOLA AKAN HİKAYELER ---
@Composable
fun AutoScrollingStories() {
    val stories = remember {
        listOf(
            StoryItem("Müşteriler", "https://images.unsplash.com/photo-1517486808906-6ca8b3f04846?q=80&w=150"),
            StoryItem("Teslimat", "https://images.unsplash.com/photo-1546519638-68e109498ffc?q=80&w=150"),
            StoryItem("Yeni Fiyat", "https://images.unsplash.com/photo-1559526324-4b87b5e36e44?q=80&w=150"),
            StoryItem("Sürprizler", "https://images.unsplash.com/photo-1486006920555-c77dce18193b?q=80&w=150"),
            StoryItem("Yolculuk", "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?q=80&w=150"),
            StoryItem("Garaj", "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?q=80&w=150")
        )
    }

    val lazyListState = rememberLazyListState()

    // Sonsuz yavaş kaydırma motoru
    LaunchedEffect(Unit) {
        while (true) {

            lazyListState.animateScrollBy(
                value = 200f,
                animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
            )
            // Listenin sonuna gelince başa sarma güvenliği
            if (!lazyListState.canScrollForward) {
                lazyListState.scrollToItem(0)
            }
        }
    }

    LazyRow(
        state = lazyListState,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(stories.size * 5) { index -> // Sonsuz döngü illüzyonu için çarpı 5
            val story = stories[index % stories.size]
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(70.dp).clip(CircleShape).border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)) {
                    AsyncImage(model = story.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize().padding(3.dp).clip(CircleShape), contentScale = ContentScale.Crop)
                }
                Text(story.title, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// --- 3. SAHİBİNDEN TARZI ÖNERİLİ GELİŞMİŞ FİLTRELEME ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSearchScreen(onClose: () -> Unit, onApplyFilters: (String, String, String, String) -> Unit) {
    var cityInput by remember { mutableStateOf("") }
    var districtInput by remember { mutableStateOf("") }
    var modelInput by remember { mutableStateOf("") }
    var dateDisplay by remember { mutableStateOf("") }

    var isCitySelected by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Statik Akıllı Veri Tabanı İl-İlçe Eşleşmeleri
    val databaseCities = listOf("İstanbul", "İzmir", "Ankara", "Bursa", "Antalya")
    val databaseDistricts = mapOf(
        "İstanbul" to listOf("Beşiktaş", "Kadıköy", "Üsküdar", "Şişli"),
        "İzmir" to listOf("Çeşme", "Bornova", "Karşıyaka", "Konak"),
        "Ankara" to listOf("Çankaya", "Keçiören", "Yenimahalle"),
        "Bursa" to listOf("Nilüfer", "Osmangazi", "Yıldırım"),
        "Antalya" to listOf("Alanya", "Muratpaşa", "Manavgat")
    )
    val databaseBrands = listOf("Porsche", "BMW", "Mercedes-Benz", "Tesla", "Audi", "Range Rover", "Mini Cooper")

    // Tahmin Listeleri (Kullanıcı harf girdikçe filtrelenir)
    val filteredCitySuggestions = databaseCities.filter { it.contains(cityInput, ignoreCase = true) && cityInput.isNotEmpty() && !isCitySelected }
    val filteredDistrictSuggestions = if (isCitySelected && databaseDistricts.containsKey(cityInput)) {
        databaseDistricts[cityInput]!!.filter { it.contains(districtInput, ignoreCase = true) && districtInput.isNotEmpty() }
    } else emptyList()
    val filteredBrandSuggestions = databaseBrands.filter { it.contains(modelInput, ignoreCase = true) && modelInput.isNotEmpty() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Detaylı Arama") }, navigationIcon = { IconButton(onClick = onClose) { Icon(Icons.Default.Close, null) } }) }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {

            // 1. İL SEÇİMİ VE ÖNERİLER
            OutlinedTextField(
                value = cityInput, onValueChange = { cityInput = it; isCitySelected = false; districtInput = "" },
                label = { Text("İl Girin") }, modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.LocationOn, null) }
            )
            if (filteredCitySuggestions.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    filteredCitySuggestions.forEach { city ->
                        Text(city, modifier = Modifier.fillMaxWidth().clickable { cityInput = city; isCitySelected = true }.padding(12.dp))
                    }
                }
            }

            // 2. İLÇE SEÇİMİ VE ÖNERİLER (Sadece İl seçildiyse aktif olur)
            OutlinedTextField(
                value = districtInput, onValueChange = { districtInput = it },
                label = { Text("İlçe Girin") }, modifier = Modifier.fillMaxWidth(),
                enabled = isCitySelected, leadingIcon = { Icon(Icons.Default.LocationOn, null) }
            )
            if (filteredDistrictSuggestions.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    filteredDistrictSuggestions.forEach { district ->
                        Text(district, modifier = Modifier.fillMaxWidth().clickable { districtInput = district }.padding(12.dp))
                    }
                }
            }

            // 3. MODEL SEÇİMİ VE ÖNERİLER
            OutlinedTextField(
                value = modelInput, onValueChange = { modelInput = it },
                label = { Text("Araba Markası") }, modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.DirectionsCar, null) }
            )
            if (filteredBrandSuggestions.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    filteredBrandSuggestions.forEach { brand ->
                        Text(brand, modifier = Modifier.fillMaxWidth().clickable { modelInput = brand }.padding(12.dp))
                    }
                }
            }

            // 4. TARİH SEÇİMİ (Zorunlu Çift Tarihli Resmi Takvim)
            OutlinedTextField(
                value = dateDisplay, onValueChange = {}, readOnly = true,
                label = { Text("Kiralama Tarih Aralığı (Zorunlu)") },
                modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                enabled = false,
                colors = TextFieldDefaults.colors(disabledTextColor = MaterialTheme.colorScheme.onSurface, disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant),
                leadingIcon = { IconButton(onClick = { showDatePicker = true }) { Icon(Icons.Default.DateRange, null) } }
            )

            Spacer(modifier = Modifier.weight(1f))

            // ARA BUTONU (Giriş doğrulamaları ile birlikte)
            Button(
                onClick = { onApplyFilters(cityInput, districtInput, modelInput, dateDisplay) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = cityInput.isNotEmpty() && dateDisplay.isNotEmpty() // En az İl ve Tarih seçimi zorunlu kılındı
            ) {
                Text("Sonuçları Göster", fontWeight = FontWeight.Bold)
            }
        }
    }

    // --- MATERIAL 3 ÇİFT TARİH SEÇİCİ DİALOG ---
    if (showDatePicker) {
        val dateRangePickerState = rememberDateRangePickerState()
        Dialog(onDismissRequest = { showDatePicker = false }) {
            Card(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Tarih Aralığı Seçin", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { showDatePicker = false }) { Icon(Icons.Default.Close, null) }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        DateRangePicker(state = dateRangePickerState, modifier = Modifier.fillMaxSize(), title = null, headline = null, showModeToggle = false)
                    }
                    Button(
                        onClick = {
                            val start = dateRangePickerState.selectedStartDateMillis
                            val end = dateRangePickerState.selectedEndDateMillis
                            if (start != null && end != null) { // İki tarihin de seçildiğini garanti ediyoruz
                                val formatter = SimpleDateFormat("dd MMM yyyy", Locale("tr"))
                                val startDateStr = formatter.format(Date(start))
                                val endDateStr = formatter.format(Date(end))
                                dateDisplay = "$startDateStr - $endDateStr"
                                showDatePicker = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null
                    ) {
                        Text("Tarihleri Onayla")
                    }
                }
            }
        }
    }
}