package practice11;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class TextRedactor {

    private static final String FILE_NAME = "notes.txt";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    writeToFile();
                    break;
                case "2":
                    readFromFile();
                    break;
                case "3":
                    System.out.println("Вихід з програми...");
                    running = false;
                    break;
                default:
                    System.out.println("Помилка: Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- МЕНЮ ТЕКСТОВОГО РЕДАКТОРА ---");
        System.out.println("1. Записати до файлу");
        System.out.println("2. Прочитати увесь вміст файлу");
        System.out.println("3. Вийти з редактора");
        System.out.print("Оберіть дію: ");
    }

    private static void writeToFile() {
        System.out.print("Введіть рядок для запису: ");
        String input = scanner.nextLine();

        // Використовуємо true у FileWriter для додавання тексту в кінець файлу (append mode)
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(input + "\n");
            System.out.println("Запис успішно виконано.");
        } catch (IOException e) {
            System.out.println("Сталася помилка при записі: " + e.getMessage());
        }
    }

    private static void readFromFile() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("Файл ще не створено. Спочатку запишіть щось.");
            return;
        }

        System.out.println("\n--- ВМІСТ ФАЙЛУ ---");
        try (Scanner fileScanner = new Scanner(file)) {
            // Оскільки структури даних (ArrayList тощо) заборонені,
            // просто виводимо рядки безпосередньо в консоль
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не знайдено: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Помилка при читанні: " + e.getMessage());
        }
        System.out.println("-------------------");
    }
}
