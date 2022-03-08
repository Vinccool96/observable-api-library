package io.github.vinccool96.observable.internal.collections

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.beans.Observable
import io.github.vinccool96.observable.collections.ObservableListBase
import io.github.vinccool96.observable.dev.Callback
import io.github.vinccool96.observable.internal.utils.IdentityHashMap

internal class ElementObserver<E>(private val extractor: Callback<E, Array<Observable>>,
        private val listenerGenerator: Callback<E, InvalidationListener>, private val list: ObservableListBase<E>) {

    private class ElementsMapElement(val listener: InvalidationListener) {

        private var counter: Int = 1

        fun increment() {
            this.counter++
        }

        fun decrement(): Int {
            return --this.counter
        }

    }

    private val elementsMap: IdentityHashMap<E, ElementsMapElement> = IdentityHashMap()

    internal fun attachListener(e: E?) {
        if (e != null) {
            if (this.elementsMap.containsKey(e)) {
                this.elementsMap[e]?.increment()
            } else {
                val listener: InvalidationListener = this.listenerGenerator.call(e)
                for (o in this.extractor.call(e)) {
                    o.addListener(listener)
                }
                this.elementsMap[e] = ElementsMapElement(listener)
            }
        }
    }

    internal fun detachListener(e: E?) {
        if (e != null) {
            if (this.elementsMap.containsKey(e)) {
                val el: ElementsMapElement = this.elementsMap[e]!!
                for (o in this.extractor.call(e)) {
                    o.removeListener(el.listener)
                }
                if (el.decrement() == 0) {
                    this.elementsMap.remove(e)
                }
            }
        }
    }

}