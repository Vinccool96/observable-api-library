package io.github.vinccool96.observable.beans.ref

import kotlin.test.Test
import kotlin.test.assertSame

class CoreWeakRefTest {

    @Test
    fun testGet() {
        val obj = Any()
        val ref = CoreWeakRefFactory.createWeakRef(obj)
        assertSame(obj, ref.get())
    }

}