package com.kinfo.pixelart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kinfo.pixelart.R;
import com.kinfo.pixelart.model.ImagesData;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private ArrayList<ImagesData> imagesDataArrayList;
    private Context context;

    public FavoriteAdapter(Context context,ArrayList<ImagesData> imagesDataArrayList) {
        this.imagesDataArrayList = imagesDataArrayList;
        this.context = context;
    }

    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favorite_frag_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return imagesDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageFavorite;
        public ViewHolder(View view) {
            super(view);

            imageFavorite =  view.findViewById(R.id.imageFavorite);


        }
    }

}


