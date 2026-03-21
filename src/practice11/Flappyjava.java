package practice11;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// --- Кастомні виключення ---
class AuthException extends Exception {
    public AuthException(String message) { super(message); }
}

class InvalidNicknameException extends AuthException {
    public InvalidNicknameException(String message) { super(message); }
}

class WrongPasswordException extends AuthException {
    public WrongPasswordException(String message) { super(message); }
}

class EmptyFieldException extends AuthException {
    public EmptyFieldException(String message) { super(message); }
}

public class Flappyjava extends JFrame {

    enum State { AUTH, MENU, PLAYING, PAUSED }
    State gameState = State.AUTH;

    // Дані користувачів (тільки масиви за умовою)
    String[] usernames = new String[10];
    String[] passwords = new String[10];
    int userCount = 0;
    String currentUser = "";

    // Ігрові параметри
    double birdY = 300;
    double birdVelocity = -20;
    int[] obstacle = {600, 300};
    int obstacleSpeed = 6;
    int gap = 190;
    int score = 0;
    int highScore = 0;
    boolean passed = false;

    // Ресурси (замінено на заглушки, якщо файли відсутні)
    Image backgroundImg = new ImageIcon(Flappyjava.class.getResource("background.png")).getImage();
    Image pipeImg = new ImageIcon(Flappyjava.class.getResource("pipe.png")).getImage();
    Image currentBirdImg = new ImageIcon(Flappyjava.class.getResource("jumpsmoothly.png")).getImage();

    public Flappyjava() {
        setTitle("Flappy Bird Deluxe - Auth Edition");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Початкові дані
        usernames[0] = "admin";
        passwords[0] = "12345";
        userCount = 1;

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImg, 0, 0, 600, 600, null);

                if (gameState == State.AUTH) {
                    drawAuthScreen(g);
                } else if (gameState == State.MENU) {
                    drawMenu(g);
                } else {
                    drawGame(g);
                }
            }

            private void drawAuthScreen(Graphics g) {
                g.setColor(new Color(0, 0, 0, 200));
                g.fillRect(0, 0, 600, 600);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("ВХІД У СИСТЕМУ", 180, 200);
                g.setFont(new Font("Arial", Font.PLAIN, 18));
                g.drawString("Натисніть 'L' щоб увійти або 'R' для реєстрації", 110, 300);
            }

            private void drawMenu(Graphics g) {
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(0, 0, 600, 600);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                g.drawString("ВІТАЄМО, " + currentUser.toUpperCase(), 120, 150);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString("РЕКОРД: " + highScore, 250, 200);
                g.drawRect(200, 240, 200, 50);
                g.drawString("ГРАТИ (Space)", 235, 275);
            }

            private void drawGame(Graphics g) {
                g.drawImage(currentBirdImg, 30, (int)birdY, 34, 24, null);
                g.drawImage(pipeImg, obstacle[0], obstacle[1], 60, 600, null);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                g.drawString("" + score, 280, 50);
            }
        };

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    handleInput(e);
                } catch (AuthException ex) {
                    // Обробка виключень через діалогові вікна (програма не падає)
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Помилка доступу", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel);
        setVisible(true);

        // Ігровий цикл
        new Timer(1000 / 60, e -> {
            if (gameState == State.PLAYING) {
                updatePhysics();
            }
            panel.repaint();
        }).start();
    }

    private void handleInput(KeyEvent e) throws AuthException {
        if (gameState == State.AUTH) {
            if (e.getKeyCode() == KeyEvent.VK_L) loginProcess();
            if (e.getKeyCode() == KeyEvent.VK_R) registerProcess();
        } else if (gameState == State.MENU) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) startGame();
        } else if (gameState == State.PLAYING) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) birdVelocity = -7.5;
        }
    }

    private void loginProcess() throws AuthException {
        String user = JOptionPane.showInputDialog("Введіть нікнейм:");
        String pass = JOptionPane.showInputDialog("Введіть пароль:");

        if (user == null || pass == null || user.isEmpty() || pass.isEmpty()) {
            throw new EmptyFieldException("Поля не можуть бути порожніми!");
        }

        boolean found = false;
        for (int i = 0; i < userCount; i++) {
            if (usernames[i].equals(user)) {
                if (passwords[i].equals(pass)) {
                    currentUser = user;
                    gameState = State.MENU;
                    found = true;
                    break;
                } else {
                    throw new WrongPasswordException("Невірний пароль для користувача " + user);
                }
            }
        }

        if (!found) {
            throw new InvalidNicknameException("Користувача " + user + " не існує!");
        }
    }

    private void registerProcess() throws AuthException {
        if (userCount >= usernames.length) {
            throw new AuthException("База даних переповнена!");
        }

        String user = JOptionPane.showInputDialog("Придумайте нікнейм:");
        String pass = JOptionPane.showInputDialog("Придумайте пароль:");

        if (user == null || pass == null || user.length() < 3) {
            throw new InvalidNicknameException("Нікнейм має бути довшим за 3 символи!");
        }

        usernames[userCount] = user;
        passwords[userCount] = pass;
        userCount++;

        JOptionPane.showMessageDialog(null, "Реєстрація успішна! Тепер увійдіть.");
    }

    private void updatePhysics() {
        birdY += birdVelocity;
        birdVelocity += 0.4;
        obstacle[0] -= obstacleSpeed;

        if (!passed && obstacle[0] + 60 <= 30) {
            score++;
            passed = true;
            if (score > highScore) highScore = score;
        }

        if (birdY < 0 || birdY > 570 || (obstacle[0] < 60 && obstacle[0] + 60 > 30 && birdY > obstacle[1] - 30)) {
            gameState = State.MENU;
        }

        if (obstacle[0] < -60) {
            obstacle[0] = 600;
            obstacle[1] = (int) (Math.random() * 300) + 200;
            passed = false;
        }
    }

    private void startGame() {
        birdY = 300;
        birdVelocity = -7;
        obstacle[0] = 600;
        score = 0;
        passed = false;
        gameState = State.PLAYING;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Flappyjava::new);
    }
}