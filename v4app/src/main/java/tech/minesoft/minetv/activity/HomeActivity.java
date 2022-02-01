package tech.minesoft.minetv.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle(R.string.title_main);
    }

}