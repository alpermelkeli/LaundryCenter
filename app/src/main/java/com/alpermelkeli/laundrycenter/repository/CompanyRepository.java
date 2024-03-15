package com.alpermelkeli.laundrycenter.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
public class CompanyRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getCompanies(CompaniesCallBack callBack){
        db.collection("Company")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<String> companies = new ArrayList<>();
                            QuerySnapshot query = task.getResult();
                            for(DocumentSnapshot document: query){
                                companies.add(document.getId());
                                }
                            callBack.onSuccess(companies);
                        }
                    }
                });


    }

    public interface CompaniesCallBack{
        void onSuccess(List<String> companies);

    }

}
