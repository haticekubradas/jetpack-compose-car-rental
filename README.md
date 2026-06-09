# jetpack-compose-car-rental
Modern Car Rental Android app built with Kotlin and Jetpack Compose. Features State Hoisting architecture, Material Design 3 UI, and modern Photo Picker integration.

#  Car Rental & Management App (Jetpack Compose)

Modern mimari prensipleri ve kullanıcı odaklı UI/UX tasarımları gözetilerek geliştirilmiş, hem Araç Sahibi (Owner) hem de Müşteri (Customer) deneyimini tek bir platformda birleştiren bir mobil araç kiralama uygulamasıdır.

##  Öne Çıkan Özellikler

- **Çift Taraflı Kullanıcı Deneyimi:** - **Müşteri Modülü:** Gelişmiş şehir/ilçe filtreleme sistemi ve modern araç kartları ile dinamik araç keşfi.
  - **Araç Sahibi Modülü:** Finansal özet kartları (Aylık kazanç, aktif ilanlar) ve canlı ilan ekleme formu.
- **Dinamik Medya Entegrasyonu:** Android Activity Result API (Photo Picker) kullanılarak cihaz galerisinden canlı fotoğraf seçebilme ve bunu asenkron olarak listeleme.
- **Gelişmiş State Yönetimi:** Ekranlar arası veri tutarlılığını sağlamak için **State Hoisting** mimari deseni uygulanmıştır.

##  Kullanılan Teknolojiler & Kütüphaneler

- **Dil:** Kotlin
- **UI Framework:** Jetpack Compose (Modern Declarative UI)
- **Tasarım Sistemi:** Material Design 3 (Card, Scaffold, OutlinedTextField, CenterAlignedTopAppBar)
- **Navigasyon:** Jetpack Navigation Component
- **Görsel Yükleme:** Coil (Asynchronous Image Loading)
- **Veri Yönetimi:** SnapshotStateList & State Hoisting (Veri akışı optimizasyonu)

##  Mimari Yaklaşım

Projede, Jetpack Compose'un modern state yönetim mekanizmaları aktif olarak kullanılmıştır. Özellikle `Owner` ve `Customer` ekranlarının tek bir canlı veri kaynağından beslenmesi amacıyla **State Hoisting** prensibi uygulanmış, verinin tek bir çatıdan (Single Source of Truth) aşağıya doğru akıtılması (Unidirectional Data Flow) sağlanmıştır. Bu sayede gereksiz recomposition'ların önüne geçilmiş ve performans optimizasyonu sağlanmıştır.

