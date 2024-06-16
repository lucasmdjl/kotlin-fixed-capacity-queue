/*
 * Fixed Capacity Queue: fixed capacity queue implementations.
 * Copyright (C) 2024 Lucas M. de Jong Larrarte
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.lucasmdjl.fixedqueuecapacity

import java.util.*

/**
 * An abstract fixed capacity queue implementation.
 * This class provides a skeletal implementation of a fixed capacity queue using an array,
 * to minimize the effort required to implement this interface.
 *
 * @param <T> the type of elements held in this queue
 * @param capacity the maximum capacity of the queue. Must be positive.
 *
 * <p>Note: This implementation is not thread-safe. If multiple threads access
 * this queue concurrently, and at least one of the threads modifies the queue
 * structurally, it must be synchronized externally.</p>
 */
public abstract class AbstractFixedCapacityArrayQueue<T: Any>(private val capacity: Int): AbstractQueue<T>() {

    /**
     * Returns the element at the specified position in this queue.
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

    private var head = 0

    /**
     * The number of elements currently in this queue.
     */
    final override var size: Int = 0
        protected set

    private val tail: Int get() = add(head, size)

    /**
     * Adds two elements less than capacity, wrapping the result if it exceeds this queue's capacity.
     * @param x The first element to add. Must be 0 <= x < capacity.
     * @param y The second element to add. Must be 0 <= y <= capacity.
     * @return x + y, wrapped at this queue's capacity.
     */
    private fun add(x: Int, y: Int): Int = run {
        val result = x + y
        if (result >= capacity) result - capacity
        else result
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

    final override fun iterator(): MutableIterator<T> {
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
        return object: Iterator<T> {
            override fun hasNext(): Boolean = peek() != null
            override fun next(): T = this@AbstractFixedCapacityArrayQueue.remove()
        }
    }
}