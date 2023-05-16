package com.example.pingpongtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Nomes das tabelas
    private static final String TABLE_USERS = "users";

    // Nomes das colunas
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    // Comandos para criar as tabelas
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_PASSWORD + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Apaga a tabela antiga se ela existir
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Cria as tabelas novamente
        onCreate(db);
    }

    // Insere um novo usuário no banco de dados
    public long createUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);

        // Insere a linha
        long id = db.insert(TABLE_USERS, null, values);

        // Fecha a conexão com o banco de dados
        db.close();

        // Retorna o id da linha recém inserida
        return id;
    }

    // Verifica se um usuário com o email e senha fornecidos existe no banco de dados
    public boolean checkUser(String email, String password) {
        String[] columns = { KEY_ID };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_EMAIL + " = ?" + " AND " + KEY_PASSWORD + " = ?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        if (count > 0) {
            return true;
        }

        return false;
    }

}
