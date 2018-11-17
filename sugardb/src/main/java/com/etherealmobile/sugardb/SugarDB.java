package com.etherealmobile.sugardb;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class SugarDB extends SQLiteOpenHelper {


    // Variables
    private String DATABASE_NAME, TABLE_NAME = "DEMO_TABLE", SQL = "";
    private ArrayList<Column> columns = new ArrayList<>();
    private SQLiteDatabase writableDatabase;
    ContentValues contentValues = new ContentValues();
    private boolean initedDb = false;

    //
    public SugarDB addData(int columnNumber, String data) {
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columns.get(columnNumber - 1).columnName, data);
        return this;
    }

    public SugarDB addData(String columnName, String data) {
        columnName = columnName.replaceAll(" ", "_");
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columnName, data);
        return this;
    }

    public SugarDB addData(int columnNumber, int data) {
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columns.get(columnNumber - 1).columnName, data);
        return this;
    }

    public SugarDB addData(String columnName, int data) {
        columnName = columnName.replaceAll(" ", "_");
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columnName, data);
        return this;
    }


    public boolean doneDataAdding() {
        long result = writableDatabase.insert(TABLE_NAME, null, contentValues);
        contentValues = new ContentValues();

        if (result == -1)
            return false;
        else
            return true;
    }

    //
    public Cursor getAllData() {
        if (!initedDb || writableDatabase == null) initDatabase();
        Cursor res = writableDatabase.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor getOneRowData(int id) {
        if (!initedDb || writableDatabase == null) initDatabase();
        String allColNames[] = new String[columns.size() + 1];
        allColNames[0] = "ID";
        for (int i = 0; i < columns.size(); i++) {
            allColNames[i + 1] = columns.get(i).columnName;
        }
        Cursor cursor = writableDatabase.query(TABLE_NAME,
                allColNames,
                allColNames[0].toString() + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, "1");

        if (cursor.getCount() > 0) {
            return cursor;
        } else {
            return null;
        }
    }

    public boolean matchColumns(String columnsToMatch[], String valuesToMatch[]) {
        String query = "";
        // columnsToMatch.get(0).columnName + " = ?" + " AND " + columnsToMatch.get(2).columnName + " = ?";
        for (int i = 0; i < columnsToMatch.length; i++) {
            query += columnsToMatch[i] + " = ? ";
            if (i != columnsToMatch.length - 1) {
                query += " AND ";
            }
        }
        Cursor cursor = writableDatabase.query(TABLE_NAME, columnsToMatch, query, valuesToMatch, null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //
    public SugarDB updateData(int columnNumber, String data) {
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columns.get(columnNumber - 1).columnName, data);
        return this;
    }

    public SugarDB updateData(int columnNumber, int data) {
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columns.get(columnNumber - 1).columnName, data);
        return this;
    }

    public boolean rowID(int id) {
        try {
            return writableDatabase.update(TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(id)}) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    //
    public boolean deleteRow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)}) == 1;
    }

    public void deleteAllDataFromTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }

    //
    public SugarDB setTableName(String tableName) {
        this.TABLE_NAME = tableName.replaceAll(" ", "_");
        return this;
    }

    public SugarDB addColumn(Column column) {
        columns.add(column);
        return this;
    }

    public SugarDB doneTableColumn() {
        SQL = " CREATE TABLE " + TABLE_NAME + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, ";
        for (int i = 0; i < columns.size(); i++) {
            SQL += " " + columns.get(i).columnName + " " + columns.get(i).columnDataType + " ";
            if (i == columns.size() - 1) {
                SQL += " ) ";
            } else {
                SQL += " , ";
            }
        }

        if (!initedDb || writableDatabase == null) initDatabase();
        return this;
    }

    //
    public String[] getAllColumns() {
        String allColNames[] = new String[columns.size() + 1];
        allColNames[0] = "ID";
        for (int i = 0; i < columns.size(); i++) {
            allColNames[i + 1] = columns.get(i).columnName;
        }
        return allColNames;
    }

    //
    public void initDatabase() {
        writableDatabase = getWritableDatabase();
        initedDb = true;
    }

    //
    public static SugarDB init(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        if (!dbName.endsWith(".db"))
            dbName += ".db";
        dbName = dbName.replaceAll(" ", "_");
        return new SugarDB(context, dbName, factory, version);
    }

    public static SugarDB init(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        if (!dbName.endsWith(".db"))
            dbName += ".db";
        dbName = dbName.replaceAll(" ", "_");
        return new SugarDB(context, dbName, factory, version, errorHandler);
    }

    //
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.writableDatabase = db;
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Saving, just in case :)
    // Codes below this might once or never be used :D
    private Context context;
    private SQLiteDatabase.CursorFactory factory;
    private int version;
    private DatabaseErrorHandler errorHandler;

    private SugarDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        //
        this.context = context;
        this.DATABASE_NAME = name;
        this.factory = factory;
        this.version = version;
    }

    private SugarDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);

        //
        this.context = context;
        this.DATABASE_NAME = name;
        this.factory = factory;
        this.version = version;
        this.errorHandler = errorHandler;
    }
}