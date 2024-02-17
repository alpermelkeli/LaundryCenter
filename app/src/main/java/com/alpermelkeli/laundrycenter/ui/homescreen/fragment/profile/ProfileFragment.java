package com.alpermelkeli.laundrycenter.ui.homescreen.fragment.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.FragmentProfileBinding;
import com.alpermelkeli.laundrycenter.viewmodel.UserViewModel;


public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    UserViewModel userViewModel;
    String email;

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

            binding.profileProgressBar.setVisibility(View.GONE);
            binding.profileConstraint.setVisibility(View.VISIBLE);
        });

        binding.loadMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO REDIRECT TO PAYMENT PAGE

            }
        });


        userViewModel.getUser(email);

        return view;

    }
}