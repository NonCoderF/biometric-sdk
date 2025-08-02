package com.sparkstudios.biometric

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

object AppVisibilityTracker : DefaultLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        super.onPause(owner)
        BiometricPromptController.resetBiometricFlag()
    }
}