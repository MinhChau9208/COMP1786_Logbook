package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todolist";
    private static final String ID_COLUMN_NAME = "_id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String DEADLINE_COLUMN_NAME = "deadline";
    private static final String DURATION_COLUMN_NAME = "duration";
    private static final String DESCRIPTIONS_COLUMN_NAME = "descriptions";
    private static final String COMPLETED_COLUMN_NAME = "completed";

    private static final String DATABASE_CREATE_QUERY = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s INTEGER NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s INTEGER NOT NULL);",
            DATABASE_NAME, ID_COLUMN_NAME, NAME_COLUMN_NAME, DEADLINE_COLUMN_NAME,
            DURATION_COLUMN_NAME, DESCRIPTIONS_COLUMN_NAME, COMPLETED_COLUMN_NAME
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        Log.w(this.getClass().getName(), DATABASE_NAME + " database upgrade to version "
                + newVersion + " - old data lost");
        onCreate(db);
    }

    public long insertDetails(String name, Date deadline, int duration, String descriptions, boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN_NAME, name);
        values.put(DEADLINE_COLUMN_NAME, String.valueOf(deadline.getTime()));
        values.put(DURATION_COLUMN_NAME, duration);
        values.put(DESCRIPTIONS_COLUMN_NAME, descriptions);
        values.put(COMPLETED_COLUMN_NAME, completed ? 1 : 0);
        long result = db.insert(DATABASE_NAME, null, values);
        db.close();
        return result;
    }

    public ArrayList<Task> getDetails() {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_NAME,
                new String[]{ID_COLUMN_NAME, NAME_COLUMN_NAME, DEADLINE_COLUMN_NAME,
                        DURATION_COLUMN_NAME, DESCRIPTIONS_COLUMN_NAME, COMPLETED_COLUMN_NAME},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                long deadlineMillis = Long.parseLong(cursor.getString(2));
                int duration = cursor.getInt(3);
                String description = cursor.getString(4);
                boolean completed = cursor.getInt(5) == 1;

                Task task = new Task(name, new Date(deadlineMillis), duration, description, completed);
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }

    public long getTaskId(Task task) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_NAME,
                new String[]{ID_COLUMN_NAME},
                NAME_COLUMN_NAME + "=? AND " + DEADLINE_COLUMN_NAME + "=? AND " +
                        DURATION_COLUMN_NAME + "=? AND " + DESCRIPTIONS_COLUMN_NAME + "=? AND " +
                        COMPLETED_COLUMN_NAME + "=?",
                new String[]{task.name, String.valueOf(task.deadline.getTime()),
                        String.valueOf(task.duration), task.descriptions, task.completed ? "1" : "0"},
                null, null, null);
        long id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return id;
    }

    public int updateTask(long id, String name, Date deadline, int duration, String descriptions, boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN_NAME, name);
        values.put(DEADLINE_COLUMN_NAME, String.valueOf(deadline.getTime()));
        values.put(DURATION_COLUMN_NAME, duration);
        values.put(DESCRIPTIONS_COLUMN_NAME, descriptions);
        values.put(COMPLETED_COLUMN_NAME, completed ? 1 : 0);
        int rowsAffected = db.update(DATABASE_NAME, values, ID_COLUMN_NAME + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    public void deleteTask(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_NAME, ID_COLUMN_NAME + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}