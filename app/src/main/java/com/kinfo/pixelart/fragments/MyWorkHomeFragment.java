package com.kinfo.pixelart.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kinfo.pixelart.R;
import com.kinfo.pixelart.tabs.mywork.MyWorkFrag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kinfo on 4/13/2018.
 */

public class MyWorkHomeFragment extends Fragment {

    ViewGroup rootView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.mywork_black,
            R.drawable.favourite_black};
    View headerView;
    LinearLayout linearLayoutWork,linearLayoutFavorite;
    ImageView imageMyWork,imageFavorite;
    TextView tvWork,tvFavorite;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.mywork, container, false);

        viewPager =  rootView.findViewById(R.id.viewpagerHome);

        tabLayout =  rootView.findViewById(R.id.tabs);
        setupViewPager(viewPager);

        return rootView;
    }

    private void setupTabIcons() {

         headerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_tab, null, false);

         linearLayoutWork =  headerView.findViewById(R.id.work);
         linearLayoutFavorite=  headerView.findViewById(R.id.fav);

        imageMyWork = headerView.findViewById(R.id.imageMyWork);
        imageFavorite = headerView.findViewById(R.id.imageFavorite);

        tvWork = headerView.findViewById(R.id.tvWork);
        tvFavorite = headerView.findViewById(R.id.tvFavorite);


        tabLayout.getTabAt(0).setCustomView(linearLayoutWork);
        tabLayout.getTabAt(1).setCustomView(linearLayoutFavorite);

    }

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFrag(new MyWorkFrag(), "My Work");
        adapter.addFrag(new FavouriteFragment(), "Favourite");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0){

                    imageMyWork.setImageResource(R.drawable.mywork_black);
                    tvWork.setTextColor(getResources().getColor(R.color.black_color));
                    imageFavorite.setImageResource(R.drawable.favourite_grey);
                    tvFavorite.setTextColor(getResources().getColor(R.color.light_black));


                }
                if(tab.getPosition() == 1){
                    imageFavorite.setImageResource(R.drawable.favourite_black);
                    tvFavorite.setTextColor(getResources().getColor(R.color.black_color));

                    imageMyWork.setImageResource(R.drawable.mywork_grey);
                    tvWork.setTextColor(getResources().getColor(R.color.light_black));

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {



            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
/*
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }*/
}

