package com.shar2wy.twitterclientapp.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shar2wy.twitterclientapp.R;
import com.shar2wy.twitterclientapp.dataModels.Tweet;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shar2wy on 15/01/17.
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private ArrayList<Tweet> mTweetsList = new ArrayList<>();
    private Context mContext;
    private static final Pattern PATTERN = Pattern.compile("#\\w+");

    public TweetsAdapter(Context context, ArrayList<Tweet> tweetsList) {
        this.mContext =context;
        this.mTweetsList=tweetsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        SpannableStringBuilder tweetBodyText = highlightPattern(mTweetsList.get(position).getText(), PATTERN);
        holder.tweetBody.setText(tweetBodyText);
//        holder.tweetBody.setText(tweetsList.get(position).getText());
        holder.tweetStatus.setText("");

    }

    @Override
    public int getItemCount() {
        return mTweetsList.size();
    }

    private SpannableStringBuilder highlightPattern(String text, Pattern pattern) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor
                    (mContext, R.color.colorPrimary)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableStringBuilder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tweetStatus;
        public TextView tweetBody;

        public ViewHolder(View v) {

            super(v);
            tweetStatus = (TextView) v.findViewById(R.id.tweet_status);
            tweetBody = (TextView) v.findViewById(R.id.tweet_body);
        }
    }
}
