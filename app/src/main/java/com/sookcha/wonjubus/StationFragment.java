package com.sookcha.wonjubus;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * Created by sookcha on 14. 11. 19..
 */
public class StationFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StationFragment newInstance(int sectionNumber) {
        StationFragment fragment = new StationFragment();
        Bundle args = new Bundle();
        // = sectionNumber;
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        Log.d("Section", String.valueOf(sectionNumber));
        return fragment;
    }

    public StationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.station_main, container, false);
        ListView tv = (ListView) rootView.findViewById(R.id.favList);
        TextView searchBox = (TextView) rootView.findViewById(R.id.editText);

        return rootView;
    }
}
