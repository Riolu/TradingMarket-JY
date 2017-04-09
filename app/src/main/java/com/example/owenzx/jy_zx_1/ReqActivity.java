package com.example.owenzx.jy_zx_1;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReqActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{
    private BottomBar mBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_reqs);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_reqs);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_reqs);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_reqs);
        navigationView.setNavigationItemSelectedListener(this);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.noTopOffset();
        mBottomBar.noNavBarGoodness();
        mBottomBar.setDefaultTabPosition(1);
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
//                Intent intent= new Intent();
                switch (menuItemId) {

                    case R.id.bb_menu_ads:
                        Intent intent2 = new Intent(ReqActivity.this, AdsActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.bb_menu_messages:
                        Intent intent3 = new Intent(ReqActivity.this, MessageActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.bb_menu_account:
                        Intent intent4 = new Intent(ReqActivity.this, AccountActivity.class);
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




    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera_reqs) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery_reqs) {

        } else if (id == R.id.nav_slideshow_reqs) {

        } else if (id == R.id.nav_manage_reqs) {

        } else if (id == R.id.nav_share_reqs) {

        } else if (id == R.id.nav_send_reqs) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_reqs);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBottomBar.setDefaultTabPosition(1);
    }

}
