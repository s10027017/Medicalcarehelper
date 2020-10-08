package tw.helper.medicalcare;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


//import tw.tcnr.medicalcare.providers.FriendsContentProvider;

import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG =" TCNR26 -> " ;
    private Intent intent = new Intent();
    BottomNavigationView bottomNav1,bottomNav2;
    private TextView a04_t001,a02_t001,mStatusTextView,member_t01;
    private Dialog mLoginDlg;
    private EditText edtUserName,edtPassword,modify_e001,modify_e002,modify_e003,modify_e004,resetpsd_e002,resetpsd_e003;
    private Menu menu;
    private MyAlertDialog myAltDlg,myAltDlg1;

    private static final String DB_File = "friends.db",DB_TABLE="sign_up"; //斷絕父子關係 static final
    private FriendDbHelper dbHper;
    private SQLiteDatabase db;
    private String name,pass,t_email,t_username,t_password,t_rpassword,test,testusername,tname,temail,tphone,taddress,tid,tpsd,cpsd;
    private int flag=1;
    private ArrayList<String> recSet;
    private static final int DBversion = 1;

    private static ContentResolver mContRes;
    private String[] MYCOLUMN = new String[]{"ID", "Email", "Username", "Password", "Phone", "Address", "Datetime", "Uid", "Membertype"};  //,"created_time" =不能
    String msg = null;
    private MediaPlayer startmusic;
    private RelativeLayout r_layout,relativelayout_sign_up;
    private ScrollView scr_main;
    private Button google_login,a02_b001,a03_b003,a01_b004,member_btn,f01_b1,f01_b2,f01_b3,f01_b4,b003;
    private ListView listView;
    List<Map<String, Object>> mList;
    private int newscrollheight;
    private ArrayList<String> recSet_sign_up_list;
    private SimpleAdapter adapter=null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;  //放在後面
    private CircleImgView img;
    private URL url;
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    private int hour;
    private MenuItem item06,item07;
    private LinearLayout member_layout;
    private Object[] store,store2;
    private ArrayList<Map<String, Object>> mmList;
    private ListView list001;
    private ScrollView sc01;
    private TextView t001,t002;
    private ActionBar actionBar;
    private ArrayList<String> data,data2;
    private String g_DisplayName,g_Email;
    private String ser_msg;
    private int servermsgcolor;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        enableStrictMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBConnector.connect_ip="https://medicalcarehelper.com/android_mysql_connect26/android_connect_db_all.php";

        initDB();
        dbmysql();

        setupViewComponent();


        testget();
        //================================================================================================
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
        bottomNav1 = findViewById(R.id.nav_view1); // 個案資訊 工作排程 統計查詢 照護園地
        bottomNav2 = findViewById(R.id.nav_view2); // 首頁 最新消息 服務紀錄 社群推廣
        // https://stackoverflow.com/questions/40176244/how-to-disable-bottomnavigationview-shift-mode
        BottomNavigationHelper.removeShiftMode(bottomNav1);  // 生一個外部的class
        bottomNav1.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        BottomNavigationHelper.removeShiftMode(bottomNav2);  // removeShiftMode 新版移除 舊版ShiftMode無法
        bottomNav2.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNav1.setOnNavigationItemSelectedListener(navListener); // 設監聽
        bottomNav2.setOnNavigationItemSelectedListener(navListener);
        bottomNav2.setSelectedItemId(R.id.nav_home);  // 預設選取畫面

        a02_t001 = (TextView) findViewById(R.id.resetpsd_t001); // Title
        a04_t001 = (TextView) findViewById(R.id.a04_t001); //您好 登入 登出用
        a04_t001.setText("");
        scr_main = (ScrollView) findViewById(R.id.scr_main); // ScrollView 裡的圖片跟文字

        relativelayout_sign_up = (RelativeLayout) findViewById(R.id.relativelayout_sign_up); // 代表sign_up的layout
        member_layout = (LinearLayout)findViewById(R.id.member_layout);

        a02_b001 = (Button) findViewById(R.id.a02_b001); // 註冊按鈕
        a03_b003 = (Button) findViewById(R.id.a03_b003); // 登入按鈕
        a01_b004 = (Button) findViewById(R.id.a01_b004); // 忘記密碼按鈕
        member_btn = (Button)findViewById(R.id.member_btn); // 會員按鈕
        f01_b1 = (Button)findViewById(R.id.f01_b1); // 資料設定
        f01_b2 = (Button)findViewById(R.id.f01_b2); // 修改密碼
//        f01_b3 = (Button)findViewById(R.id.f01_b3); // 會員列表
        f01_b4 = (Button)findViewById(R.id.f01_b4); // 登出帳號
        a02_b001.setOnClickListener(this);
        a03_b003.setOnClickListener(this);
        a01_b004.setOnClickListener(this);
        member_btn.setOnClickListener(this);
        f01_b1.setOnClickListener(this);
        f01_b2.setOnClickListener(this);
//        f01_b3.setOnClickListener(this);
        f01_b4.setOnClickListener(this);

        member_t01 = findViewById(R.id.member_t01);

        mStatusTextView = findViewById(R.id.status);
        google_login = (Button) findViewById(R.id.google_login); // google登入按鈕
        google_login.setOnClickListener(this); // 設監聽
        // --START configure_signin--
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // --END configure_signin-
        // --START build_client--
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //--END build_client--





        img = (CircleImgView) findViewById(R.id.google_icon);
        img.setOnClickListener(this);

        java.sql.Date curDate = new java.sql.Date(System.currentTimeMillis()); //  獲取當前時間
        String str = formatter.format(curDate);
//        Calendar c = Calendar.getInstance();
//        hour = c.get(Calendar.HOUR_OF_DAY);

//        a04_t001.setText("現在時間:"+"\n"+str);

        String test1 = str.substring(0,2).toString();
        Integer test2 = Integer.parseInt(test1);
//        Integer test3 = test2+8;
//        a04_t001.setText("現在時間:"+"\n"+test3);
        if(test2>=6 && test2<=18)
        {
            relativelayout_sign_up.setBackgroundResource(R.drawable.good_morning_img);
        }
        else
        {
            relativelayout_sign_up.setBackgroundResource(R.drawable.good_night_img);
        }
        // 開機動畫  抓layout
        r_layout=(RelativeLayout)findViewById(R.id.relativelayout_main);
        r_layout.setAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_test_in));
    }

    private void testget()
    {
        //================================================================================================
        test = getSharedPreferences("record",MODE_PRIVATE)
                .getString("record","");
        //================================================================================================
        //================================================================================================
        if(!test.equals(""))
        {
            data2 = findname(test);
            store2 = data2.toArray();
            tid = store2[0].toString();
            testusername = store2[1].toString();  // username

//            String recname = dbHper.FindRec_signup_name(test);//抓登入帳號的username    這邊有問題 會閃退
//            mStatusTextView.setText(recname+" 您好");
//            mStatusTextView.setText(test+" 您好");
            mStatusTextView.setText(testusername+" 您好");
            member_btn.setVisibility(View.VISIBLE);
            member_t01.setText("會員專區");
//            a04_t001.setText(test+" 您好");
//            u_menu_login(); //有成功才有登出  這邊不能跑
            //.makeText(getApplicationContext(), "登入成功!", //.LENGTH_SHORT).show();
        }
        else
        {

        }
    }



    private void initDB()
    {
        if (dbHper == null)
        {
            dbHper = new FriendDbHelper(getApplicationContext(), DB_File, null, DBversion);
        }
        recSet = dbHper.getRecSet_signup(); //all data  資料全部讀近來
    }
    //=================================================================================================================
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item)
                {
                    switch (item.getItemId())
                    {
                        case R.id.information:
                            startActivity(new Intent(getApplicationContext(),Case_info.class));
                            overridePendingTransition(0,0);
                            finish();
                            return true;
                        case R.id.schedule:
                            startActivity(new Intent(getApplicationContext(),Schedule.class));
                            overridePendingTransition(0,0);
                            finish();
                            return true;
                        case R.id.statiscs:
                            startActivity(new Intent(getApplicationContext(),Inquire.class));
                            overridePendingTransition(0,0);
                            finish();
                            return true;
                        case R.id.care:
                            startActivity(new Intent(getApplicationContext(),Carry_info.class));
                            overridePendingTransition(0,0);
                            finish();
                            return true;
//=================================================================================================================
                        case R.id.nav_home:  // 自己不用再寫了
//                            bottomNav4.setVisibility(View.GONE);
//                            bottomNav3.setVisibility(View.GONE);
//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                            overridePendingTransition(0,0);
                            return true;
                        case R.id.nav_news:
                            startActivity(new Intent(getApplicationContext(),News_info.class));
                            overridePendingTransition(0,0);
                            finish();
                            return true;
                        case R.id.nav_book:
                            startActivity(new Intent(getApplicationContext(),Record_main.class));
                            overridePendingTransition(0,0);
                            finish();
                            return true;
                        case R.id.nav_cloud:
                            startActivity(new Intent(getApplicationContext(),Fans.class));
                            overridePendingTransition(0,0);
                            finish();
                            return true;
//=================================================================================================================

                    }
                    return false;
                }
            };
    //=================================================================================================================

    //=================================================================================================================
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.google_login: // google登入
                google_signin();
//                u_menu_login(); //登入後才有登出
                main_layout(); // 切換回登入的layout
                break;
            case R.id.a02_b001: // 註冊
                mLoginDlg = new Dialog(MainActivity.this);
                mLoginDlg.setCancelable(false);
                mLoginDlg.setContentView(R.layout.sign_up_dlg);
                Button loginBtnOK = (Button) mLoginDlg.findViewById(R.id.m0905_btnOK);
                Button loginBtnCancel = (Button) mLoginDlg.findViewById(R.id.m0905_btnCancel);
                loginBtnOK.setOnClickListener(signupDlgBtnOKOnClkLis);
                loginBtnCancel.setOnClickListener(signupDlgBtnCancelOnClkLis);
                mLoginDlg.show();
                break;
            case R.id.a03_b003: // 登入
                mLoginDlg = new Dialog(MainActivity.this);
                mLoginDlg.setCancelable(false);
                mLoginDlg.setContentView(R.layout.login_dlg);
                Button loginBtnOK1 = (Button) mLoginDlg.findViewById(R.id.m0905_btnOK);
                Button loginBtnCancel1 = (Button) mLoginDlg.findViewById(R.id.m0905_btnCancel);
                loginBtnOK1.setOnClickListener(signinDlgBtnOKOnClkLis);
                loginBtnCancel1.setOnClickListener(signinDlgBtnCancelOnClkLis);
                mLoginDlg.show();
                break;
            case R.id.a01_b004: // 忘記密碼
//                mLoginDlg = new Dialog(MainActivity.this);
//                mLoginDlg.setCancelable(false);
//                mLoginDlg.setContentView(R.layout.resetpassword_dlg);
//                Button loginBtnOK2 = (Button) mLoginDlg.findViewById(R.id.m0905_btnOK);
//                Button loginBtnCancel2= (Button) mLoginDlg.findViewById(R.id.m0905_btnCancel);
//                loginBtnOK2.setOnClickListener(resetBtnOKOnClkLis);
//                loginBtnCancel2.setOnClickListener(resetBtnCancelOnClkLis);
//                mLoginDlg.show();
                break;
            case R.id.list_return:
                mLoginDlg.cancel();
                break;
            case R.id.google_icon:
//                menu.performIdentifierAction(R.id.item0,0); // 到註冊
                sign_in_layout();
//                menu.performIdentifierAction(R.menu.menu_main,0);
//                onOptionsItemSelected();
//                openContextMenu(R.menu.menu_main);

                break;
            case R.id.member_btn:
                member_layout();
                break;
            case R.id.f01_b1:  // 資料設定
                mLoginDlg = new Dialog(MainActivity.this);
                mLoginDlg.setCancelable(false);
                mLoginDlg.setContentView(R.layout.modify_dlg);
                Button loginBtnOK3 = (Button) mLoginDlg.findViewById(R.id.m0905_btnOK);
                Button loginBtnCancel3= (Button) mLoginDlg.findViewById(R.id.m0905_btnCancel);

                loginBtnOK3.setOnClickListener(modifyBtnOKOnClkLis);
                loginBtnCancel3.setOnClickListener(modifyBtnCancelOnClkLis);
                show_modify();
                mLoginDlg.show();
                break;
            case R.id.f01_b2:  // 修改密碼
                mLoginDlg = new Dialog(MainActivity.this);
                mLoginDlg.setCancelable(false);
                mLoginDlg.setContentView(R.layout.resetpassword_dlg);
                Button loginBtnOK2 = (Button) mLoginDlg.findViewById(R.id.m0905_btnOK);
                Button loginBtnCancel2= (Button) mLoginDlg.findViewById(R.id.m0905_btnCancel);
                loginBtnOK2.setOnClickListener(resetBtnOKOnClkLis);
                loginBtnCancel2.setOnClickListener(resetBtnCancelOnClkLis);
                resetpassword();
                mLoginDlg.show();
                break;
//            case R.id.f01_b3:  // 會員列表  先隱藏不要
//                mLoginDlg = new Dialog(MainActivity.this);
//                mLoginDlg.setTitle("會員列表");
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
//                recSet_sign_up_list = dbHper.getRecSet_signup();
//                List<Map<String, Object>> mList1=null;
//                mList = new ArrayList<Map<String, Object>>();
//                for (int i = 0; i < recSet_sign_up_list.size(); i++)  // i是代表y 幾筆資料
//                {
//                    Map<String, Object> item1 = new HashMap<String, Object>(); // 代表X 每筆資料裡面放幾個東西
//                    String[] fld = recSet_sign_up_list.get(i).split("#"); //initDB 裡的 recSet_sign_up_list 已經有抓了 F4可過去看
//                    item1.put("imgView",R.drawable.userconfig);                                // 密碼已拿掉+ "\n" +"密碼:" + fld[3]
//                    item1.put("txtView", "ID:" + fld[0] + "\n"+"姓名:" + fld[2] + "\n" +"Email:" + fld[1] );
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
            case R.id.f01_b4:  //登出帳號
                myAltDlg1 = new MyAlertDialog(this);  //生在外面  內建的對話盒
                myAltDlg1.setTitle(getString(R.string.a04_alertdialog_sign_out_title));
                myAltDlg1.setIcon(android.R.drawable.ic_dialog_alert);
                myAltDlg1.setCancelable(false);
                //                                    擺的位置                 顯示的文字          三個監聽
                myAltDlg1.setButton(BUTTON_POSITIVE, getString(R.string.a04_alertdialog_positive), signoutBtnOKOnClkLis);
                myAltDlg1.setButton(BUTTON_NEUTRAL, getString(R.string.a04_alertdialog_negative), signoutBtnCancelOnClkLis);

                myAltDlg1.show();
//                u_menu_main();
                break;
        }
    }

    private void google_signin()
    {
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent,RC_SIGN_IN);
    }
    private void google_signOut()
    {
        mGoogleSignInClient.signOut()  //登出
                .addOnCompleteListener(this, new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        //--START_EXCLUDE--
                        updateUI(null); //登出動作寫在這
                        // [END_EXCLUDE]
                        img.setImageResource(R.drawable.user); //還原圖示  換頭像
                        member_btn.setVisibility(View.INVISIBLE);
                        member_t01.setText("註冊登入");
                    }
                });

        SharedPreferences record = getSharedPreferences("record",MODE_PRIVATE);
        record.edit()
                .clear()   // 登出就清除  remove(key) 也可以
//                .apply();  // 修改記憶體中的暫存資料，並以非同步式寫入檔案
                .commit();

        test = getSharedPreferences("record",MODE_PRIVATE)
                .getString("record","");

    }
    private void updateUI(GoogleSignInAccount account)
    {
        if(account !=null)
        {
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));
            g_DisplayName=account.getDisplayName(); //暱稱  account. google帳號所有的東西都在這邊 可以選
            g_Email=account.getEmail();  //信箱
//            String g_GivenName=account.getGivenName(); //Firstname
//            String g_FamilyName=account.getFamilyName(); //Last name

//            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName())+"\n Email:"+
//                    account.getEmail()+"\n Firstname:"+
//                    account.getGivenName()+"\n Last name:"+
//                    account.getFamilyName());

            mStatusTextView.setText(account.getDisplayName()+" 您好");
            member_btn.setVisibility(View.VISIBLE);
            member_t01.setText("會員專區");

            SharedPreferences record = getSharedPreferences("record",MODE_PRIVATE);
            record.edit()
                    .putString("record",g_Email)
//                    .apply(); // 還有commit可以用 但它會將其選項同步寫入持永久性儲存空間 要快速建議用apply
                    .commit();
            test = getSharedPreferences("record",MODE_PRIVATE)
                    .getString("record","");

            String rec = dbHper.FindRec_signup(g_Email);//執行sqlite的定義,判斷帳號重複的方法
            if (rec != null)
            {

            }
            else
            {
                //===========================================寫入MYSQL================================================
                ArrayList<String> nameValuePairs = new ArrayList<String>();

                nameValuePairs.add(g_Email);
                nameValuePairs.add(g_DisplayName);
//            nameValuePairs.add(new BasicNameValuePair("password", t_password));  //google那邊不能取得password

                try {
                    Thread.sleep(100); //  延遲Thread 睡眠0.5秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //====================================================================================================
                String result = DBConnector.executeInsert_googlesignup(nameValuePairs);
//                dbmysql();
                //.makeText(getApplicationContext(), "註冊成功!", //.LENGTH_SHORT).show();
                //===========================================寫入MYSQL================================================
            }
            //-------改變圖像--------------
            Uri User_IMAGE = account.getPhotoUrl();
            if(User_IMAGE==null)
            {
                return;
            }

            new AsyncTask<String,Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... params) {
                    String url = params[0];
                    return getBitmapFromURL(url);
                }
                @Override
                protected void onPostExecute(Bitmap result) {
                    img.setImageBitmap(result);  //setImageBitmap
                    super.onPostExecute(result);
                }
            }.execute(User_IMAGE.toString().trim());
            //            String g_id=account.getId();
//            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        }
        else
        {
            //================================================================================================
            test = getSharedPreferences("record",MODE_PRIVATE)
                    .getString("record","");
            //================================================================================================
            //================================================================================================
            if(!test.equals(""))
            {
                mStatusTextView.setText(testusername+" 您好");
//            a04_t001.setText(test+" 您好");
//            u_menu_login(); //有成功才有登出  這邊不能跑
                //.makeText(getApplicationContext(), "登入成功!", //.LENGTH_SHORT).show();
            }
            else
            {
                mStatusTextView.setText("訪客您好");
            }
        }
    }
    private Bitmap getBitmapFromURL(String imageUrl)
    {
        try{
            url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }  catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    //=================================================================================================================
    private Button.OnClickListener signupDlgBtnOKOnClkLis = new Button.OnClickListener() // 註冊確定
    {
        public void onClick(View v)
        {
            signup_dbadd();
            mLoginDlg.cancel();
        }
    };
    private Button.OnClickListener signinDlgBtnOKOnClkLis = new Button.OnClickListener() // 登入確定
    {
        public void onClick(View v)
        {
            sign_in_verification();
            mLoginDlg.cancel();
            main_layout();
        }
    };
    private Button.OnClickListener signupDlgBtnCancelOnClkLis = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            //.makeText(getApplicationContext(), "你已取消註冊!", //.LENGTH_LONG).show();
            mLoginDlg.cancel();
        }
    };
    private Button.OnClickListener signinDlgBtnCancelOnClkLis = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            //.makeText(getApplicationContext(), "你已取消登入!", //.LENGTH_LONG).show();
//            a04_t001.setText(getString(R.string.a04_t001_1) +"  " +getString(R.string.a04_t001_2));
            mLoginDlg.cancel();
        }
    };
    private Button.OnClickListener resetBtnOKOnClkLis = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            resetpassword_mysql_update();
            mLoginDlg.cancel();
        }
    };
    private Button.OnClickListener resetBtnCancelOnClkLis = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            //.makeText(getApplicationContext(), "您已取消修改重設密碼!", //.LENGTH_SHORT).show();
            mLoginDlg.cancel();
        }
    };
    private Button.OnClickListener modifyBtnOKOnClkLis = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            modify_mysql_update();
            mLoginDlg.cancel();
        }
    };

    private Button.OnClickListener modifyBtnCancelOnClkLis = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            //.makeText(getApplicationContext(), "您已取消修改資料!", //.LENGTH_SHORT).show();
            mLoginDlg.cancel();
        }
    };
    //=================================================================================================================
    private DialogInterface.OnClickListener altDlgOnClkPosiBtnLis = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            finish();
            overridePendingTransition(R.anim.anim_test_out,R.anim.anim_test_out);
        }
    };
    private DialogInterface.OnClickListener signoutBtnOKOnClkLis = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
//            a04_t001.setText(getString(R.string.a04_t001_3));
            google_signOut();
            //================================================================================================
            SharedPreferences record = getSharedPreferences("record",MODE_PRIVATE);
            record.edit()
                    .clear()   // 登出就清除  remove(key) 也可以
//                    .apply();  // 修改記憶體中的暫存資料，並以非同步式寫入檔案
                    .commit();
            test = getSharedPreferences("record",MODE_PRIVATE)
                    .getString("record","");
//            a04_t001.setText(test);
            f01_b1.setVisibility(View.GONE);
            f01_b2.setVisibility(View.GONE);
//            f01_b3.setVisibility(View.GONE);
            f01_b4.setVisibility(View.GONE);
            main_layout();
            //================================================================================================
        }
    };
    private DialogInterface.OnClickListener  signoutBtnCancelOnClkLis = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {

        }
    };

    //=================================================================================================================

    private void signup_dbadd()  // 新增
    {   //抓dialog上的EditText
        EditText username = (EditText) mLoginDlg.findViewById(R.id.reset_e001); //姓名
        EditText email = (EditText) mLoginDlg.findViewById(R.id.a02_e002); // Email
        EditText password = (EditText) mLoginDlg.findViewById(R.id.a01_e002); //密碼
        EditText rpassword = (EditText) mLoginDlg.findViewById(R.id.resetpsd_e003); //確認密碼

        t_username=username.getText().toString().trim();
        t_email=email.getText().toString().trim();
        t_password=password.getText().toString().trim();
        t_rpassword=rpassword.getText().toString().trim();

        if(!email.getText().toString().equals(""))  //
        {
            String rec = dbHper.FindRec_signup(t_email);//執行sqlite的定義,判斷帳號重複的方法
            if (rec != null)
            {// 查看帳號有無重複
                email.setText("");
                //.makeText(getApplicationContext(), "帳號已重複!,請重新輸入", //.LENGTH_SHORT).show();
            }
            else if(t_email.equals("") || t_username.equals("") || t_password.equals("") || t_rpassword.equals(""))  //不可空白 要用.equals("")
            {// 資料不得空白
                //.makeText(getApplicationContext(), "資料不得空白!", //.LENGTH_SHORT).show();
            }
            else if(!t_password.equals(t_rpassword))
            {//密碼跟確認密碼要一樣
                //.makeText(getApplicationContext(), "密碼不一致", //.LENGTH_SHORT).show();
            }
            else // 帳號沒重複 可寫入
            {
                //===========================================寫入MYSQL================================================
                ArrayList<String> nameValuePairs = new ArrayList<String>();

                nameValuePairs.add(t_email);
                nameValuePairs.add(t_username);
                nameValuePairs.add(t_password);

                try {
                    Thread.sleep(500); //  延遲Thread 睡眠0.5秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //====================================================================================================
                String result = DBConnector.executeInsert_signup(nameValuePairs);
                dbmysql();
                //.makeText(getApplicationContext(), "註冊成功!", //.LENGTH_SHORT).show();
                //===========================================寫入MYSQL================================================
                long rowID = dbHper.insertRec_signup(t_email, t_username,t_password);//真正執行SQL 寫入資料庫
                if (rowID != -1) // -1代表沒有
                {
                    //.makeText(getApplicationContext(), "註冊成功!", //.LENGTH_SHORT).show();
                }
                else
                {
                    email.setText("");
                    username.setText("");
                    password.setText("");
                    rpassword.setText("");
                    //.makeText(getApplicationContext(), "註冊失敗!", //.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void dbmysql()
    {
        String sqlctl = "SELECT * FROM sign_up";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        // // ---------------------------
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
                int rowsAffected = dbHper.clearRec_signup();                 // 匯入前,刪除所有SQLite資料
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
                    long rowID = dbHper.insertRec(newRow);
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

    private void modify_mysql_update()
    {
        tname = modify_e001.getText().toString().trim();
        temail = modify_e002.getText().toString().trim();
        tphone = modify_e003.getText().toString().trim();
        taddress = modify_e004.getText().toString().trim();

        ArrayList<String> nameValuePairs = new ArrayList<String>();
        nameValuePairs.add(tid);
        nameValuePairs.add(tname);
        nameValuePairs.add(temail);
        nameValuePairs.add(tphone);
        nameValuePairs.add(taddress);
        String result = DBConnector.executeUpdate_modify(nameValuePairs);

        //.makeText(getApplicationContext(), "資料已修改完成,請重新登入!", //.LENGTH_SHORT).show();

        SharedPreferences record = getSharedPreferences("record",MODE_PRIVATE);
        record.edit()
                .clear()   // 登出就清除  remove(key) 也可以
//                .apply();  // 修改記憶體中的暫存資料，並以非同步式寫入檔案
                .commit();

        test = getSharedPreferences("record",MODE_PRIVATE)
                .getString("record","");

//        test=tname;
//        mStatusTextView.setText(test+" 您好");
        mStatusTextView.setText("訪客您好");

        google_signOut();

        scr_main.setVisibility(View.GONE);
        member_btn.setVisibility(View.GONE);
        bottomNav1.setVisibility(View.VISIBLE);
        bottomNav2.setVisibility(View.VISIBLE);
        scr_main.setVisibility(View.VISIBLE);

        dbmysql();
    }

    private void resetpassword_mysql_update()
    {
        resetpsd_e002 = (EditText) mLoginDlg.findViewById(R.id.resetpsd_e002);
        resetpsd_e003 = (EditText) mLoginDlg.findViewById(R.id.resetpsd_e003);

        tpsd = resetpsd_e002.getText().toString().trim();
        cpsd = resetpsd_e003.getText().toString().trim();

        if(tpsd.equals("") || cpsd.equals("")) // else if(!t_password.equals(t_rpassword))
        {
            //.makeText(getApplicationContext(), "密碼不得空白", //.LENGTH_LONG).show();
        }
        else if(!tpsd.equals(cpsd))
        {
            //.makeText(getApplicationContext(), "密碼不一致", //.LENGTH_LONG).show();
        }
        else
        {
            ArrayList<String> nameValuePairs = new ArrayList<String>();
            nameValuePairs.add(tid);
            nameValuePairs.add(tpsd);
            String result = DBConnector.executeUpdate_resetpsd(nameValuePairs);

            //.makeText(getApplicationContext(), "密碼已修改完成,請重新登入!", //.LENGTH_SHORT).show();

            SharedPreferences record = getSharedPreferences("record",MODE_PRIVATE);
            record.edit()
                    .clear()   // 登出就清除  remove(key) 也可以
//                    .apply();  // 修改記憶體中的暫存資料，並以非同步式寫入檔案
                    .commit();

            test = getSharedPreferences("record",MODE_PRIVATE)
                    .getString("record","");

//        test=tname;
//        mStatusTextView.setText(test+" 您好");
            mStatusTextView.setText("訪客您好");

            google_signOut();

            scr_main.setVisibility(View.GONE);
            member_btn.setVisibility(View.GONE);
            bottomNav1.setVisibility(View.VISIBLE);
            bottomNav2.setVisibility(View.VISIBLE);
            scr_main.setVisibility(View.VISIBLE);

            dbmysql();
        }

    }

    private void sign_in_verification()
    {
        EditText username = (EditText) mLoginDlg.findViewById(R.id.reset_e001);
        EditText password = (EditText) mLoginDlg.findViewById(R.id.resetpsd_e003);

        String t_username = username.getText().toString().trim();
        String t_password = password.getText().toString().trim();

        if(t_username.equals("") || t_password.equals(""))
        {
            //.makeText(getApplicationContext(), "帳號或密碼不得為空白!", //.LENGTH_SHORT).show();

        }

        else
        {
            String rec = dbHper.FindRec_signin(t_username, t_password);
            if (rec != null)
            {
                String recname = dbHper.FindRec_signup_name(t_username);//抓登入帳號的username    這邊有問題 會閃退
//                a04_t001.setText(getString(R.string.a04_t001_1) + "  " + recname + "  " + getString(R.string.a04_t001) );
//                mStatusTextView.setText(recname+" 您好");
//                mStatusTextView.setText(test+" 您好");
                mStatusTextView.setText(testusername+" 您好");
//                u_menu_login(); //有成功才有登出
                //.makeText(getApplicationContext(), "登入成功!", //.LENGTH_SHORT).show();

                // 建立存取資料的功能 使用過多可能會導致APP運作變慢  MODE_PRIVATE 只只允許該 APP 存取
//                	MODE_PRIVATE	常用，只允許該 APP 存取
//                	MODE_WORLD_READABLE	不建議，所有 APP 都能讀取
//                	MODE_WORLD_WRITEABLE	不建議，所有 APP 都能存取、寫入
//                	MODE_MULTI_PROCESS	允許多個 process 同時存取
                SharedPreferences record = getSharedPreferences("record",MODE_PRIVATE);
                record.edit()
                        .putString("record",t_username)
//                        .apply(); // 還有commit可以用 但它會將其選項同步寫入持永久性儲存空間 要快速建議用apply
                        .commit();

                testget();
            }
            else
            {
//                a04_t001.setText(getString(R.string.a04_t001_3));
                //.makeText(getApplicationContext(), "帳號密碼有誤!", //.LENGTH_SHORT).show();
            }
        }
    }
    private void show_modify() {

        modify_e001 = (EditText) mLoginDlg.findViewById(R.id.modify_e001);
        modify_e002 = (EditText) mLoginDlg.findViewById(R.id.modify_e002);
        modify_e003 = (EditText) mLoginDlg.findViewById(R.id.modify_e003);
        modify_e004 = (EditText) mLoginDlg.findViewById(R.id.modify_e004);

        tname = modify_e001.getText().toString().trim();
        temail = modify_e001.getText().toString().trim();
        tphone = modify_e001.getText().toString().trim();
        taddress = modify_e001.getText().toString().trim();

        //=====================================================================================
//        store=null;
//        data.clear();

        data= finddata(test);
        store = data.toArray();

        modify_e001.setText(store[1].toString()); // 第一次修改後 再點選資料設定會閃退 不過資料會修改
        modify_e002.setText(store[2].toString());
        modify_e003.setText(store[3].toString());
        modify_e004.setText(store[4].toString());
        tid = store[0].toString();
        dbmysql();
        //=====================================================================================
    }

    private ArrayList<String> findname(String test)
    {
        mContRes = getContentResolver();

        ArrayList<String> recAry = new ArrayList<String>();
        try {
//            String sql = "SELECT * FROM sign_up WHERE Email LIKE " + "'" + test + "'" + " ORDER BY id ASC";

            String sqlctl = "SELECT * FROM sign_up WHERE Email LIKE " + "'" + test + "'" + " ORDER BY id ASC";
            ArrayList<String> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(sqlctl);

//            String result = DBConnector.executeQuery(sql);
            String result = DBConnector.executeQuery(nameValuePairs);

            JSONArray jsonArray = new JSONArray(result);

            if (result != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    recAry.add(jsonData.get("ID").toString());
                    recAry.add(jsonData.get("Username").toString());
                }
            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
        return recAry;
    }

    private ArrayList<String> finddata(String test) {
        mContRes = getContentResolver();

        ArrayList<String> recAry = new ArrayList<String>();
        try {
            String sql = "SELECT * FROM sign_up WHERE Email LIKE " + "'" + test + "'" + " ORDER BY id ASC";  // 要用EMAIL去抓

            String sqlctl = "SELECT * FROM sign_up WHERE Email LIKE " + "'" + test + "'" + " ORDER BY id ASC";
            ArrayList<String> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(sqlctl);

//            String result = DBConnector.executeQuery(sql);
            String result = DBConnector.executeQuery(nameValuePairs);

            JSONArray jsonArray = new JSONArray(result);

            if (result != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    recAry.add(jsonData.get("ID").toString());
                    recAry.add(jsonData.get("Username").toString());
                    recAry.add(jsonData.get("Email").toString());
                    recAry.add(jsonData.get("Phone").toString());
                    recAry.add(jsonData.get("Address").toString());
                }
            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
        return recAry;
    }

    private void resetpassword()
    {
        resetpsd_e002 = (EditText) mLoginDlg.findViewById(R.id.resetpsd_e002);
        resetpsd_e003 = (EditText) mLoginDlg.findViewById(R.id.resetpsd_e003);

        tpsd = resetpsd_e002.getText().toString().trim();
        cpsd = resetpsd_e003.getText().toString().trim();


    }
    //=================================================================================================================

//    private void u_menu_main()
//    {
//        menu.setGroupVisible(R.id.group1,true);
//        menu.setGroupVisible(R.id.group2,false);
//    }
//
//    private void u_menu_login()
//    {
//        menu.setGroupVisible(R.id.group1,false);
//        menu.setGroupVisible(R.id.group2,true);
//    }

    private void sign_in_layout()
    {
        bottomNav1.setVisibility(View.GONE);
        bottomNav2.setVisibility(View.GONE);

        relativelayout_sign_up.setVisibility(View.VISIBLE);

        item06.setVisible(false);
        item07.setVisible(true);
    }

    private void main_layout()
    {
        bottomNav1.setVisibility(View.VISIBLE);
        bottomNav2.setVisibility(View.VISIBLE);
        scr_main.setVisibility(View.VISIBLE);

        relativelayout_sign_up.setVisibility(View.GONE);
//        member_layout.setVisibility(View.GONE);

        f01_b1.setVisibility(View.GONE);
        f01_b2.setVisibility(View.GONE);
//        f01_b3.setVisibility(View.GONE);
        f01_b4.setVisibility(View.GONE);

        item06.setVisible(true);
        item07.setVisible(false);
    }

    private void member_layout()
    {
        bottomNav1.setVisibility(View.GONE);
        bottomNav2.setVisibility(View.GONE);
        scr_main.setVisibility(View.GONE);

        f01_b1.setVisibility(View.VISIBLE);
        f01_b2.setVisibility(View.VISIBLE);
//        f01_b3.setVisibility(View.VISIBLE);
        f01_b4.setVisibility(View.VISIBLE);

        item06.setVisible(false);
        item07.setVisible(true);
    }

    public static String u_md5(String content) {  // md5加密
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    //================================ 下面是生命週期 ======================================================================
//    1.當Activity準備要產生時，先呼叫onCreate方法。
//    2.Activity產生後（還未出現在手機螢幕上），呼叫onStart方法。
//    3.當Activity出現手機上後，呼叫onResume方法。
//    4.當使用者按下返回鍵結束Activity時， 先呼叫onPause方法。
//    5.當Activity從螢幕上消失時，呼叫onStop方法。
//    6.最後完全結束Activity之前，呼叫onDestroy方法。
    @Override
    protected void onStop() {
        super.onStop();
//        if(startmusic.isPlaying()) startmusic.stop();
//        //.makeText(this, "onStop", //.LENGTH_LONG).show();
//        google_signOut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(startmusic.isPlaying()) startmusic.stop();
//        //.makeText(this, "onDestroy", //.LENGTH_LONG).show();
//        google_signOut();
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

    @Override  //加入2個系統函數 google登入用
    protected void onStart()
    {
        super.onStart();
        // --START on_start_sign_in--
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        // 取得上次登入的狀態
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
//        u_menu_login(); //有成功才有登出  閃退
        //--END on_start_sign_in--
    }
    @Override  //加入2個系統函數
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
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
    @Override
    public void finish()
    {
        super.finish();
    }

    //=================================================================================================================
    //  Menu下面這兩個要記起來 onCreateOptionsMenu onOptionsItemSelected
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        this.menu=menu;

//        item01=menu.findItem(R.id.item0);
        item06 = menu.findItem(R.id.item6); // 結束
        item07 = menu.findItem(R.id.item7); // 返回
        return true;
    }
    //=================================================================================================================
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
//            case R.id.item0:  // 註冊登入
//                startActivity(new Intent(getApplicationContext(),Sign_in.class));
//                sign_in_layout();
//                break;
//            case R.id.item1:  // 註冊帳戶
//                mLoginDlg = new Dialog(MainActivity.this);
//                mLoginDlg.setCancelable(false);
//                mLoginDlg.setContentView(R.layout.sign_up_dlg);
//                Button loginBtnOK = (Button) mLoginDlg.findViewById(R.id.m0905_btnOK);
//                Button loginBtnCancel = (Button) mLoginDlg.findViewById(R.id.m0905_btnCancel);
//                loginBtnOK.setOnClickListener(signupDlgBtnOKOnClkLis);
//                loginBtnCancel.setOnClickListener(signupDlgBtnCancelOnClkLis);
//                mLoginDlg.show();
//                break;
//            case R.id.item2:  //會員登入
////                a04_t001.setText("");
//                mLoginDlg = new Dialog(MainActivity.this);
//                mLoginDlg.setCancelable(false);
//                mLoginDlg.setContentView(R.layout.login_dlg);
//                Button loginBtnOK1 = (Button) mLoginDlg.findViewById(R.id.m0905_btnOK);
//                Button loginBtnCancel1 = (Button) mLoginDlg.findViewById(R.id.m0905_btnCancel);
//                loginBtnOK1.setOnClickListener(signinDlgBtnOKOnClkLis);
//                loginBtnCancel1.setOnClickListener(signinDlgBtnCancelOnClkLis);
//                mLoginDlg.show();
//                break;
//            case R.id.item3:  // 忘記密碼
//                mLoginDlg = new Dialog(MainActivity.this);
//                mLoginDlg.setCancelable(false);
//                mLoginDlg.setContentView(R.layout.resetpassword_dlg);
//                Button loginBtnOK2 = (Button) mLoginDlg.findViewById(R.id.m0905_btnOK);
//                Button loginBtnCancel2= (Button) mLoginDlg.findViewById(R.id.m0905_btnCancel);
//                loginBtnOK2.setOnClickListener(resetBtnOKOnClkLis);
//                loginBtnCancel2.setOnClickListener(resetBtnCancelOnClkLis);
//                mLoginDlg.show();
//                break;
            case R.id.item4:
                myAltDlg1 = new MyAlertDialog(this);  //生在外面  內建的對話盒
                myAltDlg1.setTitle(getString(R.string.a04_alertdialog_sign_out_title));
                myAltDlg1.setIcon(android.R.drawable.ic_dialog_alert);
                myAltDlg1.setCancelable(false);
                //                                    擺的位置                 顯示的文字          三個監聽
                myAltDlg1.setButton(BUTTON_POSITIVE, getString(R.string.a04_alertdialog_positive), signoutBtnOKOnClkLis);
                myAltDlg1.setButton(BUTTON_NEUTRAL, getString(R.string.a04_alertdialog_negative), signoutBtnCancelOnClkLis);

                myAltDlg1.show();
//                u_menu_main();
                break;
            case R.id.item5:  // 修改帳戶 還沒用
                mLoginDlg = new Dialog(MainActivity.this);
                mLoginDlg.setCancelable(false);
                mLoginDlg.setContentView(R.layout.modify_dlg);
                Button loginBtnOK3 = (Button) mLoginDlg.findViewById(R.id.m0905_btnOK);
                Button loginBtnCancel3= (Button) mLoginDlg.findViewById(R.id.m0905_btnCancel);
                loginBtnOK3.setOnClickListener(modifyBtnOKOnClkLis);
                loginBtnCancel3.setOnClickListener(modifyBtnCancelOnClkLis);
                show_modify();
                mLoginDlg.show();
                break;
            case R.id.item6:  // 結束
                myAltDlg = new MyAlertDialog(this);  //生在外面  內建的對話盒

                myAltDlg.setTitle(getString(R.string.a04_alertdialog_title));
                //myAltDlg.setMessage(getString(R.string.m0902_t001) + getString(R.string.m0902_b001));
                myAltDlg.setIcon(android.R.drawable.ic_dialog_alert);
                myAltDlg.setCancelable(false);
                //                                    擺的位置                              顯示的文字             三個監聽
                myAltDlg.setButton(BUTTON_POSITIVE, getString(R.string.a04_alertdialog_positive), altDlgOnClkPosiBtnLis);
                myAltDlg.setButton(BUTTON_NEUTRAL, getString(R.string.a04_alertdialog_negative), signoutBtnCancelOnClkLis);

                myAltDlg.show();

                break;
            case R.id.item7:  // 返回
                main_layout();

                break;
//            case R.id.item7: //修改帳戶
////                startActivity(new Intent(getApplicationContext(),Sign_up_list.class));
//                mLoginDlg = new Dialog(MainActivity.this);
//                mLoginDlg.setTitle("會員列表");
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
//                recSet_sign_up_list = dbHper.getRecSet_signup();
//                List<Map<String, Object>> mList1=null;
//                mList = new ArrayList<Map<String, Object>>();
//                for (int i = 0; i < recSet_sign_up_list.size(); i++)  // i是代表y 幾筆資料
//                {
//                    Map<String, Object> item1 = new HashMap<String, Object>(); // 代表X 每筆資料裡面放幾個東西
//                    String[] fld = recSet_sign_up_list.get(i).split("#"); //initDB 裡的 recSet_sign_up_list 已經有抓了 F4可過去看
//                    item1.put("imgView",R.drawable.userconfig);                                // 密碼已拿掉+ "\n" +"密碼:" + fld[3]
//                    item1.put("txtView", "ID:" + fld[0] + "\n"+"姓名:" + fld[2] + "\n" +"Email:" + fld[1] );
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
//
        }
        return super.onOptionsItemSelected(item);
    }



    //=================================================================================================================

}