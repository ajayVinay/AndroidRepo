package com.example.usermanagement.module;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.usermanagement.R;
import com.example.usermanagement.database.AppDatabase;
import com.example.usermanagement.database.UserDao;
import com.example.usermanagement.model.UserInfo;
import com.example.usermanagement.utils.CommanUtility;
import com.example.usermanagement.utils.S3Download;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Login extends AppCompatActivity {

    private static final String FILE_NAME ="textFileTest.txt";
    private  static final String path = null;

    private EditText username,password;
    private ImageView backup;

    private int  DELAY_TIME =10000;


    private UserDao mUserDao;
    private S3Download s3DownloadObj;
    private ProgressDialog progressDialog;
    private Handler handler;

    private SharedPreferences mPrefs;
    private CommanUtility commanUtility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        commanUtility = new CommanUtility(this);

        mUserDao = Room.databaseBuilder(this, AppDatabase.class, "db-contacts")
                .allowMainThreadQueries()                //Allows room to do operation on main thread
                .build()
                .getUserDAO();
        // shared pref

        s3DownloadObj = new S3Download(this);
        progressDialog = new ProgressDialog(this);
        username = (EditText) findViewById(R.id.login_user_text);
        password = (EditText) findViewById(R.id.login_password_text);
        backup = (ImageView)findViewById(R.id.img_backup);
        backup.setOnClickListener(view ->backupData());    //  using lamda
        findViewById(R.id.btn_Login).setOnClickListener(view -> login());
    }
    private void backupData() {
        syncDataAdminLogin();
    }
    private void login() {
        String email = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        Cursor checkEmailAndPass = mUserDao.chechEmailPAssword(email,pass);
        if (!email.isEmpty() && !pass.isEmpty()) {

            if (email.equals("admin123@gmail.com")&& pass.equals("admin@123")){
                commanUtility.setLoginStatus(true);
                syncDataAdminLogin();


                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adminLogin();
                    }
                },DELAY_TIME);
            }else {
                if (checkEmailAndPass.getCount()>0){
                    Intent next = new Intent(Login.this, EmployeeProfileActivity.class);
                    next.putExtra("emailid",email);
                    startActivity(next);
                } else {
                    Toast.makeText(getApplicationContext(),"UnAuthorized email or password",Toast.LENGTH_SHORT).show();
                }
            }

//          if (checkEmailAndPass.getCount()>0) {
//                 if (email.equals("admin123@gmail.com")&& pass.equals("admin@123")){
//
//
//                 }else{
//                     Intent next = new Intent(Login.this, EmployeeProfileActivity.class);
//                     next.putExtra("emailid",email);
//                     startActivity(next);
//                 }
//             }

        }
        else {
            Toast.makeText(Login.this, "Enter Your Email And Password", Toast.LENGTH_SHORT).show();
        }
    }
    // sync Data when admin login
    private void syncDataAdminLogin() {
        showLoading("DownLoading details !!");

        String path  = "/data/user/0/com.example.usermanagement/files/textFileTest.txt";
        s3DownloadObj.initDownload(path);
        s3DownloadObj.setOns3DownloadDone(new S3Download.S3DownloadInterface() {
            @Override
            public void onDownloadSuccess(String response) {
                if (response.equalsIgnoreCase("COMPLETED")) {
                    hideLoading();
                    Log.d("response", "onDownloadSuccess: "+response);
                    try {
                        String jsonDataString = readJsonDataFromFile();
                        JSONArray itemsJsonArray = new JSONArray(jsonDataString);
                        for (int i = 0; i < itemsJsonArray.length(); ++i) {

                            // write code here for handling
                            JSONObject jsonObject=itemsJsonArray.getJSONObject(i);

                             String id = jsonObject.getString("id");
                            String name = jsonObject.getString("name");
                            String fathername = jsonObject.getString("fName");
                            String mothoername = jsonObject.getString("mName");
                            String dateOfBrt = jsonObject.getString("dob");
                            String contact = jsonObject.getString("contact");
                            String address = jsonObject.getString("address");
                            String designation = jsonObject.getString("designation");
                            String email = jsonObject.getString("email");
                            String password = jsonObject.getString("password");
                            UserInfo addUserInfo = new UserInfo();
                            addUserInfo.setEmployeeID(id);
                            addUserInfo.setName(name);
                            addUserInfo.setFatherName(fathername);
                            addUserInfo.setMotherName(mothoername);
                            addUserInfo.setDateOfBirth(dateOfBrt);
                            addUserInfo.setContactNo(contact);
                            addUserInfo.setAddress(address);
                            addUserInfo.setDesignation(designation);
                            addUserInfo.setEmailId(email);
                            addUserInfo.setPassword(password);

                            mUserDao.insert(addUserInfo);
                        }
                    } catch (JSONException e) {
                        Log.e("error", e.getMessage(), e);
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),"File Downloaded",Toast.LENGTH_LONG).show();
                       /* urlFromS3 = S3Utils.generates3ShareUrl(getApplicationContext(), file_path);
                        if(!TextUtils.isEmpty(urlFromS3)) {
                            tvStatus.setText("Download : "+urlFromS3);
                        }*/
            }
            }
            @Override
            public void onDownloadError(String response) {
                hideLoading();
               // tvStatus.setText("Error : "+response);
                Log.e("error_message", "Error Uploading"+response);
            }
        });
    }
    private String readJsonDataFromFile() {
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr  = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String text ;
            while((text = br.readLine()) != null){
                sb.append(text).append("\n");
            }
            Log.d("jsonFileValue", "onDownloadSuccess: "+sb.append(text));
            final String path = getFilesDir()+"/"+FILE_NAME;
            Log.d("path", "readJson: "+path);
            Toast.makeText(getApplicationContext(),sb.append(text).append("\n").toString(),Toast.LENGTH_LONG).show();

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new String(sb);
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private void showLoading(String message) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }
    private void adminLogin() {
        Intent admin = new Intent(Login.this,ProfileActivity.class);
        startActivity(admin);
    }

}
