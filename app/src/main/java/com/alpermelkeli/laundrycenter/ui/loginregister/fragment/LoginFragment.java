package com.alpermelkeli.laundrycenter.ui.loginregister.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.FragmentLoginBinding;
import com.alpermelkeli.laundrycenter.repository.UserRepository;
import com.alpermelkeli.laundrycenter.ui.homescreen.HomeScreen;

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
                userRepository.checkUserFirstLogin(new UserRepository.CheckUserCallBack() {
                    @Override
                    public void onSuccess(Boolean success) {

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.apply();

                        Toast.makeText(getActivity(), "Giriş Başarılı", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getActivity(), HomeScreen.class);
                        intent.putExtra("email", email);

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
        binding.forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new PasswordForgetFragment());
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