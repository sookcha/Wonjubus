package com.sookcha.wonjubus;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by sookcha on 14. 11. 19..
 */
public class BusSearchFragment extends Fragment {

//    public Context defaultContext = this.getActivity().getBaseContext();
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public SimpleAdapter busAdapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    public String AssetJSONFile(String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = new FileInputStream(MainActivity.ma.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/buslist.json");
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    public static BusSearchFragment newInstance(int sectionNumber) {
        BusSearchFragment fragment = new BusSearchFragment();
        Bundle args = new Bundle();

        // = sectionNumber;
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public BusSearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bus_search, container, false);

        ListView stationList = (ListView) rootView.findViewById(R.id.busListView);
        final TextView searchBox = (TextView) rootView.findViewById(R.id.busSearchTextView);
        ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String,String>>();

        try {
            Context c = MainActivity.ma.getApplicationContext();

            String jsonLocation = AssetJSONFile(MainActivity.ma.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/buslist.json", c);
            JSONArray json = new JSONArray(jsonLocation);

            for(int i=0;i<json.length();i++){
                HashMap<String, String> map = new HashMap<String, String>();

                JSONObject e = json.getJSONObject(i);

                map.put("busNumber",  e.getString("BUSNUMBER"));
                map.put("busInformation", e.getString("BUSINFORMATION"));

                mapList.add(map);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] listViewText={R.id.nameText};
        busAdapter = new SimpleAdapter(MainActivity.ma.getApplicationContext(),mapList,R.layout.bus_list_item,new String[]{"busNumber"},listViewText);
        stationList.setAdapter(busAdapter);

        stationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                HashMap<String , String> data = new HashMap<String, String>();
                data = (HashMap<String , String>) item;
                Intent intent = new Intent(MainActivity.ma.getBaseContext(), BusInformationActivity.class);
                intent.putExtra("bus-information", data);
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
                busAdapter.getFilter().filter(text);
                busAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return rootView;
    }
}
