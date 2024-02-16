package com.alpermelkeli.laundrycenter.repository;

import androidx.annotation.NonNull;

import com.alpermelkeli.laundrycenter.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User user = new User();


    //Check if user logged in successfully
    public void checkUser(CheckUserCallBack callBack, String email, String password){

        db.collection("Users")
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(password.equals(document.getString("password"))){

                            callBack.onSuccess(true);

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
    //Get user data from database after logged in and show balance name etc.
    public void getUser(GetUserCallBack callBack, String email){

        db.collection("Users")
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            user.setBalance(document.getDouble("balance"));
                            user.setEmail(document.getString("email"));
                            user.setCompany(document.getString("company"));
                            callBack.onUserLoaded(user);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.onFailure(e.toString());
                    }
                });
    }

    public void registerUser(RegisterUserCallBack callBack, String email, String password, String company){
        DocumentReference docRef = db.collection("Users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        callBack.onFailure("User registered before");

                    }
                    else {
                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("email", email);
                        newUser.put("password",password);
                        newUser.put("company", company);
                        newUser.put("balance", 0);
                        docRef.set(newUser);
                        callBack.onRegistered(true);
                    }


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailure(e.toString());
            }
        });
    }


    public interface CheckUserCallBack {
        void onSuccess(Boolean success);
        void onFailure(String fail);
    }

    public interface GetUserCallBack {
        void onUserLoaded(User user);
        void onFailure(String fail);
    }

    public interface RegisterUserCallBack{

        void onRegistered(Boolean success);

        void onFailure(String fail);
    }

}
