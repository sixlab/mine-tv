package tech.minesoft.minetv.page.play;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.leanback.app.MineVideoFragment;
import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.app.VideoSupportFragmentGlueHost;
import androidx.leanback.media.MediaPlayerAdapter;
import androidx.leanback.media.MinePlayer;
import androidx.leanback.media.PlaybackGlue;
import androidx.leanback.widget.PlaybackControlsRow;

import tech.minesoft.minetv.page.detail.VodDetailActivity;
import tech.minesoft.minetv.vo.UrlInfo;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

/**
 * Handles video playback with media controls.
 */
public class PlaybackVideoFragment extends MineVideoFragment {

    private MinePlayer<MediaPlayerAdapter> mTransportControlGlue;
    private Handler timeHandler = new Handler();
    private Runnable runnable;
    private SurfaceHolder holder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final UrlInfo info =
                (UrlInfo) getActivity().getIntent().getSerializableExtra(VodDetailActivity.MOVIE);

        VideoSupportFragmentGlueHost glueHost = new VideoSupportFragmentGlueHost((VideoSupportFragment) PlaybackVideoFragment.this);

        MediaPlayerAdapter playerAdapter = new MediaPlayerAdapter(getContext());
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE);

        mTransportControlGlue = new MinePlayer<>(getContext(), playerAdapter);
        mTransportControlGlue.setHost(glueHost);
        mTransportControlGlue.setTitle(info.getVodName());
        mTransportControlGlue.setSubtitle("[" + info.getGroupName() + "]" + info.getItemName());
        mTransportControlGlue.playWhenPrepared();
        mTransportControlGlue.addPlayerCallback(new PlaybackGlue.PlayerCallback() {
            @Override
            public void onPlayCompleted(PlaybackGlue glue) {
                super.onPlayCompleted(glue);
                ((PlaybackActivity) getContext()).finish();
            }
        });

        // TODO 播放器地址
        playerAdapter.setDataSource(Uri.parse(info.getPlayUrl()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);

        SurfaceView logoView = new SurfaceView(getContext());
        logoView.setZOrderOnTop(true);
        logoView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        root.addView(logoView, 0);

        SurfaceView timeView = new SurfaceView(getContext());
        timeView.setZOrderOnTop(true);
        timeView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        root.addView(timeView, 0);
        this.holder = timeView.getHolder();

        runnable = new DrawTime();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    timeHandler.postDelayed(runnable, 1000);
                } catch (Exception e) {
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });

        logoView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas canvas = holder.lockCanvas();

                Paint textPaint = new Paint();
                textPaint.setColor(ContextCompat.getColor(getContext(), tech.minesoft.minetv.R.color.bl_yellow));
                textPaint.setTextSize(50);
                canvas.drawText(getString(tech.minesoft.minetv.R.string.app_name), 100, 200, textPaint);

                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        return root;
    }

    public void backward() {
        mTransportControlGlue.backward(mTransportControlGlue.getSeconds());
    }

    public void forward() {
        mTransportControlGlue.forward(mTransportControlGlue.getSeconds());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTransportControlGlue != null) {
            mTransportControlGlue.pause();
        }
    }

    @Override
    public void onDestroy() {
        timeHandler.removeCallbacks(runnable);
        runnable = null;
        super.onDestroy();
    }

    private class DrawTime implements Runnable {
        @Override
        public void run() {
            try {
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                String date = df.format(new java.util.Date());

                String current = timeParse(mTransportControlGlue.getCurrentPosition());
                String duration = timeParse(mTransportControlGlue.getDuration());

                String text = MessageFormat.format("{0}({1}/{2})", date, current, duration);

                Canvas canvas = holder.lockCanvas(null);

                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                Paint timePaint = new Paint();
                timePaint.setColor(Color.WHITE);
                timePaint.setTextSize(25);
                canvas.drawText(text, 100, 250, timePaint);

                holder.unlockCanvasAndPost(canvas);

                timeHandler.postDelayed(this, 1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static String timeParse(long duration) {
        String time = "";

        long minute = duration / 60000;
        long seconds = duration % 60000;

        long second = Math.round((float) seconds / 1000);

        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";

        if (second < 10) {
            time += "0";
        }
        time += second;

        return time;
    }
}
