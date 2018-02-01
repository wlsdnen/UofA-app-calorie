package com.example.jojinwoo.learn2caltest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
{
    public static final int PREF_READ_WRITE_MODE = 0;
    private static final String TAG = "TAG";
    private static final String DEVICE_NAME = "ANDROID";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUserInfo();
    }

    public void initUserInfo()
    {
        // Initialize a SharedPreference object
        SharedPreferences preferences = getSharedPreferences("pref", PREF_READ_WRITE_MODE);
        SharedPreferences.Editor edit = preferences.edit();

        // Pull user data from SharedPreference
        String uuid = preferences.getString("user_id",null);
        String device = preferences.getString("device", null);
        String location = preferences.getString("location", null);
        String inch = preferences.getString("screen_inch", null);

        if (uuid == null)
        {
            uuid = UUID.randomUUID().toString();
            edit.putString("user_id", uuid);
        }

        if (device == null)
        {
            edit.putString("device", DEVICE_NAME);
        }

        if (location == null)
        {
            edit.putString("location", getLocation());
        }

        if (inch == null)
        {
            String[] screenSize = getScreenDimension();
            edit.putString("screen_width", screenSize[0]);
            edit.putString("screen_height", screenSize[1]);
            edit.putString("screen_inch", screenSize[2]);
        }

        edit.commit();

        InsertData task = new InsertData();
        String postParameter = "&user_id=" + uuid;
        task.execute(postParameter, "mInitUser.php");
    }

    public void mOnClick(View v)
    {
        Intent intent = new Intent(this, Quiz.class);
        startActivity(intent);
    }

    // AsyncTask 공부.
    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            // MySQL 연결 제대로 되었는지 확인하고 성공/실패 여뷰 tv에 setText로 보여주기.
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {

            String name = (String)params[0];
            String serverURL = "http://jwdrive.myqnapcloud.com/mInitUser.php";
            String postParameters = "user_id=" + name;

            try
            {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();

            }
            catch (Exception e)
            {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }
    }


    private String[] getScreenDimension(){
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double)width / (double)dens;
        double hi = (double)height / (double)dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x+y);

        String[] screenInformation = new String[3];
        screenInformation[0] = String.valueOf(width);
        screenInformation[1] = String.valueOf(height);
        screenInformation[2] = String.format("%.2f", screenInches);

        return screenInformation;
    }

    private String getLocation()
    {
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        return countryCodeValue;
    }
}