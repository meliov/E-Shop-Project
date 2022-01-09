package sample.utils.constants;

public class RegexConstants {
    public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String PASS_REGEX = "[A-Za-z0-9]{5,16}";
    public static final String USERNAME_REGEX = "[A-Za-z0-9]{4,}";
    public static final String DATE_PATTERN = "dd.MM.yyyy HH:mm:ss";
    public static final String DATE_REGEX = "^[0-9][0-9]\\.[0-1][0-9]\\.20[0-9][0-5]$";
}
