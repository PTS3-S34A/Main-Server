package nl.soccar.mainserver.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities class for password authentication that provides methods for
 * generation random salts and adding salts to hashed passwords.
 *
 * @author PTS34A
 */
public class PasswordUtilities {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordUtilities.class);

    /**
     * Constructor that is intentionally marked private so a PasswordUtilities
     * object can never be initiated outside this class.
     */
    private PasswordUtilities() {
    }

    /**
     * Methods that generates a random salt of 16 bytes based on the SHA1PRNG
     * algoritm.
     *
     * @return The random salt as a byte array.
     */
    public static byte[] generateSalt() {
        byte[] salt = new byte[16];

        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("An error occurred while generating a salt.", e);
        }

        return salt;
    }

    /**
     * Method that adds a salt to a hashed password and combines them as one
     * byte array of 64 bytes.
     *
     * @param password The hashed password that needs to be combined with the
     * salt.
     * @param salt The salt that needs to be combined with the hashed password.
     * @return The combination of the hash password and the salt as a byte
     * array.
     */
    public static byte[] addSalt(byte[] password, byte[] salt) {
        byte[] hash = new byte[64];

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(password);
            md.update(salt);
            hash = md.digest();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("An error occurred while adding the salt to a hashed password.", e);
        }

        return hash;
    }

}
