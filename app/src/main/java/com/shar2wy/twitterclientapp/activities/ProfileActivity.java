package com.shar2wy.twitterclientapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shar2wy.twitterclientapp.R;
import com.shar2wy.twitterclientapp.RecyclerClickListener;
import com.shar2wy.twitterclientapp.adapters.TweetsAdapter;
import com.shar2wy.twitterclientapp.dataModels.EventBusModels.EventGetTweets;
import com.shar2wy.twitterclientapp.dataModels.Follower;
import com.shar2wy.twitterclientapp.dataModels.Tweet;
import com.shar2wy.twitterclientapp.utilities.ApiManager;
import com.shar2wy.twitterclientapp.utilities.RealmHelper;
import com.twitter.sdk.android.Twitter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import io.realm.Realm;

public class ProfileActivity extends AppCompatActivity {

    public static final String FOLLOWER_ID="followerID";
    private RecyclerView tweetsRecyclerView;
    private RecyclerView.Adapter tweetsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Tweet> tweetsList = new ArrayList<>();

    ApiManager mApiManager;
    Realm realm;
    RealmHelper realmHelper;
    ProgressBar progressBar;

    ImageView followerProfile;
    ImageView followerCover;
    TextView followerName;
    TextView followerHandle;
    TextView followerBio;
    long userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);

        mApiManager = new ApiManager(this);
        realm = Realm.getDefaultInstance();
        realmHelper = RealmHelper.getInstance(this);

        initViews();

        if(getIntent()!=null&&getIntent().getExtras()!=null){
            userId = getIntent().getExtras().getLong(FOLLOWER_ID);
            loadFollower(realmHelper.getFollower(realm,userId));
            if(realmHelper.getBearerToken(realm)==null){

                mApiManager.getBearerToken();

            }else {
                mApiManager.getTweets(
                        realmHelper.getBearerToken(realm).getAccess_token(),
                        userId,
                        10,
                        false
                );
            }
        }


    }

    private void loadFollower(Follower follower) {

        followerName.setText(follower.getName());
        followerHandle.setText(getResources().getString(R.string.screen_name_string_holder,follower.getScreen_name()));
        followerBio.setText(follower.getDescription());

        Glide.with(this).load(follower.getProfile_image_url()).crossFade().into(followerProfile);
        Glide.with(this).load(follower.getProfile_banner_url()).crossFade().into(followerCover);

    }

    private void initViews() {

        followerCover = (ImageView) findViewById(R.id.follower_cover);
        followerProfile = (ImageView) findViewById(R.id.follower_image);
        followerName = (TextView) findViewById(R.id.follower_name);
        followerHandle = (TextView) findViewById(R.id.follower_handle);
        followerBio = (TextView) findViewById(R.id.follower_bio);

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
        if(eventGetTweets.isSuccess()){
            loadTweets();
        }else {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTweets() {
        tweetsList.addAll(realmHelper.getTweets(realm));
        tweetsAdapter.notifyDataSetChanged();
    }
}
