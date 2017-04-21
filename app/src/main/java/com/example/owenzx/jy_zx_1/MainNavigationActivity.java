package com.example.owenzx.jy_zx_1;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainNavigationActivity extends AppCompatActivity {

    private BottomBar mainNaviBottomBar;
    private AdsFragment adsFragment;
    private ReqFragment reqFragment;
    private MessageFragment messageFragment;
    private AccountFragment accountFragment;
    private int unreadMessageNum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        setBottomBar(savedInstanceState);
        setDefaultFragment();
    }

    private void setBottomBar(Bundle savedInstanceState){
        mainNaviBottomBar = BottomBar.attach(this,savedInstanceState);
        mainNaviBottomBar.setDefaultTabPosition(0);
        mainNaviBottomBar.noTopOffset();
        mainNaviBottomBar.noNavBarGoodness();

        mainNaviBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                android.app.FragmentManager fm = getFragmentManager();
                FragmentTransaction ftrans = fm.beginTransaction();
                switch (menuItemId) {
                    case R.id.bb_menu_ads:
                        if (adsFragment==null){
                            adsFragment = new AdsFragment();
                        }
                        ftrans.replace(R.id.navi_fragment_container,adsFragment,"ads");
                        break;
                    case R.id.bb_menu_requirements:
                        if (reqFragment==null){
                            reqFragment = new ReqFragment();
                        }
                        ftrans.replace(R.id.navi_fragment_container,reqFragment,"req");
                        break;
                    case R.id.bb_menu_messages:
                        if (messageFragment==null){
                            messageFragment = new MessageFragment();
                        }
                        ftrans.replace(R.id.navi_fragment_container,messageFragment,"message");
                        break;
                    case R.id.bb_menu_account:
                        final String user_id = LoginData.getFromPrefs(MainNavigationActivity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);
                        if (user_id!=null){
                            if (accountFragment==null){
                                accountFragment = new AccountFragment();
                            }
                            ftrans.replace(R.id.navi_fragment_container,accountFragment,"account");
                        } else {
                            Intent gotoLoginIntent = new Intent(MainNavigationActivity.this,LoginActivity.class);
                            startActivity(gotoLoginIntent);
                        }
                        break;
                }
                ftrans.commit();
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                android.app.FragmentManager fm = getFragmentManager();
                Fragment currentFragment;
                switch (menuItemId) {
                    case R.id.bb_menu_ads:
                        currentFragment = fm.findFragmentByTag("ads");
                        if (currentFragment instanceof AdsFragment){
                            AdsFragment curAdsFragment = (AdsFragment) currentFragment;
                            curAdsFragment.refreshcontent();
                        }
                        break;
                    case R.id.bb_menu_requirements:
                        currentFragment = fm.findFragmentByTag("req");
                        if (currentFragment instanceof ReqFragment){
                            ReqFragment curReqFragment = (ReqFragment) currentFragment;
                            curReqFragment.refreshcontent();
                        }
                        break;
                    case R.id.bb_menu_messages:

                        break;
                    case R.id.bb_menu_account:

                        break;
                }

            }

        });
        mainNaviBottomBar.mapColorForTab(0, "#1874CD");
        mainNaviBottomBar.mapColorForTab(1, "#1874CD");
        mainNaviBottomBar.mapColorForTab(2, "#1874CD");
        mainNaviBottomBar.mapColorForTab(3, "#1874CD");
        BottomBarBadge unreadMessages = mainNaviBottomBar.makeBadgeForTabAt(2,"#FF0000",0);
        unreadMessages.show();
    }

    private void setDefaultFragment(){
        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction ftransaction = fm.beginTransaction();
        adsFragment = new AdsFragment();
        ftransaction.replace(R.id.navi_fragment_container,adsFragment,"ads");
        ftransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mainNaviBottomBar.onSaveInstanceState(outState);
    }

//    @Override
//    protected void onNewIntent(Intent intent){
//        super.onNewIntent(intent);
//        android.app.FragmentManager fm = getFragmentManager();
//        Fragment currentFragment = fm.findFragmentByTag("ads");
//        if (currentFragment instanceof AdsFragment){
//            AdsFragment curAdsFragment = (AdsFragment) currentFragment;
//            curAdsFragment.handleIntent(intent);
//        }
//    }
}
