package tech.minesoft.minetv.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.databinding.ActivityPlayerBinding;
import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.vo.UrlInfo;

public class PlayerActivity extends AppCompatActivity {

    private ActivityPlayerBinding binding;
    private PlayerView playerView;
    private ExoPlayer player;
    private TextView titleTv;
    private TextView speedTv;
    private UrlInfo info;
    private float speed = 1f;

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
        info = (UrlInfo) bundle.get(Const.SELECT_EPISODE);

        player = new ExoPlayer.Builder(this)
                .setSeekBackIncrementMs(10000)
                .setSeekForwardIncrementMs(10000)
                .build();

        playerView = binding.playerView;

        // playerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
        playerView.requestFocus();

        playerView.setPlayer(player);
        playerView.hideController();
        playerView.setBackgroundColor(getColor(R.color.black));
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        playerView.setKeepScreenOn(true);

        titleTv = playerView.findViewById(R.id.mine_title);
        speedTv = playerView.findViewById(R.id.mine_speed);

        titleTv.setText(info.getVodName() + ":" + info.getItemName());
        speedTv.setText(speed + "x");

        playerView.findViewById(R.id.mine_speed_1x).setOnClickListener(view -> {
            speed = 1;
            player.setPlaybackSpeed(speed);
            speedTv.setText(speed + "x");
        });
        playerView.findViewById(R.id.mine_speed_up).setOnClickListener(view -> {
            if (speed < 1) {
                speed = speed + 0.1f;
            } else if (speed < 5) {
                speed++;
            } else if (speed >= 5) {
                speed = 10;
            }

            player.setPlaybackSpeed(speed);
            speedTv.setText(speed + "x");
        });

        playerView.findViewById(R.id.mine_speed_down).setOnClickListener(view -> {
            if (speed >= 10) {
                speed = 5;
            } else if (speed > 1) {
                speed--;
            } else if (speed <= 1) {
                speed = speed - 0.1f;
            } else if (speed <= 0.2) {
                speed = 0.2f;
            }

            player.setPlaybackSpeed(speed);
            speedTv.setText(speed + "x");
        });

        String[] urls = info.getUrls();
        int index = 0;
        List<MediaItem> modelList = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];

            if (url.equals(info.getUrl())) {
                index = i;
            }

            String[] urlInfos = TextUtils.split(url, "\\$");

            MediaItem.Builder builder = new MediaItem.Builder();
            builder.setUri(urlInfos[1]);
            builder.setMediaMetadata(new MediaMetadata.Builder().setSubtitle(urlInfos[0]).build());
            modelList.add(builder.build());
        }

        // // Build the media item.
        // MediaItem mediaItem = MediaItem.fromUri(info.getPlayUrl());
        // // Set the media item to be played.
        // player.setMediaItem(mediaItem);

        player.setMediaItems(modelList, index, 0);
        // Prepare the player.
        player.prepare();
        // Start the playback.
        player.play();

        player.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                String subtitle = mediaItem.mediaMetadata.subtitle.toString();
                titleTv.setText(info.getVodName() + ":" + subtitle);
                DaoHelper.addView(info, subtitle);
            }
        });

        timeHandler.sendMessage(new Message());
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction()) {
            int keyCode = event.getKeyCode();

            if (playerView.isControllerVisible()) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK: // 4
                    case KeyEvent.KEYCODE_MENU: // 82
                        playerView.hideController();
                        return true;
                }
            } else {
                switch (keyCode) {
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
                    case KeyEvent.KEYCODE_DPAD_CENTER:  // 23
                    case KeyEvent.KEYCODE_ENTER: // 66
                        if (player.isPlaying()) {
                            player.pause();
                        } else {
                            player.play();
                        }
                    case KeyEvent.KEYCODE_DPAD_UP: // 19
                    case KeyEvent.KEYCODE_DPAD_DOWN: // 20
                    case KeyEvent.KEYCODE_DPAD_LEFT: // 21
                    case KeyEvent.KEYCODE_DPAD_RIGHT:  // 22
                    case KeyEvent.KEYCODE_MENU: // 82
                        playerView.showController();
                        return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
