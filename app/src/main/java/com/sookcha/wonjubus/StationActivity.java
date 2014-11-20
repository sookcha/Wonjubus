package com.sookcha.wonjubus;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StationActivity extends Activity {

    HashMap<String, String> hashMap;
    HttpResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        Intent intent = getIntent();
        TextView stationName = (TextView)findViewById(R.id.stopName);
        hashMap = (HashMap<String, String>)intent.getSerializableExtra("stop-information");
        stationName.setText(hashMap.get("stopName") + " - " + hashMap.get("stopNumber"));
        new getStationInfo().execute(null,null,null);

    }

    private class getStationInfo extends AsyncTask<URL, Integer, Integer> {
        protected Integer doInBackground(URL... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://its.wonju.go.kr/map/AjaxRouteListByStop.do");
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
            nameValuePair.add(new BasicNameValuePair("stop_id", hashMap.get("stopNumber")));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.i("PROGRESSINGG!!!",progress[0].toString());
        }


        protected void onPostExecute(Integer result) {
            Toast t = new Toast(getBaseContext());
            t.setText(response.toString());
            t.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_station, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
