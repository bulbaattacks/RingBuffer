package org.example;

import java.util.Arrays;

public class RingBuffer<T> {
    private final T[] buffer;
    private final int capacity;
    private int tail;
    private int head;
    private int currentSize;

    public RingBuffer(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be grater than 0");
        }
        this.buffer = (T[]) new Object[capacity];
        this.capacity = capacity;
    }

    public synchronized void push(T value) {
        buffer[head] = value;
        head = (head + 1) % capacity;
        ++currentSize;
    }

    public synchronized T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Buffer is empty");
        }
        var value = buffer[tail];
        buffer[tail] = null;
        tail = (tail + 1) % capacity;
        --currentSize;
        return value;
    }

    public synchronized boolean isFull() {
        return currentSize == capacity;
    }

    public synchronized boolean isEmpty() {
        return currentSize == 0;
    }

    public synchronized void close() {
        Arrays.fill(buffer, null);
        tail = 0;
        head = 0;
        currentSize = 0;
    }
}
