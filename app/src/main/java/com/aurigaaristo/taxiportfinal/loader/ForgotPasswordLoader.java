package com.aurigaaristo.taxiportfinal.loader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.aurigaaristo.taxiportfinal.BuildConfig;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ForgotPasswordLoader extends AsyncTaskLoader<Integer> {
    private int code = 0;

    private String email, pass;

    public ForgotPasswordLoader(final Context context, String email, String password){
        super(context);
        this.email = email;
        this.pass = password;

        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(code);
        }
    }

    @Override
    public void deliverResult(@Nullable Integer data) {
        super.deliverResult(data);
        if (data != null) {
            this.code = data;
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (code != 0) {
            code = 0;
        }
    }

//    code :
//    0 = send data failed
//    2 = wrong password or username
//    1 = correct

    @Override
    public Integer loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();
        String url = BuildConfig.PHP_LOADER + "password.php";

        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", pass);

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String hasil = new String(responseBody);
                    JSONObject responseObject = new JSONObject(hasil);

                    code = responseObject.getInt("code");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                code = 0;
            }
        });

        return code;
    }
}
