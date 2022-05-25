package com.ccw.project.untils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class PBKDF2 {

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    // The length of salt
    public static final int SALT_SIZE = 16;

    // The size of encryption text
    public static final int HASH_SIZE = 32;

    // The number of iteration
    public static final int PBKDF2_ITERATIONS = 1000;

    /**
     * Verify the password
     *
     */
    public static boolean verify(String password, String salt, String key)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        String result = getPBKDF2(password, salt);

        return result.equals(key);
    }

    /**
     * Create the encryption text
     *
     */
    public static String getPBKDF2(String password, String salt) throws NoSuchAlgorithmException,
            InvalidKeySpecException {

        byte[] bytes = DatatypeConverter.parseHexBinary(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), bytes, PBKDF2_ITERATIONS, HASH_SIZE * 4);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
        return DatatypeConverter.printHexBinary(hash);
    }


    /**
     * Create the salt
     *
     */
    public static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] bytes = new byte[SALT_SIZE / 2];
        random.nextBytes(bytes);

        String salt = DatatypeConverter.printHexBinary(bytes);
        return salt;
    }
}
