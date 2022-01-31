package tech.minesoft.minetv.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import tech.minesoft.minetv.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}