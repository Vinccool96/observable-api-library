package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.InvalidationListenerMock
import io.github.vinccool96.observable.beans.value.ObservableLongValueStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JVMLongPropertyBaseTest {

    @Test
    @Suppress("UNUSED_VALUE")
    fun testBindNull() {
        var property: LongPropertyMock? = LongPropertyMock()
        val v = ObservableLongValueStub(VALUE_1)
        val publicListener = InvalidationListenerMock()
        val privateListener = InvalidationListenerMock()
        property!!.addListener(publicListener)
        v.addListener(privateListener)
        property.bind(v)
        assertEquals(VALUE_1, property.get())
        assertTrue(property.bound)
        property.reset()
        publicListener.reset()
        privateListener.reset()

        // GC-ed call
        property = null
        System.gc()
        publicListener.reset()
        privateListener.reset()
        v.set(VALUE_2)
        v.get()
        publicListener.check(null, 0)
        privateListener.check(v, 1)
    }

    private class LongPropertyMock(override val bean: Any?, override val name: String?) :
            LongPropertyBase(VALUE_1) {

        private var counter: Int = 0

        constructor() : this(NO_BEAN, NO_NAME_1)

        fun reset() {
            this.counter = 0
        }

        override fun invalidated() {
            this.counter++
        }

    }

    companion object {

        private val NO_BEAN: Any? = null

        private val NO_NAME_1: String? = null

        private const val VALUE_1: Long = 42L

        private const val VALUE_2: Long = 1234567890123456789L

    }

}