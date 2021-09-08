package com.rmproduct.automaticstatement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Statement.db";

    //for general note
    private static final int VERSION = 5;
    private static final String TABLE_NAME = "AutomaticStatement";
    private static final String ID = "id";
    private static final String SERIAL_NO = "SerialNo";
    private static final String SAMPLE_NO = "SampleNo";
    private static final String DATE_ACCEPT = "Date_of_Acceptance";
    private static final String DEPT = "Department";
    private static final String TOTAL_MONEY = "Total_Earned_Money";
    private static final String TAX = "Tax";
    private static final String REMAIN_MONEY = "Remain_Money_after_deduction";
    private static final String UNI_AUTHORITY = "University_Authority";
    private static final String TCS_WING = "TCS_Wing";
    private static final String LAB_CHECK = "Money_provide_the_Lab_by_check";

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
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SERIAL_NO + " INTEGER(5), "
                    + SAMPLE_NO + " INTEGER(5), "
                    + DATE_ACCEPT + " VARCHAR(20), "
                    + DEPT + " VARCHAR(100), "
                    + TOTAL_MONEY + " FLOAT(15,3), "
                    + TAX + " FLOAT(15,3), "
                    + REMAIN_MONEY + " FLOAT(15,3), "
                    + UNI_AUTHORITY + " FLOAT(15,3), "
                    + TCS_WING + " FLOAT(15,3), "
                    + LAB_CHECK + " FLOAT(15,3) );";
            db.execSQL(CREATE_TABLE);

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

    public long makeStatementRow(int slNo, int sampleNo, String dateAccept, String dept, float totalMoney, float tax, float remainMoney, float uniAuthority, float tcsWing, float labCheck) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //String QUERY= "INSERT INTO "+TABLE_NAME+" ( "+SERIAL_NO+", "+SAMPLE_NO+", "+DATE_ACCEPT+", "+DEPT+", "+TOTAL_MONEY+", "+TAX+", "+REMAIN_MONEY+", "+UNI_AUTHORITY+", "+TCS_WING+", "+LAB_CHECK+") VALUES ( "+slNo+", "+sampleNo+", "+dateAccept+", "+dept+", "+totalMoney+", "+tax+", "+remainMoney+", "+uniAuthority+", "+tcsWing+", "+labCheck+");";

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

                models.add(model);
            } while ((cursor.moveToNext()));
        }
        sqLiteDatabase.close();

        return models;
    }

    public int deleteStatement(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NAME, ID + " = ?", new String[]{id});
    }
}
