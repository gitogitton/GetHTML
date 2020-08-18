package com.example.gethtml;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    IntentFilter intentFilter;
    MyBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById( R.id.textView );
        textView.setText( R.string.url_string );

        //バックグラウンド（HTTPSリクエスト）処理
        String yahooKyujinUrl = createRequestTopURL();
        Intent intent = new Intent( this, HttpIntent.class );
        intent.putExtra( "url_string", yahooKyujinUrl );
        startService( intent );

        //BroadcastReceiver登録（バックグラウンド処理からMainActivityに表示するまで）
        receiver = new MyBroadcastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("MY_ACTION");
        registerReceiver(receiver, intentFilter);



        Log.d( "MainActivity", "MainActivity() fin." );

    }

    private String createRequestTopURL() {
        String apiURL = "https://job.yahooapis.jp/v1/furusato/jobinfo/?";
        String appid = "dj00aiZpPU5hNXkwb1BVV3ZubSZzPWNvbnN1bWVyc2VjcmV0Jng9YzM-";  //アプリケーションID（クライアントID）

        return String.format( "%sappid=%s", apiURL, appid );
    }

}