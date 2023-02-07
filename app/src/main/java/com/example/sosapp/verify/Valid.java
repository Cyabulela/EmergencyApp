package com.example.sosapp.verify;

import java.util.regex.Pattern;

import kotlin.text.Regex;

public class Valid {
    public static boolean validNumber(String number) {
        return new Regex("[0][1-9][0-9]{8}").matches(number);
    }

    public static boolean validEmail(String email) {
        return new Regex(Pattern.compile("^(.+)@(\\S+)$", Pattern.CASE_INSENSITIVE)).matches(email);
    }
}
