import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class TimerTask implements Runnable {
    private final CyclicBarrier barrier;

    public TimerTask(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        int secondsPassed = 0;
        try {
            while (true) {
                secondsPassed++;
                System.out.println("Прошло секунд: " + secondsPassed);
                barrier.await(); // Ожидаем, пока другие потоки не будут готовы
                Thread.sleep(1000); // Ждем 1 секунду
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

class MessageEvery5SecondsTask implements Runnable {
    private final CyclicBarrier barrier;

    public MessageEvery5SecondsTask(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (int i = 0; i < 5; i++) {
                    barrier.await(); // Ждем 5 секунд
                }
                System.out.println("Сообщение каждые 5 секунд.");
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

class MessageEvery7SecondsTask implements Runnable {
    private final CyclicBarrier barrier;

    public MessageEvery7SecondsTask(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (int i = 0; i < 7; i++) {
                    barrier.await(); // Ждем 7 секунд
                }
                System.out.println("Сообщение каждые 7 секунд.");
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

class MultiThreadTimer {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3); // 3 потока: хронометр и два сообщения

        TimerTask timerTask = new TimerTask(barrier);
        MessageEvery5SecondsTask messageEvery5SecondsTask = new MessageEvery5SecondsTask(barrier);
        MessageEvery7SecondsTask messageEvery7SecondsTask = new MessageEvery7SecondsTask(barrier);

        Thread timerThread = new Thread(timerTask);
        Thread messageEvery5SecondsThread = new Thread(messageEvery5SecondsTask);
        Thread messageEvery7SecondsThread = new Thread(messageEvery7SecondsTask);

        timerThread.start();
        messageEvery5SecondsThread.start();
        messageEvery7SecondsThread.start();
    }
}
