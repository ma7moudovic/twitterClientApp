package com.shar2wy.twitterclientapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shar2wy.twitterclientapp.R;
import com.shar2wy.twitterclientapp.adapters.QuickAdapters.FollowersQuickAdapter;
import com.shar2wy.twitterclientapp.dataModels.EventBusModels.EventGetBearToken;
import com.shar2wy.twitterclientapp.dataModels.EventBusModels.EventGetFollowers;
import com.shar2wy.twitterclientapp.dataModels.Follower;
import com.shar2wy.twitterclientapp.utilities.ApiManager;
import com.shar2wy.twitterclientapp.utilities.RealmHelper;
import com.twitter.sdk.android.Twitter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;

import static com.shar2wy.twitterclientapp.activities.ProfileActivity.FOLLOWER_ID;

public class FollowersActivity extends AppCompatActivity {

    private RecyclerView followersRecyclerView;
//    private FollowersAdapter followersAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Follower> followersList = new ArrayList<>();
    Set<Follower> followersSet = new HashSet<>();

    ApiManager mApiManager;
    Realm realm;
    RealmHelper realmHelper;
    ProgressBar progressBar;
    String cursor="-1L";
    FollowersQuickAdapter mFollowersQuickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(String.format(getString(R.string.title_activity_followers),Twitter.getSessionManager().getActiveSession().getUserName()));
        Log.d("user",Twitter.getSessionManager().getActiveSession().getUserId()+"");
        initViews();

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

        }

        showProgressBar();
    }

    private void initViews() {
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
                startActivity(new Intent(FollowersActivity.this,ProfileActivity.class).putExtra(FOLLOWER_ID,follower.getId()));
            }
        });

        followersRecyclerView.setAdapter(mFollowersQuickAdapter);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Twitter.getSessionManager().clearActiveSession();
            if(Twitter.getSessionManager().getSessionMap().isEmpty()){
                Toast.makeText(this,"empty", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(FollowersActivity.this,LoginActivity.class));
            }else {
                Toast.makeText(this,"not empty", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    public void onGetBearerToken(EventGetBearToken eventGetBearToken){

        if(eventGetBearToken.isSuccess()){
            mApiManager.getFollowers(
                    realmHelper.getBearerToken(realm).getAccess_token(),
                    Twitter.getSessionManager().getActiveSession().getUserId(),
                    "-1L",
                    true,
                    true
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
    }
}
