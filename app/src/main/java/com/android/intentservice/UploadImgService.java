package com.android.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * IntentService是一个基于Service的一个类，用来处理异步的请求。你可以通过startService(Intent)来提交请求，该Service会在需要的时候创建，当完成所有的任务以后自己关闭，且请求是在工作线程处理的。
 * 使用IntentService最起码有两个好处，一方面不需要自己去new Thread了；另一方面不需要考虑在什么时候关闭该Service了。
 */
public class UploadImgService extends IntentService {

    public static final String EXTRA_IMG_PATH = "com.android.intentservice.extra.IMG_PATH";
    private static final String ACTION_UPLOAD_IMG = "com.android.intentservice.action.UPLOAD_IMAGE";
    private static final String TAG = UploadImgService.class.getSimpleName();


    public static void startUploadImg(Context context, String path) {
        Intent intent = new Intent(context, UploadImgService.class);
        intent.setAction(ACTION_UPLOAD_IMG);
        intent.putExtra(EXTRA_IMG_PATH, path);
        context.startService(intent);
    }


    public UploadImgService() {
        super("UploadImgService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_IMG.equals(action)) {
                final String path = intent.getStringExtra(EXTRA_IMG_PATH);
                handleUploadImg(path);
            }
        }
    }

    private void handleUploadImg(String path) {
        try {
            //模拟上传耗时
            Thread.sleep(3000);

            Intent intent = new Intent(MainActivity.UPLOAD_RESULT);
            intent.putExtra(EXTRA_IMG_PATH, path);
            sendBroadcast(intent);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}