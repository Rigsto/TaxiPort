package com.aurigaaristo.taxiportfinal.loader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.aurigaaristo.taxiportfinal.BuildConfig;
import com.aurigaaristo.taxiportfinal.entity.Order;
import com.aurigaaristo.taxiportfinal.preference.Pref;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class HistoryLoader extends AsyncTaskLoader<HashMap<String, Object>> {
    private HashMap<String, Object> data;
    private boolean result = false;

    private Pref pref;

    public HistoryLoader(final Context context){
        super(context);
        onContentChanged();

        pref = new Pref(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (takeContentChanged()){
            forceLoad();
        } else {
            deliverResult(data);
        }
    }

    @Override
    public void deliverResult(@Nullable HashMap<String, Object> data) {
        super.deliverResult(data);
        this.data = data;
        result = true;
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (result){
            result = false;
            data = null;
        }
    }

    @Override
    public HashMap<String, Object> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final HashMap<String, Object> data = new HashMap<>();
        //int code, int total, arraylist orders

        String url = BuildConfig.PHP_LOADER + "historyapp.php";

        RequestParams params = new RequestParams();
        params.put("email", pref.getEmailPreference());

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String hasil = new String(responseBody);
                    JSONObject responseObject = new JSONObject(hasil);

                    ArrayList<Order> orders = new ArrayList<>();

                    data.put("code", 1);
                    int total = responseObject.getInt("total");
                    data.put("total", total);

                    if (total > 0) {
                        JSONArray results = responseObject.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject object = results.getJSONObject(i);
                            Order order = new Order();
                            order.setDestination(object.getString("tujuan"));
                            order.setDate(object.getString("time"));
                            order.setCity(object.getString("kota"));
                            order.setDistrict(object.getString("kecamatan"));
                            order.setUrban(object.getString("kelurahan"));
                            orders.add(order);
                        }
                    }

                    data.put("orders", orders);
                } catch (Exception e){
                    data.put("code", 0);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                data.put("code", 0);
            }
        });

        return data;
    }
}
