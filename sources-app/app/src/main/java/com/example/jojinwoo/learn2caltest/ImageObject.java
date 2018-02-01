package com.example.jojinwoo.learn2caltest;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by jojinwoo on 2018-01-17.
 */

public class ImageObject
{
    private ArrayList<String> filenames;
    private ArrayList<Integer> labels;
    private ArrayList<Integer> estimates;
    private ArrayList<Integer> estResult;
    private ArrayList<Integer> errors;

    public ImageObject(Context context)
    {
        filenames = new ArrayList();
        labels = new ArrayList();
        estimates = new ArrayList();
        estResult = new ArrayList();
        errors = new ArrayList<>();

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
                estResult.add(-1);
                errors.add(-1);
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
    public void setEstimates(int idx, int value) { estimates.set(idx, value); }

    public ArrayList<Integer> getEstResult() {
        return estResult;
    }
    public void setEstimateResult(int idx, int value) { estResult.set(idx, value); }

    public void setError (int idx) { errors.set(idx, getLabel(idx) - getEstimate(idx)); }
    public ArrayList<Integer> getErrors() { return errors; }

}