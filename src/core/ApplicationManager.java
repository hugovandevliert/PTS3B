package core;

import data.contexts.UserMySqlContext;
import logic.repositories.UserRepository;
import models.Auction;
import models.Profile;
import models.User;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ApplicationManager {

    UserRepository userRepository;
    public ArrayList<Auction> loadedAuctions;
    public Profile loadedProfile;
    public User currentUser;

    public ApplicationManager() {
        loadedAuctions = new ArrayList<>();
        userRepository = new UserRepository(new UserMySqlContext());
    }

    public User login(final String username, final String password) {
        String[] saltAndHash = userRepository.getSaltAndHash(username);

        if (saltAndHash != null){
            if (hashString(password, saltAndHash[0]).equals(saltAndHash[1])){
                return currentUser = userRepository.getUserByUsername(username);
            }
        }
        //Password incorrect
        return null;
    }

    public boolean registerUser(final String username, final String password, final String email, final String name) {
        final Pattern validEmailAddressRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        if(username == null || username.length() == 0){
            throw new IllegalArgumentException("Username can not be empty.");
        }
        else if(username.length() > 16){
            throw new IllegalArgumentException("Username can not be longer than 16 characters.");
        }
        else if(password == null || password.length() <= 5){
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        else if(email == null || email.length() == 0){
            throw new IllegalArgumentException("Email can not be empty.");
        }
        else if(email.length() > 255){
            throw new IllegalArgumentException("Email can not be longer than 255 characters.");
        }
        else if(validEmailAddressRegex.matcher(email).find()){
            throw new IllegalArgumentException("Email should be a valid email address.");
        }
        else if(name == null || name.length() == 0){
            throw new IllegalArgumentException("Name can not be empty.");
        }

        final Character[] characters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                                        'Q', 'R', 'S', 'T', 'U', 'W', 'V', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
                                        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                                        'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

        final SecureRandom secureRandom = new SecureRandom();
        final StringBuilder saltStringBuilder = new StringBuilder();

        for(int i = 0; i < 16; i++){
            saltStringBuilder.append(characters[secureRandom.nextInt(characters.length)]);
        }

        final String salt = saltStringBuilder.toString();
        return userRepository.registerUser(username, hashString(password, salt), salt, email, name);
    }

    public String hashString(final String password, final String salt){
        if(password != null && password.length() >= 6 && salt != null && salt.length() == 16){
            try{
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                String passwordToHash = "592" + password + salt;
                messageDigest.update(passwordToHash.getBytes("UTF-8"));

                byte[] digest = messageDigest.digest();
                return String.format("%064x", new java.math.BigInteger(1, digest));
            }
            catch (NoSuchAlgorithmException | UnsupportedEncodingException ex){
                System.out.println(ex.getStackTrace());
            }
            return null;
        }
        throw new IllegalArgumentException("Password must be at least 6 characters, and SALT should be exactly 16 characters");
    }

    public boolean logout() {
        return false;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
