package logic.algorithms;

import core.javaFX.menu.MenuController;
import utilities.enums.AlertType;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256HashCalculator {

    public String hashString(final String password, final String salt){
        if (password != null && password.length() >= 6 && salt != null && salt.length() == 16){
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                String passwordToHash = "592" + password + salt;
                messageDigest.update(passwordToHash.getBytes("UTF-8"));

                byte[] digest = messageDigest.digest();
                return String.format("%064x", new java.math.BigInteger(1, digest));
            }
            catch (NoSuchAlgorithmException | UnsupportedEncodingException exception){
                MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
            }
            return null;
        }
        throw new IllegalArgumentException("Password must be at least 6 characters, and SALT should be exactly 16 characters");
    }
}
