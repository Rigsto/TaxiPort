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

public class LoginLoader extends AsyncTaskLoader<Integer> {
    private int code = 0;

    private String email, password;
    private Pref pref;

    public LoginLoader(final Context context, String email, String password){
        super(context);
        this.email = email;
        this.password = password;

        pref = new Pref(context);

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
        String url = BuildConfig.PHP_LOADER + "loginapp.php";

        RequestParams param = new RequestParams();
        param.put("email", email);
        param.put("password", password);

        client.post(url, param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String hasil = new String(responseBody);
                    JSONObject responseObject = new JSONObject(hasil);

                    code = responseObject.getInt("code");

                    if (code == 1){
                        JSONObject profile = responseObject.getJSONObject("profile");
                        pref.setEmailPreference(email);
                        pref.setPasswordPreference(password);
                        pref.setImagePreference(profile.getString("photo"));
                        pref.setNamePreference(profile.getString("nama"));
                        pref.setPhonePreference(profile.getString("phone"));
                    }
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
