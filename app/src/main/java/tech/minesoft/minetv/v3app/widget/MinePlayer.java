package tech.minesoft.minetv.v3app.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.video.ListGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.v3app.greendao.V3DaoHelper;
import tech.minesoft.minetv.v3app.vo.VideoPlayerModel;

public class MinePlayer extends ListGSYVideoPlayer {
    private static String TAG = "MinePlayer";

    public MinePlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        init();
    }

    public MinePlayer(Context context) {
        super(context);
        init();
    }

    public MinePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        getFullscreenButton().setVisibility(View.GONE);
        // setOnClickListener(v -> {
        //     Log.i(TAG, "setOnClickListener");
        // });
        // setOnDragListener((v, event) -> {
        //     Log.i(TAG, "setOnDragListener");
        //     return false;
        // });
        // setOnKeyListener((v, keyCode, event) -> {
        //     Log.i(TAG, "setOnKeyListener");
        //     return false;
        // });
        // setOnContextClickListener(v -> {
        //     Log.i(TAG, "setOnContextClickListener");
        //     return false;
        // });
        // setOnCreateContextMenuListener((menu, v, menuInfo) -> {
        //     Log.i(TAG, "setOnCreateContextMenuListener");
        // });
    }

    public String getTimes(){
        long currentPosition = getGSYVideoManager().getCurrentPosition();
        long totalPosition = getGSYVideoManager().getDuration();

        return timeParse(currentPosition) + " / " + timeParse(totalPosition);
    }

    private String timeParse(long duration) {
        if (duration < 0) {
            duration = 0;
        }
        String time = "";

        long minute = duration / 60000;
        long seconds = duration % 60000;

        long second = Math.round((float) seconds / 1000);

        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";

        if (second < 10) {
            time += "0";
        }
        time += second;

        return time;
    }

    public void seekDuration(int duration) {
        long playPosition = getGSYVideoManager().getCurrentPosition() + duration * 1000;
        int total = getDuration();
        if (playPosition < 0) {
            playPosition = 0;
        } else if (playPosition > total) {
            playPosition = total - 60000;
        }
        seekTo(playPosition);
    }

    public void playPause() {
        int state = getCurrentState();
        switch (state) {
            case GSYVideoPlayer.CURRENT_STATE_PLAYING:
                onVideoPause();
                break;
            case GSYVideoPlayer.CURRENT_STATE_PAUSE:
                onVideoResume();
                break;
        }
    }

    @Override
    public boolean playNext() {
        boolean next = super.playNext();
        if (next) {
            Log.i(TAG, "View:" + getTitleTextView());
            VideoPlayerModel current = (VideoPlayerModel) currentModel();
            V3DaoHelper.addView(current.getUrlInfo(), current.getItemName());
        }
        return next;
    }

    /**
     * 播放上一集
     *
     * @return true表示还有上一集
     */
    public boolean playPrev() {
        if (mPlayPosition > 0) {
            mPlayPosition -= 1;
            GSYVideoModel gsyVideoModel = mUriList.get(mPlayPosition);
            mSaveChangeViewTIme = 0;
            setUp(mUriList, mCache, mPlayPosition, null, mMapHeadData, false);
            if (!TextUtils.isEmpty(gsyVideoModel.getTitle()) && mTitleTextView != null) {
                mTitleTextView.setText(gsyVideoModel.getTitle());
            }
            startPlayLogic();

            Log.i(TAG, "View:" + getTitleTextView());
            VideoPlayerModel current = (VideoPlayerModel) currentModel();
            V3DaoHelper.addView(current.getUrlInfo(), current.getItemName());

            return true;
        }
        return false;
    }

    public GSYVideoModel currentModel(){
        return mUriList.get(mPlayPosition);
    }

    @Override
    public int getLayoutId() {
        return R.layout.mine_player;
    }
}
