package com.boco.miboy.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.boco.miboy.R;
import com.boco.miboy.activity.MainActivity;
import com.boco.miboy.backend.ApiCall;
import com.boco.miboy.other.Const;
import com.boco.miboy.other.ImageUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class PhotoFragment extends Fragment {
    private static final String TAG = PhotoFragment.class.getSimpleName();

    @OnClick(R.id.photo_bt)
    public void onPhotoClick(View view) {
        if (getMainActivity().hasCameraPermissions()) {
            makePhoto();
        }
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, view);
        Log.i(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: ");
        getMainActivity().progressDialog.dismiss();
        if (resultCode == RESULT_OK) {
            sendPhoto();
        } else {
            showToast(R.string.text_error_uploading_img);
        }
    }

    private void sendPhoto() {
        Log.i(TAG, "sendPhoto: ");
        String imagePath = getMainActivity().imagePath;
        if (imagePath != null && !imagePath.isEmpty()) {
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Bitmap bitmap = ImageUtil.decodeSampledBitmap(imagePath, 750, 750);

            getMainActivity().progressDialog.show();
            new ApiCall().photo(userUid, bitmap);
        } else {
            showToast(R.string.text_error_uploading_img);
        }
    }

    private void makePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = ImageUtil.createImageFile(getContext());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "dispatchTakePictureIntent: error creating file");
                showToast(R.string.text_error_creating_file);
            }
            if (photoFile != null) {
                getMainActivity().imagePath = photoFile.getAbsolutePath();

                // Continue only if the File was successfully created
                Uri photoURI = FileProvider.getUriForFile(getContext(), "com.boco.miboy.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                getMainActivity().startActivityForResult(takePictureIntent, Const.CAMERA_RC);
            }
        }
    }

    private void showToast(int text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }
}