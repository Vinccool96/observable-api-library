@file:Suppress("UNCHECKED_CAST", "unused")

package io.github.vinccool96.observable.beans.binding

import io.github.vinccool96.observable.beans.InvalidationListenerMock
import io.github.vinccool96.observable.beans.property.*
import io.github.vinccool96.observable.beans.value.*
import kotlin.math.max
import kotlin.math.min
import kotlin.native.concurrent.ThreadLocal
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class BindingsNumberCalculationsTest<T>(private val op1: ObservableValue<T>,
        private val op2: ObservableValue<T>, private val func: Functions<T>, private val v: Array<T>) {

    private lateinit var observer: InvalidationListenerMock

    interface Functions<S> {

        fun generateExpressionExpression(op1: Any, op2: Any): Binding<in S>

        fun generateExpressionPrimitive(op1: Any, op2: S): Binding<in S>

        fun generatePrimitiveExpression(op1: S, op2: Any): Binding<in S>

        fun setOp1(value: S)

        fun setOp2(value: S)

        fun check(op1: S, op2: S, exp: ObservableValue<in S>)

    }

    @BeforeTest
    fun setUp() {
        this.func.setOp1(this.v[0])
        this.func.setOp2(this.v[1])
        this.observer = InvalidationListenerMock()
    }

    @Test
    fun test_Expression_Expression() {
        val binding: Binding<in T> = this.func.generateExpressionExpression(this.op1, this.op2) as Binding<T>
        binding.addListener(this.observer)

        // check initial value
        this.func.check(this.v[0], this.v[1], binding)
        DependencyUtils.checkDependencies(binding.dependencies, this.op1, this.op2)

        // change first operand
        this.observer.reset()
        this.func.setOp1(this.v[2])
        this.func.check(this.v[2], this.v[1], binding)
        this.observer.check(binding, 1)

        // change second operand
        this.func.setOp2(this.v[3])
        this.func.check(this.v[2], this.v[3], binding)
        this.observer.check(binding, 1)

        // change both operands
        this.func.setOp1(this.v[4])
        this.func.setOp2(this.v[5])
        this.func.check(this.v[4], this.v[5], binding)
        this.observer.check(binding, 1)
    }

    @Test
    fun test_Self() {
        // using same FloatValue twice
        val binding = this.func.generateExpressionExpression(this.op1, this.op1)
        binding.addListener(this.observer)

        // check initial value
        this.func.check(this.v[0], this.v[0], binding)

        // change value
        this.func.setOp1(this.v[6])
        this.func.check(this.v[6], this.v[6], binding)
        this.observer.check(binding, 1)
    }

    @Test
    fun test_Expression_Primitive() {
        val binding = this.func.generateExpressionPrimitive(this.op1, this.v[6])
        binding.addListener(this.observer)

        // check initial value
        this.func.check(this.v[0], this.v[6], binding)
        DependencyUtils.checkDependencies(binding.dependencies, this.op1)

        // change first operand
        this.observer.reset()
        this.func.setOp1(this.v[7])
        this.func.check(this.v[7], this.v[6], binding)
        this.observer.check(binding, 1)
    }

    @Test
    fun test_Primitive_Expression() {
        val binding = this.func.generatePrimitiveExpression(this.v[8], this.op1)
        binding.addListener(this.observer)

        // check initial value
        this.func.check(this.v[8], this.v[0], binding)
        DependencyUtils.checkDependencies(binding.dependencies, this.op1)

        // change first operand
        this.observer.reset()
        this.func.setOp1(this.v[9])
        this.func.check(this.v[8], this.v[9], binding)
        this.observer.check(binding, 1)
    }

    @ThreadLocal
    companion object {

        const val EPSILON_FLOAT: Float = 1e-5f

        const val EPSILON_DOUBLE: Double = 1e-10

        val float1: FloatProperty = SimpleFloatProperty()
        val float2: FloatProperty = SimpleFloatProperty()
        val floatData = arrayOf(-3592.9f, 234872.83f, 3897.274f, 3958.9387f, -8347.347f, 217.9028f,
                -8723.782f, 82.8274f, -12.23478f, 0.92874f)

        val double1: DoubleProperty = SimpleDoubleProperty()
        val double2: DoubleProperty = SimpleDoubleProperty()
        val doubleData = arrayOf(2348.2345, -92.214, -214.0214, -908.214, 67.124, 0.214, -214.987234,
                -89724.897234, 234.25, 8721.234)

        val int1: IntProperty = SimpleIntProperty()
        val int2: IntProperty = SimpleIntProperty()
        val intData = arrayOf(248, -9384, -234, -34, -450809, 342345, 23789, -89234, -13134, 23134879)

        val long1: LongProperty = SimpleLongProperty()
        val long2: LongProperty = SimpleLongProperty()
        val longData = arrayOf(9823984L, 2908934L, -234234L, 9089234L, 132323L, -89324L, -8923442L, 78234L,
                -233487L, 988998L)

        val short1: ShortProperty = SimpleShortProperty()
        val short2: ShortProperty = SimpleShortProperty()
        val shortData = arrayOf<Short>(15, -586, -14, -2, -28175, 21396, 1486, -5577, -820, 420)

        val byte1: ByteProperty = SimpleByteProperty()
        val byte2: ByteProperty = SimpleByteProperty()
        val byteData = arrayOf<Byte>(15, -56, -14, -2, -28, 96, 86, -77, -20, 42)

    }

}

// double
class BindingsNumberCalculationsTest0 : BindingsNumberCalculationsTest<Double>(
        double1 as ObservableValue<Double>, double2 as ObservableValue<Double>,
        object : Functions<Double> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.add(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Double): Binding<Number?> {
                return Bindings.add(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Double, op2: Any): Binding<Number?> {
                return Bindings.add(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Double) {
                double1.set(value)
            }

            override fun setOp2(value: Double) {
                double2.set(value)
            }

            override fun check(op1: Double, op2: Double, exp: ObservableValue<in Double>) {
                assertEquals(op1 + op2, (exp as ObservableDoubleValue).get(), EPSILON_DOUBLE)
            }

        },
        doubleData)

class BindingsNumberCalculationsTest1 : BindingsNumberCalculationsTest<Double>(
        double1 as ObservableValue<Double>, double2 as ObservableValue<Double>,
        object : Functions<Double> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.subtract(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Double): Binding<Number?> {
                return Bindings.subtract(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Double, op2: Any): Binding<Number?> {
                return Bindings.subtract(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Double) {
                double1.set(value)
            }

            override fun setOp2(value: Double) {
                double2.set(value)
            }

            override fun check(op1: Double, op2: Double, exp: ObservableValue<in Double>) {
                assertEquals(op1 - op2, (exp as ObservableDoubleValue).get(), EPSILON_DOUBLE)
            }

        },
        doubleData)

class BindingsNumberCalculationsTest2 : BindingsNumberCalculationsTest<Double>(
        double1 as ObservableValue<Double>, double2 as ObservableValue<Double>,
        object : Functions<Double> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.multiply(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Double): Binding<Number?> {
                return Bindings.multiply(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Double, op2: Any): Binding<Number?> {
                return Bindings.multiply(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Double) {
                double1.set(value)
            }

            override fun setOp2(value: Double) {
                double2.set(value)
            }

            override fun check(op1: Double, op2: Double, exp: ObservableValue<in Double>) {
                assertEquals(op1 * op2, (exp as ObservableDoubleValue).get(), EPSILON_DOUBLE)
            }

        },
        doubleData)

class BindingsNumberCalculationsTest3 : BindingsNumberCalculationsTest<Double>(
        double1 as ObservableValue<Double>, double2 as ObservableValue<Double>,
        object : Functions<Double> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.divide(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Double): Binding<Number?> {
                return Bindings.divide(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Double, op2: Any): Binding<Number?> {
                return Bindings.divide(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Double) {
                double1.set(value)
            }

            override fun setOp2(value: Double) {
                double2.set(value)
            }

            override fun check(op1: Double, op2: Double, exp: ObservableValue<in Double>) {
                assertEquals(op1 / op2, (exp as ObservableDoubleValue).get(), EPSILON_DOUBLE)
            }

        },
        doubleData)

class BindingsNumberCalculationsTest4 : BindingsNumberCalculationsTest<Double>(
        double1 as ObservableValue<Double>, double2 as ObservableValue<Double>,
        object : Functions<Double> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.min(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Double): Binding<Number?> {
                return Bindings.min(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Double, op2: Any): Binding<Number?> {
                return Bindings.min(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Double) {
                double1.set(value)
            }

            override fun setOp2(value: Double) {
                double2.set(value)
            }

            override fun check(op1: Double, op2: Double, exp: ObservableValue<in Double>) {
                assertEquals(min(op1, op2), (exp as ObservableDoubleValue).get(), EPSILON_DOUBLE)
            }

        },
        doubleData)

class BindingsNumberCalculationsTest5 : BindingsNumberCalculationsTest<Double>(
        double1 as ObservableValue<Double>, double2 as ObservableValue<Double>,
        object : Functions<Double> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.max(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Double): Binding<Number?> {
                return Bindings.max(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Double, op2: Any): Binding<Number?> {
                return Bindings.max(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Double) {
                double1.set(value)
            }

            override fun setOp2(value: Double) {
                double2.set(value)
            }

            override fun check(op1: Double, op2: Double, exp: ObservableValue<in Double>) {
                assertEquals(max(op1, op2), (exp as ObservableDoubleValue).get(), EPSILON_DOUBLE)
            }

        },
        doubleData)

// float
class BindingsNumberCalculationsTest6 : BindingsNumberCalculationsTest<Float>(
        float1 as ObservableValue<Float>, float2 as ObservableValue<Float>,
        object : Functions<Float> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.add(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Float): Binding<Number?> {
                return Bindings.add(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Float, op2: Any): Binding<Number?> {
                return Bindings.add(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Float) {
                float1.set(value)
            }

            override fun setOp2(value: Float) {
                float2.set(value)
            }

            override fun check(op1: Float, op2: Float, exp: ObservableValue<in Float>) {
                assertEquals(op1 + op2, (exp as ObservableFloatValue).get(), EPSILON_FLOAT)
            }

        },
        floatData)

class BindingsNumberCalculationsTest7 : BindingsNumberCalculationsTest<Float>(
        float1 as ObservableValue<Float>, float2 as ObservableValue<Float>,
        object : Functions<Float> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.subtract(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Float): Binding<Number?> {
                return Bindings.subtract(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Float, op2: Any): Binding<Number?> {
                return Bindings.subtract(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Float) {
                float1.set(value)
            }

            override fun setOp2(value: Float) {
                float2.set(value)
            }

            override fun check(op1: Float, op2: Float, exp: ObservableValue<in Float>) {
                assertEquals(op1 - op2, (exp as ObservableFloatValue).get(), EPSILON_FLOAT)
            }

        },
        floatData)

class BindingsNumberCalculationsTest8 : BindingsNumberCalculationsTest<Float>(
        float1 as ObservableValue<Float>, float2 as ObservableValue<Float>,
        object : Functions<Float> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.multiply(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Float): Binding<Number?> {
                return Bindings.multiply(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Float, op2: Any): Binding<Number?> {
                return Bindings.multiply(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Float) {
                float1.set(value)
            }

            override fun setOp2(value: Float) {
                float2.set(value)
            }

            override fun check(op1: Float, op2: Float, exp: ObservableValue<in Float>) {
                assertEquals(op1 * op2, (exp as ObservableFloatValue).get(), EPSILON_FLOAT)
            }

        },
        floatData)

class BindingsNumberCalculationsTest9 : BindingsNumberCalculationsTest<Float>(
        float1 as ObservableValue<Float>, float2 as ObservableValue<Float>,
        object : Functions<Float> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.divide(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Float): Binding<Number?> {
                return Bindings.divide(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Float, op2: Any): Binding<Number?> {
                return Bindings.divide(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Float) {
                float1.set(value)
            }

            override fun setOp2(value: Float) {
                float2.set(value)
            }

            override fun check(op1: Float, op2: Float, exp: ObservableValue<in Float>) {
                assertEquals(op1 / op2, (exp as ObservableFloatValue).get(), EPSILON_FLOAT)
            }

        },
        floatData)

class BindingsNumberCalculationsTest10 : BindingsNumberCalculationsTest<Float>(
        float1 as ObservableValue<Float>, float2 as ObservableValue<Float>,
        object : Functions<Float> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.min(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Float): Binding<Number?> {
                return Bindings.min(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Float, op2: Any): Binding<Number?> {
                return Bindings.min(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Float) {
                float1.set(value)
            }

            override fun setOp2(value: Float) {
                float2.set(value)
            }

            override fun check(op1: Float, op2: Float, exp: ObservableValue<in Float>) {
                assertEquals(min(op1, op2), (exp as ObservableFloatValue).get(), EPSILON_FLOAT)
            }

        },
        floatData)

class BindingsNumberCalculationsTest11 : BindingsNumberCalculationsTest<Float>(
        float1 as ObservableValue<Float>, float2 as ObservableValue<Float>,
        object : Functions<Float> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.max(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Float): Binding<Number?> {
                return Bindings.max(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Float, op2: Any): Binding<Number?> {
                return Bindings.max(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Float) {
                float1.set(value)
            }

            override fun setOp2(value: Float) {
                float2.set(value)
            }

            override fun check(op1: Float, op2: Float, exp: ObservableValue<in Float>) {
                assertEquals(max(op1, op2), (exp as ObservableFloatValue).get(), EPSILON_FLOAT)
            }

        },
        floatData)

// long
class BindingsNumberCalculationsTest12 : BindingsNumberCalculationsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.add(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): Binding<Number?> {
                return Bindings.add(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): Binding<Number?> {
                return Bindings.add(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: ObservableValue<in Long>) {
                assertEquals(op1 + op2, (exp as ObservableLongValue).get())
            }

        },
        longData)

class BindingsNumberCalculationsTest13 : BindingsNumberCalculationsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.subtract(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): Binding<Number?> {
                return Bindings.subtract(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): Binding<Number?> {
                return Bindings.subtract(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: ObservableValue<in Long>) {
                assertEquals(op1 - op2, (exp as ObservableLongValue).get())
            }

        },
        longData)

class BindingsNumberCalculationsTest14 : BindingsNumberCalculationsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.multiply(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): Binding<Number?> {
                return Bindings.multiply(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): Binding<Number?> {
                return Bindings.multiply(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: ObservableValue<in Long>) {
                assertEquals(op1 * op2, (exp as ObservableLongValue).get())
            }

        },
        longData)

class BindingsNumberCalculationsTest15 : BindingsNumberCalculationsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.divide(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): Binding<Number?> {
                return Bindings.divide(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): Binding<Number?> {
                return Bindings.divide(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: ObservableValue<in Long>) {
                assertEquals(op1 / op2, (exp as ObservableLongValue).get())
            }

        },
        longData)

class BindingsNumberCalculationsTest16 : BindingsNumberCalculationsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.min(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): Binding<Number?> {
                return Bindings.min(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): Binding<Number?> {
                return Bindings.min(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: ObservableValue<in Long>) {
                assertEquals(min(op1, op2), (exp as ObservableLongValue).get())
            }

        },
        longData)

class BindingsNumberCalculationsTest17 : BindingsNumberCalculationsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.max(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): Binding<Number?> {
                return Bindings.max(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): Binding<Number?> {
                return Bindings.max(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: ObservableValue<in Long>) {
                assertEquals(max(op1, op2), (exp as ObservableLongValue).get())
            }

        },
        longData)

// int
class BindingsNumberCalculationsTest18 : BindingsNumberCalculationsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.add(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): Binding<Number?> {
                return Bindings.add(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): Binding<Number?> {
                return Bindings.add(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: ObservableValue<in Int>) {
                assertEquals(op1 + op2, (exp as ObservableIntValue).get())
            }

        },
        intData)

class BindingsNumberCalculationsTest19 : BindingsNumberCalculationsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.subtract(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): Binding<Number?> {
                return Bindings.subtract(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): Binding<Number?> {
                return Bindings.subtract(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: ObservableValue<in Int>) {
                assertEquals(op1 - op2, (exp as ObservableIntValue).get())
            }

        },
        intData)

class BindingsNumberCalculationsTest20 : BindingsNumberCalculationsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.multiply(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): Binding<Number?> {
                return Bindings.multiply(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): Binding<Number?> {
                return Bindings.multiply(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: ObservableValue<in Int>) {
                assertEquals(op1 * op2, (exp as ObservableIntValue).get())
            }

        },
        intData)

class BindingsNumberCalculationsTest21 : BindingsNumberCalculationsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.divide(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): Binding<Number?> {
                return Bindings.divide(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): Binding<Number?> {
                return Bindings.divide(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: ObservableValue<in Int>) {
                assertEquals(op1 / op2, (exp as ObservableIntValue).get())
            }

        },
        intData)

class BindingsNumberCalculationsTest22 : BindingsNumberCalculationsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.min(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): Binding<Number?> {
                return Bindings.min(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): Binding<Number?> {
                return Bindings.min(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: ObservableValue<in Int>) {
                assertEquals(min(op1, op2), (exp as ObservableIntValue).get())
            }

        },
        intData)

class BindingsNumberCalculationsTest23 : BindingsNumberCalculationsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.max(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): Binding<Number?> {
                return Bindings.max(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): Binding<Number?> {
                return Bindings.max(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: ObservableValue<in Int>) {
                assertEquals(max(op1, op2), (exp as ObservableIntValue).get())
            }

        },
        intData)

// short
class BindingsNumberCalculationsTest24 : BindingsNumberCalculationsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.add(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): Binding<Number?> {
                return Bindings.add(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): Binding<Number?> {
                return Bindings.add(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: ObservableValue<in Short>) {
                assertEquals(op1 + op2, (exp as ObservableIntValue).get())
            }

        },
        shortData)

class BindingsNumberCalculationsTest25 : BindingsNumberCalculationsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.subtract(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): Binding<Number?> {
                return Bindings.subtract(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): Binding<Number?> {
                return Bindings.subtract(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: ObservableValue<in Short>) {
                assertEquals(op1 - op2, (exp as ObservableIntValue).get())
            }

        },
        shortData)

class BindingsNumberCalculationsTest26 : BindingsNumberCalculationsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.multiply(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): Binding<Number?> {
                return Bindings.multiply(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): Binding<Number?> {
                return Bindings.multiply(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: ObservableValue<in Short>) {
                assertEquals(op1 * op2, (exp as ObservableIntValue).get())
            }

        },
        shortData)

class BindingsNumberCalculationsTest27 : BindingsNumberCalculationsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.divide(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): Binding<Number?> {
                return Bindings.divide(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): Binding<Number?> {
                return Bindings.divide(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: ObservableValue<in Short>) {
                assertEquals(op1 / op2, (exp as ObservableIntValue).get())
            }

        },
        shortData)

class BindingsNumberCalculationsTest28 : BindingsNumberCalculationsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.min(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): Binding<Number?> {
                return Bindings.min(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): Binding<Number?> {
                return Bindings.min(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: ObservableValue<in Short>) {
                assertEquals(minOf(op1, op2), (exp as ObservableShortValue).get())
            }

        },
        shortData)

class BindingsNumberCalculationsTest29 : BindingsNumberCalculationsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.max(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): Binding<Number?> {
                return Bindings.max(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): Binding<Number?> {
                return Bindings.max(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: ObservableValue<in Short>) {
                assertEquals(maxOf(op1, op2), (exp as ObservableShortValue).get())
            }

        },
        shortData)

// byte
class BindingsNumberCalculationsTest30 : BindingsNumberCalculationsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.add(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): Binding<Number?> {
                return Bindings.add(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): Binding<Number?> {
                return Bindings.add(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: ObservableValue<in Byte>) {
                assertEquals(op1 + op2, (exp as ObservableIntValue).get())
            }

        },
        byteData)

class BindingsNumberCalculationsTest31 : BindingsNumberCalculationsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.subtract(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): Binding<Number?> {
                return Bindings.subtract(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): Binding<Number?> {
                return Bindings.subtract(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: ObservableValue<in Byte>) {
                assertEquals(op1 - op2, (exp as ObservableIntValue).get())
            }

        },
        byteData)

class BindingsNumberCalculationsTest32 : BindingsNumberCalculationsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.multiply(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): Binding<Number?> {
                return Bindings.multiply(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): Binding<Number?> {
                return Bindings.multiply(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: ObservableValue<in Byte>) {
                assertEquals(op1 * op2, (exp as ObservableIntValue).get())
            }

        },
        byteData)

class BindingsNumberCalculationsTest33 : BindingsNumberCalculationsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.divide(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): Binding<Number?> {
                return Bindings.divide(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): Binding<Number?> {
                return Bindings.divide(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: ObservableValue<in Byte>) {
                assertEquals(op1 / op2, (exp as ObservableIntValue).get())
            }

        },
        byteData)

class BindingsNumberCalculationsTest34 : BindingsNumberCalculationsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.min(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): Binding<Number?> {
                return Bindings.min(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): Binding<Number?> {
                return Bindings.min(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: ObservableValue<in Byte>) {
                assertEquals(minOf(op1, op2), (exp as ObservableByteValue).get())
            }

        },
        byteData)

class BindingsNumberCalculationsTest35 : BindingsNumberCalculationsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): Binding<Number?> {
                return Bindings.max(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): Binding<Number?> {
                return Bindings.max(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): Binding<Number?> {
                return Bindings.max(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: ObservableValue<in Byte>) {
                assertEquals(maxOf(op1, op2), (exp as ObservableByteValue).get())
            }

        },
        byteData)