package com.ankit.rajmagram;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "tasks";
    private static final String TASK_ID = "id";
    private static final String TASK_TITLE = "title";
    private static final String TASK_DESCP = "description";
    private static final String TASK_STATUS = "status";
    private static final String TASK_DATE = "date_created";
    private static final String TASK_REMIND_DATE = "reminder_date";



    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE tasks (task_id integer PRIMARY KEY autoincrement, title string, description string, status string, date_created date, reminder_date date, reminder_status string)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
        db.execSQL("CREATE TABLE auth (token string)");
        Log.d("hhhhhhhhhhhhheeeeeere", "Inserted");
        Context context;

    }

    public void save_token(String token) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("hhhhhhhhhhhhheeeeeere", "Inserted");
        db.execSQL(String.format("INSERT INTO auth VALUES('%s')", token));

        Cursor cursor = db.rawQuery("SELECT * FROM tasks", null);
        cursor.moveToFirst();
        Log.d("todotodo", cursor.getString(1));
    }

    public String get_token() {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM auth", null);
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        catch (CursorIndexOutOfBoundsException e){
            return "";
        }
    }
    // this method is use to add new course to our sqlite database.
    public void add_task(String title, String description) {

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("dataaaaaaaaaaaaaaa", "Inside current data");
        String query = String.format("INSERT INTO tasks (title, description, status, date_created) values ('%1$s', '%2$s', 'n', datetime('now', 'localtime'))", title, description);
        db.execSQL(query);
    }

    public int add_scheduled_task(String title, String description, String scheduled_time) {

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("dataaaaaaaaaaaaaaa", "Inside current data");
        String query = String.format("INSERT INTO tasks (title, description, status, date_created, reminder_date, reminder_status) values ('%1$s', '%2$s', 'n', datetime('now', 'localtime'), '%3$s', 's')", title, description, scheduled_time);
        db.execSQL(query);

        Cursor cursor = db.rawQuery("SELECT max(task_id) FROM tasks", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public void update_task(int task_id, String title, String description) {

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("dataaaaaaaaaaaaaaa", "Inside current data");
        String query = String.format("UPDATE tasks set title='%1$s', description='%2$s', date_created=datetime('now', 'localtime') WHERE task_id=%3$s", title, description, task_id);
        db.execSQL(query);
    }

    public void delete_task(int task_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("dataaaaaaaaaaaaaaa", "Inside current data");
        db.execSQL("DELETE FROM tasks WHERE task_id="+task_id);
    }

    public ArrayList<Object> get_task_details(int task_id) {
        ArrayList<Object> return_array = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("dataaaaaaaaaaaaaaa", "Inside current data");

        Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE task_id="+task_id, null);
        cursor.moveToFirst();
        return_array.add(cursor.getInt(0));
        return_array.add(cursor.getString(1));
        return_array.add(cursor.getString(2));
        return_array.add(cursor.getString(3));
        return_array.add(cursor.getString(4));
        return_array.add(cursor.getString(5));

        return return_array;
    }

    public void mark_task_as_done(int task_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE tasks set status = 'd' WHERE task_id="+task_id);
    }

    public void mark_task_as_undone(int task_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE tasks set status = 'n' WHERE task_id="+task_id);
    }

    public ArrayList<Object> get_scheduled_task(){
        ArrayList<java.lang.Object> return_array = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("dataaaaaaaaaaaaaaa", "Inside current data");

        Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE reminder_status='n' ORDER by task_id desc", null);
        cursor.moveToFirst();
        return_array.add(cursor.getInt(0));
        return_array.add(cursor.getString(1));
        return_array.add(cursor.getString(2));
        return_array.add(cursor.getString(3));
        return_array.add(cursor.getString(4));
        return_array.add(cursor.getString(5));

        return return_array;
    }

    public void mark_task_as_not_scheduled(int task_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE tasks set reminder_status = 'd' WHERE task_id="+task_id);
    }


    public ArrayList<ArrayList<Object>> get_all_task_details(String status_type) {
        ArrayList<ArrayList<Object>> return_array = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("dataaaaaaaaaaaaaaa", "Inside current data");

        Cursor cursor = db.rawQuery("SELECT * FROM tasks where status='"+status_type+"'"+" ORDER by task_id desc", null);

        if (cursor.moveToFirst()) {
            int counter = 0;
            do {
                ArrayList<Object> array_member = new ArrayList<>();
                int tsk_id = cursor.getInt(0);
                String title = cursor.getString(1);
                String desp = cursor.getString(2);
                String status = cursor.getString(3);
                String dc = cursor.getString(4);
                String rd = cursor.getString(5);

                array_member.add(tsk_id);
                array_member.add(title);
                array_member.add(desp);
                array_member.add(status);
                array_member.add(dc);
                array_member.add(rd);

                Log.d("todotodo", String.valueOf(tsk_id));

                return_array.add(array_member);


            }
            while (cursor.moveToNext()) ;
        }
        Log.d("mytodo", "hereeeeeee");

        return return_array;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.d("a", "hereeeeeeeeeeeeee");
    }
}
