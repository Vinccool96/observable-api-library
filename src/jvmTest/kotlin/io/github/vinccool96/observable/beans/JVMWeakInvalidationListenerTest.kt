package io.github.vinccool96.observable.beans

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("UNUSED_VALUE")
class JVMWeakInvalidationListenerTest {

    @Test
    fun testHandle() {
        var listener: InvalidationListenerMock? = InvalidationListenerMock()
        val weakListener = WeakInvalidationListener(listener!!)
        val o = ObservableMock()

        // regular call
        weakListener.invalidated(o)
        listener.check(o, 1)
        assertFalse(weakListener.wasGarbageCollected)

        // GC-ed call
        o.reset()
        listener = null
        System.gc()
        assertTrue(weakListener.wasGarbageCollected)
        weakListener.invalidated(o)
        assertEquals(1, o.removeCounter)
    }

    private class ObservableMock : ObservableMockBase() {

        override fun removeListener(listener: InvalidationListener) {
            this.removeCounter++
        }

    }

}