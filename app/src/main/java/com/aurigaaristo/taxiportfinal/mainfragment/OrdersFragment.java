package com.aurigaaristo.taxiportfinal.mainfragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aurigaaristo.taxiportfinal.DetailActivity;
import com.aurigaaristo.taxiportfinal.R;
import com.aurigaaristo.taxiportfinal.adapter.OrderAdapter;
import com.aurigaaristo.taxiportfinal.adapter.TakenAdapter;
import com.aurigaaristo.taxiportfinal.clicksupport.ItemClickSupport;
import com.aurigaaristo.taxiportfinal.entity.Order;
import com.aurigaaristo.taxiportfinal.loader.OrderLoader;
import com.aurigaaristo.taxiportfinal.preference.Pref;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment implements LoaderManager.LoaderCallbacks<HashMap<String, Object>>, View.OnClickListener{
    private RecyclerView rvOrders, rvTaken;
    private ProgressBar pbOrders;
    private TextView tvNoData;
    private HashMap<String, Object> data;

    private Pref pref;
    private Button btnCheckIn;

    public OrdersFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvOrders = view.findViewById(R.id.rv_orders);
        rvTaken = view.findViewById(R.id.rv_taken);
        pbOrders = view.findViewById(R.id.pb_orders);
        tvNoData = view.findViewById(R.id.tv_order_nodata);
        btnCheckIn = view.findViewById(R.id.btn_check_in);
        btnCheckIn.setOnClickListener(this);

        pref = new Pref(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
		btnCheckIn.setVisibility(View.GONE);
        if (pref.getStatPreference() == 0){
            btnCheckIn.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.VISIBLE);
            pbOrders.setVisibility(View.GONE);
        } else {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @NonNull
    @Override
    public Loader<HashMap<String, Object>> onCreateLoader(int i, @Nullable Bundle bundle) {
        rvOrders.setVisibility(View.INVISIBLE);
        rvTaken.setVisibility(View.INVISIBLE);
        pbOrders.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.INVISIBLE);

        return new OrderLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<HashMap<String, Object>> loader, HashMap<String, Object> orders) {
        this.data = orders;
        pbOrders.setVisibility(View.INVISIBLE);
        processData();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<HashMap<String, Object>> loader) {

    }

    @SuppressWarnings("unchecked")
    private void processData(){
        boolean taken = false;
        try {
            taken = (boolean) data.get("taken");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } else if(code == 1){
            ArrayList<Order> orders;
            int j = 0;
            if (taken || total > 0){
                orders = (ArrayList<Order>) data.get("orders");

                if (taken){
                    ArrayList<Order> arrTaken = new ArrayList<>();
                    assert orders != null;
                    Order take = orders.get(0);
                    arrTaken.add(take);
                    loadTaken(arrTaken);
                    j = 1;
                }

                if (total > 0){
                    ArrayList<Order> arrOrder = new ArrayList<>();
                    assert orders != null;
                    for (int i = j; i < orders.size(); i++) {
                        arrOrder.add(orders.get(i));
                    }
                    loadOrder(arrOrder);
                }
            }

            pbOrders.setVisibility(View.GONE);
            if (taken){
                rvTaken.setVisibility(View.VISIBLE);
            }
            if (total > 0){
                rvOrders.setVisibility(View.VISIBLE);
            }
            if (!taken && total == 0){
                tvNoData.setVisibility(View.VISIBLE);
            }
        }
    }

    private void loadTaken(ArrayList<Order> arr){
        final ArrayList<Order> ta = arr;
        rvTaken.setLayoutManager(new LinearLayoutManager(getActivity()));
        TakenAdapter tAdapter = new TakenAdapter(getActivity());
        tAdapter.setData(ta);
        tAdapter.notifyDataSetChanged();
        rvTaken.setAdapter(tAdapter);

        ItemClickSupport.addTo(rvTaken).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Order order = ta.get(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("order", order);
                intent.putExtra("take", 1);
                startActivity(intent);
            }
        });

    }

    private void loadOrder(ArrayList<Order> arr) {
        final ArrayList<Order> or = arr;
        rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        OrderAdapter adapter = new OrderAdapter(getActivity());
        adapter.setData(or);
        adapter.notifyDataSetChanged();
        rvOrders.setAdapter(adapter);

        ItemClickSupport.addTo(rvOrders).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Order order = or.get(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("order", order);
                intent.putExtra("take", 0);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        btnCheckIn.setVisibility(View.GONE);
        if (pref.getStatPreference() == 0) {
            btnCheckIn.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.VISIBLE);
            pbOrders.setVisibility(View.GONE);
        } else {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_check_in){
            pref.setStatPreference(1);
            Toast.makeText(getContext(), getString(R.string.success_check_in), Toast.LENGTH_SHORT).show();

            onStart();
        }
    }
}
