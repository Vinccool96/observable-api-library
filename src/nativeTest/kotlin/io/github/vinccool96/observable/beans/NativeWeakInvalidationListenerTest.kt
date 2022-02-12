package io.github.vinccool96.observable.beans

import kotlin.native.internal.GC
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("UNUSED_VALUE")
class NativeWeakInvalidationListenerTest {

    private val o = ObservableMock()

    private lateinit var weakListener: WeakInvalidationListener

    @Test
    fun testHandle() {
        doListener()
        GC.collect()
        assertTrue(weakListener.wasGarbageCollected)
        weakListener.invalidated(o)
        assertEquals(1, o.removeCounter)
    }

    private fun doListener() {
        var listener: InvalidationListenerMock? = InvalidationListenerMock()
        weakListener = WeakInvalidationListener(listener!!)
        // regular call
        weakListener.invalidated(o)
        listener.check(o, 1)
        assertFalse(weakListener.wasGarbageCollected)

        // GC-ed call
        o.reset()
        listener = null
    }

    private class ObservableMock : BaseObservableMock() {

        override fun removeListener(listener: InvalidationListener) {
            this.removeCounter++
        }

    }

}