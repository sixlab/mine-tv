package tech.minesoft.minetv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

public class MinePlayer extends StandardGSYVideoPlayer {
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
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

    public void backward() {
        seekDuration(-10);
    }

    public void forward() {
        seekDuration(10);
    }

    public void fastBackward() {
        seekDuration(-60);
    }

    public void fastForward() {
        seekDuration(60);
    }

    private void seekDuration(int duration){
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
}
