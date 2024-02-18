package com.alpermelkeli.laundrycenter.ui.homescreen.fragment.qrscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.ActivityAfterScanBinding;

public class AfterScanActivity extends AppCompatActivity {
    ActivityAfterScanBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAfterScanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        String scannedText = intent.getStringExtra("scannedText");



        binding.scannedText.setText(scannedText);

    }
}