package com.example.jojinwoo.learn2caltest.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.example.jojinwoo.learn2caltest.Data.DataManager;
import com.example.jojinwoo.learn2caltest.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static Context contextOfMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.contextOfMainActivity = this.getApplicationContext();

        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnAbout).setOnClickListener(this);

        initDataManager();
        DataManager.getInstance(contextOfMainActivity);
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnStart:
                Intent intent = new Intent(this, QuizActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAbout:
                Toast.makeText(this, "NOT IMPLEMENTED YET", Toast.LENGTH_LONG).show();
                break;
        }

    }

    public static Context getContextOfMainActivity() { return contextOfMainActivity; }

    public static void initDataManager() { new DataManager(getContextOfMainActivity()); }

}