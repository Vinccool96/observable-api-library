package io.github.vinccool96.observable.beans.ref

import kotlin.test.Test
import kotlin.test.assertNotNull

class NativeWeakRefTest {

    @Test
    fun testGetGC() {
        var obj: Any? = Any()
        val ref = CoreWeakRefFactory.createWeakRef(obj)
        assertNotNull(ref.get())
        obj = null
    }

}