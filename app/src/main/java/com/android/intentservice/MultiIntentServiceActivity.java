package com.android.intentservice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by baina on 18-4-28.
 */

public class MultiIntentServiceActivity extends AppCompatActivity implements Handler.Callback {

    private ImageView mIvDisplay;
    private Button mBtnDownload;
    private TextView mTvStatus;
    private Handler mUIHandler;
    private List<String> urlList = Arrays.asList(
            "https://ws1.sinaimg.cn/large/610dc034ly1fgepc1lpvfj20u011i0wv.jpg",
            "https://ws1.sinaimg.cn/large/d23c7564ly1fg6qckyqxkj20u00zmaf1.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fgchgnfn7dj20u00uvgnj.jpg");
    private int mFinishCount;   //完成的任务个数

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiintentservice);
        init();
    }

    private void init() {
        mIvDisplay = findViewById(R.id.iv_display);
        mBtnDownload = findViewById(R.id.btn_download);
        mTvStatus = findViewById(R.id.tv_status);
        mUIHandler = new Handler(this);
        DownloadIntentService.setUIHandler(mUIHandler);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg != null) {
            switch (msg.what) {
                case DownloadIntentService.WHAT_DOWNLOAD_FINISHED:
                    mIvDisplay.setImageBitmap((Bitmap) msg.obj);
                    mBtnDownload.setText("完成 " + (++mFinishCount) + "个任务");
                    break;
                case DownloadIntentService.WHAT_DOWNLOAD_STARTED:
                    mTvStatus.setText(mTvStatus.getText() + (String) msg.obj);
                    break;
            }
        }
        return true;
    }

    public void downloadImage(View view) {
        Intent intent = new Intent(this, DownloadIntentService.class);
        for (String url : urlList) {
            intent.putExtra(DownloadIntentService.DOWNLOAD_URL, url);
            startService(intent);
        }
        mBtnDownload.setEnabled(false);
    }
}
