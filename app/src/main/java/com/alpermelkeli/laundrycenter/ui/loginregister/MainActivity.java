package com.alpermelkeli.laundrycenter.ui.loginregister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.ActivityMainBinding;
import com.alpermelkeli.laundrycenter.model.User;
import com.alpermelkeli.laundrycenter.ui.loginregister.fragment.LoginRegisterFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private LoginRegisterFragment loginRegisterFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        loginRegisterFragment = new LoginRegisterFragment();
        replaceFragment(loginRegisterFragment);
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.mainFrameLayout.getId(), fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



}