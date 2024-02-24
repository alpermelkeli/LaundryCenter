package com.alpermelkeli.laundrycenter.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.alpermelkeli.laundrycenter.model.User;
import com.alpermelkeli.laundrycenter.repository.UserRepository;

public class UserViewModel extends ViewModel {
    MutableLiveData<User> userLiveData;
    Boolean isLogged;
    UserRepository userRepository;

    public UserViewModel(){
        this.userLiveData = new MutableLiveData<>();
        this.userRepository = new UserRepository();
        this.isLogged = false;
    }

    public LiveData<User> getUserLiveData(){
        return userLiveData;
    }
    public Boolean getIsLogged(){
        return isLogged;
    }

    public void checkUser(String email, String password){

        userRepository.checkUser(new UserRepository.CheckUserCallBack() {
            @Override
            public void onSuccess(Boolean success) {
                isLogged = true;
            }

            @Override
            public void onFailure(String fail) {
                System.out.println(fail);
            }
        }, email, password);
    }
    public void getUser(String email){

        userRepository.getUser(new UserRepository.GetUserCallBack() {
            @Override
            public void onUserLoaded(User user) {
                userLiveData.setValue(user);
            }

            @Override
            public void onFailure(String fail) {
                System.out.println(fail);
            }
        }, email);

    }

    public void updateUserBalance(String email, double newBalance){
        userRepository.updateBalance(new UserRepository.UpdateBalanceCallBack() {
            @Override
            public void onSuccess(String success) {

            }

            @Override
            public void onFailure(String fail) {
                System.out.println(fail);
            }
        },email,newBalance);

    }
    public void addHistory(String email, long time, double amount){
        userRepository.addHistory(new UserRepository.AddHistoryCallBack() {
            @Override
            public void onSuccess(String success) {

            }
        },email,time,amount);


    }








}
