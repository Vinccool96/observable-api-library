package io.github.vinccool96.observable.beans.binding

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.beans.Observable
import io.github.vinccool96.observable.beans.value.ChangeListener
import io.github.vinccool96.observable.collections.ObservableCollections
import io.github.vinccool96.observable.collections.ObservableList
import io.github.vinccool96.observable.dev.ReturnsUnmodifiableCollection
import io.github.vinccool96.observable.internal.binding.BindingHelperObserver
import io.github.vinccool96.observable.internal.binding.ExpressionHelper

/**
 * Base class that provides most of the functionality needed to implement a [Binding] of a `Byte` value.
 *
 * `ByteBinding` provides a simple invalidation-scheme. An extending class can register dependencies by calling
 * [bind]. If one of the registered dependencies becomes invalid, this `ByteBinding` is marked as invalid. With
 * [unbind] listening to dependencies can be stopped.
 *
 * To provide a concrete implementation of this class, the method [computeValue] has to be implemented to calculate the
 * value of this binding based on the current state of the dependencies. It is called when [get] is called for an
 * invalid binding.
 *
 * See [DoubleBinding] for an example how this base class can be extended.
 *
 * @see Binding
 * @see NumberBinding
 * @see ByteExpression
 */
abstract class ByteBinding : ByteExpression(), NumberBinding {

    private var valueState: Byte = 0

    private var validState: Boolean = false

    private var observer: BindingHelperObserver? = null

    private var helper: ExpressionHelper<Number?>? = null

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
     * Start observing the dependencies for changes. If the value of one of the dependencies changes, the binding is
     * marked as invalid.
     *
     * @param dependencies the dependencies to observe
     */
    protected fun bind(vararg dependencies: Observable) {
        if (dependencies.isNotEmpty()) {
            if (this.observer == null) {
                this.observer = BindingHelperObserver(this)
            }
            for (dep in dependencies) {
                dep.addListener(this.observer!!)
            }
        }
    }

    /**
     * Stop observing the dependencies for changes.
     *
     * @param dependencies the dependencies to stop observing
     */
    protected fun unbind(vararg dependencies: Observable) {
        if (this.observer != null) {
            for (dep in dependencies) {
                dep.removeListener(this.observer!!)
            }
            this.observer = null
        }
    }

    /**
     * A default implementation of `dispose` that is empty.
     */
    override fun dispose() {
    }

    @get:ReturnsUnmodifiableCollection
    override val dependencies: ObservableList<Observable>
        get() = ObservableCollections.emptyObservableList()

    /**
     * Returns the result of [computeValue]. The method `computeValue()` is only called if the binding is invalid. The
     * result is cached and returned if the binding did not become invalid since the last call of `get()`.
     *
     * @return the current value
     */
    final override fun get(): Byte {
        if (!this.validState) {
            this.valueState = computeValue()
            this.validState = true
        }
        return this.valueState
    }

    /**
     * The method onInvalidating() can be overridden by extending classes to react, if this binding becomes invalid. The
     * default implementation is empty.
     */
    protected open fun onInvalidating() {
    }

    final override fun invalidate() {
        if (this.validState) {
            this.validState = false
            onInvalidating()
            ExpressionHelper.fireValueChangedEvent(this.helper)
        }
    }

    final override val valid: Boolean
        get() = this.validState

    /**
     * Calculates the current value of this binding.
     *
     * Classes extending `ByteBinding` have to provide an implementation of `computeValue`.
     *
     * @return the current value
     */
    protected abstract fun computeValue(): Byte

    /**
     * Returns a string representation of this `ByteBinding` object.
     *
     * @return a string representation of this `ByteBinding` object.
     */
    override fun toString(): String {
        return if (this.validState) "ByteBinding [value: ${get()}]" else "ByteBinding [invalid]"
    }

}