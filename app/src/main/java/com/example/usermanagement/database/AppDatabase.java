package com.example.usermanagement.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.example.usermanagement.model.UserInfo;


@Database(entities = {UserInfo.class},version = 1 ,exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
         public abstract UserDao getUserDAO();
}