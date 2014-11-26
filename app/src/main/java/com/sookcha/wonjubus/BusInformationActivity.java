package com.sookcha.wonjubus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class BusInformationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_information);
        Intent intent = getIntent();
        HashMap<String, String> hashMap = (HashMap<String, String>) intent.getSerializableExtra("bus-information");
        TextView busNumber = (TextView) findViewById(R.id.busNumberText);
        busNumber.setText(hashMap.get("busNumber"));

        ListView busInfo = (ListView) findViewById(R.id.busInfoList);
        ArrayList mapList = new ArrayList<HashMap<String, String>>();

        String[] a = {"기점", "종점", "첫차", "막차", "운행횟수", "배차간격"};

        HashMap<String, String> item;

        for (int i = 0; i < hashMap.get("busInformation").toString().split("\n").length; i++) {
            item = new HashMap<String, String>();

            item.put("section", a[i]);
            item.put("content", hashMap.get("busInformation").toString().split("\n")[i]);
            mapList.add(item);
        }

        int[] listViewText={R.id.sectionText,R.id.contentText};
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), mapList, R.layout.about_list_item, new String[]{"section","content"}, listViewText);

        busInfo.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bus_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
