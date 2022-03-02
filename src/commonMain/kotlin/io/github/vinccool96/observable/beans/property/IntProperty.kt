package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.value.WritableIntValue

/**
 * This class defines a [Property] wrapping a `Int` value.
 *
 * The value of a `IntProperty` can be got and set with [get], [set], and [value].
 *
 * A property can be bound and unbound unidirectional with [bind] and [unbind]. Bidirectional bindings can be created
 * and removed with [bindBidirectional] and [unbindBidirectional].
 *
 * The context of a `ObjectProperty` can be read with [bean] and [name].
 *
 * @see io.github.vinccool96.observable.beans.value.ObservableIntValue
 * @see WritableIntValue
 * @see ReadOnlyIntProperty
 * @see Property
 */
@Suppress("UNCHECKED_CAST")
expect abstract class IntProperty() : ReadOnlyIntProperty, Property<Number?>, WritableIntValue {

    override var value: Number?

    override fun bindBidirectional(other: Property<Number?>)

    override fun unbindBidirectional(other: Property<Number?>)

    /**
     * Returns a string representation of this `IntProperty` object.
     *
     * @return a string representation of this `IntProperty` object.
     */
    override fun toString(): String

    /**
     * Creates an [ObjectProperty] that bidirectionally bound to this `IntProperty`. If the value of this
     * `IntProperty` changes, the value of the `ObjectProperty` will be updated automatically and vice-versa.
     *
     * Can be used for binding an ObjectProperty to IntProperty.
     *
     * ```
     * val intProperty: IntProperty = SimpleIntProperty(1)
     * val objectProperty: ObjectProperty<Int> = SimpleObjectProperty(2)
     *
     * objectProperty.bind(intProperty.asObject())
     * ```
     *
     * @return the new `ObjectProperty`
     */
    override fun asObject(): ObjectProperty<Int>

    companion object {

        /**
         * Returns a `IntProperty` that wraps a [Property]. If the `Property` is already a `IntProperty`, it
         * will be returned. Otherwise, a new `IntProperty` is created that is bound to the `Property`.
         *
         * This is very useful when bidirectionally binding an ObjectProperty<Int> and an IntProperty.
         * ```
         * val intProperty: IntProperty = SimpleIntProperty(1)
         * val objectProperty: ObjectProperty<Int> = SimpleObjectProperty(2)
         *
         * // Need to keep the reference as bidirectional binding uses weak references
         * val objectAsInt: IntProperty = IntProperty.intProperty(objectProperty)
         *
         * intProperty.bindBidirectional(objectAsInt)
         * ```
         *
         * Another approach is to convert the LongProperty to ObjectProperty using [asObject] method.
         *
         * @param property The source `Property`
         *
         * @return A `IntProperty` that wraps the `Property` if necessary
         */
        fun intProperty(property: Property<Int?>): IntProperty

    }

}