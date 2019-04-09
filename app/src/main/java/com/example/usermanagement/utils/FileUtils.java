package com.example.usermanagement.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;


/**
 Utility class containing File-related helper functions. */
public class FileUtils {

    /**
     Copy a file from one location to another.
     @param sourceFile File to copy from.
     @param destFile File to copy to.
     @return True if successful, false otherwise.
     @exception IOException
     */
    public static Boolean copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();

            FileChannel source = null;
            FileChannel destination = null;
            try {
                source = new FileInputStream(sourceFile).getChannel();
                destination = new FileOutputStream(destFile).getChannel();
                destination.transferFrom(source, 0, source.size());
            } finally {
                if(source != null) source.close();
                if(destination != null) destination.close();
            }
            return true;
        }
        return false;
    }


    public static void copy(InputStream source, File destFile) {

        FileOutputStream destination=null;
        try {

            destination = new FileOutputStream(destFile);

            if(source != null && destination != null) {
                int length = 4096;
                byte[] buffer = new byte[length];

                int count4Flush = 0;

                while(source.read(buffer, 0, length) >= 0) {
                    destination.write(buffer);

                    count4Flush++;

                    if(count4Flush == 10) {
                        destination.flush();
                        count4Flush = 0;
                    }

                }

                destination.flush();

            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(destination!=null){
                try { destination.close(); }catch(Exception e1){}
            }
        }

    }


    public static void copy(InputStream source, OutputStream destination) throws IOException {
        if(source != null && destination != null) {
            int length = 4096;
            byte[] buffer = new byte[length];

            int count4Flush = 0;

            while(source.read(buffer, 0, length) >= 0) {
                destination.write(buffer);

                count4Flush++;

                if(count4Flush == 10) {
                    destination.flush();
                    count4Flush = 0;
                }

            }

            destination.flush();

        }


    }


    /**
     Read a text file into a String.
     @param file File to read (will not seek, so things like /proc files are
     OK).
     @return The contents of the file as a String.
     @exception IOException
     */
    public static String readTextFile(File file) throws IOException {
        byte[] buffer = new byte[(int)file.length()];
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
        stream.read(buffer);
        stream.close();
        
        return new String(buffer);
    }


}
