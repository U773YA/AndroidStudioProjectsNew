package com.project.android.studentshelper.Networking;

import android.os.AsyncTask;

/**
 * Created by Utteya on 3/29/2018.
 */

public class HttpGetAsync extends AsyncTask<String,Void,String> {
    private String mUrl;
    private AsyncCallback mAsyncCallback;

    public HttpGetAsync(String url, AsyncCallback asyncCallback) {
        this.mUrl = url;
        this.mAsyncCallback = asyncCallback;
    }

    @Override
    protected String doInBackground(String... strings) {
        return JSONhelper.getJSON(mUrl);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mAsyncCallback!=null) mAsyncCallback.onStart();
    }

    @Override
    protected void onPostExecute(String jsonReceive) {
        super.onPostExecute(jsonReceive);
        if(mAsyncCallback!=null) mAsyncCallback.onFinish(jsonReceive);

    }
}
