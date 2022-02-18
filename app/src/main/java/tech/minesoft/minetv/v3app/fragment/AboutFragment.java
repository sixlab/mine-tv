package tech.minesoft.minetv.v3app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import tech.minesoft.minetv.v3app.R;


public class AboutFragment extends Fragment {

    private WebView webView;

    public static Fragment newInstance() {
        return new AboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        webView = view.findViewById(R.id.wv_about);

        loadAbout();

        return view;
    }

    private void loadAbout() {
        webView.loadUrl("file:///android_asset/www/index.html");
    }

}
