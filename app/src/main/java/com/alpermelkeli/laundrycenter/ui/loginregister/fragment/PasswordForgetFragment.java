package com.alpermelkeli.laundrycenter.ui.loginregister.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.FragmentPasswordForgetBinding;


public class PasswordForgetFragment extends Fragment {
    FragmentPasswordForgetBinding binding;

    public PasswordForgetFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordForgetBinding.inflate(inflater,container,false);
        View view = binding.getRoot();






        return view;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out);
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}