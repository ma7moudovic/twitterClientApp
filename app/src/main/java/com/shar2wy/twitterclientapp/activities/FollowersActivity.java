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

import com.shar2wy.twitterclientapp.R;
import com.shar2wy.twitterclientapp.RecyclerClickListener;
import com.shar2wy.twitterclientapp.adapters.FollowersAdapter;
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

import io.realm.Realm;

import static com.shar2wy.twitterclientapp.activities.ProfileActivity.FOLLOWER_ID;

public class FollowersActivity extends AppCompatActivity {

    private RecyclerView followersRecyclerView;
    private FollowersAdapter followersAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Follower> followersList = new ArrayList<>();
    ApiManager mApiManager;
    Realm realm;
    RealmHelper realmHelper;
    ProgressBar progressBar;

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
        followersAdapter = new FollowersAdapter(this,followersList);
        followersRecyclerView.setAdapter(followersAdapter);

        followersRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this, followersRecyclerView, new RecyclerClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                startActivity(new Intent(FollowersActivity.this,ProfileActivity.class).putExtra(FOLLOWER_ID,followersAdapter.getItem(position).getId()));
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(FollowersActivity.this,"long clicked : "+position, Toast.LENGTH_SHORT).show();
            }
        }));

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
            loadFollowers();
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
    private void loadFollowers() {

        followersList.addAll(realmHelper.getFollowers(realm));
        followersAdapter.notifyDataSetChanged();
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }
}
