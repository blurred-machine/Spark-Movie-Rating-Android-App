package com.example.paras.imdb;


import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FetchData extends AsyncTask<Void, Void, String> {

    // declaration of variables.
    private String mUrl;
    private Context mContext;
    private DataTransferInterface mDataTransferInterface;
    private View myViewProgress;
    private View myListDisplayView;

    // constructor of the FetchData class.
    public FetchData(Context context, String url, DataTransferInterface dataTransferInterface, View myListDisplayView, View myViewProgress){
        // setting internal variables of the class as the passed variables.
        mContext = context;
        mUrl = url;
        mDataTransferInterface = dataTransferInterface;
        this.myViewProgress = myViewProgress;
        this.myListDisplayView = myListDisplayView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        myListDisplayView.setVisibility(View.INVISIBLE);
        myViewProgress.setVisibility(View.VISIBLE);

    }

    // overridden method of the AsyncTask class to do the task in background.
    @Override
    protected String doInBackground(Void... voids) {

        // creating an object of the OkHttpClient.
        // dependency is added for this class in the build.gradle file.
        OkHttpClient okHttpClient = new OkHttpClient();
        // if the connection is unable to establish then wait for 2 minutes for it to connect and then timeout and end process.
        okHttpClient.setConnectTimeout(120, TimeUnit.SECONDS);
        // if reading of the URL is unable to establish then wait for 2 minutes for it to happen and then timeout and end process.
        okHttpClient.setReadTimeout(120, TimeUnit.SECONDS);
        // getting the request for the JSON URL.
        Request request = new Request.Builder().url(mUrl).build();
        String responseData = null;

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()){
                // response contains the protocol of the URL
                responseData = response.body().string();
            }
        } catch (IOException e) {
            //e.printStackTrace();
            //Toast.makeText(mContext, "Error: "+e, Toast.LENGTH_SHORT).show();
        }
        return responseData;
    }

// method to execute after the execution of response is completed.
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        // making the progressbar invisible and list view visible.
        myViewProgress.setVisibility(View.INVISIBLE);
        myListDisplayView.setVisibility(View.VISIBLE);

        // sending the response data to the interface created before.
        mDataTransferInterface.dataHere(response);
    }
}
