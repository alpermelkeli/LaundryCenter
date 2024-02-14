package com.alpermelkeli.laundrycenter.repository;

import androidx.annotation.NonNull;

import com.alpermelkeli.laundrycenter.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User user = new User();

    public void getUser(UserCallBack callBack, String email, String password){

        db.collection("User")
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(password.equals(document.getString("password"))){
                            user.setBalance(document.getDouble("balance"));
                            user.setCompany(document.getString("company"));
                            user.setEmail(document.getString("email"));
                            user.setPassword(document.getString("password"));
                            callBack.onUserLoaded(user);

                        }
                        else {

                            callBack.onFailure("Kullanıcı şifresi yanlış");
                        }



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.onFailure("Kullanıcı bulunamadı");
                    }
                });





    }






    public interface UserCallBack{
        void onUserLoaded(User user);
        void onFailure(String fail);

    }
}
