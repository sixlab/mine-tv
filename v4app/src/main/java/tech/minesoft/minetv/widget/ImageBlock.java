package tech.minesoft.minetv.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.activity.DetailActivity;
import tech.minesoft.minetv.databinding.WidgetImageBlockBinding;

public class ImageBlock extends LinearLayout {
    private WidgetImageBlockBinding binding;

    public ImageBlock(Context context) {
        super(context);
        this.init(context, null, 0, 0);
    }

    public ImageBlock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs, 0, 0);
    }

    public ImageBlock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs, defStyleAttr, 0);
    }

    public ImageBlock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        View inflate = inflate(context, R.layout.widget_image_block,this);
        binding = WidgetImageBlockBinding.bind(inflate);

        inflate.setOnClickListener(view -> {
            Intent intent=new Intent(context, DetailActivity.class);
            intent.putExtra("vodId", 1);
            context.startActivity(intent);
        });
    }
}
