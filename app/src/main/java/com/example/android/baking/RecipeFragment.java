package com.example.android.baking;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class RecipeFragment extends Fragment implements Player.EventListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_fragment, container, false);
        ButterKnife.bind(this, view);
        if(savedInstanceState != null){
            videoPosition = savedInstanceState.getLong("videoPosition");
        }else {
            videoPosition = 0L;
        }
        populateUI(view);
        return view;
    }


    @BindView(R.id.tv_recipeInstruction)
    TextView tv_recipeInstruction;
    @BindView(R.id.exoPlayer_recipeVideo)
    SimpleExoPlayerView exoPlayer_recipeVideo;
    @BindView(R.id.iv_thumbnail)
    ImageView iv_thumbnail;
    SimpleExoPlayer player;
    Step step;
    Long videoPosition;
    int recipe_position;
    int position;
    int size_of_recipes;

    public void populateUI(View view) {
        recipe_position = getActivity().getIntent().getIntExtra("recipePosition", 0);
        position = getArguments().getInt("step_position", 0);
        getActivity().setTitle(MainActivity.postResponses.get(recipe_position).getName());
        size_of_recipes = MainActivity.postResponses.get(recipe_position).getSteps().size();
        intializeRecipe(position, view);
    }

    private void intializeRecipe(int position, View view) {
        if (position >= 0 && position < MainActivity.postResponses.get(recipe_position).getSteps().size()) {
            step = null;
            step = MainActivity.postResponses.get(recipe_position).getSteps().get(position);
            if (!(step.getVideoURL().equals("") && step.getVideoURL().isEmpty())) {
                exoPlayer_recipeVideo.setVisibility(View.VISIBLE);
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector =
                        new DefaultTrackSelector(videoTrackSelectionFactory);

                player =
                        ExoPlayerFactory.newSimpleInstance(view.getContext(), trackSelector);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(view.getContext(),
                        Util.getUserAgent(view.getContext(), getActivity().getString(R.string.app_name)), null);
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(step.getVideoURL()));
                player.prepare(videoSource);
                if(videoPosition == null){
                    videoPosition = 0L;
                }
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
    public void onResume() {
        super.onResume();
        if (player != null)
            player.setPlayWhenReady(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
            player.release();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("videoPosition",player.getCurrentPosition());
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
