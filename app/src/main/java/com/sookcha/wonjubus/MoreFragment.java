package com.sookcha.wonjubus;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.android.vending.billing.IInAppBillingService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


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
    MoreFragment fragment;

    IInAppBillingService mService;
    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };


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

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        MainActivity.ma.getBaseContext().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        ArrayList<String> skuList = new ArrayList<String> ();
        skuList.add("premiumUpgrade");
        skuList.add("gas");
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        Toast.makeText(MainActivity.ma.getBaseContext(),skuList.get(0),Toast.LENGTH_LONG).show();

        ListView aboutList = (ListView) rootView.findViewById(R.id.aboutListView);
        ArrayList<String> aboutLists = new ArrayList<String>();
        aboutLists.add("a");

        int[] listViewText={R.id.sectionText,R.id.contentText};

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> item;
        File file = new File(MainActivity.ma.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/wonjubusversion.txt");
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        String[] a = {"개발자 연락처", "me@sookcha.com","공지사항","원주버스 공지사항","버스 정보 출처", "http://its.wonju.go.kr", "데이터베이스 정보", text.toString(), "참고사항", "이 어플리케이션은 원주시 교통정보센터에서 정보를 불러옵니다. 원주시 자체 시스템에 문제가 생길때가 간혹 있어 정보 로딩이 안되거나 오차가 생기는 등의 문제가 발생할 수 있습니다. 문의사항은 위의 메일로 언제든지 연락주세요."};

        for (int i = 0; i < a.length-1; i+=2) {
            item = new HashMap<String, String>();

            item.put("section", a[i]);
            item.put("content", a[i+1]);
            list.add(item);
        }

        SimpleAdapter notes = new SimpleAdapter(MainActivity.ma.getApplicationContext(), list, R.layout.about_list_item, new String[] { "section", "content" }, listViewText);

        aboutList.setAdapter(notes);
        aboutList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://wonjubusapp.tumblr.com"));
                    startActivity(browserIntent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            MainActivity.ma.getBaseContext().unbindService(mServiceConn);
        }
    }
}
