package com.wilburomae.pezeshalms.common.utilities;

public final class StringUtilities {

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
}
