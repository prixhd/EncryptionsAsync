import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class ElGamal {

    public static String[] main(String[] args) {
        // Добавление Bouncy Castle в качестве провайдера
        Security.addProvider(new BouncyCastleProvider());
        String encryptedStrBytes = null;
        String decryptedStrMessage = null;
        PublicKey publicKey = null;
        PrivateKey privateKey = null;

        try {
            // Генерация ключей ElGamal
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ElGamal", "BC");
            keyGen.initialize(1024);
            KeyPair keyPair = keyGen.generateKeyPair();

            // Получение открытого и закрытого ключей
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

            // Создание шифратора
            Cipher cipher = Cipher.getInstance("ElGamal");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // Шифрование сообщения
            String message = args[0];
            byte[] encryptedBytes = cipher.doFinal(message.getBytes());

            System.out.println("Original Message: " + message);
            System.out.println("Encrypted Message: " + Base64.getEncoder().encodeToString(encryptedBytes));
            encryptedStrBytes = Base64.getEncoder().encodeToString(encryptedBytes);

            // Расшифрование сообщения

            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String decryptedMessage = new String(decryptedBytes);

            System.out.println("Decrypted Message: " + decryptedMessage);
            decryptedStrMessage = decryptedMessage;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String[]{encryptedStrBytes, decryptedStrMessage, String.valueOf(privateKey), String.valueOf(publicKey)};
    }
}

