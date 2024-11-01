import java.util.Scanner;
import java.util.concurrent.*;

class MatrixMultiplication {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Ввод размеров первой матрицы
            System.out.print("Введите количество строк первой матрицы: ");
            int rows1 = scanner.nextInt();
            System.out.print("Введите количество столбцов первой матрицы: ");
            int cols1 = scanner.nextInt();

            if (rows1 <= 0 || cols1 <= 0) {
                throw new IllegalArgumentException("Размеры матрицы должны быть положительными числами.");
            }

            double[][] matrix1 = new double[rows1][cols1];
            System.out.println("Введите элементы первой матрицы (" + rows1 + "x" + cols1 + "):");
            for (int i = 0; i < rows1; i++) {
                for (int j = 0; j < cols1; j++) {
                    matrix1[i][j] = scanner.nextDouble();
                }
            }

            System.out.print("Введите количество строк второй матрицы: ");
            int rows2 = scanner.nextInt();
            System.out.print("Введите количество столбцов второй матрицы: ");
            int cols2 = scanner.nextInt();

            if (rows2 <= 0 || cols2 <= 0) {
                throw new IllegalArgumentException("Размеры матрицы должны быть положительными числами.");
            }

            // Проверка на соответствие размеров матриц для умножения
            if (cols1 != rows2) {
                throw new IllegalArgumentException("Количество столбцов первой матрицы должно быть равно количеству строк второй матрицы.");
            }

            double[][] matrix2 = new double[rows2][cols2];
            System.out.println("Введите элементы второй матрицы (" + rows2 + "x" + cols2 + "):");
            for (int i = 0; i < rows2; i++) {
                for (int j = 0; j < cols2; j++) {
                    matrix2[i][j] = scanner.nextDouble();
                }
            }

            // Умножение матриц с использованием многопоточности
            double[][] resultMatrix = multiplyMatricesParallel(matrix1, matrix2);

            System.out.println("Результат умножения матриц:");
            printMatrix(resultMatrix);

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static double[][] multiplyMatricesParallel(double[][] matrix1, double[][] matrix2) {
        int rows1 = matrix1.length;
        int cols1 = matrix1[0].length;
        int cols2 = matrix2[0].length;

        double[][] result = new double[rows1][cols2];

        // Создаем пул потоков с фиксированным количеством потоков
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Создаем задачи для каждого элемента результирующей матрицы
        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                final int row = i;
                final int col = j;
                executor.submit(() -> {
                    double sum = 0;
                    for (int k = 0; k < cols1; k++) {
                        sum += matrix1[row][k] * matrix2[k][col];
                    }
                    result[row][col] = sum;
                });
            }
        }

        // Завершаем работу пула потоков и ждем завершения всех задач
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Ошибка при ожидании завершения потоков.", e);
        }

        return result;
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}