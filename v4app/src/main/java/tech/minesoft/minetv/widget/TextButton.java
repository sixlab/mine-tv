package tech.minesoft.minetv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import tech.minesoft.minetv.R;

public class TextButton extends AppCompatButton implements View.OnFocusChangeListener {
    private Context context;
    private int normalColor;
    public TextButton(@NonNull Context context) {
        super(context);
        this.init(context, null, 0);
    }

    public TextButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs, 0);
    }

    public TextButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        this.context = context;
        setNormalStyle();
        // setFocusableInTouchMode(true);
        setOnFocusChangeListener(this);
        normalColor = context.getColor(R.color.mtv_btn_normal);
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    @Override
    public void onFocusChange(View view, boolean focus) {
        if (focus) {
            setFocusStyle();
        }else{
            setNormalStyle();
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    private void setFocusStyle() {
        setBackgroundColor(context.getColor(R.color.mtv_selected));
    }

    private void setNormalStyle() {
        setBackgroundColor(normalColor);
    }
}
