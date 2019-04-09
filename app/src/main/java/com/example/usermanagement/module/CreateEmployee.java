package com.example.usermanagement.module;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.persistence.room.Room;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.usermanagement.R;
import com.example.usermanagement.database.AppDatabase;
import com.example.usermanagement.database.UserDao;
import com.example.usermanagement.model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class CreateEmployee extends AppCompatActivity {

    private EditText id,name,designation,fatherName,motherName,dateOfBirth,address,contact,emailid,password;
    private Button save;
    private UserDao mUserDao;
    public static final String CHANNEL_ID ="Push_Notification";
    public static final String CHANNEL_NAME ="Push Notification";
    public static final String CHANNEL_DESC ="Android Push Notification";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);     // 19/3/2019 change for null
                                                                // pointer exception (only check)
            }
        }
        // Database name "db-contact"
        mUserDao = Room.databaseBuilder(this, AppDatabase.class, "db-contacts")
                .allowMainThreadQueries()           //Allows room to do operation on main thread
                .build()
                .getUserDAO();
        id = findViewById(R.id.edt_id);
        name= findViewById(R.id.edt_name);
        designation = findViewById(R.id.edt_designation);
        fatherName = findViewById(R.id.edt_fatherName);
        motherName = findViewById(R.id.edt_mother);
        dateOfBirth = findViewById(R.id.edt_date_of_birth);
        address = findViewById(R.id.edt_address);
        contact = findViewById(R.id.edt_contact);
        emailid = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
       findViewById(R.id.btn_save).setOnClickListener(view->saveinfo());
    }
    private void saveinfo() {
        String edt_id = id.getText().toString().trim();
        String edt_name = name.getText().toString().trim();
        String edt_designation = designation.getText().toString().trim();
        String edt_fatherName = fatherName.getText().toString().trim();
        String edt_motherName = motherName.getText().toString().trim();
        String edt_dateof_birth = dateOfBirth.getText().toString().trim();
        String edt_address = address.getText().toString().trim();
        String edt_contact = contact.getText().toString().trim();
        String edt_email = emailid.getText().toString().trim();
        String edt_password = password.getText().toString().trim();

        if (edt_id.length() == 0 ||edt_name.length() == 0 || edt_designation.length() == 0
                || edt_fatherName.length() == 0 || edt_motherName.length() == 0 || edt_dateof_birth.length() == 0
                || edt_address.length() == 0 || edt_contact.length() == 0 || edt_email.length() == 0
                || edt_password.length() == 0) {
            Toast.makeText(CreateEmployee.this, "Please make sure all details are correct", Toast.LENGTH_SHORT).show();
        }
        else {
            if (edt_email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                Cursor cursor = mUserDao.checkMail(edt_email);
                Cursor cursorid = mUserDao.checkId(edt_id);
                if (cursor.getCount()>0 && cursorid.getCount()>0){
                    Toast.makeText(getApplicationContext(),"User Already exists with this email or Employee id",Toast.LENGTH_LONG).show();
                }
                else {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setEmployeeID(edt_id);
                    userInfo.setName(edt_name);
                    userInfo.setDesignation(edt_designation);
                    userInfo.setFatherName(edt_fatherName);
                    userInfo.setMotherName(edt_motherName);
                    userInfo.setDateOfBirth(edt_dateof_birth);
                    userInfo.setAddress(edt_address);
                    userInfo.setContactNo(edt_contact);
                    userInfo.setEmailId(edt_email);
                    userInfo.setPassword(edt_password);
                    //Insert to database
                    try {
                        mUserDao.insert(userInfo);
                        // firebase notification to get Token
           FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (task.isSuccessful()) {
                                    String token = task.getResult().getToken();
                                    Log.d("asdfghj",token);
                                    //saveToken(token);
                                }else {
                                }
                            }
                        });
                        setResult(RESULT_OK);
                        finish();
                    } catch (SQLiteConstraintException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                emailid.setError("Invalid Email Address");
            }
        }
    }
}
