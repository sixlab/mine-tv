package tech.minesoft.minetv.v5app.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

import tech.minesoft.minetv.v5app.R;

public class MineVideoPlayer extends StandardGSYVideoPlayer {
    public MineVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MineVideoPlayer(Context context) {
        super(context);
    }

    public MineVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        getFullscreenButton().setVisibility(View.GONE);
        getTitleTextView().setVisibility(View.VISIBLE);
        getBackButton().setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.mine_video_player;
    }

    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false;

        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false;

        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false;
    }

    @Override
    protected void touchDoubleUp(MotionEvent e) {
        //super.touchDoubleUp();
        //不需要双击暂停
    }

    public void setUp(List<GSYVideoModel> modelList, int index) {
        GSYVideoModel model = modelList.get(index);
        setUp(model.getUrl(), true, model.getTitle());
    }

    @Override
    public void changeUiToNormal() {

        mBottomContainer.isShown();
        super.changeUiToNormal();
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
    }

    @Override
    public void changeUiToClear() {
        super.changeUiToClear();
    }

    @Override
    public void hideAllWidget() {
        super.hideAllWidget();
    }

    public boolean isControllerShown() {
        return mBottomContainer.isShown();
    }

    public void onReplay() {
        startPlayLogic();
        mSeekTimePosition = 0;
        mProgressBar.setProgress(0);
    }

    public void seekDuration(int duration) {
        long playPosition = getGSYVideoManager().getCurrentPosition() + duration * 1000;
        int total = getDuration();
        if (playPosition < 0) {
            playPosition = 0;
        } else if (playPosition > total) {
            playPosition = total - 60000;
        }
        seekTo(playPosition);
        mCurrentPosition = playPosition;
    }

}
