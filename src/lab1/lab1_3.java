package lab1;

class lab1_3 implements Runnable {

    @Override
    public void run() {
        int secondsPassed = 0;
        try {
            while (true) {
                secondsPassed++;
                System.out.println("Прошло секунд: " + secondsPassed);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MessageEvery5SecondsTask implements Runnable {
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(5000);
                System.out.println("Сообщение каждые 5 секунд.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MessageEvery7SecondsTask implements Runnable {
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(7000);
                System.out.println("Сообщение каждые 7 секунд.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

   class MultiThreadTimer {
    public static void main(String[] args) {
        TimerTask timerTask = new TimerTask();
        MessageEvery5SecondsTask messageEvery5SecondsTask = new MessageEvery5SecondsTask();
        MessageEvery7SecondsTask messageEvery7SecondsTask = new MessageEvery7SecondsTask();

        Thread timerThread = new Thread(timerTask);
        Thread messageEvery5SecondsThread = new Thread(messageEvery5SecondsTask);
        Thread messageEvery7SecondsThread = new Thread(messageEvery7SecondsTask);
        timerThread.start();
        messageEvery5SecondsThread.start();
        messageEvery7SecondsThread.start();
    }
 
