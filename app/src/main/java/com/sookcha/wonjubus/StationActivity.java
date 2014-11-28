package com.sookcha.wonjubus;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StationActivity extends Activity {

    HashMap<String, String> hashMap;
    String responseString;
    HttpResponse response;
    HashMap<String, String> map = new HashMap<String, String>();
    ArrayList<HashMap<String, String>> mapList;
    SimpleAdapter adapter;
    SQLiteHelper db = new SQLiteHelper(this);
    ProgressBar spinner;
    TextView stopNumber;
    MenuItem favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_station);

        getActionBar().setTitle("정류장 정보");


        Intent intent = getIntent();
        TextView stationName = (TextView) findViewById(R.id.stopName);
        stopNumber = (TextView) findViewById(R.id.stopNumber);
        spinner = (ProgressBar) findViewById(R.id.progressBar2);
        spinner.setVisibility(View.GONE);

        hashMap = (HashMap<String, String>) intent.getSerializableExtra("stop-information");
        stationName.setText(hashMap.get("stopName"));
        stopNumber.setText(hashMap.get("stopNumber") + " - " + hashMap.get("location"));

        new getStationInfo().execute(null, null, null);

        ListView station = (ListView) findViewById(R.id.stationTimeList);
        mapList = new ArrayList<HashMap<String, String>>();

        int[] listViewText = {R.id.nameText, R.id.timeText};
        adapter = new SimpleAdapter(getBaseContext(), mapList, R.layout.station_time_list_item, new String[]{"stopName", "stopInfo"}, listViewText);

        station.setAdapter(adapter);
    }

    private class getStationInfo extends AsyncTask<URL, Integer, Integer> {
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
        }

        protected Integer doInBackground(URL... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://its.wonju.go.kr/map/AjaxRouteListByStop.do");
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
            nameValuePair.add(new BasicNameValuePair("stop_id", hashMap.get("stopNumber")));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                response = httpClient.execute(httpPost);
                responseString = new BasicResponseHandler().handleResponse(response);
            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        new getStationInfo().execute(null, null, null);
                    }
                });

                e.printStackTrace();

                return null;
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.i("PROGRESSINGG!!!", progress[0].toString());
        }


        protected void onPostExecute(Integer result) {
            if (responseString != null) {
                spinner.setVisibility(View.GONE);
                org.jsoup.nodes.Document responseDocument = Jsoup.parse(responseString);
                Elements elems = responseDocument.select("tr");
                String s[] = new String[elems.size()];
                for (Element el : elems) {
                    map = new HashMap<String, String>();
                    String stationName = el.text().split("[원주]")[0].replace("[", "");
                    String[] timeInfo = el.text().split("[원주]")[2].replace("]", "").split(" ");

                    map.put("stopName", stationName);
                    if (timeInfo.length > 4) {
                        map.put("stopInfo", timeInfo[3] + timeInfo[4] + " / " + timeInfo[1] + " 정거장 전");
                    } else if (timeInfo.length > 3) {
                        map.put("stopInfo", timeInfo[1] + " " + timeInfo[2]);
                    } else {
                        map.put("stopInfo", el.text().split("[원주]")[2].replace("]", ""));
                    }

                    mapList.add(map);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_station, menu);
        favorite = menu.findItem(R.id.action_favorite);
        if (db.isExist(Integer.parseInt(stopNumber.getText().toString().split("-")[0].replace(" ", "")))) {
            favorite.setVisible(false);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        TextView text = (TextView) findViewById(R.id.stopName);
        TextView stationNumber = (TextView) findViewById(R.id.stopNumber);

        switch(id) {
            case R.id.action_favorite:
                if (!db.isExist(Integer.parseInt(stationNumber.getText().toString().split("-")[0].replace(" ", "")))) {
                    Toast.makeText(getBaseContext(), text.getText() + " 정류장이 즐겨찾기에 추가되었습니다", Toast.LENGTH_LONG).show();

                    db.addFavorite(new Favorites(text.getText().toString(),
                            Integer.parseInt(stationNumber.getText().toString().split("-")[0].replace(" ", "")),
                            stationNumber.getText().toString().split("-")[1], "0", "0"));
                }
                return super.onOptionsItemSelected(item);
            case R.id.action_locate:
                Intent intent = new Intent(MainActivity.ma.getBaseContext(), LocateActivity.class);
                intent.putExtra("location", hashMap.get("location-lat") + "," + hashMap.get("location-lng"));
                startActivity(intent);

                return super.onOptionsItemSelected(item);
        }

        return false;
    }
}
