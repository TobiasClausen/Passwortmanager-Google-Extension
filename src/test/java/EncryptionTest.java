import org.example.Encryption.Encryption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EncryptionTest {
    Encryption testee = new Encryption();
    @Test
    public void encription(){
        String testword="hallo";

        byte[] transfer = testee.setEncryption(testword);

        Assertions.assertNotEquals(transfer, testword.getBytes());
        Assertions.assertEquals(testee.getDecryption(transfer), testword);
    }
}