package com.example.usermanagement.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.usermanagement.model.UserInfo;
import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(UserInfo info);                       // this is for insert value  inside database

    @Update
    void updateDatabase(UserInfo info);               // this is for update value  inside database

    @Delete
    void deleteDatabaseValue(UserInfo info);          // this is for delete value  inside database

    @Query("SELECT * FROM user_information")          // get all information
    List<UserInfo> getUserInfo();

    @Query("DELETE FROM user_information")
     void deleteAlldatabasse();                       // delete all table entry

//    @Query("DROP TABLE user_information ")
//    void dropTable();

    @Query("SELECT * FROM user_information WHERE `EmployeeID` = :userId" )  // get user detail by userId from  database
    UserInfo getUserDetailById(String userId);

    @Query("SELECT * FROM user_information WHERE `Email Id` = :username")   // get user details by email from database
    UserInfo getUserDetailByEmailId(String username );


    @Query("SELECT * FROM user_information WHERE `Email Id` = :username")    // check mail if user not exist then create
    Cursor checkMail(String username);

    @Query("SELECT * FROM user_information WHERE `EmployeeID` = :id")     // check id and create user if user not exist with this id
    Cursor checkId(String id);

    @Query("SELECT * FROM user_information WHERE `Email Id` = :username and  password = :password")
     Cursor chechEmailPAssword(String username,String password);   // email and password exist or not in database
}
