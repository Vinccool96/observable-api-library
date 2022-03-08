package io.github.vinccool96.observable.test

class LinkedList<E> : AbstractMutableList<E>(), MutableList<E> {

    override var size: Int = 0
        private set

    private var first: Node<E>? = null

    private var last: Node<E>? = null

    private var modificationCount = 0

    private fun linkFirst(e: E) {
        val f = this.first
        val newNode = Node(null, e, f)
        this.first = newNode
        if (f == null) {
            this.last = newNode
        } else {
            f.prev = newNode
        }
        this.size++
        this.modificationCount++
    }

    private fun linkLast(e: E) {
        val l = this.last
        val newNode = Node(l, e, null)
        this.last = newNode
        if (l == null) {
            this.first = newNode
        } else {
            l.next = newNode
        }
        this.size++
        this.modificationCount++
    }

    private fun linkBefore(e: E, succ: Node<E>) {
        val pred = succ.prev
        val newNode = Node(pred, e, succ)
        succ.prev = newNode
        if (pred == null) {
            this.first = newNode
        } else {
            pred.next = newNode
        }
        this.size++
        this.modificationCount++
    }

    private fun unlink(x: Node<E>): E {
        val element = x.item
        val next = x.next
        val prev = x.prev
        if (prev == null) {
            this.first = next
        } else {
            prev.next = next
            x.prev = null
        }

        if (next == null) {
            this.last = prev
        } else {
            next.prev = prev
            x.next = null
        }

        this.size--
        this.modificationCount++
        return element
    }

    override fun contains(element: E): Boolean {
        return indexOf(element) != -1
    }

    override fun add(element: E): Boolean {
        linkLast(element)
        return true
    }

    override fun remove(element: E): Boolean {
        var x = this.first
        while (x != null) {
            if (x.item == element) {
                unlink(x)
                return true
            }
            x = x.next
        }
        return false
    }

    override fun addAll(elements: Collection<E>): Boolean {
        return addAll(this.size, elements)
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        checkPositionIndex(index)

        val numNew = elements.size
        if (numNew == 0) {
            return false
        }

        val succ: Node<E>? = if (index == this.size) {
            null
        } else {
            node(index)
        }

        var pred = if (succ == null) {
            this.last
        } else {
            succ.prev
        }

        for (element in elements) {
            val newNode = Node(pred, element, null)
            if (pred == null) {
                this.first = newNode
            } else {
                pred.next = newNode
            }
            pred = newNode
        }

        if (succ == null) {
            this.last = pred
        } else {
            pred!!.next = succ
            succ.prev = pred
        }

        this.size += numNew
        this.modificationCount++
        return true
    }

    override fun clear() {
        var x = this.first
        while (x != null) {
            val next = x.next
            x.prev = null
            x.next = null
            x = next
        }
        this.first = null
        this.last = null
        this.size = 0
        this.modificationCount++
    }

    override fun get(index: Int): E {
        checkElementIndex(index)
        return node(index).item
    }

    override fun set(index: Int, element: E): E {
        checkElementIndex(index)
        val x = node(index)
        val old = x.item
        x.item = element
        return old
    }

    override fun add(index: Int, element: E) {
        checkPositionIndex(index)

        if (index == this.size) {
            linkLast(element)
        } else {
            linkBefore(element, node(index))
        }
    }

    override fun removeAt(index: Int): E {
        checkElementIndex(index)
        return unlink(node(index))
    }

    private fun isElementIndex(index: Int): Boolean {
        return 0 <= index && index < this.size
    }

    private fun isPositionIndex(index: Int): Boolean {
        return 0 <= index && index <= this.size
    }

    private fun outOfBoundsMsg(index: Int) = "Index: $index, Size: $size"

    private fun checkElementIndex(index: Int) {
        if (!isElementIndex(index)) {
            throw IndexOutOfBoundsException(outOfBoundsMsg(index))
        }
    }

    private fun checkPositionIndex(index: Int) {
        if (!isPositionIndex(index)) {
            throw IndexOutOfBoundsException(outOfBoundsMsg(index))
        }
    }

    private fun node(index: Int): Node<E> {
        return if (index < (size shr 1)) {
            var x = this.first
            for (i in 0 until index) {
                x = x!!.next
            }
            x!!
        } else {
            var x = this.last
            var i = this.size - 1
            while (i > index) {
                x = x!!.prev
                i--
            }
            x!!
        }
    }

    override fun indexOf(element: E): Int {
        var i = 0
        var x = this.first
        while (x != null) {
            if (x.item == element) {
                return i
            }
            x = x.next
            i++
        }
        return -1
    }

    override fun lastIndexOf(element: E): Int {
        var i = this.size
        var x = this.last
        while (x != null) {
            i--
            if (x.item == element) {
                return i
            }
            x = x.prev
        }
        return -1
    }

    override fun iterator(): MutableIterator<E> {
        return this.listIterator()
    }

    override fun listIterator(index: Int): MutableListIterator<E> {
        checkPositionIndex(index)
        return ListItr(index)
    }

    private inner class ListItr(index: Int) : MutableListIterator<E> {

        private var lastReturned: Node<E>? = null

        private var next: Node<E>? = if (index == this@LinkedList.size) null else node(index)

        private var nextIndex = index

        private var expectedModCount = this@LinkedList.modificationCount

        override fun hasNext(): Boolean {
            return this.nextIndex < this@LinkedList.size
        }

        override fun next(): E {
            checkForCoModification()
            if (!hasNext()) {
                throw NoSuchElementException()
            }

            this.lastReturned = this.next
            this.next = this.next!!.next
            this.nextIndex++
            return this.lastReturned!!.item
        }

        override fun hasPrevious(): Boolean {
            return this.nextIndex > 0
        }

        override fun previous(): E {
            checkForCoModification()
            if (!hasPrevious()) {
                throw NoSuchElementException()
            }
            this.lastReturned = (if (this.next == null) {
                this@LinkedList.last
            } else {
                this.next!!.prev
            }).also {
                this.next = it
            }
            this.nextIndex--
            return this.lastReturned!!.item
        }

        override fun nextIndex(): Int {
            return this.nextIndex
        }

        override fun previousIndex(): Int {
            return this.nextIndex - 1
        }

        override fun remove() {
            checkForCoModification()
            if (this.lastReturned == null) {
                throw IllegalStateException()
            }

            val lastNext = this.lastReturned!!.next
            unlink(this.lastReturned!!)
            if (this.next == this.lastReturned) {
                this.next = lastNext
            } else {
                this.nextIndex--
            }
            this.lastReturned = null
            this.expectedModCount++
        }

        override fun set(element: E) {
            if (this.lastReturned == null) {
                throw IllegalStateException()
            }
            checkForCoModification()
            this.lastReturned!!.item = element
        }

        override fun add(element: E) {
            checkForCoModification()
            this.lastReturned = null
            if (this.next == null) {
                linkLast(element)
            } else {
                linkBefore(element, this.next!!)
            }
            this.nextIndex++
            this.expectedModCount++
        }

        private fun checkForCoModification() {
            if (this@LinkedList.modificationCount != this.expectedModCount) {
                throw ConcurrentModificationException()
            }
        }

    }

    private class Node<E>(var prev: Node<E>?, var item: E, var next: Node<E>?)

}