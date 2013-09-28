package com.kmeaw.extcalc;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbOpenHelper extends SQLiteOpenHelper{

  private static final int DB_VERSION = 1;
  private static final String DB_NAME = "report";

  public static final String TABLE_NAME = "results";
  private static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( id integer primary key autoincrement, num1 integer, num2 integer, op integer, result integer, user integer, created_at TIMESTAMP default (DATETIME('now')) NOT NULL, done integer default 0 NOT NULL )";

  public DbOpenHelper(Context context) {
    super(context, DB_NAME, null,DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
  }
}
