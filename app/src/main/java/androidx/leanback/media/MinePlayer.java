package androidx.leanback.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;

public class MinePlayer<T extends PlayerAdapter> extends PlaybackTransportControlGlue<T> {

    private Toast toast = null;
    private int seconds = 30;

    /**
     * Constructor for the glue.
     *
     * @param context
     * @param impl    Implementation to underlying media player.
     */
    public MinePlayer(Context context, T impl) {
        super(context, impl);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // if (event.getAction() == KeyEvent.ACTION_UP) {
        //     // 按下
        //
        // } else if (event.getAction() == KeyEvent.ACTION_DOWN) {
        //     // 起来
        //     switch (keyCode) {
        //         case KeyEvent.KEYCODE_BACK: // 返回
        //             Log.d("key", "back--->");
        //             new AlertDialog.Builder(getContext())
        //                     .setMessage("确定退出？")
        //                     .setNegativeButton("确定", (dialog, id) -> ((PlaybackActivity) getContext()).finish())
        //                     .setPositiveButton("取消", null)
        //                     .show();
        //             return true;
        //     }
        // }
        //
        // switch (keyCode) {
        //     case KeyEvent.KEYCODE_DPAD_UP:
        //     case KeyEvent.KEYCODE_DPAD_DOWN:
        //     case KeyEvent.KEYCODE_DPAD_RIGHT:
        //     case KeyEvent.KEYCODE_DPAD_LEFT:
        //     case KeyEvent.KEYCODE_BACK:
        //     case KeyEvent.KEYCODE_ESCAPE:
        //         return false;
        // }

        final ObjectAdapter primaryActionsAdapter = mControlsRow.getPrimaryActionsAdapter();
        Action action = mControlsRow.getActionForKeyCode(primaryActionsAdapter, keyCode);
        if (action == null) {
            action = mControlsRow.getActionForKeyCode(mControlsRow.getSecondaryActionsAdapter(),
                    keyCode);
        }

        if (action != null) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                dispatchAction(action, event);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onActionClicked(Action action) {
        if (action instanceof PlaybackControlsRow.ThumbsUpAction) {
            updateSeconds(seconds + 10);
        } else if (action instanceof PlaybackControlsRow.ThumbsDownAction) {
            updateSeconds(seconds - 10);
        } else if (action instanceof PlaybackControlsRow.SkipPreviousAction) {
            updateSeconds(seconds + 60);
        } else if (action instanceof PlaybackControlsRow.SkipNextAction) {
            updateSeconds(seconds - 60);
        } else if (action instanceof PlaybackControlsRow.RepeatAction) {
            updateSeconds(30);
            // } else if (action instanceof PictureInPictureAction.RewindAction) {
            //     backward();
            // } else if (action instanceof ClosedCaptioningAction.RewindAction) {
            //     backward();
            // } else if (action instanceof HighQualityAction.RewindAction) {
            //     backward();
            // } else if (action instanceof ShuffleAction.RewindAction) {
            //     backward();
            // } else if (action instanceof MoreActions.RewindAction) {
            //     backward();
        } else if (action instanceof PlaybackControlsRow.RewindAction) {
            backward(seconds);
        } else if (action instanceof PlaybackControlsRow.FastForwardAction) {
            forward(seconds);
        } else {
            super.onActionClicked(action);
        }
    }

    @Override
    boolean dispatchAction(Action action, KeyEvent keyEvent) {
        boolean handled = false;
        if (action instanceof PlaybackControlsRow.PlayPauseAction) {
            boolean canPlay = keyEvent == null
                    || keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                    || keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY;
            boolean canPause = keyEvent == null
                    || keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                    || keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PAUSE;
            //            PLAY_PAUSE    PLAY      PAUSE
            // playing    paused                  paused
            // paused     playing       playing
            // ff/rw      playing       playing   paused
            if (canPause && mIsPlaying) {
                mIsPlaying = false;
                pause();
            } else if (canPlay && !mIsPlaying) {
                mIsPlaying = true;
                play();
            }
            onUpdatePlaybackStatusAfterUserAction();
            handled = true;
        } else if (action instanceof PlaybackControlsRow.SkipNextAction) {
            next();
            handled = true;
        } else if (action instanceof PlaybackControlsRow.SkipPreviousAction) {
            previous();
            handled = true;
        }
        return handled;
    }

    @Override
    protected void onCreatePrimaryActions(ArrayObjectAdapter adapter) {
        super.onCreatePrimaryActions(adapter);
        adapter.add(new PlaybackControlsRow.RewindAction(getContext()));
        adapter.add(new PlaybackControlsRow.FastForwardAction(getContext()));

        adapter.add(new PlaybackControlsRow.MoreActions(getContext()));
    }

    @Override
    protected void onCreateSecondaryActions(ArrayObjectAdapter adapter) {
        super.onCreateSecondaryActions(adapter);
        adapter.add(new PlaybackControlsRow.RepeatAction(getContext()));
        adapter.add(new PlaybackControlsRow.ThumbsUpAction(getContext()));
        adapter.add(new PlaybackControlsRow.ThumbsDownAction(getContext()));
        adapter.add(new PlaybackControlsRow.SkipPreviousAction(getContext()));
        adapter.add(new PlaybackControlsRow.SkipNextAction(getContext()));

        adapter.add(new PlaybackControlsRow.PictureInPictureAction(getContext()));
        adapter.add(new PlaybackControlsRow.ClosedCaptioningAction(getContext()));
        adapter.add(new PlaybackControlsRow.HighQualityAction(getContext()));
        adapter.add(new PlaybackControlsRow.ShuffleAction(getContext()));
    }

    @SuppressLint("ShowToast")
    public void toast(CharSequence text) {
        try {
            toast.getView().isShown();
            toast.setText(text);
        } catch (Exception e) {
            toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
        }
        toast.show();
    }

    public int getSeconds(){
        return seconds;
    }

    public void forward(int seconds) {
        long currentPosition = getCurrentPosition();
        seekTo(currentPosition + seconds * 1000);
    }

    public void backward(int seconds) {
        long currentPosition = getCurrentPosition();
        seekTo(currentPosition - seconds * 1000);
    }

    public void updateSeconds(int seconds) {
        if (seconds <= 0) {
            seconds = 10;
        }
        this.seconds = seconds;
        toast("当前快进/快退间隔 " + seconds + " 秒");
    }

    // HOME
    // class HomeRecaiver extends BroadcastReceiver{ onReceive
    // action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
    // SYSTEM_DIALOG_REASON_HOME_KEY  equal intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
    //
    // KeyEvent.KEYCODE_BACK; //4
    // KeyEvent.KEYCODE_MENU; //82
    // KeyEvent.KEYCODE_F5; //135 语音
    //
    //
    // KeyEvent.KEYCODE_DPAD_UP; //19
    // KeyEvent.KEYCODE_DPAD_DOWN; // 20
    // KeyEvent.KEYCODE_DPAD_LEFT; // 21
    // KeyEvent.KEYCODE_DPAD_RIGHT; // 22
    //
    // KeyEvent.KEYCODE_ENTER;        // 66 不确定
    //
    // KeyEvent.KEYCODE_VOLUME_UP; // 24
    // KeyEvent.KEYCODE_VOLUME_DOWN; // 25
}
