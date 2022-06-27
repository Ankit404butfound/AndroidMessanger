package com.ankit.rajmagram;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DATE = "date";
    private static final String DB_NAME = "db";
    private static final int DB_VERSION = 1;
    private static final String LOGIN_TABLE_NAME = "LOGIN_DETAILS";
    private static final String MESSAGE = "message";
    private static final String MESSAGE_ID = "message_id";
    private static final String MESSAGE_TABLE = "MESSAGE_DETAILS";
    private static final String SENDER = "sender";
    private static final String STATUS = "status";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE LOGIN_DETAILS (first_name string, last_name string, username string, password string, fcm_token string)");
        db.execSQL("CREATE TABLE USER_DETAILS (order_id integer PRIMARY KEY autoincrement, first_name string, last_name string, username string, last_message string)");
        db.execSQL("CREATE TABLE MESSAGE_DETAILS (message_id integer PRIMARY KEY autoincrement, message string, sender string, receiver string, status string, date string)");
        Log.d("RajMaGram", "Table created");
    }

    public void save_token(String token) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format("UPDATE LOGIN_DETAILS set fcm_token='%s'", new Object[]{token}));
        Cursor cursor = db.rawQuery("SELECT fcm_token FROM tasks", null);
        cursor.moveToFirst();
        Log.d("RajMaGram", cursor.getString(1));
    }

    public String get_token() {
        try {
            Cursor cursor = getWritableDatabase().rawQuery("SELECT fcm_token FROM tasks", null);
            cursor.moveToFirst();
            return cursor.getString(0);
        } catch (CursorIndexOutOfBoundsException e) {
            return "";
        }
    }

    public int add_incoming_message(String message, String sender, String date, String first_name, String last_name) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format("INSERT INTO MESSAGE_DETAILS (message, sender, status, date, receiver) values ('%1$s', '%2$s', 'n', %3$s, '%4$s')", new Object[]{message, sender, date, Globals.current_user}));
        if (get_user(sender).size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DELETE FROM USER_DETAILS WHERE username='");
            stringBuilder.append(sender);
            stringBuilder.append("'");
            db.execSQL(stringBuilder.toString());
        }
        db.execSQL(String.format("INSERT INTO USER_DETAILS (first_name, last_name, username, last_message) values ('%s', '%s', '%s', '%s')", new Object[]{first_name, last_name, sender, message}));
        Cursor cursor = db.rawQuery("SELECT max(order_id) FROM USER_DETAILS", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int add_outgoing_message(String message, String receiver, String first_name, String last_name) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format("INSERT INTO MESSAGE_DETAILS (message, sender, status, date, receiver) values ('%1$s', '%2$s', 'n', datetime('now', 'localtime'), '%3$s')", new Object[]{message, Globals.current_user, receiver}));
        if (get_user(receiver).size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DELETE FROM USER_DETAILS WHERE username='");
            stringBuilder.append(receiver);
            stringBuilder.append("'");
            db.execSQL(stringBuilder.toString());
        }
        db.execSQL(String.format("INSERT INTO USER_DETAILS (first_name, last_name, username, last_message) values ('%s', '%s', '%s', '%s')", new Object[]{first_name, last_name, receiver, message}));
        Cursor cursor = db.rawQuery("SELECT max(order_id) FROM USER_DETAILS", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public ArrayList<Object> get_user_by_order_id(int order_id) {
        ArrayList<Object> return_array = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT order_id, first_name, last_name, username, last_message FROM USER_DETAILS WHERE order_id=");
        stringBuilder.append(order_id);
        Cursor cursor = db.rawQuery(stringBuilder.toString(), null);
        cursor.moveToFirst();
        try {
            return_array.add(Integer.valueOf(cursor.getInt(0)));
            return_array.add(cursor.getString(1));
            return_array.add(cursor.getString(2));
            return_array.add(cursor.getString(3));
            return_array.add(cursor.getString(4));
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return return_array;
    }

    public ArrayList<Object> get_user(String username) {
        ArrayList<Object> return_array = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT order_id, first_name, last_name, username, last_message FROM USER_DETAILS WHERE username='");
        stringBuilder.append(username);
        stringBuilder.append("'");
        Cursor cursor = db.rawQuery(stringBuilder.toString(), null);
        cursor.moveToFirst();
        try {
            return_array.add(Integer.valueOf(cursor.getInt(0)));
            return_array.add(cursor.getString(1));
            return_array.add(cursor.getString(2));
            return_array.add(cursor.getString(3));
            return_array.add(cursor.getString(4));
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return return_array;
    }

    public ArrayList<Object> get_login_details() {
        ArrayList<Object> return_array = new ArrayList();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM LOGIN_DETAILS", null);
        cursor.moveToFirst();
        try {
            return_array.add(cursor.getString(0));
            return_array.add(cursor.getString(1));
            return_array.add(cursor.getString(2));
            return_array.add(cursor.getString(3));
            return_array.add(cursor.getString(4));
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return return_array;
    }

    public void save_login_details(String first_name, String last_name, String username, String password) {
        getWritableDatabase().execSQL(String.format("INSERT INTO LOGIN_DETAILS (first_name, last_name, username, password) values ('%s', '%s', '%s', '%s')", new Object[]{first_name, last_name, username, password}));
    }

    public ArrayList<ArrayList<Object>> get_all_users() {
        ArrayList<ArrayList<Object>> return_array = new ArrayList();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM USER_DETAILS ORDER BY order_id DESC", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ArrayList<Object> array_member = new ArrayList();
                    int order_id = cursor.getInt(0);
                    String first_name = cursor.getString(1);
                    String last_name = cursor.getString(2);
                    String username = cursor.getString(3);
                    String message = cursor.getString(4);
                    array_member.add(Integer.valueOf(order_id));
                    array_member.add(first_name);
                    array_member.add(last_name);
                    array_member.add(username);
                    array_member.add(message);
                    return_array.add(array_member);
                } while (cursor.moveToNext());
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return return_array;
    }

    public ArrayList<ArrayList<Object>> get_messages(String username) {
        ArrayList<ArrayList<Object>> return_array = new ArrayList();
        Cursor cursor = getWritableDatabase().rawQuery(String.format("SELECT * FROM MESSAGE_DETAILS where sender='%1$s' or receiver='%1$s'", new Object[]{username}), null);
        if (cursor.moveToFirst()) {
            do {
                ArrayList<Object> array_member = new ArrayList();
                int message_id = cursor.getInt(0);
                String message = cursor.getString(1);
                String sender = cursor.getString(2);
                String receiver = cursor.getString(3);
                String status = cursor.getString(4);
                String date = cursor.getString(5);
                array_member.add(Integer.valueOf(message_id));
                array_member.add(message);
                array_member.add(sender);
                array_member.add(receiver);
                array_member.add(status);
                array_member.add(date);
                return_array.add(array_member);
            } while (cursor.moveToNext());
        }
        return return_array;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS LOGIN_DETAILS");
        onCreate(db);
    }
}
