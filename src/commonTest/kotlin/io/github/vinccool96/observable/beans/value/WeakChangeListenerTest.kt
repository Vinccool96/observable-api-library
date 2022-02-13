package io.github.vinccool96.observable.beans.value

import io.github.vinccool96.observable.beans.ObservableMockBase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WeakChangeListenerTest {

    @Test
    fun testHandle() {
        val listener: ChangeListenerMock<Any?> = ChangeListenerMock(Any())
        val weakListener: WeakChangeListener<Any?> = WeakChangeListener(listener)
        val o = ObservableMock()
        val obj1 = Any()
        val obj2 = Any()

        // regular call
        weakListener.changed(o, obj1, obj2)
        listener.check(o, obj1, obj2, 1)
        assertFalse(weakListener.wasGarbageCollected)
        assertEquals(0, o.removeCounter)

        // GC-ed call
        o.reset()
        weakListener.clear()
        assertTrue(weakListener.wasGarbageCollected)
        weakListener.changed(o, obj2, obj1)
        assertEquals(1, o.removeCounter)
    }

    private class ObservableMock : ObservableMockBase() {

        override fun removeListener(listener: ChangeListener<in Any?>) {
            this.removeCounter++
        }

    }

}