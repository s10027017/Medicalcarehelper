package tw.helper.medicalcare;

//import android.util.Log;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.protocol.HTTP;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;


import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DBConnector {
    //宣告類別變數以方便存取，並判斷是否連線成功
//    public static int httpstate = 0;
//    static String result = null;
//    static String TAG = "tcnr26=>";
//    //---------------------------
//    static InputStream is = null;
//    static String line = null;
//    static int code;
//    static String mysql_code = null;

    public static int httpstate = 0;
    //--------------------------------------------------------
    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static String TAG = "tcnr26=>";
    private static OkHttpClient client = new OkHttpClient();
    //---------------------------------------------------------

    //-------localhost-------
//            static String connect_ip ="http://192.168.60.18/android_mysql_connect/android_connect_db_all.php";
    //-----------我的--------
//    static String connect_ip = "https://109atcnr18.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
    //-----------我的--------
//    static String connect_ip = "https://oldpa88.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
//-----------班長--------
//            static String connect_ip = "https://109atcnr01.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
//-----------01--------
//            static String connect_ip ="https://109atcnr02.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
//-----------02--------
//            static String connect_ip ="https://109atcnr05.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
//-----------03--------
//            static String connect_ip = "https://109atcnr26.000webhostapp.com/test/android_connect_db_all.php";
    static String connect_ip = "https://medicalcarehelper.com/android_mysql_connect26/android_connect_db_all.php";
//-----------04--------
//            static String connect_ip = "https://109atcnr28.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";

    /* *************************************************
     *************---MySQL查詢資料----------------
     *************************************************   */



    public static String executeQuery(ArrayList<String> query_string) {
//         client = new OkHttpClient();
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "query")
                .add("query_string", query_0)
                .build();
        Log.d(TAG, "Query=" + query_0);
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        httpstate = 0;   //設 httpcode初始直
        try (Response response = client.newCall(request).execute()) {
            // ===========================================
            // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
            httpstate = response.code();
            Log.d(TAG, "executeQuery=" + response);
            // ===========================================
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    /*  *************************************************
     *************---MySQL新增資料----------------
     *************************************************   */
    public static String executeInsert(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "insert")
                .add("name", query_0)
                .add("grp", query_1)
                .add("address", query_2)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    //    nameValuePairs.add(new BasicNameValuePair("email", g_Email));
    //    nameValuePairs.add(new BasicNameValuePair("username", g_DisplayName));
    public static String executeInsert_googlesignup(ArrayList<String> query_string)
    {
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "insert")
                .add("email", query_0)
                .add("username", query_1)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //    nameValuePairs.add(new BasicNameValuePair("email", t_email));
//    nameValuePairs.add(new BasicNameValuePair("username", t_username));
//    nameValuePairs.add(new BasicNameValuePair("password", t_password));
    public static String executeInsert_signup(ArrayList<String> query_string)
    {
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "insert")
                .add("email", query_0)
                .add("username", query_1)
                .add("password", query_2)
                .build();
        //--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

//===============================================================================================


    /* *************************************************
     *************---MySQL更新資料----------------
     *************************************************   */
    public static String executeUpdate(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "update")
                .add("id", query_0)
                .add("name", query_1)
                .add("grp", query_2)
                .add("address", query_3)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

//        nameValuePairs.add(new BasicNameValuePair("id", tid));
//        nameValuePairs.add(new BasicNameValuePair("username", tname));
//        nameValuePairs.add(new BasicNameValuePair("email", temail));
//        nameValuePairs.add(new BasicNameValuePair("phone", tphone));
//        nameValuePairs.add(new BasicNameValuePair("address", taddress));

    public static String executeUpdate_modify(ArrayList<String> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);
        String query_4 = query_string.get(4);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "update_member")
                .add("id", query_0)
                .add("username", query_1)
                .add("email", query_2)
                .add("phone", query_3)
                .add("address", query_4)
                .build();
        //--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

//            nameValuePairs.add(new BasicNameValuePair("id", tid));
//            nameValuePairs.add(new BasicNameValuePair("password", tpsd));

    public static String executeUpdate_resetpsd(ArrayList<String> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "update")
                .add("id", query_0)
                .add("password", query_1)
                .build();
        //--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /* *************************************************
     *************---MySQL刪除資料----------------
     *************************************************   */
    public static String executeDelet(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "delete")
                .add("id", query_0)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeDelet_case(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "delete")
                .add("id", query_0)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeQuery_Inquire(ArrayList<String> query_string) {
        //         client = new OkHttpClient();
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "query")
                .add("query_string", query_0)
                .build();
        Log.d(TAG, "Query=" + query_0);
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        httpstate = 0;   //設 httpcode初始直
        try (Response response = client.newCall(request).execute()) {
            // ===========================================
            // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
            httpstate = response.code();
//            Log.d(TAG, "executeQuery=" + response);
            // ===========================================
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeQuery_schedule(ArrayList<String> query_string) {
        //         client = new OkHttpClient();
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "query_schedule")
                .add("query_string", query_0)
                .build();
        Log.d(TAG, "Query=" + query_0);
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        httpstate = 0;   //設 httpcode初始直
        try (Response response = client.newCall(request).execute()) {
            // ===========================================
            // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
            httpstate = response.code();
//            Log.d(TAG, "executeQuery=" + response);
            // ===========================================
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeInsert_record(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);
        String query_4 = query_string.get(4);
        String query_5 = query_string.get(5);
        String query_6 = query_string.get(6);
        String query_7 = query_string.get(7);
        String query_8 = query_string.get(8);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "insert")
                .add("name", query_0)
                .add("record", query_1)
                .add("required", query_2)
                .add("types", query_3)
                .add("details", query_4)
                .add("startdate", query_5)
                .add("starttime", query_6)
                .add("enddate", query_7)
                .add("endtime", query_8)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeUpdate_record(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);
        String query_4 = query_string.get(4);
        String query_5 = query_string.get(5);
        String query_6 = query_string.get(6);
        String query_7 = query_string.get(7);
        String query_8 = query_string.get(8);
        String query_9 = query_string.get(9);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "update_record")
                .add("id", query_0)
                .add("name", query_1)
                .add("record", query_2)
                .add("required", query_3)
                .add("types", query_4)
                .add("details", query_5)
                .add("startdate", query_6)
                .add("starttime", query_7)
                .add("enddate", query_8)
                .add("endtime", query_9)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeUpdate_case(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip;
//        nameValuePairs.add(case_id);
//        nameValuePairs.add(update_name);
//        nameValuePairs.add(update_sex);
//        nameValuePairs.add(update_birthday);
//        nameValuePairs.add(update_age);
//        nameValuePairs.add(update_email);
//        nameValuePairs.add(update_phone);
//        nameValuePairs.add(update_addr);
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);
        String query_4 = query_string.get(4);
        String query_5 = query_string.get(5);
        String query_6 = query_string.get(6);
        String query_7 = query_string.get(7);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "update_case")
                .add("CID", query_0)
                .add("name", query_1)
                .add("sex", query_2)
                .add("birthday", query_3)
                .add("age", query_4)
                .add("email", query_5)
                .add("phone", query_6)
                .add("address", query_7)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeInsert_case_info(ArrayList<String> query_string)
    {
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0); // 姓名
        String query_1 = query_string.get(1); // 生日
        String query_2 = query_string.get(2); // 年齡
        String query_3 = query_string.get(3); // Email
        String query_4 = query_string.get(4); // 地址
//        String query_5 = query_string.get(5); // 緊急聯絡人
//        String query_6 = query_string.get(6); // 關係
//        String query_7 = query_string.get(7); // 緊急連絡人電話
//        String query_8 = query_string.get(8); // 性別
        String query_5 = query_string.get(5); // 緊急連絡人電話
        String query_6 = query_string.get(6); // 性別
        String query_7 = query_string.get(7); // test


        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "insert_case_info")
                .add("name", query_0) // 姓名
                .add("birthday", query_1) // 生日
                .add("age", query_2) // 年齡
                .add("email", query_3) // Email
                .add("address", query_4) // 地址
//                .add("relationship_name", query_5) // 緊急聯絡人
//                .add("relationship", query_6) // 關係
//                .add("phone", query_7) // 緊急連絡人電話
//                .add("sex", query_8) // 性別
                .add("phone", query_5) // 緊急聯絡人
                .add("sex", query_6) // 關係
                .add("test", query_7) // test
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
//==========================
}