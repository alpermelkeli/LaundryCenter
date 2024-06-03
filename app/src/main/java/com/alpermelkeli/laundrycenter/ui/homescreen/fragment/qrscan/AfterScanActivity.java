package com.alpermelkeli.laundrycenter.ui.homescreen.fragment.qrscan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.ActivityAfterScanBinding;
import com.alpermelkeli.laundrycenter.repository.plugAPI.TurnOnOff;
import com.alpermelkeli.laundrycenter.viewmodel.DeviceViewModel;
import com.alpermelkeli.laundrycenter.viewmodel.UserViewModel;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AfterScanActivity extends AppCompatActivity {
    TurnOnOff turnOnOff;
    DeviceViewModel deviceViewModel;
    UserViewModel userViewModel;
    ActivityAfterScanBinding binding;
    String scannedText;
    double priceData;
    String[] scanData;
    long durationInMillis;
    String email;
    double balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAfterScanBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        binding.timePicker.setHour(0);
        binding.timePicker.setMinute(0);

        turnOnOff = new TurnOnOff();

        binding.timePicker.setIs24HourView(true);

        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        Intent intent = getIntent();

        scannedText = intent.getStringExtra("scannedText");

        email = intent.getStringExtra("email");

        //id,company,channel
        scanData = scannedText.split(",");

        //Get selected time and do payment by using this data and take balance of user before this
        userViewModel.getUserLiveData().observe(this, user -> {
            balance = user.getBalance();

            binding.paymentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int hour, minute;

                    if (Build.VERSION.SDK_INT >= 23) {
                        hour = binding.timePicker.getHour();
                        minute = binding.timePicker.getMinute();

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                        LayoutInflater inflater = getLayoutInflater();

                        View dialogView = inflater.inflate(R.layout.purchase_confirm_dialog, null);
                        builder.setView(dialogView);

                        TextView message = dialogView.findViewById(R.id.dialog_message);
                        message.setText("İşlemi onaylıyor musunuz?");

                        Button positiveButton = dialogView.findViewById(R.id.positive_button);

                        AlertDialog alertDialog = builder.create();
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Evet butonuna tıklama işlemi
                                if (durationInMillis > 0) {
                                    paymentAndAddTimeToDevice(hour * 60 + minute);
                                } else {
                                    paymentAndCreateNewSession(hour * 60 + minute);
                                }
                                alertDialog.dismiss(); // AlertDialog'u kapat
                            }
                        });

                        Button negativeButton = dialogView.findViewById(R.id.negative_button);
                        negativeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Hayır butonuna tıklama işlemi
                                alertDialog.dismiss(); // AlertDialog'u kapat
                            }
                        });


                        alertDialog.show();
                    }
                }
            });
        });


        //Get fields of device to show user.
        deviceViewModel.getDeviceLiveData().observe(this, device -> {

            binding.afterScanDeviceID.setText("Cihaz numarası: " + device.getName());

            durationInMillis = device.getTime() - (System.currentTimeMillis() - device.getStart());

            if (durationInMillis > 0) {

                binding.afterScanDeviceStatus.setText("Çalışıyor");

                binding.afterScanDeviceStatus.setTextColor(Color.parseColor("#D23C1B"));

                startCountDownTimer(binding.afterScanDeviceTime,binding.afterScanDeviceStatus,durationInMillis);

            } else {
                binding.afterScanDeviceStatus.setText("Uygun");

                binding.afterScanDeviceStatus.setTextColor(Color.parseColor("#08831C"));

                binding.afterScanDeviceTime.setText("");
            }

            binding.afterScanProgressBar.setVisibility(View.GONE);
            binding.afterScanLinear.setVisibility(View.VISIBLE);
        });


        //Get Price Of Device
        deviceViewModel.getPriceLiveData().observe(this, price -> {
            priceData = price;
            binding.priceText.setText(price.toString()+"TL/saat");
        });


        userViewModel.getUser(email);
        deviceViewModel.loadDevice(scanData[0],scanData[1]);
        deviceViewModel.loadPrice(scanData[1]);
    }


    private Boolean getPaymentFromUser(int minute, double userBalance) {
        double price = (priceData / 60) * minute;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

        double newBalance = userBalance - price;

        newBalance = Double.parseDouble(decimalFormat.format(newBalance).replace(',', '.'));
        price = Double.parseDouble(decimalFormat.format(price).replace(',', '.'));

        if (newBalance < 0) {
            Toast.makeText(getApplicationContext(), "Bakiye Yetersiz", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            userViewModel.updateUserBalance(email, newBalance);
            userViewModel.addHistory(email, System.currentTimeMillis(), price);
            userViewModel.getUser(email);
            return true;
        }
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
                textView.setText("");
                statusText.setTextColor(Color.GREEN);
                statusText.setText("Uygun");
            }
        };
        countDownTimer.start();
    }
    private void paymentAndAddTimeToDevice(int minute) {
        long timeToAddInMillis = TimeUnit.MINUTES.toMillis(minute);

        deviceViewModel.getDeviceLiveData().observe(this, device -> {
            long newTime = timeToAddInMillis+device.getTime();

            // Update the device with the new time
            if(getPaymentFromUser(minute,balance)){
                deviceViewModel.updateDeviceTime(scanData[0],scanData[1],newTime);

                // Show a confirmation message
                Toast.makeText(AfterScanActivity.this, "Payment successful. Time added to the device.", Toast.LENGTH_SHORT).show();
                finish();
            }


        });
    }
    private void paymentAndCreateNewSession(int minute){
        long timeMillis = TimeUnit.MINUTES.toMillis(minute);

        //TODO get data from internet not system
        if(getPaymentFromUser(minute,balance)){
            deviceViewModel.setDeviceTime(scanData[0],scanData[1],timeMillis,System.currentTimeMillis());
            Toast.makeText(AfterScanActivity.this, "Ödeme başarılı, makineyi kullanabilirsiniz", Toast.LENGTH_SHORT).show();
            turnOnOff.sendDeviceControl(scanData[0],"MjE3OTdhdWlk598FB7E13A0F9D9EB9660D10F8A818B8588B9AC0D5FB6AE26E0BADE57B1EC1D4817B7BE91AF82CA2",scanData[2],"on");
            finish();
        }

    }
}