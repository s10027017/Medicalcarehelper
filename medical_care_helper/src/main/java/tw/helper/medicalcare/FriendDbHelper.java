package tw.helper.medicalcare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FriendDbHelper extends SQLiteOpenHelper {
    public String sCreateTableCommand;    // 資料庫名稱
    private static final String DB_FILE = "friends.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;    // 資料表名稱
    private static SQLiteDatabase database;

    private static final String DB_TABLE = "carry_info_1";
    private static final String DB_TABLE_carry_info_1 = "carry_info_1";    // 資料庫物件，固定的欄位變數
    private static final String crTBsql = "CREATE     TABLE   " + DB_TABLE + "   ( "
            + "id    INTEGER   PRIMARY KEY," + "area TEXT ," + "phone TEXT ," + "name TEXT," + "address TEXT ," + "area2 TEXT ,"
            + "phone2 TEXT," + "name2 TEXT ," + "address2 TEXT);";
    //----------------case_info--------------------------------------------------------------------------------------------------------------------------------------------------------
    private static final String DB_TABLE_case_info = "case_info";
    private static final String crTBsql_case_info = "CREATE     TABLE   " + DB_TABLE_case_info + "   ( "
            + " CID    INTEGER   PRIMARY KEY , " + " FID TEXT ," + " DID TEXT ," + " HID TEXT ," + " name TEXT , "
            + " birthday TEXT , " + " age TEXT , " + " sex TEXT , " + " email TEXT , " + " phone TEXT , " + " address TEXT );";
    //----------------news--------------------------------------------------------------------------------------------------------------------------------------
    private static final String DB_TABLE_news = "news_info";    // 資料庫物件，固定的欄位變數
    private static final String crTBsql_news = "CREATE     TABLE   " + DB_TABLE_news + "   ( "
            + "id    INTEGER   PRIMARY KEY," + "title TEXT ," + "content TEXT,"
            + "url TEXT," + "createdate TEXT);";
    //----------------signup--------------------------------------------------------------------------------------------------------------------------------------
    private static final String DB_TABLE_signup = "sign_up";    // 資料庫物件，固定的欄位變數
    private static final String crTBsql_signup = "CREATE     TABLE   " + DB_TABLE_signup + "   ( "
            + "ID    INTEGER   PRIMARY KEY," + "Email TEXT ," + "Username TEXT," + "Password TEXT,"
            + "Phone TEXT ," + "Address TEXT ," + "Datetime TEXT ," + "Uid TEXT ," + " Membertype TEXT);";    //
    //----------------record25--------------------------------------------------------------------------------------------------------------------------------------
    private static final String DB_TABLE_record = "record25";    // 資料庫物件，固定的欄位變數
    private static final String crTBsql_record = "CREATE     TABLE   " + DB_TABLE_record + "   ( "
            + " id    INTEGER   PRIMARY KEY , " + " name TEXT ," + " record TEXT ," + " required TEXT ,"
            + " types TEXT ," + " details TEXT ," + " startdate  TEXT ," + " starttime  TEXT ," + " enddate  TEXT ," + " endtime TEXT );";
    //----------------record25--------------------------------------------------------------------------------------------------------------------------------------
    //----------------schedule--------------------------------------------------------------------------------------------------------------------------------------
    private static final String DB_TABLE_schedule = "schedule";    // 資料庫物件，固定的欄位變數
    private static final String crTBsql_schedule = "CREATE     TABLE   " + DB_TABLE_schedule + "   ( "
            + " id    INTEGER   PRIMARY KEY , " + " _id TEXT ," + " member_id TEXT ," + " case_id TEXT ,"
            + " calender_id TEXT ," + " date_start TEXT ," + " date_end  TEXT ," + " time_start  TEXT ,"
            + " time_end  TEXT ," + " hour  TEXT ," + " week  TEXT ," + " name  TEXT ,"
            + " phone  TEXT ," + " address  TEXT ," + " service TEXT );";
    //----------------schedule--------------------------------------------------------------------------------------------------------------------------------------
    //----------------record19--------------------------------------------------------------------------------------------------------------------------------------
    private static final String DB_TABLE_record19 = "record";    // 資料庫物件，固定的欄位變數
    private static final String crTBsql_record19 = "CREATE     TABLE   " + DB_TABLE_record19 + "   ( "
            + " id    INTEGER   PRIMARY KEY , " + " _id TEXT ," + " member_id TEXT ," + " case_id TEXT ," + " calender_id TEXT ," + " schedule_id TEXT ,"
            + " date_start  TEXT ," + " date_end  TEXT ," + " time_start  TEXT ," + " time_end  TEXT ," + " hour  TEXT ," + " week  TEXT ," + " wek  TEXT ,"
            + " name  TEXT ," + " phone  TEXT ," + " address  TEXT ," + " service  TEXT ," + " date  TEXT ," + " year  TEXT ," + " month  TEXT ," + " day  TEXT ,"
            + " blood_pressure_big  TEXT ," + " blood_pressure_small  TEXT ," + " blood_sugar_before  TEXT ," + " blood_sugar_after  TEXT ,"
            + " weight  TEXT ," + " height TEXT );";
    //----------------record19--------------------------------------------------------------------------------------------------------------------------------------


//            {"id", "_id", "member_id", "case_id", "calender_id", "schedule_id", "date_start", "date_end", "time_start"
//            , "time_end", "hour", "week", "wek", "name", "phone", "address", "service", "date", "year", "month", "day", "blood_pressure_small", "blood_sugar_after"
//            , "weight", "height"};

    public FriendDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        //傳入的參數說明
        //  context: 用來開啟或建立資料庫的應用程式物件，如 Activity 物件
        //  name: 資料庫檔案名稱，若傳入 null 表示將資料庫暫存在記憶體
        //  factory: 用來建立指標物件的類別，若傳入 null 表示使用預設值
        //  version: 即將要建立的資料庫版本 (版本編號從 1 開始)
        //          若資料庫檔案不存在，就會呼叫 onCreate() 方法
        //          若即將建立的資料庫版本比現存的資料庫版本新，就會呼叫 onUpgrade() 方法
        //          若即將建立的資料庫版本比現存的資料庫版本舊，就會呼叫 onDowngrade() 方法
        //  errHandler: 當資料庫毀損時的處理程式，若傳入 null 表示使用預設的處理程式
    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new FriendDbHelper(context, DB_FILE, null, VERSION)
                    .getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crTBsql);
        db.execSQL(crTBsql_case_info);
        db.execSQL(crTBsql_news);
        db.execSQL(crTBsql_signup);
        db.execSQL(crTBsql_record);
        db.execSQL(crTBsql_schedule);
        db.execSQL(crTBsql_record19);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }

    //-----------------範例-------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public int RecCount() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);

        int a = recSet.getCount();

        recSet.close();
        db.close();

        return a;
//        return recSet.getCount();
    }

    //-----news_info-----
    public int RecCount_news() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_news;
        Cursor recSet = db.rawQuery(sql, null);

        int a = recSet.getCount();

        recSet.close();
        db.close();
        return a;
//        return recSet.getCount();
    }
    //-----news_info-----

    public String FindRec(String tname) {
        SQLiteDatabase db = getReadableDatabase();
        String fldSet = "ans=";
        String sql = "SELECT * FROM " + DB_TABLE + " WHERE `name` LIKE ? ORDER BY `id` ASC";
        String[] args = {"%" + tname + "%"};
        Cursor recSet = db.rawQuery(sql, args);
        int columnCount = recSet.getColumnCount();
        int a = recSet.getCount();
        int bb = 0;
        //------------------------------------
        if (recSet.getCount() != 0) {
            recSet.moveToFirst();
            fldSet = recSet.getString(0) + " "
                    + recSet.getString(1) + " "
                    + recSet.getString(2) + " "
                    + recSet.getString(3) + "\n";

            while (recSet.moveToNext()) {
                for (int i = 0; i < columnCount; i++) {
                    fldSet += recSet.getString(i) + " ";

                }
                fldSet += "\n";
            }

        }
        recSet.close();
        db.close();
        return fldSet;
    }

    public String FindRec_signup(String t_email) {
        SQLiteDatabase db = getReadableDatabase();
        String fldSet = null;
        String sql = "SELECT * FROM " + DB_TABLE_signup + " WHERE email LIKE ?";
        String[] args = {t_email};
        Cursor recSet = db.rawQuery(sql, args);

        int columnCount = recSet.getColumnCount();
        if (recSet.getCount() != 0) {
            recSet.moveToFirst();
            fldSet =
                    recSet.getString(0) + " "   //ID
                            + recSet.getString(1) + " "      // Email
                            + recSet.getString(2) + " "      //username
                            + recSet.getString(3) + "\n";    //password
//                    + recSet.getString(4) + "\n";   //created_time

            while (recSet.moveToNext()) {
                for (int i = 0; i < columnCount; i++) {
                    fldSet += recSet.getString(i) + " ";
                }
                fldSet += "\n";
            }
        }
        recSet.close();
        db.close();
        return fldSet;
    }

    public String FindRec_signup_name(String t_username) {
        SQLiteDatabase db = getReadableDatabase();
        String fldSet = null, fldSetname = null;
        String sql = "SELECT * FROM " + DB_TABLE_signup + " WHERE username LIKE ?";
        String[] args = {t_username};
        Cursor recSet = db.rawQuery(sql, args);

        int columnCount = recSet.getColumnCount();
        if (recSet.getCount() != 0) {
            recSet.moveToFirst();
            fldSet =
                    recSet.getString(2);
//                       recSet.getString(0) + " "   //ID
//                    + recSet.getString(1) + " "      // Email
//                    + recSet.getString(2) + " "      //username
//                    + recSet.getString(3) + " "      //password
//                    + recSet.getString(4) + "\n";   //created_time
//            fldSetname =recSet.getString(2);
            while (recSet.moveToNext()) {
                for (int i = 0; i < columnCount; i++) {
                    fldSet += recSet.getString(i) + " ";
//                    fldSetname +=recSet.getString(i) + " ";
                }
                fldSet += "\n";
//                fldSetname+="\n";
            }
        }
        recSet.close();
        db.close();
//        return fldSetname;
        return fldSet;
    }

    public String FindRec_signin(String t_username, String t_password) {
        SQLiteDatabase db = getReadableDatabase();
        String fldSet = null;
        String sql = "SELECT * FROM " + DB_TABLE_signup + " WHERE email LIKE ? AND password LIKE ? ORDER BY id ASC";
        String[] args = {t_username, t_password};
        Cursor recSet = db.rawQuery(sql, args);

        int columnCount = recSet.getColumnCount();
        if (recSet.getCount() != 0) {
            recSet.moveToFirst();
            fldSet = recSet.getString(0) + " "
                    + recSet.getString(1) + " "
                    + recSet.getString(2) + " "
                    + recSet.getString(3) + "\n";      //password
//                    + recSet.getString(4) + "\n";   //created_time
            while (recSet.moveToNext()) {
                for (int i = 0; i < columnCount; i++) {
                    fldSet += recSet.getString(i) + " ";
                }
                fldSet += "\n";
            }
        }
        recSet.close();
        db.close();
        return fldSet;
    }

    public ArrayList<String> getRecSet() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------------------------------
        int columnCount = recSet.getColumnCount();
        int aa = recSet.getCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#";//分割記號
            }
            recAry.add(fldSet);
            //----------------------------------------
        }
        recSet.close();
        db.close();
        return recAry;//傳回SQLITE的結果---陣列
    }

    public int clearRec() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//   String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public int deleteRec(String b_id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            String whereClause = "id = '" + b_id + "'";
            int rowsAffected = db.delete(DB_TABLE, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public int updateRec(String b_id, String b_name, String b_grp, String b_address) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);

        if (recSet.getCount() != 0) {
            ContentValues rec = new ContentValues();
            rec.put("name", b_name);
            rec.put("grp", b_grp);
            rec.put("address", b_address);
            String whereClause = "id = '" + b_id + "'";

            int rowsAffected = db.update(DB_TABLE, rec, whereClause, null);

            recSet.close();
            db.close();
            return rowsAffected;

        } else {
            recSet.close();
            db.close();
            return -1;
        }

    }

    //-----------------範例-------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //----------------case_info--------------------------------------------------------------------------------------------------------------------------------------------------------
    public long insertRec_case_info(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_case_info, null, rec);
        db.close();
        return rowID;
    }

    public long insertRec_news(String b_title, String b_content, String b_url, String b_createdate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("title", b_title);
        rec.put("content", b_content);
        rec.put("url", b_url);
        rec.put("createdate", b_createdate);
        long rowID = db.insert(DB_TABLE_news, null, rec);
        db.close();
        return rowID;
    }

    public long insertRec_signup(String t_email, String t_username, String t_password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("Email", t_email);
        rec.put("Username", t_username);
        rec.put("Password", t_password);
        long rowID = db.insert(DB_TABLE_signup, null, rec);
        db.close();
        return rowID;
    }

    public long insertRec_record(String tname, String trecord, String trequire, String ttypes, String tdetails, String tstartdate, String tstarttime, String tenddate, String tendtime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("name", tname);
        rec.put("record", trecord);
        rec.put("required", trequire);
        rec.put("types", ttypes);
        rec.put("details", tdetails);
        rec.put("startdate", tstartdate);
        rec.put("starttime", tstarttime);
        rec.put("enddate", tenddate);
        rec.put("endtime", tendtime);
        long rowID = db.insert(DB_TABLE_record, null, rec);
        db.close();
        return rowID;
    }

    //    (tname, trecord,trequire,ttypes,tdetails,tstartdate,tstarttime,tenddate,tendtime);
    public long insertRec_record(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_record, null, rec);
        db.close();
        return rowID;
    }

    public long insertRec_record19(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_record19, null, rec);
        db.close();
        return rowID;
    }

    public ArrayList<String> getRecSet_case_info() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_case_info;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------------------------------
        int columnCount = recSet.getColumnCount();
        int aa = recSet.getCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#";//分割記號
            }
            recAry.add(fldSet);
            //----------------------------------------
        }
        recSet.close();
        db.close();
        return recAry;//傳回SQLITE的結果---陣列
    }

    public ArrayList<String> getRecSet_case_info_where(String test) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_case_info + " WHERE FID = '" + test + "'";
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------------------------------
        int aa = recSet.getCount();
        int columnCount = recSet.getColumnCount();

        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#";//分割記號
            }
            recAry.add(fldSet);
            //----------------------------------------
        }
        recSet.close();
        db.close();
        return recAry;//傳回SQLITE的結果---陣列
    }

    public ArrayList<String> getRecSet_signup() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_signup;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------------------------------
        int columnCount = recSet.getColumnCount();
        int aa = recSet.getCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#";//分割記號
            }
            recAry.add(fldSet);
            //----------------------------------------
        }
        recSet.close();
        db.close();
        return recAry;//傳回SQLITE的結果---陣列
    }

    //-----Opendata的資料陣列-----news_info
    public ArrayList<String> getRecSet_news() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_news;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------------------------------
        int columnCount = recSet.getColumnCount();
        int aa = recSet.getCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#";//分割記號
            }
            recAry.add(fldSet);
            //----------------------------------------
        }
        recSet.close();
        db.close();
        return recAry;//傳回SQLITE的結果---陣列
    }

    public ArrayList<String> getRecSet_record() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_record;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------------------------------
        int columnCount = recSet.getColumnCount();
        int aa = recSet.getCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#";//分割記號
            }
            recAry.add(fldSet);
            //----------------------------------------
        }
        recSet.close();
        db.close();
        return recAry;//傳回SQLITE的結果---陣列
    }

    public ArrayList<String> getRecSet_record19() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_record19;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------------------------------
        int columnCount = recSet.getColumnCount();
        int aa = recSet.getCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#";//分割記號
            }
            recAry.add(fldSet);
            //----------------------------------------
        }
        recSet.close();
        db.close();
        return recAry;//傳回SQLITE的結果---陣列
    }

    public ArrayList<String> getRecSet_schedule() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_schedule;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------------------------------
        int columnCount = recSet.getColumnCount();
        int aa = recSet.getCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#";//分割記號
            }
            recAry.add(fldSet);
            //----------------------------------------
        }
        recSet.close();
        db.close();
        return recAry;//傳回SQLITE的結果---陣列
    }

    public int clearRec_Inquire() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_record;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_record, "1", null);
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            db.close();
            return rowsAffected;
        } else {
            db.close();
            return -1;
        }
    }

    public int clearRec_schedule() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_schedule;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_schedule, "1", null);
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            db.close();
            return rowsAffected;
        } else {
            db.close();
            return -1;
        }
    }

    public long insertRec_m_Inqire(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_record, null, rec);
        db.close();
        return rowID;
    }

    public long insertRec_schedule(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_schedule, null, rec);
        db.close();
        return rowID;
    }

    public long insertRec(ContentValues newRow) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_signup, null, newRow);
        db.close();
        return rowID;
    }

    public int clearRec_case_info() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_case_info;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//   String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_case_info, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public int clearRec_record() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_record;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//   String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_record, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public int clearRec_record19() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_record19;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//   String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_record19, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    //-----醫療院所OPENDATA寫入SQL-----carry_info
    public long insertRec_carry_info_1(String b_area, String b_phone, String b_name, String b_address) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("area", b_area);
        rec.put("phone", b_phone);
        rec.put("name", b_name);
        rec.put("address", b_address);
        long rowID = db.insert(DB_TABLE_carry_info_1, null, rec);
        db.close();
        return rowID;
    }

    public long insertRec_carry_info_2(String b_area2, String b_phone2, String b_name2, String b_address2) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("area2", b_area2);
        rec.put("phone2", b_phone2);
        rec.put("name2", b_name2);
        rec.put("address2", b_address2);
        long rowID = db.insert(DB_TABLE_carry_info_1, null, rec);
        db.close();
        return rowID;
    }

    public int clearRec_signup() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_signup;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//   String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_signup, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }
}