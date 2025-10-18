package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

class RingBufferTest {

    @Test
    void popTest() {
        var ringBuffer = new RingBuffer<Integer>(2);
        ringBuffer.push(1);
        ringBuffer.push(2);

        Assertions.assertEquals(1, ringBuffer.pop());
        Assertions.assertEquals(2, ringBuffer.pop());
    }

    @Test
    void pushWithRewriteTest() {
        var ringBuffer = new RingBuffer<Integer>(1);
        ringBuffer.push(1);
        ringBuffer.push(2);

        Assertions.assertEquals(2, ringBuffer.pop());
    }

    @RepeatedTest(100)
    void pushMultithreadingTest() throws InterruptedException {
        int threads = 20;
        var ringBuffer = new RingBuffer<Integer>(100);
        var threadPool = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads * 2);

        var expected = Collections.synchronizedSet(new HashSet<>());
        for (int i = 1; i <= threads; i++) {
            final int finalI = i;
            threadPool.submit(() -> {
                        try {
                            ringBuffer.push(finalI);
                            expected.add(finalI);
                        } finally {
                            latch.countDown();
                        }
                    }
            );
        }

        var actual = Collections.synchronizedSet(new HashSet<>());
        for (int i = 1; i <= threads; i++) {
            threadPool.submit(() -> {
                        try {
                            var popped = ringBuffer.pop();
                            actual.add(popped);
                        } finally {
                            latch.countDown();
                        }
                    }
            );
        }
        latch.await();

        Assertions.assertTrue(expected.containsAll(actual));
    }
}
