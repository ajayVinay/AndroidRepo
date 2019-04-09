package com.example.usermanagement.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "user_information")
public class UserInfo {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "EmployeeID")
    private String employeeID;

    @ColumnInfo(name ="registration")
    private String registration;

    @ColumnInfo(name = "Name" )
    private String name;

    @ColumnInfo(name ="Father Name" )
    private String fatherName;

    @ColumnInfo(name = "Mother Name" )
    private String motherName;

    @ColumnInfo(name ="Date of Birth" )
    private String DateOfBirth;

    @ColumnInfo(name ="Contact No" )
    private String contactNo;

    @ColumnInfo(name ="Address" )
    private String address;

    @ColumnInfo(name ="Designation" )
    private String designation;

    @ColumnInfo(name ="Email Id" )
    private String emailId;

    @ColumnInfo(name ="Password" )
    private String password;

    public String getEmployeeID() {
        return employeeID;
    }
    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
