package com.example.jojinwoo.learn2caltest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import static com.example.jojinwoo.learn2caltest.Quiz.NUM_OF_QUIZ;
import static com.example.jojinwoo.learn2caltest.Quiz.OFFSET_PAGE;
import static java.lang.Math.abs;
import static java.lang.Math.pow;

/**
 * Created by jojinwoo on 2018-01-17.
 */

public class Score extends Activity {

    private static final int RESULT_IN = 100;
    private static final int RESULT_OUT = 101;
    private int RESULT;

    TextView act, est, mention, mPage;
    GraphView graph;
    LineGraphSeries<DataPoint> series, prevSeries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        act = (TextView)findViewById(R.id.realValue);
        est = (TextView)findViewById(R.id.estimateValue);
        mention = (TextView)findViewById(R.id.result_mention);
        mPage = (TextView)findViewById(R.id.pageScore);

        graph = (GraphView)findViewById(R.id.graph);
        graph.setTitle("Errors");
        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(60);
        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-1000);
        graph.getViewport().setMaxY(1000);

        series = new LineGraphSeries<>();

        Intent intent = getIntent();
        int cursor = intent.getIntExtra("pointer", 0);
        ArrayList<Integer> estimates = intent.getIntegerArrayListExtra("estimates");
        ArrayList<Integer> labels = intent.getIntegerArrayListExtra("labels");
        ArrayList<Integer> errors = intent.getIntegerArrayListExtra("errors");

        for (int x=0; x<=cursor; x++) {
            series.appendData(new DataPoint(x, errors.get(x)), true, 60);
        }

        graph.addSeries(series);
        // result 값 다시 Quiz Activity로 전달하여, imgObj에 정답 array 만들고 그 안에 저장.

        RESULT = checkResult(labels.get(cursor), estimates.get(cursor));
        act.setText(Integer.toString(labels.get(cursor)));
        est.setText(Integer.toString(estimates.get(cursor)));
        mPage.setText(Integer.toString(cursor+OFFSET_PAGE)+"/"+Integer.toString(NUM_OF_QUIZ+OFFSET_PAGE));
    }

    public void onClickNext (View v)
    {
        Intent intent = new Intent();
        intent.putExtra("result", RESULT);
        setResult(RESULT_OUT, intent);
        finish();
    }

    public int checkResult(int actual, int estimate)
    {
        int state = -1;
        double tolerance = 0;

        // Accuracy
        if(actual < 100) { tolerance = 0.0005 * pow(actual, 2) + 0.1 * actual + 5; }
        else { tolerance = 0.2 * actual; }

        if ((abs(actual - estimate) > tolerance) && ((actual - estimate) < 0))
        {
            state = 1;
            mention.setText(getString(R.string.over_estimation));
            mention.setTextColor(Color.BLUE);
        }
        else if ((abs(actual - estimate) > tolerance) && ((actual - estimate) > 0))
        {
            state = -1;
            mention.setText(getString(R.string.under_estimation));
            mention.setTextColor(Color.RED);
        }
        else
        {
            state = 0;
            mention.setText(R.string.correct_estimation);
            mention.setTextColor(Color.GREEN);
        }

        return state;
    }

}
