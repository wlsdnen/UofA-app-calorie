package com.example.jojinwoo.learn2caltest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PreviewColumnChartView;

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

    ArrayList<Integer> errors;
    ArrayList<Integer> result;
    private int cursor;
    TextView act, est, mention, mPage;

    private ColumnChartView chart;
    private ColumnChartData data;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        act = (TextView)findViewById(R.id.realValue);
        est = (TextView)findViewById(R.id.estimateValue);
        mention = (TextView)findViewById(R.id.result_mention);
        mPage = (TextView)findViewById(R.id.pageScore);

        Intent intent = getIntent();
        cursor = intent.getIntExtra("pointer", 0);
        int estimate = intent.getIntExtra("estimate", 0);
        int actual   = intent.getIntExtra("actual",0);
        errors = intent.getIntegerArrayListExtra("errors");
        result = intent.getIntegerArrayListExtra("result");

        act.setText(Integer.toString(actual));
        est.setText(Integer.toString(estimate));
        mPage.setText(Integer.toString(cursor+OFFSET_PAGE)+"/"+Integer.toString(NUM_OF_QUIZ+OFFSET_PAGE));
        setText(result.get(cursor));

        chart = (ColumnChartView) findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        setGraphData();
    }

    public void onClickNext (View v)
    {
        Intent intent = new Intent();
//        intent.putExtra("result", );
        setResult(RESULT_OUT, intent);
        finish();
    }

    public void setText(int state)
    {
        if (state == 1)
        {
            mention.setText(getString(R.string.over_estimation));
            mention.setTextColor(Color.BLUE);
        }
        else if (state == -1)
        {
            mention.setText(getString(R.string.under_estimation));
            mention.setTextColor(Color.RED);
        }
        else
        {
            mention.setText(R.string.correct_estimation);
            mention.setTextColor(Color.GREEN);
        }

    }

    /**
     * Generates columns with subcolumns, columns have larger separation than subcolumns.
     */
    private void setGraphData() {

        int numColumns = cursor + OFFSET_PAGE;
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();

            int error = errors.get(i) * -1;
            int state = result.get(i);

            if (state == 0)
                values.add(new SubcolumnValue(error, ChartUtils.COLOR_GREEN));
            else if (state == -1)
                values.add(new SubcolumnValue(error, ChartUtils.COLOR_RED));
            else if (state == 1)
                values.add(new SubcolumnValue(error, ChartUtils.COLOR_BLUE));

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);

            axisValues.add(new AxisValue(i, Integer.toString(i+OFFSET_PAGE).toCharArray()));
        }

        data = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Quiz");
                axisY.setName("Erros");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        chart.setColumnChartData(data);
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {

        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

}
