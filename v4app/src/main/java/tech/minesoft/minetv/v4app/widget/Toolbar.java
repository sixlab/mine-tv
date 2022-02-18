package tech.minesoft.minetv.v4app.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import tech.minesoft.minetv.v4app.R;
import tech.minesoft.minetv.v4app.activity.HomeActivity;
import tech.minesoft.minetv.v4app.activity.SearchActivity;
import tech.minesoft.minetv.v4app.activity.SettingActivity;
import tech.minesoft.minetv.v4app.databinding.WidgetToolbarBinding;
import tech.minesoft.minetv.v4app.utils.Const;

public class Toolbar extends LinearLayout {
    private WidgetToolbarBinding binding;

    private final Handler timeHandler = new MyHandler();
    private final Runnable runnable = () -> timeHandler.sendMessage(new Message());
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(@NotNull Message msg) {
            binding.times.setText(new SimpleDateFormat("HH:mm").format(new Date()));

            timeHandler.postDelayed(runnable, 1000);
        }
    }

    public Toolbar(Context context) {
        super(context);
        this.init(context, null, 0, 0);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs, 0, 0);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs, defStyleAttr, 0);
    }

    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        View inflate = inflate(context, R.layout.widget_toolbar,this);
        binding = WidgetToolbarBinding.bind(inflate);

        binding.homeBtn.setOnClickListener(view -> {
            Intent intent=new Intent(context, HomeActivity.class);
            context.startActivity(intent);
        });

        binding.starBtn.setOnClickListener(view -> {
            Intent intent=new Intent(context, HomeActivity.class);
            intent.putExtra(Const.ACTIVITY_STAR, true);
            context.startActivity(intent);
        });

        binding.searchBtn.setOnClickListener(view -> {
            Intent intent=new Intent(context, SearchActivity.class);
            context.startActivity(intent);
        });

        binding.settingBtn.setOnClickListener(view -> {
            Intent intent=new Intent(context, SettingActivity.class);
            context.startActivity(intent);
        });

        timeHandler.sendMessage(new Message());
    }

    public void setTitle(String text){
        binding.title.setText(text);
    }

    public void setTitle(int resId){
        binding.title.setText(resId);
    }

}
