package com.example.todocalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public DBHelper(Context context) {
        super(context, "DB", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createQuery = "create table tb_data " +
                "( id long primary key, " +  // setting the saved time = id
                "title, " +  // todo
                "start_h, " +
                "start_m, " +
                "end_h, " +
                "end_m, " +
                "memo, " +
                "tf, " +  //checkbox: checked or not (true=1, false=0)
                "year, " +
                "mon, " +
                "day );";
        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String createQuery = "drop table if exists tb_data;";
        sqLiteDatabase.execSQL(createQuery);
        onCreate(sqLiteDatabase);

    }

    void insert(long id, String title, int sh, int sm, int eh, int em, String memo,
                int tf, String year, String mon, String day) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("title", title);
        contentValues.put("start_h", sh);
        contentValues.put("start_m", sm);
        contentValues.put("end_h", eh);
        contentValues.put("end_m", em);
        contentValues.put("memo", memo);
        contentValues.put("tf", tf);
        contentValues.put("year", year);
        contentValues.put("mon", mon);
        contentValues.put("day", day);

        db.insert("tb_data", null, contentValues);
    }

    void delete(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("tb_data", "id="+id, null);
    }


    void update(long id, String title, int sh, int sm, int eh, int em, String memo,
                int tf, String year, String mon, String day) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("title", title);
        contentValues.put("start_h", sh);
        contentValues.put("start_m", sm);
        contentValues.put("end_h", eh);
        contentValues.put("end_m", em);
        contentValues.put("memo", memo);
        contentValues.put("tf", tf);
        contentValues.put("year", year);
        contentValues.put("mon", mon);
        contentValues.put("day", day);

        db.update("tb_data", contentValues, "id="+id, null);
    }





}
