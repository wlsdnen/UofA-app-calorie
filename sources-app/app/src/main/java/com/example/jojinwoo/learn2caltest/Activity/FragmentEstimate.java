package com.example.jojinwoo.learn2caltest.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jojinwoo.learn2caltest.Data.DataManager;
import com.example.jojinwoo.learn2caltest.Data.SendQuery;
import com.example.jojinwoo.learn2caltest.Data.User;
import com.example.jojinwoo.learn2caltest.R;
import com.triggertrap.seekarc.SeekArc;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.jojinwoo.learn2caltest.Activity.QuizActivity.NUM_OF_QUIZ;
import static com.example.jojinwoo.learn2caltest.Activity.QuizActivity.currentPage;

/**
 * Created by jojinwoo on 2018-02-13.
 */

public class FragmentEstimate extends Fragment {

    private DataManager dataManager;

    SeekArc mSeekBar;
    TextView mValue;
    Button mCheckBtn;

    String time_start;
    String time_end;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_estimate, container, false);
        time_start = getCurrentTime();

        dataManager = DataManager.getInstance(getActivity());

        mSeekBar = (SeekArc) root.findViewById(R.id.seekBar);
        mValue = (TextView) root.findViewById(R.id.current_value);
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
        mCheckBtn = (Button) root.findViewById(R.id.btnCheck);
        mCheckBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                time_end = getCurrentTime();
                int estimate = mSeekBar.getProgress();
                setEstimate(estimate);
                makeQuery(estimate);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction tr = fm.beginTransaction();
                FragmentScore fs = new FragmentScore();
                tr.replace(R.id.frame, fs, "score");
                tr.commit();

            }
        });

        return root;
    }

    private void setEstimate(int estimate) {
        dataManager.getImageData(currentPage).submitAnswer(estimate);
        time_end = getCurrentTime();
    }

    private void makeQuery(int estimate) {

        SharedPreferences preferences = dataManager.getSharedPreferences(getActivity());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&uid="+ User.UID.getValue());
        stringBuilder.append("&iid="+ dataManager.getImageData(currentPage).getImageId());
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

    public String getCurrentTime() {
        return new SimpleDateFormat("yyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

}