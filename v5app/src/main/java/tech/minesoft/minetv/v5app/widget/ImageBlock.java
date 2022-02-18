package tech.minesoft.minetv.v5app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import tech.minesoft.minetv.v5app.R;
import tech.minesoft.minetv.v5app.databinding.WidgetImageBlockBinding;

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

        binding.vodTitle.setTextColor(context.getColor(R.color.white));
        binding.vodDesc.setTextColor(context.getColor(R.color.white));

        setFocusable(true);

        setOnFocusChangeListener((view, focus) -> {
            if (focus) {
                setBackgroundColor(context.getColor(R.color.mtv_selected));
                Toast.makeText(context, binding.vodTitle.getText(), Toast.LENGTH_SHORT).show();
            }else{
                setBackgroundResource(0);
            }
        });
    }
}
