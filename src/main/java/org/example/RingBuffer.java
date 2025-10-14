package org.example;

public class RingBuffer<T> {
    private final T[] buffer;
    private final int capacity;
    private int tail;
    private int head;
    private int currentSize;

    public RingBuffer(int capacity) {
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

    private boolean isEmpty() {
        return currentSize == 0;
    }
}
