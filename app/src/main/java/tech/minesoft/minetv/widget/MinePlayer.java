package tech.minesoft.minetv.widget;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import tech.minesoft.minetv.R;

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

    private void move(int fix) {
        Toast.makeText(mContext, ">"+fix, Toast.LENGTH_LONG).show();

        View view = new View(mContext);
        // view.setId(R.id.fullscreen);
        // view.setId(R.id.progress);
        view.setId(R.id.surface_container);
        // seekDuration(10);

        MotionEvent event;

        event = newEvent(MotionEvent.ACTION_DOWN, 500, 100);
        onTouch(view, event);

        event = newEvent(MotionEvent.ACTION_MOVE, 500 + fix, 100);
        onTouch(view, event);

        event = newEvent(MotionEvent.ACTION_UP, 500 + fix, 100);
        onTouch(view, event);
    }

    private MotionEvent newEvent(int action, int x, int y){
        // MotionEvent parameters
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        int metaState = 0;

        return MotionEvent.obtain(downTime, eventTime, action, x, y, metaState);
    }

    public void fastBackward() {
        seekDuration(-30);
    }

    public void fastForward() {
        seekDuration(30);
    }

    public void seekDuration(int duration){
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
