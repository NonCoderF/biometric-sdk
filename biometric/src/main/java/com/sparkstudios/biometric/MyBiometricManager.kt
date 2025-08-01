package com.sparkstudios.biometric

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

interface MyBiometricManagerInterface {
    fun isBiometricAvailableButNotSet(): Boolean
    fun isSecurityFeaturePresent(): Boolean
    fun triggerBiometricPrompt()
    fun setBiometricPromptCallback(biometricCallback: BiometricCallback)
    fun isBiometricAvailableAndEnrolled(): Boolean
}

interface BiometricCallback {
    fun onUnlocked()
    fun onUnlockError()
}

class MyBiometricManagerImpl(
    activity: AppCompatActivity,
    title: String,
    subtitle: String
) : MyBiometricManagerInterface {

    private val context: Context = activity
    private val executor: Executor = ContextCompat.getMainExecutor(activity)
    private var biometricCallback: BiometricCallback? = null
    private val biometricPrompt: BiometricPrompt
    private val promptInfo: BiometricPrompt.PromptInfo

    init {
        biometricPrompt = createBiometricPrompt(activity)
        promptInfo = createPromptInfo(title, subtitle)
    }

    override fun isBiometricAvailableButNotSet(): Boolean {
        val biometricManager = BiometricManager.from(context)
        val authStatus = biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        return authStatus == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
    }

    override fun isBiometricAvailableAndEnrolled(): Boolean {
        val biometricManager = BiometricManager.from(context)
        val authStatus = biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        return authStatus == BiometricManager.BIOMETRIC_SUCCESS
    }

    override fun isSecurityFeaturePresent(): Boolean {
        val biometricManager = BiometricManager.from(context)
        val authStatus = biometricManager.canAuthenticate(
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        )

        return when (authStatus) {
            BiometricManager.BIOMETRIC_SUCCESS,
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> true
            else -> false
        }
    }

    override fun triggerBiometricPrompt() {
        biometricPrompt.authenticate(promptInfo)
    }

    override fun setBiometricPromptCallback(biometricCallback: BiometricCallback) {
        this.biometricCallback = biometricCallback
    }

    private fun createBiometricPrompt(host: FragmentActivity): BiometricPrompt {
        return BiometricPrompt(host, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                biometricCallback?.onUnlocked()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                biometricCallback?.onUnlockError()
            }
        })
    }

    private fun createPromptInfo(title: String, subtitle: String): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
            )
            .build()
    }
}
