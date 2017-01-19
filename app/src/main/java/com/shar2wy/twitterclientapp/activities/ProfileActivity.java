package com.shar2wy.twitterclientapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shar2wy.twitterclientapp.R;
import com.shar2wy.twitterclientapp.RecyclerClickListener;
import com.shar2wy.twitterclientapp.adapters.TweetsAdapter;
import com.shar2wy.twitterclientapp.dataModels.eventBus.EventGetTweets;
import com.shar2wy.twitterclientapp.dataModels.Follower;
import com.shar2wy.twitterclientapp.dataModels.Tweet;
import com.shar2wy.twitterclientapp.utilities.ApiManager;
import com.shar2wy.twitterclientapp.utilities.RealmHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import io.realm.Realm;

public class ProfileActivity extends AppCompatActivity {

    public static final String FOLLOWER_ID="followerID";

    private RecyclerView mTweetsRecyclerView;
    private RecyclerView.Adapter mTweetsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Tweet> tweetsList = new ArrayList<>();

    private ApiManager mApiManager;
    private Realm mRealm;
    private RealmHelper mRealmHelper;

    private ImageView mFollowerProfileImage;
    private ImageView mFollowerProfileCover;
    private TextView mFollowerName;
    private TextView mFollowerHandle;
    private TextView mFollowerBio;

    private long mUserId;

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
        mRealm = Realm.getDefaultInstance();
        mRealmHelper = RealmHelper.getInstance(this);

        initViews();

        if(getIntent()!=null&&getIntent().getExtras()!=null){
            mUserId = getIntent().getExtras().getLong(FOLLOWER_ID);
            loadFollower(mRealmHelper.getFollower(mRealm, mUserId));
            if(mRealmHelper.getBearerToken(mRealm)==null){

                mApiManager.getBearerToken();

            }else {
                mApiManager.getTweets(
                        mRealmHelper.getBearerToken(mRealm).getAccess_token(),
                        mUserId,
                        10,
                        false
                );
            }
        }


    }

    private void loadFollower(Follower follower) {

        mFollowerName.setText(follower.getName());
        mFollowerHandle.setText(getResources().getString(R.string.screen_name_string_holder,follower.getScreen_name()));
        mFollowerBio.setText(follower.getDescription());

        Glide.with(this).load(follower.getProfile_image_url()).crossFade().into(mFollowerProfileImage);
        Glide.with(this).load(follower.getProfile_banner_url()).crossFade().into(mFollowerProfileCover);

    }

    private void initViews() {

        mFollowerProfileCover = (ImageView) findViewById(R.id.follower_cover);
        mFollowerProfileImage = (ImageView) findViewById(R.id.follower_image);
        mFollowerName = (TextView) findViewById(R.id.follower_name);
        mFollowerHandle = (TextView) findViewById(R.id.follower_handle);
        mFollowerBio = (TextView) findViewById(R.id.follower_bio);

        mTweetsRecyclerView = (RecyclerView) findViewById(R.id.follower_tweets_recycler_view);
        mTweetsRecyclerView.setHasFixedSize(true);
        mTweetsRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        mLayoutManager = new LinearLayoutManager(this);
        mTweetsRecyclerView.setLayoutManager(mLayoutManager);
        mTweetsAdapter = new TweetsAdapter(this, tweetsList);
        mTweetsRecyclerView.setAdapter(mTweetsAdapter);

        mTweetsRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this, mTweetsRecyclerView, new RecyclerClickListener.ClickListener() {
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
        if(eventGetTweets.ismSuccess()){
            loadTweets();
        }else {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTweets() {
        tweetsList.addAll(mRealmHelper.getTweets(mRealm));
        mTweetsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!mRealm.isClosed()){
            mRealm.close();
        }
    }
}
