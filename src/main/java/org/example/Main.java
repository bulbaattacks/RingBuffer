package org.example;

public class Main {
    public static void main(String[] args) {
        var ringBuffer = new RingBuffer<>(5);
        ringBuffer.push(1);
        ringBuffer.pop();
        ringBuffer.push(1);
        ringBuffer.push(1);
        ringBuffer.push(1);
        ringBuffer.close();
        ringBuffer.isFull();
        ringBuffer.isEmpty();
    }
}