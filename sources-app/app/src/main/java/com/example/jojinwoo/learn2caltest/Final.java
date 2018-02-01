package com.example.jojinwoo.learn2caltest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

import static com.example.jojinwoo.learn2caltest.MainActivity.PREF_READ_WRITE_MODE;
import static com.example.jojinwoo.learn2caltest.Quiz.NUM_OF_QUIZ;
import static com.example.jojinwoo.learn2caltest.Quiz.OFFSET_PAGE;

/**
 * Created by jojinwoo on 2018-01-23.
 */

public class Final extends AppCompatActivity implements View.OnClickListener
{
    private static final int PREF_READ_WRITE_MODE = 0;
    private boolean state;
    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        preferences = getSharedPreferences("pref", PREF_READ_WRITE_MODE);
        state = preferences.getBoolean("demographic",false);

        setResultText();

        Button btnPlay = (Button)findViewById(R.id.btnPlay);
        Button btnReuslt = (Button)findViewById(R.id.btnResult);

        btnPlay.setOnClickListener(this);
        btnReuslt.setOnClickListener(this);

    }

    // 버튼은 두개가 있다. 1. Play again, 2. See more results
    // Demographic을 제출한 적이 있다면 바로 각 Activity로 연결되고
    // 아닌 경우에는 Demographic page를 거쳐서 연결된다.


    @Override
    public void onClick(View view)
    {
        SharedPreferences.Editor edit = preferences.edit();

        switch (view.getId())
        {
            case R.id.btnPlay:
                if (state == false)
                {
                    edit.putString("NextFinalPage", "PLAY");
                    edit.commit();
                    Intent intent = new Intent(this, Demographic.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(this, Quiz.class);
                    startActivity(intent);
                }
                break;

            case R.id.btnResult:
                if (state == false)
                {
                    edit.putString("NextFinalPage", "RESULT");
                    edit.commit();
                    Intent intent = new Intent(this, Demographic.class);
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
        Intent intent = getIntent();
        ArrayList<Integer> results = intent.getIntegerArrayListExtra("estimates");

        int NUM_OF_OVEREST, NUM_OF_UNDEREST, NUM_OF_CORRECT;

        NUM_OF_OVEREST = 0;
        NUM_OF_UNDEREST = 0;
        NUM_OF_CORRECT = 0;

        for (int i=0; i<NUM_OF_QUIZ+OFFSET_PAGE; i++)
        {
            int result = results.get(i);

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

        TextView tvCorrect = (TextView)findViewById(R.id.correctValue);
        TextView tvOverestimation = (TextView)findViewById(R.id.overValue);
        TextView tvUnderestimation = (TextView)findViewById(R.id.underValue);

        tvCorrect.setText(Integer.toString(NUM_OF_CORRECT) + "/" + Integer.toString(NUM_OF_QUIZ+OFFSET_PAGE));
        tvOverestimation.setText(Integer.toString(NUM_OF_OVEREST) + "/" + Integer.toString(NUM_OF_QUIZ+OFFSET_PAGE));
        tvUnderestimation.setText(Integer.toString(NUM_OF_UNDEREST) + "/" + Integer.toString(NUM_OF_QUIZ+OFFSET_PAGE));

    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

}
