package com.sparkstudios.biometric

import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BiometricAuthHandlerTest {

    private lateinit var biometricAuthenticator: BiometricAuthenticator

    @Before
    fun setUp() {
        biometricAuthenticator = mockk()
        mockkObject(BiometricPromptController)

        every { biometricAuthenticator.isSecurityFeaturePresent() } returns true
        every { biometricAuthenticator.isBiometricAvailableButNotSet() } returns false
        every { biometricAuthenticator.isBiometricAvailableAndEnrolled() } returns true
        every { BiometricPromptController.shouldShowBiometric() } returns true

    }

    @Test
    fun `returns BYPASS when bypass is true`() {

        every { biometricAuthenticator.isSecurityFeaturePresent() } returns true
        every { biometricAuthenticator.isBiometricAvailableButNotSet() } returns false
        every { biometricAuthenticator.isBiometricAvailableAndEnrolled() } returns true
        every { BiometricPromptController.shouldShowBiometric() } returns true

        val handler = BiometricAuthHandler(
            biometricAuthenticator,
            requirement = { AuthenticationRequirement.ENABLED_AND_MANDATORY },
            bypass = { true }
        )

        val result = handler.evaluate()
        assertEquals(BiometricDecision.BYPASS, result)

        every { biometricAuthenticator.isSecurityFeaturePresent() } returns true
        every { biometricAuthenticator.isBiometricAvailableButNotSet() } returns false
        every { biometricAuthenticator.isBiometricAvailableAndEnrolled() } returns true
        every { BiometricPromptController.shouldShowBiometric() } returns true
    }

    @Test
    fun `returns BYPASS when requirement is NO_ENABLED`() {

        every { biometricAuthenticator.isSecurityFeaturePresent() } returns true
        every { biometricAuthenticator.isBiometricAvailableButNotSet() } returns false
        every { biometricAuthenticator.isBiometricAvailableAndEnrolled() } returns true

        every { BiometricPromptController.shouldShowBiometric() } returns true

        val handler = BiometricAuthHandler(
            biometricAuthenticator,
            requirement = { AuthenticationRequirement.NO_ENABLED },
            bypass = { true }
        )

        val result = handler.evaluate()
        assertEquals(BiometricDecision.BYPASS, result)
    }

    @Test
    fun `returns SECURITY_NOT_PRESENT when feature is missing in ENABLED_AND_MANDATORY`() {

        every { biometricAuthenticator.isSecurityFeaturePresent() } returns false

        val handler = BiometricAuthHandler(
            biometricAuthenticator,
            requirement = { AuthenticationRequirement.ENABLED_AND_MANDATORY },
            bypass = { false }
        )

        val result = handler.evaluate()
        assertEquals(BiometricDecision.SECURITY_NOT_PRESENT, result)
    }

    @Test
    fun `returns SETUP_REQUIRED when biometric not set in ENABLED_AND_MANDATORY`() {

        every { biometricAuthenticator.isSecurityFeaturePresent() } returns true
        every { biometricAuthenticator.isBiometricAvailableButNotSet() } returns true
        every { biometricAuthenticator.isBiometricAvailableAndEnrolled() } returns false
        every { BiometricPromptController.shouldShowBiometric() } returns true

        val handler = BiometricAuthHandler(
            biometricAuthenticator,
            requirement = { AuthenticationRequirement.ENABLED_AND_MANDATORY },
            bypass = { false }
        )

        val result = handler.evaluate()
        assertEquals(BiometricDecision.SETUP_REQUIRED, result)
    }

    @Test
    fun `returns AUTHENTICATE when biometrics set and prompt should show in ENABLED_AND_MANDATORY`() {
        every { biometricAuthenticator.isSecurityFeaturePresent() } returns true
        every { biometricAuthenticator.isBiometricAvailableButNotSet() } returns false
        every { biometricAuthenticator.isBiometricAvailableAndEnrolled() } returns true
        every { BiometricPromptController.shouldShowBiometric() } returns true

        val handler = BiometricAuthHandler(
            biometricAuthenticator,
            requirement = { AuthenticationRequirement.ENABLED_AND_MANDATORY },
            bypass = { false }
        )

        val result = handler.evaluate()
        assertEquals(BiometricDecision.AUTHENTICATE, result)
    }

    @Test
    fun `returns PROMPT_SKIPPED when prompt is not shown in ENABLED_AND_MANDATORY`() {
        every { biometricAuthenticator.isSecurityFeaturePresent() } returns true
        every { biometricAuthenticator.isBiometricAvailableButNotSet() } returns false
        every { BiometricPromptController.shouldShowBiometric() } returns false

        val handler = BiometricAuthHandler(
            biometricAuthenticator,
            requirement = { AuthenticationRequirement.ENABLED_AND_MANDATORY },
            bypass = { false }
        )

        val result = handler.evaluate()
        assertEquals(BiometricDecision.PROMPT_SKIPPED, result)
    }

    @Test
    fun `returns AUTHENTICATE when biometric enrolled in ENABLED_BUT_NOT_MANDATORY`() {
        every { biometricAuthenticator.isSecurityFeaturePresent() } returns true
        every { biometricAuthenticator.isBiometricAvailableAndEnrolled() } returns true
        every { BiometricPromptController.shouldShowBiometric() } returns true

        val handler = BiometricAuthHandler(
            biometricAuthenticator,
            requirement = { AuthenticationRequirement.ENABLED_BUT_NOT_MANDATORY },
            bypass = { false }
        )

        val result = handler.evaluate()
        assertEquals(BiometricDecision.AUTHENTICATE, result)
    }

    @Test
    fun `returns PROMPT_SKIPPED when biometric enrolled but prompt not shown in ENABLED_BUT_NOT_MANDATORY`() {
        every { biometricAuthenticator.isSecurityFeaturePresent() } returns true
        every { biometricAuthenticator.isBiometricAvailableAndEnrolled() } returns true
        every { BiometricPromptController.shouldShowBiometric() } returns false

        val handler = BiometricAuthHandler(
            biometricAuthenticator,
            requirement = { AuthenticationRequirement.ENABLED_BUT_NOT_MANDATORY },
            bypass = { false }
        )

        val result = handler.evaluate()
        assertEquals(BiometricDecision.PROMPT_SKIPPED, result)
    }

    @Test
    fun `returns PROMPT_SKIPPED when biometrics not enrolled in ENABLED_BUT_NOT_MANDATORY`() {
        every { biometricAuthenticator.isSecurityFeaturePresent() } returns true
        every { biometricAuthenticator.isBiometricAvailableAndEnrolled() } returns false

        val handler = BiometricAuthHandler(
            biometricAuthenticator,
            requirement = { AuthenticationRequirement.ENABLED_BUT_NOT_MANDATORY },
            bypass = { false }
        )

        val result = handler.evaluate()
        assertEquals(BiometricDecision.PROMPT_SKIPPED, result)
    }

    @Test
    fun `returns SECURITY_NOT_PRESENT when feature is missing in ENABLED_BUT_NOT_MANDATORY`() {
        every { biometricAuthenticator.isSecurityFeaturePresent() } returns false

        val handler = BiometricAuthHandler(
            biometricAuthenticator,
            requirement = { AuthenticationRequirement.ENABLED_BUT_NOT_MANDATORY },
            bypass = { false }
        )

        val result = handler.evaluate()
        assertEquals(BiometricDecision.SECURITY_NOT_PRESENT, result)
    }

    @Test
    fun `conflicting biometric state - both enrolled and not set`() {
        every { biometricAuthenticator.isSecurityFeaturePresent() } returns true
        every { biometricAuthenticator.isBiometricAvailableButNotSet() } returns true
        every { biometricAuthenticator.isBiometricAvailableAndEnrolled() } returns true
        every { BiometricPromptController.shouldShowBiometric() } returns true

        val handler = BiometricAuthHandler(
            biometricAuthenticator,
            requirement = { AuthenticationRequirement.ENABLED_AND_MANDATORY },
            bypass = { false }
        )

        val result = handler.evaluate()
        assertEquals(BiometricDecision.BYPASS, result)
    }

    @Test
    fun `biometric enrolled but prompt not shown`() {
        every { biometricAuthenticator.isSecurityFeaturePresent() } returns true
        every { biometricAuthenticator.isBiometricAvailableAndEnrolled() } returns true
        every { BiometricPromptController.shouldShowBiometric() } returns false

        val handler = BiometricAuthHandler(
            biometricAuthenticator,
            requirement = { AuthenticationRequirement.ENABLED_BUT_NOT_MANDATORY },
            bypass = { false }
        )

        val result = handler.evaluate()
        assertEquals(BiometricDecision.PROMPT_SKIPPED, result)
    }


}

