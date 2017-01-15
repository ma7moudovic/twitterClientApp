package com.shar2wy.twitterclientapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.shar2wy.twitterclientapp.R;
import com.shar2wy.twitterclientapp.RecyclerClickListener;
import com.shar2wy.twitterclientapp.adapters.FollowersAdapter;
import com.shar2wy.twitterclientapp.dataModels.Follower;
import com.twitter.sdk.android.Twitter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

public class FollowersActivity extends AppCompatActivity {

    private RecyclerView followersRecyclerView;
    private RecyclerView.Adapter followersAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Follower> followersList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(String.format(getString(R.string.title_activity_followers),"Mahmoud"));

        initViews();

        followersList.add(new Follower());
        followersList.add(new Follower());
        followersList.add(new Follower());
        followersList.add(new Follower());
        followersList.add(new Follower());
        followersList.add(new Follower());
        followersList.add(new Follower());
        followersList.add(new Follower());
        followersList.add(new Follower());
        followersList.add(new Follower());

        followersAdapter.notifyDataSetChanged();

    }

    private void initViews() {
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
//                Toast.makeText(FollowersActivity.this,"clicked : "+position, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(FollowersActivity.this,ProfileActivity.class));
            }

            @Override
            public void onLongClick(View view, int position) {
//                Toast.makeText(FollowersActivity.this,"long clicked : "+position, Toast.LENGTH_SHORT).show();
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
}
