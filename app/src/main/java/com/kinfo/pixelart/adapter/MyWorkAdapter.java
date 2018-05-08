package com.kinfo.pixelart.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kinfo.pixelart.R;
import com.kinfo.pixelart.model.ImagesData;

import java.util.ArrayList;

public class MyWorkAdapter extends RecyclerView.Adapter<MyWorkAdapter.ViewHolder> {
    private ArrayList<ImagesData> imagesDataArrayList;
    private Context context;
    private ItemClickListener mClickListener;

    public MyWorkAdapter(Context context,ArrayList<ImagesData> imagesDataArrayList,ItemClickListener itemClickListener) {
        this.imagesDataArrayList = imagesDataArrayList;
        this.context = context;
        this.mClickListener  = itemClickListener;

    }

    @Override
    public MyWorkAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mywork_frag_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyWorkAdapter.ViewHolder viewHolder, int i) {


    }

    public void removeItem(int position) {
        imagesDataArrayList.remove(position);
        notifyItemRemoved(position);
    }


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    @Override
    public int getItemCount() {
        return imagesDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageMyWork;
        private CardView cardView;
        public ViewHolder(View view) {
            super(view);

            cardView =  view.findViewById(R.id.card_view);
            imageMyWork =  view.findViewById(R.id.imageMyWork);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

                }
            });

        }
    }

}

