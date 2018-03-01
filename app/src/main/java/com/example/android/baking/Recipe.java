package com.example.android.baking;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.baking.model.Step;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Recipe extends AppCompatActivity implements Player.EventListener {
    @BindView(R.id.btn_previous)
    Button btn_previous;
    @BindView(R.id.btn_next)
    Button btn_next;
    @BindView(R.id.tv_recipeInstruction)
    TextView tv_recipeInstruction;
    @BindView(R.id.exoPlayer_recipeVideo)
    SimpleExoPlayerView exoPlayer_recipeVideo;
    @BindView(R.id.iv_thumbnail)
    ImageView iv_thumbnail;
    SimpleExoPlayer player;
    Step step;
    int recipe_position;
    int position;
    Long videoPosition;
    int size_of_recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            videoPosition = 0L;
            position = getIntent().getIntExtra("position", 0);
            recipe_position = getIntent().getIntExtra("recipePosition", 0);
        } else {
            videoPosition = savedInstanceState.getLong("videoPosition");
            position = savedInstanceState.getInt("position");
            recipe_position = savedInstanceState.getInt("recipePosition");
        }
        setTitle(MainActivity.postResponses.get(recipe_position).getName());
        size_of_recipes = MainActivity.postResponses.get(recipe_position).getSteps().size();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                intializeRecipe(position);
            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                intializeRecipe(position);
            }
        });
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            btn_previous.setVisibility(View.GONE);
            btn_next.setVisibility(View.GONE);
            tv_recipeInstruction.setVisibility(View.GONE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            btn_previous.setVisibility(View.VISIBLE);
            btn_next.setVisibility(View.VISIBLE);
            tv_recipeInstruction.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("videoPosition", player.getCurrentPosition());
        outState.putInt("position", position);
        outState.putInt("recipePosition", recipe_position);
    }

    private void intializeRecipe(int position) {
        if (position >= 0 && position < MainActivity.postResponses.get(recipe_position).getSteps().size()) {
            if (position == 0) {
                btn_previous.setVisibility(View.INVISIBLE);
                btn_next.setVisibility(View.VISIBLE);
            } else if (position == size_of_recipes - 1) {
                btn_next.setVisibility(View.INVISIBLE);
                btn_previous.setVisibility(View.VISIBLE);
            } else if (position > 0 && position < size_of_recipes) {
                btn_next.setVisibility(View.VISIBLE);
                btn_previous.setVisibility(View.VISIBLE);
            }
            step = null;
            step = MainActivity.postResponses.get(recipe_position).getSteps().get(position);
            if (!(step.getVideoURL().equals("") && step.getVideoURL().isEmpty())) {
                if (player != null) {
                    player.release();
                    player = null;
                }
                exoPlayer_recipeVideo.setVisibility(View.VISIBLE);
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector =
                        new DefaultTrackSelector(videoTrackSelectionFactory);

                player =
                        ExoPlayerFactory.newSimpleInstance(this, trackSelector);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                        Util.getUserAgent(this, getString(R.string.app_name)), null);
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(step.getVideoURL()));
                player.prepare(videoSource);
                player.seekTo(videoPosition);
                exoPlayer_recipeVideo.setPlayer(player);
            } else {
                exoPlayer_recipeVideo.setVisibility(View.GONE);
            }
            if (!(step.getThumbnailURL().equals("") && step.getThumbnailURL().isEmpty())) {
                iv_thumbnail.setVisibility(View.VISIBLE);
                Glide.with(this).setDefaultRequestOptions(new RequestOptions().error(R.drawable.vp_placeholder)).load(step.getThumbnailURL()).into(iv_thumbnail);
            } else {
                iv_thumbnail.setVisibility(View.GONE);
            }
            tv_recipeInstruction.setText(step.getDescription());

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        intializeRecipe(position);
        if (player != null) {
            player.setPlayWhenReady(true);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.setPlayWhenReady(false);
            player.release();
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
