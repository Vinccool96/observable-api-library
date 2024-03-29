package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.beans.binding.StringExpression
import io.github.vinccool96.observable.beans.value.ChangeListener

internal class StringConstant private constructor(override val value: String?) : StringExpression() {

    override fun get(): String? {
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

    override fun addListener(listener: ChangeListener<in String?>) {
        // no-op
    }

    override fun removeListener(listener: ChangeListener<in String?>) {
        // no-op
    }

    override fun hasListener(listener: ChangeListener<in String?>): Boolean {
        // no-op
        return false
    }

    companion object {

        fun valueOf(value: String?): StringConstant {
            return StringConstant(value)
        }

    }

}