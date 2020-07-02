package androidx.leanback.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.leanback.widget.Action;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;

public class MinePlayer<T extends PlayerAdapter> extends PlaybackTransportControlGlue<T> {

    private static Toast toast = null;

    @SuppressLint("ShowToast")
    public void show(CharSequence text, int toastDuration){
        try{
            toast.getView().isShown();
            toast.setText(text);
        } catch (Exception e) {
            toast = Toast.makeText(getContext(), text, toastDuration);
        }
        toast.show();
    }

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
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_ESCAPE:
                return false;
        }

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

    /**

     switch (keyCode) {
     case KeyEvent.KEYCODE_DPAD_RIGHT:  //向右键
     Log.d("key", "right--->");
     fragment.forward();
     break;
     case KeyEvent.KEYCODE_DPAD_LEFT: //向左键
     Log.d("key", "left--->");
     fragment.backward();
     break;
     case KeyEvent.KEYCODE_MENU: //向左键
     Log.d("key", "menu--->");
     fragment.updateSeconds();
     break;
     }
     */


    // KeyEvent.KEYCODE_ENTER; //66 不确定
    //
    // HOME 键 class  HomeRecaiver extends BroadcastReceiver{ onReceive
    // action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
    // SYSTEM_DIALOG_REASON_HOME_KEY  equal intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
    //
    // KeyEvent.KEYCODE_BACK; //4
    // KeyEvent.KEYCODE_MENU; //82
    // KeyEvent.KEYCODE_F5; //135 语音
    //
    // KeyEvent.KEYCODE_DPAD_UP; //19
    // KeyEvent.KEYCODE_DPAD_DOWN; // 20
    // KeyEvent.KEYCODE_DPAD_LEFT; // 21
    // KeyEvent.KEYCODE_DPAD_RIGHT; // 22
    //
    // KeyEvent.KEYCODE_VOLUME_UP; // 24
    // KeyEvent.KEYCODE_VOLUME_DOWN; // 25
    //
    //
    //
    //
    // public void forward() {
    //     if (mTransportControlGlue != null) {
    //         long currentPosition = mTransportControlGlue.getCurrentPosition();
    //         mTransportControlGlue.seekTo(currentPosition + seconds*1000);
    //     }
    // }
    //
    // public void backward() {
    //     if (mTransportControlGlue != null) {
    //         long currentPosition = mTransportControlGlue.getCurrentPosition();
    //         mTransportControlGlue.seekTo(currentPosition - seconds*1000);
    //     }
    // }
    //
    // public void updateSeconds() {
    //     seconds+=10;
    //     Toast.makeText(getActivity(), "当前快进/快退间隔 " +seconds +" 秒", Toast.LENGTH_LONG).show();
    // }
}
