package io.github.vinccool96.observable.beans.binding

import io.github.vinccool96.observable.beans.property.DoubleProperty
import io.github.vinccool96.observable.beans.property.SimpleDoubleProperty
import java.util.*
import kotlin.math.E
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class JVMAbstractNumberExpressionTest {

    @Test
    fun testAsString_Format() {
        val defaultLocale: Locale = Locale.getDefault()
        try {
            // checking German default
            Locale.setDefault(Locale.GERMAN)
            val d: DoubleProperty = SimpleDoubleProperty(PI)
            val s: StringBinding = d.asString("%.4f")
            DependencyUtils.checkDependencies(s.dependencies, d)
            assertEquals("3,1416", s.get())
            d.set(E)
            assertEquals("2,7183", s.get())

            // checking US default
            Locale.setDefault(Locale.US)
            d.set(PI)
            assertEquals("3.1416", s.get())
            d.set(E)
            assertEquals("2.7183", s.get())
        } finally {
            Locale.setDefault(defaultLocale)
        }
    }

    @Test
    fun testAsString_LocaleFormat() {
        // checking German default
        val d: DoubleProperty = SimpleDoubleProperty(PI)
        var s: StringBinding = d.asString(Locale.GERMAN, "%.4f")
        DependencyUtils.checkDependencies(s.dependencies, d)
        assertEquals("3,1416", s.get())
        d.set(E)
        assertEquals("2,7183", s.get())

        // checking US default
        s = d.asString(Locale.US, "%.4f")
        DependencyUtils.checkDependencies(s.dependencies, d)
        d.set(PI)
        assertEquals("3.1416", s.get())
        d.set(E)
        assertEquals("2.7183", s.get())
    }

}