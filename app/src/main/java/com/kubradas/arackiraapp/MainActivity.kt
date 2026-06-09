package com.kubradas.arackiraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kubradas.arackiraapp.ui.auth.LoginScreen
import com.kubradas.arackiraapp.ui.auth.RegisterScreen
import com.kubradas.arackiraapp.ui.theme.AracKiraAppTheme
import com.kubradas.arackiraapp.ui.customer.CustomerHomeScreen
import com.kubradas.arackiraapp.ui.owner.OwnerDashboardScreen
import com.kubradas.arackiraapp.ui.owner.OwnerHomeScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AracKiraAppTheme {
                // Sayfa geçişlerini kontrol eden ana kumanda
                val navController = rememberNavController()
                val allCars = remember {
                    mutableStateListOf(
                        Car("Porsche", "911 Carrera", "İstanbul", "Beşiktaş", "15.000 km", "12.000 TL / Gün", "https://images.unsplash.com/photo-1503376780353-7e6692767b70?q=80&w=500"),
                        Car("BMW", "M4 Coupe", "İzmir", "Çeşme", "8.500 km", "9.500 TL / Gün", "https://images.unsplash.com/photo-1555215695-3004980ad54e?q=80&w=500"),
                        Car("Mercedes-Benz", "G-Class", "Bursa", "Nilüfer", "22.000 km", "15.000 TL / Gün", "https://images.unsplash.com/photo-1520050206274-a1ae446cb3cc?q=80&w=500")
                    )
                }

                NavHost(navController = navController, startDestination = "login") {

                    composable("login") {
                        LoginScreen(
                            onNavigateToRegister = { navController.navigate("register") },
                            onLoginSuccess = { email ->
                                if (email.contains("owner", ignoreCase = true)) {
                                    navController.navigate("owner_dashboard")
                                } else {
                                    navController.navigate("customer_home")
                                }
                            }
                        )
                    }

                    composable("register") {
                        RegisterScreen(onNavigateToLogin = { navController.popBackStack() })
                    }

                    // 🌟 MÜŞTERİ EKRANI GÜNCELLEMESİ
                    composable("customer_home") {
                        CustomerHomeScreen(
                            allCars = allCars,
                            onLogout = {
                                // Login ekranına yönlendir ve arkadaki tüm sayfaları temizle (Geri basınca ev ekranı gelmesin)
                                navController.navigate("login") {
                                    popUpTo("customer_home") { inclusive = true }
                                }
                            }
                        )
                    }

                    // 🌟 ARAÇ SAHİBİ EKRANI GÜNCELLEMESİ
                    composable("owner_dashboard") {
                        // NOT: Bir önceki adımda hazırladığımız premium ekranın adı "OwnerHomeScreen" idi.
                        // Eğer dosya adını/fonksiyon adını değiştirmediysen burayı OwnerHomeScreen yapmalısın.
                        OwnerHomeScreen(
                            allCars = allCars,
                            onLogout = {
                                // Login ekranına yönlendir ve arkadaki sahip panelini temizle
                                navController.navigate("login") {
                                    popUpTo("owner_dashboard") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
