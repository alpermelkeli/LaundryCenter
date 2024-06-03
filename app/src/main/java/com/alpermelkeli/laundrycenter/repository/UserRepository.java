package com.alpermelkeli.laundrycenter.repository;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alpermelkeli.laundrycenter.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
public class UserRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    User user = new User();
    //Check if user logged in successfully and whether complete verification.
    public void checkUserFirstLogin(CheckUserCallBack callBack, String email, String password){

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                callBack.onSuccess(true);
                            }
                            else {
                                callBack.onFailure("Email verification required.");
                            }
                        }
                        else{
                            callBack.onFailure("Şifre veya kullanıcı adı yanlış.");
                        }
                    }
                });
    }

    //Difference between checkUserFirstLogin is email verification step. I added this to work more quick in first scren.
    public void checkUser(CheckUserCallBack callBack, String email, String password){

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            callBack.onSuccess(true);
                        }
                        else{
                            callBack.onFailure("Şifre veya kullanıcı adı yanlış.");
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
                            user.setHistory((List<String>) document.get("history"));
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

    //Firstly save to database if doesn't exist after that save to firebaseAuth and send email verification.
    public void registerUser(RegisterUserCallBack callBack, String email, String password, String company){
        DocumentReference docRef = db.collection("Users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        //Check if user registered the app before.
                        callBack.onFailure("User registered before");
                    }
                    else {
                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("email", email);
                        newUser.put("company", company);
                        newUser.put("balance", 0);
                        newUser.put("history", FieldValue.arrayUnion());
                        docRef.set(newUser);
                        auth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            sendVerificationEmail(user);
                                            callBack.onRegistered(true);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        callBack.onFailure(e.getMessage());
                                    }
                                });

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailure(e.getMessage());
            }
        });
    }

    public void sendVerificationEmail(FirebaseUser user){
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }

                    }
                });
    }

    public void sendPasswordResetEmail(ResetPasswordCallBack callBack, String email){
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callBack.onSuccess("Kayıtlı ise e-postana şifre sıfırlama maili gönderildi");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.onFailure("E-postana sıfırlama mailini gönderirken bir sorun oluştu:"+e.getMessage());
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

    public void addHistory(AddHistoryCallBack callBack,String email,long time, double amount){
        String date = millis_to_date(time);
        db.collection("Users")
                .document(email)
                .update("history", FieldValue.arrayUnion(date+","+amount))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            callBack.onSuccess("Succes added history");

                        }
                    }
                });
    }
    public String millis_to_date(long time){
        DateFormat obj = new SimpleDateFormat("dd MMM yyyy HH:mm");
        Date res = new Date(time);
        return obj.format(res);

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

        boolean onRegistered(Boolean success);

        void onFailure(String fail);
    }
    public interface UpdateBalanceCallBack{

        void onSuccess(String success);
        void onFailure(String fail);


    }
    public interface AddHistoryCallBack{

        void onSuccess(String success);

    }
    public interface ResetPasswordCallBack{
        void onSuccess(String success);
        void onFailure(String fail);

    }
}