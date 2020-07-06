package com.ubtv66.minetv.page.play;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.fragment.app.FragmentActivity;

/**
 * Loads {@link PlaybackVideoFragment}.
 */
public class PlaybackActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new PlaybackVideoFragment())
                    .commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("","");
        return super.onKeyDown(keyCode, event);
    }
}
