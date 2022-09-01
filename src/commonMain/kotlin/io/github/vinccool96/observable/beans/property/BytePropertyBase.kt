package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.beans.Observable
import io.github.vinccool96.observable.beans.binding.ByteBinding
import io.github.vinccool96.observable.beans.value.ChangeListener
import io.github.vinccool96.observable.beans.value.ObservableByteValue
import io.github.vinccool96.observable.beans.value.ObservableValue
import io.github.vinccool96.observable.internal.binding.ExpressionHelper
import io.github.vinccool96.ref.WeakReference
import io.github.vinccool96.ref.WeakReferenceFactory

/**
 * The class `BytePropertyBase` is the base class for a property wrapping a `Byte` value.
 *
 * It provides all the functionality required for a property except for the [bean] and [name] values, which must be
 * implemented by extending classes.
 *
 * @see ByteProperty
 *
 * @constructor The constructor of the `BytePropertyBase` that sets an initial value.
 *
 * @param initialValue the initial value of the wrapped value
 */
abstract class BytePropertyBase(initialValue: Byte) : ByteProperty() {

    private var valueState: Byte = initialValue

    private var observable: ObservableByteValue? = null

    private var listener: InvalidationListener? = null

    private var valid: Boolean = true

    private var helper: ExpressionHelper<Number?>? = null

    /**
     * The constructor of the `BytePropertyBase`. The initial value is `0`
     */
    constructor() : this(0)

    override fun addListener(listener: InvalidationListener) {
        if (!hasListener(listener)) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener)
        }
    }

    override fun removeListener(listener: InvalidationListener) {
        if (hasListener(listener)) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener)
        }
    }

    override fun hasListener(listener: InvalidationListener): Boolean {
        val curHelper = this.helper
        return curHelper != null && curHelper.invalidationListeners.contains(listener)
    }

    override fun addListener(listener: ChangeListener<in Number?>) {
        if (!hasListener(listener)) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener)
        }
    }

    override fun removeListener(listener: ChangeListener<in Number?>) {
        if (hasListener(listener)) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener)
        }
    }

    override fun hasListener(listener: ChangeListener<in Number?>): Boolean {
        val curHelper = this.helper
        return curHelper != null && curHelper.changeListeners.contains(listener)
    }

    /**
     * Sends notifications to all attached [InvalidationListeners][InvalidationListener] and
     * [ChangeListeners][ChangeListener].
     *
     * This method is called when the value is changed, either manually by calling [set] or in case of a bound property,
     * if the binding becomes invalid.
     */
    protected open fun fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(this.helper)
    }

    private fun markInvalid() {
        if (this.valid) {
            this.valid = false
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

    override fun get(): Byte {
        this.valid = true
        return this.observable?.get() ?: this.valueState
    }

    override fun set(value: Byte) {
        if (this.bound) {
            val curBean = this.bean
            throw RuntimeException((if (curBean != null) "${curBean::class.simpleName}.$name : " else "") +
                    "A bound value cannot be set.")
        }
        if (this.valueState != value) {
            this.valueState = value
            markInvalid()
        }
    }

    override val bound: Boolean
        get() = this.observable != null

    override fun bind(observable: ObservableValue<out Number?>) {
        val newObservable: ObservableByteValue = if (observable is ObservableByteValue) observable
        else object : ByteBinding() {

            init {
                super.bind(observable)
            }

            override fun computeValue(): Byte {
                return observable.value?.toByte() ?: 0
            }

        }

        if (newObservable != this.observable) {
            unbind()
            this.observable = newObservable
            if (this.listener == null) {
                this.listener = Listener(this)
            }
            this.observable!!.addListener(this.listener!!)
            markInvalid()
        }

    }

    override fun unbind() {
        if (this.observable != null) {
            this.valueState = this.observable!!.value?.toByte() ?: 0
            this.observable!!.removeListener(this.listener!!)
            this.observable = null
        }
    }

    /**
     * Returns a string representation of this `BytePropertyBase` object.
     *
     * @return a string representation of this `BytePropertyBase` object.
     */
    override fun toString(): String {
        val bean = this.bean
        val name = this.name
        val result = StringBuilder("ByteProperty [")
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

    private class Listener(ref: BytePropertyBase) : InvalidationListener {

        private val wRef: WeakReference<BytePropertyBase> = WeakReferenceFactory.createWeakRef(ref)

        override fun invalidated(observable: Observable) {
            val ref: BytePropertyBase? = this.wRef.get()
            if (ref == null) {
                observable.removeListener(this)
            } else {
                ref.markInvalid()
            }
        }

    }

}