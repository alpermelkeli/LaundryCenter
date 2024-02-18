package com.alpermelkeli.laundrycenter.ui.homescreen.fragment.qrscan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.FragmentQrScanBinding;


public class QrScanFragment extends Fragment {

    FragmentQrScanBinding binding;
    public QrScanFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQrScanBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        Bundle bundle = getArguments();
        String email = bundle.getString("email");

        binding.scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QrScanningActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });



        return view;
    }
}