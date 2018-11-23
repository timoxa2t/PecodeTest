package com.example.alex.pecodetest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Page extends Fragment implements View.OnClickListener {

    private ImageButton mAddPage;
    private ImageButton mDeletePage;
    private Button mCreateNotification;
    private TextView mPageNumberDisplay;
    private static OnPageButtonClickListener mCallback;

    public Page(){}

    @SuppressLint("ValidFragment")
    public Page(OnPageButtonClickListener callback){
        mCallback = callback;
    }

    public static Page newInstance(int pageNum){
        Page page = new Page();
        Bundle args = new Bundle();
        args.putInt(MainActivity.PAGE_NUMBER, pageNum);
        page.setArguments(args);
        return page;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_layout, container, false);
        mAddPage = view.findViewById(R.id.add_fragment);
        mDeletePage = view.findViewById(R.id.remove_page);
        mCreateNotification = view.findViewById(R.id.add_notification);
        mPageNumberDisplay = view.findViewById(R.id.page_number);
        int pageNum = getArguments().getInt(MainActivity.PAGE_NUMBER, 0);
        mPageNumberDisplay.setText(pageNum + "");
        mCreateNotification.setOnClickListener(this);
        mAddPage.setOnClickListener(this);
        if(pageNum == 1){
            mDeletePage.setVisibility(View.INVISIBLE);
        }else {
            mDeletePage.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_fragment:
                mCallback.onAddClicked(this);
                break;
            case R.id.remove_page:
                mCallback.onRemoveClicked(this);
                break;
            case R.id.add_notification:
                mCallback.onCreateNotificationClicked(this);
        }
    }
}
