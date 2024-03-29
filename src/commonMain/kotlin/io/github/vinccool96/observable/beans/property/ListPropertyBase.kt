package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.beans.Observable
import io.github.vinccool96.observable.beans.value.ChangeListener
import io.github.vinccool96.observable.beans.value.ObservableValue
import io.github.vinccool96.observable.collections.ListChangeListener
import io.github.vinccool96.observable.collections.ListChangeListener.Change
import io.github.vinccool96.observable.collections.ObservableList
import io.github.vinccool96.observable.internal.binding.ListExpressionHelper
import io.github.vinccool96.ref.WeakReference
import io.github.vinccool96.ref.WeakReferenceFactory

/**
 * The class `ListPropertyBase` is the base class for a property wrapping an [ObservableList].
 *
 * It provides all the functionality required for a property except for the [bean] and [name] values, which must be
 * implemented by extending classes.
 *
 * @param E the type of the `List` elements
 *
 * @see ObservableList
 * @see ListProperty
 *
 * @constructor The constructor of the `ListPropertyBase`.
 *
 * @param initialValue the initial value of the wrapped value
 */
@Suppress("UNCHECKED_CAST")
abstract class ListPropertyBase<E>(initialValue: ObservableList<E>?) : ListProperty<E>() {

    private var valueState: ObservableList<E>? = initialValue

    private var valid: Boolean = true

    private var observable: ObservableValue<out ObservableList<E>?>? = null

    private var listener: InvalidationListener? = null

    private var helper: ListExpressionHelper<E>? = null

    private val listChangeListener: ListChangeListener<E> = ListChangeListener { change ->
        invalidateProperties()
        invalidated()
        fireValueChangedEvent(change)
    }

    init {
        this.valueState?.addListener(this.listChangeListener)
    }

    /**
     * The constructor of `ListPropertyBase`
     */
    constructor() : this(null)

    private lateinit var size0: SizeProperty

    override val sizeProperty: ReadOnlyIntProperty
        get() {
            if (!this::size0.isInitialized) {
                this.size0 = SizeProperty()
            }
            return this.size0
        }

    private inner class SizeProperty : ReadOnlyIntPropertyBase() {

        override fun get(): Int {
            return this@ListPropertyBase.size
        }

        override val bean: Any = this@ListPropertyBase

        override val name: String = "size"

        public override fun fireValueChangedEvent() {
            super.fireValueChangedEvent()
        }

    }

    private lateinit var empty0: EmptyProperty

    override val emptyProperty: ReadOnlyBooleanProperty
        get() {
            if (!this::empty0.isInitialized) {
                this.empty0 = EmptyProperty()
            }
            return this.empty0
        }

    private inner class EmptyProperty : ReadOnlyBooleanPropertyBase() {

        override fun get(): Boolean {
            return this@ListPropertyBase.isEmpty()
        }

        override val bean: Any = this@ListPropertyBase

        override val name: String = "empty"

        public override fun fireValueChangedEvent() {
            super.fireValueChangedEvent()
        }

    }

    override fun addListener(listener: InvalidationListener) {
        if (!hasListener(listener)) {
            this.helper = ListExpressionHelper.addListener(this.helper, this, listener)
        }
    }

    override fun removeListener(listener: InvalidationListener) {
        if (hasListener(listener)) {
            this.helper = ListExpressionHelper.removeListener(this.helper, listener)
        }
    }

    override fun hasListener(listener: InvalidationListener): Boolean {
        val curHelper = this.helper
        return curHelper != null && curHelper.invalidationListeners.contains(listener)
    }

    override fun addListener(listener: ChangeListener<in ObservableList<E>?>) {
        if (!hasListener(listener)) {
            this.helper = ListExpressionHelper.addListener(this.helper, this, listener)
        }
    }

    override fun removeListener(listener: ChangeListener<in ObservableList<E>?>) {
        if (hasListener(listener)) {
            this.helper = ListExpressionHelper.removeListener(this.helper, listener)
        }
    }

    override fun hasListener(listener: ChangeListener<in ObservableList<E>?>): Boolean {
        val curHelper = this.helper
        return curHelper != null && curHelper.changeListeners.contains(listener)
    }

    override fun addListener(listener: ListChangeListener<in E>) {
        if (!hasListener(listener)) {
            this.helper = ListExpressionHelper.addListener(this.helper, this, listener)
        }
    }

    override fun removeListener(listener: ListChangeListener<in E>) {
        if (hasListener(listener)) {
            this.helper = ListExpressionHelper.removeListener(this.helper, listener)
        }
    }

    override fun hasListener(listener: ListChangeListener<in E>): Boolean {
        val curHelper = this.helper
        return curHelper != null && curHelper.listChangeListeners.contains(listener)
    }

    /**
     * Sends notifications to all attached [InvalidationListeners][InvalidationListener],
     * [ChangeListeners][ChangeListener], and [ListChangeListener].
     *
     * This method is called when the value is changed, either manually by calling [set] or in case of a bound property,
     * if the binding becomes invalid.
     */
    protected open fun fireValueChangedEvent() {
        ListExpressionHelper.fireValueChangedEvent(this.helper)
    }

    /**
     * Sends notifications to all attached [InvalidationListeners][InvalidationListener],
     * [ChangeListeners][ChangeListener], and [ListChangeListener].
     *
     * This method is called when the content of the list changes.
     *
     * @param change the change that needs to be propagated
     */
    protected open fun fireValueChangedEvent(change: Change<out E>) {
        ListExpressionHelper.fireValueChangedEvent(this.helper, change)
    }

    private fun invalidateProperties() {
        if (this::size0.isInitialized) {
            (this.sizeProperty as ListPropertyBase<E>.SizeProperty).fireValueChangedEvent()
        }
        if (this::empty0.isInitialized) {
            (this.emptyProperty as ListPropertyBase<E>.EmptyProperty).fireValueChangedEvent()
        }
    }

    private fun markInvalid(oldValue: ObservableList<E>?) {
        if (this.valid) {
            oldValue?.removeListener(this.listChangeListener)
            this.valid = false
            invalidateProperties()
            invalidated()
            fireValueChangedEvent()
        }
    }

    /**
     * The method `invalidated()` can be overridden to receive invalidation notifications. This is the preferred option
     * in `Objects` defining the property, because it requires less memory.
     *
     * The default implementation is empty.
     */
    protected open fun invalidated() {
    }

    override fun get(): ObservableList<E>? {
        if (!this.valid) {
            this.valueState = if (this.observable == null) this.valueState else this.observable!!.value
            this.valid = true
            this.valueState?.addListener(this.listChangeListener)
        }
        return this.valueState
    }

    override fun set(value: ObservableList<E>?) {
        if (this.bound) {
            val curBean = this.bean
            throw RuntimeException((if (curBean != null) "${curBean::class.simpleName}.$name : " else "") +
                    "A bound value cannot be set.")
        }
        if (this.valueState !== value) {
            val oldValue = this.valueState
            this.valueState = value
            markInvalid(oldValue)
        }
    }

    override val bound: Boolean
        get() = this.observable != null

    override fun bind(observable: ObservableValue<out ObservableList<E>?>) {
        if (observable != this.observable) {
            unbind()
            this.observable = observable
            if (this.listener == null) {
                this.listener = Listener(this)
            }
            this.observable!!.addListener(this.listener!!)
            markInvalid(this.valueState)
        }
    }

    override fun unbind() {
        if (this.observable != null) {
            this.valueState = this.observable!!.value
            this.observable!!.removeListener(this.listener!!)
            this.observable = null
        }
    }

    /**
     * Returns a string representation of this `ListPropertyBase` object.
     *
     * @return a string representation of this `ListPropertyBase` object.
     */
    override fun toString(): String {
        val bean = this.bean
        val name = this.name
        val result = StringBuilder("ListProperty [")
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ")
        }
        if (name != null && name.isNotEmpty()) {
            result.append("name: ").append(name).append(", ")
        }
        if (this.bound) {
            result.append("bound, ")
            if (this.valid) {
                result.append("value: ").append(get())
            } else {
                result.append("invalid")
            }
        } else {
            result.append("value: ").append(get())
        }
        result.append("]")
        return result.toString()
    }

    private class Listener<E>(ref: ListPropertyBase<E>) : InvalidationListener {

        private val wRef: WeakReference<ListPropertyBase<E>> = WeakReferenceFactory.createWeakRef(ref)

        override fun invalidated(observable: Observable) {
            val ref = this.wRef.get()
            if (ref == null) {
                observable.removeListener(this)
            } else {
                ref.markInvalid(ref.valueState)
            }
        }

    }

}