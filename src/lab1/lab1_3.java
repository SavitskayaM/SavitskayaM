package lab1;

class lab1_3 implements Runnable {

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            while (true) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                System.out.println("Прошедшее время: " + elapsedTime / 1000 + " секунды");
                Thread.sleep(1000); // Пауза 1 секунда
            }
        } catch (InterruptedException e) {
            System.out.println("Поток TimeDisplay был прерван");
            Thread.currentThread().interrupt(); 
        }
    }

    public static void main(String[] args) {
        // Поток сотображением времени
        lab1_3 timeDisplay = new lab1_3();
        Thread timeThread = new Thread(timeDisplay);
        timeThread.start();

        //5-ти секундная ветка сообщений
        FiveSecondMessage fiveSecondMessage = new FiveSecondMessage();
        Thread fiveSecondThread = new Thread(fiveSecondMessage);
        fiveSecondThread.start();

        // 7-ми секундная ветка сообщений
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
                Thread.sleep(5000); // Пауза в 5 секунд
            }
        } catch (InterruptedException e) {
            System.out.println("\n" + "Поток FiveSecondMessage был прерван.");
            Thread.currentThread().interrupt();
        }
    }
}

class SevenSecondMessage implements Runnable {

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("Сообщение каждые 7 секунд");
                Thread.sleep(7000); // Пуза в 7 секунд
            }
        } catch (InterruptedException e) {
            System.out.println("Поток SevenSecondMessage прерван.");
            Thread.currentThread().interrupt(); 
        }
    }
}
