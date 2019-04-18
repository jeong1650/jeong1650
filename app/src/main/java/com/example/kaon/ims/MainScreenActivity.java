package com.example.kaon.ims;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


public class MainScreenActivity extends AppCompatActivity {
    private Context mContext;

    private TabLayout mTablayout;
    private ViewPager mViewpager;
    private ViewPageAdapter mViewPageAdapter;
    public String mName;
    public String idValue;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Window window = getWindow();



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
