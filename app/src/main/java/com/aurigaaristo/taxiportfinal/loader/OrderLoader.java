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

public class OrderLoader extends AsyncTaskLoader<HashMap<String, Object>> {
    private HashMap<String, Object> item;
    private boolean result = false;
    private Pref pref;

    public OrderLoader(final Context context) {
        super(context);
        pref = new Pref(context);
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(item);
        }
    }

    @Override
    public void deliverResult(@Nullable HashMap<String, Object> data) {
        super.deliverResult(data);
        this.item = data;
        result = true;
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (result) {
            result = false;
            item = null;
        }
    }

    @Override
    public HashMap<String, Object> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final HashMap<String, Object> data = new HashMap<>();
        //int code, int total, boolean taken, arraylist orders

        String url = BuildConfig.PHP_LOADER + "order.php";
        RequestParams param = new RequestParams();
        param.put("email", pref.getEmailPreference());

        client.get(url, param, new AsyncHttpResponseHandler() {
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

                    try {
                        JSONObject object = responseObject.getJSONObject("taken");
                        Order taken = new Order();
                        taken.setId(object.getString("id_order"));
                        taken.setName(object.getString("nama"));
                        taken.setDestination(object.getString("tujuan"));
                        taken.setDate(object.getString("time"));
                        taken.setCity(object.getString("kota"));
                        taken.setDistrict(object.getString("kecamatan"));
                        taken.setPassenger(object.getString("penumpang"));
                        taken.setUrban(object.getString("kelurahan"));

                        data.put("taken", true);
                        orders.add(taken);
                    } catch (Exception e) {
                        data.put("taken", false);
                    }

                    if (total > 0) {
                        JSONArray results = responseObject.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject object = results.getJSONObject(i);
                            Order order = new Order();
                            order.setId(object.getString("id_order"));
                            order.setName(object.getString("nama"));
                            order.setDestination(object.getString("tujuan"));
                            order.setDate(object.getString("time"));
                            order.setCity(object.getString("kota"));
                            order.setDistrict(object.getString("kecamatan"));
                            order.setPassenger(object.getString("penumpang"));
                            order.setUrban(object.getString("kelurahan"));
                            orders.add(order);
                        }
                    }

                    data.put("orders", orders);

                } catch (Exception e) {
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
