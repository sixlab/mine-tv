package com.ubtv66.minetv.page.detail;

import android.app.Activity;
import android.os.Bundle;

import com.ubtv66.minetv.R;

/*
 * Details activity class that loads LeanbackDetailsFragment class
 */
public class VodDetailActivity extends Activity {
    public static final String SHARED_ELEMENT_NAME = "hero";
    public static final String MOVIE = "Movie";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }

}
// finish