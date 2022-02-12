package io.github.vinccool96.observable.beans

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WeakInvalidationListenerTest {

    @Test
    fun testHandle() {
        val listener = InvalidationListenerMock()
        val weakListener = WeakInvalidationListener(listener)
        val o = ObservableMock()

        // regular call
        weakListener.invalidated(o)
        listener.check(o, 1)
        assertFalse(weakListener.wasGarbageCollected)

        // cleared call
        o.reset()
        weakListener.clear()
        assertTrue(weakListener.wasGarbageCollected)
        weakListener.invalidated(o)
        assertEquals(1, o.removeCounter)
    }

    private class ObservableMock : BaseObservableMock() {

        override fun removeListener(listener: InvalidationListener) {
            this.removeCounter++
        }

    }

}