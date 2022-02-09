package io.github.vinccool96.observable.beans.ref

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class CoreWeakRefTest {

    @Test
    fun testGet() {
        val obj = Any()
        val ref = CoreWeakRefFactory.createWeakRef(obj)
        assertSame(obj, ref.get())
    }

    @Test
    fun testClear() {
        val obj = Any()
        val ref = CoreWeakRefFactory.createWeakRef(obj)
        assertNotNull(ref.get())
        ref.clear()
        assertNull(ref.get())
    }

}