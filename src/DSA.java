import java.security.*;
import java.util.Base64;

public class DSA {

    public static String[] main(String[] args) {
        String encodedStrMessage = null;
        String isVerifiedStr = null;
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        try {
            // Генерация ключей
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
            keyGen.initialize(1024);
            KeyPair keyPair = keyGen.generateKeyPair();

            // Получение открытого и закрытого ключей
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

            // Создание подписи
            String message = args[0];
            byte[] signature = sign(message, privateKey);
            String encodedMessage = Base64.getEncoder().encodeToString(signature);

            // Проверка подписи
            boolean isVerified = verify(message, signature, publicKey);

            System.out.println("Оригинальное сообщение: " + message);
            System.out.println("--------------------------------------");
            System.out.println("Созданная подпись: " + encodedMessage);
            encodedStrMessage = encodedMessage;
            System.out.println("--------------------------------------");
            System.out.println("Проверка подпись(Тот ли пользователь отправил и не изменена ли подпись): " + isVerified);
            isVerifiedStr = String.valueOf(isVerified);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{encodedStrMessage, isVerifiedStr, String.valueOf(privateKey), String.valueOf(publicKey)};
    }

    private static byte[] sign(String message, PrivateKey privateKey) throws Exception {
        Signature dsa = Signature.getInstance("SHA1withDSA");
        dsa.initSign(privateKey);

        byte[] messageBytes = message.getBytes("UTF-8");
        dsa.update(messageBytes);

        return dsa.sign();
    }

    private static boolean verify(String message, byte[] signature, PublicKey publicKey) throws Exception {
        Signature dsa = Signature.getInstance("SHA1withDSA");
        dsa.initVerify(publicKey);

        byte[] messageBytes = message.getBytes("UTF-8");
        dsa.update(messageBytes);

        return dsa.verify(signature);
    }
}
