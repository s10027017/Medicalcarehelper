package tw.helper.medicalcare;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Mh02 extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMarkerClickListener
{
    private static final String DB_FILE = "friend.db";
    //----------------------------------------------------------------------------------------------
    //所需要申請的權限數組
    private static final String[][] permissionsArray = new String[][]{
            {Manifest.permission.ACCESS_FINE_LOCATION, "定位"}
    };
    private List<String> permissionsList = new ArrayList<String>();

    //申請權限後的返回碼
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private GoogleMap map;
    private LatLng VGPS = new LatLng(24.172127, 120.610313);
    private float mapzoom = 16.0f;
    private TextView txtOutput,tmsg;
    private String provider;
    private Marker markerMe;
    private double dLat,dLon;
    private LocationManager locationManager;
    private int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=101;
    private String TAG = "Tcnr17=>";
     long minTime = 5000;
     float minDist = 5.0f;
    private float currentZoom = 16.0f;
    private float Anchor_x=0.5f;
    private float Anchor_y=0.99f;
    private FriendDbHelper dbHper;
    private int DBversion = 1 ;
    private ArrayList<String> recSet;

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mh02);
        checkRequiredPermission(this);
        u_checkgps();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initLocationProvider(); //核示定位
        setupviewcomponent();
    }
    private void setupviewcomponent() {
        txtOutput = (TextView) findViewById(R.id.txtOutput);
        tmsg = (TextView) findViewById(R.id.msg);

        init_app();
    }
    //=======================================
    private void init_app() {    // 檢查定位是否成功
        try {
            if (initLocationProvider()) {
                nowaddress();
            }
        } catch (Exception e) {
            //對話方塊啟用GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS未開啟")
                    .setMessage("GPS目前狀態是尚未啟用.\n" + "請先開啟定位!,再次執行APP!")
                    .setPositiveButton("離開再次執行", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //使用Intent物件啟動設定程式來更改GPS設定
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                            finish();
                        }
                    }).setNegativeButton("不啟用", null).create().show();
            return;
        }
    }
    private boolean initLocationProvider() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            return true;
        } else {
            return false;
        }
    }
    private void checkRequiredPermission(final Activity activity) { //
//        String permission_check= String[i][0] permission;
        for (int i = 0; i < permissionsArray.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissionsArray[i][0]) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permissionsArray[i][0]);
            }
        }
        if (permissionsList.size() != 0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new
                    String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int i=0; i<permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        //.makeText(getApplicationContext(), permissions[i]+"權限申請成功!", //.LENGTH_LONG).show();
                    } else {
                        //.makeText(getApplicationContext(), "權限被拒絕： "+permissions[i], //.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void updateWithNewLocation(Location location) {
        String where = "";
        if (location != null) {
            double lng = location.getLongitude();// 經度
            double lat = location.getLatitude();// 緯度
            float speed = location.getSpeed();// 速度
            long time = location.getTime();// 時間
            String timeString = getTimeString(time);
            where = "經度: " + lng + "\n緯度: " + lat + "\n速度: " + speed + "\n時間: " + timeString + "\nProvider: "
                    + provider;
            // 標記"我的位置"
            showMarkerMe(lat, lng);
            cameraFocusOnMe(lat, lng);
        } else {
            where = "*位置訊號消失*";
        }
        // 位置改變顯示
        txtOutput.setText(where);
    }
    private String getTimeString(long timeInMilliseconds) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timeInMilliseconds);
    }
    private void cameraFocusOnMe(double lat, double lng) {
        CameraPosition camPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(map.getCameraPosition().zoom)
                .build();
        /* 移動地圖鏡頭 */
        map.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));
        tmsg.setText("目前Zoom:" + map.getCameraPosition().zoom);
    }
    private void showMarkerMe(double lat, double lng) {
        if (markerMe != null)             markerMe.remove();
//------------------
        int resID = getResources().getIdentifier("pin", "drawable", getPackageName());
//-------------------------
        dLat = lat; // 南北緯
        dLon = lng; // 東西經
        String vtitle = "GPS位置:";
        String vsnippet = "座標:" + String.valueOf(dLat) + "," + String.valueOf(dLon);
        VGPS = new LatLng(lat, lng);// 更新成欲顯示的地圖座標
        MarkerOptions markerOpt = new MarkerOptions();
        markerOpt.position(new LatLng(lat, lng));
        markerOpt.title(vtitle);
        markerOpt.snippet(vsnippet);
        markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//        if (icosel==0){

//        }else{
//            image_des = BitmapDescriptorFactory.fromResource(resID);// 使用照片
//            markerOpt.icon(image_des);
//        }
//        markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        markerMe = map.addMarker(markerOpt);
//----------------------------
//        locations[0][1] = lat + "," + lng; //---偉經度更新呈現在位置---
    }
    private void u_checkgps() {
        // 取得系統服務的LocationManager物件
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 檢查是否有啟用GPS
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 顯示對話方塊啟用GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("定位管理")
                    .setMessage("GPS目前狀態是尚未啟用.\n"
                            + "請問你是否現在就設定啟用GPS?")
                    .setPositiveButton("啟用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 使用Intent物件啟動設定程式來更改GPS設定
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("不啟用", null).create().show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
//        mUiSettings = map.getUiSettings();//
//        開啟 Google Map 拖曳功能
        map.getUiSettings().setScrollGesturesEnabled(true);

//        右下角的導覽及開啟 Google Map功能
        map.getUiSettings().setMapToolbarEnabled(true);

//        左上角顯示指北針，要兩指旋轉才會出現
        map.getUiSettings().setCompassEnabled(true);

//        右下角顯示縮放按鈕的放大縮小功能
        map.getUiSettings().setZoomControlsEnabled(true);

        map.setTrafficEnabled(true);
        //開啟路況功能


        //-------------------
//
//        // Add a marker in Sydney and move the camera
////        LatLng VGPS = new LatLng(-34, 151);
//        map.addMarker(new MarkerOptions().position(VGPS).title("中區職訓"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(VGPS));
        map.moveCamera(CameraUpdateFactory.zoomBy(mapzoom));
        //----------取得定位許可-----------------------
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //----顯示我的位置ICO-------
//            //.makeText(getApplicationContext(), "GPS定位權限未允許", //.LENGTH_LONG).show();
        } else {
            //----顯示我的位置ICO-------
            map.setMyLocationEnabled(true);
            return;
        }

    }

    private void nowaddress() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(provider);
            updateWithNewLocation(location); //*****開啟GPS定位
            return;
        }

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled))
            tmsg.setText("GPS 未開啟");
        else {
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        minTime, minDist, locationListener);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                tmsg.setText("使用網路GPS");
            }

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        minTime, minDist, locationListener);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                tmsg.setText("使用精確GPS");
            }
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
            Log.d(TAG, "locationListener->onLocationChanged:" + map.getCameraPosition().zoom + " currentZoom:"
                    + currentZoom);
            tmsg.setText("目前Zoom:" + map.getCameraPosition().zoom);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                    Log.v(TAG, "Status Changed: Out of Service");
                    tmsg.setText("Out of Service");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.v(TAG, "Status Changed: Temporarily Unavailable");
                    tmsg.setText("Temporarily Unavailable");
                    break;
                case LocationProvider.AVAILABLE:
                    Log.v(TAG, "Status Changed: Available");
                    tmsg.setText("Available");
                    break;
            }

        }

        @Override
        public void onProviderEnabled(String provider) {
            tmsg.setText("onProviderEnabled");
            Log.d(TAG, "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
            Log.d(TAG, "onProviderDisabled");
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
    protected void onStop() {
        super.onStop();
        Mh02.this.finish();
//        //.makeText(this, "onStop", //.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Mh02.this.finish();
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

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public boolean onMarkerClick(final Marker marker_Animation) {
        if (!marker_Animation.getTitle().substring(0, 4).equals("Move")) {
            //非GPS移動位置;設定動畫
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final long duration = 1500; //連續時間
            final Interpolator interpolator = new BounceInterpolator();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                    marker_Animation.setAnchor(Anchor_x, Anchor_y + 2 * t); //設定標的位置
                    if (t > 0.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }} });
        } else {//GPS移動位置,不使用動畫
            Mh02.this.markerMe.hideInfoWindow();
        }
        return false;
    }

    //================================ 下面是次類別之類的東西 ==============================================

}
