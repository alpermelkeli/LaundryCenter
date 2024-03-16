package com.alpermelkeli.laundrycenter.ui.loginregister.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.FragmentPasswordForgetBinding;
import com.alpermelkeli.laundrycenter.repository.UserRepository;


public class PasswordForgetFragment extends Fragment {
    FragmentPasswordForgetBinding binding;
    UserRepository userRepository;

    public PasswordForgetFragment() {
        userRepository = new UserRepository();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordForgetBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.passwordForgetNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.passwordForgetEmailInput.getText().toString();
                // Check whether email valid.
                if(email!=null & Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    userRepository.sendPasswordResetEmail(new UserRepository.ResetPasswordCallBack() {
                        @Override
                        public void onSuccess(String success) {
                            Toast.makeText(getActivity(),success,Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getParentFragmentManager();
                            fm.popBackStack();
                        }
                        @Override
                        public void onFailure(String fail) {
                            Toast.makeText(getActivity(),fail,Toast.LENGTH_SHORT).show();

                        }
                    },email);

                }
                else {
                    Toast.makeText(getActivity(),"Lütfen geçerli bir email girin.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.passwordForgetPageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                fm.popBackStack();
            }
        });



        return view;
    }
}