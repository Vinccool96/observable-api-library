@file:Suppress("UNCHECKED_CAST", "ClassName", "unused")

package io.github.vinccool96.observable.beans.binding

import io.github.vinccool96.logging.LogLevel
import io.github.vinccool96.observable.beans.Observable
import io.github.vinccool96.observable.beans.property.*
import io.github.vinccool96.observable.collections.ObservableCollections
import io.github.vinccool96.observable.collections.ObservableList
import io.github.vinccool96.observable.dev.Callable
import io.github.vinccool96.observable.internal.binding.ErrorLoggingUtility
import kotlin.math.E
import kotlin.math.PI
import kotlin.native.concurrent.ThreadLocal
import kotlin.test.*

abstract class BindingsCreateBindingTest<T>(private val p0: Property<T>, private val p1: Property<T>,
        private val f: Functions<T>, private val value0: T, private val value1: T, private val defaultValue: T) {

    @BeforeTest
    fun setUp() {
        log.start()
    }

    @AfterTest
    fun tearDown() {
        log.stop()
    }

    @Test
    fun testNoDependencies() {
        val func0: Callable<T> = Callable { this.value0 }
        val binding0: Binding<T> = this.f.create(func0)

        this.f.check(this.value0, binding0.value)
        assertTrue(binding0.dependencies.isEmpty())
        binding0.dispose()

        // func throws exception, dependencies set to empty array
        val func1: Callable<T> = Callable { throw Exception() }
        val binding1: Binding<T> = this.f.create(func1)

        this.f.check(this.defaultValue, if (binding1 !is ObjectBinding) binding1.value else binding1.get())
        log.check(LogLevel.WARNING, Exception::class)
        assertTrue(binding1.dependencies.isEmpty())
        binding1.dispose()
    }

    @Test
    fun testOneDependency() {
        val func: Callable<T> = Callable { this.p0.value }
        val binding = this.f.create(func, this.p0)

        this.f.check(this.p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertEquals<List<*>>(binding.dependencies, listOf(this.p0))
        this.p0.value = this.value1
        this.f.check(this.p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    @Test
    fun testCreateBoolean_TwoDependencies() {
        val func: Callable<T> = Callable { this.p0.value }
        val binding = this.f.create(func, this.p0, this.p1)

        this.f.check(this.p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertTrue(binding.dependencies == listOf(this.p0, this.p1) || binding.dependencies == listOf(this.p1, this.p0))
        this.p0.value = this.value1
        this.f.check(this.p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    interface Functions<S> {

        fun create(func: Callable<S>, vararg dependencies: Observable): Binding<S>

        fun check(value0: S, value1: Any?)

    }

    @ThreadLocal
    companion object {

        private val log = ErrorLoggingUtility()

    }

}

class BindingsCreateBindingTest_Boolean : BindingsCreateBindingTest<Boolean?>(
        SimpleBooleanProperty(), SimpleBooleanProperty(),
        object : Functions<Boolean?> {

            override fun create(func: Callable<Boolean?>,
                    vararg dependencies: Observable): Binding<Boolean?> {
                return Bindings.createBooleanBinding(func, *dependencies)
            }

            override fun check(value0: Boolean?, value1: Any?) {
                assertEquals(value0, value1)
            }

        }, true, false, false)

class BindingsCreateBindingTest_Double : BindingsCreateBindingTest<Number?>(
        SimpleDoubleProperty(), SimpleDoubleProperty(),
        object : Functions<Number?> {

            override fun create(func: Callable<Number?>,
                    vararg dependencies: Observable): Binding<Number?> {
                return Bindings.createDoubleBinding(func as Callable<Double?>, *dependencies)
            }

            override fun check(value0: Number?, value1: Any?) {
                assertEquals(value0!!.toDouble(), (value1 as Number).toDouble(), 1e-10)
            }

        }, PI, -E, 0.0)

class BindingsCreateBindingTest_Float : BindingsCreateBindingTest<Number?>(
        SimpleFloatProperty(), SimpleFloatProperty(),
        object : Functions<Number?> {

            override fun create(func: Callable<Number?>,
                    vararg dependencies: Observable): Binding<Number?> {
                return Bindings.createFloatBinding(func as Callable<Float?>, *dependencies)
            }

            override fun check(value0: Number?, value1: Any?) {
                assertEquals(value0!!.toFloat(), (value1 as Number).toFloat(), 1e-5f)
            }

        }, PI.toFloat(), -E.toFloat(), 0.0f)

class BindingsCreateBindingTest_Int : BindingsCreateBindingTest<Number?>(
        SimpleIntProperty(), SimpleIntProperty(),
        object : Functions<Number?> {

            override fun create(func: Callable<Number?>,
                    vararg dependencies: Observable): Binding<Number?> {
                return Bindings.createIntBinding(func as Callable<Int?>, *dependencies)
            }

            override fun check(value0: Number?, value1: Any?) {
                assertEquals(value0!!.toInt(), (value1 as Number).toInt())
            }

        }, Int.MAX_VALUE, Int.MIN_VALUE, 0)

class BindingsCreateBindingTest_Long : BindingsCreateBindingTest<Number?>(
        SimpleLongProperty(), SimpleLongProperty(),
        object : Functions<Number?> {

            override fun create(func: Callable<Number?>,
                    vararg dependencies: Observable): Binding<Number?> {
                return Bindings.createLongBinding(func as Callable<Long?>, *dependencies)
            }

            override fun check(value0: Number?, value1: Any?) {
                assertEquals(value0!!.toLong(), (value1 as Number).toLong())
            }

        }, Long.MAX_VALUE, Long.MIN_VALUE, 0L)

class BindingsCreateBindingTest_Short : BindingsCreateBindingTest<Number?>(
        SimpleShortProperty(), SimpleShortProperty(),
        object : Functions<Number?> {

            override fun create(func: Callable<Number?>,
                    vararg dependencies: Observable): Binding<Number?> {
                return Bindings.createShortBinding(func as Callable<Short?>, *dependencies)
            }

            override fun check(value0: Number?, value1: Any?) {
                assertEquals(value0!!.toShort(), (value1 as Number).toShort())
            }

        }, Short.MAX_VALUE, Short.MIN_VALUE, (0).toShort())

class BindingsCreateBindingTest_Byte : BindingsCreateBindingTest<Number?>(
        SimpleByteProperty(), SimpleByteProperty(),
        object : Functions<Number?> {

            override fun create(func: Callable<Number?>,
                    vararg dependencies: Observable): Binding<Number?> {
                return Bindings.createByteBinding(func as Callable<Byte?>, *dependencies)
            }

            override fun check(value0: Number?, value1: Any?) {
                assertEquals(value0!!.toByte(), (value1 as Number).toByte())
            }

        }, Byte.MAX_VALUE, Byte.MIN_VALUE, (0).toByte())

class BindingsCreateBindingTest_Any : BindingsCreateBindingTest<Any?>(
        SimpleObjectProperty(null), SimpleObjectProperty(null),
        object : Functions<Any?> {

            override fun create(func: Callable<Any?>,
                    vararg dependencies: Observable): Binding<Any?> {
                return Bindings.createObjectBinding(func, null, *dependencies)
            }

            override fun check(value0: Any?, value1: Any?) {
                assertEquals(value0, value1)
            }

        }, Any(), Any(), null)

class BindingsCreateBindingTest_String : BindingsCreateBindingTest<String?>(
        SimpleStringProperty(), SimpleStringProperty(),
        object : Functions<String?> {

            override fun create(func: Callable<String?>,
                    vararg dependencies: Observable): Binding<String?> {
                return Bindings.createStringBinding(func, *dependencies)
            }

            override fun check(value0: String?, value1: Any?) {
                assertEquals(value0, value1)
            }

        }, "Hello World", "Goodbye World", "")

class BindingsCreateBindingTest_ObservableList : BindingsCreateBindingTest<ObservableList<String>?>(
        SimpleListProperty(), SimpleListProperty(),
        object : Functions<ObservableList<String>?> {

            override fun create(func: Callable<ObservableList<String>?>,
                    vararg dependencies: Observable): Binding<ObservableList<String>?> {
                return Bindings.createListBinding(func, *dependencies)
            }

            override fun check(value0: ObservableList<String>?, value1: Any?) {
                assertEquals(value0, value1)
            }

        }, ObservableCollections.observableArrayList("Hello World"),
        ObservableCollections.observableArrayList("Goodbye World"), null)

//class BindingsCreateBindingTest_ObservableMap : BindingsCreateBindingTest<ObservableMap<String, String>?>(
//        SimpleMapProperty<String, String>(), SimpleMapProperty<String, String>(),
//        object : Functions<ObservableMap<String, String>?> {
//
//            override fun create(func: Callable<ObservableMap<String, String>?>,
//                    vararg dependencies: Observable): Binding<ObservableMap<String, String>?> {
//                return Bindings.createMapBinding(func, *dependencies)
//            }
//
//            override fun check(value0: ObservableMap<String, String>?, value1: Any?) {
//                assertEquals(value0, value1)
//            }
//
//        }, ObservableCollections.observableHashMap("Hello World" to "Goodbye World"),
//        ObservableCollections.observableHashMap("foo" to "bar"), null)
//
//class BindingsCreateBindingTest_ObservableSet : BindingsCreateBindingTest<ObservableSet<String>?>(
//        SimpleSetProperty<String>(), SimpleSetProperty<String>(),
//        object : Functions<ObservableSet<String>?> {
//
//            override fun create(func: Callable<ObservableSet<String>?>,
//                    vararg dependencies: Observable): Binding<ObservableSet<String>?> {
//                return Bindings.createSetBinding(func, *dependencies)
//            }
//
//            override fun check(value0: ObservableSet<String>?, value1: Any?) {
//                assertEquals(value0, value1)
//            }
//
//        }, ObservableCollections.observableSet("Hello World"),
//        ObservableCollections.observableSet("Goodbye World"), null)
//
//class BindingsCreateBindingTest_ObservableArray : BindingsCreateBindingTest<ObservableArray<String>?>(
//        SimpleArrayProperty<String>(arrayOf()), SimpleArrayProperty<String>(arrayOf()),
//        object : Functions<ObservableArray<String>?> {
//
//            override fun create(func: Callable<ObservableArray<String>?>,
//                    vararg dependencies: Observable): Binding<ObservableArray<String>?> {
//                return Bindings.createArrayBinding(func, arrayOf(), *dependencies)
//            }
//
//            override fun check(value0: ObservableArray<String>?, value1: Any?) {
//                assertEquals(value0, value1)
//            }
//
//        }, ObservableCollections.observableObjectArray(arrayOf("foo"), "Hello World"),
//        ObservableCollections.observableObjectArray(arrayOf("bar"), "Goodbye World"), null)