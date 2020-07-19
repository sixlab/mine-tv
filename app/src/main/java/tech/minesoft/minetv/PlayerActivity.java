package tech.minesoft.minetv;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import org.jetbrains.annotations.NotNull;

import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.v2.base.BaseActivity;
import tech.minesoft.minetv.vo.UrlInfo;
import tech.minesoft.minetv.widget.MinePlayer;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager;

public class PlayerActivity extends BaseActivity {
    private static String TAG = "PlayerActivity";

    private MinePlayer videoPlayer;
    private TextView durationTv;

    OrientationUtils orientationUtils;
    private Handler timeHandler = new MyHandler();

    private Runnable runnable = () -> timeHandler.sendMessage(new Message());

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(@NotNull Message msg) {
            String times = videoPlayer.getTimes();
            durationTv.setText(times);

            timeHandler.postDelayed(runnable, 1000);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        CacheFactory.setCacheManager(ExoPlayerCacheManager.class);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        GSYVideoType.setRenderType(GSYVideoType.SUFRACE);

        setContentView(R.layout.activity_player);
        UrlInfo info = (UrlInfo) getIntent().getSerializableExtra(Const.SELECT_EPISODE);
        init(info);

        timeHandler.sendMessage(new Message());
    }

    private void init(UrlInfo info) {
        durationTv = findViewById(R.id.video_tips_duration);
        videoPlayer = findViewById(R.id.video_player);

        String title = info.getVodName() + ":" + info.getItemName();
        videoPlayer.setUp(info.getPlayUrl(), true, title);

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.play);
        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);

        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(v -> orientationUtils.resolveByClick());

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(v -> onBackPressed());
        videoPlayer.startPlayLogic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }
    }

    @Override
    public void onBackPressed() {
        //释放所有
        new AlertDialog.Builder(this)
                .setMessage("确定退出？")
                .setNegativeButton("确定", (dialog, id) -> {
                    videoPlayer.setVideoAllCallBack(null);
                    super.onBackPressed();
                })
                .setPositiveButton("取消", null)
                .show();
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    videoPlayer.forward();
                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    videoPlayer.backward();
                    return true;
                case KeyEvent.KEYCODE_DPAD_UP:
                    videoPlayer.fastForward();
                    return true;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    videoPlayer.fastBackward();
                    return true;
                case KeyEvent.KEYCODE_MENU:
                    videoPlayer.seekDuration(120);
                    break;
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    videoPlayer.playPause();
                    break;
                default:
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
