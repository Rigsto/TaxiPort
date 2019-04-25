package com.aurigaaristo.taxiportfinal.mainfragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aurigaaristo.taxiportfinal.R;
import com.aurigaaristo.taxiportfinal.adapter.HistoryAdapter;
import com.aurigaaristo.taxiportfinal.entity.Order;
import com.aurigaaristo.taxiportfinal.loader.HistoryLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<HashMap<String, Object>>{
    private RecyclerView rvOrders;
    private ProgressBar pbOrders;
    private HashMap<String, Object> data;
    private HistoryAdapter adapter;
    private TextView tvNoData;

    public HistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvOrders = view.findViewById(R.id.rv_history);
        pbOrders = view.findViewById(R.id.pb_history);
        tvNoData = view.findViewById(R.id.tv_history_nodata);

        getLoaderManager().initLoader(0, savedInstanceState, this);
    }

    @Override
    public Loader<HashMap<String, Object>> onCreateLoader(int i, @Nullable Bundle bundle) {
        rvOrders.setVisibility(View.INVISIBLE);
        tvNoData.setVisibility(View.INVISIBLE);
        pbOrders.setVisibility(View.VISIBLE);

        return new HistoryLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<HashMap<String, Object>> loader, HashMap<String, Object> orders) {
        this.data = orders;
        processData();
    }

    @SuppressWarnings("unchecked")
    private void processData(){
        int total = (int) data.get("total");
        int code =  (int) data.get("code");
        if (code == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.information));
            builder.setMessage(getString(R.string.connection_failed));
            builder.setCancelable(false);
            builder.setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //reload
                }
            });
        } else if (code == 1){
            ArrayList<Order> history;
            if (total > 0){
                history = (ArrayList<Order>) data.get("orders");
                loadHistory(history);
            }
        }

        pbOrders.setVisibility(View.GONE);
        if (total > 0){
            rvOrders.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void loadHistory(ArrayList<Order> order){
        rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HistoryAdapter(getActivity());
        adapter.setData(order);
        adapter.notifyDataSetChanged();
        rvOrders.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<HashMap<String, Object>> loader) {
        adapter.setData(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(0, null, this);
    }
}
