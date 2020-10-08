package tw.helper.medicalcare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class schedule_dbhelper extends SQLiteOpenHelper {

    private final static int _DBVersion = 1;
    private final static String  _DBName = "schedule.db";
    private final static String  _TableName = "schedule";

    public schedule_dbhelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL = "CREATE TABLE IF NOT EXISTS  schedule  ( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "member_id TEXT,"
                + "case_id TEXT,"
                + "calender_id TEXT,"
                + "date_start TEXT,"
                + "date_end TEXT,"
                + "time_start TEXT,"
                + "time_end TEXT ,"
                + "hour TEXT ,"
                + "week TEXT,"
                + "name TEXT,"
                + "phone TEXT,"
                + "address TEXT,"
                + "service TEXT);";

        db.execSQL(SQL);


        //  id,case_id,date_start,date_end,time_start,time_end,week,name,phone.address,service


        final String SQL1 = "CREATE TABLE IF NOT EXISTS     record ( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "member_id TEXT,"
                + "case_id TEXT,"
                + "calender_id TEXT,"
                + "schedule_id TEXT,"
                + "date_start TEXT,"
                + "date_end TEXT,"
                + "time_start TEXT,"
                + "time_end TEXT ,"
                + "hour TEXT ,"
                + "week TEXT,"
                + "wek TEXT,"
                + "name TEXT,"
                + "phone TEXT,"
                + "address TEXT,"
                + "service TEXT,"
                + "date TEXT,"
                + "year TEXT,"
                + "month TEXT,"
                + "day TEXT,"
                + "blood_pressure_big TEXT,"
                + "blood_pressure_small TEXT,"
                + "blood_sugar_before TEXT,"
                + "blood_sugar_after TEXT,"
                + "weight TEXT,"
                + "height TEXT );";

        db.execSQL(SQL1);












        final String SQL2 = "CREATE TABLE IF NOT EXISTS     case_info ( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "CID TEXT,"
                + "FID TEXT,"
                + "DID TEXT,"
                + "HID TEXT,"
                + "name TEXT,"
                + "birthday TEXT,"
                + "age TEXT,"
                + "sex TEXT ,"
                + "email TEXT ,"
                + "phone TEXT,"
                 + "address TEXT );";

        db.execSQL(SQL2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL = "DROP TABLE " + _TableName;
        db.execSQL(SQL);
    }
}
