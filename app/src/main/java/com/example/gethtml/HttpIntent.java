package com.example.gethtml;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpIntent extends IntentService {

    public HttpIntent() { super( "HttpIntent" ); }
    public HttpIntent(String name) {
        super(name);
    }

    private int mArticleTotalNum;
    private int mArticleNum;
    private String mArticleTitle[];
//    private static InputStream stream;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d( "HttpIntent", "onHandleIntent()" );
        String inputString = httpGetJSON( intent.getStringExtra( "url_string" ) );


/*
        //BroadcastReceiverに取得したデータを送信
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra( "url_data", inputString );
        broadcastIntent.setAction("MY_ACTION");
        getBaseContext().sendBroadcast(broadcastIntent);
*/

        //BroadcastReceiverに取得したデータを送信
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra( "total_num", mArticleTotalNum );
        broadcastIntent.putExtra( "count_num", mArticleNum );
        broadcastIntent.putExtra( "title_data", mArticleTitle );
        broadcastIntent.putExtra( "url_data", inputString );
        broadcastIntent.setAction("MY_ACTION");
        getBaseContext().sendBroadcast(broadcastIntent);
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        Log.d( "HttpIntent", "setIntentRedelivery()" );
        super.setIntentRedelivery(enabled);
    }

    @Override
    public void onCreate() {
        Log.d( "HttpIntent", "onCreate()" );
        super.onCreate();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Log.d( "HttpIntent", "onStart()" );
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d( "HttpIntent", "onStartCommand()" );
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d( "HttpIntent", "onDestroy()" );
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d( "HttpIntent", "onBind()" );
        return super.onBind(intent);
    }

    private String httpGetJSON(String strURL) {

        XmlPullParserFactory factory;

        // (1)try-catchによるエラー処理
        try {
            // (2)URLクラスを使用して通信を行う
            URL url = new URL(strURL);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            // 動作を入力に設定
            connection.setDoInput(true);
            InputStream inputStream = connection.getInputStream();
            //inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( inputStream ) );

            // (3)データの取得
            StringBuilder data = new StringBuilder();
            String tmp = "";
            while ((tmp = bufferedReader.readLine()) != null) {
                data.append( tmp );
            }

            Log.d( "HttpIntent", data.toString() );

            // (1)XmlPullParserの用意
            factory = XmlPullParserFactory.newInstance(); //Creates a factory that always returns instances of Android's built-in XmlPullParser and XmlSerializer implementation.
            factory.setNamespaceAware(true); //名前空間を使用する
            XmlPullParser myxmlPullParser = factory.newPullParser(); //Creates a new instance of a XML Pull Parser using the currently configured factory features.
            try {
                myxmlPullParser.setInput( inputStream, "UTF-8" );
//                myxmlPullParser.setInput( bufferedReader );
//                myxmlPullParser.setInput( new StringReader ( "<goo>Hello World!</goo>" ) );

/* Yahoo wepapi : xmlじゃなかった。JSONだって。
                int cntTitle = 0;
                int eventType = myxmlPullParser.getEventType();
                while( eventType != XmlPullParser.END_DOCUMENT ) {
                    if(eventType == XmlPullParser.START_DOCUMENT) {
                        Log.d("XmlPullParserSample", "Start document");
                    } else if(eventType == XmlPullParser.END_DOCUMENT) {
                        Log.d("XmlPullParserSample", "End document");
                    } else if(eventType == XmlPullParser.START_TAG) {
                        Log.d("XmlPullParserSample", "Start tag " + myxmlPullParser.getName());
                    } else if(eventType == XmlPullParser.END_TAG) {
                        Log.d("XmlPullParserSample", "End tag " + myxmlPullParser.getName());
                    } else if(eventType == XmlPullParser.TEXT) {
                        Log.d("XmlPullParserSample", "Text " + myxmlPullParser.getText());
                    }
                    eventType = myxmlPullParser.next();
                }
 */

                JSONObject jsonObject = new JSONObject( data.toString() );

                mArticleTotalNum = jsonObject.getInt( "total" );
                mArticleNum = jsonObject.getInt( "count" );
                JSONArray jasonArray = jsonObject.getJSONArray( "results" );
                mArticleTitle = new String[ jasonArray.length() ];
                for ( int i=0; i<jasonArray.length(); i++ ) {
                    JSONObject item = jasonArray.getJSONObject( i );
//                    mArticleTitle[ i ] = item.getString( "title" );
                    mArticleTitle[ i ] = item.getString( "receptionUrl" );
                }

                Log.d( "HttpIntent", "httpGetJSON() fin !" );
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            // (4)終了処理
/* comment for debug
            inputStream.close();
            bufferedReader.close();
 */



            Log.d( "HttpIntent", "httpGetJSON() fin !" );

            return data.toString();

        } catch (Exception e) {
            // (5)エラー処理
            Log.d( "HttpIntent", e.toString() );
            return null;
        }
    }

}
