package tech.minesoft.minetv.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle(R.string.title_detail);

    }
}