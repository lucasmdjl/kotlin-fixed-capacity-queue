# Fixed Capacity Queue for Kotlin
[![Build Status](https://github.com/lucasmdjl/kotlin-fixed-capacity-queue/actions/workflows/maven-build.yaml/badge.svg)](https://github.com/lucasmdjl/kotlin-fixed-capacity-queue/actions/workflows/maven-build.yaml)
[![Maven Central](https://img.shields.io/maven-central/v/dev.lucasmdjl/kotlin-fixed-capacity-queue.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:dev.lucasmdjl%20AND%20a:kotlin-fixed-capacity-queue)
[![License](https://img.shields.io/badge/License-MPL%202.0-brightgreen.svg)](https://opensource.org/licenses/MPL-2.0)

A lightweight, performant, fixed-capacity queue for Kotlin, with specialized implementations for primitives to avoid boxing overhead.

## Why use this library?

While the JDK provides excellent general-purpose queue implementations like `ArrayDeque`, this library is designed for specific use cases where:

*   **Memory efficiency is critical:** The queue is backed by a pre-allocated array that never resizes, providing predictable memory usage.
*   **High performance with primitives is needed:** Specialized implementations for `Int`, `Long`, `Double`, etc., avoid the performance cost of boxing and unboxing primitives into their object wrappers (e.g., `Int` to `Integer`).
*   **You need a bounded buffer:** Perfect for scenarios like managing a recent history of events, smoothing data streams, or implementing backpressure.

## Features

*   **Fixed Capacity:** Once created, the queue's capacity never changes.
*   **Primitive Specializations:** High-performance queues for all primitive types.
*   **Generic Support:** A standard queue for any object type `T`.
*   **Expressive API:** Provides powerful utilities like `pollIf`, `pollWhile`, and a special `headRemovingIterator` for efficient consumption.
*   **Lightweight:** A micro-library with zero dependencies.
*   **Standard Interface:** All implementations implement the `FixedCapacityQueue` interface, which extends `java.util.Queue` for easy integration with existing code.

## Installation

Available on Maven Central. Add the dependency to your project's build file.

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("dev.lucasmdjl:kotlin-fixed-capacity-queue:0.3.0")
}
```

### Gradle (Groovy DSL)

```groovy
dependencies {
    implementation 'dev.lucasmdjl:kotlin-fixed-capacity-queue:0.3.0'
}
```

### Maven
```xml
<dependency>
    <groupId>dev.lucasmdjl</groupId>
    <artifactId>kotlin-fixed-capacity-queue</artifactId>
    <version>0.3.0</version>
</dependency>
```

## Getting Started

The library provides several queue implementations. Choose the one that best fits your data type.

*   `FixedCapacityArrayQueue<T>` (for any object type)
*   `FixedCapacityIntArrayQueue`
*   `FixedCapacityLongArrayQueue`
*   `FixedCapacityDoubleArrayQueue`
*   `FixedCapacityFloatArrayQueue`
*   `FixedCapacityCharArrayQueue`
*   `FixedCapacityByteArrayQueue`
*   `FixedCapacityBooleanArrayQueue`
*   `FixedCapacityShortArrayQueue`

### Basic Example: Primitive Queue (`Int`)

This is ideal for performance-sensitive tasks involving primitive types.

```kotlin
// Create a queue with a capacity of 2
val queue = FixedCapacityIntArrayQueue(2)

// Add elements. `offer` returns `false` if the queue is full.
println("Offer 1: ${queue.offer(1)}")   // Offer 1: true
println("Offer 2: ${queue.offer(2)}")   // Offer 2: true
println("Offer 3: ${queue.offer(3)}")   // Offer 3: false (Queue is full)

// Check queue properties
println("Is full? ${queue.isFull()}")    // Is full? true
println("Size: ${queue.size}")           // Size: 2

// Peek at the head element without removing it. Returns null if empty.
println("Peek: ${queue.peek()}")         // Peek: 1

// Poll elements from the head. Returns null if empty.
println("Poll: ${queue.poll()}")         // Poll: 1
println("Poll: ${queue.poll()}")         // Poll: 2
println("Poll: ${queue.poll()}")         // Poll: null (Queue is empty)

// Check queue properties again
println("Is empty? ${queue.isEmpty()}")  // Is empty? true
```

### Example: Generic Queue (`String`)

Use `FixedCapacityArrayQueue<T>` for any non-primitive type.

```kotlin
val eventLog = FixedCapacityArrayQueue<String>(3)

eventLog.offer("User logged in")
eventLog.offer("Viewed dashboard")
eventLog.offer("Clicked save")

// The queue is now full. Adding another element will fail.
eventLog.offer("Updated profile") // returns false

// To make space, poll the oldest event
val oldestEvent = eventLog.poll()
println("Removed oldest event: $oldestEvent") // Removed oldest event: User logged in

// Now there is space for a new event
eventLog.offer("Updated profile") // returns true

// Iterate over the remaining elements
println("Current event log:")
for (event in eventLog) {
    println("- $event")
}
// Current event log:
// - Viewed dashboard
// - Clicked save
// - Updated profile
```

## Advanced Usage & Unique Features

### Conditional Polling with `pollIf`

Safely poll an element only if it matches a condition.

```kotlin
val dataQueue = FixedCapacityIntArrayQueue(5)
dataQueue.offer(11) // Odd
dataQueue.offer(20) // Even
dataQueue.offer(33) // Odd
dataQueue.offer(40) // Even

// This is cleaner than: if (q.peek() % 2 == 0) q.poll()
val firstProcessed = dataQueue.pollIf { it % 2 == 0 } // returns null (11 is odd)
dataQueue.poll() // returns 11
val secondProcessed = dataQueue.pollIf { it % 2 == 0 } // returns 20 (20 is even)

println(secondProcessed) // Output: 20
println("Remaining in queue: $dataQueue") // Output: Remaining in queue: [33, 40]
```

### Batch Polling with `pollWhile`

Efficiently remove a continuous sequence of elements from the head of the queue that satisfy a condition.

```kotlin
val tasks = FixedCapacityArrayQueue<String>(4)
tasks.offer("URGENT: Fix login")
tasks.offer("URGENT: Deploy patch")
tasks.offer("NORMAL: Update docs")
tasks.offer("URGENT: Check logs")

// Process all urgent tasks at the front of the queue
tasks.pollWhile { it.startsWith("URGENT") }

// The queue now only contains tasks that were not urgent or came after a non-urgent task.
println(tasks) // Output: [NORMAL: Update docs, URGENT: Check logs]
```

### Consuming the Queue with `headRemovingIterator`

Efficiently process and remove elements in a single loop using a special-purpose iterator.

```kotlin
val messages = FixedCapacityArrayQueue<String>(3)
messages.offer("Message A")
messages.offer("Message B")
messages.offer("Message C")

val iterator = messages.headRemovingIterator()
while (iterator.hasNext()) {
    val msg = iterator.next()
    println("Processing: $msg")
    // The remove() call polls the element we just processed
    iterator.remove()
}

println("Is queue empty? ${messages.isEmpty()}") // Output: Is queue empty? true
```

#### Bonus: Implementing `pollWhile` with `headRemovingIterator`

The `headRemovingIterator` is powerful enough that `pollWhile` could have been implemented like this:

```kotlin
fun <T> FixedCapacityQueue<T>.pollWhileExample(predicate: (T) -> Boolean) {
    val iter = this.headRemovingIterator()
    while (iter.hasNext() && predicate(iter.next())) {
        iter.remove()
    }
}
```

## License

This project is licensed under the **Mozilla Public License 2.0**. See the [LICENSE](LICENSE) file for details.
