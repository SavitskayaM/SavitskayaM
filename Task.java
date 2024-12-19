import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class Task implements Runnable {
    //Этот объект будет использоваться для генерации уникальных идентификаторов в безопасном многопоточном режиме
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    Thread thread;
    private final int id;
    private final String name;
    private Status status;

    public Task(String name) {
        this.id = ID_GENERATOR.incrementAndGet();
        this.name = name;
        this.status = new Status(1, "Создан", 0);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    /*public void run() {
        setStatus(new Status(3, "Выполняется", 1));
        System.out.println("Задача " + name + " (ID: " + id + ") начала выполнение.");
        try {
            Thread.sleep(5000); // Симуляция выполнения задачи
        } catch (InterruptedException e) {
            setStatus(new Status(4, "Приостановлено", 2));
            System.out.println("Задача " + name + " (ID: " + id + ") была приостановлена.");
            return;
        }
        setStatus(new Status(5, "Выполнено", 3));
        System.out.println("Задача " + name + " (ID: " + id + ") завершена.");
        }*/
    public void run() {
        setStatus(new Status(3, "Выполняется", 1));
        System.out.println("Задача " + name + " (ID: " + id + ") начала выполнение.");

        // Создаем отдельный поток для выполнения задачи
        thread = new Thread(() -> {
            try {
                // Симуляция выполнения задачи
                for (int i = 0; i < 5; i++) {
                    if (Thread.interrupted()) { // Проверяем прерывание потока
                        setStatus(new Status(4, "Приостановлено", 2));
                        System.out.println("Задача " + name + " (ID: " + id + ") была приостановлена.");
                        return; // Выходим из метода run()
                    }
                    Thread.sleep(1000);
                    System.out.println("Задача " + name + " (ID: " + id + ") выполняется... (" + (i + 1) + "/5)");
                }
                setStatus(new Status(5, "Выполнено", 3));
                System.out.println("Задача " + name + " (ID: " + id + ") завершена.");
            } catch (InterruptedException e) {
                // Обработка прерывания потока
                setStatus(new Status(4, "Приостановлено", 2));
                System.out.println("Задача " + name + " (ID: " + id + ") была приостановлена.");
            }
        });
        thread.start(); // Запускаем поток
    }
}