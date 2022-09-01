package io.github.vinccool96.observable.internal.collections

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.collections.ListChangeListener
import io.github.vinccool96.observable.collections.ObservableList

@Suppress("UNCHECKED_CAST")
internal class ImmutableObservableList<E>(vararg elements: E) : AbstractMutableList<E>(), ObservableList<E> {

    private val elements: Array<E>? = if (elements.isNotEmpty()) {
        elements.copyOf() as Array<E>
    } else {
        null
    }

    override fun addListener(listener: InvalidationListener) {
        // no-op
    }

    override fun removeListener(listener: InvalidationListener) {
        // no-op
    }

    override fun hasListener(listener: InvalidationListener): Boolean {
        // no-op
        return false
    }

    override fun addListener(listener: ListChangeListener<in E>) {
        // no-op
    }

    override fun removeListener(listener: ListChangeListener<in E>) {
        // no-op
    }

    override fun hasListener(listener: ListChangeListener<in E>): Boolean {
        // no-op
        return false
    }

    override fun addAll(vararg elements: E): Boolean {
        throw UnsupportedOperationException()
    }

    override fun setAll(vararg elements: E): Boolean {
        throw UnsupportedOperationException()
    }

    override fun setAll(col: Collection<E>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun removeAll(vararg elements: E): Boolean {
        throw UnsupportedOperationException()
    }

    override fun retainAll(vararg elements: E): Boolean {
        throw UnsupportedOperationException()
    }

    override fun remove(from: Int, to: Int) {
        throw UnsupportedOperationException()
    }

    override fun get(index: Int): E {
        if (index < 0 || index >= this.size) {
            throw IndexOutOfBoundsException()
        }
        return this.elements!![index]
    }

    override val size: Int
        get() = this.elements?.size ?: 0

    override fun add(index: Int, element: E) {
        // no-op
    }

    override fun removeAt(index: Int): E {
        throw UnsupportedOperationException()
    }

    override fun set(index: Int, element: E): E {
        throw UnsupportedOperationException()
    }

}