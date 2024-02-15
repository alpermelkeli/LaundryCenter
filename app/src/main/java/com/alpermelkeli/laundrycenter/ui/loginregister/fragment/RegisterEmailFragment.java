package com.alpermelkeli.laundrycenter.ui.loginregister.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.FragmentRegisterEmailBinding;

public class RegisterEmailFragment extends Fragment {

    FragmentRegisterEmailBinding binding;
    public RegisterEmailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterEmailBinding.inflate(inflater,container,false);
        View view = binding.getRoot();


        return view;
    }
}