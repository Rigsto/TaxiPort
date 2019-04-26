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

public class TakenAdapter extends RecyclerView.Adapter<TakenAdapter.CategoryViewHolder>{
    private Context context;
    private ArrayList<Order> data;

    public ArrayList<Order> getData(){
        return data;
    }

    public void setData(ArrayList<Order> data) {
        this.data = data;
    }

    public TakenAdapter(Context context){
        this.context = context;
    }

    @Override
    public TakenAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_taken, viewGroup, false);
        return new TakenAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TakenAdapter.CategoryViewHolder categoryViewHolder, int i) {
        String place = getData().get(i).getDestination();
        String time = getData().get(i).getDate();

        categoryViewHolder.tvPlace.setText(place);
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
        private ImageView imgOrder;

        public CategoryViewHolder(View view){
            super(view);
            tvPlace = view.findViewById(R.id.tv_taken_place);
            tvTime = view.findViewById(R.id.tv_taken_time);
            imgOrder = view.findViewById(R.id.img_taken);
        }
    }
}
