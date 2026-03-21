package practice11;

import java.io.*;
import java.util.Scanner;

public class ex {

    private static final String STORAGE = "notes.txt";
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Простий консольний нотатник ===");

        boolean isActive = true;

        while (isActive) {
            printMenu();
            int option = readOption();

            switch (option) {
                case 1 -> addNote();
                case 2 -> showNotes();
                case 3 -> {
                    isActive = false;
                    System.out.println("Завершення роботи...");
                }
                default -> System.out.println("Такого пункту немає !!!");
            }
        }

        input.close();
    }

    private static void printMenu() {
        System.out.println("\n--- ДІЇ ---");
        System.out.println("1. Додати запис");
        System.out.println("2. Переглянути записи");
        System.out.println("3. Вихід");
        System.out.print("Оберіть дію: ");
    }

    private static int readOption() {
        while (!input.hasNextInt()) {
            System.out.print("Введіть число, будь ласка: ");
            input.next();
        }
        int value = input.nextInt();
        input.nextLine(); // очищаємо буфер
        return value;
    }

    private static void addNote() {
        System.out.print("Введіть текст: ");
        String text = input.nextLine();

        try (FileWriter fw = new FileWriter(STORAGE, true)) {
            fw.write(text + System.lineSeparator());
            System.out.println("✔ Запис збережено");
        } catch (IOException ex) {
            System.out.println("⚠ Помилка запису: " + ex.getMessage());
        }
    }

    private static void showNotes() {
        System.out.println("\n--- ВАШІ НОТАТКИ ---");

        try (BufferedReader br = new BufferedReader(new FileReader(STORAGE))) {
            String currentLine;
            boolean hasContent = false;

            while ((currentLine = br.readLine()) != null) {
                System.out.println("• " + currentLine);
                hasContent = true;
            }

            if (!hasContent) {
                System.out.println("[Поки що нічого немає]");
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Файл ще не створений. Додайте перший запис.");
        } catch (IOException ex) {
            System.out.println("Помилка читання: " + ex.getMessage());
        }
    }
}
