import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

class TaskManager {
    private final ConcurrentHashMap<Integer, Task> tasks = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void add(Task task) {
        tasks.put(task.getId(), task);
        System.out.println("Задача добавлена: " + task.getName() + " (ID: " + task.getId() + ")");
    }

    public void start(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            executorService.submit(() -> {
                System.out.println("Запуск задачи: " + task.getName());
                task.run();
            });
        } else {
            System.out.println("Задача с ID " + taskId + " не найдена.");
        }
    }

    /*public void stop(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            System.out.println("Попытка остановить задачу " + task.getName() + " (ID: " + task.getId() + ")");
            task.setStatus(new Status(4, "Приостановлено", 2));
        } else {
            System.out.println("Задача с ID " + taskId + " не найдена.");
        }
    }*/

    public void stop(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            System.out.println("Попытка остановить задачу " + task.getName() + " (ID: " + task.getId() + ")");
            if (task.thread != null) {
                task.thread.interrupt(); // Прерываем поток задачи
            }
        } else {
            System.out.println("Задача с ID " + taskId + " не найдена.");
        }
        //public int getActiveThreadCount() {
          //  return ((ThreadPoolExecutor) executorService).getActiveCount();
        //}
    }
}