package com.alpermelkeli.laundrycenter.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alpermelkeli.laundrycenter.model.Device;
import com.alpermelkeli.laundrycenter.repository.DeviceRepository;

import java.util.List;
public class DeviceViewModel extends ViewModel {
    MutableLiveData<List<Device>> deviceListData;
    MutableLiveData<Device> deviceData;
    DeviceRepository deviceRepository;

    public DeviceViewModel(){
        this.deviceListData = new MutableLiveData<>();
        this.deviceData = new MutableLiveData<>();
        this.deviceRepository = new DeviceRepository();
    }

    public LiveData<List<Device>> getDeviceListLiveData(){
        return deviceListData;
    }

    public LiveData<Device> getDeviceLiveData(){
        return deviceData;
    }

    public void loadDevice(String id, String company){
        deviceRepository.getDeviceByID(new DeviceRepository.DeviceCallBack() {
            @Override
            public void onSuccess(Device device) {
                deviceData.setValue(device);
            }

            @Override
            public void onFailure(String fail) {

            }
        },id,company);
    }
    public void loadDeviceList(String company){
        deviceRepository.getDevicesByCompany(new DeviceRepository.DevicesCallBack() {

            @Override
            public void onSuccess(List<Device> deviceList) {
                deviceListData.setValue(deviceList);
            }

            @Override
            public void onFailure(String fail) {

            }
        },company);


    }




}
