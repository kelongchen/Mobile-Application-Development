package com.example.news_gateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Menu menu;

    private static String news_source_all = "http://newsapi.org/v1/sources?language=en&country=us&apiKey=83941ed2597c445bb0767ec57eddf040";
    private static String news_source1 = "http://newsapi.org/v1/sources?language=en&country=us&category=";
    private static String news_source2 = "&apiKey=83941ed2597c445bb0767ec57eddf040";

    private static String article_source1 = "http://newsapi.org/v1/articles?source=";
    private static String article_source2 = "&apiKey=83941ed2597c445bb0767ec57eddf040";


    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";


    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<String> items = new ArrayList<>();
    private ArrayList<String> categoryList = new ArrayList<>();

    int flag=0;
    private String sourceName;
    private MyPageAdapter pageAdapter;
    private List<Fragment> fragments;
    private ViewPager pager;
    ArrayList<HashMap<String, String>> sourceList = new ArrayList<>();
    ArrayList<HashMap<String, String>> articleList =new ArrayList<>();
    HashMap<String, Integer> colorHashMap = new HashMap<>();


    private Receiver Receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start Service(NEW SERVICE)
        Intent intent = new Intent(MainActivity.this, MyService.class);
        startService(intent);

        Receiver = new Receiver();

        IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);
        registerReceiver(Receiver, filter1);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);



        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fragments = getFragments();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pageAdapter = new MyPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);


        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            flag=1;
            AsyncLoaderTask alt = new AsyncLoaderTask(this);
            alt.execute(news_source_all);



            mDrawerList.setAdapter(new ArrayAdapter<>(this,
                    R.layout.drawer_list_item, items));
            mDrawerList.setOnItemClickListener(
                    new ListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            selectItem(position);
                        }
                    }
            );
        }
            else{
            flag=0;
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("No Network Connection");
            alertDialog.setMessage("Data cannot be accessed/loaded Without Network Connection");
            alertDialog.show();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
            getMenuInflater().inflate(R.menu.about, menu);
        for (int i = 1; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SpannableString s = new SpannableString(menuItem.getTitle());

            switch (i) {
                case 1:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type1)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type1);
                    break;

                case 2:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type2)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type2);

                    break;

                case 3:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type3)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type3);
                    break;

                case 4:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type4)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type4);
                    break;

                case 5:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type5)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type5);
                    break;

                case 6:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type6)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type6);
                    break;


                case 7:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type7)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type7);
                    break;
            }
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {

            case R.id.all:
                System.out.println("\n\n\n you clicked all");
                searchCategory("all");
                return true;

            case R.id.science:
                System.out.println("\n\n\n you clicked science");
                searchCategory("science-and-nature");
                return true;

            case R.id.gaming:
                System.out.println("\n\n\n you clicked gaming");
                searchCategory("gaming");
                return true;

            case R.id.general:
                System.out.println("\n\n\n you clicked general");
                searchCategory("general");
                return true;

            case R.id.technology:
                System.out.println("\n\n\n you clicked technology");
                searchCategory("technology");
                return true;

            case R.id.sport:
                System.out.println("\n\n\n you clicked sport");
                searchCategory("sport");
                return true;

            case R.id.entertainment:
                System.out.println("\n\n\n you clicked entertainment");
                searchCategory("entertainment");
                return true;

            case R.id.business:
                System.out.println("\n\n\n you clicked business");
                searchCategory("business");
                return true;
            default:
                searchCategory("all");
        }

        return true;

    }

    private void searchCategory(String category) {
        AsyncLoaderTask alt = new AsyncLoaderTask(this);
     if(category == "all"){
         items.clear();
         alt.execute(news_source_all);
     }
        else{
         items.clear();
         alt.execute(news_source1 + category + news_source2);
     }
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList;
        fList = new ArrayList<>();
        return fList;
    }



    private void selectItem(int position) {
        setTitle(items.get(position));
        searchArticle(items.get(position));
        mDrawerLayout.closeDrawer(mDrawerList);
        ViewPager Drawer = (ViewPager) findViewById(R.id.viewpager);
        Drawer.setBackgroundResource(0);
    }

    private void searchArticle(String s) {

        String id = null;
        for (int i = 0; i < sourceList.size(); i++) {
        if(sourceList.get(i).get("name") == s){
            id = sourceList.get(i).get("id");
            sourceName = sourceList.get(i).get("name");
            break;
        }
        }
        AsyncArticleTask alt = new AsyncArticleTask(this);
            alt.execute(article_source1+id+article_source2);
    }

    private void reDoFragments(ArrayList<HashMap<String, String>> articleList) {
        for (int i = 0; i < pageAdapter.getCount(); i++)
            pageAdapter.notifyChangeInPosition(i);
        fragments.clear();
        for (int i = 0; i < articleList.size(); i++) {
            fragments.add(MyFragment.newInstance(sourceName,(i+1),articleList.size(),articleList.get(i)));
        }
        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);
    }


    public void updateData(ArrayList<HashMap<String, String>> sourceList1) {
        sourceList =sourceList1;
        categoryList.clear();
        for(int i=0;i<sourceList.size();i++) {
            String category =sourceList.get(i).get("category");
            categoryList.add(category);
        }

        for (int i = 0; i < sourceList.size(); i++){
            String name =sourceList.get(i).get("name");
            items.add(name);
        }
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, items));
    }



    public void updateData1(ArrayList<HashMap<String, String>> articleList1) {
        articleList = articleList1;
        reDoFragments(articleList);

    }



    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }


        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }

    }



    @Override
    protected void onDestroy() {
        unregisterReceiver(Receiver);
        Intent intent = new Intent(MainActivity.this, MyService.class);
        stopService(intent);
        super.onDestroy();
    }


    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case ACTION_NEWS_STORY:
                    if (intent.hasExtra("storylist")) {
                        //articleList = intent.getParcelableArrayListExtra("storylist");
                        //Toast.makeText(MainActivity.this, "Broadcast Message A Received: ", Toast.LENGTH_SHORT).show();
                        //break;

                        Log.d(TAG, "onReceive: "+articleList.size());
                        reDoFragments(articleList);
                        //Toast.makeText(MainActivity.this, "Broadcast Message A Received: ", Toast.LENGTH_SHORT).show();
                        break;
                    }

            }
        }
    }


}

