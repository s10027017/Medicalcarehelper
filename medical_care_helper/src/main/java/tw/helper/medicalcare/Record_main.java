package tw.helper.medicalcare;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Record_main extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private Intent intent = new Intent();
    BottomNavigationView bottomNav1, bottomNav2;
    private FriendDbHelper dbHper;
    private static final int DBversion = 1;
    private static final String DB_FILE = "friends.db";
    private Button b001, b002, btntest, btnAdd_case, update_b001, update_b002;
    private EditText e001, e002, e003, e004, e004_1, e005, e005_1, e006, e007, update_e001, update_e002, update_e003, update_e004, update_e004_1, update_e005,
            update_e005_1, update_e006, update_e007;
    private String tname, ttype, tdetail, ttime1, ttime2, trecord, trequire, record, record_id, ttypes, tdetails, tstartdate, tstarttime, tenddate, tendtime;
    private TextView t001,textView3;
    private ContentResolver mContRes;
    private static final String DB_File = "friends.db", DB_TABLE = "record"; //斷絕父子關係 static final
    private ArrayList<String> recSet;
    private String[] MYCOLUMN = new String[]{"id", "_id", "member_id", "case_id", "calender_id", "schedule_id", "date_start", "date_end", "time_start"
            , "time_end", "hour", "week", "wek", "name", "phone", "address", "service", "date", "year", "month", "day", "blood_pressure_big",
            "blood_pressure_small", "blood_sugar_before", "blood_sugar_after", "weight", "height"};  // , "type", "detail", "start_time", "end_time"
    String msg = null;
    private SQLiteDatabase db;
    private TextView c01_t001;
    private Dialog mLoginDlg;
    private ListView listView;
    private int newscrollheight;
    List<Map<String, Object>> mList;
    private SimpleAdapter adapter = null;
    private ArrayList<String> recSet_record_list;
    private ArrayList<String> recSet_record_update;
    private ScrollView scr_main, scr_update;
    private ListView list001;
    private String fldSet, fldSet1;
    private int up_item = 0;
    private int iSelect = 0;
    private String d1 = "";
    private String d2 = "";
    private String d3 = "";
    private String d4 = "";
    private String s = "";
    private String t1 = "";
    private String t2 = "";
    private String record_name, record_record, record_required, record_types, record_details, record_startdate, record_starttime, record_enddate, record_endtime;
    private TimePickerDialog timePickerDialog1, timePickerDialog2, timePickerDialog3, timePickerDialog4;
    int hour1, hour2, hour3, hour4, minute1, minute2, minute3, minute4;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener1, onTimeSetListener2, onTimeSetListener3, onTimeSetListener4;
    private String sqlctl = "";
    private String ser_msg;
    private int servermsgcolor;
    private String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_main);
        //---------------------------------------------------------------------
        bottomNav1 = findViewById(R.id.nav_view1);
        bottomNav2 = findViewById(R.id.nav_view2);
        BottomNavigationHelper.removeShiftMode(bottomNav1);
        bottomNav1.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        BottomNavigationHelper.removeShiftMode(bottomNav2);
        bottomNav2.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNav1.setOnNavigationItemSelectedListener(navListener);
        bottomNav2.setOnNavigationItemSelectedListener(navListener);
        bottomNav1.setSelectedItemId(R.id.nav_book);


        DBConnector.connect_ip = "https://medicalcarehelper.com/tcnr19/android_record_db_all.php";
        initDB();
        dbmysql();
        recSet = dbHper.getRecSet_record19(); //all data  資料全部讀近來

        setupViewComponent();

        onTimeSetListener1 = new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                DecimalFormat decimalFormat = new DecimalFormat("00");
                e004_1.setText(decimalFormat.format(hourOfDay) + ":" + decimalFormat.format(minute));
            }
        };
        onTimeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                DecimalFormat decimalFormat = new DecimalFormat("00");
                e005_1.setText(decimalFormat.format(hourOfDay) + ":" + decimalFormat.format(minute));
            }
        };
        onTimeSetListener3 = new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                DecimalFormat decimalFormat = new DecimalFormat("00");
                update_e004_1.setText(decimalFormat.format(hourOfDay) + ":" + decimalFormat.format(minute));
            }
        };
        onTimeSetListener4 = new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                DecimalFormat decimalFormat = new DecimalFormat("00");
                update_e005_1.setText(decimalFormat.format(hourOfDay) + ":" + decimalFormat.format(minute));
            }
        };
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

    private void setupViewComponent() {
        btnAdd_case = (Button) findViewById(R.id.btnAdd_case);//新增case
        b001 = (Button) findViewById(R.id.btnAdd);//新增
        b002 = (Button) findViewById(R.id.c02_b001);//取消
        update_b001 = (Button) findViewById(R.id.update_b001);
        update_b002 = (Button) findViewById(R.id.update_b002);
//        btntest = (Button) findViewById(R.id.btntest);
        e001 = (EditText) findViewById(R.id.e_rm001);
        e002 = (EditText) findViewById(R.id.e_rm002);
        e003 = (EditText) findViewById(R.id.e_rm003);
        e004 = (EditText) findViewById(R.id.e_rm004);
        e004_1 = (EditText) findViewById(R.id.e_rm004_1);
        e005 = (EditText) findViewById(R.id.e_rm005);
        e005_1 = (EditText) findViewById(R.id.e_rm005_1);
        e006 = (EditText) findViewById(R.id.e_rm006);
        e007 = (EditText) findViewById(R.id.e_rm007);
        update_e001 = (EditText) findViewById(R.id.update_e001);
        update_e002 = (EditText) findViewById(R.id.update_e002);
        update_e003 = (EditText) findViewById(R.id.update_e003);
        update_e004 = (EditText) findViewById(R.id.update_e004);
        update_e004_1 = (EditText) findViewById(R.id.update_e004_1);
        update_e005 = (EditText) findViewById(R.id.update_e005);
        update_e005_1 = (EditText) findViewById(R.id.update_e005_1);
        update_e006 = (EditText) findViewById(R.id.update_e006);
        update_e007 = (EditText) findViewById(R.id.update_e007);
//        t001 = (TextView) findViewById(R.id.count_t);
        c01_t001 = (TextView) findViewById(R.id.c01_t001);
//        textView3 = (TextView) findViewById(R.id.textView3);
//        c01_t001.setText(record);   // 測試 成功
        e004.setOnTouchListener(this);
        e004_1.setOnTouchListener(this);
        e005.setOnTouchListener(this);
        e005_1.setOnTouchListener(this);
        update_e004.setOnTouchListener(this);
        update_e004_1.setOnTouchListener(this);
        update_e005.setOnTouchListener(this);
        update_e005_1.setOnTouchListener(this);
        b001.setOnClickListener(this);
        b002.setOnClickListener(this);
//        btntest.setOnClickListener(this);
        btnAdd_case.setOnClickListener(this);
        update_b001.setOnClickListener(this);
        update_b002.setOnClickListener(this);
        scr_main = (ScrollView) findViewById(R.id.scr_main);
        scr_update = (ScrollView) findViewById(R.id.scr_update);

        list001 = (ListView) findViewById(R.id.listView1);
        list001.getLayoutParams().height = newscrollheight;
        list001.setLayoutParams(list001.getLayoutParams()); // 重定ScrollView大小

        mList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < recSet.size(); i++)  // i是代表y 幾筆資料
        {
            Map<String, Object> item1 = new HashMap<String, Object>(); // 代表X 每筆資料裡面放幾個東西
            String[] fld = recSet.get(i).split("#"); //initDB 裡的 recSet_sign_up_list 已經有抓了 F4可過去看
            item1.put("imgView", R.drawable.userconfig);                                // 密碼已拿掉+ "\n" +"密碼:" + fld[3]
//           0 ID 1 姓名 2 紀錄 3 需求 4 類型 5細項 6開始日期 7開始時間 8結束日期 9結束時間

            item1.put("txtView", "ID:" + fld[0] + "\t\t\t\t\t" + "姓名:" + fld[13] + "\t\t\t\t\t" + "類型:" + fld[16] + "\n"
                    + "開始時間:" + fld[6] + "\t\t\t" + "結束時間:" + fld[7] + "\n"
                    + "血糖飯前:\t\t" + fld[23] + "\t\t血糖飯後:" + fld[24] + "\n"
                    + "心跳:\t\t" + fld[21] + "\t\t~\t\t" + fld[22] + "\n"
                    + "身高:\t\t" + fld[25] + "\t\t體重:\t\t" + fld[26] + "\n");  //debug  recSet_record_list & String[] fld  多好幾個空的

            mList.add(item1);
        }
        adapter = new SimpleAdapter(
                this, // put的那些陣列
                mList,
                R.layout.sign_up_list_item,  // 顯示的樣式
                new String[]{"imgView", "txtView"}, // 這邊多加 下面也要多加
                new int[]{R.id.imgView, R.id.txtView} // 放到這邊的物件 有三個就三個
        );

        list001.setAdapter(adapter);
//        list001.setOnItemClickListener(listviewOnItemClkLis);    //  點進去的監聽事件

        test = getSharedPreferences("record",MODE_PRIVATE)
                .getString("record","");
//        textView3.setText(test);
//        textView3.setText("");
    }

    private void initDB() {
        if (dbHper == null) {
            dbHper = new FriendDbHelper(getApplicationContext(), DB_File, null, DBversion);
        }
        recSet = dbHper.getRecSet_record19(); //all data  資料全部讀近來
    }

    private ListView.OnItemClickListener listviewOnItemClkLis = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            updatelayout();

//            recSet_record_update = dbHper.getRecSet_record_update();
//            iSelect = list001.getSelectedItemPosition(); //找到按何項   iSelect這邊會有錯誤 -1 會閃退 抓不到位置
//            String[] update = recSet_record_list.get(iSelect).split("#");   這邊會有錯誤
            String[] update = recSet.get(position).split("#");
//            String s = "資料：共" + recSet.size() + " 筆," + "你按下  " + String.valueOf(iSelect + 1) + "項"; //起始為0
//            tvTitle.setText(s);
//            b_id.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.Red));
//            b_id.setText(fld[0]);
            update_e001.setText(update[1]);  // 點擊到的秀出來
            update_e006.setText(update[2]);
            update_e007.setText(update[3]);

            update_e002.setText(update[4]);
            update_e003.setText(update[5]);
            update_e004.setText(update[6]);
            update_e004_1.setText(update[7]);
            update_e005.setText(update[8]);
            update_e005_1.setText(update[9]);
            //-------目前所選的item---
            up_item = iSelect;

            record_id = update[0].toString();

            record_name = update_e001.getText().toString().trim();
            record_record = update_e006.getText().toString().trim();
            record_required = update_e007.getText().toString().trim();

            record_types = update_e002.getText().toString().trim();
            record_details = update_e003.getText().toString().trim();
            record_startdate = update_e004.getText().toString().trim();
            record_starttime = update_e004_1.getText().toString().trim();
            record_enddate = update_e005.getText().toString().trim();
            record_endtime = update_e005_1.getText().toString().trim();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                tname = e001.getText().toString().trim();
                trecord = e006.getText().toString().trim();
                trequire = e007.getText().toString().trim();
                ttypes = e002.getText().toString().trim();
                tdetails = e003.getText().toString().trim();
                tstartdate = e004.getText().toString().trim();
                tstarttime = e004_1.getText().toString().trim();
                tenddate = e005.getText().toString().trim();
                tendtime = e005_1.getText().toString().trim();
                if (tname.equals("") || trecord.equals("") || trequire.equals("")) {
                    //.makeText(Record_main.this, "資料空白無法新增 !", //.LENGTH_SHORT).show();
                } else {
                    //===========================================寫入MYSQL================================================
                    ArrayList<String> nameValuePairs = new ArrayList<String>();

                    nameValuePairs.add(tname);
                    nameValuePairs.add(trecord);
                    nameValuePairs.add(trequire);
                    nameValuePairs.add(ttypes);
                    nameValuePairs.add(tdetails);
                    nameValuePairs.add(tstartdate);
                    nameValuePairs.add(tstarttime);
                    nameValuePairs.add(tenddate);
                    nameValuePairs.add(tendtime);

                    try {
                        Thread.sleep(500); //  延遲Thread 睡眠0.5秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //====================================================================================================
                    String result = DBConnector.executeInsert_record(nameValuePairs);
                    dbmysql();
                    //.makeText(getApplicationContext(), "新增成功!", //.LENGTH_SHORT).show();
                    //===========================================寫入MYSQL================================================
//                    long rowID = dbHper.insertRec_record(tname, trecord, trequire, ttypes, tdetails, tstartdate, tstarttime, tenddate, tendtime);//真正執行SQL 寫入資料庫
                    long rowID=0;
                    if (rowID != -1) // -1代表沒有
                    {
                        //.makeText(getApplicationContext(), "新增成功!", //.LENGTH_SHORT).show();
                        main_layout();

                        dbmysql();
                        setupViewComponent();
                    } else {
                        //.makeText(getApplicationContext(), "新增失敗!", //.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.c02_b001:
                main_layout();
                break;
//            case R.id.btntest:  // 測試用 可拿掉
//                mLoginDlg = new Dialog(Record_main.this);
//                mLoginDlg.setTitle("服務紀錄");
//                mLoginDlg.setCancelable(false);
//
//                mLoginDlg.setContentView(R.layout.sign_up_list_dialog);
//
////                dialogtext = (TextView) mLoginDlg.findViewById(R.id.list_title);//dialog表頭文字設定
////                dialogtext.setText(getString(R.string.h01_b001));//設定與按鈕相同文字
//                //---------------------------------------------------------------------------------
//                listView = (ListView) mLoginDlg.findViewById(R.id.sign_up_list);
//                listView.getLayoutParams().height = newscrollheight;
//                listView.setLayoutParams(listView.getLayoutParams()); // 重定ScrollView大小
//                //=========== 下面開始抓 SQLite 資料=============
//                recSet_record_list = dbHper.getRecSet_record();
//                List<Map<String, Object>> mList1 = null;
//                mList = new ArrayList<Map<String, Object>>();
//                for (int i = 0; i < recSet_record_list.size(); i++)  // i是代表y 幾筆資料
//                {
//                    Map<String, Object> item1 = new HashMap<String, Object>(); // 代表X 每筆資料裡面放幾個東西
//                    String[] fld = recSet_record_list.get(i).split("#"); //initDB 裡的 recSet_sign_up_list 已經有抓了 F4可過去看
//                    item1.put("imgView", R.drawable.c_a01);                                // 密碼已拿掉+ "\n" +"密碼:" + fld[3]
//                    item1.put("txtView", "ID:" + fld[0] + "\n" + "姓名:" + fld[1] + "\n" + "紀錄:" + fld[2] + "\n" + "需求:" + fld[3]);  //debug  recSet_record_list & String[] fld  多好幾個空的
//                    mList.add(item1);
//                }
//                adapter = new SimpleAdapter(
//                        this, // put的那些陣列
//                        mList,
//                        R.layout.sign_up_list_item,  // 顯示的樣式
//                        new String[]{"imgView", "txtView"}, // 這邊多加 下面也要多加
//                        new int[]{R.id.imgView, R.id.txtView} // 放到這邊的物件 有三個就三個
//                );
//                listView.setAdapter(adapter);
//                //----------------------------------
//                Button list_return = (Button) mLoginDlg.findViewById(R.id.list_return);
//                list_return.setOnClickListener(this);
//                //-------------------------------------------------------
//                mLoginDlg.show();
//                break;
            case R.id.btnAdd_case:  // 新增案主資料
//                case_layout();
                break;
            case R.id.update_b001: // 返回
                list001.setVisibility(View.VISIBLE);
                btnAdd_case.setVisibility(View.VISIBLE);
                scr_update.setVisibility(View.GONE);
                break;
            case R.id.update_b002: // 更新
                modify_mysql_update();
                break;
            case R.id.list_return:
                mLoginDlg.cancel();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN:
                switch (view.getId())
                {
                    case R.id.e_rm004:   // 開始時間 日期
                        e004.setText("");
                        Calendar now1 = Calendar.getInstance();
                        DatePickerDialog datePicDlg1 = new DatePickerDialog(
                                this,
                                datePicDigOnDateSelLis1,
                                now1.get(Calendar.YEAR),
                                now1.get(Calendar.MONTH),
                                now1.get(Calendar.DAY_OF_MONTH)
                        );
                        datePicDlg1.setTitle("選擇日期");
                        datePicDlg1.setIcon(android.R.drawable.ic_dialog_info);
                        datePicDlg1.setCancelable(false);
                        datePicDlg1.show();
                        break;
                    case R.id.e_rm004_1:  // 開始時間 時間
                        e004_1.setText("");
                        timePickerDialog1 = new TimePickerDialog(this, onTimeSetListener1, hour1, minute1, true);
                        timePickerDialog1.show();
                        break;
                    case R.id.e_rm005:   // 結束時間 日期
                        e005.setText("");
                        Calendar now = Calendar.getInstance();
                        DatePickerDialog datePicDlg2 = new DatePickerDialog(
                                this,
                                datePicDigOnDateSelLis2,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH)
                        );
                        datePicDlg2.setTitle("選擇日期");
                        datePicDlg2.setIcon(android.R.drawable.ic_dialog_info);
                        datePicDlg2.setCancelable(false);
                        datePicDlg2.show();
                        break;
                    case R.id.e_rm005_1:  // 結束時間 時間
                        e005_1.setText("");
                        timePickerDialog2 = new TimePickerDialog(this, onTimeSetListener2, hour2, minute2, true);
                        timePickerDialog2.show();
                        break;

                    case R.id.update_e004: //  更新 開始時間 日期
                        update_e004.setText("");
                        Calendar now3 = Calendar.getInstance();
                        DatePickerDialog datePicDlg3 = new DatePickerDialog(
                                this,
                                datePicDigOnDateSelLis3,
                                now3.get(Calendar.YEAR),
                                now3.get(Calendar.MONTH),
                                now3.get(Calendar.DAY_OF_MONTH)
                        );
                        datePicDlg3.setTitle("選擇日期");
                        datePicDlg3.setIcon(android.R.drawable.ic_dialog_info);
                        datePicDlg3.setCancelable(false);
                        datePicDlg3.show();
                        break;
                    case R.id.update_e004_1: //  更新 開始時間 時間
                        update_e004_1.setText("");
                        timePickerDialog3 = new TimePickerDialog(this, onTimeSetListener3, hour3, minute3, true);
                        timePickerDialog3.show();
                        break;
                    case R.id.update_e005: //  更新 結束時間 日期
                        update_e005.setText("");
                        Calendar now4 = Calendar.getInstance();
                        DatePickerDialog datePicDlg4 = new DatePickerDialog(
                                this,
                                datePicDigOnDateSelLis4,
                                now4.get(Calendar.YEAR),
                                now4.get(Calendar.MONTH),
                                now4.get(Calendar.DAY_OF_MONTH)
                        );
                        datePicDlg4.setTitle("選擇日期");
                        datePicDlg4.setIcon(android.R.drawable.ic_dialog_info);
                        datePicDlg4.setCancelable(false);
                        datePicDlg4.show();
                        break;
                    case R.id.update_e005_1: //  更新 結束時間 時間
                        update_e005_1.setText("");
                        timePickerDialog4 = new TimePickerDialog(this, onTimeSetListener4, hour4, minute4, true);
                        timePickerDialog4.show();
                        break;
                }
                break;
        }
        return false;
    }

    private void main_layout() {
        scr_main.setVisibility(View.GONE);
        btnAdd_case.setVisibility(View.VISIBLE);
    }

    private void case_layout() {
        scr_main.setVisibility(View.VISIBLE);
        btnAdd_case.setVisibility(View.GONE);
    }

    private void updatelayout() {
        scr_update.setVisibility(View.VISIBLE);
        scr_main.setVisibility(View.GONE);
        btnAdd_case.setVisibility(View.GONE);
        list001.setVisibility(View.GONE);
    }

    private void modify_mysql_update() {
        record_name = update_e001.getText().toString().trim();
        record_record = update_e006.getText().toString().trim();
        record_required = update_e007.getText().toString().trim();

        record_types = update_e002.getText().toString().trim();
        record_details = update_e003.getText().toString().trim();
        record_startdate = update_e004.getText().toString().trim();
        record_starttime = update_e004_1.getText().toString().trim();
        record_enddate = update_e005.getText().toString().trim();
        record_endtime = update_e005_1.getText().toString().trim();

        ArrayList<String> nameValuePairs = new ArrayList<String>();
        nameValuePairs.add(record_id);
        nameValuePairs.add(record_name);
        nameValuePairs.add(record_record);
        nameValuePairs.add(record_required);
        nameValuePairs.add(record_types);
        nameValuePairs.add(record_details);
        nameValuePairs.add(record_startdate);
        nameValuePairs.add(record_starttime);
        nameValuePairs.add(record_enddate);
        nameValuePairs.add(record_endtime);

        String result = DBConnector.executeUpdate_record(nameValuePairs);

        //.makeText(getApplicationContext(), "資料已修改完成,請重新載入!", //.LENGTH_SHORT).show();

        initDB();
        dbmysql();
        initDB();
        setupViewComponent();

        scr_update.setVisibility(View.GONE);
        btnAdd_case.setVisibility(View.VISIBLE);
        list001.setVisibility(View.VISIBLE);
    }

    private void dbmysql() {
        sqlctl = "SELECT * FROM record";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        // // ---------------------------
        try {
            String result = DBConnector.executeQuery(nameValuePairs);
            chk_httpstate();
//        String r = result.toString().trim();
//==========================================
            //以下程式碼一定要放在前端藍色程式碼執行之後，才能取得狀態碼
            //存取類別成員 DBConnector.httpstate 判定是否回應 200(連線要求成功)
//            Log.d(TAG, "httpstate=" + DBConnector.httpstate);

            //======================================
            // 選擇讀取特定欄位
            // String result = DBConnector.executeQuery("SELECT id,name FROM
            // member");
            /*******************************************************************************************
             * SQL 結果有多筆資料時使用JSONArray 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             *******************************************************************************************/
            JSONArray jsonArray = new JSONArray(result);
            int a = 0;
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                //--------------------------------------------------------
                int rowsAffected = dbHper.clearRec_record19(); // 匯入前,刪除所有SQLite資料
                //--------------------------------------------------------
                // 處理JASON 傳回來的每筆資料
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    //
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位
                    // --取出 jsonObject
                    // 每個欄位("key","value")-----------------------
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
                        Log.d("TAG", "第" + i + "個欄位 key:" + key + " value:" + value);
                    }
                    // -------------------加入SQLite---------------------------------------
                    long rowID = dbHper.insertRec_record19(newRow);//寫入SQLite
//                    long rowID = dbHper.insertRec_signup(t_email, t_username,t_password);//真正執行SQL 寫入資料庫
                    //tvTitle.setTextColor(Color.BLUE);
                    //tvTitle.setText("顯示資料： 共加入" + Integer.toString(jsonArray.length()) + " 筆");
                }
                // ---------------------------
            } else {
                //.makeText(getApplicationContext(), "主機資料庫無資料", //.LENGTH_LONG).show();
            }
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.e("TAG", e.toString());
        }
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
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 4:
                    ser_msg = "用戶端錯誤回應，請稍後在試(code:" + DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 5:
                    ser_msg = "伺服器error responses，請稍後在試(code:" + DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
            }
//                //.makeText(getBaseContext(), msg, //.LENGTH_SHORT).show();
        }
        if (DBConnector.httpstate == 0) {
            ser_msg = "遠端資料庫異常(code:" + DBConnector.httpstate + ") ";
        }

        //-------------------------------------------------------------------
    }

    private DatePickerDialog.OnDateSetListener datePicDigOnDateSelLis1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            d1 = (year + "-" + (month / 10) +
                    (month % 10 + 1) + "-" + (dayOfMonth / 10) +
                    dayOfMonth % 10);
            e004.setText(d1 + "\n" + s);
        }
    };

    private DatePickerDialog.OnDateSetListener datePicDigOnDateSelLis2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            d2 = (year + "-" + (month / 10) +
                    (month % 10 + 1) + "-" + (dayOfMonth / 10) +
                    dayOfMonth % 10);
            e005.setText(d2 + "\n" + s);
        }
    };

    private DatePickerDialog.OnDateSetListener datePicDigOnDateSelLis3 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            d3 = (year + "-" + (month / 10) +
                    (month % 10 + 1) + "-" + (dayOfMonth / 10) +
                    dayOfMonth % 10);
            update_e004.setText(d3 + "\n" + s);
        }
    };

    private DatePickerDialog.OnDateSetListener datePicDigOnDateSelLis4 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            d4 = (year + "-" + (month / 10) +
                    (month % 10 + 1) + "-" + (dayOfMonth / 10) +
                    dayOfMonth % 10);
            update_e005.setText(d4 + "\n" + s);
        }
    };

//    public void pickertwo(View view) {
//        timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour,minute, false);
//        timePickerDialog.show();
//    }

    //=================================================================================================================
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.information:
//                            bottomNav4.setVisibility(View.VISIBLE);
//                            bottomNav3.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(), Case_info.class));
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
                        case R.id.schedule:
//                            bottomNav4.setVisibility(View.GONE);
//                            bottomNav3.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(), Schedule.class));
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
                        case R.id.statiscs:
//                            bottomNav4.setVisibility(View.GONE);
//                            bottomNav3.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(), Inquire.class));
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
                        case R.id.care:
//                            bottomNav4.setVisibility(View.GONE);
//                            bottomNav3.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(), Carry_info.class));
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
//=================================================================================================================
                        case R.id.nav_home:
//                            bottomNav4.setVisibility(View.GONE);
//                            bottomNav3.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
                        case R.id.nav_news:
//                            bottomNav4.setVisibility(View.GONE);
//                            bottomNav3.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(), News_info.class));
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
                        case R.id.nav_book:
//                            bottomNav4.setVisibility(View.GONE);
//                            bottomNav3.setVisibility(View.VISIBLE);
//                            startActivity(new Intent(getApplicationContext(), Record_main.class));
//                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.nav_cloud:
//                            bottomNav4.setVisibility(View.GONE);
//                            bottomNav3.setVisibility(View.GONE);
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
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
        super.onStop();
        Record_main.this.finish();
//        //.makeText(this, "onStop", //.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
        super.onDestroy();
//        Case_info_basic.this.finish();
//        //.makeText(this, "onDestroy", //.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
//        //.makeText(this, "onPause", //.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initDB();
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
    public void onBackPressed() {
//        stop.performClick();// performClick 整個關掉
//        super.onBackPressed();
    }

    //================================ 下面是MENU ==========================================
    //  Menu下面這兩個最基本的要記起來 onCreateOptionsMenu & onOptionsItemSelected
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);  //選layout ,menu
        return true;
    }

    //====================================================================================
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: //menu那邊+一個 結束 string也要+
                this.finish();
                onBackPressed();  // 關掉
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //================================ 下面是次類別之類的東西 ==============================================

}
