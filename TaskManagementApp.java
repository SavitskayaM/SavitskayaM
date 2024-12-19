//6lr: сделать визуализацию этого кода (можно с помощью библиотеки javafx)
//создай задачу
//создай задачу
//запусти 1
//запусти 2
//приостанови 1


import java.util.Scanner;

public class TaskManagementApp {

    private static final TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        generateSampleTasks();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1 - Добавить задачу");
            System.out.println("2 - Запустить задачу");
            System.out.println("3 - Остановить задачу");
            System.out.println("4 - Выход");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addTask(scanner);
                    break;
                case 2:
                    startTask(scanner);
                    break;
                case 3:
                    stopTask(scanner);
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void generateSampleTasks() {
        for (int i = 1; i <= 3; i++) {
            Task task = new Task("Задача " + i);
            taskManager.add(task);
        }
    }

    private static void addTask(Scanner scanner) {
        System.out.print("Введите имя задачи: ");
        String name = scanner.next();
        Task task = new Task(name);
        taskManager.add(task);
    }

    private static void startTask(Scanner scanner) {
        System.out.print("Введите ID задачи для запуска: ");
        int id = scanner.nextInt();
        taskManager.start(id);
    }

    private static void stopTask(Scanner scanner) {
        System.out.print("Введите ID задачи для остановки: ");
        int id = scanner.nextInt();
        taskManager.stop(id);
    }
}


