/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.lucasmdjl.fixedcapacityqueue

import java.util.*

/**
 * Represents a queue with a fixed maximum capacity. This interface extends the standard [Queue] interface, adding
 * methods and properties that relate specifically to the fixed capacity nature of the queue, as well as some other
 * utility methods.
 *
 * Implementations of this interface will restrict the total number of elements in the queue to a [capacity].
 * When the queue is full, attempts to add more elements will typically fail or overwrite older elements depending on
 * the specific implementation.
 *
 * @param T the type of elements held in this queue.
 */
public interface FixedCapacityQueue<T> : Queue<T> {

    /**
     * The maximum number of elements the queue can hold. Once the [size] of the queue
     * reaches this value, the queue is considered full.
     */
    public val capacity: Int

    /**
     * Checks if the queue is full, i.e., if the number of elements in the queue has reached its [capacity].
     *
     * @return true if the queue is full, false otherwise.
     */
    public fun isFull(): Boolean


    /**
     * Removes and returns the head of the queue if it satisfies the given predicate.
     *
     * This method is a convenient way to perform a conditional poll in a single operation.
     * It is equivalent to:
     * ```
     * if (!isEmpty() && predicate(peek()!!)) {
     *     return poll()
     * } else {
     *     return null
     * }
     * ```
     * @param predicate a predicate that the head element must satisfy to be removed and returned.
     * @return the head element of the queue if it exists and satisfies the predicate, or null otherwise.
     */
    public fun pollIf(predicate: (T) -> Boolean): T?

    /**
     * Returns a special-purpose iterator that allows for consuming the queue
     * by repeatedly removing its head element.
     *
     * The `remove()` method on this iterator can be used to remove the current
     * head of the queue, as long as the last `next()` call returned it.
     * A `next()` -> `remove()` sequence can be called repeatedly until this
     * queue is empty.
     *
     * Example of valid usage:
     * ```
     * val it = queue.headRemovingIterator()
     * while (it.hasNext()) {
     *     println("Processing: ${it.next()}")
     *     it.remove() // Removes the processed element from the queue
     * }
     * ```
     *
     * This is an optional operation.
     *
     * @return a mutable iterator designed for consuming the queue from its head.
     * @throws UnsupportedOperationException if the implementation does not support
     * this method.
     */
    public fun headRemovingIterator(): MutableIterator<T>

}
