package com.example.kaon.ims;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


public class MainScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;

    private TabLayout mTablayout;
    private ViewPager mViewpager;
    private ViewPageAdapter mViewPageAdapter;
    public String mName;
    public String idValue;

    ScheduleFragment scheduleFragment;
    WaitpersonFragment waitpersonFragment;
    NoticeFragment noticeFragment;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar);

        Window window = getWindow();

        scheduleFragment = new ScheduleFragment();
        waitpersonFragment = new WaitpersonFragment();
        noticeFragment = new NoticeFragment();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);



        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);



        window.setStatusBarColor(Color.parseColor("#4e67c3"));
        Intent intent = getIntent();
        mName= intent.getExtras().getString("NAME");
        idValue = intent.getExtras().getString("id");

//        ScheduleFragment scheduleFragment = new ScheduleFragment();
//        Bundle bundle = new Bundle(2);
//        bundle.putString("NAME",mName);
//        bundle.putString("id",idValue);
//        scheduleFragment.setArguments(bundle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mContext = getApplicationContext();

        mTablayout = (TabLayout) findViewById(R.id.layout_tab);

        mTablayout.addTab(mTablayout.newTab().setIcon(R.drawable.calendar).setText("면접 일정"));
        mTablayout.addTab(mTablayout.newTab().setIcon(R.drawable.file).setText("서류 대기"));
        mTablayout.addTab(mTablayout.newTab().setIcon(R.drawable.people).setText("나의 면접자"));
        mTablayout.addTab(mTablayout.newTab().setIcon(R.drawable.logout).setText("로그아웃"));

        mViewpager = (ViewPager) findViewById(R.id.pager_content);
        mViewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(),mTablayout.getTabCount());
        mViewpager.setAdapter(mViewPageAdapter);

        mViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewpager.setCurrentItem(tab.getPosition());
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainScreenActivity.this, SettingActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_bar, menu);

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id == R.id.nav_schedule){
            mViewpager.setCurrentItem(0);

        } else if(id == R.id.nav_wait){
            mViewpager.setCurrentItem(1);
        } else if(id == R.id.nav_notice){
            mViewpager.setCurrentItem(2);
        }
        return false;
    }

    private class ViewPageAdapter extends FragmentPagerAdapter {

        private int mPageCount;

        public ViewPageAdapter(FragmentManager fm, int pageCount) {
            super(fm);
            this.mPageCount = pageCount;
        }

        @Nullable

        @Override
        public Fragment getItem(int position) {

            switch (position){

                case 0:
                    ScheduleFragment scheduleFragment = new ScheduleFragment();
                    scheduleFragment.setUsername(mName);
                    scheduleFragment.setIdvalue(idValue);
                    return scheduleFragment;

                case 1:
                    WaitpersonFragment waitpersonFragment = new WaitpersonFragment();
                    waitpersonFragment.setUsername(mName);
                    waitpersonFragment.setId(idValue);
                    return waitpersonFragment;

                case 2:
                    NoticeFragment noticeFragment = new NoticeFragment();
                    noticeFragment.setUsername(mName);
                    noticeFragment.setId(idValue);
                    return noticeFragment;
                case 3:
                    NotingFragment notingFragment = new NotingFragment();
                    return notingFragment;

                case 4:
                    LogoutFragment logoutFragment = new LogoutFragment();
                    return logoutFragment;

                default:

                    return null;
            }
//            if(i=0){
//                ScheduleFragment scheduleFragment = new ScheduleFragment();
//                    scheduleFragment.setUsername(mName);
//                    scheduleFragment.setIdvalue(idValue);
//                    return scheduleFragment;
//
//            } else if(i==2){
//                NoticeFragment noticeFragment = new NoticeFragment();
//                    noticeFragment.setUsername(mName);
//                    noticeFragment.setId(idValue);
//                    return noticeFragment;
//            } else if(i==3) {
//                LogoutFragment logoutFragment = new LogoutFragment();
//                    return logoutFragment;
//            }

        }

        @Override
        public int getCount() {
            return 5;
        }
    }

}
