package com.sparkstudios.biometric

object BiometricPromptController {

    private var shouldShowBiometric = true

    fun resetBiometricFlag(){
        shouldShowBiometric = true
    }

    fun shouldShowBiometric(): Boolean {
        return shouldShowBiometric.also { shouldShowBiometric = false }
    }
}
