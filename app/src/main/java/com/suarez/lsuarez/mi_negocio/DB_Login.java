package com.suarez.lsuarez.mi_negocio;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DB_Login extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "alta_revend.db";
    public static final String TABLE_NAME = "login";
    public static final String COLUMN_CLIENTE = "CLIENTE";
    public static final String COLUMN_DNI = "DNI";
    public static final String COLUMN_NOMBRE = "NOMBRE";
    public static final String COLUMN_FOTO = "FOTO";
    public static final String COLUMN_ZONA = "ZONA";

    public DB_Login(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_CLIENTE + " INTEGER PRIMARY KEY, " + COLUMN_DNI +
                " INTEGER, " + COLUMN_NOMBRE + " TEXT, " + COLUMN_FOTO + " TEXT, "
                + COLUMN_ZONA + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(int cliente, int dni, String nombre, String foto, String zona) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CLIENTE,cliente);
        contentValues.put(COLUMN_DNI,dni);
        contentValues.put(COLUMN_NOMBRE,nombre);
        contentValues.put(COLUMN_FOTO,foto);
        contentValues.put(COLUMN_ZONA,zona);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean updateFoto(String cliente, String foto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FOTO,foto);
        db.update(TABLE_NAME, contentValues, "CLIENTE = ?",new String[] {String.valueOf(cliente)});
        return true;
    }

    public Integer deleteData (String cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "CLIENTE = ?",new String[] {cliente});
    }
}