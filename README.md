# 🔐 Biometric SDK for Android

**A production-grade biometric authentication SDK designed for apps that demand trust.**  
Ideal for fintech, banking, e-wallets, secure messaging, parental control, or any app requiring **identity protection** and **transaction-level security**.

This SDK simplifies AndroidX biometric integration with modern Kotlin practices, supporting:

- Fingerprint
- Face Unlock
- Device Credential fallback (PIN/Pattern/Password)

Built for real-world apps where **authentication reliability is non-negotiable**.

---


## 🚀 Key Features

- ✅ Seamless integration with AndroidX Biometric APIs
- ✅ Supports Fingerprint, Face, and Device Credential fallback
- ✅ Designed for fintech, banking, and high-trust apps
- ✅ `MANDATORY`, `OPTIONAL`, `BYPASSABLE` auth modes
- ✅ Lifecycle-safe: no biometric prompts while app is backgrounded
- ✅ Clean Kotlin interfaces — easy to test with MockK
- ✅ Pluggable, SDK-ready structure — ideal for JitPack/Maven distribution

---

## 📦 Installation via JitPack

Use this SDK directly in your Android project using **JitPack**.

---

### ✅ Step 1: Add JitPack to repositories

In your root-level `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

---

### ✅ Step 2: Add the SDK dependency

In your **module-level** `build.gradle.kts`:

```kotlin
implementation("com.github.NoNCoderF:biometric-sdk:1.0.0")
```

---

## 🧑‍💻 Usage Guide

This SDK offers **two flexible integration modes** depending on your product's security needs:

---

### 🔐 Mode 1: Identity Protection Mode (On-Demand Authentication)

> Best for apps that want to secure **specific features** like viewing a profile, accessing settings, or opening sensitive notes.

Biometric is triggered manually when the user takes a high-trust action.

#### ✅ Example: Trigger Auth Before Showing Profile

```kotlin
BiometricAuthenticatorImpl(
    biometricManager = MyBiometricManagerImpl(
        activity = this,
        title = "Verify Identity",
        subtitle = "Please authenticate to continue"
    ),
    callback = object : BiometricAuthenticatorCallback {
        override fun onBiometricSuccess() {
            // Proceed to sensitive content
        }

        override fun onBiometricFailure() {
            // Show retry / fallback UI
        }
    }
).authenticate()
```

---

### 💳 Mode 2: Transaction-Level Security (Automatic Per-Screen Auth)

> Use this when your app needs **biometric protection for every screen or session**, like banking apps, parental control, or work-profile locked apps.

Biometric prompts auto-trigger as the user navigates the app.

---

#### ✅ Step 1: Register AppVisibilityTracker (Once)

In your `Application` or base `Activity`:

```kotlin
override fun onCreate() {
    super.onCreate()
    ProcessLifecycleOwner.get().lifecycle.addObserver(AppVisibilityTracker)
}
```

---

#### ✅ Step 2: Create a `BaseBiometricActivity`

```kotlin
abstract class BaseBiometricActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()

        if (AppVisibilityTracker.isAppVisible) {
            BiometricAuthenticatorImpl(
                biometricManager = MyBiometricManagerImpl(
                    activity = this,
                    title = "Unlock App",
                    subtitle = "Please authenticate"
                ),
                callback = object : BiometricAuthenticatorCallback {
                    override fun onBiometricSuccess() {
                        // Authenticated ✅
                    }

                    override fun onBiometricFailure() {
                        // Access denied / fallback
                    }
                }
            ).authenticate()
        }
    }
}
```

Then extend it across your app:

```kotlin
class DashboardActivity : BaseBiometricActivity()
```

---

### ⚙️ Optional: Use `AuthMode` for Customizable Flow

```kotlin
when (BiometricAuthHandler.AuthMode.MANDATORY) {
    MANDATORY -> biometric.authenticate()
    OPTIONAL  -> showPromptOrSkip()
    BYPASSABLE -> skipAuthentication()
}
```

---

### 🧭 Use Case Table

| Use Case                             | Recommended Mode                |
|--------------------------------------|----------------------------------|
| Secure a settings/profile page       | 🔐 Identity Protection (Mode 1)  |
| Authenticate before money transfer   | 💳 Transaction-Level (Mode 2)    |
| Lock every screen or session         | 💳 Transaction-Level (Mode 2)    |
| Let user choose to skip biometric    | Use with `OPTIONAL` / `BYPASSABLE` |

---

💡 Use one mode or both — the SDK adapts to your product’s security needs.

---

## 🧪 Testing

- 🔍 Built for testability using dependency injection
- ✅ Easily mock `BiometricAuthenticatorCallback` with [MockK](https://mockk.io)
- 🧼 `AppVisibilityTracker` is stateless and safe to test

## 📜 License

MIT License — free for commercial and non-commercial use.  
Just give credit where it’s due ❤️

---

## 👨‍💻 Author

Built with ❤️ by [Nizamuddin Ahmed](https://github.com/NoNCoderF)  
Contributions, issues, and stars are always welcome ⭐
