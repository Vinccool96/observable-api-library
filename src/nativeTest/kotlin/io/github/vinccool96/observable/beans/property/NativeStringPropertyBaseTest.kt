package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.InvalidationListenerMock
import io.github.vinccool96.observable.beans.value.ObservableStringValueStub
import kotlin.native.internal.GC
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("UNUSED_VALUE")
class NativeStringPropertyBaseTest {

    private val v = ObservableStringValueStub(VALUE_1)

    private val publicListener = InvalidationListenerMock()

    private val privateListener = InvalidationListenerMock()

    @Test
    fun testBindNull() {
        initP()
        GC.collect()
        publicListener.reset()
        privateListener.reset()
        v.set(VALUE_2)
        v.get()
        publicListener.check(null, 0)
        privateListener.check(v, 1)
    }

    private fun initP() {
        var property: StringPropertyMock? = StringPropertyMock()
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
    }

    private class StringPropertyMock(override val bean: Any?, override val name: String?) :
            StringPropertyBase(VALUE_1) {

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

        private const val VALUE_1: String = "Hello World"

        private const val VALUE_2: String = "Goodbye"

    }

}