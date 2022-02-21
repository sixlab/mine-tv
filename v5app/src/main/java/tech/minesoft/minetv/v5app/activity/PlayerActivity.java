package tech.minesoft.minetv.v5app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.minesoft.minetv.v5app.bean.MineViewInfo;
import tech.minesoft.minetv.v5app.databinding.ActivityPlayerBinding;
import tech.minesoft.minetv.v5app.greendao.DaoHelper;
import tech.minesoft.minetv.v5app.player.MineVideoPlayer;
import tech.minesoft.minetv.v5app.utils.Const;
import tech.minesoft.minetv.v5app.utils.TimeUtils;
import tech.minesoft.minetv.v5app.vo.UrlInfo;
import tech.minesoft.minetv.v5app.vo.VideoPlayerModel;

public class PlayerActivity extends AppCompatActivity {

    private ActivityPlayerBinding binding;
    private MineVideoPlayer videoPlayer;

    private MineViewInfo viewInfo;
    private UrlInfo info;

    private TextView speedTv;
    private float speed = 1f;

    private Handler timeHandler = new MyHandler();
    private Runnable runnable = () -> timeHandler.sendMessage(new Message());

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(@NotNull Message msg) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String t = formatter.format(new Date().getTime());

            long position = videoPlayer.getCurrentPositionWhenPlaying();
            String p = TimeUtils.hh_mm_ss(position);
            viewInfo.setView_position(position);
            DaoHelper.updateView(viewInfo);

            long duration = videoPlayer.getDuration();
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

        videoPlayer = binding.videoPlayer;

        // videoPlayer.setOnKeyListener((view, keyCode, event) -> {
        //
        //     Toast.makeText(PlayerActivity.this, "Listen:" + keyCode + ">" + event.getAction(), Toast.LENGTH_SHORT).show();
        //
        //     return false;
        // });

        String[] urls = info.getUrls();
        int index = 0;
        List<GSYVideoModel> modelList = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];

            if (url.equals(info.getUrl())) {
                index = i;
            }

            String[] urlInfos = TextUtils.split(url, "\\$");

            VideoPlayerModel videoModel = new VideoPlayerModel(urlInfos[1], info.getVodName() + ":" + urlInfos[0]);
            videoModel.setUrlInfo(info);
            videoModel.setItemName(urlInfos[0]);
            modelList.add(videoModel);
        }
        videoPlayer.setUp(modelList, index);

        // //增加封面
        // ImageView imageView = new ImageView(this);
        // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // imageView.setImageResource(R.drawable.play);
        // videoPlayer.setThumbImageView(imageView);

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);

        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(v -> onBackPressed());
        videoPlayer.startPlayLogic();

        // 恢复上次进度
        videoPlayer.seekTo(viewInfo.getView_position());

        // playerView.hideController();
        // playerView.setBackgroundColor(getColor(R.color.black));
        // playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        // playerView.setKeepScreenOn(true);

        // playerView.findViewById(R.id.mine_speed_1x).setOnClickListener(view -> {
        //     speed = 1;
        //     player.setPlaybackSpeed(speed);
        //     speedTv.setText(speed + "x");
        // });
        // playerView.findViewById(R.id.mine_speed_up).setOnClickListener(view -> {
        //     if (speed < 1) {
        //         speed = new BigDecimal(String.valueOf(speed)).add(new BigDecimal("0.1")).floatValue();
        //     } else {
        //         speed++;
        //     }
        //
        //     if (speed >= 5) {
        //         speed = 5;
        //     }
        //
        //     player.setPlaybackSpeed(speed);
        //     speedTv.setText(speed + "x");
        // });
        //
        // playerView.findViewById(R.id.mine_speed_down).setOnClickListener(view -> {
        //     if (speed > 1) {
        //         speed--;
        //     } else {
        //         speed = new BigDecimal(String.valueOf(speed)).subtract(new BigDecimal("0.1")).floatValue();
        //     }
        //
        //     if (speed <= 0.2) {
        //         speed = 0.2f;
        //     }
        //
        //     player.setPlaybackSpeed(speed);
        //     speedTv.setText(speed + "x");
        // });

        // player.addListener(new Player.Listener() {
        //     @Override
        //     public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
        //         String subtitle = mediaItem.mediaMetadata.subtitle.toString();
        //         titleTv.setText(info.getVodName() + ":" + subtitle);
        //
        //         info.setItemName(subtitle);
        //
        //         viewInfo = DaoHelper.addView(info);
        //     }
        //
        //     @Override
        //     public void onPlayerError(PlaybackException error) {
        //         Toast.makeText(PlayerActivity.this, "error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
        //     }
        // });

        timeHandler.sendMessage(new Message());
    }

    @Override
    protected void onPause() {
        videoPlayer.onVideoPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        videoPlayer.onVideoResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        GSYVideoManager.releaseAllVideos();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //释放所有
        Button dialogBtn = new AlertDialog.Builder(this)
                .setMessage("确定退出？")
                .setNegativeButton("确定", (dialog, id) -> {
                    videoPlayer.setVideoAllCallBack(null);
                    super.onBackPressed();
                })
                .setPositiveButton("取消", null)
                .show().getButton(DialogInterface.BUTTON_NEGATIVE);

        if (dialogBtn != null) dialogBtn.requestFocus();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction()) {
            int keyCode = event.getKeyCode();

            if (videoPlayer.isControllerShown()) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_MENU: // 82
                    case KeyEvent.KEYCODE_BACK: // 4
                    case KeyEvent.KEYCODE_DPAD_DOWN: // 20
                        videoPlayer.hideAllWidget();
                        break;
                }
            } else {
                // case KeyEvent.KEYCODE_DPAD_UP: // 19
                videoPlayer.changeUiToPlayingShow();
            }

            int duration = 1;
            switch (keyCode) {
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE: //85
                case KeyEvent.KEYCODE_DPAD_CENTER:  // 23
                case KeyEvent.KEYCODE_ENTER: // 66
                    videoPlayer.changeUiToPlayingShow();

                    int state = videoPlayer.getCurrentState();
                    switch (state) {
                        case GSYVideoPlayer.CURRENT_STATE_PLAYING:
                            videoPlayer.onVideoPause();
                            break;
                        case GSYVideoPlayer.CURRENT_STATE_PAUSE:
                            videoPlayer.onVideoResume();
                            break;
                        case GSYVideoPlayer.CURRENT_STATE_AUTO_COMPLETE:
                            videoPlayer.onReplay();
                            break;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT: // 21
                    duration = -1;
                case KeyEvent.KEYCODE_DPAD_RIGHT:  // 22
                    if (0 == event.getRepeatCount()) {
                        duration = duration * 10;
                    } else {
                        duration = duration * 15;
                    }
                    videoPlayer.seekDuration(duration);
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
