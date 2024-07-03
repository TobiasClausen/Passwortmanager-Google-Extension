import org.example.PasswordGenerator.PasswordGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PasswordGeneratorTest {
    @Test
    public void createPassword(){
        String actual = PasswordGenerator.createPassword(10);
        Assertions.assertNotEquals(actual, null);
        Assertions.assertTrue(actual.length()==10);
    }

    @Test
    public void createUniquePassword(){
        String firstPassword = PasswordGenerator.createPassword(10);
        String secondPassword = PasswordGenerator.createPassword(10);
        Assertions.assertNotEquals(firstPassword, secondPassword);
    }
}
