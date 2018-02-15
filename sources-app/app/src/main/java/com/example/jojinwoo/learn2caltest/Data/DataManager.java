package com.example.jojinwoo.learn2caltest.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.example.jojinwoo.learn2caltest.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

/**
 * Created by jojinwoo on 2018-02-10.
 */

public class DataManager {

    private static DataManager instance;
    private static int currentIdx;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private ArrayList<Image> imgObj;

    public DataManager(Context context) {
        this.currentIdx = 0;
        this.imgObj = new ArrayList<Image>();
        this.loadImages(context);
        this.shuffleImages();
        this.getSharedPreferences(context);
        this.getEditor();
        this.initUserInfo(context);
    }

    public static DataManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataManager(context.getApplicationContext());
        }

        return instance;
    }

    public static SharedPreferences getSharedPreferences(Context context)
    {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        }

        return sharedPreferences;
    }

    public static SharedPreferences.Editor getEditor()
    {
        if (editor == null) {
            editor = sharedPreferences.edit();
        }

        return editor;
    }

    public void loadImages(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.list);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
                int i = Integer.parseInt(arr[0].replace(".jpg","")); // image id
                int c = Integer.parseInt(arr[1]); // calorie information
                Image newImage = new Image(i, c);
                imgObj.add(newImage);
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void shuffleImages() {
        long seed = System.nanoTime();
        Collections.shuffle(imgObj, new Random(seed));
    }

    public Image getImageData (int index) {
        return imgObj.get(index);
    }

    public void initUserInfo(Context context) {

        for (User type : User.values())
        {
            String temp = sharedPreferences.getString(type.toString(), null);
            if (temp != null) {
                type.setValue(temp);
                continue;
            }

            String value = null;

            switch (type)
            {
                case UID:
                    value = UUID.randomUUID().toString();
                    break;
                case LOCATION:
                    value = getLocation(context);
                    break;
                case DEVICE_NAME:
                    value = "ANDROID";
                    break;
                case SCREEN_WIDTH:
                    value = getScreenDimension(context, 1);
                    break;
                case SCREEN_HEIGHT:
                    value = getScreenDimension(context, 2);
                    break;
                case SCREEN_INCH:
                    value = getScreenDimension(context, 3);
                    break;
            }

            type.setValue(value);
            editor.putString(type.toString(), value);
            editor.commit();

        }

        SendQuery task = new SendQuery();
        String postParameter = "&user_id=" + User.UID.getValue();
        task.execute(postParameter, "mInitUser.php");
    }

    private static String getScreenDimension(Context context, int value) {

        int width= context.getResources().getDisplayMetrics().widthPixels;
        int height= context.getResources().getDisplayMetrics().heightPixels;
        int dens = context.getResources().getDisplayMetrics().densityDpi;
        double wi = (double) width / (double) dens;
        double hi = (double) height / (double) dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);

        String[] screenInformation = new String[3];
        screenInformation[0] = String.valueOf(width);
        screenInformation[1] = String.valueOf(height);
        screenInformation[2] = String.format("%.2f", screenInches);

        if (value == 1)
            return screenInformation[0];
        else if (value == 2)
            return screenInformation[1];
        else
            return screenInformation[2];
    }

    private static String getLocation(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        return countryCodeValue;
    }

    private static String getDeviceName() {
        String reqString = Build.MANUFACTURER + "_" + Build.MODEL;
        return reqString;
    }

}