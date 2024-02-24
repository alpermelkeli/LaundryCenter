package com.alpermelkeli.laundrycenter.ui.homescreen.fragment.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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
import com.alpermelkeli.laundrycenter.databinding.FragmentProfileBinding;
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
            binding.profileBalanceText.setText(user.getBalance().toString()+" ₺");
            binding.profileIDText.setText(user.getEmail());
            history = user.getHistory();
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
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("email");
                editor.remove("password");
                editor.apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        binding.useHistoryProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

// LayoutInflater ile custom layout dosyasını yükleyin
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_history, null);
                builder.setView(dialogView);

                AlertDialog alertDialog = builder.create();

                Window window = alertDialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams layoutParams = window.getAttributes();

                    layoutParams.gravity = Gravity.BOTTOM;
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    window.setAttributes(layoutParams);
                }

                TextView message = dialogView.findViewById(R.id.dialog_message_history);
                if (history.toString() != null){
                    message.setText(history.toString());

                }
                else {
                    message.setText("Hesap Oluşturuldu");
                }

                alertDialog.show();



            }
        });

        userViewModel.getUser(email);

        return view;

    }

}