package nl.soccar.mainserver.util.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import nl.soccar.mainserver.util.PasswordUtilities;
import org.junit.Test;

/**
 * JUnit test that tests the nl.soccar.mainserver.util.PaswordUtilities class.
 *
 * @author PTS34A
 */
public class PasswordUtilitiesTest {

    private static final int DEFAULT_SALT_LENGTH = 16;
    private static final int HASHED_PASSWORD_LENGTH = 64;
    
    // Declaration of test object.
    private final String password = "Welkom123";

    /**
     * Tests the generalteSalt Method.
     */
    @Test
    public void generatingSaltTest() {
        byte[] salt = PasswordUtilities.generateSalt();
        // Checks if the salt is not null.
        assertNotNull(salt);
        // Checks if the salt is 16 long.
        assertEquals(DEFAULT_SALT_LENGTH, salt.length);
    }
    
    /**
     * Tests the addSalt Method.
     */
    @Test
    public void hashingTest() {
        byte[] salt = PasswordUtilities.generateSalt();
        // Checks if the salt is not null.
        assertNotNull(salt);
        // Checks if the salt is 16 bytes long.
        assertEquals(DEFAULT_SALT_LENGTH, salt.length);
        
        byte[] hashedPassword = PasswordUtilities.addSalt(password.getBytes(), salt);
        //Checks if the hashedPassword is not null
        assertNotNull(hashedPassword);
        // Checks if the hashedpassword is 64 bytes long.
        assertEquals(HASHED_PASSWORD_LENGTH, hashedPassword.length);
    }
}
