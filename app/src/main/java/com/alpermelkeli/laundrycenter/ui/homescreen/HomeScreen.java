package com.alpermelkeli.laundrycenter.ui.homescreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.ActivityHomeScreenBinding;
import com.alpermelkeli.laundrycenter.ui.homescreen.fragment.devices.DevicesFragment;
import com.alpermelkeli.laundrycenter.ui.homescreen.fragment.profile.ProfileFragment;
import com.alpermelkeli.laundrycenter.ui.homescreen.fragment.qrscan.QrScanFragment;
import com.google.android.material.navigation.NavigationBarView;

public class HomeScreen extends AppCompatActivity {
    ActivityHomeScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.homeScreenNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.profile) {

                    replaceFragment(new ProfileFragment());
                    return true;

                } else if (itemId == R.id.qrScan) {

                    replaceFragment(new QrScanFragment());
                    return true;

                } else if (itemId == R.id.devices) {

                    replaceFragment(new DevicesFragment());
                    return true;
                }
            return false;

            }
        });

    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.homeScreenFrame, fragment)
                .commit();
    }
}