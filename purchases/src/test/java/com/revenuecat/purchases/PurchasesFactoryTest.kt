package com.revenuecat.purchases

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PurchasesFactoryTest {

    private val contextMock = mockk<Context>()
    private val apiKeyValidatorMock = mockk<APIKeyValidator>()

    private lateinit var purchasesFactory: PurchasesFactory

    @Before
    fun setup() {
        clearAllMocks()

        purchasesFactory = PurchasesFactory(apiKeyValidatorMock)

        every { apiKeyValidatorMock.validateAndLog("fakeApiKey", Store.PLAY_STORE) } just runs
    }

    @Test
    fun `creating purchase checks context has INTERNET permission`() {
        val configuration = createConfiguration()
        every {
            contextMock.checkCallingOrSelfPermission(Manifest.permission.INTERNET)
        } returns PackageManager.PERMISSION_DENIED
        assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
            purchasesFactory.validateConfiguration(configuration)
        }.withMessage("Purchases requires INTERNET permission.")
    }

    @Test
    fun `creating purchase checks api key is not empty`() {
        val configuration = createConfiguration(apiKey = "")
        every {
            contextMock.checkCallingOrSelfPermission(Manifest.permission.INTERNET)
        } returns PackageManager.PERMISSION_GRANTED
        assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
            purchasesFactory.validateConfiguration(configuration)
        }.withMessage("API key must be set. Get this from the RevenueCat web app")
    }

    @Test
    fun `creating purchase checks context application context is an application`() {
        val configuration = createConfiguration()
        val nonApplicationContextMock = mockk<Context>()
        every {
            contextMock.checkCallingOrSelfPermission(Manifest.permission.INTERNET)
        } returns PackageManager.PERMISSION_GRANTED
        every {
            contextMock.applicationContext
        } returns nonApplicationContextMock
        assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
            purchasesFactory.validateConfiguration(configuration)
        }.withMessage("Needs an application context.")
    }

    @Test
    fun `creating purchase validates api key is valid`() {
        val configuration = createConfiguration()
        val applicationContextMock = mockk<Application>()
        every {
            contextMock.checkCallingOrSelfPermission(Manifest.permission.INTERNET)
        } returns PackageManager.PERMISSION_GRANTED
        every {
            contextMock.applicationContext
        } returns applicationContextMock
        purchasesFactory.validateConfiguration(configuration)
        verify(exactly = 1) { apiKeyValidatorMock.validateAndLog("fakeApiKey", Store.PLAY_STORE) }
    }

    private fun createConfiguration(apiKey: String = "fakeApiKey"): PurchasesConfiguration {
        return PurchasesConfiguration.Builder(contextMock, apiKey).build()
    }
}
