package com.rmproduct.automaticstatement;

import java.util.Date;

public class StatementModel {
    private int slNo, sampleNo, id;
    private String dept, dateAccept;
    private float totalEarned, tax, remainMoney, uniAuthority, tcsWing, labCheck, testCost, pi, chair, teachers, labDev, staff;

    public StatementModel() {
    }

    public StatementModel(int slNo, int sampleNo, int id, String dept, String dateAccept, float totalEarned, float tax, float remainMoney, float uniAuthority, float tcsWing, float labCheck, float testCost, float pi, float chair, float teachers, float labDev, float staff) {
        this.slNo = slNo;
        this.sampleNo = sampleNo;
        this.id = id;
        this.dept = dept;
        this.dateAccept = dateAccept;
        this.totalEarned = totalEarned;
        this.tax = tax;
        this.remainMoney = remainMoney;
        this.uniAuthority = uniAuthority;
        this.tcsWing = tcsWing;
        this.labCheck = labCheck;
        this.testCost = testCost;
        this.pi = pi;
        this.chair = chair;
        this.teachers = teachers;
        this.labDev = labDev;
        this.staff = staff;
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

    public float getTestCost() {
        return testCost;
    }

    public void setTestCost(float testCost) {
        this.testCost = testCost;
    }

    public float getPi() {
        return pi;
    }

    public void setPi(float pi) {
        this.pi = pi;
    }

    public float getChair() {
        return chair;
    }

    public void setChair(float chair) {
        this.chair = chair;
    }

    public float getTeachers() {
        return teachers;
    }

    public void setTeachers(float teachers) {
        this.teachers = teachers;
    }

    public float getLabDev() {
        return labDev;
    }

    public void setLabDev(float labDev) {
        this.labDev = labDev;
    }

    public float getStaff() {
        return staff;
    }

    public void setStaff(float staff) {
        this.staff = staff;
    }
}
