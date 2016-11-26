package com.boco.miboy.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.boco.miboy.other.Const;
import com.boco.miboy.other.ImageUtil;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public abstract class PermissionActivity extends AppCompatActivity {
    private static final String TAG = PermissionActivity.class.getSimpleName();
    public String imagePath;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// Permissions//////////////////////////////////////////////
    protected boolean isCameraPossible() {
        return ActivityCompat.checkSelfPermission(this, CAMERA) == PackageManager
                .PERMISSION_GRANTED;
    }

    protected boolean isReadPossible() {
        return ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager
                .PERMISSION_GRANTED;
    }

    protected boolean isWritePossible() {
        return ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager
                .PERMISSION_GRANTED;
    }

    public boolean hasCameraPermissions() {
        boolean res = false;
        if (!isCameraPossible() || !isReadPossible() || !isWritePossible()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, Const.PERMISSIONS_ALL);
        } else {
            Log.i(TAG, "Camera permissions have already been granted");
            res = true;
        }
        return res;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// Image Dialog/////////////////////////////////////////////

    private void makePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = ImageUtil.createImageFile(this);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "dispatchTakePictureIntent: error creating file");
//                showSnackBar(R.string.text_error_creating_file);
            }
            if (photoFile != null) {
                imagePath = photoFile.getAbsolutePath();

                // Continue only if the File was successfully created
                Uri photoURI = FileProvider.getUriForFile(this, "com.boco.hh.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Const.CAMERA_RC);
            }
        }
    }

    private void gallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, Const.RESULT_LOAD_IMAGE);
    }
}