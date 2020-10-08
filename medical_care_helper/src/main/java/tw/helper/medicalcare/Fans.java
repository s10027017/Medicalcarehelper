package tw.helper.medicalcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class Fans extends AppCompatActivity implements View.OnClickListener{
    private Intent intent = new Intent();
    BottomNavigationView bottomNav1, bottomNav2;
    private Button f01_b001,f01_b002,f01_b003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fans);
        bottomNav1 = findViewById(R.id.nav_view1);
        bottomNav2 = findViewById(R.id.nav_view2);
        BottomNavigationHelper.removeShiftMode(bottomNav1);
        bottomNav1.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        BottomNavigationHelper.removeShiftMode(bottomNav2);
        bottomNav2.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNav1.setOnNavigationItemSelectedListener(navListener);
        bottomNav2.setOnNavigationItemSelectedListener(navListener);
        bottomNav2.setSelectedItemId(R.id.nav_cloud);
        f01_b001=(Button)findViewById(R.id.f01_b001);
        f01_b002=(Button)findViewById(R.id.f01_b002);
        f01_b003=(Button)findViewById(R.id.f01_b003);
        f01_b001.setOnClickListener(this);
        f01_b002.setOnClickListener(this);
        f01_b003.setOnClickListener(this);
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
//                            bottomNav4.setVisibility(View.GONE);
//                            bottomNav3.setVisibility(View.GONE);
//                            startActivity(new Intent(getApplicationContext(), Fans.class));
//                            overridePendingTransition(0, 0);
                            return true;
//=================================================================================================================

                    }
                    return false;
                }
            };
//=================================================================================================================



    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.f01_b001:
                Uri uri_web = Uri.parse("https://medicalcarehelper.com/");
                Intent it_web = new Intent(Intent.ACTION_VIEW, uri_web);
                startActivity(it_web);
                break;
            case R.id.f01_b002:
                Uri uri_facebook = Uri.parse("https://www.facebook.com/%E9%86%AB%E7%99%82%E7%85%A7%E8%AD%B7%E5%B0%8F%E5%B9%AB%E6%89%8BMedical-Care-Helper-104374188016071/");
                Intent it_facebook = new Intent(Intent.ACTION_VIEW, uri_facebook);
                startActivity(it_facebook);
                break;
            case R.id.f01_b003:
                Uri uri_line = Uri.parse("https://line.me/R/ti/g/Pc5GbBwca5");
                Intent it_line = new Intent(Intent.ACTION_VIEW, uri_line);
                startActivity(it_line);
                break;
        }
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
        Fans.this.finish();
//        //.makeText(this, "onStop", //.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Fans.this.finish();
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