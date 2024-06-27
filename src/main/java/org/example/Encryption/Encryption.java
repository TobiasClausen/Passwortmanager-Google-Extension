package org.example.Encryption;

import javax.crypto.Cipher;
import java.security.KeyPair;

public class Encryption {
    private KeyPair key;
    private KeyManager keyManager;
    private static final String KEYSTORE_FILE = "/Users/tobiasclausen/Documents/CSS/2024/Projekte/Passwordmanager/Passwordmanager/src/main/java/org/example/Encryption/keystore.jks";


    public Encryption() {
        keyManager = new KeyManager(KEYSTORE_FILE);

        if (!keyManager.hasfileKeys()) {
            key = keyManager.keyGenerator();
            keyManager.saveKeys(key);
        } else {
            key = keyManager.loadKeys();
        }
    }

    private byte[] encrypt(String message) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
            return cipher.doFinal(message.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String decrypt(byte[] encryptedMessage) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
            return new String(cipher.doFinal(encryptedMessage));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] setEncryption(String message) {
        return encrypt(message);
    }

    public String getDecryption(byte[] encryptedMessage) {
        return decrypt(encryptedMessage);
    }
}
