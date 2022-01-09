package sample.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidation {
    public static boolean fieldIsValid(String check, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(check);
        return matcher.find();
    }
}
