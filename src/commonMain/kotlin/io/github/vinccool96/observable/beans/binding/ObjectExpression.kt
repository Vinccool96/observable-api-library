package io.github.vinccool96.observable.beans.binding

import io.github.vinccool96.observable.beans.Observable
import io.github.vinccool96.observable.beans.value.ObservableObjectValue
import io.github.vinccool96.observable.collections.ObservableCollections
import io.github.vinccool96.observable.collections.ObservableList
import io.github.vinccool96.observable.dev.ReturnsUnmodifiableCollection
import io.github.vinccool96.observable.internal.binding.StringFormatter

/**
 * An `ObjectExpression` is a [ObservableObjectValue] plus additional convenience methods to generate bindings in a
 * fluent style.
 *
 * A concrete subclass of `ObjectExpression` has to implement the method [ObservableObjectValue.get], which provides the
 * actual value of this expression.
 *
 * @param T The type of the wrapped value
 */
abstract class ObjectExpression<T> : ObservableObjectValue<T> {

    override val value: T
        get() = this.get()

    /**
     * Creates a new `BooleanExpression` that holds `true` if this and another [ObservableObjectValue] are equal.
     *
     * @param other the other `ObservableObjectValue`
     *
     * @return the new `BooleanExpression`
     */
    fun isEqualTo(other: ObservableObjectValue<*>): BooleanBinding {
        return Bindings.equal(this, other)
    }

    /**
     * Creates a new `BooleanExpression` that holds `true` if this `ObjectExpression` is equal to a constant value.
     *
     * @param other the constant value
     *
     * @return the new `BooleanExpression`
     */
    fun isEqualTo(other: Any?): BooleanBinding {
        return Bindings.equal(this, other)
    }

    /**
     * Creates a new `BooleanExpression` that holds `true` if this and another [ObservableObjectValue] are not equal.
     *
     * @param other the other `ObservableObjectValue`
     *
     * @return the new `BooleanExpression`
     */
    fun isNotEqualTo(other: ObservableObjectValue<*>): BooleanBinding {
        return Bindings.notEqual(this, other)
    }

    /**
     * Creates a new `BooleanExpression` that holds `true` if this `ObjectExpression` is not equal to a constant value.
     *
     * @param other the constant value
     *
     * @return the new `BooleanExpression`
     */
    fun isNotEqualTo(other: Any?): BooleanBinding {
        return Bindings.notEqual(this, other)
    }

    /**
     * Creates a new [BooleanBinding] that holds `true` if this `ObjectExpression` is `null`.
     *
     * @return the new `BooleanBinding`
     */
    fun isNull(): BooleanBinding {
        return Bindings.isNull(this)
    }

    /**
     * Creates a new [BooleanBinding] that holds `true` if this `ObjectExpression` is not `null`.
     *
     * @return the new `BooleanBinding`
     */
    fun isNotNull(): BooleanBinding {
        return Bindings.isNotNull(this)
    }

    /**
     * Creates a [StringBinding] that holds the value of the `ObjectExpression` turned into a `String`. If the value of
     * this `ObjectExpression` changes, the value of the `StringBinding` will be updated automatically.
     *
     * The conversion is done without any formatting applied.
     *
     * @return the new `StringBinding`
     */
    fun asString(): StringBinding {
        return StringFormatter.convert(this) as StringBinding
    }

    /**
     * Creates a [StringBinding] that holds the value of the `ObjectExpression` turned into a `String`. If the value of
     * this `ObjectExpression` changes, the value of the `StringBinding` will be updated automatically.
     *
     * @param stringMaker How the `String` is created
     *
     * @return the new `StringBinding`
     */
    fun asString(stringMaker: () -> String): StringBinding {
        return Bindings.format(stringMaker, this) as StringBinding
    }

    companion object {

        /**
         * Returns an `ObjectExpression` that wraps an [ObservableObjectValue]. If the `ObservableObjectValue` is
         * already an `ObjectExpression`, it will be returned. Otherwise, a new [ObjectBinding] is created that is bound
         * to the `ObservableObjectValue`.
         *
         * @param value The source `ObservableObjectValue`
         * @param T The type of the wrapped value
         *
         * @return A `ObjectExpression` that wraps the `ObservableObjectValue` if necessary
         */
        fun <T> objectExpression(value: ObservableObjectValue<T>): ObjectExpression<T> {
            return if (value is ObjectExpression) value else object : ObjectBinding<T>() {

                init {
                    super.bind(value)
                }

                override fun dispose() {
                    super.unbind(value)
                }

                override fun computeValue(): T {
                    return value.get()
                }

                @get:ReturnsUnmodifiableCollection
                override val dependencies: ObservableList<Observable>
                    get() = ObservableCollections.singletonObservableList(value)

            }
        }

    }

}