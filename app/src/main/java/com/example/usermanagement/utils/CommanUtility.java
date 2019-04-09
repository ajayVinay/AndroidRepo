package com.example.usermanagement.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

public class CommanUtility {
    protected Context context;

    public CommanUtility(Context context) {
        this.context = context;
    }
    public File initializeMediaPath(String optionalSubDir) {
        File firebaseMediaDir = null;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            firebaseMediaDir = ContextCompat.getExternalFilesDirs(context, null)[0];

        } else {
            firebaseMediaDir = context.getFilesDir();
        }
        if (TextUtils.isEmpty(optionalSubDir) || optionalSubDir.trim().equals("")) {
            return firebaseMediaDir;
        } else {
            firebaseMediaDir = new File(firebaseMediaDir, optionalSubDir);
            firebaseMediaDir.mkdirs();
        }
        return firebaseMediaDir;
    }
    public Bitmap decodeBitmapEfficiently(String imagePath, int reqWidth, int reqHeight) {

        if (reqWidth == 0) {
            reqWidth = 100;
        }
        if (reqHeight == 0) {
            reqHeight = 100;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();

        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imagePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);

    }

    private  int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
    public String getFilePath(final Context context, final Uri uri) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return getFilePathForKitKatUpperVersion(context, uri);
        } else {
            return getFilePathForKitKatLowerVersion(context, uri);
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getFilePathForKitKatUpperVersion(final Context context, final Uri uri) {

        if(DocumentsContract.isDocumentUri(context, uri)) {
           // ExternalStorageProvider
            if(isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
           // DownloadsProvider
            else if(isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
              // MediaProvider
            else if(isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if(cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if(cursor != null) cursor.close();
        }
        return null;
    }

    private String getFilePathForKitKatLowerVersion(final Context context, final Uri uri) {
        if("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    /**
     @param uri The Uri to check.
     @return Whether the Uri authority is ExternalStorageProvider.
     */
    public  boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     @param uri The Uri to check.
     @return Whether the Uri authority is DownloadsProvider.
     */
    public  boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    /**
     @param uri The Uri to check.
     @return Whether the Uri authority is MediaProvider.
     */
    public  boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public SharedPreferences getSharedPrefrences(){

        return context.getSharedPreferences(AppConstant.SHARED_PREFRENCES, MODE_PRIVATE);
    }

    public void setLoginStatus(boolean isLogin){

        SharedPreferences mPref = getSharedPrefrences();
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(AppConstant.LOGIN_STATUS, isLogin);
        editor.commit();
    }
    public boolean getLoginStatus(){

       return getSharedPrefrences().getBoolean(AppConstant.LOGIN_STATUS, false);

    }

    public void clearSharedPreference(){
        SharedPreferences mPref = getSharedPrefrences();
        SharedPreferences.Editor editor = mPref.edit();
        editor.clear();
        editor.commit();

    }
}
