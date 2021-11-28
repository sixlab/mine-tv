package tech.minesoft.minetv.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.base.BaseActivity;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.vo.UrlInfo;
import tech.minesoft.minetv.vo.VideoPlayerModel;
import tech.minesoft.minetv.widget.MinePlayer;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager;

public class PlayerActivity extends BaseActivity {
    private MinePlayer videoPlayer;
    private TextView durationTv;
    private LinearLayout playerMenu;

    private boolean menuShown = false;
    private int menuIndex = 0;

    private static final int[] MENU_BTNS = {
            R.id.player_menu_next,
            R.id.player_menu_prev,
    };
    private static final int MENU_LENGTH = MENU_BTNS.length;

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

        videoPlayer.requestFocus();
    }

    private void init(UrlInfo info) {
        durationTv = findViewById(R.id.video_tips_duration);
        videoPlayer = findViewById(R.id.video_player);
        playerMenu = findViewById(R.id.player_menu);

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

        videoPlayer.setUp(modelList, true, index);
        // videoPlayer.setUp(info.getPlayUrl(), true, title);

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

        findViewById(R.id.player_menu_next).setOnClickListener(v -> {
            videoPlayer.playNext();
        });

        findViewById(R.id.player_menu_prev).setOnClickListener(v -> {
            videoPlayer.playPrev();
        });

//        TestListener testListener = new TestListener(this.getApplicationContext());
//        findViewById(R.id.player_menu_prev).setOnKeyListener(testListener);
//        findViewById(R.id.player_menu_next).setOnKeyListener(testListener);
//        findViewById(R.id.activity_play).setOnKeyListener(testListener);
//        findViewById(R.id.video_player).setOnKeyListener(testListener);
//        findViewById(R.id.player_menu).setOnKeyListener(testListener);
//        findViewById(R.id.video_tips_logo).setOnKeyListener(testListener);
//        findViewById(R.id.video_tips_time).setOnKeyListener(testListener);
//        findViewById(R.id.video_tips_duration).setOnKeyListener(testListener);

        // findViewById(R.id.player_menu_full).setOnClickListener(v -> {
        //     showText("player_menu_full");
        // });
        //
        // findViewById(R.id.player_menu_origin).setOnClickListener(v -> {
        //     showText("player_menu_origin");
        // });
        //
        // findViewById(R.id.player_menu_ls).setOnClickListener(v -> {
        //     showText("player_menu_ls");
        // });
        //
        // findViewById(R.id.player_menu_169).setOnClickListener(v -> {
        //     showText("16:9_"+menuIndex);
        // });
        //
        // findViewById(R.id.player_menu_43).setOnClickListener(v -> {
        //     showText("5:4_"+ menuIndex);
        // });
        //
        // findViewById(R.id.player_menu_direction).setOnClickListener(v -> {
        //     direction = -direction;
        // });

        menuIndex = 0;
        menuShown = false;
        renderMenuBtnColor();
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
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }
        super.onDestroy();
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

    private Toast toast = null;

    @SuppressLint("ShowToast")
    public void showText(CharSequence text) {
        try {
            toast.getView().isShown();
            toast.setText(text);
        } catch (Exception e) {
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            if(KeyEvent.KEYCODE_MENU == keyCode){
                toggleMenu();
                videoPlayer.playPause();
                return true;
            }

            if(menuShown){
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        Button view = findViewById(MENU_BTNS[menuIndex]);
                        view.performClick();
                        toggleMenu();
                        return true;

                    case KeyEvent.KEYCODE_BACK:
                        toggleMenu();
                        return true;

                    case KeyEvent.KEYCODE_DPAD_UP:
                        menuIndex--;
                        if (menuIndex < 0) {
                            menuIndex = MENU_LENGTH - 1;
                        }
                        renderMenuBtnColor();
                        return true;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        menuIndex++;
                        if (menuIndex >= MENU_LENGTH) {
                            menuIndex = 0;
                        }
                        renderMenuBtnColor();
                        return true;
                }
            }else{
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        videoPlayer.playPause();
                        return true;

                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        videoPlayer.seekDuration(-5);
                        return true;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        videoPlayer.seekDuration(5);
                        return true;

                    case KeyEvent.KEYCODE_DPAD_UP:
                        videoPlayer.seekDuration(120);
                        return true;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        videoPlayer.seekDuration(-120);
                        return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void renderMenuBtnColor() {
        for (int menuBtn : MENU_BTNS) {
            findViewById(menuBtn).setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }

        findViewById(MENU_BTNS[menuIndex]).setBackgroundColor(getResources().getColor(R.color.bl_blue));
    }

    private void toggleMenu() {
        menuShown = !menuShown;
        playerMenu.setVisibility(menuShown ? View.VISIBLE : View.INVISIBLE);
        if (menuShown) {
            playerMenu.requestFocus();
        }else {
            videoPlayer.requestFocus();
        }
    }
}
