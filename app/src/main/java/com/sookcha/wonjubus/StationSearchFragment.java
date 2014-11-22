package com.sookcha.wonjubus;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by sookcha on 14. 11. 19..
 */
public class StationSearchFragment extends Fragment {

//    public Context defaultContext = this.getActivity().getBaseContext();
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    SimpleAdapter adapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    public String AssetJSONFile(String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = getResources().openRawResource(R.raw.stationlist);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    public static StationSearchFragment newInstance(int sectionNumber) {
        StationSearchFragment fragment = new StationSearchFragment();
        Bundle args = new Bundle();

        // = sectionNumber;
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public StationSearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.station_main, container, false);

        ListView stationList = (ListView) rootView.findViewById(R.id.listView);
        final TextView searchBox = (TextView) rootView.findViewById(R.id.editText);
        ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String,String>>();

        try {
            Context c = MainActivity.ma.getApplicationContext();
            String jsonLocation = AssetJSONFile("res/stationList.txt", c);
            JSONArray json = new JSONArray(jsonLocation);

            for(int i=0;i<json.length();i++){
                HashMap<String, String> map = new HashMap<String, String>();

                JSONObject e = json.getJSONObject(i);

                map.put("stopName",  e.getString("STOP_SHORTNAME"));
                map.put("stopNumber", e.getString("STOP_ID"));
                map.put("location", e.getString("REMARK"));
                map.put("location-lat", e.getString("LAT"));
                map.put("location-lng", e.getString("LNG"));

                mapList.add(map);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] listViewText={R.id.nameText};
        adapter = new SimpleAdapter(MainActivity.ma.getApplicationContext(),mapList,R.layout.bus_list_item,new String[]{"stopName"},listViewText);
        stationList.setAdapter(adapter);

        stationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                HashMap<String , String> data = new HashMap<String, String>();
                data = (HashMap<String , String>) item;
                Intent intent = new Intent(MainActivity.ma.getBaseContext(), StationActivity.class);
                intent.putExtra("stop-information", data);
                startActivity(intent);
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = searchBox.getText().toString().toLowerCase(Locale.getDefault());
                adapter.getFilter().filter(text);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return rootView;
    }
}
