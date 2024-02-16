package com.alpermelkeli.laundrycenter.ui.loginregister.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.FragmentLoginBinding;
import com.alpermelkeli.laundrycenter.databinding.FragmentRegisterPasswordBinding;
import com.alpermelkeli.laundrycenter.repository.UserRepository;
import com.alpermelkeli.laundrycenter.ui.homescreen.HomeScreen;
import com.alpermelkeli.laundrycenter.viewmodel.UserViewModel;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        View view = binding.getRoot();




        binding.loginNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmailInput.getText().toString();
                String password = binding.loginPasswordInput.getText().toString();


                //TODO Use ViewModel
                UserRepository userRepository = new UserRepository();
                userRepository.checkUser(new UserRepository.CheckUserCallBack() {
                    @Override
                    public void onSuccess(Boolean success) {

                        Toast.makeText(getActivity(), "Giriş Başarılı", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getActivity(), HomeScreen.class);

                        startActivity(intent);

                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(String fail) {
                        Toast.makeText(getActivity(), fail, Toast.LENGTH_LONG).show();

                    }
                }, email, password);
            }
        });

        binding.loginPageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                fm.popBackStack();
            }
        });

        return view;
    }
}