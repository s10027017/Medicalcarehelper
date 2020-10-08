package tw.helper.medicalcare;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Carry_info extends AppCompatActivity implements View.OnClickListener {
    private Intent intent = new Intent();
    BottomNavigationView bottomNav1, bottomNav2;
    private Button b001, b002, b003, b004, b005, b007, b008;
    private Dialog mLoginDlg;
    private int newscrollheight;
    private TextView dialogtext;
    private ListView listView;
    private String check_t;
    private int DBversion = 1;
    private FriendDbHelper dbHper;
    private String DB_FILE = "friends.db";
    private static final String DB_TABLE_carry_info_1 = "carry_info_1";    // 資料庫物件，固定的欄位變數
    private static final String DB_TABLE_carry_info_2 = "carry_info_2";
    private ArrayList<String> recSet_carry_info_1;
    private ArrayList<String> recSet_carry_info_2;
    private ContentResolver mContRes;
    private Handler handler = new Handler();
    private ProgressDialog pd;
    private WebView webView;
    private String MAP_URL = "file:///android_asset/GoogleMap.html";
    private RelativeLayout rela1;
    private ScrollView scollview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carry_info);
        //-------------抓取遠端資料庫設定執行續------------------------------
        StrictMode.setThreadPolicy(new
                StrictMode.
                        ThreadPolicy.Builder().
                detectDiskReads().
                detectDiskWrites().
                detectNetwork().
                penaltyLog().
                build());
        StrictMode.setVmPolicy(
                new
                        StrictMode.
                                VmPolicy.
                                Builder().
                        detectLeakedSqlLiteObjects().
                        penaltyLog().
                        penaltyDeath().
                        build());
        mContRes = getContentResolver();
        //---------------------------------------------------------------------
        initDB();
        dowload();
        setupviewcomponent();
    }

    private void setupviewcomponent() {
        bottomNav1 = findViewById(R.id.nav_view1);
        bottomNav2 = findViewById(R.id.nav_view2);
        BottomNavigationHelper.removeShiftMode(bottomNav1);
        bottomNav1.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        BottomNavigationHelper.removeShiftMode(bottomNav2);
        bottomNav2.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNav1.setOnNavigationItemSelectedListener(navListener);
        bottomNav2.setOnNavigationItemSelectedListener(navListener);
        bottomNav2.setSelectedItemId(R.id.care);
        b001 = (Button) findViewById(R.id.h01_b001);
        b002 = (Button) findViewById(R.id.h01_b002);
        b003 = (Button) findViewById(R.id.h01_b003);
        b004 = (Button) findViewById(R.id.h01_b004);
        b005 = (Button) findViewById(R.id.h01_b005);
//        b006 = (Button) findViewById(R.id.h01_b006);
        b007 = (Button) findViewById(R.id.h01_b007);
        b008 = (Button) findViewById(R.id.h01_b008);
        b001.setOnClickListener(this);
        b002.setOnClickListener(this);
        b003.setOnClickListener(this);
        b004.setOnClickListener(this);
        b005.setOnClickListener(this);
//        b006.setOnClickListener(this);
        b007.setOnClickListener(this);
        b008.setOnClickListener(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        newscrollheight = displayMetrics.heightPixels * 90 / 100; // 設定ScrollView使用尺寸的4/5

        rela1 = (RelativeLayout)findViewById(R.id.RelativeLayout1);
        scollview1 = (ScrollView)findViewById(R.id.scrollview1);

        rela1.setVisibility(View.INVISIBLE);
        scollview1.setVisibility(View.VISIBLE);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(MAP_URL);
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
//                            bottomNav4.setVisibility(View.GONE);
//                            bottomNav3.setVisibility(View.GONE);
//                            startActivity(new Intent(getApplicationContext(), Carry_info.class));
//                            overridePendingTransition(0, 0);
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
    private void initDB() {
        if (dbHper == null) {
            dbHper = new FriendDbHelper(getApplicationContext(), DB_FILE, null, DBversion);
        }
        recSet_carry_info_1 = dbHper.getRecSet();

    }

    private void dowload() {
        if (dbHper.RecCount() == 0) {
            pd = new ProgressDialog(Carry_info.this);
            pd.setMessage("資料下載中...");
            pd.setCancelable(false);
            pd.show();

            handler.postDelayed(updateTimer, 1000); // 延遲
        }
    }

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {

            showOpendata();
            showOpendata2();
            pd.cancel();
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.h01_b001:
                mLoginDlg = new Dialog(Carry_info.this);
                mLoginDlg.setTitle(getString(R.string.h0101_t001));
                mLoginDlg.setCancelable(false);

                mLoginDlg.setContentView(R.layout.carry_info_dialog);

                dialogtext = (TextView) mLoginDlg.findViewById(R.id.mh0101_t001);//dialog表頭文字設定
                dialogtext.setText(getString(R.string.h01_b001));//設定與按鈕相同文字
                //---------------------------------------------------------------------------------
                //在DIALOG顯示JSON資料
                listView = (ListView) mLoginDlg.findViewById(R.id.listview);
                listView.getLayoutParams().height = newscrollheight;
                listView.setLayoutParams(listView.getLayoutParams()); // 重定ScrollView大小
                //-----讀取SQL裡的Opendata-----
                recSet_carry_info_1 = dbHper.getRecSet();
                SimpleAdapter adapter = null;
                List<Map<String, Object>> mList = null;
                mList = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < recSet_carry_info_1.size(); i++) {
                    Map<String, Object> item = new HashMap<>();
                    String[] fld = recSet_carry_info_1.get(i).split("#");
                    //-----同一資料表.欄位為空時.不寫入陣列-----
                    for (int j = 1; j < 5; j++) {
                        if (!fld[j].equals("null")) {
                            item.put("Area", fld[1]);
                            item.put("Phone", fld[2]);
                            item.put("Status", fld[3]);
                            item.put("Add", fld[4]);
                            mList.add(item);
                        }
                    }
                    //-----同一資料表.欄位為空時.不寫入陣列-----
                }
                adapter = new SimpleAdapter(getApplicationContext(),
                        mList,
                        R.layout.carry_info_list,
                        new String[]{"ID", "Area", "Phone", "Status", "Add"},
                        new int[]{R.id.t001, R.id.t002, R.id.t003, R.id.t004, R.id.t005});

                listView.setAdapter(adapter);//設定listview
                //製作dialog的取消按鍵
                Button b006 = (Button) mLoginDlg.findViewById(R.id.mh0101_b001);
                b006.setOnClickListener(this);
                //-------------------------------------------------------
                mLoginDlg.show();
                break;
            //------第二顆按鈕---------------------------------------------------------
            case R.id.h01_b002:
                mLoginDlg = new Dialog(Carry_info.this);
                mLoginDlg.setTitle(getString(R.string.h01_b002));
                mLoginDlg.setCancelable(false);

                mLoginDlg.setContentView(R.layout.carry_info_dialog);

                dialogtext = (TextView) mLoginDlg.findViewById(R.id.mh0101_t001);
                dialogtext.setText(getString(R.string.h01_b002));
                //---------------------------------------------------------------------------------
                //在DIALOG顯示JSON資料
                listView = (ListView) mLoginDlg.findViewById(R.id.listview);
                listView.getLayoutParams().height = newscrollheight;
                listView.setLayoutParams(listView.getLayoutParams()); // 重定ScrollView大小
                // XML直接網路下載，網路操作一定要在新的執行序

//                recSet_carry_info_1 = dbHper.getRecSet("carry_info_1");
                List<Map<String, Object>> mList1 = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < recSet_carry_info_1.size(); i++) {
                    Map<String, Object> item2 = new HashMap<>();
                    String[] fld = recSet_carry_info_1.get(i).split("#");
                    //-----同一資料表.欄位為空時.不寫入陣列-----
                    for (int j = 5; j < 9; j++) {
                        if (!fld[j].equals("null")) {
                            item2.put("Area2", fld[5]);
                            item2.put("Phone2", fld[6]);
                            item2.put("Status2", fld[7]);
                            item2.put("Add2", fld[8]);
                            mList1.add(item2);
                        }
                    }
                    //-----同一資料表.欄位為空時.不寫入陣列-----
                }

                //設定listview
                SimpleAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
                        mList1,
                        R.layout.carry_info_list,
                        new String[]{"ID", "Area2", "Phone2", "Status2", "Add2"},
                        new int[]{R.id.t001, R.id.t002, R.id.t003, R.id.t004, R.id.t005});
                listView.setAdapter(adapter2);

                //製作dialog的取消按鍵
                Button b007 = (Button) mLoginDlg.findViewById(R.id.mh0101_b001);
                b007.setOnClickListener(this);
                //-------------------------------------------------------

                mLoginDlg.show();
                break;
            case R.id.h01_b003:
                Uri uri = Uri.parse("https://www.nhi.gov.tw/QueryN/Query1.aspx?n=FC660C5B07007373&sms=36A0BB334ECB4011&topn=5FE8C9FEAE863B46");
                Intent it2 = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(it2);
                break;
            case R.id.h01_b004:
                Uri uri2 = Uri.parse("https://www.nhi.gov.tw/Content_List.aspx?n=A068D27CBF677629&topn=5FE8C9FEAE863B46");
                Intent it3 = new Intent(Intent.ACTION_VIEW, uri2);

                startActivity(it3);
                break;
            case R.id.h01_b005:
                Uri uri3 = Uri.parse("https://1966.gov.tw/LTC/cp-4495-48857-201.html");
                Intent it4 = new Intent(Intent.ACTION_VIEW, uri3);

                startActivity(it4);
                break;
            case R.id.h01_b006:
//            Intent it =new Intent();
//            it.setClass(Carry_info.this,Mh02.class);
//            startActivity(it);
//                startActivity(new Intent(getApplicationContext(), Mh02.class));
                break;
            case R.id.h01_b007:
                scollview1.setVisibility(View.INVISIBLE);
                rela1.setVisibility(View.VISIBLE);
                webView.loadUrl(MAP_URL);
                break;
            case R.id.h01_b008:
                scollview1.setVisibility(View.VISIBLE);
                rela1.setVisibility(View.INVISIBLE);
                break;
            case R.id.mh0101_b001:
                mLoginDlg.cancel();
                break;
        }
    }

    private void showOpendata() {


        try {
            String Task_opendata
                    = new TransTask().execute("https://quality.data.gov.tw/dq_download_json.php?nid=84329&md5_url=2c7da00cbaf153add88894e433ffecaf").get();
            //String Task_opendata = new TransTask().execute("https://data.epa.gov.tw/api/v1/aqx_p_432?limit=1000&api_key=9be7b239-557b-4c10-9775-78cadfc555e9&format=json").get();
            //解析JSON
            JSONArray jsonArray = new JSONArray(Task_opendata);
            int xx = jsonArray.length();//debug 時可確認是否正確抓到資料

            //排序-------------------
            jsonArray = sortjsonArray(jsonArray);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
//                String Level = jsonData.getString("序號");
                String SiteName = jsonData.getString("區域");
                String Phone = jsonData.getString("電話");
                String Status = jsonData.getString("醫事機構名稱");
                String Add = jsonData.getString("地址");
                //機構名稱 區域  連絡電話
                //----設定省落同樣字元
//                if (SiteName.equals(check_t)) {
//                    SiteName = "..";
//                } else {
//                    check_t = SiteName;
//                }
//                    String msg = null;
                long rowID = dbHper.insertRec_carry_info_1(SiteName, Phone, Status, Add); //真正執行SQL
                int dd = 5;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void showOpendata2() {

        try {
            String Task_opendata2
                    = new TransTask().execute("https://datacenter.taichung.gov.tw/swagger/OpenData/ba6cf04a-2734-4f46-afde-a47da9a72663").get();
            //解析JSON
            JSONArray jsonArray2 = new JSONArray(Task_opendata2);
            int xx = jsonArray2.length();//debug 時可確認是否正確抓到資料

            //排序-------------------
            jsonArray2 = sortjsonArray(jsonArray2);

            for (int j = 0; j < jsonArray2.length(); j++) {
                JSONObject jsonData2 = jsonArray2.getJSONObject(j);
//                String Level = jsonData.getString("序號");
                String SiteName2 = jsonData2.getString("區域");
                String Phone2 = jsonData2.getString("連絡電話");
                String Status2 = jsonData2.getString("機構名稱");
                String Add2 = jsonData2.getString("地址");
                //機構名稱 區域  連絡電話
                //----設定省落同樣字元
//                if (SiteName.equals(check_t)) {
//                    SiteName = "..";
//                } else {
//                    check_t = SiteName;
//                }
                String msg = null;
                long rowID = dbHper.insertRec_carry_info_2(SiteName2, Phone2, Status2, Add2); //真正執行SQL
                //-----需要SHOW出筆數//-----此段打開-----
//                    if (rowID != -1) {
//                        msg = "新增記錄  成功 ! \n" + "目前資料表共有 " + dbHper.RecCount() + " 筆記錄 !";
//                    } else {
//                        msg = "新增記錄  失敗 ! ";
//                    }
//                    //.makeText(getApplicationContext(), msg, //.LENGTH_SHORT).show();
                //-----需要SHOW出筆數//-----此段打開-----
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//        }
    }

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
//        //.makeText(this, "onStop", //.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(updateTimer);
        super.onDestroy();
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
    private JSONArray sortjsonArray(JSONArray jsonArray) {
        //county自定義的排序Method
        final ArrayList<JSONObject> json = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {//將資料存入Arraylist json 中
            try {
                json.add(jsonArray.getJSONObject(i));
            } catch (JSONException jsone) {
                jsone.printStackTrace();
            }

        }

        Collections.sort(json, new Comparator<JSONObject>() {

                    @Override
                    public int compare(JSONObject jsonOb1, JSONObject jsonOb2) {
                        // 用多重key 排序
                        String lidCounty = "", ridCounty = "";
//                String lidStatus="",ridStatus="";
//                String lidPM25="",ridPM25="";
                        try {
                            lidCounty = jsonOb1.getString("County");
                            ridCounty = jsonOb2.getString("County");
//                    lidStatus = jsonOb1.getString("Status");
//                    ridStatus = jsonOb2.getString("Status");
//                    整數判斷方法
//                    if(!jsonOb1.getString("PM2.5").isEmpty()&&!jsonOb2.getString("PM2.5").isEmpty()
//                            &&!jsonOb1.getString("PM2.5").equals("ND")&&!jsonOb2.getString("PM2.5").equals("ND")){
//                        lidPM25=String.format("%02d",Integer.parseInt(jsonOb1.getString("PM2.5")));
//                        ridPM25=String.format("%02d",Integer.parseInt(jsonOb2.getString("PM2.5")));
//                    }else{
//                        lidPM25="0";
//                        ridPM25="0";
//                    }
                        } catch (JSONException jsone) {
                            jsone.printStackTrace();
                        }

                        return lidCounty.compareTo(ridCounty);
                    }
                }

        );
        return new JSONArray(json);
    }

    private class TransTask extends AsyncTask<String, Void, String> {
        String ans;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String line = in.readLine();
                while (line != null) {
                    Log.d("HTTP", line);
                    sb.append(line);
                    line = in.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ans = sb.toString();
            //------------
            return ans;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("s", "s:" + s);
            parseJson(s);
        }

        private void parseJson(String s) {

        }
    }
}
