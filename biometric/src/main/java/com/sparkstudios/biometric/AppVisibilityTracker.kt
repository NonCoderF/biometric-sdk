package com.sparkstudios.biometric

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

public object AppVisibilityTracker : DefaultLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        super.onPause(owner)
        BiometricPromptController.resetBiometricFlag()
    }
}