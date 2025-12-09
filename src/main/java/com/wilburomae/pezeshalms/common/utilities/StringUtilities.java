package com.wilburomae.pezeshalms.common.utilities;

import java.security.SecureRandom;
import java.util.Random;

public final class StringUtilities {

    private static final Random RANDOM = new SecureRandom();

    private StringUtilities() {
    }

    public static String toPlural(String singular) {
        if (singular == null || singular.isBlank()) return singular;

        String lower = singular.toLowerCase().trim();

        if (lower.length() < 3) {
            return singular + "s";
        }

        if (lower.endsWith("s") || lower.endsWith("x") || lower.endsWith("z") || lower.endsWith("ch") || lower.endsWith("sh")) {
            return singular + "es";
        }

        if (lower.endsWith("y") && singular.length() > 1 && !isVowel(lower.charAt(lower.length() - 2))) {
            return singular.substring(0, singular.length() - 1) + "ies";
        }

        if (lower.endsWith("f")) {
            return singular.substring(0, singular.length() - 1) + "ves";
        }

        if (lower.endsWith("fe")) {
            return singular.substring(0, singular.length() - 2) + "ves";
        }

        return singular + "s";
    }

    public static boolean isVowel(char c) {
        c = Character.toLowerCase(c);
        return "aeiou".indexOf(c) != -1;
    }


    public static char[] UPPER = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    public static char[] LOWER = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    public static char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static String fixedRandom(char[] alphabet, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphabet[RANDOM.nextInt(alphabet.length)]);
        }
        return sb.toString();
    }
}
