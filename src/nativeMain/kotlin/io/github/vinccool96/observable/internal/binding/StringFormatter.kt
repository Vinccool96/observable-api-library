package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.Observable
import io.github.vinccool96.observable.beans.binding.StringBinding
import io.github.vinccool96.observable.beans.binding.StringExpression
import io.github.vinccool96.observable.beans.value.ObservableValue
import io.github.vinccool96.observable.collections.ObservableCollections
import io.github.vinccool96.observable.collections.ObservableList
import io.github.vinccool96.observable.dev.ReturnsUnmodifiableCollection
import io.github.vinccool96.observable.internal.utils.ArrayUtils

internal actual abstract class StringFormatter : StringBinding() {

    private class ConvertStringBinding(private val observableValue: ObservableValue<*>) : StringBinding() {

        init {
            bind(observableValue)
        }

        override fun dispose() {
            unbind(observableValue)
        }

        override fun computeValue(): String {
            val value: Any? = observableValue.value
            return if ((value == null)) "null" else value.toString()
        }

        @get:ReturnsUnmodifiableCollection
        override val dependencies: ObservableList<Observable>
            get() = ObservableCollections.singletonObservableList(observableValue)

    }

    private class ConcatStringFormatter(private val dep: Array<ObservableValue<*>>, private val args: Array<Any>) :
            StringFormatter() {

        init {
            super.bind(*dep)
        }

        override fun dispose() {
            super.unbind(*dep)
        }

        override fun computeValue(): String {
            val builder = StringBuilder()
            for (obj: Any in args) {
                builder.append(extractValue(obj))
            }
            return builder.toString()
        }

        @get:ReturnsUnmodifiableCollection
        override val dependencies: ObservableList<Observable>
            get() {
                return ObservableCollections.unmodifiableObservableList(ObservableCollections.observableArrayList(*dep))
            }
    }

    actual companion object {

        internal fun extractValue(obj: Any): Any? {
            return if (ObservableValue::class.isInstance(obj)) (obj as ObservableValue<*>).value else obj
        }

        fun extractValues(objs: Array<Any>): Array<Any> {
            val n: Int = objs.size
            val values: Array<Any?> = arrayOfNulls(n)
            for (i in 0 until n) {
                values[i] = extractValue(objs[i])
            }
            return ArrayUtils.copyOfNotNulls(values)
        }

        actual fun extractDependencies(vararg args: Any?): Array<ObservableValue<*>> {
            val dependencies: MutableList<ObservableValue<*>> = ArrayList()
            for (obj: Any? in args) {
                if (obj is ObservableValue<*>) {
                    dependencies.add(obj)
                }
            }
            return dependencies.toTypedArray()
        }

        actual fun convert(observableValue: ObservableValue<*>): StringExpression {
            return if (observableValue is StringExpression) {
                observableValue
            } else {
                ConvertStringBinding(observableValue)
            }
        }

        @Suppress("UNCHECKED_CAST")
        actual fun concat(vararg args: Any?): StringExpression {
            if (args.isEmpty()) {
                return StringConstant.valueOf("")
            }
            if (args.size == 1) {
                val cur: Any? = args[0]
                return if (cur is ObservableValue<*>) convert(cur) else StringConstant.valueOf(cur.toString())
            }
            if (extractDependencies(*args).isEmpty()) {
                val builder = StringBuilder()
                for (obj: Any? in args) {
                    builder.append(obj)
                }
                return StringConstant.valueOf(builder.toString())
            }
            val dependencies = extractDependencies(*args)
            return ConcatStringFormatter(dependencies, args as Array<Any>)
        }

        actual fun format(stringMaker: () -> String, vararg args: Any): StringExpression {
            if (extractDependencies(*args).isEmpty()) {
                return StringConstant.valueOf(stringMaker())
            }
            val formatter: StringFormatter = object : StringFormatter() {

                init {
                    super.bind(*extractDependencies(*args))
                }

                override fun dispose() {
                    super.unbind(*extractDependencies(*args))
                }

                override fun computeValue(): String {
                    return stringMaker()
                }

                @get:ReturnsUnmodifiableCollection
                override val dependencies: ObservableList<Observable>
                    get() = ObservableCollections.unmodifiableObservableList(
                            ObservableCollections.observableArrayList(*extractDependencies(*args)))

            }
            // Force calculation to check format
            formatter.get()
            return formatter
        }

    }

}