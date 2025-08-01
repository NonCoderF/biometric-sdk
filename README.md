# 🔐 Biometric SDK for Android

A lightweight and test-covered biometric authentication SDK built using modern Kotlin practices.  
Seamlessly plug into your Android apps to handle device-level biometric and credential verification.

---

## 🚀 Features

- ✅ Supports Fingerprint, Face, and Device Credentials
- ✅ Simple interface to plug into any Android Activity or Fragment
- ✅ Flexible authentication requirements (`MANDATORY`, `OPTIONAL`, `BYPASSABLE`)
- ✅ Robust error handling and fallback support
- ✅ 100% test-covered logic using MockK
- ✅ Built with modular, SDK-ready structure

---

## 📦 Installation (via JitPack)

1. Add JitPack to your `settings.gradle.kts` or `build.gradle.kts` (root):

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
