package com.aurigaaristo.taxiportfinal.clicksupport;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aurigaaristo.taxiportfinal.R;

public class ItemClickSupport {
    private final RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;

    private ItemClickSupport(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
        this.recyclerView.setTag(R.id.item_click_support, this);
        this.recyclerView.addOnChildAttachStateChangeListener(attachListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onItemClickListener != null){
                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(v);
                onItemClickListener.onItemClicked(recyclerView, holder.getAdapterPosition(), v);
            }
        }
    };

    private RecyclerView.OnChildAttachStateChangeListener attachListener = new RecyclerView.OnChildAttachStateChangeListener(){
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {
            if (onItemClickListener != null){
                view.setOnClickListener(onClickListener);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(@NonNull View view) {

        }
    };

    public static ItemClickSupport addTo(RecyclerView recyclerView){
        ItemClickSupport support = (ItemClickSupport) recyclerView.getTag(R.id.item_click_support);
        if (support == null){
            support = new ItemClickSupport(recyclerView);
        }
        return support;
    }

    public ItemClickSupport setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
        return this;
    }

    private void detach(RecyclerView view){
        view.removeOnChildAttachStateChangeListener(attachListener);
        view.setTag(R.id.item_click_support, null);
    }

    public interface OnItemClickListener {
        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }
}
