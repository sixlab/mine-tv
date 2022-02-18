package tech.minesoft.minetv.v3app.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import tech.minesoft.minetv.v3app.R;


public class SettingFragment extends Fragment {

    private RadioGroup rg;
    private Button button;

    public static Fragment newInstance() {
        return new SettingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        return view;
    }

    private Toast toast = null;

    @SuppressLint("ShowToast")
    public void showText(CharSequence text) {
        try {
            toast.getView().isShown();
            toast.setText(text);
        } catch (Exception e) {
            toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

}
