# 🔐 Biometric SDK for Android

[![](https://jitpack.io/v/NoNCoderF/biometric-sdk.svg)](https://jitpack.io/#NoNCoderF/biometric-sdk)
![License](https://img.shields.io/github/license/NoNCoderF/biometric-sdk)


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
- ✅ `NOT_ENABLED`, `ENABLED_AND_MANDATORY`, `ENABLED_BUT_NOT_MANDATORY` auth modes
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
        maven { url = uri("https://jitpack.io") }
    }
}
```

---

### ✅ Step 2: Add the SDK dependency

In your **module-level** `build.gradle.kts`:

```kotlin
implementation("com.github.NoNCoderF:biometric-sdk:v1.0.0")
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
private val biometricAuthenticator: BiometricAuthenticator by lazy {
    val biometricManager = MyBiometricManagerImpl(
        this, //AppCompatActivity
        "Unlock",
        "Unlock to use the app"
    )

    BiometricAuthenticatorImpl(
        biometricManager,
        object : BiometricAuthenticatorCallback{
            override fun onBiometricSuccess() {
                //Proceed onSuccess
            }

            override fun onBiometricFailure() {
                //Proceed onFailure (Cancelled by user)
            }

        }
    )
}

private val biometricAuthHandler: BiometricAuthHandler by lazy {
    BiometricAuthHandler(
        biometricAuthenticator = biometricAuthenticator,
        requirement = { AuthenticationRequirement.ENABLED_AND_MANDATORY },
        bypass = { false },
    )
}

//On user action
// Evaluate biometric flow dynamically
fun onClick() {
    //Reset the prompt controller
    BiometricPromptController.resetBiometricFlag()
    when (biometricAuthHandler.evaluate()) {
        BiometricDecision.BYPASS -> {
            // Biometric is turned off or bypassed manually
        }
        BiometricDecision.SECURITY_NOT_PRESENT -> {
            // Device doesn't support biometric OR no credential fallback
        }
        BiometricDecision.SETUP_REQUIRED -> {
            // Biometric is supported but user hasn't enrolled — suggest settings intent
        }
        BiometricDecision.AUTHENTICATE -> {
            biometricAuthenticator.authenticate()
        }
    }
}

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

    private val biometricAuthenticator: BiometricAuthenticator by lazy {
        val biometricManager = MyBiometricManagerImpl(
            this, //AppCompatActivity
            "Unlock",
            "Unlock to use the app"
        )

        BiometricAuthenticatorImpl(
            biometricManager,
            object : BiometricAuthenticatorCallback{
                override fun onBiometricSuccess() {
                    //Proceed onSuccess
                }

                override fun onBiometricFailure() {
                    //Proceed onFailure (Cancelled by user)
                }

            }
        )
    }

    private val biometricAuthHandler: BiometricAuthHandler by lazy {
        BiometricAuthHandler(
            biometricAuthenticator = biometricAuthenticator,
            requirement = { AuthenticationRequirement.ENABLED_AND_MANDATORY },
            bypass = { false },
        )
    }

    override fun onResume() {
        super.onResume()

        // Evaluate biometric flow dynamically
        when (biometricAuthHandler.evaluate()) {
            BiometricDecision.BYPASS -> {
                // Biometric is turned off or bypassed manually
            }
            BiometricDecision.PROMPT_SKIPPED -> {
                // App in foreground, biometric not required in this state
            }
            BiometricDecision.SECURITY_NOT_PRESENT -> {
                // Device doesn't support biometric OR no credential fallback
            }
            BiometricDecision.SETUP_REQUIRED -> {
                // Biometric is supported but user hasn't enrolled — suggest settings intent
            }
            BiometricDecision.AUTHENTICATE -> {
                biometricAuthenticator.authenticate()
            }
        }
    }
}
```

Then extend it across your app:

```kotlin
class DashboardActivity : BaseBiometricActivity()
```

### 🧭 Use Case Table

| Use Case                             | Recommended Mode                |
|--------------------------------------|----------------------------------|
| Secure a settings/profile page       | 🔐 Identity Protection (Mode 1)  |
| Authenticate before money transfer   | 💳 Transaction-Level (Mode 2)    |
| Lock every screen or session         | 💳 Transaction-Level (Mode 2)    |

---

💡 Use one mode or both — the SDK adapts to your product’s security needs.

---

## 🧪 Testing

- 🔍 Built for testability using dependency injection
- ✅ Easily mock `BiometricAuthenticator` with [MockK](https://mockk.io)
- 🧼 `AppVisibilityTracker` is stateless and safe to test

## 📜 License

MIT License — free for commercial and non-commercial use.  
Just give credit where it’s due ❤️

---

## 👨‍💻 Author

Built with ❤️ by [Nizamuddin Ahmed](https://github.com/NoNCoderF)  
Contributions, issues, and stars are always welcome ⭐
