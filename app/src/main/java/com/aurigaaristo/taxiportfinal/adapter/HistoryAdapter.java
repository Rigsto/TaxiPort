package com.aurigaaristo.taxiportfinal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aurigaaristo.taxiportfinal.R;
import com.aurigaaristo.taxiportfinal.entity.Order;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.CategoryViewHolder> {
    private Context context;
    private ArrayList<Order> data;

    public ArrayList<Order> getData(){
        return data;
    }

    public void setData(ArrayList<Order> data) {
        this.data = data;
    }

    public HistoryAdapter(Context context){
        this.context = context;
    }

    @Override
    public HistoryAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_history, viewGroup, false);
        return new HistoryAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.CategoryViewHolder categoryViewHolder, int i) {
        String place1 = getData().get(i).getDestination();
        String place2 = getData().get(i).getUrban() + ", " + getData().get(i).getCity();
        String time = getData().get(i).getDate();

        categoryViewHolder.tvPlace.setText(place1);
        categoryViewHolder.tvUrban.setText(place2);
        categoryViewHolder.tvTime.setText(time);

        Picasso.with(context).load("https://liseia.org/wp-content/uploads/2018/04/no-photo-male.jpg").into(categoryViewHolder.imgOrder);
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPlace;
        private TextView tvTime;
        private TextView tvUrban;
        private ImageView imgOrder;

        public CategoryViewHolder(View view){
            super(view);
            tvPlace = view.findViewById(R.id.tv_history_place);
            tvTime = view.findViewById(R.id.tv_history_time);
            tvUrban = view.findViewById(R.id.tv_history_urban_district);
            imgOrder = view.findViewById(R.id.img_order);
        }
    }
}
