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
    MutableLiveData<Double> priceData;

    public DeviceViewModel(){
        this.deviceListData = new MutableLiveData<>();
        this.deviceData = new MutableLiveData<>();
        this.priceData = new MutableLiveData<>();
        this.deviceRepository = new DeviceRepository();
    }

    public LiveData<List<Device>> getDeviceListLiveData(){
        return deviceListData;
    }

    public LiveData<Device> getDeviceLiveData(){
        return deviceData;
    }
    public LiveData<Double> getPriceLiveData(){return priceData;}

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
    public void loadPrice(String company){
        deviceRepository.getPriceByCompany(new DeviceRepository.CompanyPriceCallback() {
            @Override
            public void onSuccess(Double price) {
                priceData.setValue(price);
            }

            @Override
            public void onFailure(String fail) {
                System.out.println(fail);
            }
        },company);
    }
    public void updateDeviceTime(String deviceID,String company,long newtime){
        deviceRepository.updateDeviceTimeByID(new DeviceRepository.UpdateTimeCallBack() {
            @Override
            public void onSuccess(Boolean bool) {
                System.out.println("Başarılıııı");
            }

            @Override
            public void onFailure(String fail) {
                System.out.println(fail);
            }
        },deviceID,company,newtime);


    }
    public void setDeviceTime(String id, String company, long time, long start){
        deviceRepository.setDeviceTimeByID(new DeviceRepository.SetTimeCallBack() {
            @Override
            public void onSuccess(Boolean bool) {
                System.out.println("BAŞARIIII");
            }
        },id,company,time,start);


    }




}
