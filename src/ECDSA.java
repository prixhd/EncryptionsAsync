import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Base64;

public class ECDSA {
    public static String[] main(String[] args) {
        String encodedStrMessage = null;
        String isVerifiedStr = null;
        PrivateKey privateKey = null;
        PublicKey publicKey = null;

        try {
            // Генерация ключей ECDSA
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            keyGen.initialize(new ECGenParameterSpec("secp521r1")); // Выбор эллиптической кривой (может потребоваться заменить на поддерживаемую)

            /*  Какие есть эллиптические кривые
                1) secp256r1 (или P-256): Это одна из кривых, рекомендованных NIST (Национальным институтом стандартов и технологий США) для использования в криптографии. Она обеспечивает 128-битный уровень безопасности.
                2) secp384r1 (или P-384): Еще одна рекомендованная NIST кривая, обеспечивающая 192-битный уровень безопасности.
                3) secp521r1 (или P-521): Это также кривая, предложенная NIST, и обеспечивает 256-битный уровень безопасности.
            */

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
        return new String[]{encodedStrMessage, isVerifiedStr, Arrays.toString(privateKey.getEncoded()), Arrays.toString(publicKey.getEncoded())};
    }

    private static byte[] sign(String message, PrivateKey privateKey) throws Exception {
        Signature ecdsa = Signature.getInstance("SHA256withECDSA");
        ecdsa.initSign(privateKey);

        byte[] messageBytes = message.getBytes("UTF-8");
        ecdsa.update(messageBytes);

        return ecdsa.sign();
    }

    private static boolean verify(String message, byte[] signature, PublicKey publicKey) throws Exception {
        Signature ecdsa = Signature.getInstance("SHA256withECDSA");
        ecdsa.initVerify(publicKey);

        byte[] messageBytes = message.getBytes("UTF-8");
        ecdsa.update(messageBytes);

        return ecdsa.verify(signature);
    }
}
