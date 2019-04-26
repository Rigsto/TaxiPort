package com.aurigaaristo.taxiportfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aurigaaristo.taxiportfinal.entity.Order;
import com.aurigaaristo.taxiportfinal.orderprocessloader.ArriveLoader;
import com.aurigaaristo.taxiportfinal.orderprocessloader.TakeOrderLoader;
import com.aurigaaristo.taxiportfinal.preference.Pref;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTime, tvId, tvName, tvDest, tvCity, tvDistrict, tvUrban, tvPassenger;
    private Button btnAccept, btnReject;
    private ProgressBar pbAccept;

    private Order order;
    private Intent intent;
    private int take;

    private boolean sendTake = false;
    private boolean sendArrive = false;

    private static final int TAKE_ORDER_ID = 1;
    private static final int ARRIVED_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTime = findViewById(R.id.tv_detail_time);
        tvId = findViewById(R.id.tv_detail_id);
        tvName = findViewById(R.id.tv_detail_name);
        tvDest = findViewById(R.id.tv_detail_destination);
        tvCity = findViewById(R.id.tv_detail_city);
        tvDistrict = findViewById(R.id.tv_detail_district);
        tvUrban = findViewById(R.id.tv_detail_urban);
        tvPassenger = findViewById(R.id.tv_detail_passengers);
        btnAccept = findViewById(R.id.btn_detail_accept);
        btnAccept.setOnClickListener(this);
        btnReject = findViewById(R.id.btn_detail_reject);
        btnReject.setOnClickListener(this);
        pbAccept = findViewById(R.id.pb_accept);
        pbAccept.setVisibility(View.GONE);

        if (getIntent() != null) {
            order = getIntent().getParcelableExtra("order");

            tvTime.setText(order.getDate());
            tvId.setText(order.getId());
            tvName.setText(order.getName());
            tvDest.setText(order.getDestination());
            tvCity.setText(order.getCity());
            tvDistrict.setText(order.getDistrict());
            tvUrban.setText(order.getUrban());
            tvPassenger.setText(order.getPassenger());

            take = getIntent().getIntExtra("take", 0);
            if (take == 0) {
                btnAccept.setText(getString(R.string.accept));
                btnReject.setText(getString(R.string.reject));
                btnReject.setBackground(getDrawable(R.drawable.button_red));
                btnReject.setTextColor(getResources().getColor(R.color.red));

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.order_details));
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            } else if (take == 1) {
                btnAccept.setText(getString(R.string.arrived));
                btnReject.setText(getString(R.string.return_to_airport));
                btnReject.setBackground(getDrawable(R.drawable.button_blue));
                btnReject.setTextColor(getResources().getColor(R.color.blue));

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.ongoing_orders));
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (take == 0) {
            switch (v.getId()) {
                case R.id.btn_detail_accept: //accept an order
                    pbAccept.setVisibility(View.VISIBLE);
                    btnAccept.setVisibility(View.GONE);

                    takeOrder(order);
                    break;
                case R.id.btn_detail_reject: //reject order
                    intent = new Intent(DetailActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        } else if (take == 1) {
            switch (v.getId()) {
                case R.id.btn_detail_accept: //arrived
                    pbAccept.setVisibility(View.VISIBLE);
                    btnAccept.setVisibility(View.GONE);

                    arrived(order);
                    break;
                case R.id.btn_detail_reject: //return to airport
                    intent = new Intent(DetailActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void takeOrder(Order order){
        String id = order.getId();

        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        if (sendTake) {
            getSupportLoaderManager().restartLoader(TAKE_ORDER_ID, bundle, takeOrderListener);
        } else {
            getSupportLoaderManager().initLoader(TAKE_ORDER_ID, bundle, takeOrderListener);
            sendTake = true;
        }
    }

    private LoaderManager.LoaderCallbacks<Integer> takeOrderListener = new LoaderManager.LoaderCallbacks<Integer>() {
        @NonNull
        @Override
        public Loader<Integer> onCreateLoader(int i, @Nullable Bundle bundle) {
            String id = "";
            if (bundle != null){
                id = bundle.getString("id");
            }

            return new TakeOrderLoader(getApplicationContext(), id);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Integer> loader, Integer code) {
            switch (code){
                case 0:
                    Toast.makeText(getApplicationContext(), getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                    btnAccept.setVisibility(View.VISIBLE);
                    pbAccept.setVisibility(View.GONE);
                    break;
                case 1:
                    intent = new Intent(DetailActivity.this, DetailActivity.class);
                    intent.putExtra("order", order);
                    intent.putExtra("take", 1);
                    startActivity(intent);
                    finish();
                    break;
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Integer> loader) {

        }
    };

    private void arrived(Order order){
        String id = order.getId();

        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        if (sendArrive) {
            getSupportLoaderManager().restartLoader(ARRIVED_ID, bundle, arrivedListener);
        } else {
            getSupportLoaderManager().initLoader(ARRIVED_ID, bundle, arrivedListener);
            sendArrive = true;
        }
    }

    private LoaderManager.LoaderCallbacks<Integer> arrivedListener = new LoaderManager.LoaderCallbacks<Integer>() {
        @Override
        public Loader<Integer> onCreateLoader(int i, @Nullable Bundle bundle) {
            String id = "";
            if (bundle != null){
                id = bundle.getString("id");
            }

            return new ArriveLoader(getApplicationContext(), id);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Integer> loader, Integer code) {
            switch (code){
                case 0:
                    Toast.makeText(getApplicationContext(), getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                    btnAccept.setVisibility(View.VISIBLE);
                    pbAccept.setVisibility(View.GONE);
                    break;
                case 1:
                    Pref pref = new Pref(getApplicationContext());
                    pref.setStatPreference(0);

                    intent = new Intent(DetailActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Integer> loader) {

        }
    };
}
