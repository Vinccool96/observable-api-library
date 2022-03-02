package io.github.vinccool96.observable.beans.binding

import io.github.vinccool96.logging.LogLevel
import io.github.vinccool96.observable.beans.Observable
import io.github.vinccool96.observable.beans.property.*
import io.github.vinccool96.observable.dev.Callable
import io.github.vinccool96.observable.internal.binding.ErrorLoggingUtility
import kotlin.math.E
import kotlin.math.PI
import kotlin.native.concurrent.ThreadLocal
import kotlin.test.*

@Suppress("UNCHECKED_CAST")
class BindingsCreateBindingTest {

    private lateinit var p0: Property<out Any?>

    private lateinit var p1: Property<out Any?>

    private lateinit var f: Functions<out Any?>

    private var value0: Any? = null

    private var value1: Any? = null

    private var defaultValue: Any? = null

    @BeforeTest
    fun setUp() {
        log.start()
    }

    @AfterTest
    fun tearDown() {
        log.stop()
    }

    private fun prepareBoolean() {
        this.p0 = SimpleBooleanProperty()
        this.p1 = SimpleBooleanProperty()
        this.f = object : Functions<Boolean?> {

            override fun create(func: Callable<Boolean?>, vararg dependencies: Observable): Binding<Boolean?> {
                return Bindings.createBooleanBinding(func, *dependencies)
            }

            override fun check(value0: Boolean?, value1: Any?) {
                assertEquals(value0, value1)
            }

        }
        this.value0 = true
        this.value1 = false
        this.defaultValue = false
    }

    @Test
    fun testNoDependencies_Boolean() {
        prepareBoolean()
        val v0 = this.value0 as Boolean?
        val f = this.f as Functions<Boolean?>
        val dV = this.defaultValue as Boolean?

        val func0: Callable<Boolean?> = Callable { v0 }
        val binding0: Binding<Boolean?> = f.create(func0)

        f.check(v0, binding0.value)
        assertTrue(binding0.dependencies.isEmpty())
        binding0.dispose()

        // func throws exception, dependencies set to empty array
        val func1: Callable<Boolean?> = Callable { throw Exception() }
        val binding1: Binding<Boolean?> = f.create(func1)

        f.check(dV, if (binding1 !is ObjectBinding) binding1.value else binding1.get())
        log.check(LogLevel.WARNING, Exception::class)
        assertTrue(binding1.dependencies.isEmpty())
        binding1.dispose()
    }

    @Test
    fun testOneDependency_Boolean() {
        prepareBoolean()
        val p0 = this.p0 as Property<Boolean?>
        val f = this.f as Functions<Boolean?>
        val v1 = this.value1 as Boolean?

        val func: Callable<Boolean?> = Callable { p0.value }
        val binding = f.create(func, p0)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertEquals<List<*>>(binding.dependencies, listOf(this.p0))
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    @Test
    fun testCreateBoolean_TwoDependencies_Boolean() {
        prepareBoolean()
        val p0 = this.p0 as Property<Boolean?>
        val p1 = this.p1 as Property<Boolean?>
        val f = this.f as Functions<Boolean?>
        val v1 = this.value1 as Boolean?

        val func: Callable<Boolean?> = Callable { p0.value }
        val binding = f.create(func, p0, p1)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertContentEquals(listOf(p0, p1), binding.dependencies)
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    private fun prepareDouble() {
        this.p0 = SimpleDoubleProperty()
        this.p1 = SimpleDoubleProperty()
        this.f = object : Functions<Number?> {

            override fun create(func: Callable<Number?>, vararg dependencies: Observable): Binding<Number?> {
                return Bindings.createDoubleBinding(func as Callable<Double?>, *dependencies)
            }

            override fun check(value0: Number?, value1: Any?) {
                assertEquals(value0!!.toDouble(), (value1 as Number).toDouble(), EPSILON_DOUBLE)
            }

        }
        this.value0 = PI
        this.value1 = -E
        this.defaultValue = 0.0
    }

    @Test
    fun testNoDependencies_Double() {
        prepareDouble()
        val v0 = this.value0 as Double?
        val f = this.f as Functions<Double?>
        val dV = this.defaultValue as Double?

        val func0: Callable<Double?> = Callable { v0 }
        val binding0: Binding<Double?> = f.create(func0)

        f.check(v0, binding0.value)
        assertTrue(binding0.dependencies.isEmpty())
        binding0.dispose()

        // func throws exception, dependencies set to empty array
        val func1: Callable<Double?> = Callable { throw Exception() }
        val binding1: Binding<Double?> = f.create(func1)

        f.check(dV, if (binding1 !is ObjectBinding) binding1.value else binding1.get())
        log.check(LogLevel.WARNING, Exception::class)
        assertTrue(binding1.dependencies.isEmpty())
        binding1.dispose()
    }

    @Test
    fun testOneDependency_Double() {
        prepareDouble()
        val p0 = this.p0 as Property<Double?>
        val f = this.f as Functions<Double?>
        val v1 = this.value1 as Double?

        val func: Callable<Double?> = Callable { p0.value }
        val binding = f.create(func, p0)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertEquals<List<*>>(binding.dependencies, listOf(this.p0))
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    @Test
    fun testCreateBoolean_TwoDependencies_Double() {
        prepareDouble()
        val p0 = this.p0 as Property<Double?>
        val p1 = this.p1 as Property<Double?>
        val f = this.f as Functions<Double?>
        val v1 = this.value1 as Double?

        val func: Callable<Double?> = Callable { p0.value }
        val binding = f.create(func, p0, p1)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertContentEquals(listOf(p0, p1), binding.dependencies)
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    private fun prepareFloat() {
        this.p0 = SimpleFloatProperty()
        this.p1 = SimpleFloatProperty()
        this.f = object : Functions<Number?> {

            override fun create(func: Callable<Number?>, vararg dependencies: Observable): Binding<Number?> {
                return Bindings.createFloatBinding(func as Callable<Float?>, *dependencies)
            }

            override fun check(value0: Number?, value1: Any?) {
                assertEquals(value0!!.toFloat(), (value1 as Number).toFloat(), EPSILON_FLOAT)
            }

        }
        this.value0 = PI.toFloat()
        this.value1 = -E.toFloat()
        this.defaultValue = 0.0f
    }

    @Test
    fun testNoDependencies_Float() {
        prepareFloat()
        val v0 = this.value0 as Float?
        val f = this.f as Functions<Float?>
        val dV = this.defaultValue as Float?

        val func0: Callable<Float?> = Callable { v0 }
        val binding0: Binding<Float?> = f.create(func0)

        f.check(v0, binding0.value)
        assertTrue(binding0.dependencies.isEmpty())
        binding0.dispose()

        // func throws exception, dependencies set to empty array
        val func1: Callable<Float?> = Callable { throw Exception() }
        val binding1: Binding<Float?> = f.create(func1)

        f.check(dV, if (binding1 !is ObjectBinding) binding1.value else binding1.get())
        log.check(LogLevel.WARNING, Exception::class)
        assertTrue(binding1.dependencies.isEmpty())
        binding1.dispose()
    }

    @Test
    fun testOneDependency_Float() {
        prepareFloat()
        val p0 = this.p0 as Property<Float?>
        val f = this.f as Functions<Float?>
        val v1 = this.value1 as Float?

        val func: Callable<Float?> = Callable { p0.value }
        val binding = f.create(func, p0)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertEquals<List<*>>(binding.dependencies, listOf(this.p0))
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    @Test
    fun testCreateBoolean_TwoDependencies_Float() {
        prepareFloat()
        val p0 = this.p0 as Property<Float?>
        val p1 = this.p1 as Property<Float?>
        val f = this.f as Functions<Float?>
        val v1 = this.value1 as Float?

        val func: Callable<Float?> = Callable { p0.value }
        val binding = f.create(func, p0, p1)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertContentEquals(listOf(p0, p1), binding.dependencies)
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    private fun prepareInt() {
        this.p0 = SimpleIntProperty()
        this.p1 = SimpleIntProperty()
        this.f = object : Functions<Number?> {

            override fun create(func: Callable<Number?>, vararg dependencies: Observable): Binding<Number?> {
                return Bindings.createIntBinding(func as Callable<Int?>, *dependencies)
            }

            override fun check(value0: Number?, value1: Any?) {
                assertEquals(value0!!.toInt(), (value1 as Number).toInt())
            }

        }
        this.value0 = Int.MAX_VALUE
        this.value1 = Int.MIN_VALUE
        this.defaultValue = 0
    }

    @Test
    fun testNoDependencies_Int() {
        prepareInt()
        val v0 = this.value0 as Int?
        val f = this.f as Functions<Int?>
        val dV = this.defaultValue as Int?

        val func0: Callable<Int?> = Callable { v0 }
        val binding0: Binding<Int?> = f.create(func0)

        f.check(v0, binding0.value)
        assertTrue(binding0.dependencies.isEmpty())
        binding0.dispose()

        // func throws exception, dependencies set to empty array
        val func1: Callable<Int?> = Callable { throw Exception() }
        val binding1: Binding<Int?> = f.create(func1)

        f.check(dV, if (binding1 !is ObjectBinding) binding1.value else binding1.get())
        log.check(LogLevel.WARNING, Exception::class)
        assertTrue(binding1.dependencies.isEmpty())
        binding1.dispose()
    }

    @Test
    fun testOneDependency_Int() {
        prepareInt()
        val p0 = this.p0 as Property<Int?>
        val f = this.f as Functions<Int?>
        val v1 = this.value1 as Int?

        val func: Callable<Int?> = Callable { p0.value }
        val binding = f.create(func, p0)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertEquals<List<*>>(binding.dependencies, listOf(this.p0))
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    @Test
    fun testCreateBoolean_TwoDependencies_Int() {
        prepareInt()
        val p0 = this.p0 as Property<Int?>
        val p1 = this.p1 as Property<Int?>
        val f = this.f as Functions<Int?>
        val v1 = this.value1 as Int?

        val func: Callable<Int?> = Callable { p0.value }
        val binding = f.create(func, p0, p1)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertContentEquals(listOf(p0, p1), binding.dependencies)
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    private fun prepareLong() {
        this.p0 = SimpleLongProperty()
        this.p1 = SimpleLongProperty()
        this.f = object : Functions<Number?> {

            override fun create(func: Callable<Number?>, vararg dependencies: Observable): Binding<Number?> {
                return Bindings.createLongBinding(func as Callable<Long?>, *dependencies)
            }

            override fun check(value0: Number?, value1: Any?) {
                assertEquals(value0!!.toLong(), (value1 as Number).toLong())
            }

        }
        this.value0 = Long.MAX_VALUE
        this.value1 = Long.MIN_VALUE
        this.defaultValue = 0L
    }

    @Test
    fun testNoDependencies_Long() {
        prepareLong()
        val v0 = this.value0 as Long?
        val f = this.f as Functions<Long?>
        val dV = this.defaultValue as Long?

        val func0: Callable<Long?> = Callable { v0 }
        val binding0: Binding<Long?> = f.create(func0)

        f.check(v0, binding0.value)
        assertTrue(binding0.dependencies.isEmpty())
        binding0.dispose()

        // func throws exception, dependencies set to empty array
        val func1: Callable<Long?> = Callable { throw Exception() }
        val binding1: Binding<Long?> = f.create(func1)

        f.check(dV, if (binding1 !is ObjectBinding) binding1.value else binding1.get())
        log.check(LogLevel.WARNING, Exception::class)
        assertTrue(binding1.dependencies.isEmpty())
        binding1.dispose()
    }

    @Test
    fun testOneDependency_Long() {
        prepareLong()
        val p0 = this.p0 as Property<Long?>
        val f = this.f as Functions<Long?>
        val v1 = this.value1 as Long?

        val func: Callable<Long?> = Callable { p0.value }
        val binding = f.create(func, p0)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertEquals<List<*>>(binding.dependencies, listOf(this.p0))
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    @Test
    fun testCreateBoolean_TwoDependencies_Long() {
        prepareLong()
        val p0 = this.p0 as Property<Long?>
        val p1 = this.p1 as Property<Long?>
        val f = this.f as Functions<Long?>
        val v1 = this.value1 as Long?

        val func: Callable<Long?> = Callable { p0.value }
        val binding = f.create(func, p0, p1)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertContentEquals(listOf(p0, p1), binding.dependencies)
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    private fun prepareShort() {
        this.p0 = SimpleShortProperty()
        this.p1 = SimpleShortProperty()
        this.f = object : Functions<Number?> {

            override fun create(func: Callable<Number?>, vararg dependencies: Observable): Binding<Number?> {
                return Bindings.createShortBinding(func as Callable<Short?>, *dependencies)
            }

            override fun check(value0: Number?, value1: Any?) {
                assertEquals(value0!!.toShort(), (value1 as Number).toShort())
            }

        }
        this.value0 = Short.MAX_VALUE
        this.value1 = Short.MIN_VALUE
        this.defaultValue = (0).toShort()
    }

    @Test
    fun testNoDependencies_Short() {
        prepareShort()
        val v0 = this.value0 as Short?
        val f = this.f as Functions<Short?>
        val dV = this.defaultValue as Short?

        val func0: Callable<Short?> = Callable { v0 }
        val binding0: Binding<Short?> = f.create(func0)

        f.check(v0, binding0.value)
        assertTrue(binding0.dependencies.isEmpty())
        binding0.dispose()

        // func throws exception, dependencies set to empty array
        val func1: Callable<Short?> = Callable { throw Exception() }
        val binding1: Binding<Short?> = f.create(func1)

        f.check(dV, if (binding1 !is ObjectBinding) binding1.value else binding1.get())
        log.check(LogLevel.WARNING, Exception::class)
        assertTrue(binding1.dependencies.isEmpty())
        binding1.dispose()
    }

    @Test
    fun testOneDependency_Short() {
        prepareShort()
        val p0 = this.p0 as Property<Short?>
        val f = this.f as Functions<Short?>
        val v1 = this.value1 as Short?

        val func: Callable<Short?> = Callable { p0.value }
        val binding = f.create(func, p0)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertEquals<List<*>>(binding.dependencies, listOf(this.p0))
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    @Test
    fun testCreateBoolean_TwoDependencies_Short() {
        prepareShort()
        val p0 = this.p0 as Property<Short?>
        val p1 = this.p1 as Property<Short?>
        val f = this.f as Functions<Short?>
        val v1 = this.value1 as Short?

        val func: Callable<Short?> = Callable { p0.value }
        val binding = f.create(func, p0, p1)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertContentEquals(listOf(p0, p1), binding.dependencies)
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    private fun prepareByte() {
        this.p0 = SimpleByteProperty()
        this.p1 = SimpleByteProperty()
        this.f = object : Functions<Number?> {

            override fun create(func: Callable<Number?>, vararg dependencies: Observable): Binding<Number?> {
                return Bindings.createByteBinding(func as Callable<Byte?>, *dependencies)
            }

            override fun check(value0: Number?, value1: Any?) {
                assertEquals(value0!!.toByte(), (value1 as Number).toByte())
            }

        }
        this.value0 = Byte.MAX_VALUE
        this.value1 = Byte.MIN_VALUE
        this.defaultValue = (0).toByte()
    }

    @Test
    fun testNoDependencies_Byte() {
        prepareByte()
        val v0 = this.value0 as Byte?
        val f = this.f as Functions<Byte?>
        val dV = this.defaultValue as Byte?

        val func0: Callable<Byte?> = Callable { v0 }
        val binding0: Binding<Byte?> = f.create(func0)

        f.check(v0, binding0.value)
        assertTrue(binding0.dependencies.isEmpty())
        binding0.dispose()

        // func throws exception, dependencies set to empty array
        val func1: Callable<Byte?> = Callable { throw Exception() }
        val binding1: Binding<Byte?> = f.create(func1)

        f.check(dV, if (binding1 !is ObjectBinding) binding1.value else binding1.get())
        log.check(LogLevel.WARNING, Exception::class)
        assertTrue(binding1.dependencies.isEmpty())
        binding1.dispose()
    }

    @Test
    fun testOneDependency_Byte() {
        prepareByte()
        val p0 = this.p0 as Property<Byte?>
        val f = this.f as Functions<Byte?>
        val v1 = this.value1 as Byte?

        val func: Callable<Byte?> = Callable { p0.value }
        val binding = f.create(func, p0)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertEquals<List<*>>(binding.dependencies, listOf(this.p0))
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    @Test
    fun testCreateBoolean_TwoDependencies_Byte() {
        prepareByte()
        val p0 = this.p0 as Property<Byte?>
        val p1 = this.p1 as Property<Byte?>
        val f = this.f as Functions<Byte?>
        val v1 = this.value1 as Byte?

        val func: Callable<Byte?> = Callable { p0.value }
        val binding = f.create(func, p0, p1)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertContentEquals(listOf(p0, p1), binding.dependencies)
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    private fun prepareObject() {
        this.p0 = SimpleObjectProperty(null)
        this.p1 = SimpleObjectProperty(null)
        this.f = object : Functions<Any?> {

            override fun create(func: Callable<Any?>, vararg dependencies: Observable): Binding<Any?> {
                return Bindings.createObjectBinding(func, null, *dependencies)
            }

            override fun check(value0: Any?, value1: Any?) {
                assertEquals(value0, value1)
            }

        }
        this.value0 = Any()
        this.value1 = Any()
        this.defaultValue = null
    }

    @Test
    fun testNoDependencies_Object() {
        prepareObject()
        val v0 = this.value0
        val f = this.f as Functions<Any?>
        val dV = this.defaultValue

        val func0: Callable<Any?> = Callable { v0 }
        val binding0: Binding<Any?> = f.create(func0)

        f.check(v0, binding0.value)
        assertTrue(binding0.dependencies.isEmpty())
        binding0.dispose()

        // func throws exception, dependencies set to empty array
        val func1: Callable<Any?> = Callable { throw Exception() }
        val binding1: Binding<Any?> = f.create(func1)

        f.check(dV, if (binding1 !is ObjectBinding) binding1.value else binding1.get())
        log.check(LogLevel.WARNING, Exception::class)
        assertTrue(binding1.dependencies.isEmpty())
        binding1.dispose()
    }

    @Test
    fun testOneDependency_Object() {
        prepareObject()
        val p0 = this.p0 as Property<Any?>
        val f = this.f as Functions<Any?>
        val v1 = this.value1

        val func: Callable<Any?> = Callable { p0.value }
        val binding = f.create(func, p0)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertEquals<List<*>>(binding.dependencies, listOf(this.p0))
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    @Test
    fun testCreateBoolean_TwoDependencies_Object() {
        prepareObject()
        val p0 = this.p0 as Property<Any?>
        val p1 = this.p1 as Property<Any?>
        val f = this.f as Functions<Any?>
        val v1 = this.value1

        val func: Callable<Any?> = Callable { p0.value }
        val binding = f.create(func, p0, p1)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertContentEquals(listOf(p0, p1), binding.dependencies)
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    private fun prepareString() {
        this.p0 = SimpleStringProperty()
        this.p1 = SimpleStringProperty()
        this.f = object : Functions<String?> {

            override fun create(func: Callable<String?>, vararg dependencies: Observable): Binding<String?> {
                return Bindings.createStringBinding(func, *dependencies)
            }

            override fun check(value0: String?, value1: Any?) {
                assertEquals(value0, value1)
            }

        }
        this.value0 = "Hello World"
        this.value1 = "Goodbye World"
        this.defaultValue = ""
    }

    @Test
    fun testNoDependencies_String() {
        prepareString()
        val v0 = this.value0 as String?
        val f = this.f as Functions<String?>
        val dV = this.defaultValue as String?

        val func0: Callable<String?> = Callable { v0 }
        val binding0: Binding<String?> = f.create(func0)

        f.check(v0, binding0.value)
        assertTrue(binding0.dependencies.isEmpty())
        binding0.dispose()

        // func throws exception, dependencies set to empty array
        val func1: Callable<String?> = Callable { throw Exception() }
        val binding1: Binding<String?> = f.create(func1)

        f.check(dV, if (binding1 !is ObjectBinding) binding1.value else binding1.get())
        log.check(LogLevel.WARNING, Exception::class)
        assertTrue(binding1.dependencies.isEmpty())
        binding1.dispose()
    }

    @Test
    fun testOneDependency_String() {
        prepareString()
        val p0 = this.p0 as Property<String?>
        val f = this.f as Functions<String?>
        val v1 = this.value1 as String?

        val func: Callable<String?> = Callable { p0.value }
        val binding = f.create(func, p0)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertEquals<List<*>>(binding.dependencies, listOf(this.p0))
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    @Test
    fun testCreateBoolean_TwoDependencies_String() {
        prepareString()
        val p0 = this.p0 as Property<String?>
        val p1 = this.p1 as Property<String?>
        val f = this.f as Functions<String?>
        val v1 = this.value1 as String?

        val func: Callable<String?> = Callable { p0.value }
        val binding = f.create(func, p0, p1)

        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        assertContentEquals(listOf(p0, p1), binding.dependencies)
        p0.value = v1
        f.check(p0.value, if (binding !is ObjectBinding) binding.value else binding.get())
        binding.dispose()
    }

    interface Functions<S> {

        fun create(func: Callable<S>, vararg dependencies: Observable): Binding<S>

        fun check(value0: S, value1: Any?)

    }

    @ThreadLocal
    companion object {

        private const val EPSILON_FLOAT: Float = 1e-5f

        private const val EPSILON_DOUBLE: Double = 1e-10

        private val log = ErrorLoggingUtility()

    }

}