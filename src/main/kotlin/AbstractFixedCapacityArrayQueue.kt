/*
 * Fixed Capacity Queue: a Kotlin micro-library with fixed capacity queue implementations.
 * Copyright (C) 2025 Lucas M. de Jong Larrarte
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
public abstract class AbstractFixedCapacityArrayQueue<T : Any>(public val capacity: Int) : AbstractQueue<T>() {

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
    final override var size: Int = 0
        protected set

    /**
     * The current index of this queue tail.
     */
    private val tail: Int get() = add(head, size)

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

    /**
     * Checks if this queue is full.
     * @return true if this queue is full, false otherwise.
     */
    public fun isFull(): Boolean = size == capacity

    final override fun offer(e: T): Boolean {
        return if (isFull()) false
        else {
            setElement(tail, e)
            size++
            true
        }
    }

    /**
     * Retrieves and removes the head of this queue if it matches the predicate, or returns null if it does not match
     * or if this queue is empty.
     * @param predicate a function that evaluates the element.
     * @return the head element if it matches the predicate, or null otherwise.
     */
    public fun pollIf(predicate: (T) -> Boolean): T? {
        if (isEmpty()) return null
        val element = getElement(head)
        return if (predicate(element)) {
            head = add(head, 1)
            size--
            element
        } else null
    }

    final override fun poll(): T? = pollIf { true }

    final override fun peek(): T? = if (isEmpty()) null else getElement(head)

    /**
     * Returns an iterator over the elements contained in this queue.
     * The iterator operates on a snapshot of the queue at the time of creation.
     * Modifications to the iterator do not affect the original queue.
     *
     * @return an iterator over the elements contained in this queue
     */
    final override fun iterator(): MutableIterator<T> {
        // Create snapshot to avoid throwing UnsupportedOperationException
        // while maintaining queue contract of not removing arbitrary elements
        val snapshot = MutableList(size) {
            getElement(add(head, it))
        }
        return snapshot.iterator()
    }

    /**
     * Returns an iterator that consumes the elements from this queue.
     * Iterating over this iterator will remove the elements from this queue.
     *
     * @return an iterator that consumes this queue's elements.
     */
    public fun consumingIterator(): Iterator<T> {
        return object : Iterator<T> {
            override fun hasNext(): Boolean = peek() != null
            override fun next(): T = this@AbstractFixedCapacityArrayQueue.remove()
        }
    }
}
