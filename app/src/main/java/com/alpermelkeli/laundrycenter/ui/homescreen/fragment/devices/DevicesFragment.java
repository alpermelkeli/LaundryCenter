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


public class DevicesFragment extends Fragment {
    FragmentDevicesBinding binding;
    DeviceViewModel deviceViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDevicesBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        deviceViewModel = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        binding.devicesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        deviceViewModel.getDeviceListLiveData().observe(getViewLifecycleOwner(), deviceList -> {

            DevicesAdapter devicesAdapter = new DevicesAdapter(deviceList);
            binding.devicesRecyclerView.setAdapter(devicesAdapter);
            binding.devicesProgressBar.setVisibility(View.GONE);
            binding.devicesConstraint.setVisibility(View.VISIBLE);
        });


        // TODO Transfer data and get BeytepeErkek from db
        deviceViewModel.loadDeviceList("BeytepeErkek");

        return view;
    }
}