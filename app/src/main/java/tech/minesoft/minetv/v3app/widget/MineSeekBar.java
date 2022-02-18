package tech.minesoft.minetv.v3app.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.appcompat.widget.AppCompatSeekBar;

public class MineSeekBar extends AppCompatSeekBar {
    public MineSeekBar(Context context) {
        super(context);
    }

    public MineSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MineSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
