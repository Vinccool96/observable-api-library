package io.github.vinccool96.observable.test

import kotlin.math.max
import kotlin.test.assertTrue

actual class BitVector actual constructor() {

    init {
        initWords(BITS_PER_WORD)
    }

    private lateinit var words: LongArray

    private var wordsInUse = 0

    actual fun set(index: Int) {
        if (index < 0) {
            throw IndexOutOfBoundsException("bitIndex < 0: $index")
        }

        val wordIndex: Int = wordIndex(index)
        expandTo(wordIndex)

        this.words[wordIndex] = this.words[wordIndex] or (1L shl index) // Restores invariants

        checkInvariants()
    }

    actual operator fun get(index: Int): Boolean {
        if (index < 0) throw IndexOutOfBoundsException("bitIndex < 0: $index")

        checkInvariants()

        val wordIndex: Int = wordIndex(index)
        return (wordIndex < wordsInUse && words[wordIndex] and (1L shl index) != 0L)
    }

    private fun expandTo(wordIndex: Int) {
        val wordsRequired = wordIndex + 1
        if (this.wordsInUse < wordsRequired) {
            ensureCapacity(wordsRequired)
            this.wordsInUse = wordsRequired
        }
    }

    private fun checkInvariants() {
        assertTrue(this.wordsInUse == 0 || this.words[this.wordsInUse - 1] != 0L)
        assertTrue(this.wordsInUse >= 0 && this.wordsInUse <= this.words.size)
        assertTrue(this.wordsInUse == this.words.size || this.words[this.wordsInUse] == 0L)
    }

    private fun initWords(nBits: Int) {
        this.words = LongArray(wordIndex(nBits - 1) + 1)
    }

    private fun ensureCapacity(wordsRequired: Int) {
        if (this.words.size < wordsRequired) {
            // Allocate larger of doubled size or required size
            val request: Int = max(2 * this.words.size, wordsRequired)
            this.words = this.words.copyOf(request)
        }
    }

    companion object {

        private const val ADDRESS_BITS_PER_WORD = 6

        private const val BITS_PER_WORD = 1 shl ADDRESS_BITS_PER_WORD

        private const val BIT_INDEX_MASK = BITS_PER_WORD - 1

        private fun wordIndex(bitIndex: Int): Int {
            return bitIndex shr ADDRESS_BITS_PER_WORD
        }

    }

}