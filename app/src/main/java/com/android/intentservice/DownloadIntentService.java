package com.android.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadIntentService extends IntentService {

    public static final String DOWNLOAD_URL = "down_load_url";
    private static final String TAG = DownloadIntentService.class.getSimpleName();
    private static Handler mUIHandler;
    public static final int WHAT_DOWNLOAD_FINISHED = 1;
    public static final int WHAT_DOWNLOAD_STARTED = 2;

    public DownloadIntentService() {
        super(TAG);
    }

    public static void setUIHandler(final Handler UIHandler) {
        mUIHandler = UIHandler;
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        String url = intent.getStringExtra(DOWNLOAD_URL);
        if (!TextUtils.isEmpty(url)) {
            sendMessageToMainThread(WHAT_DOWNLOAD_STARTED, "\n " + System.currentTimeMillis() + " 开始下载任务：\n" + url);
            try {
                Bitmap bitmap = downloadUrlToBitmap(url);
                //延迟一秒发送消息
                SystemClock.sleep(1000);
                sendMessageToMainThread(WHAT_DOWNLOAD_FINISHED, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessageToMainThread(final int id, final Object o) {
        if (mUIHandler != null) {
            mUIHandler.sendMessage(mUIHandler.obtainMessage(id, o));
        }
    }

    private Bitmap downloadUrlToBitmap(String url) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
        BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        urlConnection.disconnect();
        in.close();
        return bitmap;
    }
}