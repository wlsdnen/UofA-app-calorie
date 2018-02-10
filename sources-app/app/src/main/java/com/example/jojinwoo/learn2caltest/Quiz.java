package com.example.jojinwoo.learn2caltest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.triggertrap.seekarc.SeekArc;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.jojinwoo.learn2caltest.MainActivity.PREF_READ_WRITE_MODE;

/**
 * Created by jojinwoo on 2018-01-08.
 */

public class Quiz extends AppCompatActivity {

    public static final int OFFSET_PAGE = 1;
    public static final int NUM_OF_QUIZ = 5 - OFFSET_PAGE;
    private static final int EST_IN = 100;
    private static final int EST_OUT = 101;
    private static final int RESULT_OUT = 102;
    private int pointer;
    private String time_start, time_end;
    ImageObject imgObj;
    SeekArc mSeekBar;
    TextView mValue, mPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null)
            pointer = savedInstanceState.getInt("cursor");

        // Initialization
        if (imgObj == null)
        {
            pointer = 0;
            imgObj = new ImageObject(this);
            mSeekBar = (SeekArc) findViewById(R.id.seekBar);
            mValue = (TextView) findViewById(R.id.current_value);
            mPage = (TextView) findViewById(R.id.pageQuiz);
            mPage.setText(Integer.toString(pointer+OFFSET_PAGE)+"/"+Integer.toString(NUM_OF_QUIZ+OFFSET_PAGE));
            mValue.setText(Integer.toString(mSeekBar.getProgress()));
            setImage(pointer);
            time_start = getCurrentTime();
        }
        mSeekBar.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onProgressChanged(SeekArc seekArc, int progress,
                                          boolean fromUser) {
                mValue.setText(String.valueOf(progress));
            }
        });

    }

    private void sendResult(int estimate)
    {
        // Initialize a SharedPreference object
        SharedPreferences preferences = getSharedPreferences("pref", PREF_READ_WRITE_MODE);

        Log.d("FILENAME", imgObj.getFilename(pointer));
        Log.d("FILENAME", imgObj.getFilename(pointer).replace("img", ""));
        // Pull user data from SharedPreference
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&uid="+ preferences.getString("user_id",null));
        stringBuilder.append("&iid="+ imgObj.getFilename(pointer).replace("img", ""));
        stringBuilder.append("&est="+ Integer.toString(estimate));
        stringBuilder.append("&loc="+ preferences.getString("location",null));
        stringBuilder.append("&inch="+ preferences.getString("screen_inch", null));
        stringBuilder.append("&width="+ preferences.getString("screen_width", null));
        stringBuilder.append("&height="+ preferences.getString("screen_height", null));
        stringBuilder.append("&device="+ preferences.getString("device",null));
        stringBuilder.append("&start="+ time_start);
        stringBuilder.append("&end="+ time_end);

        Log.d("POST response", stringBuilder.toString());
        InsertData task = new InsertData();
        // user_id(uuid), location, screen_size(pixels; width & height), estimations, Where the data came from: app, OS (Android), Time per one quiz
        task.execute(stringBuilder.toString(), "mInsertEstimate.php");
    }

    // 'Check' button listener.
    public void onClickCheck(View v)
    {
        int est = mSeekBar.getProgress();

        imgObj.setEstimates(pointer, est);
        time_end = getCurrentTime();

        sendResult(est);

        Intent intent = new Intent(this, Score.class);
        intent.putExtra("pointer", pointer);
        intent.putIntegerArrayListExtra("errors", imgObj.getErrors());
        intent.putIntegerArrayListExtra("result", imgObj.getResult());
        intent.putExtra("actual", imgObj.getLabel(pointer));
        intent.putExtra("estimate",   imgObj.getEstimate(pointer));
        startActivityForResult(intent, EST_IN);
    }

    public String getCurrentTime()
    {
        return new SimpleDateFormat("yyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == EST_OUT)
        {
            if (pointer < NUM_OF_QUIZ)
            {
                pointer = pointer + 1;
                mPage.setText(Integer.toString(pointer+OFFSET_PAGE)+"/"+Integer.toString(NUM_OF_QUIZ+OFFSET_PAGE));
                setImage(pointer);
                mSeekBar.setProgress(0);
                mValue.setText("0");
                time_start = getCurrentTime();
            }
            else // 문제를 다 풀었을 때, Final page로 이동
            {
                Intent intent = new Intent(this, Final.class);
                intent.putIntegerArrayListExtra("result", imgObj.getResult());
                startActivityForResult(intent, RESULT_OUT);
            }
        }
    }

    private void setImage (int ptr)
    {
        ImageView img = ((ImageView) findViewById(R.id.imgFood));
        // Image setter
        Context c = getApplicationContext();
        int id = c.getResources().getIdentifier("drawable/"+ imgObj.getFilename(ptr), null, c.getPackageName());
        // Set initial image.
        img.setImageResource(id);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("cursor", pointer);
    }

}