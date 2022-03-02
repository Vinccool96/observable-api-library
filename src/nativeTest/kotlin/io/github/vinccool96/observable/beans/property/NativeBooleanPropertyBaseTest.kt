package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.InvalidationListenerMock
import io.github.vinccool96.observable.beans.value.ObservableBooleanValueStub
import kotlin.native.internal.GC
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("UNUSED_VALUE")
class NativeBooleanPropertyBaseTest {

    private val v = ObservableBooleanValueStub(false)

    private val publicListener = InvalidationListenerMock()

    private val privateListener = InvalidationListenerMock()

    @Test
    fun testBindNull() {
        initP()
        GC.collect()
        publicListener.reset()
        privateListener.reset()
        v.set(true)
        v.get()
        publicListener.check(null, 0)
        privateListener.check(v, 1)
    }

    private fun initP() {
        var property: BooleanPropertyMock? = BooleanPropertyMock()
        property!!.addListener(publicListener)
        v.addListener(privateListener)
        property.bind(v)
        assertEquals(false, property.get())
        assertTrue(property.bound)
        property.reset()
        publicListener.reset()
        privateListener.reset()

        // GC-ed call
        property = null
    }

    private class BooleanPropertyMock(override val bean: Any?, override val name: String?) :
            BooleanPropertyBase(false) {

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

    }

}