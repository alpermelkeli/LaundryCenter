package com.alpermelkeli.laundrycenter.model;
import java.util.List;
public class User {
    private Double balance;
    private String company;
    private String email;
    private List<String> history;

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
