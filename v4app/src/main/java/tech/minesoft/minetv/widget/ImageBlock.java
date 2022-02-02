package tech.minesoft.minetv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import tech.minesoft.minetv.R;
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

        setFocusable(true);

        setOnFocusChangeListener((view, focus) -> {
            if (focus) {
                setBackgroundColor(getResources().getColor(R.color.mtv_main));
                binding.vodTitle.setTextColor(getResources().getColor(R.color.white));
                binding.vodDesc.setTextColor(getResources().getColor(R.color.white));

                Toast.makeText(context, binding.vodTitle.getText(), Toast.LENGTH_SHORT).show();
            }else{
                setBackgroundResource(0);
                binding.vodTitle.setTextColor(getResources().getColor(R.color.black));
                binding.vodDesc.setTextColor(getResources().getColor(R.color.black));
            }
        });
    }
}
