package com.example.owenzx.jy_zx_1;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomBar mBottomBar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ListAdapter adapter;
    private JsonObjectRequest adsreq;
    private ListView listView_ad;
//    private Handler handler = new Handler();
//    public final Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            MySingleton.getInstance(AdsActivity.this).addToRequestQueue(adsreq);
//            adapter.notifyDataSetChanged();
//        }
//    };

    final String adsMainUrl = "http://lizunks.xicp.io:34789/trade_test/all_product.php";
    final String getTypeUrl = "http://lizunks.xicp.io:34789/trade_test/search_type.php";
    final String searchAdUrl = "http://lizunks.xicp.io:34789/trade_test/search_product.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        handleIntent(getIntent());

        final ArrayList<HashMap<String,String>> prodList = new ArrayList<HashMap<String, String>>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent createAdForm = new Intent(view.getContext(), AdFormActivity.class);
                startActivity(createAdForm);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.noTopOffset();
        mBottomBar.noNavBarGoodness();
        mBottomBar.setDefaultTabPosition(0);

        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
//                Intent intent= new Intent();
                switch (menuItemId) {

                    case R.id.bb_menu_requirements:
                        Intent intent2 = new Intent(AdsActivity.this, ReqActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.bb_menu_messages:
                        Intent intent3 = new Intent(AdsActivity.this, MessageActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.bb_menu_account:
                        Intent intent4 = new Intent(AdsActivity.this, AccountActivity.class);
                        startActivity(intent4);
                        break;
                }
//                startActivity(intent);
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }

        });
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, "#7B1FA2");
        mBottomBar.mapColorForTab(3, "#FF5252");


        final ArrayList<String> JSONAdStrList = new ArrayList<String>();

        adsreq = new JsonObjectRequest(Request.Method.GET, adsMainUrl,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        JSONArray ja = response.getJSONArray("posts");
                        for (int i=0; i<ja.length(); ++i){
                            JSONObject jsonpost = ja.getJSONObject(i);
                            String productID = jsonpost.getString("pd_id");
                            String prodName = jsonpost.getString("name");
                            String prodPrice = jsonpost.getString("price");
                            String prodDetail = jsonpost.getString("detail");
                            HashMap<String,String> prod = new HashMap<String, String>();
                            prod.put("pd_id",productID);
                            prod.put("name",prodName);
                            prod.put("price",prodPrice);
                            prod.put("detail",prodDetail);
                            prodList.add(prod);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(AdsActivity.this).addToRequestQueue(adsreq);

        String [] from = {"name","price","detail"};
        int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_ads, adList);
        adapter = new SimpleAdapter(getApplicationContext(),prodList,R.layout.list_item_adsnew,from,to);

        listView_ad = (ListView) findViewById(R.id.listview_ad);
        assert listView_ad != null;
        listView_ad.setAdapter(adapter);

        listView_ad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

//                String adDetail = adapter.getItem(pos);
                Intent intent = new Intent(AdsActivity.this,AdDetailActivity.class).putExtra("pd_id",prodList.get(pos).get("pd_id"));
                intent.putExtra("name",prodList.get(pos).get("name"));
                intent.putExtra("price",prodList.get(pos).get("price"));
                intent.putExtra("detail",prodList.get(pos).get("detail"));
//                Intent intent = new Intent(AdsActivity.this,AdDetailActivity.class).putExtra(Intent.EXTRA_TEXT,adDetail);
                startActivity(intent);
            }
        });
//        handler.postDelayed(runnable,1000);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search_ads, menu);
        inflater.inflate(R.menu.ads, menu);

        //Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            final ArrayList<HashMap<String,String>> prodList = new ArrayList<HashMap<String, String>>();

            Map<String, String> params = new HashMap<String, String>();
            params.put("type", "sdl");


            CustomRequest adsreq = new CustomRequest(Request.Method.POST, getTypeUrl,params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1){
                            JSONArray ja = response.getJSONArray("posts");
                            for (int i=0; i<ja.length(); ++i){
                                JSONObject jsonpost = ja.getJSONObject(i);
                                String productID = jsonpost.getString("pd_id");
                                String prodName = jsonpost.getString("name");
                                String prodPrice = jsonpost.getString("price");
                                String prodDetail = jsonpost.getString("detail");
                                HashMap<String,String> prod = new HashMap<String, String>();
                                prod.put("pd_id",productID);
                                prod.put("name",prodName);
                                prod.put("price",prodPrice);
                                prod.put("detail",prodDetail);
                                prodList.add(prod);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String [] from = {"name","price","detail"};
                    int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
                    adapter = new SimpleAdapter(getApplicationContext(),prodList,R.layout.list_item_adsnew,from,to);
                    listView_ad.setAdapter(adapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
//            MySingleton.getInstance().getRequestQueue().;
            MySingleton.getInstance(AdsActivity.this).addToRequestQueue(adsreq);

            Toast.makeText(getApplicationContext(),
                    "!!!",
                    Toast.LENGTH_SHORT).show();



        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
//        MySingleton.getInstance(AdsActivity.this).addToRequestQueue(adsreq);
//        adapter.notifyDataSetChanged();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Ads Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.owenzx.jy_zx_1/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }


    @Override
    public void onResume() {
        super.onResume();
//        MySingleton.getInstance(AdsActivity.this).addToRequestQueue(adsreq);
//        adapter.notifyDataSetChanged();
        mBottomBar.setDefaultTabPosition(0);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Ads Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.owenzx.jy_zx_1/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mBottomBar.onSaveInstanceState(outState);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use te query to do sth.

            final ArrayList<HashMap<String,String>> prodList = new ArrayList<HashMap<String, String>>();

            Map<String, String> params = new HashMap<String, String>();
            params.put("name", query);


            CustomRequest adsreq = new CustomRequest(Request.Method.POST,searchAdUrl,params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1){
                            JSONArray ja = response.getJSONArray("posts");
                            for (int i=0; i<ja.length(); ++i){
                                JSONObject jsonpost = ja.getJSONObject(i);
                                String productID = jsonpost.getString("pd_id");
                                String prodName = jsonpost.getString("name");
                                String prodPrice = jsonpost.getString("price");
                                String prodDetail = jsonpost.getString("detail");
                                HashMap<String,String> prod = new HashMap<String, String>();
                                prod.put("pd_id",productID);
                                prod.put("name",prodName);
                                prod.put("price",prodPrice);
                                prod.put("detail",prodDetail);
                                prodList.add(prod);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String [] from = {"name","price","detail"};
                    int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
                    adapter = new SimpleAdapter(getApplicationContext(),prodList,R.layout.list_item_adsnew,from,to);
                    listView_ad.setAdapter(adapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
//            MySingleton.getInstance().getRequestQueue().;
            MySingleton.getInstance(AdsActivity.this).addToRequestQueue(adsreq);

//
//            Intent intent2 = new Intent(AdsActivity.this, AdsSearchActivity.class);
//            startActivity(intent2);
        }
    }
}
