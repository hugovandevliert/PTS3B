package core;

import data.contexts.UserMySqlContext;
import logic.repositories.UserRepository;
import models.Auction;
import models.Profile;
import models.User;
import java.io.IOException;
import logic.algorithms.Sha256HashCalculator;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ApplicationManager {

    private Sha256HashCalculator sha256HashCalculator;
    private UserRepository userRepository;
    public ArrayList<Auction> loadedAuctions;
    public Profile loadedProfile;
    private User currentUser;

    public ApplicationManager() {
        loadedAuctions = new ArrayList<>();
        userRepository = new UserRepository(new UserMySqlContext());
        sha256HashCalculator = new Sha256HashCalculator();
    }

    public boolean login(final String username, final String password) throws SQLException, IOException, ClassNotFoundException, NoSuchAlgorithmException {
        final String[] saltAndHash = userRepository.getSaltAndHash(username);

        if (saltAndHash != null){
            if (sha256HashCalculator.hashString(password, saltAndHash[0]).equals(saltAndHash[1])){
                currentUser = userRepository.getUserByUsername(username);
                return true;
            }
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean registerUser(final String username, final String password, final String email, final String name) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        final Pattern validEmailAddressRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        if (name == null || name.length() == 0){
            throw new IllegalArgumentException("Name can not be empty.");
        }
        else if (email == null || email.length() == 0){
            throw new IllegalArgumentException("Email can not be empty.");
        }
        else if (email.length() > 255){
            throw new IllegalArgumentException("Email can not be longer than 255 characters.");
        }
        else if (!validEmailAddressRegex.matcher(email).find()){
            throw new IllegalArgumentException("Email should be a valid email address.");
        }
        else if (username == null || username.length() == 0){
            throw new IllegalArgumentException("Username can not be empty.");
        }
        else if (username.length() > 16){
            throw new IllegalArgumentException("Username can not be longer than 16 characters.");
        }
        else if (password.length() < 6){
            throw new IllegalArgumentException("Password should be at least 6 characters");
        }
        else if (password.matches("^[0-9]*$")) {
            throw new IllegalArgumentException("Password should not only contain numbers");
        }
        else if (password.length() > 32) {
            throw new IllegalArgumentException("Password should not exceed 32 characters");
        }
        else if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{6,}")) {
            throw new IllegalArgumentException("Password doesn't contain upper/lower case letter or at least one number or special character");
        }

        final String salt = generateSalt();
        return userRepository.registerUser(username, sha256HashCalculator.hashString(password, salt), salt, email, name);
    }

    private String generateSalt(){
        final Character[] characters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                'Q', 'R', 'S', 'T', 'U', 'W', 'V', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
                'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

        final SecureRandom secureRandom = new SecureRandom();
        final StringBuilder saltStringBuilder = new StringBuilder();

        for (int i = 0; i < 16; i++){
            saltStringBuilder.append(characters[secureRandom.nextInt(characters.length)]);
        }

        return saltStringBuilder.toString();
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
