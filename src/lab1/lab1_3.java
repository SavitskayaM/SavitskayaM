package lab1;

class lab1_3 implements Runnable {

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            while (true) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                System.out.println("Прошедшее время: " + elapsedTime / 1000 + " секунды");
                Thread.sleep(1000); // Pause for 1 second
            }
        } catch (InterruptedException e) {
            System.out.println("Поток TimeDisplay был прерван");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
    }

    public static void main(String[] args) {
        // Create and start the time display thread
        lab1_3 timeDisplay = new lab1_3();
        Thread timeThread = new Thread(timeDisplay);
        timeThread.start();

        // Create and start the 5-second message thread
        FiveSecondMessage fiveSecondMessage = new FiveSecondMessage();
        Thread fiveSecondThread = new Thread(fiveSecondMessage);
        fiveSecondThread.start();

        // Create and start the 7-second message thread
        SevenSecondMessage sevenSecondMessage = new SevenSecondMessage();
        Thread sevenSecondThread = new Thread(sevenSecondMessage);
        sevenSecondThread.start();
    }
}

class FiveSecondMessage implements Runnable {

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("Сообщение каждые 5 секунд");
                Thread.sleep(5000); // Pause for 5 seconds
            }
        } catch (InterruptedException e) {
            System.out.println("\n" + "Поток FiveSecondMessage был прерван.");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
    }
}

class SevenSecondMessage implements Runnable {

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("Сообщение каждые 7 секунд");
                Thread.sleep(7000); // Pause for 7 seconds
            }
        } catch (InterruptedException e) {
            System.out.println("Поток SevenSecondMessage прерван.");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
    }
}