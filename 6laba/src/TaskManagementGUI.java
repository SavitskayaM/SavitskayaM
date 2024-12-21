import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class Status {
    private int id;
    private String name;
    private int code;
    private String description;

    public Status(int id, String name, int code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

class Task implements Runnable {
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

    public void run() {
        setStatus(new Status(3, "Выполняется", 1));
        System.out.println("Задача " + name + " (ID: " + id + ") начала выполнение.");

        thread = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    if (Thread.interrupted()) {
                        setStatus(new Status(4, "Приостановлено", 2));
                        System.out.println("Задача " + name + " (ID: " + id + ") была приостановлена.");
                        return;
                    }
                    Thread.sleep(1000);
                    System.out.println("Задача " + name + " (ID: " + id + ") выполняется... (" + (i + 1) + "/5)");
                }
                setStatus(new Status(5, "Выполнено", 3));
                System.out.println("Задача " + name + " (ID: " + id + ") завершена.");
            } catch (InterruptedException e) {
                setStatus(new Status(4, "Приостановлено", 2));
                System.out.println("Задача " + name + " (ID: " + id + ") была приостановлена.");
            }
        });
        thread.start();
    }
}

class TaskManager {
    private final ConcurrentHashMap<Integer, Task> tasks = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool(5);

    public void add(Task task) {
        tasks.put(task.getId(), task);
        System.out.println("Задача добавлена: " + task.getName() + " (ID: " + task.getId() + ")");
    }

    public void start(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            if (task.getStatus().getCode() == 0 || task.getStatus().getCode() == 2) {
                System.out.println("Запуск задачи: " + task.getName());
                task.run(); // Запускаем задачу напрямую
            } else {
                System.out.println("Задача " + task.getName() + " (ID: " + task.getId() + ") не может быть запущена, так как её статус: " + task.getStatus().getName());
            }
        } else {
            System.out.println("Задача с ID " + taskId + " не найдена.");
        }
    }

    public void stop(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            if (task.getStatus().getCode() == 1) {
                System.out.println("Попытка остановить задачу " + task.getName() + " (ID: " + task.getId() + ")");
                if (task.thread != null) {
                    task.thread.interrupt();
                }
            } else {
                System.out.println("Задача " + task.getName() + " (ID: " + task.getId() + ") не может быть остановлена, так как её статус: " + task.getStatus().getName());
            }
        } else {
            System.out.println("Задача с ID " + taskId + " не найдена.");
        }
    }

    public void remove(int taskId) {
        Task task = tasks.remove(taskId);
        if (task != null) {
            System.out.println("Задача " + task.getName() + " (ID: " + task.getId() + ") удалена.");
        } else {
            System.out.println("Задача с ID " + taskId + " не найдена.");
        }
    }

    public void startAllTasks() {
        for (Task task : tasks.values()) {
            if (task.getStatus().getCode() == 0 || task.getStatus().getCode() == 2) {
                System.out.println("Запуск задачи: " + task.getName());
                task.run(); // Запускаем задачу напрямую
            } else {
                System.out.println("Задача " + task.getName() + " (ID: " + task.getId() + ") не может быть запущена, так как её статус: " + task.getStatus().getName());
            }
        }
    }

    public ConcurrentHashMap<Integer, Task> getTasks() {
        return tasks;
    }
}

public class TaskManagementGUI extends JFrame {
    private final TaskManager taskManager = new TaskManager();
    private final DefaultTableModel tableModel;
    private final JTable taskTable;

    public TaskManagementGUI() {
        setTitle("Менеджер задач");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Создаем таблицу для отображения задач
        tableModel = new DefaultTableModel(new Object[]{"ID", "Имя", "Статус"}, 0);
        taskTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();

        buttonPanel.add(new JLabel("Имя задачи:"));
        buttonPanel.add(nameField);
        buttonPanel.add(new JLabel("ID задачи:"));
        buttonPanel.add(idField);

        JButton addButton = new JButton("Добавить задачу");
        JButton startButton = new JButton("Запустить задачу");
        JButton stopButton = new JButton("Остановить задачу");
        JButton removeButton = new JButton("Удалить задачу");
        JButton startAllButton = new JButton("Запустить все задачи"); // Новая кнопка

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                if (!name.isEmpty()) {
                    Task task = new Task(name);
                    taskManager.add(task);
                    updateTable();
                    nameField.setText("");
                }
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    taskManager.start(id);
                    updateTable();
                    idField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Введите корректный ID задачи.");
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    taskManager.stop(id);
                    updateTable();
                    idField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Введите корректный ID задачи.");
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    taskManager.remove(id);
                    updateTable();
                    idField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Введите корректный ID задачи.");
                }
            }
        });

        startAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskManager.startAllTasks();
                updateTable();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(startAllButton); // Добавляем новую кнопку

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTable() {
        tableModel.setRowCount(0); // Очищаем таблицу
        for (Task task : taskManager.getTasks().values()) {
            tableModel.addRow(new Object[]{task.getId(), task.getName(), task.getStatus().getName()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TaskManagementGUI gui = new TaskManagementGUI();
                gui.setVisible(true);
            }
        });
    }
}