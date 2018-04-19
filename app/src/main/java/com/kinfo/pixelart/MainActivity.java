package com.kinfo.pixelart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kinfo.pixelart.tabs.gallery.GalleryFragment;
import com.kinfo.pixelart.tabs.home.ColorByNo;
import com.kinfo.pixelart.tabs.home.HomeFragment;
import com.kinfo.pixelart.tabs.mywork.MyWorkFragment;
import com.kinfo.pixelart.utils.CustomTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        context = MainActivity.this;
       // get view ids
        findIds();


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
    }

    public void findIds(){

        viewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void setupTabIcons() {
        Log.e("setupTabIcons","setupTabIcons");
        tabLayout.getTabAt(0).setCustomView(R.layout.home_tab);
        tabLayout.getTabAt(1).setCustomView(R.layout.gallery_tab);
        tabLayout.getTabAt(2).setCustomView(R.layout.mywork_tab);
    }

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "");
        adapter.addFragment(new GalleryFragment(), "");
        adapter.addFragment(new MyWorkFragment(), "");
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                viewPager.setCurrentItem(tab.getPosition());
                View view=tab.getCustomView();
                ImageView image= (ImageView) view.findViewById(R.id.image);

                if(tab.getPosition()==0){
                    image.setImageResource(R.drawable.home_icon);
                }else if(tab.getPosition()==1){
                    image.setImageResource(R.drawable.gallery_icon);
                }else if(tab.getPosition()==2){
                    image.setImageResource(R.drawable.mywork_icon);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                View view=tab.getCustomView();
                ImageView image= (ImageView) view.findViewById(R.id.image);

                if(tab.getPosition()==0){
                    image.setImageResource(R.drawable.home_icon_grey);
                }else if(tab.getPosition()==1){
                    image.setImageResource(R.drawable.gallery_icon_grey);
                }else if(tab.getPosition()==2){
                    image.setImageResource(R.drawable.mywork_icon);
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                View view=tab.getCustomView();
                ImageView image= (ImageView) view.findViewById(R.id.image);

                if(tab.getPosition()==0){
                    image.setImageResource(R.drawable.home_icon);
                }else if(tab.getPosition()==1){
                    image.setImageResource(R.drawable.gallery_icon);
                }else if(tab.getPosition()==2){
                    image.setImageResource(R.drawable.mywork_icon);
                }
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

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.e("position",""+mFragmentTitleList.get(position));
            return mFragmentTitleList.get(position);
        }
    }

}
