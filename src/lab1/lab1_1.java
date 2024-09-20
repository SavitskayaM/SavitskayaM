package lab1;

class DivisibleByTenThread extends Thread {
    @Override
    public void run() {
        try {
            for (int i = 0; i <= 100; i++) {
                if (i % 10 == 0) {
                    System.out.println(i);
                    Thread.sleep(500); 
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted.");
        }
    }
}

public class lab1_1 {
    public static void main(String[] args) {
        DivisibleByTenThread thread = new DivisibleByTenThread();
        thread.start();
    }
}
