package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.value.ChangeListener
import io.github.vinccool96.observable.beans.value.ObservableValue
import java.lang.reflect.Field

@Suppress("KotlinConstantConditions", "UNCHECKED_CAST")
object ExpressionHelperUtility {

    fun <T> getChangeListeners(observable: ObservableValue<T>): List<ChangeListener<in T>> {
        val helperAny = getExpressionHelper(observable)
        return if (helperAny == null) {
            emptyList()
        } else {
            val helper: ExpressionHelper<T> = helperAny as ExpressionHelper<T>
            helper.changeListeners.asList()
        }
    }

    private fun getExpressionHelper(bean: Any): Any? {
        var clazz: Class<*> = bean::class.java
        while (clazz != Any::class) {
            try {
                val field: Field = clazz.getDeclaredField("helper")
                field.isAccessible = true
                val helper = field[bean]
                field.isAccessible = false
                return helper
            } catch (_: Exception) {
            }
            clazz = clazz.superclass
        }
        return null
    }

}