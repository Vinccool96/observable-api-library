package io.github.vinccool96.observable.beans.value

import io.github.vinccool96.observable.beans.ObservableMockBase
import kotlin.native.internal.GC
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("UNUSED_VALUE")
class NativeWeakChangeListenerTest {

    private lateinit var weakListener: WeakChangeListener<Any?>
    private val o = ObservableMock()
    private val obj1 = Any()
    private val obj2 = Any()

    @Test
    fun testHandle() {
        // GC-ed call
        initListener()
        this.o.reset()
        GC.collect()
        assertTrue(this.weakListener.wasGarbageCollected)
        this.weakListener.changed(this.o, this.obj2, this.obj1)
        assertEquals(1, this.o.removeCounter)
    }

    private fun initListener() {
        var listener: ChangeListenerMock<Any?>? = ChangeListenerMock(Any())
        this.weakListener = WeakChangeListener(listener!!)

        // regular call
        this.weakListener.changed(this.o, this.obj1, this.obj2)
        listener.check(this.o, this.obj1, this.obj2, 1)
        assertFalse(this.weakListener.wasGarbageCollected)
        assertEquals(0, this.o.removeCounter)
        listener = null
    }

    private class ObservableMock : ObservableMockBase() {

        override fun removeListener(listener: ChangeListener<in Any?>) {
            this.removeCounter++
        }

    }

}