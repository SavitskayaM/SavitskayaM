package lab1;

import java.util.LinkedList;
import java.util.Queue;

class lab1_4 {

    public static void main(String[] args) {
        Buffer buffer = new Buffer(5); // Buffer size is 5

        Thread producerThread = new Thread(new Producer(buffer));
        Thread consumerThread = new Thread(new Consumer(buffer));

        producerThread.start();
        consumerThread.start();
    }
}

class Buffer {
    private Queue<Integer> queue;
    private int maxSize;

    public Buffer(int maxSize) {
        this.queue = new LinkedList<>();
        this.maxSize = maxSize;
    }

    public synchronized void produce(int value) throws InterruptedException {
        while (queue.size() == maxSize) {
            System.out.println("Буфер заполнен, производитель ждет...");
            wait();
        }
        queue.add(value);
        System.out.println("Производитель " + value);
        notify(); // Notify consumer that new data is available
    }

    public synchronized int consume() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println("Буфер пуст, потребитель ждет...");
            wait();
        }
        int value = queue.poll();
        System.out.println("Потребитель: " + value);
        notify(); // Notify producer that space is available
        return value;
    }
}

class Producer implements Runnable {
    private Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            int value = 0;
            while (true) {
                buffer.produce(value++);
                Thread.sleep(1000); // Simulate time taken to produce data
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                buffer.consume();
                Thread.sleep(1500); // Simulate time taken to consume data
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}