package com.example.purchaserequisitionapp;

public class PerchaseRequisition {

    private String id;
    private String date;
    private String approved;

    public PerchaseRequisition(String id, String date, String approved){
        this.id = id;
        this.date = date;
        this.approved = approved;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getApproved() {
        return approved;
    }

    @Override
    public String toString(){
        return this.getApproved();
    }
}
