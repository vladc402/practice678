package practice5;

import java.util.Scanner;

public class ex2 {
    public static double f(double x, double a, double b) {
        // Перший випадок: x ∈ (-3, 3)
        if (x > -3 && x < 3) {
            double expression = 3 * x * x - a;
            if (expression < 0) {
                return Double.NaN; // вираз від'ємний
            }
            return Math.sqrt(expression);
        }
        // 2 випадок: x = 3
        else if (x == 3) {
            return -b * x + 3;
        }
        // 3 випадок: x ∈ (3, 8)
        else if (x > 3 && x < 8) {
            return Math.cos(x - 4);
        }
        // x поза областю визн
        else {
            return Double.NaN;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Обчислення значень функції f(x)");
        System.out.println("f(x) = sqrt(3x^2 - a)   при x ∈ (-3, 3)");
        System.out.println("f(x) = -b*x + 3         при x = 3");
        System.out.println("f(x) = cos(x - 4)       при x ∈ (3, 8)");
        System.out.println();

        try {
            // Введення параметрів
            System.out.print("Введіть значення параметра a: ");
            double a = scanner.nextDouble();

            System.out.print("Введіть значення параметра b: ");
            double b = scanner.nextDouble();

            System.out.print("Введіть значення аргументу x: ");
            double x = scanner.nextDouble();

            // Обчислення значення функції
            double result = f(x, a, b);

            // Виведення результату
            if (Double.isNaN(result)) {
                System.out.println("Функція f(x) не визначена для заданих значень.");
            } else {
                System.out.printf("f(%.2f) = %.6f%n", x, result);
            }

        } catch (Exception e) {
            System.out.println("Помилка: некоректне введення. Будь ласка, введіть числові значення.");
        } finally {
            scanner.close();
        }
    }
}
