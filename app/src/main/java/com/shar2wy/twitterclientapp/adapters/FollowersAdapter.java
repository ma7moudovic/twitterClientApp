package com.shar2wy.twitterclientapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shar2wy.twitterclientapp.R;
import com.shar2wy.twitterclientapp.dataModels.Follower;

import java.util.ArrayList;

/**
 * Created by Shar2wy on 12/01/17.
 */

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder>{

    ArrayList<Follower> followersList;
    Context context;

    public FollowersAdapter() {
    }

    public FollowersAdapter(Context context, ArrayList<Follower> followersList){
        this.context = context;
        this.followersList = followersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_layout_tweet, parent, false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        return viewHolder;

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_follower, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView followerName;
        public TextView followerHandle;
        public TextView followerBio;
        public ImageView follwerImage;

        public ViewHolder(View v) {

            super(v);
            followerName = (TextView) v.findViewById(R.id.follower_name);
            followerHandle = (TextView) v.findViewById(R.id.follower_handle);
            followerBio = (TextView) v.findViewById(R.id.follower_bio);
            follwerImage = (ImageView) v.findViewById(R.id.follower_image);

        }
    }
}
