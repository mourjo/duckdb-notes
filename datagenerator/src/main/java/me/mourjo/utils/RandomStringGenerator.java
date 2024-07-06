package me.mourjo.utils;

import java.security.SecureRandom;

public class RandomStringGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString(String prefix, int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append("_");

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
}
