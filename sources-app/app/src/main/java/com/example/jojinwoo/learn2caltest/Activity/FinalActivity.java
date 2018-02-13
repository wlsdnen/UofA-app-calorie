package com.example.jojinwoo.learn2caltest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.jojinwoo.learn2caltest.Data.DataManager;
import com.example.jojinwoo.learn2caltest.R;

import java.util.ArrayList;

/**
 * Created by jojinwoo on 2018-01-23.
 */

public class FinalActivity extends AppCompatActivity implements View.OnClickListener
{
    private DataManager dataManager;
    private boolean isDemoSaved;

    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        dataManager = DataManager.getInstance(this);
        preferences = dataManager.getSharedPreferences(this);
        edit = dataManager.getEditor();
        isDemoSaved = preferences.getBoolean("demographic",false);

        setResultText();

        findViewById(R.id.btnPlay).setOnClickListener(this);
        findViewById(R.id.btnResult).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnPlay:
                if (isDemoSaved == false)
                {
                    edit.putString("NextFinalPage", "PLAY");
                    edit.commit();
                    Intent intent = new Intent(this, DemographicActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(this, QuizActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.btnResult:
                if (isDemoSaved == false)
                {
                    edit.putString("NextFinalPage", "RESULT");
                    edit.commit();
                    Intent intent = new Intent(this, DemographicActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    public void setResultText()
    {
        int NUM_OF_OVEREST, NUM_OF_UNDEREST, NUM_OF_CORRECT;

        NUM_OF_OVEREST = 0;
        NUM_OF_UNDEREST = 0;
        NUM_OF_CORRECT = 0;

        for (int i = 0; i< QuizActivity.NUM_OF_QUIZ + QuizActivity.OFFSET_PAGE; i++)
        {
            int result = dataManager.getImageData(i).getResult();

            if (result == -1)
            {
                NUM_OF_UNDEREST++;
            }
            else if (result == 1)
            {
                NUM_OF_OVEREST++;
            }
            else if (result == 0)
            {
                NUM_OF_CORRECT++;
            }
        }

        ((TextView)findViewById(R.id.correctValue)).setText(Integer.toString(NUM_OF_CORRECT) + "/" + Integer.toString(QuizActivity.NUM_OF_QUIZ+ QuizActivity.OFFSET_PAGE));;
        ((TextView)findViewById(R.id.overValue)).setText(Integer.toString(NUM_OF_OVEREST) + "/" + Integer.toString(QuizActivity.NUM_OF_QUIZ+ QuizActivity.OFFSET_PAGE));
        ((TextView)findViewById(R.id.underValue)).setText(Integer.toString(NUM_OF_UNDEREST) + "/" + Integer.toString(QuizActivity.NUM_OF_QUIZ+ QuizActivity.OFFSET_PAGE));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

}
