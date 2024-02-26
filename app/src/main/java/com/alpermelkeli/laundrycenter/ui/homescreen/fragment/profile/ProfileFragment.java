package com.alpermelkeli.laundrycenter.ui.homescreen.fragment.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.DialogHistoryBinding;
import com.alpermelkeli.laundrycenter.databinding.FragmentProfileBinding;
import com.alpermelkeli.laundrycenter.model.User;
import com.alpermelkeli.laundrycenter.ui.loginregister.MainActivity;
import com.alpermelkeli.laundrycenter.viewmodel.UserViewModel;
import java.util.List;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    UserViewModel userViewModel;
    String email;
    List<String> history;
    public ProfileFragment() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater,container,false);

        View view = binding.getRoot();

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);


        Bundle bundle = getArguments();


        if (bundle != null) {
            email = bundle.getString("email");
        }

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            getDataFromViewModel(user);
            binding.profileProgressBar.setVisibility(View.GONE);
            binding.profileConstraint.setVisibility(View.VISIBLE);
        });

        binding.loadMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO REDIRECT TO PAYMENT PAGE

            }
        });

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        binding.useHistoryProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialog();
            }
        });

        userViewModel.getUser(email);

        return view;

    }
    public void logout(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.apply();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void createAlertDialog(){
        DialogHistoryBinding bindingDialog;

        bindingDialog = DialogHistoryBinding.inflate(getLayoutInflater());

        AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());

        View dialogView = bindingDialog.getRoot();

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        Window window = alertDialog.getWindow();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());

        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        layoutParams.gravity = Gravity.BOTTOM;

        window.setAttributes(layoutParams);

        dialogView.setBackgroundColor(Color.TRANSPARENT);

        TextView message = bindingDialog.dialogMessageHistory;

        if (history != null){

            StringBuilder sb = new StringBuilder();

            for(String i : history){
                sb.append(i);
                sb.append(" ₺ \uD83D\uDCB8");
                sb.append("\n");
            }

            message.setText(sb);

        } else {
            message.setText("Hesap Oluşturuldu");
        }
        alertDialog.show();


    }

    public void getDataFromViewModel(User user){
        binding.profileBalanceText.setText(user.getBalance().toString()+" ₺");
        binding.profileIDText.setText(user.getEmail());
        history = user.getHistory();
        binding.profileProgressBar.setVisibility(View.GONE);
        binding.profileConstraint.setVisibility(View.VISIBLE);
    }
}