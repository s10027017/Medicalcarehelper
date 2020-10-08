package tw.helper.medicalcare;



import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;



import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;




import static java.util.Calendar.DAY_OF_WEEK;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Schedule extends AppCompatActivity {


    BottomNavigationView bottomNav1, bottomNav2;



    private TextView tv ;
    TextView t001, t002;
    Button b001, b002;
    private int year;
    private int month;
    private int monthday;
    private int wek;
    private int day;
    private String[]   allid=new String[1];
    LinearLayout l001,l002;
    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    TableRow.LayoutParams params3=new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    TableRow.LayoutParams params4=new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

    Calendar calendar = Calendar.getInstance();
    private Dialog schedule_insert,schedule_select,schedule_record,schedule_update;
    private DatePickerDialog picker;
    private schedule_dbhelper dbhelper;
    private schedule_dbhelper dbhelper_friends;
    private static OkHttpClient client = new OkHttpClient();
    private String[] weekday = {"日", "一", "二", "三", "四", "五", "六", "七"};


    private String[][] member_case = {
            { "1", "小一", "0972078901", "台中市西屯區中工一路","吃飯" },
            { "2", "陳二", "0972078902", "台中市西屯區中工二路","吃飯" },
            { "3", "張三", "0972078903", "台中市西屯區中工三路","吃飯" },
            { "4", "李四", "0972078904", "台中市西屯區中工四路","吃飯" },
            { "5", "王五", "0972078905", "台中市西屯區中工五路","吃飯" },
            { "6", "阿六", "0972078906", "台中市西屯區中工六路","吃飯" },
            { "7", "黃七", "0972078907", "台中市西屯區中工七路","吃飯" },
            { "8", "周八", "0972078908", "台中市西屯區中工八路","吃飯" },
            { "9", "潘九", "0972078909", "台中市北屯區松竹路二段160巷5-3號","吃飯" },

    };


    private Spinner s001,s002,s003;

    String nowyear,nowmonth,nowday,nowtime,nowweek;
    String[][][] final_arr = new String[21][32][10];
    private CheckBox c001,c002,c003,c004,c005,c006,c000;
    private Uri uri;
    int  calender_id;
    String   schedule_id;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//抓取系統時間


        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        bottomNav1 = findViewById(R.id.nav_view1);
        bottomNav2 = findViewById(R.id.nav_view2);
        BottomNavigationHelper.removeShiftMode(bottomNav1);
        bottomNav1.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        BottomNavigationHelper.removeShiftMode(bottomNav2);
        bottomNav2.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNav1.setOnNavigationItemSelectedListener(navListener);
        bottomNav2.setOnNavigationItemSelectedListener(navListener);
        bottomNav1.setSelectedItemId(R.id.schedule);



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

        calendar.setTime(new Date());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) +1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        wek=calendar.get(DAY_OF_WEEK);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        monthday = calendar.get(Calendar.DATE);


        l001 = (LinearLayout) findViewById(R.id.g01_l001);
        l002 = (LinearLayout) findViewById(R.id.g01_l002);
        b001=(Button)findViewById(R.id.g01_b001);
        b002=(Button)findViewById(R.id.g01_b002);
        t001=(TextView) findViewById(R.id.g01_t001);
        t002=(TextView) findViewById(R.id.g01_t002);

        b001.setOnClickListener(on_button);
        b002.setOnClickListener(on_button);

        params1.setMargins(0,0,1,1);
        params1.width=199;
        params1.height=149;

        params2.setMargins(0,1,1,1);
        params2.width=199;
        params2.height=98;
        params2.gravity=1;

        params3.setMargins(1,0,1,1);
        params3.width=98;
        params3.height=149;
        params3.gravity=1;

        params4.setMargins(1,1,1,1);
        params4.width=98;
        params4.height=98;
        params4.gravity=1;

        t002.setText(Integer.toString(month)+getString(R.string.g01_t002));

        //抓取圖形字--------------------------------------
        ImageView iv = new ImageView(this);
        iv.setLayoutParams(params1);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setImageResource(R.drawable.word);
        l001.addView(iv);
        //-------------------------------------------------


        //產生時間表頭-------------------------------
        for (int i = 6; i <= 21; i++)
        {
            String mm = String.format("%02d", i)+":00";
            TextView   tv = new TextView(this); // tv 繼承TextView
            tv.setLayoutParams(params2);
            tv.setText(mm); // 將產生的物件填入文字.
            tv.setGravity(1);
            tv.setTextSize(12);
            tv.setBackgroundColor(Color.parseColor("#A5D6E8"));
            l001.addView(tv);// 顯示textview物件

        }
     initdb();
        setupviewconponent();



//
//        FormBody body = new FormBody.Builder()
//                .add("selefunc_string", "query")
//                .add("query_string", "select  name,phone,address from case_info")
//                .build();
//
////--------------
//        Request request = new Request.Builder()
//                .url("https://medicalcarehelper.com/tcnr18/android_connect_db_all.php")
//                .post(body)
//                .build();
//
//
//    //    String   result=response.body().string();
//     try (Response response = client.newCall(request).execute()) {
////
//      String   result=response.body().string();
//
//
//
//                 } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            JSONObject json = new JSONObject(result);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//
////            JSONObject object=JSONObject.fromObject(result);
////
////
////            3 for(int i=0;i<array.size();i++){
////                4     Map o=(Map)array.get(i);
////                5     System.out.println(o.get("name")+" "+o.get("price"));
////                6 }
//            // ===========================================
//            // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
//        //    httpstate = response.code();
////            Log.d(TAG, "executeQuery=" + response);
//            // ===========================================
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        }

    private void initdb() {

        dbhelper =new schedule_dbhelper(this, "schedule.db", null, 1);

        String member_id = getSharedPreferences("record", MODE_PRIVATE).getString("record", "");

        SQLiteDatabase db1 = dbhelper.getWritableDatabase();
        db1.delete("case_info",null,null);
        db1.close();


        FormBody body2 = new FormBody.Builder()
                .add("selefunc_string", "query")
                .add("query_string","select * from  case_info")
                .build();
        Request request2 = new Request.Builder()
                .url("https://medicalcarehelper.com/tcnr18/android_connect_db_all.php")
                .post(body2)
                .build();




        try (Response response2 = client.newCall(request2).execute()) {

            String ss= response2.body().string();
            JSONArray jsonArray = new JSONArray(ss);



            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonData = jsonArray.getJSONObject(i);
                SQLiteDatabase db2 = dbhelper.getWritableDatabase();
//通過getString("")分別取出裡面的資訊
                //    String name = jsonData.getString("name");
                ContentValues newrow=new ContentValues();
                newrow.put("CID",jsonData.getString("CID"));
                newrow.put("FID",jsonData.getString("FID"));
                newrow.put("DID",jsonData.getString("DID"));
                newrow.put("HID",jsonData.getString("HID"));
                newrow.put("name",jsonData.getString("name"));
                newrow.put("birthday",jsonData.getString("birthday"));
                newrow.put("age",jsonData.getString("age"));
                newrow.put("sex",jsonData.getString("sex"));
                newrow.put("email",jsonData.getString("email"));
                newrow.put("phone",jsonData.getString("phone"));
                newrow.put("address",jsonData.getString("address"));
                db2.insert("case_info",null,newrow);
                db2.close();
                //   //.makeText(getApplicationContext(), "name:"+name, //.LENGTH_LONG).show();;
            }





            String     msg="ok";
            //     //.makeText(getApplicationContext(), msg, //.LENGTH_LONG).show();;
        } catch (IOException | JSONException e) {

            String     msg="not ok";
            //.makeText(getApplicationContext(), msg, //.LENGTH_LONG).show();;
        }


        if ((member_id!= null)&&(member_id.length() != 0)){





        SQLiteDatabase db = dbhelper.getWritableDatabase();
            db.delete("schedule",null,null);
            db.delete("record",null,null);
            db.close();




       // //.makeText(getApplicationContext(), "init", //.LENGTH_LONG).show();;
        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "query")
                .add("query_string","select * from schedule  where member_id='"+member_id+"'")
                .build();
        Request request = new Request.Builder()
                .url("https://medicalcarehelper.com/tcnr19/android_schedule_db_all.php")
                .post(body)
                .build();




        try (Response response = client.newCall(request).execute()) {

            String ss= response.body().string();
            JSONArray jsonArray = new JSONArray(ss);



            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonData = jsonArray.getJSONObject(i);
                SQLiteDatabase db3 = dbhelper.getWritableDatabase();
//通過getString("")分別取出裡面的資訊
            //    String name = jsonData.getString("name");
                ContentValues newrow=new ContentValues();
                newrow.put("member_id",jsonData.getString("member_id"));
                newrow.put("case_id",jsonData.getString("case_id"));
                newrow.put("calender_id",jsonData.getString("calender_id"));
                newrow.put("date_start",jsonData.getString("date_start"));
                newrow.put("date_end",jsonData.getString("date_end"));
                newrow.put("time_start",jsonData.getString("time_start"));
                newrow.put("time_end",jsonData.getString("time_end"));
                newrow.put("hour",jsonData.getString("hour"));
                newrow.put("week",jsonData.getString("week"));
                newrow.put("name",jsonData.getString("name"));
                newrow.put("phone",jsonData.getString("phone"));
                newrow.put("address",jsonData.getString("address"));
                newrow.put("service",jsonData.getString("service"));
                db3.insert("schedule",null,newrow);
                db3.close();
                //   //.makeText(getApplicationContext(), "name:"+name, //.LENGTH_LONG).show();;
            }





            String     msg="ok";
      //     //.makeText(getApplicationContext(), msg, //.LENGTH_LONG).show();;
        } catch (IOException | JSONException e) {

            String     msg="not ok";
            //.makeText(getApplicationContext(), msg, //.LENGTH_LONG).show();;
        }


            FormBody body1 = new FormBody.Builder()
                    .add("selefunc_string", "query")
                    .add("query_string","select * from record  where member_id='"+member_id+"'")
                    .build();
            Request request1 = new Request.Builder()
                    .url("https://medicalcarehelper.com/tcnr19/android_record_db_all.php")
                    .post(body1)
                    .build();




            try (Response response1= client.newCall(request1).execute()) {

                String ss= response1.body().string();
                JSONArray jsonArray = new JSONArray(ss);



                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    SQLiteDatabase db4 = dbhelper.getWritableDatabase();
//通過getString("")分別取出裡面的資訊
                    //    String name = jsonData.getString("name");
                    ContentValues newrow=new ContentValues();
                    newrow.put("member_id",jsonData.getString("member_id"));
                    newrow.put("case_id",jsonData.getString("case_id"));
                    newrow.put("calender_id",jsonData.getString("calender_id"));
                    newrow.put("date_start",jsonData.getString("date_start"));
                    newrow.put("date_end",jsonData.getString("date_end"));
                    newrow.put("time_start",jsonData.getString("time_start"));
                    newrow.put("time_end",jsonData.getString("time_end"));
                    newrow.put("hour",jsonData.getString("hour"));
                    newrow.put("week",jsonData.getString("week"));
                    newrow.put("wek",jsonData.getString("wek"));
                    newrow.put("name",jsonData.getString("name"));
                    newrow.put("phone",jsonData.getString("phone"));
                    newrow.put("address",jsonData.getString("address"));
                    newrow.put("service",jsonData.getString("service"));
                    newrow.put("date",jsonData.getString("date"));
                    newrow.put("year",jsonData.getString("year"));
                    newrow.put("month",jsonData.getString("month"));
                    newrow.put("day",jsonData.getString("day"));
                    newrow.put("blood_pressure_big",jsonData.getString("blood_pressure_big"));
                    newrow.put("blood_pressure_small",jsonData.getString("blood_pressure_small"));
                    newrow.put("blood_sugar_before",jsonData.getString("blood_sugar_before"));
                    newrow.put("blood_sugar_after",jsonData.getString("blood_sugar_after"));
                    newrow.put("weight",jsonData.getString("weight"));
                    newrow.put("height",jsonData.getString("height"));
                    db4.insert("record",null,newrow);
                    db4.close();
                    //   //.makeText(getApplicationContext(), "name:"+name, //.LENGTH_LONG).show();;
                }





                String     msg="ok";
                //     //.makeText(getApplicationContext(), msg, //.LENGTH_LONG).show();;
            } catch (IOException | JSONException e) {

                String     msg="not ok";
                //.makeText(getApplicationContext(), msg, //.LENGTH_LONG).show();;
            }

        }
    }


    private  Button.OnClickListener on_calender= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            calender_id=v.getId();
            schedule_id=(String)v.getTag();
            //    //.makeText(getApplicationContext(), schedule_id, //.LENGTH_LONG).show();
            allid=schedule_id.split(",");
            //   String tmp="schedule";
            String  schedule_id=(String) v.getTag();
            if (schedule_id=="-1"){
                show_insert();
            }
            else if(allid[1].equals("schedule")){
                show_select();

            }else{
                show_record();
            }
        }
    };


    ///行事曆排程設定
    private  Button.OnClickListener on_insert= new Button.OnClickListener() {


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {

            switch (v.getId()) {



                case R.id.g01_b002:


                    String week="";
                    if (c000.isChecked())
                        week += ",0";
                    if (c001.isChecked())
                        week += ",1";
                    if (c002.isChecked())
                        week  += ",2";
                    if (c003.isChecked())
                        week += ",3";
                    if (c004.isChecked())
                        week += ",4";
                    if (c005.isChecked())
                        week += ",5";
                    if (c006.isChecked())
                        week += ",6";
                    week=week.substring(1);
                    TextView t000 = (TextView) schedule_insert.findViewById(R.id.g01_t000);
                    TextView t005 = (TextView) schedule_insert.findViewById(R.id.g01_t005);

                    String  tmp=(t005.getText()).toString();

                    TextView t007 = (TextView) schedule_insert.findViewById(R.id.g01_t007);
                    CharSequence tmp3=t007.getText();

                    TextView t009 = (TextView) schedule_insert.findViewById(R.id.g01_t009);
                    CharSequence tmp5=t009.getText().subSequence(0, 2);
                    String   calender_id = (t000.getText()).toString();
                    String   name= ((SpinnerItem)s001.getSelectedItem()).getValue();
                    String   name_id= ((SpinnerItem)s001.getSelectedItem()).getID();
                    String   service = (String) s002.getSelectedItem();
                    String   time_start = (t009.getText()).toString();
                    time_start  =time_start .substring(0, 2);
                    time_start=Integer.toString(Integer.parseInt(time_start));
                    String   time_end = (String) s003.getSelectedItem();
                    time_end  =time_end .substring(0, 2);
                    time_end=Integer.toString(Integer.parseInt(time_end));
                    String hour=Integer.toString(Integer.parseInt(time_end)-Integer.parseInt(time_start)+1);
                    String date1= (t005.getText()).toString();
                    String date_start=date1.substring(0,4) +"-"+date1.substring(5,7) +"-"+ date1.substring(8,10);
                    String date2= (t007.getText()).toString();
                    String date_end=date2.substring(0,4) +"-"+date2.substring(5,7) +"-"+ date2.substring(8,10);

                    SQLiteDatabase db11 = dbhelper.getWritableDatabase();

                    String[]  tmp_id=name_id.split(",");
                    String[] MYCOLUMN = new String[]{"_id","phone","address"};
                    Cursor rs =db11.query("case_info", MYCOLUMN," _id=?", tmp_id, null, null, null);
                    rs.moveToFirst();
                                   String phone=rs.getString(1);
                             String address=rs.getString(2);
                   rs.close();
                   db11.close();
//                    String phone=member_case[Integer.parseInt(name_id)][2];
//                    String address=member_case[Integer.parseInt(name_id)][3];

                    //  //.makeText(getApplicationContext(), name_id, //.LENGTH_LONG).show();

                    //     List<String> aListData = new ArrayList<NameValuePair>();
                    String member_id = getSharedPreferences("record", MODE_PRIVATE).getString("record", "");
                    //.makeText(getApplicationContext(), member_id, //.LENGTH_LONG).show();;
                    ContentValues newrow=new ContentValues();
                    newrow.put("member_id",member_id);
                    newrow.put("case_id","1");
                    newrow.put("calender_id",calender_id);
                    newrow.put("date_start",date_start);
                    newrow.put("date_end",date_end);
                    newrow.put("time_start",time_start);
                    newrow.put("time_end",time_end);
                    newrow.put("hour",hour);
                    newrow.put("week",week);
                    newrow.put("name",name);
                    newrow.put("phone",phone);
                    newrow.put("address",address);
                    newrow.put("service",service);

                    SQLiteDatabase db = dbhelper.getWritableDatabase();
                    db.insert("schedule",null,newrow);//
                    db.close();

                    //  mContRes.insert(FriendsContentProvider.CONTENT_URI_schedule, newrow);

                    FormBody body = new FormBody.Builder()
                            .add("selefunc_string", "insert")
                            .add("case_id","1")
                            .add("member_id",member_id)
                            .add("calender_id",calender_id)
                            .add("date_start",date_start)
                            .add("date_end",date_end)
                            .add("time_start",time_start)
                            .add("time_end",time_end)
                            .add("hour",hour)
                            .add("week",week)
                            .add("name",name)
                            .add("phone",phone)
                            .add("address",address)
                            .add("service",service)
                            .build();
//--------------
                    Request request = new Request.Builder()
                            .url("https://medicalcarehelper.com/tcnr19/android_schedule_db_all.php")
                            .post(body)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        String ss= response.body().string();
                      String     msg="ok";
                        //.makeText(getApplicationContext(), msg, //.LENGTH_LONG).show();;
                    } catch (IOException e) {
                        e.printStackTrace();
                        String     msg="not ok";
                        //.makeText(getApplicationContext(), msg, //.LENGTH_LONG).show();;
                    }


//                   final  List<NameValuePair> params = new ArrayList<NameValuePair>();
//
//
//                    params.add(new BasicNameValuePair("case_id", "1"));
//                    params.add(new BasicNameValuePair("calender_id", calender_id));
//                    params.add(new BasicNameValuePair("date_start", date_start));
//                    params.add(new BasicNameValuePair("date_end", date_end));
//                    params.add(new BasicNameValuePair("time_start", time_start));
//                    params.add(new BasicNameValuePair("time_end", time_end));
//                    params.add(new BasicNameValuePair("hour", hour));
//                    params.add(new BasicNameValuePair("week", week));
//                    params.add(new BasicNameValuePair("name", name));
//                    params.add(new BasicNameValuePair("phone", phone));
//                    params.add(new BasicNameValuePair("address", address));
//                    params.add(new BasicNameValuePair("service", service));
//                    final HttpClient httpclient = new DefaultHttpClient();
//                    final HttpPost httppost = new HttpPost("https://medicalcarehelper.com/tcnr19/android_schedule_db_all.php");
//                    params.add(new BasicNameValuePair("selefunc_string", "insert"));
////
////
//                    new Thread(new Runnable( ){
//                        public void run(){
//
//                            try {
//                                httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//                                HttpResponse response = httpclient.execute(httppost);  //執行
//                                HttpEntity entity = response.getEntity();
//                                //     MSG="OK";
//                            } catch (Exception e) {
//                                //   MSG="NOT  OK";
//                            }
//                        }
//                    }).start();



                    schedule_insert.cancel();
                    setupviewconponent();
                    break;

                case R.id.g01_b003:
                    schedule_insert.cancel();
                    break;
            }
        }
    };



    private  Button.OnClickListener on_select= new Button.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.g01_b001:
                    schedule_select.cancel();
                    show_update();
                    break;
                case R.id.g01_b002:

                    TextView t000 = (TextView) schedule_select.findViewById(R.id.g01_t000);
                    //  String   calender_id1 = (t000.getText()).toString();
                    String[] tmp_id=  Integer.toString(calender_id).split(";");;
                    SQLiteDatabase db = dbhelper.getWritableDatabase();
                    db.delete("schedule","calender_id=?",tmp_id);
                    db.close();


//                    mContRes=getContentResolver();
//                    int cc=mContRes.delete(FriendsContentProvider.CONTENT_URI_schedule,"id=?",allid);


                    FormBody body = new FormBody.Builder()
                            .add("selefunc_string", "delete")
                            .add("calender_id",tmp_id[0])
                            .build();
//--------------
                    Request request = new Request.Builder()
                            .url("https://medicalcarehelper.com/tcnr19/android_schedule_db_all.php")
                            .post(body)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        String ss= response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


//                    List<NameValuePair> params = new ArrayList<NameValuePair>();
//                    try {
//                        HttpClient httpclient = new DefaultHttpClient();
//                        HttpPost httppost = new HttpPost("https://medicalcarehelper.com/tcnr19/android_schedule_db_all.php");
//                        params.add(new BasicNameValuePair("selefunc_string", "delete"));
//                      //  params.add(new BasicNameValuePair("calender_id", allid[0]));
//                       params.add(new BasicNameValuePair("calender_id", calender_id1));
//                        httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//                        HttpResponse response = httpclient.execute(httppost);  //執行
//                        HttpEntity entity = response.getEntity();
//
//                    } catch (Exception e) {
//
//                    }


                    schedule_select.cancel();
                    setupviewconponent();
                    break;
                case R.id.g01_b003:
                    TextView t006 = (TextView) schedule_select.findViewById(R.id.g01_t006);
                    String   phone = (t006.getText()).toString().substring(3);

                    uri = Uri.parse("tel:"+phone);
                    Intent it = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(it);
                    break;
                case R.id.g01_b004:
                    TextView t007 = (TextView) schedule_select.findViewById(R.id.g01_t007);
                    String   addressName = (t007.getText()).toString().substring(3);
                    Uri uri = Uri.parse("http://maps.google.com/maps?f=d&daddr="+addressName+"&hl=en");

                    Intent it1 = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it1);






                    break;
                case R.id.g01_b005:
                    String[]   tmpid=Integer.toString(calender_id).split(",");

                    SQLiteDatabase db1 = dbhelper.getWritableDatabase();
                    String[] MYCOLUMN = new String[]{"_id","case_id","date_start","date_end","time_start","time_end","week","name","phone","address","service","hour"};
                    Cursor rs =db1.query("schedule", MYCOLUMN, "calender_id=?", tmpid, null, null, null);
                    rs.moveToFirst();

//                    mContRes = getContentResolver();
//                    String[] MYCOLUMN = new String[]{"_id","case_id","date_start","date_end","time_start","time_end","week","name","phone","address","service","hour"};
//                    Cursor rs =mContRes.query(FriendsContentProvider.CONTENT_URI_schedule,MYCOLUMN , "calender_id=?", tmpid, null);




                    String todate=Integer.toString(calender_id).substring(0,4)+"-"+Integer.toString(calender_id).substring(4,6)+"-"+Integer.toString(calender_id).substring(6,8);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();

                    try {
                        c.setTime(format.parse(todate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String wek=Integer.toString(c.get(Calendar.DAY_OF_WEEK));


                    //     List<String> aListData = new ArrayList<String>();
                    String member_id = getSharedPreferences("record", MODE_PRIVATE).getString("record", "");
                    ContentValues newrow=new ContentValues();
                    newrow.put("member_id",member_id);
                    newrow.put("calender_id",calender_id);
                    newrow.put("schedule_id",schedule_id);
                    newrow.put("date",todate);
                    newrow.put("year",Integer.toString(calender_id).substring(0,4));
                    newrow.put("month",Integer.toString(calender_id).substring(4,6));
                    newrow.put("day",Integer.toString(calender_id).substring(6,8));
                    newrow.put("date_start",rs.getString(2));
                    newrow.put("date_end",rs.getString(3));
                    newrow.put("time_start",rs.getString(4));
                    newrow.put("time_end",rs.getString(5));
                    newrow.put("hour",rs.getString(11));
                    newrow.put("week",rs.getString(6));
                    newrow.put("wek",wek);
                    newrow.put("name",rs.getString(7));
                    newrow.put("phone",rs.getString(8));
                    newrow.put("address",rs.getString(9));
                    newrow.put("service",rs.getString(10));




                    SQLiteDatabase db2 = dbhelper.getWritableDatabase();
                    db2.insert("record",null,newrow);//
                    db2.close();



//                    mContRes = getContentResolver();
//                    mContRes.insert(FriendsContentProvider.CONTENT_URI_record, newrow);

                    FormBody body1 = new FormBody.Builder()
                            .add("selefunc_string", "insert")
                            .add("calender_id", Integer.toString(calender_id))
                            .add("member_id", member_id)
                            .add("schedule_id", schedule_id)
                            .add("date", todate)
                            .add("year", Integer.toString(calender_id).substring(0,4))
                            .add("month", Integer.toString(calender_id).substring(4,6))
                            .add("day", Integer.toString(calender_id).substring(6,8))
                            .add("date_start", rs.getString(2))
                            .add("date_end", rs.getString(3))
                            .add("time_start", rs.getString(4))
                            .add("time_end", rs.getString(5))
                            .add("hour", rs.getString(11))
                            .add("week", rs.getString(6))
                            .add("wek", wek)
                            .add("name", rs.getString(7))
                            .add("phone", rs.getString(8))
                            .add("address", rs.getString(9))
                            .add("service", rs.getString(10))
                            .build();
//--------------
                    Request request1 = new Request.Builder()
                            .url("https://medicalcarehelper.com/tcnr19/android_record_db_all.php")
                            .post(body1)
                            .build();

                    try (Response response = client.newCall(request1).execute()) {
                        String ss= response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    List<NameValuePair> param = new ArrayList<NameValuePair>();
//
//
//                    param.add(new BasicNameValuePair("calender_id", Integer.toString(calender_id)));
//                    param.add(new BasicNameValuePair("schedule_id", schedule_id));
//                    param.add(new BasicNameValuePair("date", todate));
//                    param.add(new BasicNameValuePair("year", Integer.toString(calender_id).substring(0,4)));
//                    param.add(new BasicNameValuePair("month", Integer.toString(calender_id).substring(4,6)));
//                    param.add(new BasicNameValuePair("day", Integer.toString(calender_id).substring(6,8)));
//                    param.add(new BasicNameValuePair("date_start", rs.getString(2)));
//                    param.add(new BasicNameValuePair("date_end", rs.getString(3)));
//                    param.add(new BasicNameValuePair("time_start", rs.getString(4)));
//                    param.add(new BasicNameValuePair("time_end", rs.getString(5)));
//                    param.add(new BasicNameValuePair("hour", rs.getString(11)));
//                    param.add(new BasicNameValuePair("week", rs.getString(6)));
//                    param.add(new BasicNameValuePair("wek", wek));
//                    param.add(new BasicNameValuePair("name", rs.getString(7)));
//                    param.add(new BasicNameValuePair("phone", rs.getString(8)));
//                    param.add(new BasicNameValuePair("address", rs.getString(9)));
//                    param.add(new BasicNameValuePair("service", rs.getString(10)));
//
//                    try {
//                        HttpClient httpclient = new DefaultHttpClient();
//                        HttpPost httppost = new HttpPost("https://medicalcarehelper.com/tcnr19/android_record_db_all.php");
//                        param.add(new BasicNameValuePair("selefunc_string", "insert"));
//                        httppost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
//                        HttpResponse response = httpclient.execute(httppost);  //執行
//                        HttpEntity entity = response.getEntity();
//                         //    MSG="OK";
//                        //     is = entity.getContent();
//                         //.makeText(getApplicationContext(), "pass", //.LENGTH_LONG).show();
//                        //   Log.d(TAG, "pass 1:"+"connection success ");
//                    } catch (Exception e) {
//                      //    MSG="NOT  OK";
//                        //.makeText(getApplicationContext(), "fall", //.LENGTH_LONG).show();
//                        //     Log.d(TAG, "Fail 1"+e.toString());
//                    }


                    db1.close();
                    schedule_select.cancel();
//                    SQLiteDatabase db2 = dbhelper.getWritableDatabase();
//                    db2.insert("record", null, newrow);
//                    db2.close();

                    schedule_select.cancel();

                    setupviewconponent();
                    rs.close();
                    break;
                case R.id.g01_b006:
                    schedule_select.cancel();
                    break;

            }      }
    };

    private  Button.OnClickListener on_record= new Button.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {

            TextView t000 = (TextView) schedule_record.findViewById(R.id.g01_t000);
            String   calender_id1 = (t000.getText()).toString();
            switch (v.getId()){


                case R.id.g01_b001:

                    //   TextView t000 = (TextView) schedule_select.findViewById(R.id.g01_t000);
                    String   calender_id2 = (t000.getText()).toString();
                    //   String[] tmp_id=  allid[0].split(";");
                    String[] tmp_id=  calender_id2.split(";");;
                    SQLiteDatabase db = dbhelper.getWritableDatabase();
                    db.delete("record","calender_id=?",tmp_id);
                    // db.delete("record","_id=?",tmp_id);
                    //   db.delete()
                    db.close();
                    //.makeText(getApplicationContext(), tmp_id[0],//.LENGTH_LONG).show();

//                    mContRes=getContentResolver();
//                    int cc=mContRes.delete(FriendsContentProvider.CONTENT_URI_schedule,"id=?",allid);


                    FormBody body = new FormBody.Builder()
                            .add("selefunc_string", "delete")
                            .add("calender_id",tmp_id[0])
                            .build();
//--------------
                    Request request = new Request.Builder()
                            .url("https://medicalcarehelper.com/tcnr19/android_record_db_all.php")
                            .post(body)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        String ss= response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//
//                    mContRes=getContentResolver();
//                    int cc=mContRes.delete(FriendsContentProvider.CONTENT_URI_record,"id=?",allid);
//                    List<NameValuePair> params = new ArrayList<NameValuePair>();
//                    try {
//                        HttpClient httpclient = new DefaultHttpClient();
//                        HttpPost httppost = new HttpPost("https://medicalcarehelper.com/tcnr19/android_record_db_all.php");
//                        params.add(new BasicNameValuePair("selefunc_string", "delete"));
//                        params.add(new BasicNameValuePair("calender_id", calender_id1));
//                        //   params.add(new BasicNameValuePair("calender_id", allid[0]));
//                        httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//                        HttpResponse response = httpclient.execute(httppost);  //執行
//                        HttpEntity entity = response.getEntity();
//
//                    } catch (Exception e) {
//
//                    }
                    schedule_record.cancel();
                    setupviewconponent();
                    break;
                case R.id.g01_b002:
                    //   uri = Uri.parse("tel:"+schedule_list.get(schedule_Id).get(8));
                    Intent it = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(it);
                    break;
                case R.id.g01_b003:

                    TextView t0000 = (TextView) schedule_record.findViewById(R.id.g01_t000);
                    String   calender_id3 = (t0000.getText()).toString();
                    EditText e001=schedule_record.findViewById(R.id.g01_e001);
                    EditText e002=schedule_record.findViewById(R.id.g01_e002);
                    EditText e003=schedule_record.findViewById(R.id.g01_e003);
                    EditText e004=schedule_record.findViewById(R.id.g01_e004);
                    EditText e005=schedule_record.findViewById(R.id.g01_e005);
                    EditText e006=schedule_record.findViewById(R.id.g01_e006);


                    //    //.makeText(getApplicationContext(), calender_id2, //.LENGTH_LONG).show();
                    ContentValues newrow=new ContentValues();
                    newrow.put("blood_pressure_big",e001.getText().toString());
                    newrow.put("blood_pressure_small",e002.getText().toString());
                    newrow.put("blood_sugar_before",e003.getText().toString());
                    newrow.put("blood_sugar_after",e004.getText().toString());
                    newrow.put("weight",e005.getText().toString());
                    newrow.put("height",e006.getText().toString());
                    String[] tmpid=calender_id3.split(",");







                    SQLiteDatabase db2 = dbhelper.getWritableDatabase();
                    db2.update("record",newrow,"calender_id=?",tmpid);//
                    db2.close();

//                    mContRes = getContentResolver();
//                    int dd= mContRes.update(FriendsContentProvider.CONTENT_URI_record, newrow,"calender_id=?",tmpid);



                    FormBody body1 = new FormBody.Builder()
                            .add("selefunc_string", "update")
                            .add("calender_id",calender_id3)
                            .add("blood_pressure_big",e001.getText().toString())
                            .add("blood_pressure_small",e002.getText().toString())
                            .add("blood_sugar_before",e003.getText().toString())
                            .add("blood_sugar_after",e004.getText().toString())
                            .add("weight",e005.getText().toString())
                            .add("height",e006.getText().toString())
                            .build();
//--------------
                    Request request1 = new Request.Builder()
                            .url("https://medicalcarehelper.com/tcnr19/android_record_db_all.php")
                            .post(body1)
                            .build();

                    try (Response response = client.newCall(request1).execute()) {
                        String ss= response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


//
//                    List<NameValuePair> param = new ArrayList<NameValuePair>();
//
//
//                    param.add(new BasicNameValuePair("blood_pressure_big",e001.getText().toString()));
//                    param.add(new BasicNameValuePair("blood_pressure_small",e002.getText().toString()));
//                    param.add(new BasicNameValuePair("blood_sugar_before",e003.getText().toString()));
//                    param.add(new BasicNameValuePair("blood_sugar_after",e004.getText().toString()));
//                    param.add(new BasicNameValuePair("weight",e005.getText().toString()));
//                    param.add(new BasicNameValuePair("height",e006.getText().toString()));
//
////                    //int dd=mContRes.update(FriendsContentProvider.CONTENT_URI_record,"id=?",allid);
////
//                    try {
//                        HttpClient httpclient = new DefaultHttpClient();
//                       HttpPost httppost = new HttpPost("https://medicalcarehelper.com/tcnr19/android_record_db_all.php");
//                      param.add(new BasicNameValuePair("selefunc_string", "update"));
//                      param.add(new BasicNameValuePair("calender_id", calender_id2));
//                        //   params.add(new BasicNameValuePair("calender_id", allid[0]));
//                        httppost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
//                        HttpResponse response = httpclient.execute(httppost);  //執行
//                        HttpEntity entity = response.getEntity();
//
//                    } catch (Exception e) {
//
//                    }
                    schedule_record.cancel();
                    setupviewconponent();
                    break;
                case R.id.g01_b004:
                    schedule_record.cancel();
                    break;

            }      }
    };

    private  Button.OnClickListener on_button= new Button.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.g01_b001:
                    calendar.add(Calendar.MONTH, -1);
                    break;
                case R.id.g01_b002:
                    calendar.add(Calendar.MONTH, +1);
                    break;
            }
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) +1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            t001.setText(Integer.toString(year)+getString(R.string.g01_t001));
            t002.setText(Integer.toString(month)+getString(R.string.g01_t002));

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            wek=calendar.get(DAY_OF_WEEK);
            calendar.set(Calendar.DATE, 1);
            calendar.roll(Calendar.DATE, -1);
            monthday = calendar.get(Calendar.DATE);

            //   String[][][] arr = new String[22][monthday+1][10];
            //-----------------------------------------
            setupviewconponent();

        }
    };
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupviewconponent( ) {


        String[][][] arr = new String[22][32][10];
        int gap;


//        mContRes = getContentResolver();
//        Cursor rs =mContRes.query(FriendsContentProvider.CONTENT_URI_schedule, new String[] {"_id","case_id","date_start","date_end","time_start","time_end","week","name","phone","address","service"}, null, null, null);
//        rs.moveToFirst();

        SQLiteDatabase db1 = dbhelper.getWritableDatabase();


        String[] MYCOLUMN = new String[]{"_id","case_id","date_start","date_end","time_start","time_end","week","name","phone","address","service"};
        Cursor rs =db1.query("schedule", MYCOLUMN, null, null, null, null, null);
        rs.moveToFirst();



        while(!rs.isAfterLast()){
            //for(int i = 0;i<schedule_list.size();i++){

            ;
            String[]   allweek=rs.getString(6).split(",");//循環取出list中的所有用戶名的值
            int time_start = Integer.parseInt(rs.getString(4));
            int time_end = Integer.parseInt(rs.getString(5));



            for (int m = 0; m < allweek.length; m = m + 1) {
                int week= Integer.parseInt(allweek[m]) ;
                if (week > wek) {
                    gap = week - wek+2;
                } else if (week < wek) {
                    gap = week - wek + 7+2;
                } else {
                    gap = 0+2;
                }

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String date_start=rs.getString(2);
                String date_end=rs.getString(3);



                for (int day = gap; day < monthday; day = day + 7) {

                    String date_now=String.format("%04d", year) +"-"+String.format("%02d", month) +"-"+ String.format("%02d", day );
                    try {
                        Date date_start1 = df.parse(date_start);
                        Date date_end1 = df.parse(date_end);
                        Date date_now1 = df.parse(date_now);
                        if ((date_start1.getTime() <= date_now1 .getTime()) &&(date_end1.getTime() >= date_now1 .getTime())) {
                            for (int time = time_start; time < time_end+1; time = time + 1) {

                                String tv_id1 = String.format("%04d", year) + String.format("%02d", month) + String.format("%02d", day ) + String.format("%02d", time);
                                arr[time][day][1] = tv_id1;
                                arr[time][day][2] = rs.getString(7);
                                if (time==time_start){
                                    arr[time][day][3] = "#FAACcC";
                                }else{
                                    arr[time][day][3] = "#FAACAC";
                                }

                                arr[time][day][4] = rs.getString(0)+",schedule";
                            }
                        }
                    } catch (Exception exception) {
                        //   exception.printStackTrace();
                    }

                }
            }
            rs.moveToNext();
        }

//                { "12", "阿六", "2020050120","5","1", "20","2" },
//                { "10", "阿六", "2020050414","5","4", "14","2" },
//                { "11", "阿六", "2020050418","5","4", "18","2" },
//                { "12", "阿六", "2020050420","5","4", "20","2" }
//rs.getString(8)




//        mContRes = getContentResolver();
//        String[] MYCOLUMN = new String[] {"_id","name","calender_id","month","day","time_start","hour","schedule_id"};
//
//        Cursor rs1 =mContRes.query(FriendsContentProvider.CONTENT_URI_record, MYCOLUMN, null, null, null);
//        rs1.moveToFirst();
////

        SQLiteDatabase db = dbhelper.getWritableDatabase();


        String[] MYCOLUMN1 = new String[] {"_id","name","calender_id","month","day","time_start","hour","schedule_id"};
        Cursor rs1 =db1.query("record", MYCOLUMN1, null, null, null, null, null);
        rs1.moveToFirst();

        while(!rs1.isAfterLast()) {
            Integer mon = Integer.parseInt(rs1.getString(3));
            Integer day = Integer.parseInt(rs1.getString(4));
            Integer time = Integer.parseInt(rs1.getString(5));//time_start
            Integer section = Integer.parseInt(rs1.getString(6));//hour
            //      //.makeText(getApplicationContext(), rs1.getString(0)+","+rs1.getString(1)+","+rs1.getString(2)+","+rs1.getString(3)+","+rs1.getString(4)+","+rs1.getString(5)+","+rs1.getString(6), //.LENGTH_LONG).show();
            if (mon.equals(month)) {

                for (int h=0 ;h<section;h++){
                    String calender_id=Integer.toString(Integer.parseInt(rs1.getString(2))+h);
                    arr[time+h][day][1] =calender_id;
                    arr[time+h][day][2] = rs1.getString(1);
                    arr[time+h][day][3] = "#E6F67C";
                    arr[time+h][day][4] = rs1.getString(0)+",record";
                }

            }
            rs1.moveToNext();
        }



        l002.removeAllViews();//清除所以行事
        int m;
        //自動產生日期和星期--------------------------------
        TableRow tr1 = new TableRow(this);
        for (int i = 1; i <= monthday; i++) // 產 生日期和星期
        {
            int ii = (i + wek - 2) % 7;//計算當月的1號是星期幾
            String mm = String.format("%02d", i) + "\n" + weekday[ii];

            tv = new TextView(this); // tv 繼承TextView
            tv.setLayoutParams(params3);
            tv.setText(mm);
            tv.setGravity(1);
            tv.setTextSize(12);
            tv.setWidth(100);
            if ((ii==0)||(ii==6)){
                tv.setBackgroundColor(Color.parseColor("#A5C8E8"));
            }else{
                tv.setBackgroundColor(Color.parseColor("#A5D6E8"));
            }


            tr1.addView(tv);
        }
        l002.addView(tr1);
//---------------------
        //自動產生行事--------------------------------
        for ( int time = 6; time <= 21; time++) {//跑出6點到21點.共16個TABLEROW
//
            TableRow tr = new TableRow(this);
            for (int i = 1; i <= monthday; i++) // 依當時的月份天數,跑出行事曆
            {
                int ii = (i + wek - 2) % 7;//
                tv = new TextView(this); // tv 繼承TextView
                int tv_id = Integer.parseInt(String.format("%04d", year) + String.format("%02d", month) + String.format("%02d", i) + String.format("%02d", time));
                tv.setId(tv_id);
                tv.setLayoutParams(params4);
                tv.setGravity(1);
                tv.setText(arr[time][i][2]);

                int strLen;
                if (arr[time][i][4] != null && (strLen = arr[time][i][3].length()) != 0){
                    //    tv.setTag(Integer.parseInt(arr[time][i][4]));
                    tv.setTag((arr[time][i][4]));
                }
                else{
                    tv.setTag("-1");
                }

                tv.setTextSize(8);
//                //如果是星期六和星期日.;則改變底色------------------------------------
                if ((ii == 0) || (ii == 6)) {
                    tv.setBackgroundColor(Color.parseColor("#AFFCC1"));
                } else {
                    tv.setBackgroundColor(Color.parseColor("#CAFCD6"));
                }
//
                int strLen1;
                if (arr[time][i][3] != null && (strLen1 = arr[time][i][3].length()) != 0){
                    tv.setBackgroundColor(Color.parseColor(arr[time][i][3]));
                }
                //---------------------------------------------------------------
                tr.addView(tv);
                tv.setOnClickListener(on_calender);
            }
            l002.addView(tr);
        }
        final_arr=arr;
        //   View v=findViewById(2020080109);
        rs.close();
        rs1.close();
    }

    private void show_insert(){
        schedule_insert= new Dialog(Schedule.this);

        schedule_insert.setCancelable(false);
        schedule_insert.setContentView(R.layout.schedule_insert);
        schedule_insert.show();
        TextView t000 = (TextView) schedule_insert.findViewById(R.id.g01_t000);
        TextView t001 = (TextView) schedule_insert.findViewById(R.id.g01_t001);
        TextView t002 = (TextView) schedule_insert.findViewById(R.id.g01_t002);
        TextView t003 = (TextView) schedule_insert.findViewById(R.id.g01_t003);
        TextView t004 = (TextView) schedule_insert.findViewById(R.id.g01_t004);
        TextView t005 = (TextView) schedule_insert.findViewById(R.id.g01_t005);
        TextView t006 = (TextView) schedule_insert.findViewById(R.id.g01_t006);
        TextView t007 = (TextView) schedule_insert.findViewById(R.id.g01_t007);
        TextView t008 = (TextView) schedule_insert.findViewById(R.id.g01_t008);
        TextView t009 = (TextView) schedule_insert.findViewById(R.id.g01_t009);

        Button b002 = (Button) schedule_insert.findViewById(R.id.g01_b002);
        Button b003 = (Button) schedule_insert.findViewById(R.id.g01_b003);
        c000 = (CheckBox) schedule_insert.findViewById(R.id.c000);
        c001 = (CheckBox) schedule_insert.findViewById(R.id.c001);
        c002 = (CheckBox) schedule_insert.findViewById(R.id.c002);
        c003 = (CheckBox) schedule_insert.findViewById(R.id.c003);
        c004 = (CheckBox) schedule_insert.findViewById(R.id.c004);
        c005 = (CheckBox) schedule_insert.findViewById(R.id.c005);
        c006 = (CheckBox) schedule_insert.findViewById(R.id.c006);



        b002.setOnClickListener(on_insert);
        b003.setOnClickListener(on_insert);
        t000.setText(Integer.toString(calender_id));
        t001.setText("工作排程小幫手");
        //   t003.setText(getString(R.string.g01_t003)+Integer.toString(calender_id));
        // 初始化控制元件
        nowyear= Integer.toString(calender_id).substring(0,4);
        nowmonth=Integer.toString(calender_id).substring(4,6);
        nowday = Integer.toString(calender_id).substring(6,8);
        nowtime = Integer.toString(calender_id).substring(8,10);;



        nowweek =Integer.toString(calendar.get(DAY_OF_WEEK));


        t005.setText(nowyear+"年"+nowmonth+"月"+nowday+"日");
        t007.setText(nowyear+"年"+nowmonth+"月"+nowday+"日");
        t009.setText(nowtime+"點");



//        SQLiteDatabase db1 = dbhelper_friends.getWritableDatabase();
//
//
//        String[] MYCOLUMN = new String[]{"id","t001","t001","t001"};
//        Cursor rs =db1.query("case_info", MYCOLUMN, null, null, null, null, null);
//        rs.moveToFirst();
//
      SQLiteDatabase db1 = dbhelper.getWritableDatabase();
////
////
        String[] MYCOLUMN = new String[]{"_id","name"};
        Cursor rs10 =db1.query("case_info", MYCOLUMN, null, null, null, null, null);
        rs10.moveToFirst();

//
        List<SpinnerItem> list = new ArrayList<SpinnerItem>();
        while(!rs10.isAfterLast()){
            //for(int i = 0;i<schedule_list.size();i++){
            list.add(new SpinnerItem(rs10.getString(0), rs10.getString(1)));
            rs10.moveToNext();
        }

        rs10.close();

        //List<SpinnerItem> list = new ArrayList<SpinnerItem>();
//        while(!rs.isAfterLast()) {
//            //for(int i = 0;i<schedule_list.size();i++){
//            list.add(new SpinnerItem(rs.getString(0), rs.getString(1)));
//            rs.moveToNext();
//
//        }







//        list.add(new SpinnerItem("0", "小一"));
//        list.add(new SpinnerItem("1", "陳二"));
//        list.add(new SpinnerItem("2", "張三"));
//        list.add(new SpinnerItem("3", "李四"));
//        list.add(new SpinnerItem("4", "王五"));
//        list.add(new SpinnerItem("5", "阿六"));
//        list.add(new SpinnerItem("6", "黃七"));
//        list.add(new SpinnerItem("7", "周八"));
//        list.add(new SpinnerItem("8", "潘九"));
        ArrayAdapter<SpinnerItem> adapter= new ArrayAdapter<SpinnerItem>(getApplicationContext(),  android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s001 = (Spinner) schedule_insert.findViewById(R.id.s001);
        s001.setAdapter(adapter);



        String[] mItems2 = {"健康檢查","洗澡服務","吃飯","逛街"};
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, mItems2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s002 = (Spinner) schedule_insert.findViewById(R.id.s002);
        s002.setAdapter(adapter2);



        ArrayList<String>mItems3=new ArrayList<String>();


        int iday=Integer.parseInt(nowday);
        int itime=Integer.parseInt(nowtime);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Integer.parseInt(nowyear),Integer.parseInt(nowmonth)-1,Integer.parseInt(nowday));
        int iweek=calendar.get(DAY_OF_WEEK)-1;
        //.makeText(getApplicationContext(), Integer.toString(iweek), //.LENGTH_LONG).show();
        switch(iweek){

            case 0:
                c000.setChecked(true);
                c000.setEnabled(false);
                break;
            case 1:
                c001.setChecked(true);
                c001.setEnabled(false);
                break;
            case 2:
                c002.setChecked(true);
                c002.setEnabled(false);
                break;
            case 3:
                c003.setChecked(true);
                c003.setEnabled(false);
                break;
            case 4:
                c004.setChecked(true);
                c004.setEnabled(false);
                break;
            case 5:
                c005.setChecked(true);
                c005.setEnabled(false);
                break;
            case 6:
                c006.setChecked(true);
                c006.setEnabled(false);
                break;
        }


        for(int i=itime;i<=21; i++){
            if (final_arr[i][iday][2]== null || (final_arr[i][iday][2].length()) == 0) {
                mItems3.add(String.format("%02d",i) + "點");

            }
            else{
                break;
            }
        }

        ArrayAdapter adapter3=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,mItems3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s003 = (Spinner) schedule_insert.findViewById(R.id.s003);
        s003.setAdapter(adapter3);





    }


    private void show_update(){
        schedule_update = new Dialog(Schedule.this);

        schedule_update.setCancelable(false);
        schedule_update.setContentView(R.layout.schedule_update);
        schedule_update.show();
        TextView t000 = (TextView) schedule_update.findViewById(R.id.g01_t000);
        TextView t001 = (TextView) schedule_update.findViewById(R.id.g01_t001);
        TextView t002 = (TextView) schedule_update.findViewById(R.id.g01_t002);
        TextView t003 = (TextView) schedule_update.findViewById(R.id.g01_t003);
        TextView t004 = (TextView) schedule_update.findViewById(R.id.g01_t004);
        TextView t005 = (TextView) schedule_update.findViewById(R.id.g01_t005);
        TextView t006 = (TextView) schedule_update.findViewById(R.id.g01_t006);
        TextView t007 = (TextView) schedule_update.findViewById(R.id.g01_t007);
        TextView t008 = (TextView) schedule_update.findViewById(R.id.g01_t008);
        TextView t009 = (TextView) schedule_update.findViewById(R.id.g01_t009);

        Button b002 = (Button) schedule_update.findViewById(R.id.g01_b002);
        Button b003 = (Button) schedule_update.findViewById(R.id.g01_b003);
        c000 = (CheckBox) schedule_update.findViewById(R.id.c000);
        c001 = (CheckBox) schedule_update.findViewById(R.id.c001);
        c002 = (CheckBox) schedule_update.findViewById(R.id.c002);
        c003 = (CheckBox) schedule_update.findViewById(R.id.c003);
        c004 = (CheckBox) schedule_update.findViewById(R.id.c004);
        c005 = (CheckBox) schedule_update.findViewById(R.id.c005);
        c006 = (CheckBox) schedule_update.findViewById(R.id.c006);



        b002.setOnClickListener(on_insert);
        b003.setOnClickListener(on_insert);
        t000.setText(Integer.toString(calender_id));
        t001.setText("工作排程修改");
        //   t003.setText(getString(R.string.g01_t003)+Integer.toString(calender_id));
        // 初始化控制元件

//        mContRes = getContentResolver();
//        String[] MYCOLUMN = new String[]{"_id","case_id","date_start","date_end","time_start","time_end","week","name","phone","address","service","hour"};
//        Cursor rs =mContRes.query(FriendsContentProvider.CONTENT_URI_schedule,MYCOLUMN , null, null, null);
//
//
//        rs.moveToFirst();


        SQLiteDatabase db1 = dbhelper.getWritableDatabase();
        String[] tmpid=Integer.toString(calender_id).split(",");

        String[] MYCOLUMN = new String[]{"_id","case_id","date_start","date_end","time_start","time_end","week","name","phone","address","service","hour"};
        Cursor rs =db1.query("schedule", MYCOLUMN, "calender_id=?", tmpid, null, null, null);
        rs.moveToFirst();


        String todate=Integer.toString(calender_id).substring(0,4)+"-"+Integer.toString(calender_id).substring(4,6)+"-"+Integer.toString(calender_id).substring(6,8);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();








        nowyear= Integer.toString(calender_id).substring(0,4);
        nowmonth=Integer.toString(calender_id).substring(4,6);
        nowday = Integer.toString(calender_id).substring(6,8);
        nowtime = Integer.toString(calender_id).substring(8,10);;



        nowweek =Integer.toString(calendar.get(DAY_OF_WEEK));


        t005.setText(nowyear+"年"+nowmonth+"月"+nowday+"日");
        t007.setText(nowyear+"年"+nowmonth+"月"+nowday+"日");
        t009.setText(nowtime+"點");







        List<SpinnerItem> list = new ArrayList<SpinnerItem>();

        list.add(new SpinnerItem("0", "小一"));
        list.add(new SpinnerItem("1", "陳二"));
        list.add(new SpinnerItem("2", "張三"));
        list.add(new SpinnerItem("3", "李四"));
        list.add(new SpinnerItem("4", "王五"));
        list.add(new SpinnerItem("5", "阿六"));
        list.add(new SpinnerItem("6", "黃七"));
        list.add(new SpinnerItem("7", "周八"));
        list.add(new SpinnerItem("8", "潘九"));
        ArrayAdapter<SpinnerItem> adapter= new ArrayAdapter<SpinnerItem>(getApplicationContext(),  android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s001 = (Spinner) schedule_update.findViewById(R.id.s001);
        s001.setAdapter(adapter);



        String[] mItems2 = {"健康檢查","洗澡服務","吃飯","逛街"};
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, mItems2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s002 = (Spinner) schedule_update.findViewById(R.id.s002);
        s002.setAdapter(adapter2);



        ArrayList<String>mItems3=new ArrayList<String>();


        int iday=Integer.parseInt(nowday);
        int itime=Integer.parseInt(nowtime);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Integer.parseInt(nowyear),Integer.parseInt(nowmonth)-1,Integer.parseInt(nowday));
        int iweek=calendar.get(DAY_OF_WEEK)-1;
        //.makeText(getApplicationContext(), Integer.toString(iweek), //.LENGTH_LONG).show();
        switch(iweek){

            case 0:
                c000.setChecked(true);
                c000.setEnabled(false);
                break;
            case 1:
                c001.setChecked(true);
                c001.setEnabled(false);
                break;
            case 2:
                c002.setChecked(true);
                c002.setEnabled(false);
                break;
            case 3:
                c003.setChecked(true);
                c003.setEnabled(false);
                break;
            case 4:
                c004.setChecked(true);
                c004.setEnabled(false);
                break;
            case 5:
                c005.setChecked(true);
                c005.setEnabled(false);
                break;
            case 6:
                c006.setChecked(true);
                c006.setEnabled(false);
                break;
        }


        for(int i=itime;i<=21; i++){
            if (final_arr[i][iday][2]== null || (final_arr[i][iday][2].length()) == 0) {
                mItems3.add(String.format("%02d",i) + "點");

            }
            else{
                break;
            }
        }

        ArrayAdapter adapter3=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,mItems3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s003 = (Spinner) schedule_update.findViewById(R.id.s003);
        s003.setAdapter(adapter3);



        rs.close();

    }

    private void show_select(){
        //   //.makeText(Schedule.this, "schedule_Id"+Integer.toString(schedule_Id), //.LENGTH_SHORT).show();
        schedule_select = new Dialog(Schedule.this);
        // mLoginDlg.setTitle(getString(R.string.app_name));
        schedule_select.setCancelable(false);
        schedule_select.setContentView(R.layout.schedule_select);
        schedule_select.show();
        TextView t000 = (TextView) schedule_select.findViewById(R.id.g01_t000);
        TextView t001 = (TextView) schedule_select.findViewById(R.id.g01_t001);
        TextView t002 = (TextView) schedule_select.findViewById(R.id.g01_t002);
        TextView t003 = (TextView) schedule_select.findViewById(R.id.g01_t003);
        TextView t0031 = (TextView) schedule_select.findViewById(R.id.g01_t0031);
        TextView t004 = (TextView)schedule_select.findViewById(R.id.g01_t004);
        TextView t005 = (TextView) schedule_select.findViewById(R.id.g01_t005);
        TextView t006 = (TextView) schedule_select.findViewById(R.id.g01_t006);
        TextView t007 = (TextView) schedule_select.findViewById(R.id.g01_t007);
        TextView t008 = (TextView) schedule_select.findViewById(R.id.g01_t008);
        TextView t009 = (TextView) schedule_select.findViewById(R.id.g01_t009);
        TextView t010 = (TextView) schedule_select.findViewById(R.id.g01_t010);
        TextView t011 = (TextView) schedule_select.findViewById(R.id.g01_t011);

        Button b001 = (Button) schedule_select.findViewById(R.id.g01_b001);
        Button b002 = (Button) schedule_select.findViewById(R.id.g01_b002);
        Button b003 = (Button) schedule_select.findViewById(R.id.g01_b003);
        Button b004 = (Button) schedule_select.findViewById(R.id.g01_b004);
        Button b005 = (Button) schedule_select.findViewById(R.id.g01_b005);
        Button b006 = (Button) schedule_select.findViewById(R.id.g01_b006);


        b001.setOnClickListener(on_select);
        b002.setOnClickListener(on_select);
        b003.setOnClickListener(on_select);
        b004.setOnClickListener(on_select);
        b005.setOnClickListener(on_select);
        b006.setOnClickListener(on_select);



        // allid=schedule_id.split(",");
//        SQLiteDatabase db = dbhelper.getWritableDatabase();
//        // { "0","1", "2020-05-01", "2020-09-01", "11","12","1,3,5", "張三","0972078902","台中市北屯區松竹路10號","洗澡服務" },
//        String sql="select _id,case_id,date_start,date_end,time_start,time_end,week,name,phone,address,service,hour from schedule where  _id="+allid[0];
//        Cursor rs = db.rawQuery(sql, null);//
//        mContRes = getContentResolver();
//        String[] MYCOLUMN = new String[]{"_id","case_id","date_start","date_end","time_start","time_end","week","name","phone","address","service","hour","calender_id"};
//        Cursor rs =mContRes.query(FriendsContentProvider.CONTENT_URI_schedule,MYCOLUMN , null, null, null);
//       rs.moveToFirst();

        SQLiteDatabase db1 = dbhelper.getWritableDatabase();
        String[] tmpid=Integer.toString(calender_id).split(",");
        String[] MYCOLUMN = new String[]{"_id","case_id","date_start","date_end","time_start","time_end","week","name","phone","address","service","hour","calender_id"};
        Cursor rs =db1.query("schedule", MYCOLUMN, "calender_id=?", tmpid, null, null, null);
        rs.moveToFirst();

        // rs.moveToFirst();
        //   //.makeText(getApplicationContext(), sql, //.LENGTH_LONG).show();
        t000.setText(rs.getString(12));
        t001.setText("排程起始日:"+rs.getString(2));
        t002.setText("排程截止日:"+rs.getString(3));
        t003.setText("服務時間:"+rs.getString(4)+"點~"+rs.getString(5)+"點");
        t0031.setText("時間長度:"+rs.getString(11)+"小時");
        String[]   allweek=rs.getString(6).split(",");//循環取出list中的所有用戶名的值
        String sweek="";
        for (int m = 0; m < allweek.length; m = m + 1) {
            if (m==0) {
                sweek="星期"+weekday[Integer.parseInt(allweek[m])];
            }else{
                sweek=sweek+",星期"+weekday[Integer.parseInt(allweek[m])];;
            }
        }
        t004.setText("每週排程:"+sweek);
        t005.setText("姓名:"+rs.getString(7));
        t006.setText("手機:"+rs.getString(8));
        t007.setText("地址:"+rs.getString(9));
        t008.setText("姓名:"+rs.getString(7));
        t009.setText("服務項目:"+rs.getString(10));
        String  today= Integer.toString(calender_id).substring(4,6)+"月"+Integer.toString(calender_id).substring(6,8)+"日"+rs.getString(4)+"時~"+rs.getString(5)+"時";
        t010.setText("服務時間:"+today);
        t011.setText("服務狀態:未完成");


        rs.close();

    }
    public void show_calender(View view){
        picker = new DatePickerDialog(Schedule.this,

                new DatePickerDialog.OnDateSetListener() {
                    TextView t007 = (TextView) schedule_insert.findViewById(R.id.g01_t007);

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        t007.setText(year+"年"+String.format("%02d", monthOfYear + 1)+"月"+String.format("%02d", dayOfMonth)+"日"  );

                    }

                }, Integer.parseInt(nowyear), Integer.parseInt(nowmonth)-1, Integer.parseInt(nowday));

//
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.parseInt(nowyear));
        calendar.set(Calendar.MONTH,Integer.parseInt(nowmonth)-1);
        calendar.set(Calendar.DATE,Integer.parseInt(nowday));


        picker.getDatePicker().setMinDate(calendar.getTimeInMillis());
        picker.show();
    }

    private void show_record(){
        //   //.makeText(Schedule.this, "schedule_Id"+Integer.toString(schedule_Id), //.LENGTH_SHORT).show();
        schedule_record = new Dialog(Schedule.this);
        // mLoginDlg.setTitle(getString(R.string.app_name));
        schedule_record.setCancelable(false);
        schedule_record.setContentView(R.layout.schedule_record);
        schedule_record.show();
        TextView t000 = (TextView) schedule_record.findViewById(R.id.g01_t000);
        TextView t001 = (TextView) schedule_record.findViewById(R.id.g01_t001);
        TextView t002 = (TextView) schedule_record.findViewById(R.id.g01_t002);
        TextView t003 = (TextView) schedule_record.findViewById(R.id.g01_t003);
        TextView t004 = (TextView)schedule_record.findViewById(R.id.g01_t004);
        TextView t005 = (TextView) schedule_record.findViewById(R.id.g01_t005);
        TextView t006 = (TextView) schedule_record.findViewById(R.id.g01_t006);
        TextView t007 = (TextView) schedule_record.findViewById(R.id.g01_t007);
        TextView t008 = (TextView) schedule_record.findViewById(R.id.g01_t008);
        TextView t009 = (TextView) schedule_record.findViewById(R.id.g01_t009);
        EditText e001 = (EditText) schedule_record.findViewById(R.id.g01_e001);
        EditText e002 = (EditText) schedule_record.findViewById(R.id.g01_e002);
        EditText e003 = (EditText) schedule_record.findViewById(R.id.g01_e003);
        EditText e004 = (EditText) schedule_record.findViewById(R.id.g01_e004);
        EditText e005 = (EditText) schedule_record.findViewById(R.id.g01_e005);
        EditText e006 = (EditText) schedule_record.findViewById(R.id.g01_e006);



        Button b001 = (Button) schedule_record.findViewById(R.id.g01_b001);
        Button b002 = (Button) schedule_record.findViewById(R.id.g01_b002);
        Button b003 = (Button) schedule_record.findViewById(R.id.g01_b003);
        Button b004 = (Button) schedule_record.findViewById(R.id.g01_b004);


        b001.setOnClickListener(on_record);
        b002.setOnClickListener(on_record);
        b003.setOnClickListener(on_record);
        b004.setOnClickListener(on_record);


        //    allid=schedule_id.split(",");
//        SQLiteDatabase db = dbhelper.getWritableDatabase();
//
//        String sql="select _id,case_id,date_start,date_end,time_start,time_end,week,name,phone,address,service,hour from record where  _id="+allid[0];
//        //  //.makeText(getApplicationContext(), sql, //.LENGTH_SHORT).show();
//        Cursor rs = db.rawQuery(sql, null);//

//        mContRes = getContentResolver();
//        String[] MYCOLUMN = new String[]{"_id","case_id","date_start","date_end","time_start","time_end","week","name","phone","address","service","hour","calender_id","blood_pressure_big","blood_pressure_small","blood_suger_before","blood_suger_after","weight","height"};
//        Cursor rs =mContRes.query(FriendsContentProvider.CONTENT_URI_record,MYCOLUMN , null, null, null);
//        rs.moveToFirst();


        SQLiteDatabase db1 = dbhelper.getWritableDatabase();
        String[] tmpid=Integer.toString(calender_id).split(",");
        String[] MYCOLUMN = new String[]{"_id","case_id","date_start","date_end","time_start","time_end","week","name","phone","address","service","hour","calender_id","blood_pressure_big","blood_pressure_small","blood_sugar_before","blood_sugar_after","weight","height"};
        Cursor rs =db1.query("record", MYCOLUMN, "calender_id=?", tmpid, null, null, null);
        rs.moveToFirst();

        t000.setText(rs.getString(12));
        t001.setText("排程起始日:"+rs.getString(2));
        t002.setText("排程截止日:"+rs.getString(3));
        t003.setText("服務時間:"+rs.getString(4)+"點~"+rs.getString(5)+"點");

        String[]   allweek=rs.getString(6).split(",");//循環取出list中的所有用戶名的值
        String sweek="";
        for (int m = 0; m < allweek.length; m = m + 1) {
            if (m==0) {
                sweek="星期"+weekday[Integer.parseInt(allweek[m])];
            }else{
                sweek=sweek+",星期"+weekday[Integer.parseInt(allweek[m])];;
            }
        }
        t004.setText("每週排程:"+sweek);
//        t005.setText("姓名:"+rs.getString(7));
//        t006.setText("手機:"+rs.getString(8));
//        t007.setText("地址:"+rs.getString(9));
        t005.setText("姓名:"+rs.getString(7));
        t006.setText("電話:"+rs.getString(8));
        t007.setText("服務項目:"+rs.getString(10));
        //  String  today= Integer.toString(calender_id).substring(4,6)+"月"+Integer.toString(calender_id).substring(6,8)+"日"+rs.getString(4)+"時~"+rs.getString(5)+"時";
        // t008.setText("服務時間:"+today);
        t009.setText("服務狀態:已完成");
        e001.setText(rs.getString(13));
        e002.setText(rs.getString(14));
        e003.setText(rs.getString(15));
        e004.setText(rs.getString(16));
        e005.setText(rs.getString(17));
        e006.setText(rs.getString(18));

        rs.close();
    }

    public class SpinnerItem {
        private String ID = "";
        private String Value = "";
        public SpinnerItem(String iD, String value) {
            ID = iD;
            Value = value;
        }
        @Override
        public String toString() { return Value;  }
        public String getID() {
            return ID;
        }
        public String getValue() {
            return Value;
        }
    }

//    private final Runnable updateTimer = new Runnable() {
//        @Override
//        public void run() {
//              mHandler.postDelayed(this, timer * 1000);// time轉換成毫秒 updateTime
//        }
//    };

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
//                            bottomNav4.setVisibility(View.GONE);
//                            bottomNav3.setVisibility(View.GONE);
//                            startActivity(new Intent(getApplicationContext(), Schedule.class));
//                            overridePendingTransition(0, 0);
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
        Schedule.this.finish();
//        //.makeText(this, "onStop", //.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Schedule.this.finish();
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
