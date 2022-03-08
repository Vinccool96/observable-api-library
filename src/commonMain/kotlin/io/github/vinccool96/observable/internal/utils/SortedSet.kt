package io.github.vinccool96.observable.internal.utils

import io.github.vinccool96.observable.collections.ObservableCollections

internal class SortedSet<E> : AbstractMutableSet<E>(), MutableSet<E> {

    private val innerSet: MutableSet<E> = hashSetOf()

    override val size: Int
        get() = this.innerSet.size

    override fun add(element: E): Boolean {
        return this.innerSet.add(element)
    }

    override fun iterator(): MutableIterator<E> {
        return SortedIterator()
    }

    private inner class SortedIterator : MutableIterator<E> {

        private val innerItr: MutableIterator<E>

        private var previous: Container<E>? = null

        init {
            val list = ObservableCollections.observableArrayList(this@SortedSet.innerSet.toMutableList())
            val sorted = list.sorted()
            this.innerItr = sorted.iterator()
        }

        override fun hasNext(): Boolean {
            return this.innerItr.hasNext()
        }

        override fun next(): E {
            val e = this.innerItr.next()
            this.previous = Container(e)
            return e
        }

        override fun remove() {
            this.innerItr.remove()
            this@SortedSet.innerSet.remove(this.previous!!.v)
        }

    }

    private class Container<T>(val v: T)

}