package com.aurigaaristo.taxiportfinal.orderprocessloader;

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

public class ArriveLoader extends AsyncTaskLoader<Integer> {
    private String idOrder, email;
    private int code = 0;

    private Pref pref;

    public ArriveLoader(final Context context, String id){
        super(context);
        this.idOrder = id;

        pref = new Pref(context);
        email = pref.getEmailPreference();
    }

//    code :
//    0 = failed
//    1 = success

    @Nullable
    @Override
    public Integer loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();
        String url = BuildConfig.PHP_LOADER + "arrived.php";

        RequestParams params = new RequestParams();
        params.put("idOrder", idOrder);
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
