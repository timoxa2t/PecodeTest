package com.example.alex.pecodetest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnPageButtonClickListener {

    public static final String PAGE_NUMBER = "page number";
    public static final String SERVICE_ACTION = "service action";
    public static final String ACTION_ADD_NOTIFICATION = "add notification";
    public static final String ACTION_REMOVE_NOTIFICATION = "remove notification";
    public static final String STARTING_PAGE = "start page";
    public static final String SAVED_PAGES = "saved pages";

    public ViewPager mViewPager;
    public Pager mAdapter;
    public List<Integer> pageNumbers;
    public List<Page> mPages;
    public static int mPageCounter = 1;
    public FragmentManager mFragmentManager;
    public Context mContext;
    public Intent mNotifServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPages = new ArrayList<>();
        pageNumbers = new ArrayList<>();
        int startPage = getIntent().getIntExtra(STARTING_PAGE, 0);
        getPagesFromSharedPreferences();
        mFragmentManager = getSupportFragmentManager();
        mContext = this;
        mAdapter = new Pager(mFragmentManager, mContext, mPages);
        mViewPager.setAdapter(mAdapter);
        new Page(this);
        if(startPage > 0 ){
            mViewPager.setCurrentItem(startPage);
        }
        mNotifServiceIntent = new Intent(this, NotificationService.class);
    }

    private void getPagesFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("PecodeTest", MODE_PRIVATE);
        Map<String, Integer> map = (Map<String, Integer>) sharedPreferences.getAll();
        for(int i = 0; i < map.size(); i++){
            int pageNum = map.get(String.valueOf(i));
            Log.e("mainTag", "Index = " + i + "  pageNum = " + pageNum);
            pageNumbers.add(pageNum);
            mPages.add(Page.newInstance(pageNum));
        }
        if(mPages.isEmpty())addPage();
    }


    @Override
    public void onAddClicked(Page page) {
        addPage();
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mPages.size() - 1);
    }

    @Override
    public void onRemoveClicked(Page page) {

        mViewPager.setAdapter(null);
        int index = mPages.indexOf(page);
        int pageNumber = pageNumbers.get(index);
        mNotifServiceIntent.putExtra(SERVICE_ACTION, ACTION_REMOVE_NOTIFICATION);
        mNotifServiceIntent.putExtra(PAGE_NUMBER, pageNumber);
        startService(mNotifServiceIntent);
        pageNumbers.remove(index);
        mPages.remove(page);

        mAdapter = new Pager(mFragmentManager, mContext, mPages);
        mViewPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateNotificationClicked(Page page) {
        int pageIndex = mPages.indexOf(page);
        int pageNum = pageNumbers.get(pageIndex);
        mNotifServiceIntent.putExtra(SERVICE_ACTION, ACTION_ADD_NOTIFICATION);
        mNotifServiceIntent.putExtra(PAGE_NUMBER, pageNum);
        startService(mNotifServiceIntent);
    }

    private void addPage(){
        int num = 1;
        if(!pageNumbers.isEmpty()) {
            num = pageNumbers.get(pageNumbers.size() - 1) + 1;
        }
        pageNumbers.add(num);

        mPages.add(Page.newInstance(num));
    }

    @Override
    protected void onStop() {
        SharedPreferences sharedPreferences = getSharedPreferences("PecodeTest", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        for(int i = 0; i < pageNumbers.size(); i++){
            editor.putInt(String.valueOf(i), pageNumbers.get(i));
        }
        editor.commit();
        super.onStop();
    }
}
