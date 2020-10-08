package tw.helper.medicalcare;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

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

public class Case_info extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnTouchListener {
    private Intent intent = new Intent();
    BottomNavigationView bottomNav1, bottomNav2;
    private HashMap<String, Object> item;
    private List<Map<String, Object>> mList = new ArrayList<>();  //建立一個可以新增內容的Array
    private GridView b01_gv001;
    private FriendDbHelper dbHper;
    private static final String DB_file = "friends.db", DB_TABLE = "case_info";
    private static final int DBversion = 1;
    private ArrayList<String> recSet;
    private String TAG = "tcnr18=>";
    private String ser_msg;
    private int servermsgcolor;
    private ScrollView scr_main, scr_update;
    private EditText e001, e002, e003, e004, e005, e006, e007, update_e001, update_e002, update_e003, update_e004, update_e005, update_e006, update_e007;
    private String te001, te002, te003, te004, te005, trb001, update_trb001, te006, te007, te0066;
    private Spinner s001, s002, s003, update_s001, update_s002, update_s003;
    private RadioButton rb001, rb002, update_rb001, update_rb002;
    private Button b001, b002, update_b001, update_b002, update_b003;
    private String sS001, sS002, update_sS001, update_sS002, sS003, case_id, update_name, update_birthday, update_age,
            update_email, update_addr, update_phone, update_sex;
    private String d = "";
    private String d2 = "";
    private String test;
    private Menu menu;
    private MenuItem menu_insert,menu_exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_info);
        DBConnector.connect_ip = "https://medicalcarehelper.com/tcnr18/android_connect_db_all.php";

        SharedPreferences record = getSharedPreferences("record", MODE_PRIVATE);
        test = getSharedPreferences("record", MODE_PRIVATE)
                .getString("record", "");

        bottomNav1 = findViewById(R.id.nav_view1);
        bottomNav2 = findViewById(R.id.nav_view2);
        BottomNavigationHelper.removeShiftMode(bottomNav1);
        bottomNav1.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        BottomNavigationHelper.removeShiftMode(bottomNav2);
        bottomNav2.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNav1.setOnNavigationItemSelectedListener(navListener);
        bottomNav2.setOnNavigationItemSelectedListener(navListener);

        initDB();
        dbmysql();

        setupViewComponent();
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

    private void initDB() {
        if (dbHper == null)
            dbHper = new FriendDbHelper(this, DB_file, null, DBversion);

        recSet = dbHper.getRecSet_case_info_where(test);
//        recSet = dbHper.getRecSet_case_info(); //重新載入SQLite
    }

    //抓取MySQL資料 並寫進SQLite  如SQLite有資料會先刪除再寫入
    private void dbmysql() {
        String sqlctl = "SELECT * FROM case_info";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = DBConnector.executeQuery(nameValuePairs);
//==========================================
            chk_httpstate();
            //======================================
            Log.d(TAG, "httpstate=" + DBConnector.httpstate);
            /*******************************************************************************************
             * SQL 結果有多筆資料時使用JSONArray 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             *******************************************************************************************/
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                //--------------------------------------------------------
                int rowsAffected = dbHper.clearRec_case_info();                 // 匯入前,刪除所有SQLite資料
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
                        Log.d(TAG, "第" + i + "個欄位 key:" + key + " value:" + value);
                    }
                    // -------------------加入SQLite---------------------------------------
                    long rowID = dbHper.insertRec_case_info(newRow);
                }
            } else {
                Log.d(TAG, "主機資料庫無資料(code: " + DBConnector.httpstate + ") ");
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private void chk_httpstate() {
        //以下程式碼一定要放在前端藍色程式碼執行之後，才能取得狀態碼
        //存取類別成員 DBConnector.httpstate 判定是否回應 200(連線要求成功)
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
        }
        if (DBConnector.httpstate == 0) {
            ser_msg = "遠端資料庫異常(code:" + DBConnector.httpstate + ") ";
        }
        //-------------------------------------------------------------------
    }

    private void setupViewComponent() {
        b01_gv001 = (GridView) findViewById(R.id.b01_gridview01);
        scr_main = (ScrollView) findViewById(R.id.scr_main);
        scr_update = (ScrollView) findViewById(R.id.scr_update);
//從資料庫取值
        mList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < recSet.size(); i++) {
            item = new HashMap<String, Object>();// TODO 要注意物件new在for迴圈內, 不然會出錯變重複
            String[] fld = recSet.get(i).split("#");
            item.put("img", R.drawable.userconfig);
            item.put("name", fld[4]);
//            item.put(TAG_IMAGE, imgArr[i%4]); //之前設定的陣列圖片，可以刪除，記得drawable要清
            mList.add(item);
        }
        //==========設定listView============
        //因為自定義Adapter，圖片網址已經寫在那邊，這裡只要載入文字就好
        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                mList,
                R.layout.case_info_list,
                new String[]{"img", "name"},
                new int[]{R.id.case_info_gv_img, R.id.case_info_gv_name}
        );
        b01_gv001.setAdapter(adapter);//將抓取的資料設定到表格視窗
        b01_gv001.setOnItemClickListener(onClickGridView);//建立表格視窗按鈕監聽

        e001 = (EditText) findViewById(R.id.b02_e001);//被照護者姓名
        e002 = (EditText) findViewById(R.id.b02_e002);//生日
        e003 = (EditText) findViewById(R.id.b02_e003);//年齡
        e004 = (EditText) findViewById(R.id.b02_e004);//Email
        e005 = (EditText) findViewById(R.id.b02_e005);//地址
//        e006 = (EditText) findViewById(R.id.b02_e006);//緊急連絡人
        e007 = (EditText) findViewById(R.id.b02_e007);//緊急連絡電話

        s001 = (Spinner) findViewById(R.id.b02_s001);//縣市
        s002 = (Spinner) findViewById(R.id.b02_s002);//地區
//        s003 = (Spinner) findViewById(R.id.b02_s003);//關係
        rb001 = (RadioButton) findViewById(R.id.b02_rb001);//男
        rb002 = (RadioButton) findViewById(R.id.b02_rb002);//女
        b001 = (Button) findViewById(R.id.b02_b001);//取消
        b002 = (Button) findViewById(R.id.b02_b002);//新增
//        -------------------------------------------------------------------------------------------------------------------
        update_e001 = (EditText) findViewById(R.id.update_b02_e001);//被照護者姓名
        update_e002 = (EditText) findViewById(R.id.update_b02_e002);//生日
        update_e003 = (EditText) findViewById(R.id.update_b02_e003);//年齡
        update_e004 = (EditText) findViewById(R.id.update_b02_e004);//Email
        update_e005 = (EditText) findViewById(R.id.update_b02_e005);//地址
//        update_e006 = (EditText) findViewById(R.id.update_b02_e006);//緊急連絡人
        update_e007 = (EditText) findViewById(R.id.update_b02_e007);//緊急連絡電話

        update_s001 = (Spinner) findViewById(R.id.update_b02_s001);//縣市
        update_s002 = (Spinner) findViewById(R.id.update_b02_s002);//地區
//        update_s003 = (Spinner) findViewById(R.id.update_b02_s003);//關係
        update_rb001 = (RadioButton) findViewById(R.id.update_b02_rb001);//男
        update_rb002 = (RadioButton) findViewById(R.id.update_b02_rb002);//女
        update_b001 = (Button) findViewById(R.id.update_b02_b001);//取消
        update_b002 = (Button) findViewById(R.id.update_b02_b002);//新增
        update_b003 = (Button) findViewById(R.id.update_b02_b003);//新增

        // update下面還沒做

        //縣市
        ArrayAdapter<CharSequence> //設定ArrayAdapter的變數
                adapsexlist01 = ArrayAdapter.createFromResource(this, R.array.b01_a002,   //設定資料來源
                android.R.layout.simple_spinner_item); //textViewResId
        adapsexlist01.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定Spinner下拉的選單類型
        s001.setAdapter(adapsexlist01);
        s001.setOnItemSelectedListener(this);
        update_s001.setAdapter(adapsexlist01);
        update_s001.setOnItemSelectedListener(this);
        //關係
        ArrayAdapter<CharSequence> //設定ArrayAdapter的變數
                adapsexlist03 = ArrayAdapter.createFromResource(this, R.array.b01_a004,   //設定資料來源
                android.R.layout.simple_spinner_item); //textViewResId
        adapsexlist03.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定Spinner下拉的選單類型
//        s003.setAdapter(adapsexlist03);
//        s003.setOnItemSelectedListener(this);
//        update_s003.setAdapter(adapsexlist03);
//        update_s003.setOnItemSelectedListener(this);
//        -------------------------------------------------------------------------------------------------------------------
        e002.setOnTouchListener(this);
        update_e002.setOnTouchListener(this);
        b001.setOnClickListener(this);
        b002.setOnClickListener(this);
        update_b001.setOnClickListener(this);
        update_b002.setOnClickListener(this);
        update_b003.setOnClickListener(this);
//        -------------------------------------------------------------------------------------------------------------------

    }

    private final GridView.OnItemClickListener onClickGridView = new GridView.OnItemClickListener() {
        //建立表格視窗按鈕監聽方法
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            updatelayout();
            String[] update = recSet.get(position).split("#");
            // CID FID DID HID name birthday age sex email phone address
            //  0   1      2     3       4            5          6     7      8         9           10
            update_e001.setText(update[4]);//姓名
            update_e002.setText(update[5]);//生日
            update_e003.setText(update[6]);//年齡
            update_e004.setText(update[8]);//Email
            update_e005.setText(update[10]);//地址
//            update_e006.setText(update[]);//緊急連絡人
            update_e007.setText(update[9]);//緊急連絡電話
            if (update_rb001.isChecked()) { // 性別
                update_trb001 = update_rb001.getText().toString();
            } else if (update_rb002.isChecked()) {
                update_trb001 = update_rb002.getText().toString();
            }

            case_id = update[0].toString();

            update_name = update_e001.getText().toString().trim();
            update_birthday = update_e002.getText().toString().trim();
            update_age = update_e003.getText().toString().trim();
            update_email = update_e004.getText().toString().trim();
//            te005 = sS001 + sS002 + e005.getText().toString().trim(); // 地址
//            update_e005.setText("");  // 要更新前 要空的 不然會疊加
            update_addr = update_sS001 + update_sS002 + update_e005.getText().toString().trim();
            update_phone = update_e007.getText().toString().trim();
            update_sex = update_trb001;
        }
    };

    private void case_layout() {
        b01_gv001.setVisibility(View.GONE);
        scr_main.setVisibility(View.VISIBLE);
        menu_insert.setVisible(false);
        menu_exit.setVisible(true);

        e001.setText("");
        e002.setText("");
        e003.setText("");
        e004.setText("");
        e005.setText("");
        e007.setText("");
    }

    private void main_layout() {
        scr_main.setVisibility(View.GONE);
        b01_gv001.setVisibility(View.VISIBLE);
    }

    private void updatelayout() {
        b01_gv001.setVisibility(View.GONE);
        scr_update.setVisibility(View.VISIBLE);
        menu_insert.setVisible(false);
        menu_exit.setVisible(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b02_b001:
                e001.setText("");
                e002.setText("");
                e003.setText("");
                e004.setText("");
                e005.setText("");
                main_layout();
//                Case_info_basic.this.finish();
//                startActivity(new Intent(getApplicationContext(), Case_info.class));
                break;
            case R.id.b02_b002: // 新增
                te001 = e001.getText().toString().trim(); // 姓名
                te002 = e002.getText().toString().trim(); // 生日
                te003 = e003.getText().toString().trim(); // 年齡
                te004 = e004.getText().toString().trim(); // Email
                te005 = sS001 + sS002 + e005.getText().toString().trim(); // 地址
//                te006 = e006.getText().toString().trim(); // 緊急聯絡人
//                te0066 = sS003; // 關係
                te007 = e007.getText().toString().trim(); // 緊急連絡人電話

                test = getSharedPreferences("record", MODE_PRIVATE)
                        .getString("record", "");

                if (rb001.isChecked()) { // 性別
                    trb001 = rb001.getText().toString();
                } else if (rb002.isChecked()) {
                    trb001 = rb002.getText().toString();
                }
                if (te001.equals("") || te002.equals("") || te003.equals("") || te004.equals("") || te005.equals("")
                        || te007.equals("") || trb001.equals("")) {
                    //.makeText(this, "資料空白無法新增 !", //.LENGTH_SHORT).show();
                } else {
                    //===========================================寫入MYSQL================================================
                    ArrayList<String> nameValuePairs = new ArrayList<String>();

                    nameValuePairs.add(te001);
                    nameValuePairs.add(te002);
                    nameValuePairs.add(te003);
                    nameValuePairs.add(te004);
                    nameValuePairs.add(te005);
//                    nameValuePairs.add(te006);
//                    nameValuePairs.add(te0066);
                    nameValuePairs.add(te007);
                    nameValuePairs.add(trb001);
                    nameValuePairs.add(test);

                    try {
                        Thread.sleep(500); //  延遲Thread 睡眠0.5秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //====================================================================================================
                    String result = DBConnector.executeInsert_case_info(nameValuePairs);
                    dbmysql();
                    //.makeText(getApplicationContext(), "新增成功!", //.LENGTH_SHORT).show();

                    main_layout();
                    recSet = dbHper.getRecSet_case_info_where(test);
                    setupViewComponent();
                }
                break;
            case R.id.update_b02_b001:
                b01_gv001.setVisibility(View.VISIBLE);
                scr_update.setVisibility(View.GONE);
                break;
            case R.id.update_b02_b002:
                modify_mysql_update();
                break;
            case R.id.update_b02_b003:
                modify_mysql_del();
                break;
        }

    }

    private void modify_mysql_update() {
        update_name = update_e001.getText().toString().trim();
        update_birthday = update_e002.getText().toString().trim();
        update_age = update_e003.getText().toString().trim();
        update_email = update_e004.getText().toString().trim();
//            te005 = sS001 + sS002 + e005.getText().toString().trim(); // 地址
//        update_e005.setText("");  // 要更新前 要空的 不然會疊加
//        String update_e005_1 = update_e005.getText().toString().trim();  // 再抓一次
        update_addr = update_sS001 + update_sS002 + update_e005.getText().toString().trim();
        update_phone = update_e007.getText().toString().trim();
        update_sex = update_trb001;

        ArrayList<String> nameValuePairs = new ArrayList<String>();
        nameValuePairs.add(case_id);
        nameValuePairs.add(update_name);
        nameValuePairs.add(update_sex);
        nameValuePairs.add(update_birthday);
        nameValuePairs.add(update_age);
        nameValuePairs.add(update_email);
        nameValuePairs.add(update_phone);
        nameValuePairs.add(update_addr);

        String result = DBConnector.executeUpdate_case(nameValuePairs);

        //.makeText(getApplicationContext(), "資料已修改完成,請重新載入!", //.LENGTH_SHORT).show();

        scr_update.setVisibility(View.GONE);
        b01_gv001.setVisibility(View.VISIBLE);

        initDB();
        dbmysql();
        initDB();
        setupViewComponent();
    }

    private void modify_mysql_del()
    {

        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(case_id);

//-----------------------------------------------
        String result = DBConnector.executeDelet_case(nameValuePairs);

        //.makeText(getApplicationContext(), "資料已刪除,請重新載入!", //.LENGTH_SHORT).show();

        scr_update.setVisibility(View.GONE);
        b01_gv001.setVisibility(View.VISIBLE);
        menu_insert.setVisible(true);
        menu_exit.setVisible(false);

        initDB();
        dbmysql();
        initDB();
        setupViewComponent();


    }
    private DatePickerDialog.OnDateSetListener datePicDigOnDateSelLis = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            d = (year + getString(R.string.n_yy) +
                    (month + 1) + getString(R.string.n_mm) +
                    dayOfMonth + getString(R.string.n_dd)
            );
            e002.setText(d + "\n");
            Calendar now = Calendar.getInstance();
            int age = now.get(Calendar.YEAR) - year;
            e003.setText(age + getString(R.string.age));
        }
    };

    private DatePickerDialog.OnDateSetListener datePicDigOnDateSelLis2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            d2 = (year + getString(R.string.n_yy) +
                    (month + 1) + getString(R.string.n_mm) +
                    dayOfMonth + getString(R.string.n_dd)
            );
            update_e002.setText(d2 + "\n");
            Calendar now2 = Calendar.getInstance();
            int age2 = now2.get(Calendar.YEAR) - year;
            update_e003.setText(age2 + getString(R.string.age));
        }
    };

//    private DatePickerDialog.OnDateSetListener datePicDigOnDateSelLis4 = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//            d4 = (year + "-" + (month / 10) +
//                    (month % 10 + 1) + "-" + (dayOfMonth / 10) +
//                    dayOfMonth % 10);
//            update_e005.setText(d4 + "\n" + s);
//        }
//    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN:
                switch (v.getId()) {
                    case R.id.b02_e002:
                        e003.setText("");
                        Calendar now = Calendar.getInstance();
                        DatePickerDialog datePicDlg = new DatePickerDialog(
                                this,
                                datePicDigOnDateSelLis,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH)
                        );
//                        datePicDlg.setTitle(getString(R.string.b02_e002));
//                datePicDlg.setMessage(getString(R.string.F10701_datemessage));
                        datePicDlg.setIcon(android.R.drawable.ic_dialog_info);
                        datePicDlg.setCancelable(false);
                        datePicDlg.show();
                        break;

                    case R.id.update_b02_e002:
                        update_e002.setText("");
                        Calendar now2 = Calendar.getInstance();
                        DatePickerDialog datePicDlg2 = new DatePickerDialog(
                                this,
                                datePicDigOnDateSelLis2,
                                now2.get(Calendar.YEAR),
                                now2.get(Calendar.MONTH),
                                now2.get(Calendar.DAY_OF_MONTH)
                        );
//                        datePicDlg.setTitle(getString(R.string.b02_e002));
//                datePicDlg.setMessage(getString(R.string.F10701_datemessage));
                        datePicDlg2.setIcon(android.R.drawable.ic_dialog_info);
                        datePicDlg2.setCancelable(false);
                        datePicDlg2.show();
                        break;

//                    case R.id.update_e005: //  更新 結束時間 日期
//                        update_e005.setText("");
//                        Calendar now4 = Calendar.getInstance();
//                        DatePickerDialog datePicDlg4 = new DatePickerDialog(
//                                this,
//                                datePicDigOnDateSelLis4,
//                                now4.get(Calendar.YEAR),
//                                now4.get(Calendar.MONTH),
//                                now4.get(Calendar.DAY_OF_MONTH)
//                        );
//                        datePicDlg4.setTitle("選擇日期");
//                        datePicDlg4.setIcon(android.R.drawable.ic_dialog_info);
//                        datePicDlg4.setCancelable(false);
//                        datePicDlg4.show();
//                        break;
                }
                break;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.b02_s001:
                sS001 = parent.getSelectedItem().toString();
                if (sS001.equals(u_chinaanimal(position))) {
                    u_changecountries(position);
                } else {
                    Log.d(TAG, "選擇錯誤");
                }
                break;
            case R.id.update_b02_s001:
                update_sS001 = parent.getSelectedItem().toString();
                if (update_sS001.equals(u_chinaanimal(position))) {
                    u_changecountries(position);
                } else {
                    Log.d(TAG, "選擇錯誤");
                }
                break;
            case R.id.b02_s002:
                sS002 = parent.getSelectedItem().toString();
                break;
            case R.id.update_b02_s002:
                update_sS002 = parent.getSelectedItem().toString();
                break;
//            case R.id.b02_s003:
//                sS003 = parent.getSelectedItem().toString();
//                break;
        }
    }

    private void u_changecountries(int position) {
        //設定Spinner的內容
        ArrayAdapter<CharSequence> //設定ArrayAdapter的變數
                adapsexlist02 = ArrayAdapter.createFromResource(this, R.array.ch_a001 + position,   //設定資料來源
                android.R.layout.simple_spinner_item); //textViewResId
        adapsexlist02.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定Spinner下拉的選單類型
        s002.setAdapter(adapsexlist02);
        s002.setOnItemSelectedListener(this);
        update_s002.setAdapter(adapsexlist02);
        update_s002.setOnItemSelectedListener(this);
    }

    private String u_chinaanimal(int input_i) {
        String c_number = "";
        String china_number[] = {"臺北市", "新北市", "桃園市", "臺中市", "臺南市", "高雄市", "基隆市", "新竹縣", "新竹市",
                "苗栗縣", "彰化縣", "南投縣", "雲林縣", "嘉義縣", "嘉義市", "屏東縣", "宜蘭縣", "花蓮縣", "臺東縣", "澎湖縣", "連江縣"};
        c_number = china_number[input_i % 21];
        return c_number;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //.makeText(getApplicationContext(), "請選擇縣市與地區", //.LENGTH_LONG).show();
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
                            startActivity(new Intent(getApplicationContext(), Inquire.class));
                            overridePendingTransition(0, 0);
                            finish();
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
        Case_info.this.finish();
//        //.makeText(this, "onStop", //.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Case_info.this.finish();
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
    public void onBackPressed() {
//        stop.performClick();// performClick 整個關掉
//        super.onBackPressed();
    }

    //================================ 下面是MENU ==========================================
    //  Menu下面這兩個最基本的要記起來 onCreateOptionsMenu & onOptionsItemSelected
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.case_info, menu);  //選layout ,menu

        getMenuInflater().inflate(R.menu.case_info,menu);
        this.menu=menu;

        menu_insert = menu.findItem(R.id.b01_insert);
        menu_exit = menu.findItem(R.id.action_settings);

//        getMenuInflater().inflate(R.menu.menu_main,menu);
//        this.menu=menu;
//
////        item01=menu.findItem(R.id.item0);
//        item06 = menu.findItem(R.id.item6); // 結束
//        item07 = menu.findItem(R.id.item7); // 返回
        return true;
    }

    //====================================================================================
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: //menu那邊+一個 結束 string也要+
//                Case_info.this.finish();
//                startActivity(new Intent(getApplicationContext(), Case_info.class));

                b01_gv001.setVisibility(View.VISIBLE);
                scr_main.setVisibility(View.GONE);
                scr_update.setVisibility(View.GONE);
                menu_insert.setVisible(true);
                menu_exit.setVisible(false);

                break;

            case R.id.b01_insert:
                case_layout();
                SharedPreferences record = getSharedPreferences("record", MODE_PRIVATE);
                test = getSharedPreferences("record", MODE_PRIVATE)
                        .getString("record", "");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //================================ 下面是次類別之類的東西 ==============================================

}
