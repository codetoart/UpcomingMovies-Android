package com.codetoart.android.upcomingmovies

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.codetoart.android.upcomingmovies.data.local.PreferenceHelper
import com.codetoart.android.upcomingmovies.data.model.Configuration
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PreferenceHelperTest {

    private val appContext = InstrumentationRegistry.getInstrumentation().context

    private val objectMapper = ObjectMapperSingleton.get()
    private val preferenceHelper = PreferenceHelper.init(appContext, objectMapper)

    private lateinit var allPreferences: Map<String, *>

    private lateinit var mockConfiguration: Configuration

    @Before
    fun getAllPreferences() {
        println("-> getAllPreferences -> before")

        mockConfiguration = objectMapper.fromJson(MockConfiguration.actualMock, Configuration::class.java)!!

        allPreferences = preferenceHelper.getSharedPreferences().all
        preferenceHelper.getSharedPreferences().edit().clear().commit()
    }

    @Test
    fun getConfiguration_nullOnFirstExecution() {
        println("-> getConfiguration_nullOnFirstExecution")

        val configuration = preferenceHelper.getConfiguration()
        assertNull(configuration)
    }

    @Test
    fun getConfiguration_NonNullAfterSet() {
        println("-> getConfiguration_NonNullAfterSet")

        preferenceHelper.setConfiguration(mockConfiguration)
        val configuration = preferenceHelper.getConfiguration()
        assertEquals(mockConfiguration, configuration)
    }

    @After
    fun restoreAllPreferences() {
        println("-> restoreAllPreferences -> after")

        preferenceHelper.getSharedPreferences().edit().clear().commit()

        preferenceHelper.getSharedPreferences().edit().apply {
            for (entry in allPreferences.entries) {
                val value = entry.value
                val key = entry.key
                when (value) {
                    is String -> putString(key, value)
                    is Set<*> -> putStringSet(key, value as Set<String>)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Float -> putFloat(key, value)
                    is Boolean -> putBoolean(key, value)
                }
            }
            commit()
        }
    }
}
