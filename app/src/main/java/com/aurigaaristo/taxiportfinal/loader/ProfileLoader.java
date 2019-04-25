package com.aurigaaristo.taxiportfinal.loader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.aurigaaristo.taxiportfinal.BuildConfig;
import com.aurigaaristo.taxiportfinal.preference.Pref;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileLoader extends AsyncTaskLoader<Integer> {
    private int code = 0;

    private String name, phone, email;
    private Pref pref;

    public ProfileLoader(final Context context, String name, String phone){
        super(context);
        this.name = name;
        this.phone = phone;

        pref = new Pref(context);
        this.email = pref.getEmailPreference();

        onContentChanged();
    }

//    code :
//    0 = failed
//    1 = success

    @Nullable
    @Override
    public Integer loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();
        String url = BuildConfig.PHP_LOADER + "editprofile.php";

        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("phone", phone);
        params.put("email", email);

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
