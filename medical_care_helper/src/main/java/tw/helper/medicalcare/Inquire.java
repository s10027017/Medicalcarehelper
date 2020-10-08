package tw.helper.medicalcare;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import tw.tcnr.medicalcare.providers.FriendsContentProvider;

public class Inquire extends AppCompatActivity {
    public String[][] data = {
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "13:00", "15:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "13:00", "15:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "13:00", "15:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "13:00", "15:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "13:00", "15:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
            {"2020/09/01", "09:00", "11:00", "2", "TEST"},
    };
    private EditText e001, e002;
    private Intent intent = new Intent();
    BottomNavigationView bottomNav1, bottomNav2;

    private ArrayList<String> recSet_schedule;
    private FriendDbHelper dbHper;
    List<Map<String, Object>> mList;

    private static final String DB_File = "friends.db", DB_TABLE = "schedule"; //斷絕父子關係 static final
    private SQLiteDatabase db;
    String msg = null;
    private String[] MYCOLUMN = new String[]{"id", "_id", "member_id", "case_id", "calender_id", "date_start", "date_end", "time_start", "time_end", "hour", "week", "name", "phone", "address", "service"};
    private ArrayList<String> recSet;
    private static final int DBversion = 1;
    private String sqlctl;
    private String ser_msg;
    private int servermsgcolor;
    private String TAG="tcnrmm=>";
    private ArrayList<String> recSet_schedule_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inquire);

        DBConnector.connect_ip="https://medicalcarehelper.com/tcnr19/android_schedule_db_all.php";

        FriendDbHelper friendDbHelper = new FriendDbHelper(getApplicationContext(),DB_File,null,1);
        db = friendDbHelper.getReadableDatabase();

        initDB();
        dbmysql();

        recSet_schedule_list = dbHper.getRecSet_schedule();

        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        mList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < recSet.size(); i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            String[] fld = recSet.get(i).split("#"); //initDB 裡的 recSet_sign_up_list 已經有抓了 F4可過去看
//            String test1 = fld[9].substring(0,2).toString();
//            String test2 = fld[7].substring(0,2).toString();
            String test1 = fld[7].toString();
            String test2 = fld[8].toString();
            Integer q1 = Integer.parseInt(test1);
            Integer q2 = Integer.parseInt(test2);
            Integer q3 = q2-q1;
            item.put("date", fld[6]);
            item.put("start", fld[7]);
            item.put("end", fld[8]);
            item.put("hour", q3);
            item.put("note", fld[11]);  // 先顯示姓名吧  原本是5 細項

            items.add(item);
        }

//        recSet_record_list = dbHper.getRecSet_record();
//
//        List<Map<String, Object>> mList1=null;
//        mList = new ArrayList<Map<String, Object>>();
//        for (int i = 0; i < recSet_record_list.size(); i++)  // i是代表y 幾筆資料
//        {
//            Map<String, Object> item1 = new HashMap<String, Object>(); // 代表X 每筆資料裡面放幾個東西
//            String[] fld = recSet_record_list.get(i).split("#"); //initDB 裡的 recSet_sign_up_list 已經有抓了 F4可過去看
//            item1.put("imgView",R.drawable.c_a01);                                // 密碼已拿掉+ "\n" +"密碼:" + fld[3]
////           0 ID 1 姓名 2 紀錄 3 需求 4 類型 5細項 6開始日期 7開始時間 8結束日期 9結束時間
//            item1.put("txtView", "ID:" + fld[0] + "\t\t\t\t\t\t\t\t\t\t"+"姓名:" + fld[1] + "\t\t\t\t\t"+"類型:" + fld[4] + "\n"
//                    +"開始時間:" + fld[6] + "\t\t\t" + fld[7] + "\n" +"結束時間:" + fld[8] + "\t\t\t" + fld[9] + "\n"
//                    +"細項:" + fld[5] + "\n" +"紀錄:" + fld[2] + "\n" +"需求:" + fld[3] );  //debug  recSet_record_list & String[] fld  多好幾個空的
//            mList.add(item1);
//        }

        //將資料轉換成<key,value>的型態
//        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
//        for (int i = 0; i < data.length; i++) {
//            Map<String, Object> item = new HashMap<String, Object>();
//            item.put("date", data[i][0]);
//            item.put("start", data[i][1]);
//            item.put("end", data[i][2]);
//            item.put("hour", data[i][3]);
//            item.put("note", data[i][4]);
//            items.add(item);
//        }

        //帶入對應資料
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                items,
                R.layout.inquire_item,
                new String[]{"date", "start", "end", "hour","note"},
                new int[]{R.id.d01_t005, R.id.d01_t006, R.id.d01_t007, R.id.d01_t008, R.id.d01_t009}
        );
        ListView listview = (ListView) findViewById(R.id.d01_l001);
        listview.setAdapter(adapter);
        //     listview.setOnItemClickListener(onClickListView);

        e001 = (EditText) findViewById(R.id.d01_e001);
        e002 = (EditText) findViewById(R.id.d01_e002);

        e001.setInputType(InputType.TYPE_NULL);
        e002.setInputType(InputType.TYPE_NULL);


        e001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(Inquire.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        e001.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        e002.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(Inquire.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        e002.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        bottomNav1 = findViewById(R.id.nav_view1);
        bottomNav2 = findViewById(R.id.nav_view2);
        BottomNavigationHelper.removeShiftMode(bottomNav1);
        bottomNav1.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        BottomNavigationHelper.removeShiftMode(bottomNav2);
        bottomNav2.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNav1.setOnNavigationItemSelectedListener(navListener);
        bottomNav2.setOnNavigationItemSelectedListener(navListener);
        bottomNav1.setSelectedItemId(R.id.statiscs);

        initDB();
        dbmysql();

    }

    public static void enableStrictMode(Context context) {
        StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads()
                        .detectDiskWrites()
                        .detectNetwork()
                        .penaltyLog()
                        .build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects()
                        .penaltyLog()
                        .build());
    }

    private void dbmysql() {
//        mContRes = getContentResolver();
//        Cursor cur = mContRes.query(FriendsContentProvider.CONTENT_URI_record, MYCOLUMN, null, null, null);
//        cur.moveToFirst(); // 一定要寫，不然會出錯

//        sqlctl = "SELECT * FROM member record";
        sqlctl = "SELECT * FROM schedule";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = DBConnector.executeQuery(nameValuePairs);
//=========================================
            chk_httpstate();
            //=========================================
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                //--------------------------------------------------------
                int rowsAffected = dbHper.clearRec_schedule();                 // 匯入前,刪除所有SQLite資料
                //--------------------------------------------------------
                // 處理JASON 傳回來的每筆資料
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
                    Iterator itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        String value = jsonData.getString(key); // 取出欄位的值
                        if (value == null) {
                            continue;
                        } else if ("".equals(value.trim())) {
                            continue;
                        } else {
                            jsonData.put(key, value.trim());
                        }
                        // ------------------------------------------------------------------
                        newRow.put(key, value.toString()); // 動態找出有幾個欄位
                        // -------------------------------------------------------------------
                    }
                    // -------------------加入SQLite---------------------------------------
                    long rowID = dbHper.insertRec_schedule(newRow);
                }
                // ---------------------------
            } else {
                ser_msg = "主機資料庫無資料(code:" + DBConnector.httpstate + ") ";
//                servermsgcolor= ContextCompat.getColor(this, R.color.Red);
//                b_servermsg.setTextColor(servermsgcolor);
//                //.makeText(getApplicationContext(), "主機資料庫無資料", //.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        // // ---------------------------
//        try {
//            String result = DBConnector.executeQuery("SELECT * FROM record");
////        String r = result.toString().trim();
////==========================================
//            //以下程式碼一定要放在前端藍色程式碼執行之後，才能取得狀態碼
//            //存取類別成員 DBConnector.httpstate 判定是否回應 200(連線要求成功)
////            Log.d(TAG, "httpstate=" + DBConnector.httpstate);
//
//            if (DBConnector.httpstate == 200) {
//                Uri uri = FriendsContentProvider.CONTENT_URI_record;
//                mContRes.delete(uri, null, null);  //清空SQLite
//                //.makeText(getBaseContext(), "已經完成由伺服器會入資料",
//                        //.LENGTH_LONG).show();
//            } else {
//                int checkcode = DBConnector.httpstate / 100;
//                switch (checkcode) {
//                    case 1:
//                        msg = "資訊回應(code:" + DBConnector.httpstate + ")";
//                        break;
//                    case 2:
//                        msg = "已經完成由伺服器匯入資料(code:" + DBConnector.httpstate + ")";
//                        break;
//                    case 3:
//                        msg = "伺服器重定向訊息，請稍後在試(code:" + DBConnector.httpstate + ")";
//                        break;
//                    case 4:
//                        msg = "用戶端錯誤回應，請稍後在試(code:" + DBConnector.httpstate + ")";
//                        break;
//                    case 5:
//                        msg = "伺服器error responses，請稍後在試(code:" + DBConnector.httpstate + ")";
//                        break;
//                }
//                //.makeText(getBaseContext(), msg, //.LENGTH_LONG).show();
//            }
//            //======================================
//            // 選擇讀取特定欄位
//            // String result = DBConnector.executeQuery("SELECT id,name FROM
//            // member");
//            /*******************************************************************************************
//             * SQL 結果有多筆資料時使用JSONArray 只有一筆資料時直接建立JSONObject物件 JSONObject
//             * jsonData = new JSONObject(result);
//             *******************************************************************************************/
//            JSONArray jsonArray = new JSONArray(result);
//            int a=0;
//            // -------------------------------------------------------
//            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
//                Uri uri = FriendsContentProvider.CONTENT_URI_record;
//                mContRes.delete(uri, null, null); // 匯入前,刪除所有SQLite資料
//                int aa=0;
//                // ----------------------------
//                // 處理JASON 傳回來的每筆資料
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonData = jsonArray.getJSONObject(i);
//                    //
//                    ContentValues newRow = new ContentValues();
//                    // --(1) 自動取的欄位
//                    // --取出 jsonObject
//                    // 每個欄位("key","value")-----------------------
//                    Iterator itt = jsonData.keys();
//                    while (itt.hasNext()) {
//                        String key = itt.next().toString();
//                        String value = jsonData.getString(key); // 取出欄位的值
//                        if (value == null) {
//                            continue;
//                        } else if ("".equals(value.trim())) {
//                            continue;
//                        } else {
//                            jsonData.put(key, value.trim());
//                        }
//                        // ------------------------------------------------------------------
//                        newRow.put(key, value.toString()); // 動態找出有幾個欄位
//                        // -------------------------------------------------------------------
//                        // Log.d(TAG, "第" + i + "個欄位 key:" + key + " value:" + value);
//                    }
//                    // -------------------加入SQLite---------------------------------------
//                    mContRes.insert(FriendsContentProvider.CONTENT_URI_record, newRow); //寫入SQLite
////                    long rowID = dbHper.insertRec_signup(t_email, t_username,t_password);//真正執行SQL 寫入資料庫
//
//                    //tvTitle.setTextColor(Color.BLUE);
//                    //tvTitle.setText("顯示資料： 共加入" + Integer.toString(jsonArray.length()) + " 筆");
//                }
//                // ---------------------------
//            } else {
//                //.makeText(getApplicationContext(), "主機資料庫無資料", //.LENGTH_LONG).show();
//            }
//            // --------------------------------------------------------
//
//        } catch (Exception e) {
//            // Log.e("log_tag", e.toString());
//        }
//        cur.close();
//        sqliteupdate(); //抓取SQLite資料
    }

    //**************************************************
//*       檢查連線狀況
//**************************************************
    private void chk_httpstate() {
        ////-------------------------------
        //存取類別成員 DBConnector01.httpstate 判定是否回應 200(連線要求成功)
        if (DBConnector.httpstate == 200) {
            ser_msg = "伺服器匯入資料(code:" + DBConnector.httpstate + ") ";
            servermsgcolor = ContextCompat.getColor(this, R.color.Navy);
//                //.makeText(getBaseContext(), "由伺服器匯入資料 ",
//                        //.LENGTH_SHORT).show();
        } else {
            int checkcode = DBConnector.httpstate / 100;
            switch (checkcode) {
                case 1:
                    ser_msg = "資訊回應(code:" + DBConnector.httpstate + ") ";
                    break;
                case 2:
                    ser_msg = "已經完成由伺服器會入資料(code:" + DBConnector.httpstate + ") ";
                    break;
                case 3:
                    ser_msg = "伺服器重定向訊息，請稍後在試(code:" + DBConnector.httpstate + ") ";
//                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 4:
                    ser_msg = "用戶端錯誤回應，請稍後在試(code:" + DBConnector.httpstate + ") ";
//                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 5:
                    ser_msg = "伺服器error responses，請稍後在試(code:" + DBConnector.httpstate + ") ";
//                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
            }
//                //.makeText(getBaseContext(), msg, //.LENGTH_SHORT).show();
        }
        if (DBConnector.httpstate == 0) {
            ser_msg = "遠端資料庫異常(code:" + DBConnector.httpstate + ") ";
        }
        //.makeText(getBaseContext(), ser_msg, //.LENGTH_SHORT).show();
//        b_servermsg.setText(ser_msg);
//        b_servermsg.setTextColor(servermsgcolor);

        //-------------------------------------------------------------------
    }


//    private void sqliteupdate()
//    {
//        Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI_record, MYCOLUMN, null, null, null);
//        int tcount = c.getCount();
//        int columnCount = c.getColumnCount();
//        String fidSet = "show = ";
//
//        for (int i = 0; i < tcount; i++) {
//            c.moveToPosition(i);
//            for (int j = 0; j < columnCount; j++) {
//                fidSet += c.getString(j) + ", ";
//            }
//        }
//
//        c.close();
//    }

    private void initDB()
    {
        if (dbHper == null)
        {
            dbHper = new FriendDbHelper(getApplicationContext(), DB_File, null, DBversion);
        }
        recSet = dbHper.getRecSet_schedule(); //all data  資料全部讀近來
    }


    //=================================================================================================================
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.information:
                            startActivity(new Intent(getApplicationContext(), Case_info.class));
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
                        case R.id.schedule:
                            startActivity(new Intent(getApplicationContext(), Schedule.class));
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
                        case R.id.statiscs:
//                            bottomNav4.setVisibility(View.GONE);
//                            bottomNav3.setVisibility(View.GONE);
//                            startActivity(new Intent(getApplicationContext(), Inquire.class));
//                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.care:
                            startActivity(new Intent(getApplicationContext(), Carry_info.class));
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
//=================================================================================================================
                        case R.id.nav_home:
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
                        case R.id.nav_news:
                            startActivity(new Intent(getApplicationContext(), News_info.class));
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
                        case R.id.nav_book:
                            startActivity(new Intent(getApplicationContext(), Record_main.class));
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
                        case R.id.nav_cloud:
                            startActivity(new Intent(getApplicationContext(), Fans.class));
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
//=================================================================================================================

                    }
                    return false;
                }
            };
    //=================================================================================================================
//================================ 下面是生命週期 ===========================================
//    1.當Activity準備要產生時，先呼叫onCreate方法。
//    2.Activity產生後（還未出現在手機螢幕上），呼叫onStart方法。
//    3.當Activity出現手機上後，呼叫onResume方法。
//    4.當使用者按下返回鍵結束Activity時， 先呼叫onPause方法。
//    5.當Activity從螢幕上消失時，呼叫onStop方法。
//    6.最後完全結束Activity之前，呼叫onDestroy方法。
    @Override
    protected void onStop() {
        super.onStop();
        Inquire.this.finish();
//        //.makeText(this, "onStop", //.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Inquire.this.finish();
//        //.makeText(this, "onDestroy", //.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        //.makeText(this, "onPause", //.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        //.makeText(this, "onResume", //.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        //.makeText(this, "onStart", //.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        //.makeText(this, "onRestart", //.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed()
    {
//        stop.performClick();// performClick 整個關掉
//        super.onBackPressed();
    }
    //================================ 下面是MENU ==========================================
    //  Menu下面這兩個最基本的要記起來 onCreateOptionsMenu & onOptionsItemSelected
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);  //選layout ,menu
        return true;
    }
    //====================================================================================
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings: //menu那邊+一個 結束 string也要+
                this.finish();
                onBackPressed();  // 關掉
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //================================ 下面是次類別之類的東西 ==============================================

}
