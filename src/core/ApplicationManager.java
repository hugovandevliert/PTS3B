package core;

import data.contexts.UserMySqlContext;
import logic.repositories.UserRepository;
import models.Auction;
import models.Profile;
import models.User;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ApplicationManager {

    public ArrayList<Auction> loadedAuctions;
    public Profile loadedProfile;
    public User currentUser;

    private UserRepository userRepository;

    public ApplicationManager() {
        loadedAuctions = new ArrayList<>();
        userRepository = new UserRepository(new UserMySqlContext());
    }

    public User login(String username, String password) {
        /*String[] saltAndHash = userRepository.getSaltAndHash(username);

        if(saltAndHash == null){
            //Username not found or SQLException was caught
            return null;
        }
        else{
            try{
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                String passwordToHash = "592" + password + saltAndHash[0];
                messageDigest.update(passwordToHash.getBytes("UTF-8"));

                byte[] digest = messageDigest.digest();
                String hashedPassword = String.format("%064x", new java.math.BigInteger(1, digest));

                if(hashedPassword == saltAndHash[1]){
                    return userRepository.getUserByUsername(username);
                }
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex){
                System.out.println(ex.getStackTrace());
            }
            //Passwords didn't match or exception was caught
            return null;
        }
        //return currentUser = new User("", "", "");*/

        return currentUser = userRepository.getUserByUsername(username);
    }

    public boolean registerUser(String username, String password, String email, String name) {
        return false;
    }

    public boolean logout() {
        return false;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
