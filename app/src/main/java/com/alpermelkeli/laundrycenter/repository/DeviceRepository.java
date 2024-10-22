package com.alpermelkeli.laundrycenter.repository;

import androidx.annotation.NonNull;

import com.alpermelkeli.laundrycenter.model.Device;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DeviceRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Device device = new Device();

    public void getDeviceByID(DeviceCallBack callBack, String id,String company) {

        db.collection("Company")
                .document(company)
                .collection("Devices")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                callBack.onSuccess(document.toObject(Device.class));
                            } else {
                                callBack.onFailure("there is no device");
                            }
                        } else {
                            callBack.onFailure(task.getException().toString());
                        }
                    }
                });
    }

    public void getDevicesByCompany(DevicesCallBack callBack, String company){

        db.collection("Company")
                .document(company)
                .collection("Devices")
                .orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Device> deviceList = new ArrayList<>();
                        if(task.isSuccessful()){

                            QuerySnapshot querySnapshot = task.getResult();
                            for(DocumentSnapshot document : querySnapshot){
                                Device newDevice = new Device();
                                newDevice.setId(document.getString("id"));
                                newDevice.setChannel(document.getString("channel"));
                                newDevice.setTime(document.getLong("time"));
                                newDevice.setName(document.getString("name"));
                                newDevice.setStart(document.getLong("start"));
                                deviceList.add(newDevice);

                            }

                            callBack.onSuccess(deviceList);

                        }
                        else{
                            callBack.onFailure(task.getException().toString());

                        }
                    }
                });



    }
    public void getPriceByCompany(CompanyPriceCallback callback, String company){
        db.collection("Company")
                .document(company)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            callback.onSuccess(document.getDouble("price"));
                        }
                        else {
                            callback.onFailure(task.getException().toString());
                        }
                    }
                });

    }

    public void updateDeviceTimeByID(UpdateTimeCallBack callBack, String id,String company,long newTime){
        db.collection("Company")
                .document(company)
                .collection("Devices")
                .document(id)
                .update("time",newTime)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            callBack.onSuccess(true);
                        }
                    }
                });



    }
    public void setDeviceTimeByID(SetTimeCallBack callBack,String id,String company, long time, long start){
        db.collection("Company")
                .document(company)
                .collection("Devices")
                .document(id)
                .update("time",time, "start",start,"status","on")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            callBack.onSuccess(true);
                        }
                    }
                });
    }

    public interface DeviceCallBack{

        void onSuccess(Device device);

        void onFailure(String fail);

    }
    public interface DevicesCallBack{

        void onSuccess(List<Device> deviceList);
        void onFailure(String fail);

    }
    public interface CompanyPriceCallback{

        void onSuccess(Double price);
        void onFailure(String fail);

    }
    public interface UpdateTimeCallBack{

        void onSuccess(Boolean bool);
        void onFailure(String fail);


    }
    public interface SetTimeCallBack{

        void onSuccess(Boolean bool);

    }

}
