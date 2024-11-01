import java.util.Scanner;
import java.util.concurrent.*;

class MatrixVectorMultiplication {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Ввод размеров матрицы
            System.out.print("Введите количество строк матрицы: ");
            int rows = scanner.nextInt();
            System.out.print("Введите количество столбцов матрицы: ");
            int cols = scanner.nextInt();

            // Проверка на корректность размеров матрицы
            if (rows <= 0 || cols <= 0) {
                throw new IllegalArgumentException("Размеры матрицы должны быть положительными числами.");
            }

            // Ввод матрицы
            double[][] matrix = new double[rows][cols];
            System.out.println("Введите элементы матрицы (" + rows + "x" + cols + "):");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] = scanner.nextDouble();
                }
            }

            // Ввод размера вектора
            System.out.print("Введите размер вектора: ");
            int vectorSize = scanner.nextInt();

            // Проверка на соответствие размеров матрицы и вектора
            if (vectorSize != cols) {
                throw new IllegalArgumentException("Размер вектора должен совпадать с количеством столбцов матрицы.");
            }

            // Ввод вектора
            double[] vector = new double[vectorSize];
            System.out.println("Введите элементы вектора (" + vectorSize + "):");
            for (int i = 0; i < vectorSize; i++) {
                vector[i] = scanner.nextDouble();
            }

            // Умножение матрицы на вектор с использованием многопоточности
            double[] result = multiplyMatrixByVectorParallel(matrix, vector);

            // Вывод результата
            System.out.println("Результат умножения матрицы на вектор:");
            for (double value : result) {
                System.out.println(value);
            }

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static double[] multiplyMatrixByVectorParallel(double[][] matrix, double[] vector) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        if (vector.length != cols) {
            throw new IllegalArgumentException("Размер вектора должен совпадать с количеством столбцов матрицы.");
        }


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

                    // Умножение матриц
                    double[][] resultMatrix = multiplyMatrices(matrix1, matrix2);

                    System.out.println("Результат умножения матриц:");
                    printMatrix(resultMatrix);

                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                } finally {
                    scanner.close();
                }
            }
            public static double[][] multiplyMatrices(double[][] matrix1, double[][] matrix2) {
                int rows1 = matrix1.length;
                int cols1 = matrix1[0].length;
                int cols2 = matrix2[0].length;

                double[][] result = new double[rows1][cols2];

                for (int i = 0; i < rows1; i++) {
                    for (int j = 0; j < cols2; j++) {
                        double sum = 0;
                        for (int k = 0; k < cols1; k++) {
                            sum += matrix1[i][k] * matrix2[k][j];
                        }
                        result[i][j] = sum;
                    }
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
        double[] result = new double[rows];

        // Создаем пул потоков с фиксированным количеством потоков
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Создаем задачи для каждой строки матрицы
        for (int i = 0; i < rows; i++) {
            final int rowIndex = i;
            executor.submit(() -> {
                double sum = 0;
                for (int j = 0; j < cols; j++) {
                    sum += matrix[rowIndex][j] * vector[j];
                }
                result[rowIndex] = sum;
            });
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
}