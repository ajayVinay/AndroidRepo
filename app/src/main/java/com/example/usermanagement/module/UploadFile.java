package com.example.usermanagement.module;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.usermanagement.R;
import com.example.usermanagement.utils.S3Download;
import com.example.usermanagement.utils.S3Uploader;
import com.example.usermanagement.utils.S3Utils;

public class UploadFile  extends AppCompatActivity {
    private   String file_path ;
    private LinearLayout upload_layout_info,download_layout_info;
    S3Uploader s3uploaderObj;
    S3Download s3DownloadObj;
    String urlFromS3 = null;
    private TextView tvStatus;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Intent intent = getIntent();
        Bundle bundle =  intent.getExtras();
        file_path = bundle.getString("filepath");
        Log.d("dfghjklsdfg", "onCreate: " +file_path);

        tvStatus = findViewById(R.id.tv_text);
        // s3 object
        s3uploaderObj = new S3Uploader(this);
        s3DownloadObj = new S3Download(this);
        progressDialog = new ProgressDialog(this);

        upload_layout_info =(LinearLayout)findViewById(R.id.upload_layout_info);
        download_layout_info =(LinearLayout)findViewById(R.id.download_layout_info);
        upload_layout_info.setOnClickListener(View->uploadFile(file_path));

        //download_layout_info.setOnClickListener(View->downloadFile(file_path));
    }
    private void uploadFile(String file_path) {

        if (file_path !=null){
             showLoading("Uploading details !!");
            s3uploaderObj.initUpload(file_path);
            s3uploaderObj.setOns3UploadDone(new S3Uploader.S3UploadInterface() {
                @Override
                public void onUploadSuccess(String response) {
                    if (response.equalsIgnoreCase("COMPLETED")) {
                        hideLoading();

                        Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();
                        //tvStatus.setText("File Uploaded");
                       // urlFromS3 = S3Utils.generates3ShareUrl(getApplicationContext(), file_path);
                       /*if(!TextUtils.isEmpty(urlFromS3)) { tvStatus.setTextColor(getResources().getColor(R.color.colorAccent));

                            Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();
                            Log.d("urlfroms3", "onUploadSuccess: "+urlFromS3);
                        }*/
                    }
                }
                @Override
                public void onUploadError(String response) {
                  hideLoading();
                  tvStatus.setText("Error : "+response);
                    Log.e("error_response", "Error Uploading"+response);
                }
            });
        }
        }

    private void downloadFile(String file_path) {

        if(file_path != null){
            showLoading("DownLoading details !!");

            // final Bitmap bitmap = BitmapFactory.decodeFile(path);
            s3DownloadObj.initDownload(file_path);
            s3DownloadObj.setOns3DownloadDone(new S3Download.S3DownloadInterface() {
                @Override
                public void onDownloadSuccess(String response) {
                    if (response.equalsIgnoreCase("COMPLETED")) {
                        hideLoading();

                        Log.d("response", "onDownloadSuccess: "+response);
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
                    tvStatus.setText("Error : "+response);
                    Log.e("error_message", "Error Uploading"+response);
                }
            });
        }
    }
    private void showLoading(String message) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }
    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}

