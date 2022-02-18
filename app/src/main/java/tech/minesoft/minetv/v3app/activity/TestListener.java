package tech.minesoft.minetv.v3app.activity;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class TestListener implements View.OnKeyListener, View.OnClickListener {
    private Context context;

    public TestListener(Context applicationContext) {
        context = applicationContext;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "click", Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Toast.makeText(context, "key:" + keyCode, Toast.LENGTH_SHORT);
        return false;
    }
}
