package com.example.jojinwoo.learn2caltest.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jojinwoo.learn2caltest.Data.DataManager;
import com.example.jojinwoo.learn2caltest.R;

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

import static com.example.jojinwoo.learn2caltest.Activity.QuizActivity.NUM_OF_QUIZ;
import static com.example.jojinwoo.learn2caltest.Activity.QuizActivity.currentPage;
import static java.lang.Math.abs;

/**
 * Created by jojinwoo on 2018-01-17.
 */

public class FragmentScore extends Fragment {

    DataManager dataManager;

    TextView tvActual;
    TextView tvEstimate;
    TextView tvResult;
    Button mNextBtn;

    private ColumnChartView chart;
    private ColumnChartData data;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_score, container, false);

        dataManager = DataManager.getInstance(getActivity());

        tvActual = (TextView)root.findViewById(R.id.realValue);
        tvEstimate = (TextView)root.findViewById(R.id.estimateValue);
        tvResult = (TextView)root.findViewById(R.id.result_mention);
        tvActual.setText(Integer.toString(dataManager.getImageData(currentPage).getCalorie()));
        tvEstimate.setText(Integer.toString(dataManager.getImageData(currentPage).getUserAnswer()));
        setText(dataManager.getImageData(currentPage).getResult());

        chart = (ColumnChartView) root.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        setGraphData();

        mNextBtn = root.findViewById(R.id.btnNext);
        mNextBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (currentPage < NUM_OF_QUIZ) {
                    currentPage = currentPage + 1;
                    ((QuizActivity) getActivity()).setViewImage();
                    ((QuizActivity) getActivity()).setViewPage();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction tr = fm.beginTransaction();
                    FragmentEstimate fe = new FragmentEstimate();
                    tr.replace(R.id.frame, fe, "estimate");
                    tr.commit();
                }
                else
                {
                    currentPage = 0;
                    dataManager.shuffleImages();
                    Intent intent = new Intent(getActivity(), FinalActivity.class);
                    startActivity(intent);
                }
            }
        });

        return root;
    }

    public void setText(int state)
    {
        if (state == 1) {
            tvResult.setText(getString(R.string.over_estimation));
            tvResult.setTextColor(Color.BLUE);
        }
        else if (state == -1) {
            tvResult.setText(getString(R.string.under_estimation));
            tvResult.setTextColor(Color.RED);
        }
        else {
            tvResult.setText(R.string.correct_estimation);
            tvResult.setTextColor(Color.GREEN);
        }
    }

    /**
     * Generates columns with subcolumns, columns have larger separation than subcolumns.
     */
    private void setGraphData() {

        int numColumns = currentPage + QuizActivity.OFFSET_PAGE;
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();

            int error = dataManager.getImageData(i).getError();
            int state = dataManager.getImageData(i).getResult();

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

            axisValues.add(new AxisValue(i, Integer.toString(i+ QuizActivity.OFFSET_PAGE).toCharArray()));
        }

        if (numColumns < 5) {
            for (int i = numColumns; i<5; ++i) {
                values = new ArrayList<SubcolumnValue>();
                values.add(new SubcolumnValue(0, Color.parseColor("#FFFFFF")));
                Column column = new Column(values);
                column.setHasLabels(hasLabels);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
                axisValues.add(new AxisValue(i, Integer.toString(i+ QuizActivity.OFFSET_PAGE).toCharArray()));
            }
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
            Toast.makeText(getActivity(), Integer.toString((int)value.getValue()), Toast.LENGTH_LONG);
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

}
