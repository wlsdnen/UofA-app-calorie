package com.example.jojinwoo.learn2caltest;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/**
 * Created by jojinwoo on 2018-01-17.
 */

public class ImageObject
{
    private ArrayList<String> filenames;
    private ArrayList<Integer> labels;
    private ArrayList<Integer> estimates;
    private ArrayList<Integer> errors;
    private ArrayList<Integer> result;
    public static ArrayList<String> savingSlot;

    public ImageObject(Context context)
    {
        filenames = new ArrayList<String>();
        labels = new ArrayList<Integer>();
        estimates = new ArrayList<Integer>();
        errors = new ArrayList<Integer>();
        result = new ArrayList<Integer>();
        savingSlot = new ArrayList<String>();

        InputStream is = context.getResources().openRawResource(R.raw.list);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try
        {
            while ((line = br.readLine()) != null)
            {
                String[] arr = line.split(",");
                filenames.add(("img" + arr[0]).replace(".jpg",""));
                labels.add(Integer.parseInt(arr[1]));
                estimates.add(-1);
                errors.add(-1);
                result.add(-1);
                Log.d("File reading: ", line);
            }

            long seed = System.nanoTime();
            Collections.shuffle(filenames, new Random(seed));
            Collections.shuffle(labels, new Random(seed));
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public ArrayList<Integer> getLabels() {
        return labels;
    }
    public int getLabel(int idx) { return labels.get(idx); }

    public ArrayList<String> getFilenames() {
        return filenames;
    }
    public String getFilename(int idx) { return filenames.get(idx); }

    public ArrayList<Integer> getEstimates() {
        return estimates;
    }
    public int getEstimate(int idx) { return estimates.get(idx); }
    public void setEstimates(int idx, int value)
    {
        estimates.set(idx, value);
        setError(idx);
        setResult(idx);
    }

    public ArrayList<Integer> getErrors() {
        return errors;
    }
    private void setError(int idx)
    {
        int error = getLabel(idx) - getEstimate(idx);
        errors.set(idx, error);
    }

    public void setResult(int idx)
    {
        int actual = getLabel(idx);
        int estimate = getEstimate(idx);

        int state = -1;
        double tolerance = 0;

        // Accuracy
        if(actual < 100) { tolerance = 0.0005 * pow(actual, 2) + 0.1 * actual + 5; }
        else { tolerance = 0.2 * actual; }

        if ((abs(actual - estimate) > tolerance) && ((actual - estimate) < 0)) { state = 1; }
        else if ((abs(actual - estimate) > tolerance) && ((actual - estimate) > 0)) { state = -1; }
        else { state = 0; }

        result.set(idx, state);
    }

    public ArrayList<Integer> getResult() {
        return result;
    }

}