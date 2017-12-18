package com.greetty.videoviewdemo.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Created by Greetty on 2017/12/18.
 */
public class MyVideoView2 extends VideoView {

    private static final String TAG = "MyVideoView2";
    private boolean mFullScreen = false;
    private boolean mLoop;
    private boolean mVideoError;
    private boolean mStop;

    public MyVideoView2(Context context) {
        super(context);
        initListener();
    }

    public MyVideoView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initListener();
    }

    public MyVideoView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initListener();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mFullScreen) {
            int width = getDefaultSize(0, widthMeasureSpec);
            int height = getDefaultSize(0, heightMeasureSpec);
            setMeasuredDimension(width, height);
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void initListener(){
        setOnPreparedListener(mOnVideoPreparedListener);
        setOnCompletionListener(mOnVideoCompletionListener);
        setOnErrorListener(mOnVideoErrorListener);
    }


    private final MediaPlayer.OnPreparedListener mOnVideoPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mVideoError = false;
            if (!mStop) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                }
                mp.start();
            }
        }
    };

    private MediaPlayer.OnErrorListener mOnVideoErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            mVideoError = true;
            Toast.makeText(MyVideoView2.this.getContext(), "错误: 无法播放此视频播.",
                    Toast.LENGTH_LONG).show();
            return true;
        }
    };

    private MediaPlayer.OnCompletionListener mOnVideoCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.e(TAG, "mStop: " + mStop);
            Log.e(TAG, "mVideoError: " + mVideoError);
            Log.e(TAG, "mLoop: " + mLoop);

            if (!mStop && !mVideoError && mLoop) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                mp.start();
            }
        }
    };

    public void onActivityStart() {
        mStop = false;
        start();
    }

    public void onActivityStop() {
        mStop = true;
        stopPlayback();
    }



    /**
     * 设置视频资源位置
     * @param path
     */
    public void setSource(String path) {
        Log.e(TAG, "video path: " + path);

        if (path.startsWith("/"))
            setVideoPath(path);
        else
            setVideoURI(Uri.parse(path));
        start();
    }

    /**
     * 设置是否全屏播放
     * @param fullScreen boolean
     */
    public void setFullScreen(boolean fullScreen) {
        this.mFullScreen = fullScreen;
    }

    /**
     * 设置是否循环播放
     * @param loop  boolean
     */
    public void setLoop(boolean loop){
        this.mLoop=loop;
    }

}
