/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.lucasmdjl.fixedcapacityqueue

import java.util.*

/**
 * An abstract fixed capacity queue implementation.
 * This class provides a skeletal implementation of a fixed capacity queue using an array,
 * to minimize the effort required to implement this interface.
 *
 * Implementors should make sure all elements of the underlying array are initialized up to
 * capacity.
 *
 * Note: This implementation is not thread-safe. If multiple threads access
 * this queue concurrently, and at least one of the threads modifies the queue
 * structurally, it must be synchronized externally.
 *
 * @param <T> the type of elements held in this queue
 * @param capacity the maximum capacity of the queue. Must be positive.
 */
public abstract class AbstractFixedCapacityArrayQueue<T : Any>(public final override val capacity: Int) :
    AbstractQueue<T>(), FixedCapacityQueue<T> {

    /**
     * Returns the element at the specified position in this queue.
     *
     * This method will only be called with indices that correspond to
     * valid elements in the queue (i.e., positions between head and tail).
     *
     * @param i index of the element to return
     * @return the element at the specified position in this queue
     */
    protected abstract fun getElement(i: Int): T

    /**
     * Replaces the element at the specified position in this queue with the specified element.
     *
     * @param i index of the element to replace
     * @param element element to be stored at the specified position
     */
    protected abstract fun setElement(i: Int, element: T)

    init {
        require(capacity > 0) {
            "Capacity should be greater than 0."
        }
    }

    /**
     * The current index of this queue head.
     */
    private var head = 0

    /**
     * The number of elements currently in this queue.
     */
    public final override var size: Int = 0
        protected set

    /**
     * The number of modifications on this queue.
     */
    private var mods = 0

    /**
     * The current index of this queue tail.
     */
    private val tail: Int get() = add(head, size)

    public final override fun isFull(): Boolean = size == capacity

    /**
     * Adds two elements less than capacity, wrapping the result if it exceeds this queue's capacity.
     * @param x The first element to add. Must be 0 <= x < capacity.
     * @param y The second element to add. Must be 0 <= y <= capacity.
     * @return x + y, wrapped at this queue's capacity.
     */
    private fun add(x: Int, y: Int): Int = run {
        val sum = x + y
        // Use conditional instead of modulo since sum < 2*capacity (performance optimization)
        if (sum >= capacity) sum - capacity
        else sum
    }

    public final override fun offer(e: T): Boolean {
        return if (isFull()) false
        else {
            setElement(tail, e)
            size++
            mods++
            true
        }
    }

    public final override fun pollIf(predicate: (T) -> Boolean): T? = pollIfInternal(predicate)

    public final override fun poll(): T? = pollIfInternal { true }

    private inline fun pollIfInternal(predicate: (T) -> Boolean): T? {
        if (isEmpty()) return null
        val element = getElement(head)
        return if (predicate(element)) {
            head = add(head, 1)
            size--
            mods++
            element
        } else null
    }

    public final override fun peek(): T? = if (isEmpty()) null else getElement(head)

    /**
     * Returns a read-only iterator over the elements in this queue.
     *
     * The iterator is fail-fast, throwing a [ConcurrentModificationException] if the
     * queue is modified during iteration.
     *
     * @return a read-only iterator over the elements in this queue.
     */
    public final override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        private var i = 0
        private val expectedMods = mods
        private val next: Int
            get() = add(head, i)

        override fun hasNext(): Boolean = i < size

        override fun next(): T {
            if (mods != expectedMods) {
                throw ConcurrentModificationException()
            }
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            val current = getElement(next)
            i++
            return current
        }

        override fun remove() {
            throw UnsupportedOperationException("remove")
        }

    }

    public final override fun headRemovingIterator(): MutableIterator<T> = object : MutableIterator<T> {
        private var i = 0
        private var expectedMods = mods
        private val next: Int
            get() = add(head, i)
        private var lastReturned: T? = null

        override fun hasNext(): Boolean = i < size

        override fun next(): T {
            if (mods != expectedMods) {
                throw ConcurrentModificationException()
            }
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            val current = getElement(next)
            lastReturned = current
            i++
            return current
        }

        override fun remove() {
            if (mods != expectedMods) {
                throw ConcurrentModificationException()
            }
            if (lastReturned == null) {
                throw IllegalStateException("next hasn't been called")
            }
            if (i != 1) {
                throw IllegalStateException("Can only remove the head element")
            }
            poll()
            lastReturned = null
            i--
            expectedMods = mods
        }

    }

}
