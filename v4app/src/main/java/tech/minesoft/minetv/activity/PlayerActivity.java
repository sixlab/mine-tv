package tech.minesoft.minetv.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import tech.minesoft.minetv.databinding.ActivityPlayerBinding;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.vo.UrlInfo;

public class PlayerActivity extends AppCompatActivity {

    private ActivityPlayerBinding binding;
    private StyledPlayerView playerView;
    private ExoPlayer player;

    private Handler timeHandler = new MyHandler();

    private Runnable runnable = () -> timeHandler.sendMessage(new Message());

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(@NotNull Message msg) {
            long duration = player.getDuration();
            long position = player.getCurrentPosition();

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String t = formatter.format(new Date().getTime());
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            String d = formatter.format(duration);
            String p = formatter.format(position);

            binding.timeTv.setText(t + "\n" + p + "/" + d);

            timeHandler.postDelayed(runnable, 1000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        UrlInfo info = (UrlInfo) bundle.get(Const.SELECT_EPISODE);

        player = new ExoPlayer.Builder(this).build();

        playerView = binding.playerView;

        // playerView.setControllerVisibilityListener(this);
        // playerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
        playerView.requestFocus();

        playerView.setPlayer(player);
        playerView.hideController();

        // Build the media item.
        MediaItem mediaItem = MediaItem.fromUri(info.getPlayUrl());

        // Set the media item to be played.
        player.setMediaItem(mediaItem);
        // Prepare the player.
        player.prepare();
        // Start the playback.
        player.play();

        timeHandler.sendMessage(new Message());
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Toast.makeText(PlayerActivity.this, event.getAction() + ":::" + event.getKeyCode(), Toast.LENGTH_SHORT).show();
        if(KeyEvent.ACTION_DOWN == event.getAction()){
            int keyCode = event.getKeyCode();
            switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_UP: // 19
                    playerView.showController();
                    return true;
                case KeyEvent.KEYCODE_DPAD_DOWN: // 20
                    playerView.hideController();
                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT: // 21
                    player.seekBack();
                    return true;
                case KeyEvent.KEYCODE_DPAD_RIGHT:  // 22
                    player.seekForward();
                    return true;
                case KeyEvent.KEYCODE_DPAD_CENTER:  // 23
                case KeyEvent.KEYCODE_ENTER: // 66
                    if (player.isPlaying()) {
                        player.pause();
                    } else {
                        player.play();
                    }
                    return true;
                case KeyEvent.KEYCODE_BACK: // 4
                    new AlertDialog.Builder(this)
                            .setMessage("是否退出？")
                            .setNegativeButton("确定", (dialog, id) -> {
                                player.release();
                                PlayerActivity.this.finish();
                            })
                            .setPositiveButton("取消", null)
                            .show();
                    return true;
                case KeyEvent.KEYCODE_MENU: // 82
                    // TODO
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
