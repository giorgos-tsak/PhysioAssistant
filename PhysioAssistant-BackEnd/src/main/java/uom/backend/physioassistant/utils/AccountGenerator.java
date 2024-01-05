package uom.backend.physioassistant.utils;

import java.util.ArrayList;
import java.util.Random;

public class AccountGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int PASSWORD_LENGTH = 4;
    private static final int USERNAME_LENGTH = 4;


    public static String generateUniqueUsername(ArrayList<String> usernames){

        String username = generateUsername();
        while(usernames.contains(username)){
            username = generateUsername();
        }
        return username;
    }

    private static String generateUsername() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < USERNAME_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static String generatePassword() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

}
