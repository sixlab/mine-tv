package tech.minesoft.minetv.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import tech.minesoft.minetv.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

}