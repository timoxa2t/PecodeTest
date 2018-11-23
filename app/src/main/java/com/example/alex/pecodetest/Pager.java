package com.example.alex.pecodetest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class Pager extends FragmentStatePagerAdapter {

    private List<Page> mViews;
    private Context mContext;


    public Pager(FragmentManager fragmentManager, Context context, List<Page> views){
        super(fragmentManager);
        mContext = context;
        mViews = views;
    }


    public Page getView(int position){
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mViews.size();
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        container.removeView(mViews.get(position).getView());
    }

    @Override
    public Fragment getItem(int position) {
        if(position >= 0 && !mViews.isEmpty() && position < mViews.size()) {
            return mViews.get(position);
        }
        return new Fragment();
    }



    @Override
    public int getItemPosition(@NonNull Object object) {
        for(int i = 0; i < getCount(); i++){
            if((Page) object == mViews.get(i)){
                return i;
            }
        }
        return POSITION_NONE;
    }
}
