package com.android.intentservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;

import java.util.Arrays;
import java.util.List;

/**
 * Created by baina on 18-4-28.
 */

public class DownloadHandlerThread extends HandlerThread implements Handler.Callback {

    private static final String TAG = DownloadHandlerThread.class.getSimpleName();
    private static final String KEY_URL = "url";
    public static final int TYPE_START = 1;
    public static final int TYPE_FINISHED = 2;

    private Handler mWorkerHandler;
    private Handler mUIHandler;
    private List<String> mDownloadUrlList;

    public DownloadHandlerThread(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mWorkerHandler = new Handler(getLooper(), this);    //使用子线程中的 Looper
        if (mUIHandler == null) {
            throw new IllegalArgumentException("Not set UIHandler!");
        }
        //将接收到的任务消息挨个添加到消息队列中
        for (String url : mDownloadUrlList) {
            Message message = mWorkerHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString(KEY_URL, url);
            message.setData(bundle);
            mWorkerHandler.sendMessage(message);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg == null || msg.getData() == null) {
            return false;
        }
        String url = (String) msg.getData().get(KEY_URL);
        if (mUIHandler != null) {
            //下载开始，通知主线程
            Message startMsg = mUIHandler.obtainMessage(TYPE_START, "\n 开始下载 @" + System.currentTimeMillis() + "\n" + url);
            mUIHandler.sendMessage(startMsg);
        }

        SystemClock.sleep(2000);    //模拟下载

        if (mUIHandler != null) {
            //下载完成，通知主线程
            Message finishMsg = mUIHandler.obtainMessage(TYPE_FINISHED, "\n 下载完成 @" + System.currentTimeMillis() + "\n" + url);
            mUIHandler.sendMessage(finishMsg);
        }

        return true;
    }

    public void setDownloadUrls(String... urls) {
        mDownloadUrlList = Arrays.asList(urls);
    }

    //注入主线程 Handler
    public DownloadHandlerThread setUIHandler(Handler UIHandler) {
        mUIHandler = UIHandler;
        return this;
    }

    @Override
    public boolean quitSafely() {
        getLooper().quit();
        mUIHandler = null;
        return super.quitSafely();
    }
}
