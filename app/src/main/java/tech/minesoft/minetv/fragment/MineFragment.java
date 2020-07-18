package tech.minesoft.minetv.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.data.RequestHelper;


public class MineFragment extends Fragment {

    public static Fragment newInstance() {
        return new MineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        Button btn = view.findViewById(R.id.btn_change_url);
        btn.setOnClickListener(v -> {
            RequestHelper.urlIndex++;
            if (RequestHelper.urlIndex >= RequestHelper.BASE_URLs.length) {
                RequestHelper.urlIndex = 0;
            }

            showText("链接：" + RequestHelper.BASE_URLs[RequestHelper.urlIndex] + "\n索引：" + RequestHelper.urlIndex);

        });

        return view;
    }

    private Toast toast = null;

    @SuppressLint("ShowToast")
    public void showText(CharSequence text) {
        try {
            toast.getView().isShown();
            toast.setText(text);
        } catch (Exception e) {
            toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
        }
        toast.show();
    }

}
