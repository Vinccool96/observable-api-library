package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.InvalidationListenerMock
import io.github.vinccool96.observable.beans.value.ObservableListValueStub
import io.github.vinccool96.observable.collections.ObservableCollections
import io.github.vinccool96.observable.collections.ObservableList
import kotlin.test.*

class JVMListPropertyBaseTest {

    @Test
    @Suppress("UNUSED_VALUE")
    fun testBindNull() {
        var property: ListPropertyMock? = ListPropertyMock()
        val v = ObservableListValueStub(VALUE_1)
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
        v.set(VALUE_2a)
        publicListener.reset()
        privateListener.reset()
        System.gc()
        publicListener.reset()
        privateListener.reset()
        v.set(VALUE_2b)
        v.get()
        publicListener.check(null, 0)
        privateListener.check(v, 1)
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