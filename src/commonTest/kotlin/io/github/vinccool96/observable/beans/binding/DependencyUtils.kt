package io.github.vinccool96.observable.beans.binding

import io.github.vinccool96.observable.beans.Observable
import io.github.vinccool96.observable.collections.ObservableCollections
import io.github.vinccool96.observable.collections.ObservableList
import kotlin.test.assertTrue

object DependencyUtils {

    fun checkDependencies(seq: ObservableList<Observable>, vararg deps: Any) {
        // we want to check the source dependencies, therefore we have to remove all intermediate bindings
        val copy: ObservableList<Observable> = ObservableCollections.observableArrayList(seq)
        val it = copy.listIterator()
        while (it.hasNext()) {
            val obj = it.next()
            if (obj is Binding<*>) {
                it.remove()
                for (newDep in obj.dependencies) {
                    it.add(newDep)
                }
            }
        }
        for (obj in deps) {
            assertTrue(copy.contains(obj))
        }
    }

}
