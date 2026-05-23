# WCD — Android Application

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Firebase-Integrated-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" />
  <img src="https://img.shields.io/badge/Build-Gradle%20Kotlin%20DSL-02303A?style=for-the-badge&logo=gradle&logoColor=white" />
  <img src="https://img.shields.io/badge/Status-In%20Development-orange?style=for-the-badge" />
</p>

> A Kotlin-based Android application built with modern Android development practices — Firebase backend, Room local database, and a clean architecture setup using KSP.

---

## 📱 About

**WCD** is an Android application developed using **Kotlin** and **Android Studio**, inspired by terminal-style workflows and directory navigation concepts. It leverages Firebase for backend services and Room (via KSP) for local data persistence.

---

## ✨ Features

- 🔐 Firebase authentication and backend integration
- 🗄️ Local data persistence using Room Database (via KSP)
- 🧭 Intuitive navigation and directory-style workflow
- ⚙️ Utility-focused interface for practical use
- 📱 Mobile-first design built on AndroidX

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| Platform | Android (AndroidX) |
| Backend | Firebase (Google Services) |
| Local DB | Room (via KSP) |
| Build System | Gradle with Kotlin DSL |
| IDE | Android Studio |

---

## 📦 Dependencies & Plugins

```kotlin
// Root build.gradle.kts
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)           // Kotlin Symbol Processing (Room)
    alias(libs.plugins.google.services) // Firebase
}
```

**Repositories used:**
- Google Maven
- Maven Central
- JitPack (`https://jitpack.io`)

---

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest stable)
- JDK 11 or higher
- Android SDK installed
- A Firebase project (for backend features)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/SUJITHCHALLA12/WCD.git
   cd WCD
   ```

2. **Open in Android Studio**
   - File → Open → select the project folder

3. **Add Firebase config**
   - Go to [Firebase Console](https://console.firebase.google.com)
   - Download `google-services.json`
   - Place it in the `/app` directory

4. **Build and run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or press ▶️ Run in Android Studio.

---

## 📁 Project Structure

```
WCD/
├── app/                    # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/       # Kotlin source files
│   │   │   ├── res/        # Layouts, drawables, strings
│   │   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml  # Version catalog
├── build.gradle.kts        # Root build file
├── settings.gradle.kts     # Project settings
└── gradle.properties       # Gradle configuration
```

---

## ⚙️ Gradle Configuration

- **JVM Heap:** `2048m` (optimized for Android builds)
- **AndroidX:** Enabled
- **Kotlin Code Style:** Official
- **Non-transitive R classes:** Enabled (faster builds)
- **Build System:** Kotlin DSL (`.kts`)

---

## 🧪 Build Commands

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test

# Clean build
./gradlew clean
```

---

## 📚 What I Learned

- Modern Android development with **Kotlin**
- **Firebase** integration in Android (Auth, Realtime DB / Firestore)
- **Room Database** setup using KSP annotation processing
- **Kotlin DSL** for Gradle build scripts
- **AndroidX** library management and version catalogs
- Activity lifecycle, navigation, and UI structuring

---

## 🙋 Author

**Sujith Challa**
- GitHub: [@SUJITHCHALLA12](https://github.com/SUJITHCHALLA12)
- Project: [Smart Attendance](https://sujithchalla12.github.io/smart-attendance)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

<p align="center">
  <i>"Learn deeply. Build consistently. Improve endlessly."</i>
</p>
