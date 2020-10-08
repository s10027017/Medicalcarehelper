package tw.helper.medicalcare;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
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
import java.util.Iterator;
import java.util.Map;

public class Case_info_basic extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnTouchListener {
    private static final String DB_file = "friends.db", DB_TABLE = "case_info";
    private FriendDbHelper dbHper;

    private static final int DBversion = 1;
    private int index = 0;
    private ArrayList<String> recSet;
    private Intent intent = new Intent();

    BottomNavigationView bottomNav1, bottomNav2;
    private EditText e001, e002, e003, e004, e005, e006, e007;
    private Spinner s001, s002, s003;
    private RadioButton rb001, rb002;
    private Button b001, b002;

    private String sS001, sS002, sS003;
    private String te001, te002, te003, te004, te005, trb001, te006, te007;
    private ArrayList<Map<String, Object>> mList;
    private String d = "";
    private ContentResolver mContRes;
    private ContentResolver mContRes_case_info;
    private String[] MYCOLUMN = new String[]{"CID", "FID", "DID", "HID", "name", "sex", "birthday", "age",  "email",  "address", "phone","t005", "t006", "t007", "t008", "t009"};
    private String TAG = "tcnr18=>";
    String msg = null;
    private String id;
    private String ser_msg;
    private int servermsgcolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_info_basic);
        DBConnector.connect_ip="https://medicalcarehelper.com/tcnr18/android_connect_db_all.php";
        setupViewComponent();

        dbmysql(); //抓取MySQL資料 並寫進SQLite  如SQLite有資料會先刪除再寫入
        initDB(); //抓取SQLite資料  沒有刪除現有SQLite功能
        setvaluefromSQLite();
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
    //**************************************************
//*       檢查連線狀況
//**************************************************
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
    //抓取SQLite資料  如果SQLite沒有資料表創建資料表
    private void initDB() {
        if (dbHper == null)
            dbHper = new FriendDbHelper(this, DB_file, null, DBversion);
        recSet = dbHper.getRecSet_case_info(); //重新載入SQLite
    }

    private void setupViewComponent() {
        bottomNav1 = findViewById(R.id.nav_view1);
        bottomNav2 = findViewById(R.id.nav_view2);
        BottomNavigationHelper.removeShiftMode(bottomNav1);
        bottomNav1.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        BottomNavigationHelper.removeShiftMode(bottomNav2);
        bottomNav2.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNav1.setOnNavigationItemSelectedListener(navListener);
        bottomNav2.setOnNavigationItemSelectedListener(navListener);
//        -------------------------------------------------------------------------------------------------------------------
        //設定跳轉class為this.class  (也就是去抓)
        Intent intent = this.getIntent();
        //設定class的標題title
        id = intent.getStringExtra("id");
//        -------------------------------------------------------------------------------------------------------------------
        e001 = (EditText) findViewById(R.id.b02_e001);//被照護者姓名
        e002 = (EditText) findViewById(R.id.b02_e002);//生日
        e003 = (EditText) findViewById(R.id.b02_e003);//年齡
        e004 = (EditText) findViewById(R.id.b02_e004);//Email
        e005 = (EditText) findViewById(R.id.b02_e005);//地址
        e006 = (EditText) findViewById(R.id.b02_e006);//緊急連絡人
        e007 = (EditText) findViewById(R.id.b02_e007);//緊急連絡電話

        s001 = (Spinner) findViewById(R.id.b02_s001);//縣市
        s002 = (Spinner) findViewById(R.id.b02_s002);//地區
        s003 = (Spinner) findViewById(R.id.b02_s003);//關係
        rb001 = (RadioButton) findViewById(R.id.b02_rb001);//男
        rb002 = (RadioButton) findViewById(R.id.b02_rb002);//女
        b001 = (Button) findViewById(R.id.b02_b001);//取消
        b002 = (Button) findViewById(R.id.b02_b002);//儲存
//        -------------------------------------------------------------------------------------------------------------------
        //縣市
        ArrayAdapter<CharSequence> //設定ArrayAdapter的變數
                adapsexlist01 = ArrayAdapter.createFromResource(this, R.array.b01_a002,   //設定資料來源
                android.R.layout.simple_spinner_item); //textViewResId
        adapsexlist01.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定Spinner下拉的選單類型
        s001.setAdapter(adapsexlist01);
        s001.setOnItemSelectedListener(this);
        //關係
        ArrayAdapter<CharSequence> //設定ArrayAdapter的變數
                adapsexlist03 = ArrayAdapter.createFromResource(this, R.array.b01_a004,   //設定資料來源
                android.R.layout.simple_spinner_item); //textViewResId
        adapsexlist03.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定Spinner下拉的選單類型
        s003.setAdapter(adapsexlist03);
        s003.setOnItemSelectedListener(this);
//        -------------------------------------------------------------------------------------------------------------------
        e002.setOnTouchListener(this);
        b001.setOnClickListener(this);
        b002.setOnClickListener(this);
//        -------------------------------------------------------------------------------------------------------------------

    }

    private void setvaluefromSQLite() {
        for (int i=0;i<recSet.size();i++){

        }
//        switch (j){
//            case 4:  //姓名
//                e001.setText(c.getString(j));
//                break;
//            case 5: //性別
//                String a=c.getString(j);
////                        a="女";
//                if (a.equals("男")){
//                    rb001.setChecked(true);
//                }else if (a.equals("女")){
//                    rb002.setChecked(true);
//                }
//                break;
//            case 6: //生日
//                e002.setText(c.getString(j));
//                break;
//            case 7: //年齡
//                e003.setText(c.getString(j));
//                break;
//            case 8: //Email
//                e004.setText(c.getString(j));
//                break;
//            case 9: //地址
//
//                e005.setText(c.getString(j));
//                break;
//        }
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
                break;
            case R.id.b02_b002:
                te001 = e001.getText().toString().trim();
                te002 = e002.getText().toString().trim();
                te003 = e003.getText().toString().trim();
                te004 = e004.getText().toString().trim();
                te005 = sS001 + sS002 + e005.getText().toString().trim();
                te006 = e006.getText().toString().trim();
                te007 = e007.getText().toString().trim();
                if (rb001.isChecked()) {
                    trb001 = rb001.getText().toString();
                } else if (rb002.isChecked()) {
                    trb001 = rb002.getText().toString();
                }
                //==========================================================
                MyAlertDialog dialog = new MyAlertDialog(Case_info_basic.this);
                dialog.setTitle("測試用dialog");
                dialog.setMessage("確定要更新");
                dialog.show();
                break;
        }
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
            case R.id.b02_s002:
                sS002 = parent.getSelectedItem().toString();
                break;
            case R.id.b02_s003:
                sS003 = parent.getSelectedItem().toString();
                break;
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
                            return true;
                        case R.id.schedule:
                            startActivity(new Intent(getApplicationContext(), Schedule.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.statiscs:
                            startActivity(new Intent(getApplicationContext(), Inquire.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.care:
                            startActivity(new Intent(getApplicationContext(), Carry_info.class));
                            overridePendingTransition(0, 0);
                            return true;
//=================================================================================================================
                        case R.id.nav_home:
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.nav_news:
                            startActivity(new Intent(getApplicationContext(), News_info.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.nav_book:
                            startActivity(new Intent(getApplicationContext(), Record_main.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.nav_cloud:
                            startActivity(new Intent(getApplicationContext(), Fans.class));
                            overridePendingTransition(0, 0);
                            return true;
//=================================================================================================================

//=================================================================================================================
                    }
                    return false;
                }
            };
    //================================ 下面是生命週期 ===========================================
//    1.當Activity準備要產生時，先呼叫onCreate方法。
//    2.Activity產生後（還未出現在手機螢幕上），呼叫onStart方法。
//    3.當Activity出現手機上後，呼叫onResume方法。
//    4.當使用者按下返回鍵結束Activity時， 先呼叫onPause方法。
//    5.當Activity從螢幕上消失時，呼叫onStop方法。
//    6.最後完全結束Activity之前，呼叫onDestroy方法。
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
        return true;
    }

    //====================================================================================
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: //menu那邊+一個 結束 string也要+
                Case_info_basic.this.finish();
//                onBackPressed();  // 關掉
                break;
//            case R.id.basic:
//
//                return true;
//            case R.id.visit:
//                startActivity(new Intent(getApplicationContext(), Case_info_visit.class));
//                overridePendingTransition(0, 0);
//                return true;
//            case R.id.notice:
//                startActivity(new Intent(getApplicationContext(), Case_info_notice.class));
//                overridePendingTransition(0, 0);
//                return true;
//            case R.id.contract:
//                startActivity(new Intent(getApplicationContext(), Case_info_contract.class));
//                overridePendingTransition(0, 0);
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //================================ 下面是次類別之類的東西 ==============================================
}
