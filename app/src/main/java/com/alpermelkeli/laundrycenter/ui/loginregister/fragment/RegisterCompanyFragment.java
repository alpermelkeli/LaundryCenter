package com.alpermelkeli.laundrycenter.ui.loginregister.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.alpermelkeli.laundrycenter.R;
import com.alpermelkeli.laundrycenter.databinding.FragmentRegisterCompanyBinding;
import com.alpermelkeli.laundrycenter.repository.CompanyRepository;
import com.alpermelkeli.laundrycenter.repository.UserRepository;

import java.util.List;


public class RegisterCompanyFragment extends Fragment {
    private CompanyRepository companyRepository;
    private String selectedSchool= null;
    private List<String> companiesData;
    FragmentRegisterCompanyBinding binding;
    public RegisterCompanyFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterCompanyBinding.inflate(inflater, container, false);

        View view = binding.getRoot();
        companyRepository = new CompanyRepository();



        companyRepository.getCompanies(new CompanyRepository.CompaniesCallBack() {
            @Override
            public void onSuccess(List<String> companies) {
                companiesData = companies;
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, companiesData);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.companySpinner.setAdapter(adapter);
                binding.companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedSchool = companiesData.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });







        Bundle bundle = getArguments();

        String email = bundle.getString("email");
        String password = bundle.getString("password");




        binding.companyNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedSchool==null){
                    Toast.makeText(getActivity(), "Lütfen bir kurum seçin", Toast.LENGTH_LONG).show();
                }
                else {

                    // There is no need to viewmodel because it is not liveData
                    UserRepository userRepository = new UserRepository();
                    userRepository.registerUser(new UserRepository.RegisterUserCallBack() {
                        @Override
                        public void onRegistered(Boolean success) {


                            Toast.makeText(getActivity(), "Kaydınız başarılı giriş yapın", Toast.LENGTH_LONG).show();

                            // Redirect to first fragment.
                            FragmentManager fragmentManager = getParentFragmentManager();
                            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.mainFrameLayout, new LoginRegisterFragment())
                                    .addToBackStack(null)
                                    .commit();

                        }



                        @Override
                        public void onFailure(String fail) {
                            Toast.makeText(getActivity(),fail,Toast.LENGTH_LONG).show();
                        }
                    },email, password, selectedSchool);

                }
            }
        });

        binding.companyPageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                fm.popBackStack();
            }
        });

        return view;
    }
}