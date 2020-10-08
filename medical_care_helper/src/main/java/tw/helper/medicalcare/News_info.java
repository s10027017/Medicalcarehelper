package tw.helper.medicalcare;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

//   java未匯入
public class News_info extends AppCompatActivity {
    private Intent intent = new Intent();
    BottomNavigationView bottomNav1, bottomNav2;
    private String[] listFromResource;
    private ArrayList<Map<String, Object>> mList;  //非固定數量陣列
    private Dialog newsdlg;
    private ListView listView;
    private Button e01_b001;
    private TextView e01_t003;
    private TextView e01_t004;
    private String[] listFromResource2;
    private ImageButton e01_imgb001;
    private FriendDbHelper dbHper;
    private int DBversion = 1;
    private String DB_FILE = "friends.db";
    private static final String DB_TABLE = "news_info";    // 資料庫物件，固定的欄位變數
    private ArrayList<String> recSet_news_info;
    //    private int index;
    private String title;
    private Intent intent01=new Intent();
    private HashMap<String, Object> item;
    private String content;
    private String url;
    private String createdate;
    private ContentResolver mContRes;
    private ProgressDialog pd;
    private Handler handler=new Handler();
//    private String content;
//    private String url;
//    private String createdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_info);
//        //-------------抓取遠端資料庫設定執行續------------------------------
//        StrictMode.setThreadPolicy(new
//                StrictMode.
//                        ThreadPolicy.Builder().
//                detectDiskReads().
//                detectDiskWrites().
//                detectNetwork().
//                penaltyLog().
//                build());
//        StrictMode.setVmPolicy(
//                new
//                        StrictMode.
//                                VmPolicy.
//                                Builder().
//                        detectLeakedSqlLiteObjects().
//                        penaltyLog().
//                        penaltyDeath().
//                        build());
//        mContRes = getContentResolver();
//        //---------------------------------------------------------------------

        //--------連接SQLite------
        initDB();
        dowload();
//        getOpenData();
//        if(dbHper.RecCount_news()==0){//資料庫有資料的話
//            getOpenData();
//        }else{//資料庫沒資料就抓取opendata存入資料庫
//            showdata();//抓取資料庫顯示資料
//        }
        bottomNav1 = findViewById(R.id.nav_view1);
        bottomNav2 = findViewById(R.id.nav_view2);
        // https://stackoverflow.com/questions/40176244/how-to-disable-bottomnavigationview-shift-mode
        BottomNavigationHelper.removeShiftMode(bottomNav1);  // 生一個外部的class
        bottomNav1.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        BottomNavigationHelper.removeShiftMode(bottomNav2);  // removeShiftMode 新版移除 舊版ShiftMode無法
        bottomNav2.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNav1.setOnNavigationItemSelectedListener(navListener);
        bottomNav2.setOnNavigationItemSelectedListener(navListener);
        bottomNav2.setSelectedItemId(R.id.nav_news);
        setupViewComponent();
    }

    private void setupViewComponent() {

//        txtresult = (TextView) findViewById(R.id.e01_e001);
//        e01_t003=(TextView)findViewById(R.id.e01_t003);  //dlg新聞標題
//        e01_t004=(TextView)findViewById(R.id.e01_t004); //dlg新聞內容
        //listFromResource = getResources().getStringArray(R.array.e_news_title);  //新聞標題陣列
        //listFromResource2 = getResources().getStringArray(R.array.e_news_content); //新聞內容陣列
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 90 / 100; // 設定ScrollView使用尺寸的4/5
        //
        listView = (ListView) findViewById(R.id.e01_listview);
        listView.getLayoutParams().height = newscrollheight;
        listView.setLayoutParams(listView.getLayoutParams()); // 重定ScrollView大小
        showdata();
        //設定listview
    }
    private void showdata(){

        //-----讀取SQLlite裡的Opendata-----
        recSet_news_info = dbHper.getRecSet_news();
        //SimpleAdapter adapter = null;
        //List<Map<String, Object>> mList=null;
        mList = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < recSet_news_info.size(); i++) {
            item = new HashMap<String, Object>();
            String[] fld = recSet_news_info.get(i).split("#");
            item.put("id", fld[0]);
            item.put("title", fld[1]);
            item.put("content", fld[2]);
            item.put("url", fld[3]);
            item.put("createdate", fld[4]);
            mList.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                mList,
                R.layout.news_list_item,
                new String[]{"title"},
                new int[]{R.id.e01_t002}
        );
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(liON);

    }
//    private final ListView.OnItemClickListener liON = new ListView.OnItemClickListener(){
//
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            //.makeText(getApplicationContext(),position ,//.LENGTH_SHORT ).show();
//
//
//            intent.setClass(News_info.this, News_content.class)
//                    .putExtra("index", position);
//
//            startActivity(intent);
//        }
//    };

    AdapterView.OnItemClickListener liON = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //.makeText(getApplicationContext(),Integer.toString(position),//.LENGTH_SHORT).show();
            intent01.setClass(getApplicationContext(), News_content.class).putExtra("index", position);;
            startActivity(intent01);

//
        }
    };

    private void initDB() {
        if (dbHper == null) {
            dbHper = new FriendDbHelper(getApplicationContext(), DB_FILE, null, DBversion);
        }
        recSet_news_info = dbHper.getRecSet_news();
    }
    private void dowload() {
        if (dbHper.RecCount_news() == 0) {
            pd = new ProgressDialog(News_info.this);
            pd.setMessage("資料下載中...");
            pd.setCancelable(false);
            pd.show();

            handler.postDelayed(updateTimer, 2000); // 延遲
        }
    }
    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            getOpenData();
            pd.cancel();
            showdata();
        }

    };

    private void getOpenData() {
        //if (dbHper.RecCount_news() == 0) {
        try {
            String Task_opendata
                    = new TransTask().execute("https://www.fda.gov.tw/DataAction").get();
            //List<Map<String, Object>> mList;
            //mList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(Task_opendata);
//
            for (int i = 0; i < 30; i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                //Map<String, Object> item = new HashMap<String, Object>();

                //String index = String.valueOf(i);  //轉字串
                title = jsonData.getString("標題");
                content = jsonData.getString("內容");
                url = jsonData.getString("附檔連結");
                createdate = jsonData.getString("發布日期");

                String msg = null;
                long rowID = dbHper.insertRec_news( title, content, url, createdate); //真正執行SQL
                if (rowID != -1) {
                    msg = "新增記錄  成功 ! \n" + "目前資料表共有 " + dbHper.RecCount_news() + " 筆記錄 !";
                } else {
                    msg = "新增記錄  失敗 ! ";
                }
                ////.makeText(getApplicationContext(), msg, //.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    //}

//    private Button.OnClickListener e01_imgb001on=new Button.OnClickListener(){
//
//        @Override
//        public void onClick(View v) {
//            newsdlg.cancel();
//        }
//    };


    //=================================================================================================================
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.information:
                            startActivity(new Intent(getApplicationContext(), Case_info_basic.class));
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
//                            bottomNav4.setVisibility(View.GONE);
//                            bottomNav3.setVisibility(View.GONE);
//                            startActivity(new Intent(getApplicationContext(), News_info.class));
//                            overridePendingTransition(0, 0);
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
        News_info.this.finish();
//        //.makeText(this, "onStop", //.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        News_info.this.finish();
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
        super.onBackPressed();
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
    private JSONArray sortjsonArray(JSONArray jsonArray) {
        //county自定義的排序Method
        final ArrayList<JSONObject> json = new ArrayList<>();
        for (int i=0;i<jsonArray.length();i++){//將資料存入Arraylist json 中
            try {
                json.add(jsonArray.getJSONObject(i));
            }catch (JSONException jsone){
                jsone.printStackTrace();
            }

        }

//        Collections.sort(json,new Comparator<JSONObject>(){
//
//                    @Override
//                    public int compare(JSONObject jsonOb1, JSONObject jsonOb2) {
//                        // 用多重key 排序
//                        String lidCounty = "", ridCounty = "";
////                String lidStatus="",ridStatus="";
////                String lidPM25="",ridPM25="";
//                        try {
//                            lidCounty = jsonOb1.getString("County");
//                            ridCounty = jsonOb2.getString("County");
////                    lidStatus = jsonOb1.getString("Status");
////                    ridStatus = jsonOb2.getString("Status");
////                    整數判斷方法
////                    if(!jsonOb1.getString("PM2.5").isEmpty()&&!jsonOb2.getString("PM2.5").isEmpty()
////                            &&!jsonOb1.getString("PM2.5").equals("ND")&&!jsonOb2.getString("PM2.5").equals("ND")){
////                        lidPM25=String.format("%02d",Integer.parseInt(jsonOb1.getString("PM2.5")));
////                        ridPM25=String.format("%02d",Integer.parseInt(jsonOb2.getString("PM2.5")));
////                    }else{
////                        lidPM25="0";
////                        ridPM25="0";
////                    }
//                        }catch (JSONException jsone){
//                            jsone.printStackTrace();
//                        }
//
//                        return lidCounty.compareTo(ridCounty);
//                    }
//                }
//
//        );

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
