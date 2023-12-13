package io.github.vinccool96.observable.internal.collections

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.beans.Observable
import io.github.vinccool96.observable.collections.ModifiableObservableListBase
import io.github.vinccool96.observable.collections.ObservableList
import io.github.vinccool96.observable.dev.Callback
import io.github.vinccool96.observable.internal.collections.NonIterableListChange.SimplePermutationChange
import io.github.vinccool96.observable.internal.utils.BitVector

internal open class ObservableListWrapper<E>(list: MutableList<E>) : ModifiableObservableListBase<E>(),
        ObservableList<E>, SortableList<E>, RandomAccess {

    private val backingList: MutableList<E> = list

    private var elementObserver0: ElementObserver<E>? = null

    private val elementObserver: ElementObserver<E>?
        get() = this.elementObserver0

    constructor(list: MutableList<E>, extractor: Callback<E, Array<Observable>>) : this(list) {
        this.elementObserver0 = ElementObserver(extractor, { param ->
            InvalidationListener {
                beginChange()
                val size = this@ObservableListWrapper.size
                for (i in 0 until size) {
                    if (this@ObservableListWrapper[i] == param) {
                        nextUpdate(i)
                    }
                }
                endChange()
            }
        }, this)
        for (e in this.backingList) {
            this.elementObserver!!.attachListener(e)
        }
    }

    override fun get(index: Int): E {
        return this.backingList[index]
    }

    override val size: Int
        get() = this.backingList.size

    override fun doAdd(index: Int, element: E) {
        if (this.elementObserver != null) {
            this.elementObserver!!.attachListener(element)
        }
        this.backingList.add(index, element)
    }

    override fun doSet(index: Int, element: E): E {
        val removed = this.backingList.set(index, element)
        if (this.elementObserver != null) {
            this.elementObserver!!.detachListener(removed)
            this.elementObserver!!.attachListener(element)
        }
        return removed
    }

    override fun doRemove(index: Int): E {
        val removed = this.backingList.removeAt(index)
        if (this.elementObserver != null) {
            this.elementObserver!!.detachListener(removed)
        }
        return removed
    }

    override fun indexOf(element: E): Int {
        return this.backingList.indexOf(element)
    }

    override fun lastIndexOf(element: E): Int {
        return this.backingList.lastIndexOf(element)
    }

    override operator fun contains(element: E): Boolean {
        return this.backingList.contains(element)
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        return this.backingList.containsAll(elements)
    }

    override fun clear() {
        if (this.elementObserver != null) {
            for (e in this) {
                this.elementObserver!!.detachListener(e)
            }
        }
        if (this.hasListeners) {
            beginChange()
            nextRemove(0, this)
        }
        this.backingList.clear()
        ++this.modificationCount
        if (this.hasListeners) {
            endChange()
        }
    }

    override fun remove(from: Int, to: Int) {
        beginChange()
        for (i in from until to) {
            removeAt(from)
        }
        endChange()
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        beginChange()
        val bs = BitVector(elements.size)
        for (i in this.indices) {
            if (elements.contains(this[i])) {
                bs.set(i, true)
            }
        }
        if (!bs.empty) {
            var cur = this.size
            while (bs.previousSetBit(cur - 1).also { cur = it } >= 0) {
                removeAt(cur)
            }
        }
        endChange()
        return !bs.empty
    }

    private lateinit var helper: SortHelper

    @Suppress("UNCHECKED_CAST")
    override fun sort() {
        if (this.backingList.isNotEmpty()) {
            val perm: IntArray = this.sortHelper.sort((this.backingList as MutableList<Comparable<Any?>>))
            fireChange(SimplePermutationChange(0, this.size, perm, this))
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun sortWith(comparator: Comparator<in E>) {
        if (this.backingList.isNotEmpty()) {
            val perm: IntArray = this.sortHelper.sort(this.backingList as MutableList<Comparable<Any?>>,
                    comparator as Comparator<in Comparable<Any?>>)
            fireChange(SimplePermutationChange(0, this.size, perm, this))
        }
    }

    private val sortHelper: SortHelper
        get() {
            if (!this::helper.isInitialized) {
                this.helper = SortHelper()
            }
            return this.helper
        }
}