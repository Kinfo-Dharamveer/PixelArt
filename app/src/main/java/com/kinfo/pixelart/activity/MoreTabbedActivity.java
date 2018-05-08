package com.kinfo.pixelart.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.kinfo.pixelart.AppConstants;
import com.kinfo.pixelart.R;
import com.kinfo.pixelart.fragments.AllFragment;
import com.kinfo.pixelart.fragments.AnimalFragment;

public class MoreTabbedActivity extends AppCompatActivity {



    private ViewPager viewPagerMoreAct;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_tabbed);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        viewPagerMoreAct= findViewById(R.id.viewPagerMoreAct);
        tabLayout= findViewById(R.id.tabsMoreActivity);


        tabLayout.addTab(tabLayout.newTab().setText("ALL"));
        tabLayout.addTab(tabLayout.newTab().setText("ANIMALS"));
        tabLayout.addTab(tabLayout.newTab().setText("FASHION"));
        tabLayout.addTab(tabLayout.newTab().setText("KIDS TIME"));
        tabLayout.addTab(tabLayout.newTab().setText("PLEASURE"));
        tabLayout.addTab(tabLayout.newTab().setText("FOOD"));
        tabLayout.addTab(tabLayout.newTab().setText("PEOPLE"));
        tabLayout.addTab(tabLayout.newTab().setText("GAME"));
        tabLayout.addTab(tabLayout.newTab().setText("HOLIDAY"));
        tabLayout.addTab(tabLayout.newTab().setText("NATURE"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        setupViewPager(viewPagerMoreAct);

        if(getIntent().getExtras() !=null){

            String ANIMALS = getIntent().getStringExtra(AppConstants.ANIMALS);
            String FASHION = getIntent().getStringExtra(AppConstants.FASHION);
            String KIDSTIME = getIntent().getStringExtra(AppConstants.KIDSTIME);
            String PLEASURE = getIntent().getStringExtra(AppConstants.PLEASURE);
            String FOOD = getIntent().getStringExtra(AppConstants.FOOD);
            String PEOPLE = getIntent().getStringExtra(AppConstants.PEOPLE);
            String GAME = getIntent().getStringExtra(AppConstants.GAME);
            String HOLIDAY = getIntent().getStringExtra(AppConstants.HOLIDAY);
            String NATURE = getIntent().getStringExtra(AppConstants.NATURE);



            if(ANIMALS!=null){

                if(ANIMALS.contains("ANIMALS")){

                    viewPagerMoreAct.setCurrentItem(1);

                }
            }

            if(FASHION!=null){

                if(FASHION.contains("FASHION")){
                    viewPagerMoreAct.setCurrentItem(2);

                }

            }

            if(KIDSTIME!=null){
                if(KIDSTIME.contains("KIDSTIME")){
                    viewPagerMoreAct.setCurrentItem(3);

                }
            }


            if(PLEASURE!=null) {
                if (PLEASURE.contains("PLEASURE")) {
                    viewPagerMoreAct.setCurrentItem(4);

                }
            }

            if(FOOD!=null){
                if(FOOD.contains("FOOD")){
                    viewPagerMoreAct.setCurrentItem(5);

                }
            }


            if(PEOPLE!=null){
                if(PEOPLE.contains("PEOPLE")){
                    viewPagerMoreAct.setCurrentItem(6);

                }
            }

            if(GAME!=null){
                if(GAME.contains("GAME")){
                    viewPagerMoreAct.setCurrentItem(7);

                }
            }

            if(HOLIDAY!=null) {
                if (HOLIDAY.contains("HOLIDAY")) {
                    viewPagerMoreAct.setCurrentItem(8);

                }
            }

            if(NATURE!=null){
                if(NATURE.contains("NATURE")){
                    viewPagerMoreAct.setCurrentItem(9);

                }

            }


        }





    }



    private void setupViewPager(final ViewPager viewPagerMoreAct) {

        ViewPagerMoreAdapter adapter = new ViewPagerMoreAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        viewPagerMoreAct.setAdapter(adapter);


        viewPagerMoreAct.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerMoreAct.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0){



                }
                if(tab.getPosition() == 1){

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:


                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private static class ViewPagerMoreAdapter extends FragmentStatePagerAdapter {


        int mNumOfTabs;

        public ViewPagerMoreAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    AllFragment allFragment = new AllFragment();
                    return allFragment;
                case 1:
                    AnimalFragment animalFragment = new AnimalFragment();
                    return animalFragment;

                case 2:
                    AllFragment allFragment2 = new AllFragment();
                    return allFragment2;

                case 3:
                    AllFragment allFragmen3 = new AllFragment();
                    return allFragmen3;
                case 4:
                    AllFragment allFragment4 = new AllFragment();
                    return allFragment4;
                case 5:
                    AllFragment allFragment5 = new AllFragment();
                    return allFragment5;
                case 6:
                    AllFragment allFragment6 = new AllFragment();
                    return allFragment6;
                case 7:
                    AllFragment allFragment7 = new AllFragment();
                    return allFragment7;
                case 8:
                    AllFragment allFragment8 = new AllFragment();
                    return allFragment8;
                case 9:
                    AllFragment allFragment9 = new AllFragment();
                    return allFragment9;


                default:
                    AllFragment allFragment10 = new AllFragment();
                    return allFragment10;

            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

    }
}
