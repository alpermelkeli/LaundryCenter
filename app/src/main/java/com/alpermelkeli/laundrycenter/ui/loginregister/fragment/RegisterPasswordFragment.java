package com.alpermelkeli.laundrycenter.ui.loginregister.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.FragmentRegisterPasswordBinding;

public class RegisterPasswordFragment extends Fragment {
    String email;
    FragmentRegisterPasswordBinding binding;
    public RegisterPasswordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterPasswordBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        Bundle bundle = getArguments();
        email = bundle.getString("email");

        binding.passwordNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = binding.registerPasswordlInput.getText().toString();
                bundle.putString("password", password);
                // TODO transfer data to RegisterCompanyFragment with bundle and replaceFragment.
                RegisterCompanyFragment registerCompanyFragment = new RegisterCompanyFragment();
                registerCompanyFragment.setArguments(bundle);
                replaceFragment(registerCompanyFragment);
            }
        });
        binding.passwordPageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                fm.popBackStack();
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
}