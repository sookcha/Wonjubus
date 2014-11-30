package com.sookcha.wonjubus;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class MainActivity extends Activity implements ActionBar.TabListener {
    public static int sec = 0;
    private List<Fragment> mFragments = new Vector<Fragment>();
    static MainActivity ma;
    public static SimpleAdapter favoriteAdapter;
    public static ListView tv;
    public static ArrayList mapList;
    private int downloadCount = 0;

    StationSearchFragment stationSearch = new StationSearchFragment();
    BusSearchFragment busSearch = new BusSearchFragment();

    String output;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ma=this;

        mFragments.add(new PlaceholderFragment());
        mFragments.add(new StationSearchFragment());
        mFragments.add(new BusSearchFragment());
        mFragments.add(new MoreFragment());

        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        File file = new File(ma.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/buslist.json");
        File busFile = new File(ma.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/stationlist.json");
        if (!file.exists() || !busFile.exists()) {
            checkUpdate();
        } else {
            new checkUpdateTask().execute();
        }

    }

    public void checkUpdate() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("DB 업데이트가 있습니다");
        alert.setMessage("DB가 초기화되지 않았거나 새로운 업데이트가 있습니다. 현재 다운로드 중입니다. 잠시만 기다려주세요.");

        alert.show();
        File versionFile = new File(MainActivity.ma.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/wonjubusversion.txt");
        File stationFile = new File(MainActivity.ma.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/stationlist.json");
        File busFile = new File(MainActivity.ma.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/buslist.json");

        versionFile.delete();
        stationFile.delete();
        busFile.delete();

        String url = "https://gist.githubusercontent.com/sookcha/3a1c01ad1e8dfecfdca1/raw/buslist.json";
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

            String stationUrl = "https://gist.githubusercontent.com/sookcha/cb3a2d5a53b07ba40b4f/raw/stationlist.json";
            DownloadManager.Request stationRequest = new DownloadManager.Request(Uri.parse(stationUrl));

            String versionUrl = "https://gist.githubusercontent.com/sookcha/b4f5f94b319be003232a/raw/wonjubusversion.txt";
            DownloadManager.Request versionRequest = new DownloadManager.Request(Uri.parse(versionUrl));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }

            request.setTitle("원주버스 DB 다운로드");
            request.setDescription("버스 정보 DB를 다운로드합니다");

            stationRequest.setTitle("원주버스 DB 다운로드");
            stationRequest.setDescription("정류장 정보 DB를 다운로드합니다");

            versionRequest.setTitle("원주버스 DB 다운로드");
            versionRequest.setDescription("정류장 정보 DB를 다운로드합니다");

            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "buslist.json");
            stationRequest.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "stationlist.json");
            versionRequest.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "wonjubusversion.txt");


        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        manager.enqueue(request);
        manager.enqueue(stationRequest);
        manager.enqueue(versionRequest);


    }

    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            File stationFile = new File(MainActivity.ma.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/stationlist.json");
            if (stationFile.exists()) {
                downloadCount++;
                Toast.makeText(ma.getBaseContext(),String.valueOf(downloadCount),Toast.LENGTH_SHORT).show();

                //long download_id = intent.getLongExtra("extra_download_id", 0);
                if (downloadCount == 3) {
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }

            }
        }
    };


    private class checkUpdateTask extends AsyncTask<URL, Integer, Integer> {
        @Override
        protected Integer doInBackground(URL... params) {
            String versionUrl = "https://gist.githubusercontent.com/sookcha/b4f5f94b319be003232a/raw/wonjubusversion.txt";
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(versionUrl);

            HttpResponse httpResponse = null;
            try {
                httpResponse = httpClient.execute(httpGet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            try {
                output = EntityUtils.toString(httpEntity).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Integer result) {
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


            Integer numberOutput = Integer.parseInt(output);
            if (Integer.parseInt(text.toString().replace("\n","")) < numberOutput) {
                checkUpdate();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override


        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "즐겨찾기";
                case 1:
                        return "정류장";
                    case 2:
                        return "버스";
                    case 3:
                        return "더보기";
                }
                return null;
            }

        }

        /**
         * A placeholder fragment containing a simple view.
         */
        public static class PlaceholderFragment extends Fragment {

            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            // = sectionNumber;
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {

        }

        @Override
        public void onResume() {
            super.onResume();
            mapList.clear();

            SQLiteHelper db = new SQLiteHelper(MainActivity.ma.getBaseContext());
            List<Favorites> favList = db.getAllFavorites();
            for(int i=0; i<favList.size(); i++){
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("stopName",  favList.get(i).name);
                map.put("stopNumber", favList.get(i).number.toString());
                map.put("location", favList.get(i).location);
                map.put("location-lat", favList.get(i).locationLAT);
                map.put("location-lng", favList.get(i).locationLNG);

                mapList.add(map);
            }

            favoriteAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.favorite_menu, menu);
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            SQLiteHelper db = new SQLiteHelper(MainActivity.ma.getBaseContext());

            switch (item.getItemId()) {
                case R.id.action_remove:
                    HashMap<String, String> map = (HashMap<String, String>) mapList.get(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                    db.deleteFavorite(map.get("stopNumber"));

                    mapList.clear();

                    List<Favorites> favList = db.getAllFavorites();
                    for(int i=0; i<favList.size(); i++){
                        HashMap<String, String> mapA = new HashMap<String, String>();

                        mapA.put("stopName",  favList.get(i).name);
                        mapA.put("stopNumber", favList.get(i).number.toString());
                        mapA.put("location", favList.get(i).location);
                        /*
                        map.put("location-lat", map.get("LAT"));
                        map.put("location-lng", map.get("LNG"));
                        */
                        mapList.add(mapA);
                    }
                    favoriteAdapter.notifyDataSetChanged();


                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            SQLiteHelper db = new SQLiteHelper(rootView.getContext());
            tv = (ListView) rootView.findViewById(R.id.favList);

            List<Favorites> favList = db.getAllFavorites();


            mapList = new ArrayList<HashMap<String,String>>();

            for(int i=0; i<favList.size(); i++){
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("id", String.valueOf(favList.get(i).id));
                map.put("stopName",  favList.get(i).name);
                map.put("stopNumber", favList.get(i).number.toString());
                map.put("location", favList.get(i).location);
                map.put("location-lat", favList.get(i).locationLAT);
                map.put("location-lng", favList.get(i).locationLNG);


                mapList.add(map);
            }

            int[] listViewText={R.id.nameText};

            favoriteAdapter = new SimpleAdapter(MainActivity.ma.getApplicationContext(),mapList,R.layout.bus_list_item,new String[]{"stopName"},listViewText);
            registerForContextMenu(tv);
            tv.setAdapter(favoriteAdapter);


            tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getItemAtPosition(position);
                    HashMap<String, String> data;

                    data = (HashMap<String, String>) item;
                    Intent intent = new Intent(MainActivity.ma.getBaseContext(), StationActivity.class);
                    intent.putExtra("stop-information", data);
                    startActivity(intent);
                }
            });


            return rootView;
        }
    }

}
