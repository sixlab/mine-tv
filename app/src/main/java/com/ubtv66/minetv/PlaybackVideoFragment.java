package com.ubtv66.minetv;

import android.net.Uri;
import android.os.Bundle;

import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.app.VideoSupportFragmentGlueHost;
import androidx.leanback.media.MediaPlayerAdapter;
import androidx.leanback.media.MinePlayer;
import androidx.leanback.widget.PlaybackControlsRow;

import com.ubtv66.minetv.vo.VodInfo;

/**
 * Handles video playback with media controls.
 */
public class PlaybackVideoFragment extends VideoSupportFragment {

    private MinePlayer<MediaPlayerAdapter> mTransportControlGlue;
    private int seconds = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final VodInfo info =
                (VodInfo) getActivity().getIntent().getSerializableExtra(VodItemActivity.MOVIE);

        VideoSupportFragmentGlueHost glueHost =
                new VideoSupportFragmentGlueHost(PlaybackVideoFragment.this);

        MediaPlayerAdapter playerAdapter = new MediaPlayerAdapter(getContext());
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE);

        mTransportControlGlue = new MinePlayer<>(getContext(), playerAdapter);
        mTransportControlGlue.setHost(glueHost);
        mTransportControlGlue.setTitle(info.getVodName());
        mTransportControlGlue.playWhenPrepared();

        // TODO 播放器地址
        playerAdapter.setDataSource(Uri.parse(info.getVodIntro()));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTransportControlGlue != null) {
            mTransportControlGlue.pause();
        }
    }
}
