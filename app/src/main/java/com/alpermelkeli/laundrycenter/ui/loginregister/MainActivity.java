package com.alpermelkeli.laundrycenter.ui.loginregister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.alpermelkeli.laundrycenter.TimeAPI.TimeApi;
import com.alpermelkeli.laundrycenter.databinding.ActivityMainBinding;
import com.alpermelkeli.laundrycenter.repository.UserRepository;
import com.alpermelkeli.laundrycenter.ui.homescreen.HomeScreen;
import com.alpermelkeli.laundrycenter.ui.loginregister.fragment.LoginRegisterFragment;
import com.alpermelkeli.laundrycenter.viewmodel.DeviceViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private LoginRegisterFragment loginRegisterFragment;
    private TimeApi timeApi;

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final long TIME_THRESHOLD = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        timeApi = new TimeApi();
        timeApi.getCurrentTimeMillis(new TimeApi.OnTimeReceivedListener() {
            @Override
            public void onTimeReceived(long timeMillis) {
                long currentTime = System.currentTimeMillis();
                if (Math.abs(currentTime - timeMillis) > TIME_THRESHOLD) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Telefonunuzun saat ayarını yapın.", Toast.LENGTH_LONG).show();
                    });
                } else {
                    runOnUiThread(() -> {
                        loginRegisterFragment = new LoginRegisterFragment();
                        autoLogin();
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {

                });
            }
        });
    }




    private void autoLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String password = sharedPreferences.getString(KEY_PASSWORD, "");

        if (!email.isEmpty() && !password.isEmpty()) {
            login(email, password);
        }
        else{
            replaceFragment(loginRegisterFragment);
            binding.mainProgress.setVisibility(View.GONE);
        }
    }

    private void login(String email, String password) {
        UserRepository userRepository = new UserRepository();
        userRepository.checkUser(new UserRepository.CheckUserCallBack() {
            @Override
            public void onSuccess(Boolean success) {
                Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish(); // Aktiviteyi kapat
            }

            @Override
            public void onFailure(String fail) {
                Log.e("Login", fail);
            }
        }, email, password);
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.mainFrameLayout.getId(), fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
