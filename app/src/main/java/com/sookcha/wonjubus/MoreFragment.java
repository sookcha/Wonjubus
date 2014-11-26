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
public class MoreFragment extends Fragment {

//    public Context defaultContext = this.getActivity().getBaseContext();
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    public static MoreFragment newInstance(int sectionNumber) {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();
        // = sectionNumber;
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MoreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.more_fragment, container, false);
        ListView aboutList = (ListView) rootView.findViewById(R.id.aboutListView);
        ArrayList<String> aboutLists = new ArrayList<String>();
        aboutLists.add("a");


        int[] listViewText={R.id.sectionText,R.id.contentText};

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> item;
        String[] a = {"개발자 연락처", "me@sookcha.com", "버스 정보 출처", "http://its.wonju.go.kr", "데이터베이스 정보", "20141119", "참고사항", "이 어플리케이션은 원주시 교통정보센터에서 정보를 불러옵니다. 원주시 자체 시스템에 문제가 생길때가 간혹 있어 정보 로딩이 안되거나 오차가 생기는 등의 문제가 발생할 수 있습니다. 문의사항은 위의 메일로 언제든지 연락주세요."};



        for (int i = 0; i < a.length-1; i+=2) {
            item = new HashMap<String, String>();

            item.put("section", a[i]);
            item.put("content", a[i+1]);
            list.add(item);
        }

        SimpleAdapter notes = new SimpleAdapter(MainActivity.ma.getApplicationContext(), list, R.layout.about_list_item, new String[] { "section", "content" }, listViewText);

        aboutList.setAdapter(notes);

        return rootView;
    }
}
