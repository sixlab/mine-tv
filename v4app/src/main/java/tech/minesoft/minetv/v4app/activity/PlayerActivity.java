package tech.minesoft.minetv.v4app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.minesoft.minetv.v4app.R;
import tech.minesoft.minetv.v4app.bean.MineViewInfo;
import tech.minesoft.minetv.v4app.databinding.ActivityPlayerBinding;
import tech.minesoft.minetv.v4app.greendao.DaoHelper;
import tech.minesoft.minetv.v4app.utils.Const;
import tech.minesoft.minetv.v4app.utils.TimeUtils;
import tech.minesoft.minetv.v4app.vo.UrlInfo;

public class PlayerActivity extends AppCompatActivity {

    private ActivityPlayerBinding binding;
    private PlayerView playerView;
    private ExoPlayer player;
    private TextView titleTv;
    private TextView speedTv;
    private UrlInfo info;
    private float speed = 1f;
    private MineViewInfo viewInfo;

    private Handler timeHandler = new MyHandler();

    private Runnable runnable = () -> timeHandler.sendMessage(new Message());

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(@NotNull Message msg) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String t = formatter.format(new Date().getTime());

            long position = player.getCurrentPosition();
            String p = TimeUtils.hh_mm_ss(position);
            viewInfo.setView_position(position);
            DaoHelper.updateView(viewInfo);

            long duration = player.getDuration();
            if (C.TIME_UNSET == duration) {
                duration = 0;
            }
            String d = TimeUtils.hh_mm_ss(duration);

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
        viewInfo = (MineViewInfo) bundle.get(Const.SELECT_EPISODE_VIEW);

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
                speed = new BigDecimal(String.valueOf(speed)).add(new BigDecimal("0.1")).floatValue();
            } else {
                speed++;
            }

            if (speed >= 5) {
                speed = 5;
            }

            player.setPlaybackSpeed(speed);
            speedTv.setText(speed + "x");
        });

        playerView.findViewById(R.id.mine_speed_down).setOnClickListener(view -> {
            if (speed > 1) {
                speed--;
            } else {
                speed = new BigDecimal(String.valueOf(speed)).subtract(new BigDecimal("0.1")).floatValue();
            }

            if (speed <= 0.2) {
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

        player.seekTo(viewInfo.getView_position());

        player.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                String subtitle = mediaItem.mediaMetadata.subtitle.toString();
                titleTv.setText(info.getVodName() + ":" + subtitle);

                info.setItemName(subtitle);

                viewInfo = DaoHelper.addView(info);
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                Toast.makeText(PlayerActivity.this, "error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Button dialogBtn = new AlertDialog.Builder(this)
                                .setMessage("是否退出？")
                                .setNegativeButton("确定", (dialog, id) -> {
                                    player.release();
                                    PlayerActivity.this.finish();
                                })
                                .setPositiveButton("取消", null)
                                .show().getButton(DialogInterface.BUTTON_NEGATIVE);

                        if (dialogBtn != null) dialogBtn.requestFocus();
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
