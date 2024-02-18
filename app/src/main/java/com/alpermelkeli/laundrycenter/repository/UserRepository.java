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
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String storedPassword = document.getString("password");
                                if (storedPassword != null && password.equals(storedPassword)) {
                                    callBack.onSuccess(true);
                                } else {
                                    callBack.onFailure("Kullanıcı şifresi yanlış");
                                }
                            } else {
                                // E-posta adresine sahip belge var ama belgede "password" alanı yok
                                callBack.onFailure("Kullanıcı bulunamadı");
                            }
                        } else {
                            // Firestore'dan belge alırken hata oluştu
                            callBack.onFailure("Belge alınırken hata oluştu: " + task.getException());
                        }
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

    public void updateBalance(UpdateBalanceCallBack callBack, String email, double newBalance){
        db.collection("Users")
                .document(email)
                .update("balance",newBalance)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      if (task.isSuccessful()){
                          callBack.onSuccess("Success");
                      }
                      else {
                          callBack.onFailure(task.getException().toString());
                      }
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
    public interface UpdateBalanceCallBack{

        void onSuccess(String success);
        void onFailure(String fail);


    }

}
