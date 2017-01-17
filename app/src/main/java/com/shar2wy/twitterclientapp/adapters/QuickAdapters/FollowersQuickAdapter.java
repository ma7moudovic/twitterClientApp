package com.shar2wy.twitterclientapp.adapters.QuickAdapters;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shar2wy.twitterclientapp.R;
import com.shar2wy.twitterclientapp.activities.FollowersActivity;
import com.shar2wy.twitterclientapp.activities.ProfileActivity;
import com.shar2wy.twitterclientapp.dataModels.Follower;

import java.util.List;

import static com.shar2wy.twitterclientapp.activities.ProfileActivity.FOLLOWER_ID;

/**
 * Created by Shar2wy on 17/01/17.
 */

public class FollowersQuickAdapter extends BaseQuickAdapter<Follower, BaseViewHolder> {

    private OnFollowerClickListener mOnFollowerClickListener;

    public FollowersQuickAdapter(int layoutResId, List<Follower> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Follower follower) {
        String name = follower.getName();
        String screenName = follower.getScreen_name();
        String description = follower.getDescription();
        String imageUrl = follower.getProfile_image_url();

        baseViewHolder.setText(R.id.follower_name, name).setText(R.id.follower_handle,
                mContext.getString(R.string.screen_name_string_holder, screenName));

        if (description != null) {
            baseViewHolder.setText(R.id.follower_bio, description);
            baseViewHolder.setVisible(R.id.follower_bio, true);
        } else {
            baseViewHolder.setVisible(R.id.follower_bio, false);
        }

        Glide.with(mContext).load(imageUrl).crossFade().into((ImageView) baseViewHolder.getView(R.id
                .follower_image));

        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnFollowerClickListener != null) {
                    mOnFollowerClickListener.onFollowerClick(follower);
                }
            }
        });

    }

    public void setOnFollowerClickListener(OnFollowerClickListener listener) {
        mOnFollowerClickListener = listener;
    }

    public interface OnFollowerClickListener {
        void onFollowerClick( Follower follower);
    }
}
