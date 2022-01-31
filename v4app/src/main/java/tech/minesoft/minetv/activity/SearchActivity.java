package tech.minesoft.minetv.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import tech.minesoft.minetv.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}