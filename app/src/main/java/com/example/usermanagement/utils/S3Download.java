package com.example.usermanagement.utils;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.File;

public class S3Download {
    private static final String TAG = "S3Uploader";

    Context context;
    private TransferUtility transferUtility;
    public S3DownloadInterface s3DownloadInterface;

    public S3Download(Context context) {
        this.context = context;
        transferUtility = AmazonUtil.getTransferUtility(context);
    }
    public void initDownload(String filePath) {
        File file = new File(filePath);

        ObjectMetadata myObjectMetadata = new ObjectMetadata();
        myObjectMetadata.setContentType("text/html");
        String mediaUrl = file.getName();
       // final File localFile =File.createTempFile("image",getFil)
        TransferObserver observer = transferUtility.download(AWSKeys.BUCKET_NAME, mediaUrl,
                file);
        observer.setTransferListener(new DownloadListener());
    }
    private class DownloadListener implements TransferListener {

        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e(TAG, "Error during upload: " + id, e);
            s3DownloadInterface.onDownloadError(e.getLocalizedMessage());
        }
        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
        }
        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d(TAG, "onStateChangedDownload: " + id + ", " + newState);
            Log.d("dfghjkl","download done");
            if (newState == TransferState.COMPLETED) {
                s3DownloadInterface.onDownloadSuccess(newState.name());
                s3DownloadInterface.onDownloadSuccess("Success");
            }
        }
    }
    public void setOns3DownloadDone(S3DownloadInterface s3UploadInterface) {
        this.s3DownloadInterface = s3UploadInterface;
    }
    public interface S3DownloadInterface {
        void onDownloadSuccess(String response);

        void onDownloadError(String response);
    }
}
