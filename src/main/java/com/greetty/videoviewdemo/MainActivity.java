package com.greetty.videoviewdemo;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.greetty.videoviewdemo.view.MyVideoView;
import com.greetty.videoviewdemo.view.MyVideoView2;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MyVideoView mMyVideoView;
    private RelativeLayout mMainActivityLayout;
    private MyVideoView myVideoView;

    private MyVideoView2 mMyVideoView2;
    private int mDuration = 0;
    private int mCurrentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main1);


        mMyVideoView2 = (MyVideoView2) findViewById(R.id.my_video2);
        mMyVideoView2.setFullScreen(true);
        mMyVideoView2.setLoop(true);
        mMyVideoView2.setSource("mnt/sdcard/shengshi.mp4");

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    mCurrentPosition = mMyVideoView2.getCurrentPosition();
                    mDuration = mMyVideoView2.getDuration();

                    Log.e(TAG, "Video current position: " + mCurrentPosition +
                            " 进度为：" + (mCurrentPosition * 100 / mDuration) + "%");
                    SystemClock.sleep(1000);
                }
            }
        }.start();
//        initView();

    }

    private void initView() {
        mMainActivityLayout = (RelativeLayout) findViewById(R.id.activity_main);
        myVideoView = new MyVideoView(MainActivity.this, true, true);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        myVideoView.setLayoutParams(layoutParams);
        mMainActivityLayout.addView(myVideoView);
        myVideoView.setSource("sdcard/ghsy.mp4");

    }

}
