package ru.romanbrazhnikov.userformsender.utils;

import android.util.Patterns;

/**
 * Created by roman on 20.10.17.
 */

public class ValidationUtils {
    public static final String regExContainsDigits = "[0-9]+";
    public static final String regExContainsLetters = "[a-zA-Z]+";


    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean containsDigit(String password) {
        return password.matches(regExContainsDigits);
    }

    public static boolean containsLetter(String password) {
        return password.matches(regExContainsLetters);
    }
}
