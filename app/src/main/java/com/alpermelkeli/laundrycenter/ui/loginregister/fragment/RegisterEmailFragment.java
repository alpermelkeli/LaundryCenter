package com.alpermelkeli.laundrycenter.ui.loginregister.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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


        binding.emailPageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        binding.emailNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.registerEmailInput.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("email", email);




            }
        });


        return view;
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrameLayout, fragment);
                ft.addToBackStack(null);
                ft.commit();
    }




}