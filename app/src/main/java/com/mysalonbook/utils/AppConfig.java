package com.mysalonbook.utils;

public class AppConfig {
    private static final String server = "http://localhost/mysalonbook";
    public static String URL_LOGIN = server + "/api/user/login.php";
    public static String URL_SIGN_UP = server + "/api/user/signup.php";
    public static String URL_BOOKING_CREATE = server + "/api/booking/create.php";
    public static String URL_BOOKING_READ = server + "/api/booking/read.php";
    public static String URL_BOOKING_UPDATE = server + "/api/booking/update.php";
    public static String URL_BOOKING_DELETE = server + "/api/booking/delete.php";
    public static String URL_BOOKING_COUNT = server + "/api/booking/count.php";
}
