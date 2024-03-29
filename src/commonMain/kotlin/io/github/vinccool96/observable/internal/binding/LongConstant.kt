package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.beans.value.ChangeListener
import io.github.vinccool96.observable.beans.value.ObservableLongValue

internal class LongConstant private constructor(override val value: Long) : ObservableLongValue {

    override fun get(): Long {
        return this.value
    }

    override fun addListener(listener: InvalidationListener) {
        // no-op
    }

    override fun removeListener(listener: InvalidationListener) {
        // no-op
    }

    override fun hasListener(listener: InvalidationListener): Boolean {
        // no-op
        return false
    }

    override fun addListener(listener: ChangeListener<in Number?>) {
        // no-op
    }

    override fun removeListener(listener: ChangeListener<in Number?>) {
        // no-op
    }

    override fun hasListener(listener: ChangeListener<in Number?>): Boolean {
        // no-op
        return false
    }

    override val intValue: Int
        get() = this.value.toInt()

    override val longValue: Long
        get() = this.value

    override val floatValue: Float
        get() = this.value.toFloat()

    override val doubleValue: Double
        get() = this.value.toDouble()

    override val shortValue: Short
        get() = this.value.toShort()

    override val byteValue: Byte
        get() = this.value.toByte()

    companion object {

        fun valueOf(value: Long): LongConstant {
            return LongConstant(value)
        }

    }

}