package tw.helper.medicalcare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class News_content extends AppCompatActivity implements View.OnClickListener {
    //private Intent intent = new Intent();
    BottomNavigationView bottomNav1, bottomNav2;
    private TextView title;
    private TextView content;
    private Button backbtn;
    private FriendDbHelper dbHper;
    private int DBversion = 1;
    private String DB_FILE = "friends.db";
    private static final String DB_TABLE = "news_info";
    private ArrayList<String> recSet;
    private int news_index;
    private String news_title;
    private String news_content;
    private String news_url;
    private String news_created;
    private ArrayList<Map<String, Object>> mList;
    private HashMap<String, Object> item;
    private SimpleAdapter adapter;

    private ListView listview02;
    private ArrayList<String> recSet_news_content;
    private TextToSpeech tts;
    private Context context;
    private String news_content_new2;
    private String news_content_new1;
    private ImageButton ttsplay;
    private ImageButton ttsstop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);
        bottomNav1 = findViewById(R.id.nav_view1);
        bottomNav2 = findViewById(R.id.nav_view2);
        BottomNavigationHelper.removeShiftMode(bottomNav1);
        bottomNav1.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        BottomNavigationHelper.removeShiftMode(bottomNav2);
        bottomNav2.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNav1.setOnNavigationItemSelectedListener(navListener);
        bottomNav2.setOnNavigationItemSelectedListener(navListener);
        //bottomNav2.setSelectedItemId(R.id.nav_news);
        setupViewComponent();
    }

    private void setupViewComponent() {
        title= (TextView) findViewById(R.id.e01_t003);
        //content=(TextView)findViewById(R.id.e01_t005);
//        sview=(ScrollView)findViewById(R.id.e01_s001);
//        l_content=(LinearLayout)findViewById(R.id.e01_layout004);
        listview02=(ListView)findViewById(R.id.e01_listview02);
        ttsplay= (ImageButton)findViewById(R.id.e01_ttsplay);
        ttsstop= (ImageButton)findViewById(R.id.e01_ttsstop);



        ttsplay.setOnClickListener(this);
        ttsstop.setOnClickListener(this);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 90 / 100; // 設定ScrollView使用尺寸的0.7
        listview02.getLayoutParams().height = newscrollheight;
        listview02.setLayoutParams(listview02.getLayoutParams()); // 重定ScrollView大小

        //----------TTS語音朗讀----------------
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // 設定語系
                    //setLanguage：設置語言。
                    //英語為Locale.ENGEN;
                    //法語為Locale.FRENCH;
                    //德語為Locale.GERMAN;
                    //意大利語為Locale.ITALIAN;
                    //漢語普通話為Locale.CHINA（需安裝中文引擎，如科大訊飛+）
                    int result = tts.setLanguage(Locale.CHINESE);

                    //setPitch：設置音調.1.0正常音調;低於1.0的為低音;高於1.0的為高音
                    tts.setPitch(1.0f); // 設定語音間距

                    //setSpeechRate：設置語速.1.0正常語速; 0.5慢一半的語速; 2.0;快一倍的語速
                    tts.setSpeechRate(1.0f); // 設定語音速率
                    //------------------------------------------
                    //speak : 開始對指定文本進行語音朗讀。
                    //synthesizeToFile : 把指定文本的朗讀語音輸出到文件。
                    //stop : 停止朗讀。
                    //shutdown : 關閉語音引擎。
                    //isSpeaking : 判斷是否在語音朗讀。
                    //getLanguage : 獲取當前的語言。
                    //getCurrentEngine : 獲取當前的語音引擎。
                    //getEngines : 獲取系統支持的所有語音引擎。
                    //------------------------------------------
                    // 確認手機所設定的TTS引擎是否支援該語系的語音
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                        //.makeText(context, getString(R.string.msg1), //.LENGTH_SHORT).show();
                    } else {
//                        //.makeText(context, getString(R.string.msg2), //.LENGTH_SHORT).show();
//                        talkBtn.setEnabled(true);
//                        Drawable top = getResources().getDrawable(R.drawable.speaker_on);
//                        talkBtn.setCompoundDrawablesWithIntrinsicBounds(
//                                null, top, null, null);
                    }
                } else {
//                    //.makeText(context, getString(R.string.msg3), //.LENGTH_SHORT).show();
                }
            }
        });

        //backbtn=(Button)findViewById(R.id.e01_b001);

        dbHper = new FriendDbHelper(getApplicationContext(), DB_FILE, null, DBversion);
        recSet_news_content = dbHper.getRecSet_news();
        //-----------------------從News_info接收資料用----------------------------------
//        Intent intent = this.getIntent();
//        news_index = intent.getIntExtra("index",0);//getIntExtra後面要給個預設值避免取不到值
        Intent intent = getIntent();
        news_index = intent.getIntExtra("index",0);

        //-------------------取得資料庫資料-------------------------------------------
        try{
            String[] fld = recSet_news_content.get(news_index).split("#");
            // ------------對照 AST Point 123(act,Point ,PlaceDB,SQLwriteThread)
            news_title = fld[1];//
            news_content = fld[2];//
            //news_url = fld[3];//
            news_created = fld[4];//

//            //文章資料處理 &ensp; <br />
            news_content_new1 = news_content.replace("<br />", "\n");
            news_content_new2 = news_content_new1.replace("&ensp;", "\0");

            //=====Set Adapter=====
            mList = new ArrayList<>();
            item = new HashMap<String, Object>();
//            item.put("content",news_content);
            item.put("content","發布日期:"+news_created+"\n\n"+news_content_new2);
            mList.add(item);
            adapter = new SimpleAdapter(
                    getApplicationContext(),
                    mList,
                    R.layout.news_contentview,
                    new String[]{"content"},
                    new int[]{R.id.e01_t005}
            );



        }catch(RuntimeException e){

        }

        //---------隨機圖片-----------
//        int i=(int)(Math.random()*3);

        //------------------------------
        //ImageView showimg = (ImageView) findViewById(R.id.ast_point_img01); //layout放置圖片
//        String httpname = "http://192.168.60.61/pic/p0" + i+ ".jpg";   //圖片來源
//        Picasso.get() .load("https://images.unsplash.com/photo-1508821018848-ecd7deba3d41") .into(showimg);
//        Picasso.get() .load(point_img[i]) .into(showimg);  //從陣列 point_img中隨機抓取圖片作為背景

        //Picasso.get() .load(url) .into(showimg);

        //設定class標題
        //this.setTitle(news_title);//設定title
        //------------將接收資料傳到layout-----------------------------------------
        title.setText(news_title);
        listview02.setAdapter(adapter);
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.e01_ttsplay:
                tts.stop();
                String talkString = news_content_new2;
//        tts.speak(CharSequence text,   int queueMode,  android.os.Bundle params,   String utteranceId);
                tts.speak(talkString, TextToSpeech.QUEUE_FLUSH, null);
                ttsplay.setVisibility(View.INVISIBLE);
                ttsstop.setVisibility(View.VISIBLE);
                break;

            case R.id.e01_ttsstop:
                tts.stop();
                ttsplay.setVisibility(View.VISIBLE);
                ttsstop.setVisibility(View.INVISIBLE);

                break;


        }



    }

    //=================================================================================================================
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.information:
                            startActivity(new Intent(getApplicationContext(), Case_info_basic.class));
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
        if (tts != null) {
            //stop : 停止朗讀。
            //shutdown : 關閉語音引擎。
            tts.stop();
            tts.shutdown();
        }
        News_content.this.finish();
//        //.makeText(this, "onStop", //.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            //stop : 停止朗讀。
            //shutdown : 關閉語音引擎。
            tts.stop();
            tts.shutdown();
        }
        News_content.this.finish();
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
//        //.makeText(getApplicationContext(),getString(R.string.onBackPressed), //.LENGTH_SHORT).show();
        //.makeText(getApplicationContext(),"請按右上角結束", //.LENGTH_SHORT).show();

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
                startActivity(new Intent(getApplicationContext(),News_info.class));
                if (tts != null) {
                    //stop : 停止朗讀。
                    //shutdown : 關閉語音引擎。
                    tts.stop();
                    tts.shutdown();
                }
//                onBackPressed();  // 關掉
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        switch (parent.getId()){
//            case 0:
//
//                break;
//            case 1:
//
//                break;
//        }
//    }


    //================================ 下面是次類別之類的東西 ==============================================

}
