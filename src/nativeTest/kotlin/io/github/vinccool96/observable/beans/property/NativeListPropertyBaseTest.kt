package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.InvalidationListenerMock
import io.github.vinccool96.observable.beans.value.ObservableListValueStub
import io.github.vinccool96.observable.collections.ObservableCollections
import io.github.vinccool96.observable.collections.ObservableList
import kotlin.native.internal.GC
import kotlin.test.*

class NativeListPropertyBaseTest {

    private val v = ObservableListValueStub(VALUE_1)

    private val publicListener = InvalidationListenerMock()

    private val privateListener = InvalidationListenerMock()

    @Test
    fun testBindNull() {
        initProp()
        GC.collect()
        this.publicListener.reset()
        this.privateListener.reset()
        this.v.set(VALUE_2b)
        this.v.get()
        this.publicListener.check(null, 0)
        this.privateListener.check(v, 1)
    }

    @Suppress("UNUSED_VALUE")
    private fun initProp() {
        var property: ListPropertyMock? = ListPropertyMock()
        property!!.addListener(this.publicListener)
        this.v.addListener(this.privateListener)
        property.bind(this.v)
        assertEquals(VALUE_1, property.get())
        assertTrue(property.bound)
        property.reset()
        this.publicListener.reset()
        this.privateListener.reset()

        // GC-ed call
        property = null
        this.v.set(VALUE_2a)
        this.publicListener.reset()
        this.privateListener.reset()
    }

    private class ListPropertyMock : ListPropertyBase<Any>() {

        override val bean = NO_BEAN

        override val name = NO_NAME_1

        private var counter: Int = 0

        override fun invalidated() {
            this.counter++
        }

        fun reset() {
            this.counter = 0
        }

    }

    companion object {

        private val NO_BEAN: Any? = null

        private val NO_NAME_1: String? = null

        private val VALUE_1: ObservableList<Any> = ObservableCollections.observableArrayList()

        private val VALUE_2a: ObservableList<Any> = ObservableCollections.observableArrayList(Any(), Any())

        private val VALUE_2b: ObservableList<Any> = ObservableCollections.observableArrayList(Any(), Any(), Any())

    }

}