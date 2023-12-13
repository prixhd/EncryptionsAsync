import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;


public class EncryptionInterface extends JFrame {
    private JTextField messageField;
    private JComboBox<String> algorithmComboBox;
    private JTextArea resultArea;
    private JTextArea algorithmInfoArea; // Новое поле для текста об алгоритме


    public EncryptionInterface() {
        setTitle("Crypto GUI");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        getContentPane().add(panel);

        Font font = new Font("Arial", Font.PLAIN, 22); // Изменено значение размера шрифта

        JLabel messageLabel = new JLabel("Введите данные:");
        messageLabel.setFont(font);
        panel.add(messageLabel);

        messageField = new JTextField(15);
        messageField.setFont(font);
        panel.add(messageField);

        JLabel algorithmLabel = new JLabel("Алгоритм:");
        algorithmLabel.setFont(font);
        panel.add(algorithmLabel);

        String[] algorithms = {"RSA", "DSA", "RCDSA", "ElGamal"};
        algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.setFont(font);
        panel.add(algorithmComboBox);

        JButton encryptButton = new JButton("Шифровка/Расшифровка");
        encryptButton.setFont(font);
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Handle encryption logic based on the selected algorithm
                String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
                displayAlgorithmInfo(selectedAlgorithm);

                System.out.println(selectedAlgorithm);
                // Implement encryption logic here
                if (Objects.equals(selectedAlgorithm, "RSA")) {
                    try {
                        String[] strings = RSA.rsaGetKeys();
                        resultArea.setText("Результат шифровки: " + Base64.getEncoder().encodeToString((RSA.rsaEncrypted(messageField.getText()))) + "\n" +
                                "Результат расшифровки: " + RSA.rsaDecrypted(RSA.rsaEncrypted(messageField.getText())) + "\n\n" +
                                "Приватный ключ: " + strings[0] + "\n" +
                                "Публичный ключ: " + strings[1] + "\n");

                    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                             IllegalBlockSizeException | BadPaddingException | IOException |
                             InvalidKeySpecException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (Objects.equals(selectedAlgorithm, "ElGamal")) {
                    String[] strings = ElGamal.main(new String[]{messageField.getText()});
                    resultArea.setText("Результат шифровки: " + strings[0] + "\n" +
                            "Результат расшифровки: " + strings[1] + "\n\n" +
                            "Приватный ключ: " + strings[2] + "\n" +
                            "Публичный ключ: " + strings[3] + "\n");
                } else {
                    resultArea.setText("Данный алгоритм шифрования не поддерживает провверку подписок, Выберите один из алгоритмов: RSA/ElGamal");
                }
            }
        });
        panel.add(encryptButton);

        JButton decryptButton = new JButton("Верификация");
        decryptButton.setFont(font);
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle decryption/verification logic based on the selected algorithm
                String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
                // Implement decryption/verification logic here
                if (Objects.equals(selectedAlgorithm, "DSA")) {
                    String[] strings = DSA.main(new String[]{messageField.getText()});
                    resultArea.setText("Результат созданной подписи: " + strings[0] + "\n" +
                            "Не повреждена ли подпись: " + strings[1]+ "\n\n" +
                            "Приватный ключ: " + strings[2] + "\n" +
                            "Публичный ключ: " + strings[3] + "\n");
                } else if (Objects.equals(selectedAlgorithm, "RCDSA")) {
                    String[] strings = ECDSA.main(new String[]{messageField.getText()});
                    resultArea.setText("Результат созданной подписи: " + strings[0] + "\n" +
                            "Не повреждена ли подпись: " + strings[1]+ "\n\n" +
                            "Приватный ключ: " + strings[2] + "\n" +
                            "Публичный ключ: " + strings[3] + "\n");
                }
            }
        });
        panel.add(decryptButton);

        resultArea = new JTextArea(10, 50); // Увеличено значение строк и столбцов
        resultArea.setFont(font);
        resultArea.setEditable(false);

        // Установка шрифта для JTextArea

        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane);

        JLabel sui = new JLabel("Краткая инфромация о алгоритме:");
        sui.setFont(font);
        panel.add(sui);

        algorithmInfoArea = new JTextArea(10, 50);
        algorithmInfoArea.setFont(font);
        algorithmInfoArea.setEditable(false);
        JScrollPane algorithmInfoScrollPane = new JScrollPane(algorithmInfoArea);

        panel.add(algorithmInfoScrollPane);

        setFont(font);

        setVisible(true);
    }

    private void displayAlgorithmInfo(String selectedAlgorithm) {
        String info = "";
        switch (selectedAlgorithm) {
            case "RSA":
                info = "RSA (Rivest-Shamir-Adleman):\n" +
                        "Генерация ключей: Процесс генерации ключей включает в себя выбор двух простых больших чисел (p и q), " +
                        "вычисление их произведения (n), и выбор открытой экспоненты (e). Закрытый ключ состоит из n и закрытой экспоненты (d).\n" +
                        "\n" +
                        "Шифрование: Обычно используется для шифрования сессионных ключей. Входные данные — блок данных, который может быть представлен как целое число, меньшее по модулю n.\n" +
                        "\n" +
                        "Расшифрование: Входные данные — шифртекст, который также представляется как целое число, меньшее по модулю n.";
                break;
            case "DSA":
                info = "DSA (Digital Signature Algorithm):\n" +
                        "Генерация ключей: Генерация ключей включает в себя выбор большого простого числа (p), вычисление других параметров, включая генератор (g), закрытый ключ (x), и открытый ключ (y).\n" +
                        "\n" +
                        "Подпись: Входные данные — хэш-значение сообщения, которое подписывается закрытым ключом.\n" +
                        "\n" +
                        "Проверка подписи: Входные данные — хэш-значение сообщения, подпись и открытый ключ.";
                break;
            case "ElGamal":
                info = "ElGamal:\n" +
                        "Генерация ключей: Генерация ключей включает в себя выбор большого простого числа (p) и генератора (g). Закрытый ключ — случайное число (a), открытый ключ — g^a mod p.\n" +
                        "\n" +
                        "Шифрование: Входные данные — открытый текст, который представляется как элемент поля (обычно число от 1 до p-1).\n" +
                        "\n" +
                        "Расшифрование: Входные данные — шифртекст, который представляется парой элементов поля.";
                break;
            case "RCDSA":
                info = "RCDSA:\n" +
                        "Генерация ключей: Выбор параметров эллиптической кривой, генерация закрытого ключа (случайной точки на кривой) и вычисление открытого ключа.\n" +
                        "\n" +
                        "Подпись: Входные данные — хэш-значение сообщения, которое подписывается закрытым ключом.\n" +
                        "\n" +
                        "Проверка подписи: Входные данные — хэш-значение сообщения, подпись и открытый ключ.";

            break;
        }
        algorithmInfoArea.setText(info);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EncryptionInterface();
            }
        });
    }
}
