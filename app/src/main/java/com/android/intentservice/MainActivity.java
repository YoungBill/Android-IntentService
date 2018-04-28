package com.android.intentservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addTask(View view) {
        switch (view.getId()) {
            case R.id.addTaskSingleBt:
                startActivity(new Intent(MainActivity.this, SingleIntentServiceActivity.class));
                break;
            case R.id.addTaskMultipleBt:
                startActivity(new Intent(MainActivity.this, MultiIntentServiceActivity.class));
                break;
            case R.id.handlerThreadBt:
                startActivity(new Intent(MainActivity.this, HandlerThreadActivity.class));
                break;
        }
    }
}
