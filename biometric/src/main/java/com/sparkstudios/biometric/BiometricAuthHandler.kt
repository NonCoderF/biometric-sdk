package com.sparkstudios.biometric

enum class AuthenticationRequirement {
    NO_ENABLED,
    ENABLED_BUT_NOT_MANDATORY,
    ENABLED_AND_MANDATORY
}

enum class BiometricDecision {
    BYPASS,
    PROMPT_SKIPPED,
    SECURITY_NOT_PRESENT,
    SETUP_REQUIRED,
    AUTHENTICATE
}

open class BiometricAuthHandler(
    private val biometricAuthenticator: BiometricAuthenticator,
    private val requirement: () -> AuthenticationRequirement,
    private val bypass: () -> Boolean,
) {

    fun evaluate(): BiometricDecision {

        if (bypass()) return BiometricDecision.BYPASS

        when (requirement()) {
            AuthenticationRequirement.NO_ENABLED -> {
                return BiometricDecision.BYPASS
            }

            AuthenticationRequirement.ENABLED_BUT_NOT_MANDATORY -> {
                if (!biometricAuthenticator.isSecurityFeaturePresent())
                    return BiometricDecision.SECURITY_NOT_PRESENT

                if (isBiometricStateInConsistent())
                    return BiometricDecision.BYPASS

                if (biometricAuthenticator.isBiometricAvailableAndEnrolled()) {
                    return if (BiometricPromptController.shouldShowBiometric()) {
                        BiometricDecision.AUTHENTICATE
                    } else {
                        BiometricDecision.PROMPT_SKIPPED
                    }
                }

                return BiometricDecision.PROMPT_SKIPPED
            }

            AuthenticationRequirement.ENABLED_AND_MANDATORY -> {
                if (!biometricAuthenticator.isSecurityFeaturePresent())
                    return BiometricDecision.SECURITY_NOT_PRESENT

                if (isBiometricStateInConsistent())
                    return BiometricDecision.BYPASS

                if (biometricAuthenticator.isBiometricAvailableButNotSet())
                    return BiometricDecision.SETUP_REQUIRED

                return if (BiometricPromptController.shouldShowBiometric()) {
                    BiometricDecision.AUTHENTICATE
                } else {
                    BiometricDecision.PROMPT_SKIPPED
                }
            }
        }
    }

    private fun isBiometricStateInConsistent(): Boolean {
        val enrolled = biometricAuthenticator.isBiometricAvailableAndEnrolled()
        val notSet = biometricAuthenticator.isBiometricAvailableButNotSet()

        return enrolled && notSet
    }
}
