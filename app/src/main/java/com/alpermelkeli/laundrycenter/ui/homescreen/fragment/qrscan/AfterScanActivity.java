package com.alpermelkeli.laundrycenter.ui.homescreen.fragment.qrscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.ActivityAfterScanBinding;
import com.alpermelkeli.laundrycenter.viewmodel.DeviceViewModel;

import java.util.concurrent.TimeUnit;

public class AfterScanActivity extends AppCompatActivity {
    DeviceViewModel deviceViewModel;
    ActivityAfterScanBinding binding;
    String scannedText;
    double priceData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAfterScanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding.timePicker.setIs24HourView(true);

        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);

        Intent intent = getIntent();

        scannedText = intent.getStringExtra("scannedText");

        String[] scanData = scannedText.split(",");

        deviceViewModel.getDeviceLiveData().observe(this, device -> {
            binding.afterScanDeviceID.setText(device.getId());
            long durationInMillis = device.getTime() - (System.currentTimeMillis() - device.getStart());
            if (durationInMillis > 0) {
                binding.afterScanDeviceStatus.setText("Çalışıyor");
                binding.afterScanDeviceStatus.setTextColor(Color.RED);
                startCountDownTimer(binding.afterScanDeviceTime,binding.afterScanDeviceStatus,durationInMillis);
            } else {
                binding.afterScanDeviceStatus.setText("Uygun");
                binding.afterScanDeviceStatus.setTextColor(Color.GREEN);
                binding.afterScanDeviceTime.setText("");
            }
        });

        deviceViewModel.getPriceLiveData().observe(this, price -> {
            priceData = price;
            binding.priceText.setText(price.toString()+"TL/saat");
        });



        binding.paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour, minute;
                if (Build.VERSION.SDK_INT >= 23) {
                    hour = binding.timePicker.getHour();
                    minute = binding.timePicker.getMinute();


                }



            }
        });


        deviceViewModel.loadDevice(scanData[0],scanData[1]);
        deviceViewModel.loadPrice(scanData[1]);
    }




    private void startCountDownTimer(final TextView textView, final TextView statusText , long durationInMillis) {
        CountDownTimer countDownTimer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                textView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }

            @Override
            public void onFinish() {
                textView.setText("00:00:00");
                statusText.setTextColor(Color.GREEN);
                statusText.setText("Uygun");
            }
        };
        countDownTimer.start();
    }
}