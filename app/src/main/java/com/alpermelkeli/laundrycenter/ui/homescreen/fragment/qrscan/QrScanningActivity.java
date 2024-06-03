package com.alpermelkeli.laundrycenter.ui.homescreen.fragment.qrscan;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alpermelkeli.laundrycenter.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QrScanningActivity extends AppCompatActivity {
    private String email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.setPrompt("");
        integrator.setCameraId(0);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null && result.getContents() != null) {

            String scannedText = result.getContents();
            Intent intent = new Intent(QrScanningActivity.this, AfterScanActivity.class);
            intent.putExtra("scannedText",scannedText);
            intent.putExtra("email",email);
            startActivity(intent);

        } else {
            Toast.makeText(this, "Qr taranırken bir sorun oluştu.",Toast.LENGTH_LONG).show();
        }
        finish();
    }
}
