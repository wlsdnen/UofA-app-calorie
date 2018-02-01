package com.example.jojinwoo.learn2caltest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import static com.example.jojinwoo.learn2caltest.MainActivity.PREF_READ_WRITE_MODE;

/**
 * Created by jojinwoo on 2018-01-07.
 */

public class Demographic extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener
{
    private static final int FT_MIN = 4;
    private static final int FT_MAX = 7;
    private static final int IN_MIN = 0;
    private static final int IN_MAX = 11;
    private static final int CM_MIN = 120;
    private static final int CM_MAX = 220;
    private static final int LBS_MIN = 34;
    private static final int LBS_MAX = 770;
    private static final int KG_MIN = 15;
    private static final int KG_MAX = 350;

    int w_state, h_state;
    ArrayAdapter<CharSequence> adspin, adspin2, adspin3;
    Button btnSubmit;
    Spinner spin, spin2, spin3;
    EditText et1, et2;
    RadioGroup rg;
    int lbs, kg, cm, ft, in;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographic);

        spin = (Spinner)findViewById(R.id.spinner);
        spin2 = (Spinner)findViewById(R.id.spinner2);
        spin3 = (Spinner)findViewById(R.id.spinner3);

        adspin = ArrayAdapter.createFromResource(this, R.array.ages, android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adspin);
        spin.setOnItemSelectedListener(this);

        adspin2 = ArrayAdapter.createFromResource(this, R.array.height, android.R.layout.simple_spinner_item);
        adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(adspin2);
        spin2.setOnItemSelectedListener(this);

        adspin3 = ArrayAdapter.createFromResource(this, R.array.weight, android.R.layout.simple_spinner_item);
        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin3.setAdapter(adspin3);
        spin3.setOnItemSelectedListener(this);

        spin.setSelection(3);
        et1 = (EditText)findViewById(R.id.et_height);
        et2 = (EditText)findViewById(R.id.et_weight);
        et1.setOnClickListener(this);
        et2.setOnClickListener(this);

        rg = (RadioGroup)findViewById(R.id.radioGender);

        btnSubmit = (Button)findViewById(R.id.submit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {
        switch (parent.getId())
        {
            case R.id.spinner:
                break;

            case R.id.spinner2:
                h_state = position;
                Log.e("h_state changed: ", Integer.toString(h_state));
                break;

            case R.id.spinner3:
                w_state = position;
                Log.e("w_state changed: ", Integer.toString(w_state));
                break;
        }
        // 성별, 키 그리고 체중 선택했을 때 어떻게 데이터 처리할 것인지 추가.
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) { }

    @Override
    public void onClick(View v)
    {

        Log.d("DEMO-",Integer.toString(v.getId()));

        switch (v.getId())
        {
            case R.id.et_height:
                if (h_state == 0) setDialog(R.layout.height_ftin);
                else if (h_state == 1) setDialog(R.layout.height_cm);
                break;
            case R.id.et_weight:
                if (w_state == 0) setDialog(R.layout.weight_lbs);
                else if (w_state == 1) setDialog(R.layout.weight_kg);
                break;
            case R.id.submit:
                submitClicked();
                break;
        }
    }

    public void submitClicked()
    {
        // 1. 모두 입력 했는지 체크 - yet
        // 2. demographic 전송 - done
        // 3. 현재 flow에 맞는 다음 페이지 선택 - done
        SharedPreferences preferences = getSharedPreferences("pref", PREF_READ_WRITE_MODE);
        SharedPreferences.Editor edit = preferences.edit();

        StringBuilder stringBuilder = new StringBuilder();
        edit.putBoolean("demographic", true);
        edit.commit();

        // User ID
        String user_id = preferences.getString("user_id", null);
        stringBuilder.append("&uid=" + user_id);

        // Gender
        RadioButton rb = (RadioButton)findViewById(rg.getCheckedRadioButtonId());
        String gender = rb.getText().toString();
        stringBuilder.append("&gender=" + gender);

        // Age
        String age = spin.getSelectedItem().toString();
        stringBuilder.append("&age="+age);

        // Height
        String height = et1.getText().toString();
        stringBuilder.append("&height=" + height);

        // Weight
        String weight = et2.getText().toString();
        stringBuilder.append("&weight=" + weight);

        InsertData task = new InsertData();
        task.execute(stringBuilder.toString(), "mUpdateDemographic.php");

        String next = preferences.getString("NextFinalPage", null);

        if (next.equals("PLAY"))
            startActivity(new Intent(this, Quiz.class));

        else if (next.equals("RESULT"))
            startActivity(new Intent(this, MainActivity.class));

    }

    public void setDialog(int id)
    {
        final ConstraintLayout constraintLayout = (ConstraintLayout) View.inflate(this, id, null);

        String str = "";
        StringBuilder output = new StringBuilder();

        switch(id) {
            case R.layout.height_ftin:
                final NumberPicker np_ft = (NumberPicker) constraintLayout.findViewById(R.id.numberPickerFeet);
                final NumberPicker np_in = (NumberPicker) constraintLayout.findViewById(R.id.numberPickerInch);
                str = "Height(ft,in)";
                np_ft.setMinValue(FT_MIN);
                np_ft.setMaxValue(FT_MAX);
                np_ft.setValue(4);
                np_ft.setWrapSelectorWheel(false);

                np_in.setMinValue(IN_MIN);
                np_in.setMaxValue(IN_MAX);
                np_in.setValue(0);
                np_in.setWrapSelectorWheel(true);

                new AlertDialog.Builder(this)
                        .setTitle(str)
                        .setView(constraintLayout)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ft = np_ft.getValue();
                                in = np_in.getValue();
                                et1.setText(Integer.toString(ft)+ " " + Integer.toString(in));
                            }
                        })
                        .show();
                break;

            case R.layout.height_cm:
                str = "Height(cm)";
                final NumberPicker np_cm = (NumberPicker) constraintLayout.findViewById(R.id.numberPickerCM);
                np_cm.setMinValue(CM_MIN);
                np_cm.setMaxValue(CM_MAX);
                np_cm.setValue(180);
                np_cm.setWrapSelectorWheel(true);

                new AlertDialog.Builder(this)
                        .setTitle(str)
                        .setView(constraintLayout)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cm = np_cm.getValue();
                                et1.setText(Integer.toString(cm));
                            }
                        })
                        .show();
                break;
            case R.layout.weight_lbs:
                str = "Weight(lbs)";
                final NumberPicker np_lbs = (NumberPicker) constraintLayout.findViewById(R.id.numberPickerLBS);
                np_lbs.setMinValue(LBS_MIN);
                np_lbs.setMaxValue(LBS_MAX);
                np_lbs.setValue(130);
                np_lbs.setWrapSelectorWheel(true);

                new AlertDialog.Builder(this)
                        .setTitle(str)
                        .setView(constraintLayout)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                lbs = np_lbs.getValue();
                                et2.setText(Integer.toString(lbs));
                            }
                        })
                        .show();
                break;

            case R.layout.weight_kg:
                str = "Weight(kg)";
                final NumberPicker np_kg = (NumberPicker)constraintLayout.findViewById(R.id.numberPickerKG);
                np_kg.setMinValue(KG_MIN); np_kg.setMaxValue(KG_MAX); np_kg.setValue(60);
                np_kg.setWrapSelectorWheel(true);

                new AlertDialog.Builder(this)
                        .setTitle(str)
                        .setView(constraintLayout)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                kg = np_kg.getValue();
                                et2.setText(Integer.toString(kg));
                            }
                        })
                        .show();
                break;
        }

    }

}