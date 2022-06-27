package com.ankit.rajmagram;

import java.util.ArrayList;

public class Globals {
    public static String RESULT_FROM_ASYNC_TASK;
    public static String SERVER_POINT = "https://test-app-goo.herokuapp.com/";
    public static int current_order_id;
    public static String current_receiver;
    public static String current_receiver_first_name;
    public static String current_receiver_last_name;
    public static String current_user;
    public static ArrayList msg_array = new ArrayList();
    public static String password;

    static {
        String str = "";
        current_user = str;
        password = str;
        current_receiver = str;
        current_receiver_first_name = str;
        current_receiver_last_name = str;
    }

    public static String login_url(String username, String password) {
        return String.format("%s/get_user?username=%s&password=%s&by_username=%s", new Object[]{SERVER_POINT, username, password, username});
    }

    public static String get_user_url(String username) {
        return String.format("%s/get_user?username=%s&password=%s&by_username=%s", new Object[]{SERVER_POINT, username, password, current_user});
    }

    public static String send_msg_url(String message) {
        return String.format("%s/send_message?from_username=%s&to_username=%s&message=%s&password=%s", new Object[]{SERVER_POINT, current_user, current_receiver, message, password});
    }

    public static String new_message_url() {
        return String.format("%s/check_new_messages?username=%s&password=%s", new Object[]{SERVER_POINT, current_user, password});
    }

    public static String sign_up_url(String first_name, String last_name, String username) {
        return String.format("%s/register?first_name=%s&last_name=%s&username=%s", new Object[]{SERVER_POINT, first_name, last_name, username});
    }
}
