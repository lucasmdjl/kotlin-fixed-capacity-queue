# Fixed Capacity Queue

## Overview

This library provides fixed capacity queue implementations for various primitives and generic types.

## Installation

### Gradle

```kotlin
dependencies {
    implementation("com.example:fixed-capacity-queue:0.1.0")
}
```

### Maven
```xml
<dependency>
    <groupId>io.github.lucasmdjl</groupId>
    <artifactId>fixed-capacity-queue</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Example Usage

```kotlin
val queue = FixedCapacityIntArrayQueue(2)

// Add elements to the queue
queue.offer(1)         // Output: true 
queue.offer(2)         // Output: true
queue.offer(3)         // Output: false

// Peek at the head of the queue
println(queue.peek())  // Output: 1

// Poll elements from the queue
println(queue.poll())  // Output: 1
println(queue.poll())  // Output: 2
println(queue.poll())  // Output: null
```