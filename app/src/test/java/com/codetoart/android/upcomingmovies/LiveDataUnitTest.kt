package com.codetoart.android.upcomingmovies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LiveDataUnitTest {

    // Execute tasks synchronously
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // The class that has the lifecycle
    @Mock
    lateinit var owner: LifecycleOwner

    // The observer of the event under test
    @Mock
    lateinit var eventObserver: Observer<Int>

    // Defines the Android Lifecycle of an object, used to trigger different events
    private lateinit var lifecycle: LifecycleRegistry

    private var liveData = MutableLiveData<Int>()

    @Captor
    lateinit var valueArgumentCaptor: ArgumentCaptor<Int>

    @Before
    fun setUpLifecycles() {

        MockitoAnnotations.initMocks(this)

        // Link custom lifecycle owner with the lifecycle register.
        lifecycle = LifecycleRegistry(owner)
        `when`(owner.lifecycle).thenReturn(lifecycle)

        // Start observing
        liveData.observe(owner, eventObserver)

        // Start in a non-active state
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    @Test
    fun valueNotSet_onFirstOnResume() {

        // On resume
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // no update should be emitted because no value has been set
        verify(eventObserver, never()).onChanged(ArgumentMatchers.anyInt())
    }

    @Test
    fun valueEmitted_onFirstOnStart() {

        liveData.value = 20

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        verify(eventObserver, times(1)).onChanged(valueArgumentCaptor.capture())

        assertThat(valueArgumentCaptor.value, `is`(20))
    }

    @Test
    fun valueEmittedOnlyOnce_onSecondOnResume() {

        liveData.value = 20
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        verify(eventObserver, times(1)).onChanged(ArgumentMatchers.anyInt())
        reset(eventObserver)

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        liveData.value = 30
        liveData.value = 40
        liveData.value = 50

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        verify(eventObserver, times(1)).onChanged(valueArgumentCaptor.capture())
        assertThat(valueArgumentCaptor.value, `is`(50))
    }
}
