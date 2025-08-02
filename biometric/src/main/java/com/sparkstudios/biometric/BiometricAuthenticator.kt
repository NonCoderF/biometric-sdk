package com.sparkstudios.biometric

interface BiometricAuthenticator {
    fun authenticate()
    fun isBiometricAvailableButNotSet(): Boolean
    fun isBiometricAvailableAndEnrolled(): Boolean
    fun isSecurityFeaturePresent(): Boolean
}

interface BiometricAuthenticatorCallback{
    fun onBiometricSuccess()
    fun onBiometricFailure(errorCode: Int, errString: CharSequence)
}

open class BiometricAuthenticatorImpl(
    private val biometricManager: MyBiometricManagerInterface,
    private val biometricAuthenticatorCallback: BiometricAuthenticatorCallback
) : BiometricAuthenticator {

    override fun authenticate() {
        biometricManager.setBiometricPromptCallback(object : BiometricCallback {
            override fun onUnlocked() {
                biometricAuthenticatorCallback.onBiometricSuccess()
            }

            override fun onUnlockError(errorCode: Int, errString: CharSequence) {
                biometricAuthenticatorCallback.onBiometricFailure(errorCode, errString)
            }
        })
        biometricManager.triggerBiometricPrompt()
    }

    override fun isBiometricAvailableButNotSet(): Boolean {
        return biometricManager.isBiometricAvailableButNotSet()
    }

    override fun isBiometricAvailableAndEnrolled(): Boolean {
        return biometricManager.isBiometricAvailableAndEnrolled()
    }

    override fun isSecurityFeaturePresent(): Boolean {
        return biometricManager.isSecurityFeaturePresent()
    }
}


