package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.binding.StringBinding
import io.github.vinccool96.observable.beans.binding.StringExpression
import io.github.vinccool96.observable.beans.value.ObservableValue

internal expect abstract class StringFormatter : StringBinding {

    companion object {

        fun extractDependencies(vararg args: Any?): Array<ObservableValue<*>>

        fun convert(observableValue: ObservableValue<*>): StringExpression

        fun concat(vararg args: Any?): StringExpression

        fun format(stringMaker: () -> String, vararg args: Any): StringExpression

    }

}