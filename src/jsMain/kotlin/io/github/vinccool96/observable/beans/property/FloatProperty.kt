package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.binding.Bindings
import io.github.vinccool96.observable.beans.value.WritableFloatValue
import io.github.vinccool96.observable.internal.binding.BidirectionalBinding
import io.github.vinccool96.observable.internal.binding.Logging

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
actual abstract class FloatProperty : ReadOnlyFloatProperty(), Property<Number?>, WritableFloatValue {

    actual override var value: Number?
        get() = super.value
        set(value) {
            if (value == null) {
                Logging.logger.info("Attempt to set float property to null, using default value instead.",
                        NullPointerException())
            }
            this.set(value?.toFloat() ?: 0.0f)
        }

    actual override fun bindBidirectional(other: Property<Number?>) {
        Bindings.bindBidirectional(this, other)
    }

    actual override fun unbindBidirectional(other: Property<Number?>) {
        Bindings.unbindBidirectional(this, other)
    }

    /**
     * Returns a string representation of this `FloatProperty` object.
     *
     * @return a string representation of this `FloatProperty` object.
     */
    actual override fun toString(): String {
        val bean = this.bean
        val name = this.name
        val result = StringBuilder("FloatProperty [")
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ")
        }
        if (name != null && name.isNotEmpty()) {
            result.append("name: ").append(name).append(", ")
        }
        val v: Float = get()
        result.append("value: ").append(if (v == 0.0f) "0.0" else "$v").append("]")
        return result.toString()
    }

    /**
     * Creates an [ObjectProperty] that bidirectionally bound to this `FloatProperty`. If the value of this
     * `FloatProperty` changes, the value of the `ObjectProperty` will be updated automatically and vice-versa.
     *
     * Can be used for binding an ObjectProperty to FloatProperty.
     *
     * ```
     * val floatProperty: FloatProperty = SimpleFloatProperty(0.0f)
     * val objectProperty: ObjectProperty<Float> = SimpleObjectProperty0.0f
     *
     * objectProperty.bind(floatProperty.asObject())
     * ```
     *
     * @return the new `ObjectProperty`
     */
    actual override fun asObject(): ObjectProperty<Float> {
        return object : ObjectPropertyBase<Float>(this@FloatProperty.floatValue) {

            init {
                BidirectionalBinding.bind(this as Property<Number?>, this@FloatProperty)
            }

            override val bean: Any?
                get() = null // Virtual property, does not exist on a bean

            override val name: String?
                get() = this@FloatProperty.name

            protected fun finalize() {
                try {
                    BidirectionalBinding.unbind(this, this@FloatProperty)
                } finally {
                }
            }

        }
    }

    actual companion object {

        /**
         * Returns a `FloatProperty` that wraps a [Property]. If the `Property` is already a `FloatProperty`, it
         * will be returned. Otherwise, a new `FloatProperty` is created that is bound to the `Property`.
         *
         * This is very useful when bidirectionally binding an ObjectProperty<Float> and an FloatProperty.
         * ```
         * val floatProperty: FloatProperty = SimpleFloatProperty(0.0f)
         * val objectProperty: ObjectProperty<Float> = SimpleObjectProperty(0.0f)
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
        actual fun floatProperty(property: Property<Float?>): FloatProperty {
            return if (property is FloatProperty) property else object : FloatPropertyBase() {

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