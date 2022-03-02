@file:Suppress("unused")

package io.github.vinccool96.observable.beans.binding

import io.github.vinccool96.observable.beans.property.*
import kotlin.math.E
import kotlin.math.PI
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class BindingsNumberTest(private val op: NumberExpression) {

    private lateinit var binding: NumberBinding

    @AfterTest
    fun tearDown() {
        if (this::binding.isInitialized) {
            this.binding.dispose()
        }
    }

    @Test
    fun testNegate() {
        this.binding = -this.op
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(negate(this.op.value), this.binding.value)
    }

    @Test
    fun testAdd_Double() {
        this.binding = this.op + DOUBLE
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(add(this.op.value, DOUBLE), this.binding.value)
    }

    @Test
    fun testAdd_Long() {
        this.binding = this.op + LONG
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(add(this.op.value, LONG), this.binding.value)
    }

    @Test
    fun testAdd_Float() {
        this.binding = this.op + FLOAT
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(add(this.op.value, FLOAT), this.binding.value)
    }

    @Test
    fun testAdd_Int() {
        this.binding = this.op + INT
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(add(this.op.value, INT), this.binding.value,
                "'add(${this.op.value!!::class}): ${add(this.op.value, INT)!!::class}, ${this.binding.value!!::class}")
    }

    @Test
    fun testSub_Double() {
        this.binding = this.op - DOUBLE
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(sub(this.op.value, DOUBLE), this.binding.value)
    }

    @Test
    fun testSub_Float() {
        this.binding = this.op - FLOAT
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(sub(this.op.value, FLOAT), this.binding.value)
    }

    @Test
    fun testSub_Long() {
        this.binding = this.op - LONG
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(sub(this.op.value, LONG), this.binding.value)
    }

    @Test
    fun testSub_Int() {
        this.binding = this.op - INT
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(sub(this.op.value, INT), this.binding.value)
    }

    @Test
    fun testMul_Double() {
        this.binding = this.op * DOUBLE
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(mul(this.op.value, DOUBLE), this.binding.value)
    }

    @Test
    fun testMul_Float() {
        this.binding = this.op * FLOAT
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(mul(this.op.value, FLOAT), this.binding.value)
    }

    @Test
    fun testMul_Long() {
        this.binding = this.op * LONG
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(mul(this.op.value, LONG), this.binding.value)
    }

    @Test
    fun testMul_Int() {
        this.binding = this.op * INT
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(mul(this.op.value, INT), this.binding.value)
    }

    @Test
    fun testDiv_Double() {
        this.binding = this.op / DOUBLE
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(div(this.op.value, DOUBLE), this.binding.value)
    }

    @Test
    fun testDiv_Float() {
        this.binding = this.op / FLOAT
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(div(this.op.value, FLOAT), this.binding.value)
    }

    @Test
    fun testDiv_Long() {
        this.binding = this.op / LONG
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(div(this.op.value, LONG), this.binding.value)
    }

    @Test
    fun testDiv_Int() {
        this.binding = this.op / INT
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(div(this.op.value, INT), this.binding.value)
    }

    @Test
    fun testEq_Double() {
        val binding = this.op.isEqualTo(DOUBLE, EPSILON)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(eq(this.op.value, DOUBLE), binding.value)
        binding.dispose()
    }

    @Test
    fun testEq_Float() {
        val binding = this.op.isEqualTo(FLOAT, EPSILON)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(eq(this.op.value, FLOAT), binding.value)
        binding.dispose()
    }

    @Test
    fun testEq_Long() {
        val binding = this.op.isEqualTo(LONG)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(eq(this.op.value, LONG), binding.value)
        binding.dispose()
    }

    @Test
    fun testEq_Int() {
        val binding = this.op.isEqualTo(INT)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(eq(this.op.value, INT), binding.value)
        binding.dispose()
    }

    @Test
    fun testNeq_Double() {
        val binding = this.op.isNotEqualTo(DOUBLE, EPSILON)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(neq(this.op.value, DOUBLE), binding.value)
        binding.dispose()
    }

    @Test
    fun testNeq_Float() {
        val binding = this.op.isNotEqualTo(FLOAT, EPSILON)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(neq(this.op.value, FLOAT), binding.value)
        binding.dispose()
    }

    @Test
    fun testNeq_Long() {
        val binding = this.op.isNotEqualTo(LONG)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(neq(this.op.value, LONG), binding.value)
        binding.dispose()
    }

    @Test
    fun testNeq_Int() {
        val binding = this.op.isNotEqualTo(INT)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(neq(this.op.value, INT), binding.value)
        binding.dispose()
    }

    @Test
    fun testGt_Double() {
        val binding = this.op.greaterThan(DOUBLE)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(gt(this.op.value, DOUBLE), binding.value)
        binding.dispose()
    }

    @Test
    fun testGt_Float() {
        val binding = this.op.greaterThan(FLOAT)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(gt(this.op.value, FLOAT), binding.value)
        binding.dispose()
    }

    @Test
    fun testGt_Long() {
        val binding = this.op.greaterThan(LONG)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(gt(this.op.value, LONG), binding.value)
        binding.dispose()
    }

    @Test
    fun testGt_Int() {
        val binding = this.op.greaterThan(INT)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(gt(this.op.value, INT), binding.value)
        binding.dispose()
    }

    @Test
    fun testLt_Double() {
        val binding = this.op.lessThan(DOUBLE)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(lt(this.op.value, DOUBLE), binding.value)
        binding.dispose()
    }

    @Test
    fun testLt_Float() {
        val binding = this.op.lessThan(FLOAT)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(lt(this.op.value, FLOAT), binding.value)
        binding.dispose()
    }

    @Test
    fun testLt_Long() {
        val binding = this.op.lessThan(LONG)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(lt(this.op.value, LONG), binding.value)
        binding.dispose()
    }

    @Test
    fun testLt_Int() {
        val binding = this.op.lessThan(INT)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(lt(this.op.value, INT), binding.value)
        binding.dispose()
    }

    @Test
    fun testGte_Double() {
        val binding = this.op.greaterThanOrEqualTo(DOUBLE)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(gte(this.op.value, DOUBLE), binding.value)
        binding.dispose()
    }

    @Test
    fun testGte_Float() {
        val binding = this.op.greaterThanOrEqualTo(FLOAT)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(gte(this.op.value, FLOAT), binding.value)
        binding.dispose()
    }

    @Test
    fun testGte_Long() {
        val binding = this.op.greaterThanOrEqualTo(LONG)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(gte(this.op.value, LONG), binding.value)
        binding.dispose()
    }

    @Test
    fun testGte_Int() {
        val binding = this.op.greaterThanOrEqualTo(INT)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(gte(this.op.value, INT), binding.value)
        binding.dispose()
    }

    @Test
    fun testLte_Double() {
        val binding = this.op.lessThanOrEqualTo(DOUBLE)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(lte(this.op.value, DOUBLE), binding.value)
        binding.dispose()
    }

    @Test
    fun testLte_Float() {
        val binding = this.op.lessThanOrEqualTo(FLOAT)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(lte(this.op.value, FLOAT), binding.value)
        binding.dispose()
    }

    @Test
    fun testLte_Long() {
        val binding = this.op.lessThanOrEqualTo(LONG)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(lte(this.op.value, LONG), binding.value)
        binding.dispose()
    }

    @Test
    fun testLte_Int() {
        val binding = this.op.lessThanOrEqualTo(INT)
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        assertEquals(lte(this.op.value, INT), binding.value)
        binding.dispose()
    }

    @Test
    fun testMin_Double() {
        this.binding = Bindings.min(this.op, DOUBLE)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(min(this.op.value, DOUBLE), this.binding.value)
    }

    @Test
    fun testMin_Float() {
        this.binding = Bindings.min(this.op, FLOAT)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(min(this.op.value, FLOAT), this.binding.value)
    }

    @Test
    fun testMin_Long() {
        this.binding = Bindings.min(this.op, LONG)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(min(this.op.value, LONG), this.binding.value)
    }

    @Test
    fun testMin_Int() {
        this.binding = Bindings.min(this.op, INT)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(min(this.op.value, INT), this.binding.value)
    }

    @Test
    fun testMin_Short() {
        this.binding = Bindings.min(this.op, SHORT)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(min(this.op.value, SHORT), this.binding.value)
    }

    @Test
    fun testMin_Byte() {
        this.binding = Bindings.min(this.op, BYTE)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(min(this.op.value, BYTE), this.binding.value)
    }

    @Test
    fun testMax_Double() {
        this.binding = Bindings.max(this.op, DOUBLE)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(max(this.op.value, DOUBLE), this.binding.value)
    }

    @Test
    fun testMax_Float() {
        this.binding = Bindings.max(this.op, FLOAT)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(max(this.op.value, FLOAT), this.binding.value)
    }

    @Test
    fun testMax_Long() {
        this.binding = Bindings.max(this.op, LONG)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(max(this.op.value, LONG), this.binding.value)
    }

    @Test
    fun testMax_Int() {
        this.binding = Bindings.max(this.op, INT)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(max(this.op.value, INT), this.binding.value)
    }

    @Test
    fun testMax_Short() {
        this.binding = Bindings.max(this.op, SHORT)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(max(this.op.value, SHORT), this.binding.value)
    }

    @Test
    fun testMax_Byte() {
        this.binding = Bindings.max(this.op, BYTE)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op)
        assertEquals(max(this.op.value, BYTE), this.binding.value)
    }

    private fun negate(value: Number?): Number? {
        if (value == null) {
            return null
        }
        return when (value::class) {
            Double::class -> -value.toDouble()
            Float::class -> -value.toFloat()
            Long::class -> -value.toLong()
            Int::class -> -value.toInt()
            else -> -value.toInt()
        }
    }

    private fun add(value1: Number?, value2: Number?): Number? {
        return when {
            value1 == null || value2 == null -> null
            value1::class == Double::class || value2::class == Double::class -> value1.toDouble() + value2.toDouble()
            value1::class == Float::class || value2::class == Float::class -> value1.toFloat() + value2.toFloat()
            value1::class == Long::class || value2::class == Long::class -> value1.toLong() + value2.toLong()
            else -> value1.toInt() + value2.toInt()
        }
    }

    private fun sub(value1: Number?, value2: Number?): Number? {
        return when {
            value1 == null || value2 == null -> null
            value1::class == Double::class || value2::class == Double::class -> value1.toDouble() - value2.toDouble()
            value1::class == Float::class || value2::class == Float::class -> value1.toFloat() - value2.toFloat()
            value1::class == Long::class || value2::class == Long::class -> value1.toLong() - value2.toLong()
            else -> value1.toInt() - value2.toInt()
        }
    }

    private fun mul(value1: Number?, value2: Number?): Number? {
        return when {
            value1 == null || value2 == null -> null
            value1::class == Double::class || value2::class == Double::class -> value1.toDouble() * value2.toDouble()
            value1::class == Float::class || value2::class == Float::class -> value1.toFloat() * value2.toFloat()
            value1::class == Long::class || value2::class == Long::class -> value1.toLong() * value2.toLong()
            else -> value1.toInt() * value2.toInt()
        }
    }

    private fun div(value1: Number?, value2: Number?): Number? {
        return when {
            value1 == null || value2 == null -> null
            value1::class == Double::class || value2::class == Double::class -> value1.toDouble() / value2.toDouble()
            value1::class == Float::class || value2::class == Float::class -> value1.toFloat() / value2.toFloat()
            value1::class == Long::class || value2::class == Long::class -> value1.toLong() / value2.toLong()
            else -> value1.toInt() / value2.toInt()
        }
    }

    private fun eq(value1: Number?, value2: Number?): Boolean? {
        return when {
            value1 == null || value2 == null -> null
            value1::class == Double::class || value2::class == Double::class -> value1.toDouble() == value2.toDouble()
            value1::class == Float::class || value2::class == Float::class -> value1.toFloat() == value2.toFloat()
            value1::class == Long::class || value2::class == Long::class -> value1.toLong() == value2.toLong()
            else -> value1.toInt() == value2.toInt()
        }
    }

    private fun neq(value1: Number?, value2: Number?): Boolean? {
        return when {
            value1 == null || value2 == null -> null
            value1::class == Double::class || value2::class == Double::class -> value1.toDouble() != value2.toDouble()
            value1::class == Float::class || value2::class == Float::class -> value1.toFloat() != value2.toFloat()
            value1::class == Long::class || value2::class == Long::class -> value1.toLong() != value2.toLong()
            else -> value1.toInt() != value2.toInt()
        }
    }

    private fun gt(value1: Number?, value2: Number?): Boolean? {
        return when {
            value1 == null || value2 == null -> null
            value1::class == Double::class || value2::class == Double::class -> value1.toDouble() > value2.toDouble()
            value1::class == Float::class || value2::class == Float::class -> value1.toFloat() > value2.toFloat()
            value1::class == Long::class || value2::class == Long::class -> value1.toLong() > value2.toLong()
            else -> value1.toInt() > value2.toInt()
        }
    }

    private fun lt(value1: Number?, value2: Number?): Boolean? {
        return when {
            value1 == null || value2 == null -> null
            value1::class == Double::class || value2::class == Double::class -> value1.toDouble() < value2.toDouble()
            value1::class == Float::class || value2::class == Float::class -> value1.toFloat() < value2.toFloat()
            value1::class == Long::class || value2::class == Long::class -> value1.toLong() < value2.toLong()
            else -> value1.toInt() < value2.toInt()
        }
    }

    private fun gte(value1: Number?, value2: Number?): Boolean? {
        return when {
            value1 == null || value2 == null -> null
            value1::class == Double::class || value2::class == Double::class -> value1.toDouble() >= value2.toDouble()
            value1::class == Float::class || value2::class == Float::class -> value1.toFloat() >= value2.toFloat()
            value1::class == Long::class || value2::class == Long::class -> value1.toLong() >= value2.toLong()
            else -> value1.toInt() >= value2.toInt()
        }
    }

    private fun lte(value1: Number?, value2: Number?): Boolean? {
        return when {
            value1 == null || value2 == null -> null
            value1::class == Double::class || value2::class == Double::class -> value1.toDouble() <= value2.toDouble()
            value1::class == Float::class || value2::class == Float::class -> value1.toFloat() <= value2.toFloat()
            value1::class == Long::class || value2::class == Long::class -> value1.toLong() <= value2.toLong()
            else -> value1.toInt() <= value2.toInt()
        }
    }

    private fun min(value1: Number?, value2: Number?): Number? {
        return when {
            value1 == null || value2 == null -> null
            value1::class == Double::class || value2::class == Double::class -> kotlin.math.min(value1.toDouble(),
                    value2.toDouble())
            value1::class == Float::class || value2::class == Float::class -> kotlin.math.min(value1.toFloat(),
                    value2.toFloat())
            value1::class == Long::class || value2::class == Long::class -> kotlin.math.min(value1.toLong(),
                    value2.toLong())
            value1::class == Int::class || value2::class == Int::class -> kotlin.math.min(value1.toInt(),
                    value2.toInt())
            value1::class == Short::class || value2::class == Short::class -> minOf(value1.toShort(), value2.toShort())
            else -> minOf(value1.toByte(), value2.toByte())
        }
    }

    private fun max(value1: Number?, value2: Number?): Number? {
        return when {
            value1 == null || value2 == null -> null
            value1::class == Double::class || value2::class == Double::class -> kotlin.math.max(value1.toDouble(),
                    value2.toDouble())
            value1::class == Float::class || value2::class == Float::class -> kotlin.math.max(value1.toFloat(),
                    value2.toFloat())
            value1::class == Long::class || value2::class == Long::class -> kotlin.math.max(value1.toLong(),
                    value2.toLong())
            value1::class == Int::class || value2::class == Int::class -> kotlin.math.max(value1.toInt(),
                    value2.toInt())
            value1::class == Short::class || value2::class == Short::class -> maxOf(value1.toShort(), value2.toShort())
            else -> maxOf(value1.toByte(), value2.toByte())
        }
    }

    companion object {

        const val DOUBLE: Double = PI

        const val FLOAT: Float = E.toFloat()

        const val INT: Int = 13

        const val LONG: Long = 42L

        const val SHORT: Short = 13

        const val BYTE: Byte = 4

        const val EPSILON: Double = 1e-12

    }

}

class BindingsNumberTest0 : BindingsNumberTest(SimpleDoubleProperty(DOUBLE))

class BindingsNumberTest1 : BindingsNumberTest(SimpleFloatProperty(FLOAT))

class BindingsNumberTest2 : BindingsNumberTest(SimpleLongProperty(LONG))

class BindingsNumberTest3 : BindingsNumberTest(SimpleIntProperty(INT))

class BindingsNumberTest4 : BindingsNumberTest(SimpleShortProperty(SHORT))

class BindingsNumberTest5 : BindingsNumberTest(SimpleByteProperty(BYTE))
