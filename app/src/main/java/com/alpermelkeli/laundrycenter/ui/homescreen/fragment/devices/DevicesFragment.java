package com.alpermelkeli.laundrycenter.ui.homescreen.fragment.devices;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.FragmentDevicesBinding;
import com.alpermelkeli.laundrycenter.ui.homescreen.fragment.devices.adapter.DevicesAdapter;
import com.alpermelkeli.laundrycenter.viewmodel.DeviceViewModel;
import com.alpermelkeli.laundrycenter.viewmodel.UserViewModel;


public class DevicesFragment extends Fragment {
    FragmentDevicesBinding binding;
    DeviceViewModel deviceViewModel;
    UserViewModel userViewModel;
    String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDevicesBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        Bundle bundle = getArguments();
        email = bundle.getString("email");
        deviceViewModel = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        binding.devicesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {

            deviceViewModel.loadDeviceList(user.getCompany());
            deviceViewModel.getDeviceListLiveData().observe(getViewLifecycleOwner(), deviceList -> {

                DevicesAdapter devicesAdapter = new DevicesAdapter(deviceList);
                binding.devicesRecyclerView.setAdapter(devicesAdapter);
                binding.devicesProgressBar.setVisibility(View.GONE);
                binding.devicesConstraint.setVisibility(View.VISIBLE);
            });

        });


        userViewModel.getUser(email);
        return view;
    }
}