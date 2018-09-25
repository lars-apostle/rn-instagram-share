package com.travelshelf;

import android.content.Intent;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import android.net.Uri;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.ComponentName;
import android.support.v4.content.FileProvider;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

public class RNInstagramShareModule extends ReactContextBaseJavaModule {

    private ReactApplicationContext reactContext;
    public RNInstagramShareModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext=reactContext;
    }
    @Override
    public String getName() {
        return "InstagramShare";
    }

    public String instagramPackageName() {
        return "com.instagram.android";
    }

    public Uri getVideoFromUrl(String src) {
        // Set basic values
        int count;
        String tempFile = Environment.getExternalStorageDirectory().toString() + "/temp.mp4";

        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            int lenghtOfFile = connection.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream output = new FileOutputStream(tempFile);

            byte data[] = new byte[1024];

            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            // get the file which has been saved
            File videoFile = new File(tempFile);

            return Uri.parse(videoFile.getPath());
        } catch (IOException e) {
            return null;
        }
    }

    public Uri getUriFromBitmap(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            int lenghtOfFile = connection.getContentLength();
            InputStream input = connection.getInputStream();
            Bitmap imageBitmap = BitmapFactory.decodeStream(input);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(lenghtOfFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(this.reactContext.getContentResolver(), imageBitmap, "temp.jpg", "this is the description");
            return Uri.parse(path);
        } catch (IOException e) {
            return null;
        }
    }

    @ReactMethod
    private void share(String type, String mediaPath)
    {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setPackage(instagramPackageName());

        if(type.equals("image")) {
            share.setType("image/*");
            share.putExtra(Intent.EXTRA_STREAM, getUriFromBitmap(mediaPath));
        }

        if(type.equals("video")) {
            share.setType("video/*");
            share.putExtra(Intent.EXTRA_STREAM, getVideoFromUrl(mediaPath));
        }

        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        reactContext.startActivity(share);
    }

    @ReactMethod
    public void canShare(final Promise promise)
    {
        PackageManager pm = this.reactContext.getPackageManager();

        try {
            pm.getPackageInfo(instagramPackageName(), 0);
            promise.resolve(true);
        } catch (PackageManager.NameNotFoundException e) {
            promise.resolve(false);
        }
    }
}