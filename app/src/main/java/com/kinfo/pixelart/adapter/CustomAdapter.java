package com.kinfo.pixelart.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kinfo.pixelart.MainActivity;
import com.kinfo.pixelart.R;
import com.kinfo.pixelart.model.ImageModel;
import com.kinfo.pixelart.tabs.home.ColorByNo;

import java.util.ArrayList;

/**
 * Created by kinfo on 4/17/2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private ArrayList<ImageModel> list;
    private Context mContext;

    public CustomAdapter(ArrayList<ImageModel> Data, Context cxt) {
        list = Data;
        mContext = cxt;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        holder.sandbox_image.setImageResource(list.get(position).getImage());
        holder.sandbox_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("sandboxImage Clicked","sandboxImage Clicked");
                Intent intent = new Intent(mContext,ColorByNo.class);
                intent.putExtra("image",list.get(position).getImage());
                mContext.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView status_image,sandbox_image;

        public MyViewHolder(View v) {
            super(v);

            sandbox_image = (ImageView) v.findViewById(R.id.sandbox_image);
            status_image = (ImageView) v.findViewById(R.id.status_image);
        }
    }

    private void showFragment(final Fragment targetFragment, final String className) {
        ((MainActivity) mContext).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_main, targetFragment, className)
                //.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}
