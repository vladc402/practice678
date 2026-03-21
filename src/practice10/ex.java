package practice10;

import java.util.Scanner;

// --- Кастомні виключення ---
class BaseAuthException extends Exception {
    public BaseAuthException(String message) { super(message); }
}

class UserLimitException extends BaseAuthException {
    public UserLimitException(String message) { super(message); }
}

class InvalidUsernameException extends BaseAuthException {
    public InvalidUsernameException(String message) { super(message); }
}

class InvalidPasswordException extends BaseAuthException {
    public InvalidPasswordException(String message) { super(message); }
}

class UserNotFoundException extends BaseAuthException {
    public UserNotFoundException(String message) { super(message); }
}

class AuthFailedException extends BaseAuthException {
    public AuthFailedException(String message) { super(message); }
}

// --- Основний клас ---
public class ex {
    private static final int MAX_USERS = 15;
    private static String[] usernames = new String[MAX_USERS];
    private static String[] passwords = new String[MAX_USERS];

    // Початковий список заборонених слів
    private static String[] forbiddenPasswords = {"admin", "pass", "password", "qwerty", "ytrewq"};
    private static int forbiddenCount = 5;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- СИСТЕМА АУТЕНТИФІКАЦІЇ ---");
            System.out.println("1. Реєстрація");
            System.out.println("2. Видалення користувача");
            System.out.println("3. Вхід (Дія від користувача)");
            System.out.println("4. Додати заборонене слово для паролів");
            System.out.println("5. Вихід");
            System.out.print("Оберіть опцію: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1": register(); break;
                    case "2": delete(); break;
                    case "3": login(); break;
                    case "4": addForbiddenWord(); break;
                    case "5": System.exit(0);
                    default: System.out.println("Невірний вибір!");
                }
            } catch (UserLimitException | InvalidUsernameException | InvalidPasswordException |
                     UserNotFoundException | AuthFailedException e) {
                System.out.println("ПОМИЛКА: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Сталася непередбачена системна помилка.");
            }
        }
    }

    // --- Функції логіки ---

    private static void register() throws UserLimitException, InvalidUsernameException, InvalidPasswordException {
        int index = findFreeSpace();
        if (index == -1) throw new UserLimitException("Досягнуто ліміт користувачів (15).");

        System.out.print("Логін (мін. 5 симв, без пробілів): ");
        String name = scanner.nextLine();
        validateUsername(name);

        System.out.print("Пароль (мін. 10 симв, 3 цифри, 1 спецсимвол): ");
        String pass = scanner.nextLine();
        validatePassword(pass);

        usernames[index] = name;
        passwords[index] = pass;
        System.out.println("Користувача зареєстровано успішно!");
    }

    private static void delete() throws UserNotFoundException {
        System.out.print("Введіть ім'я користувача для видалення: ");
        String name = scanner.nextLine();

        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && usernames[i].equals(name)) {
                usernames[i] = null;
                passwords[i] = null;
                System.out.println("Користувача видалено.");
                return;
            }
        }
        throw new UserNotFoundException("Користувача з таким ім'ям не знайдено.");
    }

    private static void login() throws AuthFailedException {
        System.out.print("Логін: ");
        String name = scanner.nextLine();
        System.out.print("Пароль: ");
        String pass = scanner.nextLine();

        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && usernames[i].equals(name)) {
                if (passwords[i].equals(pass)) {
                    System.out.println("АУТЕНТИФІКАЦІЯ УСПІШНА. Виконання дії від " + name);
                    return;
                }
            }
        }
        throw new AuthFailedException("Невірне ім'я користувача або пароль.");
    }

    private static void addForbiddenWord() {
        System.out.print("Введіть нове заборонене слово: ");
        String word = scanner.nextLine();
        // Динамічно розширюємо масив заборонених слів (ручна робота з масивом)
        String[] newForbidden = new String[forbiddenPasswords.length + 1];
        for (int i = 0; i < forbiddenPasswords.length; i++) newForbidden[i] = forbiddenPasswords[i];
        newForbidden[forbiddenPasswords.length] = word;
        forbiddenPasswords = newForbidden;
        System.out.println("Слово '" + word + "' додано до списку заборонених.");
    }

    // --- Валідатори ---

    private static void validateUsername(String name) throws InvalidUsernameException {
        if (name.length() < 5) throw new InvalidUsernameException("Логін занадто короткий.");
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == ' ') throw new InvalidUsernameException("Логін не може містити пробіли.");
        }
    }

    private static void validatePassword(String pass) throws InvalidPasswordException {
        if (pass.length() < 10) throw new InvalidPasswordException("Пароль має бути не менше 10 символів.");

        int digits = 0;
        int specials = 0;
        boolean hasSpace = false;

        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);
            if (Character.isDigit(c)) digits++;
            else if (isSpecialChar(c)) specials++;
            else if (c == ' ') hasSpace = true;
        }

        if (hasSpace) throw new InvalidPasswordException("Пароль не може містити пробіли.");
        if (digits < 3) throw new InvalidPasswordException("Пароль має містити хоча б 3 цифри.");
        if (specials < 1) throw new InvalidPasswordException("Пароль має містити хоча б 1 спецсимвол.");

        for (String forbidden : forbiddenPasswords) {
            if (containsSubstring(pass, forbidden)) {
                throw new InvalidPasswordException("Пароль містить заборонене слово: " + forbidden);
            }
        }
    }

    // Допоміжна функція пошуку підрядка без регулярних виразів
    private static boolean containsSubstring(String str, String sub) {
        if (sub.length() == 0) return true;
        for (int i = 0; i <= str.length() - sub.length(); i++) {
            int j;
            for (j = 0; j < sub.length(); j++) {
                if (str.charAt(i + j) != sub.charAt(j)) break;
            }
            if (j == sub.length()) return true;
        }
        return false;
    }

    private static boolean isSpecialChar(char c) {
        String specials = "!@#$%^&*()-_=+[]{};:'\",.<>/?|\\";
        for (int i = 0; i < specials.length(); i++) {
            if (c == specials.charAt(i)) return true;
        }
        return false;
    }

    private static int findFreeSpace() {
        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] == null) return i;
        }
        return -1;
    }
}
