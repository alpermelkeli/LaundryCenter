package com.alpermelkeli.laundrycenter.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.alpermelkeli.laundrycenter.model.User;
import com.alpermelkeli.laundrycenter.repository.UserRepository;

public class UserViewModel extends ViewModel {
    MutableLiveData<User> userLiveData;
    UserRepository userRepository;

    public UserViewModel(){
        this.userLiveData = new MutableLiveData<>();
        this.userRepository = new UserRepository();
    }

    public LiveData<User> getUserLiveData(){
        return userLiveData;
    }


    public void loadUser(String email, String password){

        userRepository.getUser(new UserRepository.UserCallBack() {
            @Override
            public void onUserLoaded(User user) {
                userLiveData.setValue(user);
            }

            @Override
            public void onFailure(String fail) {
                System.out.println(fail);
            }
        }, email, password);


    }








}
