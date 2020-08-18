package com.example.gethtml;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
        Log.d( "MyBroadcastReceiver", "onReceive()" );
        Bundle bundle = intent.getExtras();
/*
        String data = bundle.getString("url_data" );
*/
        int data = bundle.getInt("total_num" );
        int count = bundle.getInt("count_num" );
        String title[] = bundle.getStringArray( "title_data" );
        String jsonData = bundle.getString( "url_data" );

        Toast.makeText(
                context,
                "onReceive! " /*+ data*/,
                Toast.LENGTH_LONG).show();

        //MainActivityへHTMLの内容を表示する
        TextView textView = ( (Activity)context ).findViewById( R.id.textView );
//        textView.setText( data );
        String str = String.valueOf( count ) + " / " + String.valueOf( data );
        textView.setText( str );

        str = title[0]
                + "\n" + title[1]
                + "\n" + title[2]
                + "\n" + title[3]
                + "\n" + title[4]
                + "\n" + title[5]
                + "\n" + title[6]
                + "\n" + title[7]
                + "\n" + title[8]
                + "\n" + title[9]
                + "\n"
                + "\n" + jsonData;

        TextView articleTitle = ( (Activity)context ).findViewById( R.id.article_Title );
        articleTitle.setText( str );
    }
}
