package com.alpermelkeli.laundrycenter.ui.loginregister.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.FragmentLoginRegisterBinding;


public class LoginRegisterFragment extends Fragment {
    FragmentLoginRegisterBinding binding;

    public LoginRegisterFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginRegisterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new RegisterEmailFragment());
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new LoginFragment());
            }
        });


        return view;
    }


    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in,
                        R.anim.fade_out,                //I added animations.
                        R.anim.fade_in,
                        R.anim.slide_out);
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //Exit app when user clicked on back button
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });
    }


}