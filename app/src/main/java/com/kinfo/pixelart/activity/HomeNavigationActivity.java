package com.kinfo.pixelart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.kinfo.pixelart.R;
import com.kinfo.pixelart.dialogs.RateDialog;
import com.kinfo.pixelart.fragments.MyWorkHomeFragment;
import com.kinfo.pixelart.tabs.gallery.GalleryFragment;
import com.kinfo.pixelart.tabs.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar

        setContentView(R.layout.activity_main2);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager =  findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "HomeFragment");
        adapter.addFragment(new GalleryFragment(), "GalleryFragment");
        adapter.addFragment(new MyWorkHomeFragment(), "MyWorkHomeFragment");
        viewPager.setAdapter(adapter);

        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                viewPager.setCurrentItem(tab.getPosition());
                View view=tab.getCustomView();
                ImageView image=  view.findViewById(R.id.image);

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
                ImageView image=  view.findViewById(R.id.image);

                if(tab.getPosition()==0){
                    image.setImageResource(R.drawable.home_icon_grey);
                }else if(tab.getPosition()==1){
                    image.setImageResource(R.drawable.gallery_icon_grey);
                }else if(tab.getPosition()==2){
                    image.setImageResource(R.drawable.mywork_icon_grey);
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                View view=tab.getCustomView();
                ImageView image=  view.findViewById(R.id.image);

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

    private void setupTabIcons() {
        Log.e("setupTabIcons","setupTabIcons");
        tabLayout.getTabAt(0).setCustomView(R.layout.home_tab);
        tabLayout.getTabAt(1).setCustomView(R.layout.gallery_tab);
        tabLayout.getTabAt(2).setCustomView(R.layout.mywork_tab);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_star) {

            RateDialog rateDialogActivity = new RateDialog(this);
            rateDialogActivity.show();


        } else if (id == R.id.nav_feedback) {

            sendFeedback();

        } else if (id == R.id.nav_about) {


            startActivity(new Intent(this,AboutUsActivity.class));
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Start a new activity for sending a feedback email
    private void sendFeedback() {
        final Intent _Intent = new Intent(Intent.ACTION_SEND);
        _Intent.setType("text/html");
        _Intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ getString(R.string.mail_feedback_email) });
        _Intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject));
        _Intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_feedback_message));
        startActivity(Intent.createChooser(_Intent, getString(R.string.title_send_feedback)));
    }
}
