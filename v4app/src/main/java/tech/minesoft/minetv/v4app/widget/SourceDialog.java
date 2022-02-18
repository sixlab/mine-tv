package tech.minesoft.minetv.v4app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import tech.minesoft.minetv.v4app.databinding.WidgetSourceDialogBinding;

public class SourceDialog extends Dialog {
    private WidgetSourceDialogBinding binding;
    private Context context;
    public EditText inputCode;
    public EditText inputUrl;

    public SourceDialog(@NonNull Context context) {
        super(context);
        this.init(context, true, null);
    }

    public SourceDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.init(context, true, null);
    }

    protected SourceDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.init(context, cancelable, cancelListener);
    }

    private void init(Context context, boolean cancelable, OnCancelListener cancelListener) {
        this.context = context;

        binding = WidgetSourceDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCancelPop.setOnClickListener(this::clickCancel);
        binding.btnSavePop.setOnClickListener(this::clickCancel);

        this.inputCode = binding.inputCode;
        this.inputUrl = binding.inputUrl;
    }

    public void setOnOkListener(View.OnClickListener listener) {
        binding.btnSavePop.setOnClickListener(listener);
    }

    private void clickSave(View view) {

    }

    private void clickCancel(View view) {
        cancel();
    }

}
