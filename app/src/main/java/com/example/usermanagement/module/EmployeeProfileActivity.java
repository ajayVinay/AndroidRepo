package com.example.usermanagement.module;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.usermanagement.R;
import com.example.usermanagement.database.AppDatabase;
import com.example.usermanagement.database.UserDao;
import com.example.usermanagement.model.UserInfo;

public class EmployeeProfileActivity extends AppCompatActivity {
    private TextView tvId,tvname,tvDesignation,tvFatherName,tvMOtherName,tvDateOfBirth,tvAddress,tvContactNo,tvEmailId;
    private String email_id;
    private ProgressDialog progressDialog;
    private UserInfo mUserInfo;
    private UserDao mUserDao ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee_profile);

        mUserDao = Room.databaseBuilder(this, AppDatabase.class, "db-contacts")
                .allowMainThreadQueries()           //Allows room to do operation on main thread
                .build()
                .getUserDAO();

        tvname = findViewById(R.id.tv_name);
        tvId = findViewById(R.id.tv_emp_id);
        tvDesignation = findViewById(R.id.tv_designation);
        tvFatherName = findViewById(R.id.tv_fname);
        tvMOtherName = findViewById(R.id.tv_mname);
        tvDateOfBirth = findViewById(R.id.tv_dob);
        tvAddress = findViewById(R.id.tv_address);
        tvContactNo = findViewById(R.id.tv_contact);
        tvEmailId = findViewById(R.id.tv_email);

        Intent intent = getIntent();
        email_id = intent.getStringExtra("emailid");
        Log.d("email", "onCreate: "+email_id);

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();
    }

    public class MyAsyncTasks extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(EmployeeProfileActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            getUserDetaByMailId(email_id);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (mUserInfo != null) {

                String name = mUserInfo.getName();
                String role = mUserInfo.getDesignation();
                String fName = mUserInfo.getFatherName();
                String mName = mUserInfo.getMotherName();
                String dob = mUserInfo.getDateOfBirth();
                String userAddress = mUserInfo.getAddress();
                String phoneNumber = mUserInfo.getContactNo();
                String emailid = mUserInfo.getEmailId();

                Log.d("username", name);
//                Log.d("emailId", emailid);
//                Log.d("pNumber", phoneNumber);
//                Log.d("uAddress", userAddress);
                tvname.setText(name);
               // tvId.setText(" UI900" + Integer.toString(mUserInfo.getEmployeeID())); // if taking id type integer
                tvId.setText(" UI900" +(mUserInfo.getEmployeeID()));
                tvDesignation.setText(role);
               tvFatherName.setText(fName);
               tvMOtherName.setText(mName);
               tvDateOfBirth.setText(dob);
               tvAddress.setText(userAddress);
               tvContactNo.setText(phoneNumber);
                tvEmailId.setText(emailid);
            }
        }
    }
    private void getUserDetaByMailId(String email_id) {
       UserInfo userInfo = mUserDao.getUserDetailByEmailId(email_id);
       this.mUserInfo = userInfo;

    }
}
