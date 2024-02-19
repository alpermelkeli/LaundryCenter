package com.alpermelkeli.laundrycenter.ui.loginregister.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
                // Check whether email valid.
                if(email!=null & Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);
                    RegisterPasswordFragment registerPasswordFragment = new RegisterPasswordFragment();
                    registerPasswordFragment.setArguments(bundle);
                    replaceFragment(registerPasswordFragment);
                }
                else {
                    Toast.makeText(getActivity(),"Lütfen geçerli bir email girin.",Toast.LENGTH_SHORT).show();
                }


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