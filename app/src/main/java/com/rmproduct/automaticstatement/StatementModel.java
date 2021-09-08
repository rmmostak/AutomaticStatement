package com.rmproduct.automaticstatement;

import java.util.Date;

public class StatementModel {
    private int slNo, sampleNo, id;
    private String dept, dateAccept;
    private float totalEarned, tax, remainMoney, uniAuthority, tcsWing, labCheck;

    public StatementModel() {
    }

    public StatementModel(int id, int slNo, int sampleNo, String dateAccept, String dept, float totalEarned, float tax, float remainMoney, float uniAuthority, float tcsWing, float labCheck) {
        this.id = id;
        this.slNo = slNo;
        this.sampleNo = sampleNo;
        this.dateAccept = dateAccept;
        this.dept = dept;
        this.totalEarned = totalEarned;
        this.tax = tax;
        this.remainMoney = remainMoney;
        this.uniAuthority = uniAuthority;
        this.tcsWing = tcsWing;
        this.labCheck = labCheck;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSlNo() {
        return slNo;
    }

    public void setSlNo(int slNo) {
        this.slNo = slNo;
    }

    public int getSampleNo() {
        return sampleNo;
    }

    public void setSampleNo(int sampleNo) {
        this.sampleNo = sampleNo;
    }

    public String getDateAccept() {
        return dateAccept;
    }

    public void setDateAccept(String dateAccept) {
        this.dateAccept = dateAccept;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public float getTotalEarned() {
        return totalEarned;
    }

    public void setTotalEarned(float totalEarned) {
        this.totalEarned = totalEarned;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(float remainMoney) {
        this.remainMoney = remainMoney;
    }

    public float getUniAuthority() {
        return uniAuthority;
    }

    public void setUniAuthority(float uniAuthority) {
        this.uniAuthority = uniAuthority;
    }

    public float getTcsWing() {
        return tcsWing;
    }

    public void setTcsWing(float tcsWing) {
        this.tcsWing = tcsWing;
    }

    public float getLabCheck() {
        return labCheck;
    }

    public void setLabCheck(float labCheck) {
        this.labCheck = labCheck;
    }
}
