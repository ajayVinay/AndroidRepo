package com.example.usermanagement.module;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.usermanagement.Helper.IOHelper;
import com.example.usermanagement.R;
import com.example.usermanagement.adapter.UserRecyclerAdpater;
import com.example.usermanagement.database.AppDatabase;
import com.example.usermanagement.database.UserDao;
import com.example.usermanagement.model.UserInfo;
import com.example.usermanagement.utils.CommanUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private FloatingActionButton mAddContactFloatingActionButton;
    private static final String FILE_NAME ="textFileTest.txt";
    private static final int RC_CREATE_USER = 1;
    private static final int RC_UPDATE_USER = 2;
    private RecyclerView mContactsRecyclerView;
    private UserRecyclerAdpater mUserRecyclerAdpater;
    private UserDao mUserDao;
    private  static String path = null;
    private ImageView imgCloud;
    private Toolbar mToolbar;
    private CommanUtility commanUtility;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupToolbar();
        commanUtility = new CommanUtility(this);

        // Database name "db-contact"

        mUserDao = Room.databaseBuilder(this, AppDatabase.class, "db-contacts")
                .allowMainThreadQueries()          //Allows room to do operation on main thread
                .build()
                .getUserDAO();
        mContactsRecyclerView = findViewById(R.id.contactsRecyclerView);
        mContactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAddContactFloatingActionButton = findViewById(R.id.addContactFloatingActionButton);
        mUserRecyclerAdpater = new UserRecyclerAdpater(this, new ArrayList<UserInfo>());

        mUserRecyclerAdpater.addActionCallback(userInfo -> {
            Intent intent = new Intent(ProfileActivity.this, UpdateEmployeeInfo.class);
            intent.putExtra(UpdateEmployeeInfo.EXTRA_USER_ID, userInfo.getEmployeeID());
            Log.d("userID", "onCreate: "+userInfo.getEmployeeID());
            startActivityForResult(intent, RC_UPDATE_USER);
        });
        mContactsRecyclerView.setAdapter(mUserRecyclerAdpater);
        mAddContactFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, CreateEmployee.class);
                startActivityForResult(intent, RC_CREATE_USER);
            }
        });
        loadData();
        imgCloud.setOnClickListener(View->callToUpload());
    }
    private void setupToolbar() {
        mToolbar =  (Toolbar)findViewById(R.id.top_actionbar_profile) ;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        imgCloud = (ImageView)findViewById(R.id.add_icon_img);
        imgCloud.setImageDrawable(getResources().getDrawable(R.drawable.cloud));
        imgCloud.setVisibility(View.VISIBLE);
    }
    private void loadData() {
        mUserRecyclerAdpater.updateData(mUserDao.getUserInfo());
        JsonArray testArray = new JsonArray();
        for(int i=0;i<mUserDao.getUserInfo().size();i++){
            UserInfo userInfo = mUserDao.getUserInfo().get(i);
            JsonObject testJson = new JsonObject();
            testJson.addProperty("id",userInfo.getEmployeeID());
            testJson.addProperty("name",userInfo.getName());
            testJson.addProperty("fName",userInfo.getFatherName());
            testJson.addProperty("mName",userInfo.getMotherName());
            testJson.addProperty("dob",userInfo.getDateOfBirth());
            testJson.addProperty("contact",userInfo.getContactNo());
            testJson.addProperty("address",userInfo.getAddress());
            testJson.addProperty("designation",userInfo.getDesignation());
            testJson.addProperty("email",userInfo.getEmailId());
            testJson.addProperty("password",userInfo.getPassword());
            testArray.add(testJson);
            Log.d("testArrayData", "loadData:" +testArray);
        }
        IOHelper.writeToFile(getApplicationContext(),FILE_NAME,testArray.toString());

        //"/data/data/your_project_package_structure/files/textFileTest.txt"     file path
        //readJson();
    }
    private void readJson()  {
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr  = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text ;
            while((text = br.readLine()) != null){
                sb.append(text).append("\n");
            }
            Log.d("readLineArray", "readJson: "+sb.append(text));
            path = getFilesDir()+"/"+FILE_NAME;
            Log.d("path", "readJson: "+path);
            Toast.makeText(getApplicationContext(),sb.append(text).append("\n").toString(),Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
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
       /* String jsonString = IOHelper.stringFromFile(new File(FILE_NAME));
        JsonArray jsonArray = new JsonArray();
*/
    }
    private void callToUpload() {
        readJson();
        Intent nextActivity = new Intent(ProfileActivity.this,UploadFile.class);
        nextActivity.putExtra("filepath",path);
        startActivity(nextActivity);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CREATE_USER && resultCode == RESULT_OK) {
            loadData();
        } else if (requestCode == RC_UPDATE_USER && resultCode == RESULT_OK) {
            loadData();
    }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                Toast.makeText(getApplicationContext(),"Logout Selected",Toast.LENGTH_LONG).show();
                Intent outLogin = new Intent(ProfileActivity.this,Login.class);
               commanUtility.clearSharedPreference();
                mUserDao.deleteAlldatabasse();
                Toast.makeText(getApplicationContext(),"Databse Deleted",Toast.LENGTH_LONG).show();
                startActivity(outLogin);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}