package logic.algorithms;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256HashCalculator {

    public String hashString(final String password, final String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (password != null  && salt != null){
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            String passwordToHash = "592" + password + salt;
            messageDigest.update(passwordToHash.getBytes("UTF-8"));

            byte[] digest = messageDigest.digest();
            return String.format("%064x", new java.math.BigInteger(1, digest));
        }
        throw new IllegalArgumentException("Password and salt can not be empty or null");
    }
}
