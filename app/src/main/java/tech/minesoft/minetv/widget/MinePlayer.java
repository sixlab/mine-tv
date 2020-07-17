package tech.minesoft.minetv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.DragEvent;
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

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "setOnClickListener");
            }
        });
        setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.i(TAG, "setOnDragListener");
                return false;
            }
        });
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG, "setOnKeyListener");
                return false;
            }
        });
        setOnContextClickListener(new OnContextClickListener() {
            @Override
            public boolean onContextClick(View v) {
                Log.i(TAG, "setOnContextClickListener");
                return false;
            }
        });
        setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                Log.i(TAG, "setOnCreateContextMenuListener");
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "keydown:"+keyCode);
        return super.onKeyDown(keyCode, event);
    }

    public void onClickUi() {
        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            onClickUiToggle();
            startDismissControlViewTimer();
        }
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
