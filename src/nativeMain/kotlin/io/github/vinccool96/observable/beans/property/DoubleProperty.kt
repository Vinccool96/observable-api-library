package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.binding.Bindings
import io.github.vinccool96.observable.beans.value.WritableDoubleValue
import io.github.vinccool96.observable.internal.binding.BidirectionalBinding
import io.github.vinccool96.observable.internal.binding.Logging

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
actual abstract class DoubleProperty : ReadOnlyDoubleProperty(), Property<Number?>, WritableDoubleValue {

    actual override var value: Number?
        get() = super.value
        set(value) {
            if (value == null) {
                Logging.logger.info("Attempt to set double property to null, using default value instead.",
                        NullPointerException())
            }
            this.set(value?.toDouble() ?: 0.0)
        }

    actual override fun bindBidirectional(other: Property<Number?>) {
        Bindings.bindBidirectional(this, other)
    }

    actual override fun unbindBidirectional(other: Property<Number?>) {
        Bindings.unbindBidirectional(this, other)
    }

    /**
     * Returns a string representation of this `DoubleProperty` object.
     *
     * @return a string representation of this `DoubleProperty` object.
     */
    actual override fun toString(): String {
        val bean = this.bean
        val name = this.name
        val result = StringBuilder("DoubleProperty [")
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ")
        }
        if (name != null && name.isNotEmpty()) {
            result.append("name: ").append(name).append(", ")
        }
        result.append("value: ").append(get()).append("]")
        return result.toString()
    }

    /**
     * Creates an [ObjectProperty] that bidirectionally bound to this `DoubleProperty`. If the value of this
     * `DoubleProperty` changes, the value of the `ObjectProperty` will be updated automatically and vice-versa.
     *
     * Can be used for binding an ObjectProperty to DoubleProperty.
     *
     * ```
     * val doubleProperty: DoubleProperty = SimpleDoubleProperty(0.0)
     * val objectProperty: ObjectProperty<Double> = SimpleObjectProperty0.0
     *
     * objectProperty.bind(doubleProperty.asObject())
     * ```
     *
     * @return the new `ObjectProperty`
     */
    actual override fun asObject(): ObjectProperty<Double> {
        return object : ObjectPropertyBase<Double>(this@DoubleProperty.doubleValue) {

            init {
                BidirectionalBinding.bind(this as Property<Number?>, this@DoubleProperty)
            }

            override val bean: Any?
                get() = null // Virtual property, does not exist on a bean

            override val name: String?
                get() = this@DoubleProperty.name

            protected fun finalize() {
                try {
                    BidirectionalBinding.unbind(this, this@DoubleProperty)
                } finally {
                }
            }

        }
    }

    actual companion object {

        /**
         * Returns a `DoubleProperty` that wraps a [Property]. If the `Property` is already a `DoubleProperty`, it
         * will be returned. Otherwise, a new `DoubleProperty` is created that is bound to the `Property`.
         *
         * This is very useful when bidirectionally binding an ObjectProperty<Double> and an DoubleProperty.
         * ```
         * val doubleProperty: DoubleProperty = SimpleDoubleProperty(0.0)
         * val objectProperty: ObjectProperty<Double> = SimpleObjectProperty(0.0)
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
        actual fun doubleProperty(property: Property<Double?>): DoubleProperty {
            return if (property is DoubleProperty) property else object : DoublePropertyBase() {

                init {
                    BidirectionalBinding.bind(this, property as Property<Number?>)
                }

                override val bean: Any?
                    get() = null // Virtual property, does not exist on a bean

                override val name: String?
                    get() = property.name

                protected fun finalize() {
                    try {
                        BidirectionalBinding.unbind(property, this)
                    } finally {
                    }
                }

            }
        }

    }

}