package androidx.leanback.app;

import android.app.AlertDialog;
import android.view.InputEvent;
import android.view.KeyEvent;

import com.ubtv66.minetv.page.play.PlaybackActivity;

public class MineVideoFragment extends VideoSupportFragment {

    @Override
    boolean onInterceptInputEvent(InputEvent event) {
        final boolean controlsHidden = !mControlVisible;
        boolean consumeEvent = false;
        int keyCode = KeyEvent.KEYCODE_UNKNOWN;
        int keyAction = 0;

        if (event instanceof KeyEvent) {
            keyCode = ((KeyEvent) event).getKeyCode();
            keyAction = ((KeyEvent) event).getAction();
            if (mInputEventHandler != null) {
                consumeEvent = mInputEventHandler.onKey(getView(), keyCode, (KeyEvent) event);
            }
        }

        if (!consumeEvent) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_DPAD_DOWN:
                case KeyEvent.KEYCODE_DPAD_UP:
                    // Event may be consumed; regardless, if controls are hidden then these keys will
                    // bring up the controls.
                    if (controlsHidden) {
                        consumeEvent = true;
                    }
                    if (keyAction == KeyEvent.ACTION_DOWN) {
                        tickle();
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (controlsHidden) {
                        consumeEvent = true;
                        if (keyAction == KeyEvent.ACTION_DOWN) {
                            //   快退
                            backward();
                        }
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    // Event may be consumed; regardless, if controls are hidden then these keys will
                    // bring up the controls.
                    if (controlsHidden) {
                        consumeEvent = true;
                        if (keyAction == KeyEvent.ACTION_DOWN) {
                            //   快进
                            forward();
                        }
                    }
                    break;
                case KeyEvent.KEYCODE_BACK:
                case KeyEvent.KEYCODE_ESCAPE:
                    if (mInSeek) {
                        // when in seek, the SeekUi will handle the BACK.
                        return false;
                    }
                    // If controls are not hidden, back will be consumed to fade
                    // them out (even if the key was consumed by the handler).

                    consumeEvent = true;

                    if (!controlsHidden) {
                        // 显示
                        if (((KeyEvent) event).getAction() == KeyEvent.ACTION_UP) {
                            hideControlsOverlay(true);
                        }
                    } else {
                        // 隐藏
                        if (((KeyEvent) event).getAction() == KeyEvent.ACTION_DOWN) {
                            new AlertDialog.Builder(getContext())
                                    .setMessage("确定退出？")
                                    .setNegativeButton("确定", (dialog, id) -> ((PlaybackActivity) getContext()).finish())
                                    .setPositiveButton("取消", null)
                                    .show();
                        }
                    }
                    break;
                default:
                    if (consumeEvent) {
                        if (keyAction == KeyEvent.ACTION_DOWN) {
                            tickle();
                        }
                    }
            }
        }
        return consumeEvent;
    }

    protected void backward() {

    }

    protected void forward() {

    }
}
