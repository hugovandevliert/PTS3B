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

public class ApplicationManager {

    UserRepository userRepository;
    public ArrayList<Auction> loadedAuctions;
    public Profile loadedProfile;
    public User currentUser;

    public ApplicationManager() {
        loadedAuctions = new ArrayList<>();
        userRepository = new UserRepository(new UserMySqlContext());
    }

    public User login(String username, String password) {
        String[] saltAndHash = userRepository.getSaltAndHash(username);

        if(saltAndHash == null){
            //Username not found or SQLException was caught
            return null;
        }
        else{
            if(hashString(password, saltAndHash[0]).equals(saltAndHash[1])){
                return currentUser = userRepository.getUserByUsername(username);
            }
            //Password incorrect
            return null;
        }
    }

    public boolean registerUser(String username, String password, String email, String name) {
        Character[] characters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'W', 'V', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder saltStringBuilder = new StringBuilder();
        for(int i = 0; i < 10; i++){
            saltStringBuilder.append(characters[secureRandom.nextInt(characters.length)]);
        }
        String salt = saltStringBuilder.toString();
        return false; //userRepository.registerUser(username, hashString(password, salt), email, name, salt);
    }

    public String hashString(String password, String salt){
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
        throw new IllegalArgumentException("Password must be at least 6 characters, and salt should be exactly 10 characters");
    }

    public boolean logout() {
        return false;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
