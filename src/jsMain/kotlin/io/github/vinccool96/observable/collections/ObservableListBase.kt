package io.github.vinccool96.observable.collections

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.internal.collections.ListChangeBuilder
import io.github.vinccool96.observable.internal.collections.ListListenerHelper

@Suppress("ConvertArgumentToSet")
actual abstract class ObservableListBase<E> : AbstractMutableList<E>(), ObservableList<E> {

    private var helper: ListListenerHelper<E>? = null

    private val changeBuilder: ListChangeBuilder<E> = ListChangeBuilder(this)

    protected actual fun nextUpdate(pos: Int) {
        this.changeBuilder.nextUpdate(pos)
    }

    protected actual fun nextSet(idx: Int, old: E) {
        this.changeBuilder.nextSet(idx, old)
    }

    protected actual fun nextReplace(from: Int, to: Int, removed: MutableList<out E>) {
        this.changeBuilder.nextReplace(from, to, removed)
    }

    protected actual fun nextRemove(idx: Int, removed: MutableList<out E>) {
        this.changeBuilder.nextRemove(idx, removed)
    }

    protected actual fun nextRemove(idx: Int, removed: E) {
        this.changeBuilder.nextRemove(idx, removed)
    }

    protected actual fun nextPermutation(from: Int, to: Int, perm: IntArray) {
        this.changeBuilder.nextPermutation(from, to, perm)
    }

    protected actual fun nextAdd(from: Int, to: Int) {
        this.changeBuilder.nextAdd(from, to)
    }

    protected actual fun beginChange() {
        this.changeBuilder.beginChange()
    }

    protected actual fun endChange() {
        this.changeBuilder.endChange()
    }

    actual override fun addListener(listener: InvalidationListener) {
        if (!hasListener(listener)) {
            this.helper = ListListenerHelper.addListener(this.helper, listener)
        }
    }

    actual override fun removeListener(listener: InvalidationListener) {
        if (hasListener(listener)) {
            this.helper = ListListenerHelper.removeListener(this.helper, listener)
        }
    }

    actual override fun hasListener(listener: InvalidationListener): Boolean {
        val curHelper = this.helper
        return curHelper != null && curHelper.invalidationListeners.contains(listener)
    }

    actual override fun addListener(listener: ListChangeListener<in E>) {
        if (!hasListener(listener)) {
            this.helper = ListListenerHelper.addListener(this.helper, listener)
        }
    }

    actual override fun removeListener(listener: ListChangeListener<in E>) {
        if (hasListener(listener)) {
            this.helper = ListListenerHelper.removeListener(this.helper, listener)
        }
    }

    actual override fun hasListener(listener: ListChangeListener<in E>): Boolean {
        val curHelper = this.helper
        return curHelper != null && curHelper.listChangeListeners.contains(listener)
    }

    internal actual fun fireChanges(change: ListChangeListener.Change<out E>) {
        fireChange(change)
    }

    protected actual fun fireChange(change: ListChangeListener.Change<out E>) {
        ListListenerHelper.fireValueChangedEvent(this.helper, change)
    }

    protected actual val hasListeners: Boolean
        get() = ListListenerHelper.hasListeners(this.helper)

    actual override fun addAll(vararg elements: E): Boolean {
        return addAll(elements.asList())
    }

    actual override fun setAll(vararg elements: E): Boolean {
        return setAll(elements.asList())
    }

    actual override fun setAll(col: Collection<E>): Boolean {
        throw UnsupportedOperationException()
    }

    actual override fun removeAll(vararg elements: E): Boolean {
        return removeAll(elements.toList())
    }

    actual override fun retainAll(vararg elements: E): Boolean {
        return retainAll(elements.toList())
    }

    actual override fun remove(from: Int, to: Int) {
        removeTheRange(from, to)
    }

    actual override fun add(index: Int, element: E) {
        throw UnsupportedOperationException()
    }

    actual override fun removeAt(index: Int): E {
        throw UnsupportedOperationException()
    }

    actual override fun set(index: Int, element: E): E {
        throw UnsupportedOperationException()
    }

    protected actual open fun removeTheRange(fromIndex: Int, toIndex: Int) {
        super.removeRange(fromIndex, toIndex)
    }

    actual override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }
        if (other !is List<*>) {
            return false
        }

        return orderedEquals(this, other)
    }

    internal actual fun orderedEquals(c: Collection<*>, other: Collection<*>): Boolean {
        if (c.size != other.size) {
            return false
        }

        val otherIterator = other.iterator()
        for (elem in c) {
            val elemOther = otherIterator.next()
            if (elem != elemOther) {
                return false
            }
        }
        return true
    }

}