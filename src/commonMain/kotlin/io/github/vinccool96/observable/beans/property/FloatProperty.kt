package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.value.WritableFloatValue

/**
 * This class defines a [Property] wrapping a `Float` value.
 *
 * The value of a `FloatProperty` can be got and set with [get], [set], and [value].
 *
 * A property can be bound and unbound unidirectional with [bind] and [unbind]. Bidirectional bindings can be created
 * and removed with [bindBidirectional] and [unbindBidirectional].
 *
 * The context of a `ObjectProperty` can be read with [bean] and [name].
 *
 * @see io.github.vinccool96.observable.beans.value.ObservableFloatValue
 * @see WritableFloatValue
 * @see ReadOnlyFloatProperty
 * @see Property
 */
@Suppress("UNCHECKED_CAST")
expect abstract class FloatProperty() : ReadOnlyFloatProperty, Property<Number?>, WritableFloatValue {

    override var value: Number?

    override fun bindBidirectional(other: Property<Number?>)

    override fun unbindBidirectional(other: Property<Number?>)

    /**
     * Returns a string representation of this `FloatProperty` object.
     *
     * @return a string representation of this `FloatProperty` object.
     */
    override fun toString(): String

    /**
     * Creates an [ObjectProperty] that bidirectionally bound to this `FloatProperty`. If the value of this
     * `FloatProperty` changes, the value of the `ObjectProperty` will be updated automatically and vice-versa.
     *
     * Can be used for binding an ObjectProperty to FloatProperty.
     *
     * ```
     * val floatProperty: FloatProperty = SimpleFloatProperty(1)
     * val objectProperty: ObjectProperty<Float> = SimpleObjectProperty(2)
     *
     * objectProperty.bind(floatProperty.asObject())
     * ```
     *
     * @return the new `ObjectProperty`
     */
    override fun asObject(): ObjectProperty<Float>

    companion object {

        /**
         * Returns a `FloatProperty` that wraps a [Property]. If the `Property` is already a `FloatProperty`, it
         * will be returned. Otherwise, a new `FloatProperty` is created that is bound to the `Property`.
         *
         * This is very useful when bidirectionally binding an ObjectProperty<Float> and an FloatProperty.
         * ```
         * val floatProperty: FloatProperty = SimpleFloatProperty(1)
         * val objectProperty: ObjectProperty<Float> = SimpleObjectProperty(2)
         *
         * // Need to keep the reference as bidirectional binding uses weak references
         * val objectAsFloat: FloatProperty = FloatProperty.floatProperty(objectProperty)
         *
         * floatProperty.bindBidirectional(objectAsFloat)
         * ```
         *
         * Another approach is to convert the LongProperty to ObjectProperty using [asObject] method.
         *
         * @param property The source `Property`
         *
         * @return A `FloatProperty` that wraps the `Property` if necessary
         */
        fun floatProperty(property: Property<Float?>): FloatProperty

    }

}