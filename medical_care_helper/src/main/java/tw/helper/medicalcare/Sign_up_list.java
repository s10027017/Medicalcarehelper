package tw.helper.medicalcare;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sign_up_list extends ListActivity  //這邊要先繼承 ListActivity
{
    private FriendDbHelper dbHper;
    private static final String DB_FILE = "friends.db";
    private static final String DB_TABLE = "signup";
    private static final int DBversion = 1;

    private ArrayList<String> recSet;
    private int index = 0;
    String msg = null;
    private TextView tvTitle;
    List<Map<String, Object>> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_list);
        setupViewComponent();
    }

    private void setupViewComponent()
    {
        initDB();  //叫資料庫
        tvTitle = (TextView) findViewById(R.id.tvIdTitle);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Navy));
        tvTitle.setBackgroundResource(R.color.Aqua);
        //tvTitle.setText("顯示資料： 共 " + recSet.size() + " 筆");
        tvTitle.setText("會員列表");
        //=========== 下面開始抓 SQLite 資料=============
        mList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < recSet.size(); i++)  // i是代表y 幾筆資料
        {
            Map<String, Object> item = new HashMap<String, Object>(); // 代表X 每筆資料裡面放幾個東西
            String[] fld = recSet.get(i).split("#"); //initDB 裡的 recSet 已經有抓了 F4可過去看
            item.put("imgView",R.drawable.userconfig);
            item.put("txtView", "ID:" + fld[0] + "\n"+"姓名:" + fld[2] + "\n" +"Email:" + fld[1]  + "\n" +"密碼:" + fld[3]);
            mList.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(
                this, // put的那些陣列
                mList,
                R.layout.sign_up_list_item,  // 顯示的樣式
                new String[]{"imgView", "txtView"}, // 這邊多加 下面也要多加
                new int[]{R.id.imgView, R.id.txtView} // 放到這邊的物件 有三個就三個
        );
        setListAdapter(adapter);
        //----------------------------------
        ListView listview = getListView();
        listview.setTextFilterEnabled(true);
//        listview.setOnItemClickListener(listviewOnItemClkLis);

    }

//    private ListView.OnItemClickListener listviewOnItemClkLis = new ListView.OnItemClickListener()
//    {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//        {
//            String s = "你按下第 " + Integer.toString(position + 1) + "筆\n" + ((TextView) view.findViewById(R.id.txtView)).getText()
//                    .toString();
//            tvTitle.setText(s);
//        }
//    };

    private void initDB()
    {
        if(dbHper == null)  //上面要宣告完成
            dbHper = new FriendDbHelper(this,DB_FILE,null,DBversion);
        recSet = dbHper.getRecSet_signup();
    }



//================================ 下面是生命週期 ===========================================
//    1.當Activity準備要產生時，先呼叫onCreate方法。
//    2.Activity產生後（還未出現在手機螢幕上），呼叫onStart方法。
//    3.當Activity出現手機上後，呼叫onResume方法。
//    4.當使用者按下返回鍵結束Activity時， 先呼叫onPause方法。
//    5.當Activity從螢幕上消失時，呼叫onStop方法。
//    6.最後完全結束Activity之前，呼叫onDestroy方法。

    @Override
    protected void onStop()
    {
        super.onStop();
//        //.makeText(this, "onStop", //.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
//        //.makeText(this, "onDestroy", //.LENGTH_LONG).show();
    }

    @Override
    protected void onPause()  //中斷把他關了
    {
        super.onPause();
        if(dbHper!=null)
        {
            dbHper.close();
            dbHper = null;
        }
//        //.makeText(this, "onPause", //.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() //重啟再把他開起來
    {
        super.onResume();
        if(dbHper == null)
        {
            dbHper = new FriendDbHelper(this,DB_FILE,null,DBversion);
        }
//        //.makeText(this, "onResume", //.LENGTH_LONG).show();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
//        //.makeText(this, "onStart", //.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart()
    {
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //================================ 下面是次類別之類的東西 ==============================================
}
