package com.example.jojinwoo.learn2caltest.Activity;

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

    private DataManager dataManager;

    // constant values
    public static final int OFFSET_PAGE     = 1;
    public static final int NUM_OF_QUIZ     = 5 - OFFSET_PAGE;
    private static final int DEFAULT_VALUE  = 0;
    private static final int ESTIMATE_IN    = 100;
    private static final int ESTIMATE_OUT   = 101;
    private static final int RESULT_OUT     = 102;

    // variables
    int currentPage;
    String time_start, time_end;

    // UI objects
    SeekArc mSeekBar;
    TextView mValue;
    TextView mPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null)
            currentPage = savedInstanceState.getInt("currentPage");
        else {
            // instance
            dataManager = DataManager.getInstance(this);

            // variables
            currentPage = DEFAULT_VALUE;
            time_start = getCurrentTime();

            // UI components
            mSeekBar = (SeekArc) findViewById(R.id.seekBar);
            mValue = (TextView) findViewById(R.id.current_value);
            mPage = (TextView) findViewById(R.id.pageQuiz);
            setViewPage();
            setViewImage();
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

    // 'Check' button listener.
    public void onClickCheck(View v)
    {
        int estimate = mSeekBar.getProgress();
        setEstimate(estimate);
        time_end = getCurrentTime();

        makeQuery(estimate);

        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("currentPage", currentPage);
        startActivityForResult(intent, ESTIMATE_IN);
    }

    private void setEstimate(int estimate) {
        dataManager.getImageData(dataManager.getCurrentIdx()).submitAnswer(estimate);
        dataManager.setCurrentIdx();
    }

    private void makeQuery(int estimate)
    {
        // Initialize a SharedPreference object
        SharedPreferences preferences = dataManager.getSharedPreferences(this);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("&uid="+ User.UID.getValue());
        stringBuilder.append("&iid="+ dataManager.getImageData(dataManager.getCurrentIdx()).getImageId());
        stringBuilder.append("&est="+ estimate);
        stringBuilder.append("&loc="+ User.LOCATION.getValue());
        stringBuilder.append("&inch="+ User.SCREEN_INCH.getValue());
        stringBuilder.append("&width="+ User.SCREEN_WIDTH.getValue());
        stringBuilder.append("&height="+ User.SCREEN_HEIGHT.getValue());
        stringBuilder.append("&device="+ User.DEVICE_NAME.getValue());
        stringBuilder.append("&start="+ time_start);
        stringBuilder.append("&end="+ time_end);

        SendQuery task = new SendQuery();
        task.execute(stringBuilder.toString(), "mInsertEstimate.php");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ESTIMATE_OUT) {
            if (currentPage < NUM_OF_QUIZ) {
                currentPage = currentPage + 1;
                time_start = getCurrentTime();
                setViewPage();
                setViewImage();
                setViewProgress();
                setViewValue(DEFAULT_VALUE);
            }
            else {
                currentPage = 0;
                dataManager.shuffleImages();
                Intent intent = new Intent(this, FinalActivity.class);
                startActivityForResult(intent, RESULT_OUT);
            }
        }
    }

    public String getCurrentTime()
    {
        return new SimpleDateFormat("yyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    private void setViewProgress()
    {
        mSeekBar.setProgress(DEFAULT_VALUE);
    }

    private void setViewValue(int value)
    {
        mValue.setText(Integer.toString(value));
    }

    private void setViewImage()
    {
        Context c = getApplicationContext();
        int id = c.getResources().getIdentifier("drawable/"+ "img" + dataManager.getImageData(currentPage).getImageId(), null, c.getPackageName());
        ((ImageView)findViewById(R.id.imgFood)).setImageResource(id);
    }

    private void setViewPage()
    {
        mPage.setText(Integer.toString(currentPage +OFFSET_PAGE)+"/"+Integer.toString(NUM_OF_QUIZ+OFFSET_PAGE));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage", currentPage);
    }

}