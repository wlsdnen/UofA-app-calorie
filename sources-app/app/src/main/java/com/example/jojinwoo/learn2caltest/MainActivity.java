package com.example.jojinwoo.learn2caltest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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