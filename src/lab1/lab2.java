import java.util.LinkedList;
import java.util.Queue;

class Buffer {
    private final int capacity;
    private final Queue<Integer> queue = new LinkedList<>();

    public Buffer(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void produce(int value) throws InterruptedException {
        while (queue.size() == capacity) {
            System.out.println("Буфер заполнен, производитель ждет...");
            wait();
        }
        queue.add(value);
        System.out.println("Производитель сгенерировал: " + value);
        notify();
    }

    public synchronized int consume() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println("Буфер пуст, потребитель ждет...");
            wait();
        }
        int value = queue.poll();
        System.out.println("Потребитель забрал: " + value);
        notify();
        return value;
    }
}

class Producer implements Runnable {
    private final Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        int value = 0;
        try {
            while (true) {
                buffer.produce(value++);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private final Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                buffer.consume();
                Thread.sleep(750);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class ProducerConsumer {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(5);
        Thread producerThread = new Thread(new Producer(buffer));
        Thread consumerThread = new Thread(new Consumer(buffer));
        producerThread.start();
        consumerThread.start();
    }
}
