package com.rmproduct.automaticstatement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Statement.db";

    //for general note
    public static int VERSION = 21;
    public static final String TABLE_NAME = "AutomaticStatement";
    public static final String SERIAL_NO = "SerialNo";
    public static final String SAMPLE_NO = "SampleNo";
    public static final String DATE_ACCEPT = "DateOfAcceptance";
    public static final String DEPT = "Department";
    public static final String TOTAL_MONEY = "TotalEarnedMoney";
    public static final String TAX = "Tax";
    public static final String REMAIN_MONEY = "RemainMoneyAfterDeduction";
    public static final String UNI_AUTHORITY = "UniversityAuthority";
    public static final String TCS_WING = "TCSWing";
    public static final String LAB_CHECK = "MoneyProvideToTheDept";
    public static final String TEST_COST = "TestCost";
    public static final String PI = "PI";
    public static final String CHAIR = "Chair";
    public static final String TEACHERS = "Teachers";
    public static final String LAB_DEV = "LabDevelopment";
    public static final String STAFF = "Staff";

    //private static final
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SELECT_STATEMENT = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + SERIAL_NO + " ASC";

    Context context;

    @RequiresApi(api = Build.VERSION_CODES.P)
    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
                    + SERIAL_NO + " INTEGER PRIMARY KEY NOT NULL, "
                    + SAMPLE_NO + " INTEGER(5), "
                    + DATE_ACCEPT + " VARCHAR(20), "
                    + DEPT + " VARCHAR(100), "
                    + TOTAL_MONEY + " FLOAT(15.3), "
                    + TAX + " FLOAT(15.3), "
                    + REMAIN_MONEY + " FLOAT(15.3), "
                    + UNI_AUTHORITY + " FLOAT(15.3), "
                    + TCS_WING + " FLOAT(15.3), "
                    + LAB_CHECK + " FLOAT(15.3), "
                    + TEST_COST + " FLOAT(15.3), "
                    + PI + " FLOAT(15.3), "
                    + CHAIR + " FLOAT(15.3), "
                    + TEACHERS + " FLOAT(15.3), "
                    + LAB_DEV + " FLOAT(15.3), "
                    + STAFF + " FLOAT(15.3));";
            db.execSQL(CREATE_TABLE);

            /*String QUERY = "INSERT INTO " + TABLE_NAME + " ( " + SERIAL_NO + ", " + SAMPLE_NO + ", " + DATE_ACCEPT + ", " + DEPT + ", " + TOTAL_MONEY + ", " + TAX + ", " + REMAIN_MONEY + ", " + UNI_AUTHORITY + ", " + TCS_WING + ", " + LAB_CHECK + ") VALUES ( 1001, null, null, 'Total', 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);";
            db.execSQL(QUERY);*/

        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {

            db.execSQL(DROP_TABLE);
            onCreate(db);

        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
        }
    }

    public long makeStatementRow(int slNo, int sampleNo, String dateAccept, String dept, float totalMoney, float tax, float remainMoney, float uniAuthority, float tcsWing, float labCheck, float testCost, float pi, float chair, float teachers, float labDev, float staff) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        /*String QUERY = "INSERT INTO " + TABLE_NAME + " ( " + SERIAL_NO + ", " + SAMPLE_NO + ", " + DATE_ACCEPT + ", " + DEPT + ", " + TOTAL_MONEY + ", " + TAX + ", " + REMAIN_MONEY + ", " + UNI_AUTHORITY + ", " + TCS_WING + ", " + LAB_CHECK + ") VALUES ( 'Serial\nNo', 'Sample\nNo', 'Date of\nAcceptance', 'Department', 'Total Earned\nMoney', 'Tax 10%', 'Remaining Money\nafter deduction', 'University\nAuthority 25%', 'TCS Wing\n5%', 'Money provide\nthe Lab by check');";
        sqLiteDatabase.execSQL(QUERY);*/

        ContentValues values = new ContentValues();
        values.put(SERIAL_NO, slNo);
        values.put(SAMPLE_NO, sampleNo);
        values.put(DATE_ACCEPT, dateAccept);
        values.put(DEPT, dept);
        values.put(TOTAL_MONEY, totalMoney);
        values.put(TAX, tax);
        values.put(REMAIN_MONEY, remainMoney);
        values.put(UNI_AUTHORITY, uniAuthority);
        values.put(TCS_WING, tcsWing);
        values.put(LAB_CHECK, labCheck);
        values.put(TEST_COST, testCost);
        values.put(PI, pi);
        values.put(CHAIR, chair);
        values.put(TEACHERS, teachers);
        values.put(LAB_DEV, labDev);
        values.put(STAFF, staff);

        return sqLiteDatabase.insert(TABLE_NAME, null, values);
        //sqLiteDatabase.execSQL(QUERY);
    }

    public List<StatementModel> statementModelList() {
        List<StatementModel> models = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_STATEMENT, null);

        if (cursor.moveToFirst()) {
            do {
                StatementModel model = new StatementModel();

                model.setSlNo(cursor.getInt(cursor.getColumnIndex(SERIAL_NO)));
                model.setSampleNo(cursor.getInt(cursor.getColumnIndex(SAMPLE_NO)));
                model.setDateAccept(cursor.getString(cursor.getColumnIndex(DATE_ACCEPT)));
                model.setDept(cursor.getString(cursor.getColumnIndex(DEPT)));
                model.setTotalEarned(cursor.getFloat(cursor.getColumnIndex(TOTAL_MONEY)));
                model.setTax(cursor.getFloat(cursor.getColumnIndex(TAX)));
                model.setRemainMoney(cursor.getFloat(cursor.getColumnIndex(REMAIN_MONEY)));
                model.setUniAuthority(cursor.getFloat(cursor.getColumnIndex(UNI_AUTHORITY)));
                model.setTcsWing(cursor.getFloat(cursor.getColumnIndex(TCS_WING)));
                model.setLabCheck(cursor.getFloat(cursor.getColumnIndex(LAB_CHECK)));
                model.setTestCost(cursor.getFloat(cursor.getColumnIndex(TEST_COST)));
                model.setPi(cursor.getFloat(cursor.getColumnIndex(PI)));
                model.setChair(cursor.getFloat(cursor.getColumnIndex(CHAIR)));
                model.setTeachers(cursor.getFloat(cursor.getColumnIndex(TEACHERS)));
                model.setLabDev(cursor.getFloat(cursor.getColumnIndex(LAB_DEV)));
                model.setStaff(cursor.getFloat(cursor.getColumnIndex(STAFF)));

                models.add(model);
            } while ((cursor.moveToNext()));
        }
        sqLiteDatabase.close();

        return models;
    }

    public void deleteStatement(int id) {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + SERIAL_NO + " = " + id;
            database.execSQL(deleteQuery);
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    public void updateRow(String key) {
        SQLiteDatabase database = this.getWritableDatabase();

    }

    public boolean dropStatement(String table) {
        boolean state = false;
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            String dropQuery = "DROP TABLE " + table + ";";
            database.execSQL(dropQuery);
            onCreate(database);
            state = true;
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            state = false;
        }
        return state;
    }

    public long countRow() {
        SQLiteDatabase database = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(database, TABLE_NAME);
    }

    public String getValue(String keys, String clue) {
        String value = "";
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            String getValue = "SELECT " + keys + " FROM " + TABLE_NAME + " WHERE " + keys + " = '" + clue + "'";
            Cursor cursor = database.rawQuery(getValue, null);
            if (cursor.moveToFirst()) {
                do {
                    value = cursor.getString(cursor.getColumnIndex(keys));
                } while (cursor.moveToNext());
            }
            database.close();
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
        return value;
    }

    public float getSum(String clue) {
        float sum = 0;
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            String query = "SELECT " + clue + " FROM " + TABLE_NAME;
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    sum = cursor.getFloat(cursor.getColumnIndex(clue)) + sum;
                } while (cursor.moveToNext());
            }
            database.close();
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
        return sum;
    }
}
