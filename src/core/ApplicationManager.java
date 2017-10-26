package core;

import data.contexts.UserMySqlContext;
import logic.repositories.UserRepository;
import models.Auction;
import models.Profile;
import models.User;
import java.io.IOException;
import logic.algorithms.Sha256HashCalculator;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ApplicationManager {

    private Sha256HashCalculator sha256HashCalculator;
    private UserRepository userRepository;
    public ArrayList<Auction> loadedAuctions;
    public Profile loadedProfile;
    public User currentUser;

    public ApplicationManager() {
        loadedAuctions = new ArrayList<>();
        userRepository = new UserRepository(new UserMySqlContext());
        sha256HashCalculator = new Sha256HashCalculator();
    }

    public User login(final String username, final String password) throws SQLException, IOException, ClassNotFoundException {
        String[] saltAndHash = userRepository.getSaltAndHash(username);

        if (saltAndHash != null){
            if (sha256HashCalculator.hashString(password, saltAndHash[0]).equals(saltAndHash[1])){
                return currentUser = userRepository.getUserByUsername(username);
            }
        }
        //Password incorrect
        return null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean registerUser(final String username, final String password, final String email, final String name) {
        final Pattern validEmailAddressRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        if (username == null || username.length() == 0){
            throw new IllegalArgumentException("Username can not be empty.");
        }
        else if (username.length() > 16){
            throw new IllegalArgumentException("Username can not be longer than 16 characters.");
        }
        else if (password == null || password.length() <= 5){
            throw new IllegalArgumentException("Password must be at least 6 characters.");
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
        else if (name == null || name.length() == 0){
            throw new IllegalArgumentException("Name can not be empty.");
        }

        final String salt = generateSalt();
        return userRepository.registerUser(username, sha256HashCalculator.hashString(password, salt), salt, email, name);
    }

    public String generateSalt(){
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

    public boolean logout() {
        return false;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
