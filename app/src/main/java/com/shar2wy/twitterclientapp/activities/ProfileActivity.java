package com.shar2wy.twitterclientapp.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.shar2wy.twitterclientapp.R;
import com.shar2wy.twitterclientapp.RecyclerClickListener;
import com.shar2wy.twitterclientapp.adapters.TweetsAdapter;
import com.shar2wy.twitterclientapp.dataModels.EventBusModels.EventGetTweets;
import com.shar2wy.twitterclientapp.dataModels.Tweet;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView tweetsRecyclerView;
    private RecyclerView.Adapter tweetsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Tweet> tweetsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);

//        getSupportActionBar().setTitle(String.format(getString(R.string.title_activity_followers),"Mahmoud"));

        initViews();

        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());
        tweetsList.add(new Tweet());

        tweetsAdapter.notifyDataSetChanged();

        Toast.makeText(this,tweetsList.size()+" tweet", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        tweetsRecyclerView = (RecyclerView) findViewById(R.id.follower_tweets_recycler_view);
        tweetsRecyclerView.setHasFixedSize(true);
        tweetsRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        mLayoutManager = new LinearLayoutManager(this);
        tweetsRecyclerView.setLayoutManager(mLayoutManager);
        tweetsAdapter = new TweetsAdapter(this, tweetsList);
        tweetsRecyclerView.setAdapter(tweetsAdapter);

        tweetsRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this, tweetsRecyclerView, new RecyclerClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(ProfileActivity.this,"clicked : "+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(ProfileActivity.this,"long clicked : "+position, Toast.LENGTH_SHORT).show();

            }
        }));
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
    public void onGetTweets(EventGetTweets eventGetTweets){

    }
}
