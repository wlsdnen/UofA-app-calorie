package com.example.jojinwoo.learn2caltest.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jojinwoo.learn2caltest.Data.DataManager;
import com.example.jojinwoo.learn2caltest.Data.SendQuery;
import com.example.jojinwoo.learn2caltest.Data.User;
import com.example.jojinwoo.learn2caltest.R;
import com.triggertrap.seekarc.SeekArc;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jojinwoo on 2018-01-08.
 */

public class QuizActivity extends AppCompatActivity {

    // constant values
    public static final int OFFSET_PAGE     = 1;
    public static final int NUM_OF_QUIZ     = 60 - OFFSET_PAGE;
    public static int currentPage;
    private static final int DEFAULT_VALUE  = 0;

    private DataManager dataManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

//        if (savedInstanceState != null)
//            currentPage = savedInstanceState.getInt("currentPage");
//        else {
        currentPage = DEFAULT_VALUE;

        dataManager = DataManager.getInstance(this);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        FragmentEstimate fe = new FragmentEstimate();
        tr.add(R.id.frame, fe, "estimate");
        tr.commit();

        setViewPage();
        setViewImage();
//        }
    }

    public void setViewImage() {
        Context c = getApplicationContext();
        int id = c.getResources().getIdentifier("drawable/"+ "img" + dataManager.getImageData(currentPage).getImageId(), null, c.getPackageName());
        ((ImageView)findViewById(R.id.imgFood)).setImageResource(id);
    }

    public void setViewPage() { ((TextView) findViewById(R.id.pageQuiz)).setText(Integer.toString(currentPage +OFFSET_PAGE)+"/"+Integer.toString(NUM_OF_QUIZ+OFFSET_PAGE)); }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage", currentPage);
    }

}