package com.kinfo.pixelart.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kinfo.pixelart.R;
import com.kinfo.pixelart.adapter.FavoriteAdapter;
import com.kinfo.pixelart.model.ImagesData;
import com.varunest.sparkbutton.SparkButton;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {


    private View view;
    private ArrayList<ImagesData> imagesDataList;
    private FavoriteAdapter favoriteAdapter;
    private RecyclerView favRecyclerview;
    private RelativeLayout empty_view;
    private SparkButton sparkButton;
    private ImageView handImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.layout_favourite, container, false);


        favRecyclerview  = view.findViewById(R.id.favRecyclerview);
        empty_view  = view.findViewById(R.id.empty_view);


        imagesDataList = fill_with_data();


        sparkButton =  view.findViewById(R.id.star_button2);
        handImage =  view.findViewById(R.id.handImage);




        final TranslateAnimation animation = new TranslateAnimation(50, 0, 500, 0);
        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());

        handImage.startAnimation(animation);

        favoriteAdapter=new FavoriteAdapter(getActivity(), imagesDataList);

        LinearLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        favRecyclerview.setLayoutManager(gridLayoutManager);
        favRecyclerview.setAdapter(favoriteAdapter);


        if (imagesDataList.isEmpty()) {
            favRecyclerview.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
        }
        else {
            favRecyclerview.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
        }

        return view;

    }



    private ArrayList<ImagesData> fill_with_data() {
        ArrayList<ImagesData> data = new ArrayList<>();

       /* data.add(new ImagesData(R.drawable.cat));
        data.add(new ImagesData(R.drawable.cartoon));
*/
        return data;
    }


    private class MyAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            sparkButton.playAnimation();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
