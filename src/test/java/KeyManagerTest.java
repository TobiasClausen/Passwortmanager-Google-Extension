

import static org.junit.jupiter.api.Assertions.*;

import org.example.Encryption.KeyManager;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.util.Arrays;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class KeyManagerTest {

    private KeyManager keyManager;
    private static final String TEMP_KEYSTORE_FILE = "temp_keystore.jks";
    private static final char[] PASSWORD = "password".toCharArray();
    private static final String ALIAS = "alias";

    @BeforeAll
    public static void setupClass() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @BeforeEach
    public void setUp() {
        keyManager = new KeyManager(TEMP_KEYSTORE_FILE);
        File file = new File(TEMP_KEYSTORE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @AfterEach
    public void tearDown() {
        File file = new File(TEMP_KEYSTORE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testKeyGenerator() {
        KeyPair keyPair = keyManager.keyGenerator();
        assertNotNull(keyPair);
        assertNotNull(keyPair.getPrivate());
        assertNotNull(keyPair.getPublic());
    }

    @Test
    public void testSaveAndLoadKeys() {
        KeyPair keyPair = keyManager.keyGenerator();

        try {
            keyManager.saveKeys(keyPair);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new FileInputStream(TEMP_KEYSTORE_FILE), PASSWORD);
            assertTrue(keyStore.containsAlias(ALIAS));
        } catch (Exception e) {
            fail("Exception thrown during saveKeys: " + e.getMessage());
        }

        KeyPair loadedKeyPair = keyManager.loadKeys();
        assertNotNull(loadedKeyPair);
        assertNotNull(loadedKeyPair.getPrivate());
        assertNotNull(loadedKeyPair.getPublic());

        assertTrue(Arrays.equals(keyPair.getPrivate().getEncoded(), loadedKeyPair.getPrivate().getEncoded()));
        assertTrue(Arrays.equals(keyPair.getPublic().getEncoded(), loadedKeyPair.getPublic().getEncoded()));
    }

    @Test
    public void testHasfileKeys() {
        KeyPair keyPair = keyManager.keyGenerator();

        assertFalse(keyManager.hasfileKeys());

        keyManager.saveKeys(keyPair);
        assertTrue(keyManager.hasfileKeys());
    }
}
