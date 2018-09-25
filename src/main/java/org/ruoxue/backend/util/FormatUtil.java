package org.ruoxue.backend.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtil {

    public static boolean isPhoneNumber(String test) {
        Pattern pattern = Pattern.compile("^1\\d{10}$");
        Matcher matcher = pattern.matcher(test);
        return matcher.find();
    }

}
