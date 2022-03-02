package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.value.WritableDoubleValue

/**
 * This class defines a [Property] wrapping a `Double` value.
 *
 * The value of a `DoubleProperty` can be got and set with [get], [set], and [value].
 *
 * A property can be bound and unbound unidirectional with [bind] and [unbind]. Bidirectional bindings can be created
 * and removed with [bindBidirectional] and [unbindBidirectional].
 *
 * The context of a `ObjectProperty` can be read with [bean] and [name].
 *
 * @see io.github.vinccool96.observable.beans.value.ObservableDoubleValue
 * @see WritableDoubleValue
 * @see ReadOnlyDoubleProperty
 * @see Property
 */
@Suppress("UNCHECKED_CAST")
expect abstract class DoubleProperty() : ReadOnlyDoubleProperty, Property<Number?>, WritableDoubleValue {

    override var value: Number?

    override fun bindBidirectional(other: Property<Number?>)

    override fun unbindBidirectional(other: Property<Number?>)

    /**
     * Returns a string representation of this `DoubleProperty` object.
     *
     * @return a string representation of this `DoubleProperty` object.
     */
    override fun toString(): String

    /**
     * Creates an [ObjectProperty] that bidirectionally bound to this `DoubleProperty`. If the value of this
     * `DoubleProperty` changes, the value of the `ObjectProperty` will be updated automatically and vice-versa.
     *
     * Can be used for binding an ObjectProperty to DoubleProperty.
     *
     * ```
     * val doubleProperty: DoubleProperty = SimpleDoubleProperty(1)
     * val objectProperty: ObjectProperty<Double> = SimpleObjectProperty(2)
     *
     * objectProperty.bind(doubleProperty.asObject())
     * ```
     *
     * @return the new `ObjectProperty`
     */
    override fun asObject(): ObjectProperty<Double>

    companion object {

        /**
         * Returns a `DoubleProperty` that wraps a [Property]. If the `Property` is already a `DoubleProperty`, it
         * will be returned. Otherwise, a new `DoubleProperty` is created that is bound to the `Property`.
         *
         * This is very useful when bidirectionally binding an ObjectProperty<Double> and an DoubleProperty.
         * ```
         * val doubleProperty: DoubleProperty = SimpleDoubleProperty(1)
         * val objectProperty: ObjectProperty<Double> = SimpleObjectProperty(2)
         *
         * // Need to keep the reference as bidirectional binding uses weak references
         * val objectAsDouble: DoubleProperty = DoubleProperty.doubleProperty(objectProperty)
         *
         * doubleProperty.bindBidirectional(objectAsDouble)
         * ```
         *
         * Another approach is to convert the LongProperty to ObjectProperty using [asObject] method.
         *
         * @param property The source `Property`
         *
         * @return A `DoubleProperty` that wraps the `Property` if necessary
         */
        fun doubleProperty(property: Property<Double?>): DoubleProperty

    }

}