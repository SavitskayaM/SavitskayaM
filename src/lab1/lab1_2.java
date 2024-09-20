package lab1;

class DivisibleByTenPrinter implements Runnable {

    @Override
    public void run() {
        try {
            for (int i = 0; i <= 100; i++) {
                if (i % 10 == 0) {
                    System.out.println(i);
                    Thread.sleep(500); // Pause for 500 milliseconds (0.5 seconds)
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted.");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
    }

    public static void main(String[] args) {
        DivisibleByTenPrinter printer = new DivisibleByTenPrinter();
        Thread thread = new Thread(printer);
        thread.start();
    }
}


