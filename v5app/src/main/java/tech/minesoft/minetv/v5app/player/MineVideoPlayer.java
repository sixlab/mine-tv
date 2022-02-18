package tech.minesoft.minetv.v5app.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

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
}
