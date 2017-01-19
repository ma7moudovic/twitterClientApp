package com.shar2wy.twitterclientapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shar2wy.twitterclientapp.LocaleHelper;
import com.shar2wy.twitterclientapp.PrefsManager;
import com.shar2wy.twitterclientapp.R;
import com.shar2wy.twitterclientapp.adapters.QuickAdapters.FollowersQuickAdapter;
import com.shar2wy.twitterclientapp.dataModels.EventBusModels.EventGetBearToken;
import com.shar2wy.twitterclientapp.dataModels.EventBusModels.EventGetFollowers;
import com.shar2wy.twitterclientapp.dataModels.EventBusModels.EventGetUserInfo;
import com.shar2wy.twitterclientapp.dataModels.Follower;
import com.shar2wy.twitterclientapp.utilities.ApiManager;
import com.shar2wy.twitterclientapp.utilities.RealmHelper;
import com.twitter.sdk.android.Twitter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import io.realm.Realm;

import static com.shar2wy.twitterclientapp.activities.ProfileActivity.FOLLOWER_ID;

public class FollowersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView followersRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Follower> followersList = new ArrayList<>();

    ApiManager mApiManager;
    Realm realm;
    RealmHelper realmHelper;
    ProgressBar progressBar;
    String cursor="-1L";
    FollowersQuickAdapter mFollowersQuickAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    View headerLayout;
    TextView followerName;
    ImageView followerImage, followerCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(String.format(getString(R.string.title_activity_followers),Twitter.getSessionManager().getActiveSession().getUserName()));
        Log.d("user",Twitter.getSessionManager().getActiveSession().getUserId()+"");
        initViews();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0); // 0-index header

        followerName = (TextView) headerLayout.findViewById(R.id.follower_name);
//        followerHandle = (TextView) headerLayout.findViewById(R.id.follower_handle);

        followerImage = (ImageView) headerLayout.findViewById(R.id.follower_image);
        followerCover = (ImageView) headerLayout.findViewById(R.id.follower_cover);

        mApiManager = new ApiManager(FollowersActivity.this);
        realm = Realm.getDefaultInstance();
        realmHelper = RealmHelper.getInstance(this);

        if(realmHelper.getBearerToken(realm)==null){

            mApiManager.getBearerToken();

        }else {

            mApiManager.getFollowers(realmHelper.getBearerToken(realm).getAccess_token(),
                    Twitter.getSessionManager().getActiveSession().getUserId(),
                    "-1L",
                    true,
                    true
            );

            Follower user = realmHelper.getUserInfo(realm,Twitter.getSessionManager().getActiveSession().getUserId());
            if(user==null){
                mApiManager.getUserInfo(
                        realmHelper.getBearerToken(realm).getAccess_token(),
                        Twitter.getSessionManager().getActiveSession().getUserId()
                );
            }else {
                loadAccountInfo(user);
            }
        }

        showProgressBar();
    }

    private void loadAccountInfo(Follower userInfo) {

        followerName.setText(getString(R.string.screen_name_string_holder,userInfo.getScreen_name()));
        Glide.with(this).load(userInfo.getProfile_image_url()).crossFade().into(followerImage);
        Glide.with(this).load(userInfo.getProfile_banner_url()).crossFade().into(followerCover);

    }

    private void initViews() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.follower_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mApiManager.getFollowers(realmHelper.getBearerToken(realm).getAccess_token(),
                        Twitter.getSessionManager().getActiveSession().getUserId(),
                        "-1L",
                        true,
                        true
                );
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        followersRecyclerView = (RecyclerView) findViewById(R.id.followers_recycler_view);
        followersRecyclerView.setHasFixedSize(true);
        followersRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        mLayoutManager = new LinearLayoutManager(this);
        followersRecyclerView.setLayoutManager(mLayoutManager);

        mFollowersQuickAdapter = new FollowersQuickAdapter(R.layout.item_layout_follower,followersList);
        mFollowersQuickAdapter.setEnableLoadMore(true);
        mFollowersQuickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mApiManager.getFollowers(realmHelper.getBearerToken(realm).getAccess_token(),
                        Twitter.getSessionManager().getActiveSession().getUserId(),
                        cursor,
                        true,
                        true
                );
            }
        });
        mFollowersQuickAdapter.setOnFollowerClickListener(new FollowersQuickAdapter.OnFollowerClickListener() {
            @Override
            public void onFollowerClick(Follower follower) {
                startActivity(new Intent(FollowersActivity.this,ProfileActivity.class)
                        .putExtra(FOLLOWER_ID,follower.getId()));
            }
        });

        followersRecyclerView.setAdapter(mFollowersQuickAdapter);

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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onGetFollowersSuccess(EventGetFollowers eventGetFollowers){

        if(eventGetFollowers.isSuccess()){
            cursor=eventGetFollowers.getNext_cursor_str();
            loadFollowers(eventGetFollowers.getPrevious_cursor_str()=="0");

        }else {
            Toast.makeText(this, "fail to get Followers", Toast.LENGTH_SHORT).show();
        }
        hideProgressBar();

    }

    @Subscribe
    public void onGetUserInfo(EventGetUserInfo eventGetUserInfo){
        if(eventGetUserInfo.isSuccess()){
            Follower userInfo = realmHelper.getUserInfo(realm,Twitter.getSessionManager().getActiveSession().getUserId());
            loadAccountInfo(userInfo);
        }else {
            Toast.makeText(this,getString(R.string.msg_fail_get_user_info), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onGetBearerToken(EventGetBearToken eventGetBearToken){

        if(eventGetBearToken.isSuccess()){
            mApiManager.getFollowers(
                    realmHelper.getBearerToken(realm).getAccess_token(),
                    Twitter.getSessionManager().getActiveSession().getUserId(),
                    "-1L",
                    true,
                    true
            );
            mApiManager.getUserInfo(realmHelper.getBearerToken(realm).getAccess_token(),
                    Twitter.getSessionManager().getActiveSession().getUserId()
            );
        }else {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }
        hideProgressBar();
    }

    private void loadFollowers(boolean clear) {
        mFollowersQuickAdapter.setNewData(realmHelper.getFollowers(realm));
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        if(mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_change_lang) {
            showChangeLanguageDialog();
        }else if(id == R.id.nav_logout){
            showLogoutWarningDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showChangeLanguageDialog(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.changelangauge_msg));

        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.changelangauge_msg_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                if(LocaleHelper.getSelectedLanguage().equalsIgnoreCase(PrefsManager.ARABIC_LOCAL_LANGUAGE)){
                    LocaleHelper.setSelectedLanguage(PrefsManager.ENGLISH_LOCAL_LANGUAGE,getApplicationContext());
                }else {
                    LocaleHelper.setSelectedLanguage(PrefsManager.ARABIC_LOCAL_LANGUAGE,getApplicationContext());
                }
                restartApp();
            }
        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.changelangauge_msg_no),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void restartApp() {
        Intent refresh = new Intent(this, DispatchActivity.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(refresh);
    }

    private void showLogoutWarningDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_msg_logout).setPositiveButton(R.string.dialog_btn_logout,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Twitter.getSessionManager().clearActiveSession();
                        if(Twitter.getSessionManager().getSessionMap().isEmpty()){
//                Toast.makeText(this,"empty", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(FollowersActivity.this,LoginActivity.class));
                        }else {
//                Toast.makeText(this,"not empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

}
