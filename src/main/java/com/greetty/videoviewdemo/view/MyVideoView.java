package com.greetty.videoviewdemo.view;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

/**
 * Created by Greetty on 2017/12/15.
 * <p>
 * 自定义VideoView
 */
public class MyVideoView extends VideoView {

    private static final String TAG = "MyVideoView";

    private boolean mLoop;
    private MediaPlayer mMp = null;
    private boolean mVideoError = false;
    private boolean mStop = false;
    private boolean mFullScreen = false;

    public MyVideoView(Context context, boolean loop, boolean fullScreen) {
        super(context);
        this.mLoop = loop;
        this.mFullScreen = fullScreen;
        setOnPreparedListener(mOnVideoPreparedListener);
        setOnCompletionListener(mOnVideoCompletionListener);
        setOnErrorListener(mOnVideoErrorListener);

        //隐藏进度条
        MediaController mMediaController = new MediaController(context);
        mMediaController.setVisibility(GONE);
        mMediaController.setMediaPlayer(this);
    }


    private final OnPreparedListener mOnVideoPreparedListener = new OnPreparedListener() {
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

    private OnErrorListener mOnVideoErrorListener = new OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            mVideoError = true;
            Toast.makeText(MyVideoView.this.getContext(), "错误: 无法播放此视频播.",
                    Toast.LENGTH_LONG).show();
            return true;
        }
    };

    private OnCompletionListener mOnVideoCompletionListener = new OnCompletionListener() {

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

    /**
     * 设置是否循环播放
     *
     * @param loop loop
     */
    public void setLoop(boolean loop) {
        mLoop = loop;
    }

    public boolean getLoop() {
        return mLoop;
    }

    /**
     * 设置全屏播放
     *
     * @param fullScreen fullScreen
     */
    public void setFullScreen(boolean fullScreen) {
        mFullScreen = fullScreen;
    }

    public boolean getFullScreen() {
        return mFullScreen;
    }

    public void onActivityStart() {
        mStop = false;
        start();
    }

    public void onActivityStop() {
        mStop = true;
        stopPlayback();
    }

    public void setSource(String path) {
        Log.e(TAG, "video path: " + path);

        if (path.startsWith("/"))
            setVideoPath(path);
        else
            setVideoURI(Uri.parse(path));
        start();
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
}
