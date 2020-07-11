package tech.minesoft.minetv;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import tech.minesoft.minetv.data.DbHelper;

public class MineFragment extends Fragment {
    private static final String TAG = MineFragment.class.getName();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        DbHelper.init(getContext());

        setupUIElements();

        setupEventListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setupUIElements() {

    }

    private void setupEventListeners() {

    }

}
