package com.example.calcprocalculator.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MyDatabase extends SQLiteOpenHelper {

    public static final String TABLE_HISTORY = "history";
    public static final String CO_ID = "id";
    public static final String CO_EXPRESSION = "expression";
    public static final String CO_RESULT = "result";
    public static final String CO_TIMER = "TIMER";


    public MyDatabase(@Nullable Context context) {
        super(context, "Database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String history = "CREATE TABLE " + TABLE_HISTORY + " ("
                + CO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CO_EXPRESSION + " TEXT NOT NULL, "
                + CO_RESULT + " TEXT NOT NULL, "
                + CO_TIMER + " TEXT NOT NULL"
                + ")";

        db.execSQL(history);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_HISTORY);
        onCreate(db);

    }

    public void insertHistory(String expression , String result , String timer){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CO_EXPRESSION,expression);
        cv.put(CO_RESULT,result);
        cv.put(CO_TIMER,timer);
        long j = db.insert(TABLE_HISTORY,null,cv);
        db.close();

    }

    // ===== جلب كل السجلات =====
    public List<HistoryModel> getAllHistory() {
        List<HistoryModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HISTORY + " ORDER BY " + CO_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                HistoryModel item = new HistoryModel(
                        cursor.getString(cursor.getColumnIndexOrThrow(CO_EXPRESSION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CO_RESULT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CO_TIMER))
                );
                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // ===== حذف كل السجلات =====
    public void deleteAllHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, null, null);
        db.close();
    }



}
