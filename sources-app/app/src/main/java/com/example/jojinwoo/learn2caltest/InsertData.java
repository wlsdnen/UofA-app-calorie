package com.example.jojinwoo.learn2caltest;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by jojinwoo on 2018-01-31.
 */

public class InsertData extends AsyncTask<String, Void, String> {

    private static final String serverURL = "http://jwdrive.myqnapcloud.com/";

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.d(TAG, "POST response  - " + result);
    }

    @Override
    protected String doInBackground(String... params) {

        String postParameters = params[0];
        String postAddress = params[1];

        try
        {
            URL url = new URL(serverURL+postAddress);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            //httpURLConnection.setRequestProperty("content-type", "application/json");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "POST response code - " + responseStatusCode);

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }

            bufferedReader.close();

            return sb.toString();

        }
        catch (Exception e)
        {
            Log.d(TAG, "InsertData: Error ", e);
            return new String("Error: " + e.getMessage());
        }

    }


}