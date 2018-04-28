package com.android.intentservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by baina on 18-4-28.
 */

public class HandlerThreadActivity extends AppCompatActivity implements Handler.Callback {

    private TextView mTvStartMsg;
    private TextView mTvFinishMsg;
    private Button mBtnStartDownload;

    private Handler mUIHandler;
    private DownloadHandlerThread mDownloadThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handlerthread);
        init();
    }

    private void init() {
        mTvStartMsg = findViewById(R.id.tv_start_msg);
        mTvFinishMsg = findViewById(R.id.tv_finish_msg);
        mBtnStartDownload = findViewById(R.id.btn_start_download);
        mUIHandler = new Handler(this);
        mDownloadThread = new DownloadHandlerThread("下载线程");
        mDownloadThread.setUIHandler(mUIHandler);
        mDownloadThread.setDownloadUrls(
                "http://pan.baidu.com/s/1qYc3EDQ",
                "http://bbs.005.tv/thread-589833-1-1.html",
                "http://list.youku.com/show/id_zc51e1d547a5b11e2a19e.html?");
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case DownloadHandlerThread.TYPE_FINISHED:
                mTvFinishMsg.setText(mTvFinishMsg.getText().toString() + "\n " + msg.obj);
                break;
            case DownloadHandlerThread.TYPE_START:
                mTvStartMsg.setText(mTvStartMsg.getText().toString() + "\n " + msg.obj);
                break;
        }
        return true;
    }

    public void startDownload(View view) {
        mDownloadThread.start();
        mBtnStartDownload.setText("正在下载");
        mBtnStartDownload.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDownloadThread.quitSafely();
    }
}
