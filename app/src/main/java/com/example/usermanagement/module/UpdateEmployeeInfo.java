package com.example.usermanagement.module;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.usermanagement.R;
import com.example.usermanagement.database.AppDatabase;
import com.example.usermanagement.database.UserDao;
import com.example.usermanagement.model.UserInfo;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateEmployeeInfo extends AppCompatActivity {

    public static String EXTRA_USER_ID = "user_id";
    private EditText name,designation,fatherName,motherName,dateOfBirth,address,contact,emailid,password,id;
    private CircleImageView img_profile;
    private UserDao mUserDao;
    private LinearLayout mUpdateButton;
    private Toolbar mToolbar;
    private UserInfo userInfo;
    private ImageView delete;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        mUserDao = Room.databaseBuilder(this, AppDatabase.class, "db-contacts")
                .allowMainThreadQueries()       //Allows room to do operation on main thread
                .build()
                .getUserDAO();

        img_profile=(CircleImageView)findViewById(R.id.user_profile);
        id = findViewById(R.id.edt_id);
        name= findViewById(R.id.edt_name);
        designation = findViewById(R.id.edt_designation);
        fatherName  = findViewById(R.id.edt_fatherName);
        motherName  = findViewById(R.id.edt_mother);
        dateOfBirth = findViewById(R.id.edt_date_of_birth);
        address     = findViewById(R.id.edt_address);
        contact     = findViewById(R.id.edt_contact);
        emailid     = findViewById(R.id.edt_email);
        password    = findViewById(R.id.edt_password);
        mUpdateButton= (LinearLayout)findViewById(R.id.upload_layout_info) ;
        mToolbar     =  (Toolbar)findViewById(R.id.update_actionbar) ;
        delete = (ImageView)findViewById(R.id.add_icon_img);
        delete.setImageDrawable(getResources().getDrawable(R.drawable.delete_icon));
        delete.setVisibility(View.VISIBLE);

        userInfo = mUserDao.getUserDetailById(getIntent().getStringExtra(EXTRA_USER_ID));
        Log.d("userGetIdUpdate", "onCreate: "+getIntent().getStringExtra(EXTRA_USER_ID));
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserDao.deleteDatabaseValue(userInfo);
                setResult(RESULT_OK);
                finish();
            }
        });
        initViews();
    }
    private void initViews() {
        id.setText(userInfo.getEmployeeID());
        name.setText(userInfo.getName());
        designation.setText(userInfo.getDesignation());
        fatherName.setText(userInfo.getFatherName());
        motherName.setText(userInfo.getMotherName());
        dateOfBirth.setText(userInfo.getDateOfBirth());
        address.setText(userInfo.getAddress());
        contact.setText(userInfo.getContactNo());
        emailid.setText(userInfo.getEmailId());
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String edt_id = id.getText().toString().trim();
                String new_name = name.getText().toString().trim();
                String new_role = designation.getText().toString().trim();
                String new_fName = fatherName.getText().toString().trim();
                String new_mName = motherName.getText().toString().trim();
                String new_dob = dateOfBirth.getText().toString().trim();
                String new_add = address.getText().toString().trim();
                String new_cont = contact.getText().toString().trim();
                String new_email = emailid.getText().toString().trim();

                userInfo.setEmployeeID(edt_id);
            userInfo.setName(new_name);
            userInfo.setDesignation(new_role);
            userInfo.setFatherName(new_fName);
            userInfo.setMotherName(new_mName);
            userInfo.setDateOfBirth(new_dob);
            userInfo.setAddress(new_add);
            userInfo.setContactNo(new_cont);
            userInfo.setEmailId(new_email);

            mUserDao.updateDatabase(userInfo);    // update database
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
