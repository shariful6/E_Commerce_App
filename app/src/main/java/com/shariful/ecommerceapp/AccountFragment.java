package com.shariful.ecommerceapp;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shariful.ecommerceapp.Model.ImageModel;
import com.shariful.ecommerceapp.Model.ProductModel;
import com.shariful.ecommerceapp.Retrofit.ApiClient;
import com.shariful.ecommerceapp.Retrofit.ApiInterface;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    TextView nameTv,phoneTv;

    ApiInterface apiInterface;
    String email;
    String phone;
    ImageView profileIv;


    private  static  final  int CAMERA_REQUEST_CODE=10;
    private  static  final  int STORAGE_REQUEST_CODE=20;
    private  static  final  int IMAGE_PICK_CAMERA_CODE=30;
    private  static  final  int IMAGE_PICK_GALLERY_CODE=40;
    String cameraPermission[] ;
    String storagePermission[];
    Uri image_rui=null;
    ProgressDialog pd;
    private Bitmap bitmap;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        profileIv = view.findViewById(R.id.profileIv_id);
        nameTv = view.findViewById(R.id.userNameTv_id);
        phoneTv = view.findViewById(R.id.userPhoneTv_id);
        pd = new ProgressDialog(getContext());
        loadUserInfo();

        //init permission
        cameraPermission = new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        profileIv.setOnClickListener(v->{
            showImagePicDialog();
        });


        return view;
    }


    private void loadUserInfo() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("emailkey", "No Email Found !");
        String name = sharedPreferences.getString("namekey", "No name Found !");
        phone = sharedPreferences.getString("phonekey", "No phone Found !");

        nameTv.setText(name);
        phoneTv.setText(phone);

        loadProfileImage();
    }
    private void loadProfileImage(){

        Call<ImageModel> imageModelCall = apiInterface.loadPicture(email);

        imageModelCall.enqueue(new Callback<ImageModel>() {
            @Override
            public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {

                if (response.isSuccessful() && response.body()!=null)
                {
                    ImageModel imageModel = response.body();

                    if (imageModel.isSuccess())
                    {
                        String img = imageModel.getMessage();
                       // Toast.makeText(getActivity(), ""+img, Toast.LENGTH_SHORT).show();
                        String url = "https://sssoftwarehub.xyz/ECommerceApp/"+img;


                        /**
                        Glide.with(getActivity())
                                .load(url)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(profileIv);

                        */

                        try{
                            Picasso.get().load(url).placeholder(R.drawable.profile).into(profileIv);
                        }
                        catch (Exception e){
                        }

                        /**
                        Glide.with(getActivity())
                                .load(url)
                                .centerCrop()
                                .placeholder(R.drawable.profile)
                                .into(profileIv);

                        */
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Failed To retrive !!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ImageModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Could not Connect", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //permission
    private void showImagePicDialog() {

        String option[]= {"Camera","Gallery"};
        //create aletdialouge
        AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        //set tittle
        builder.setTitle("Choose Image from");
        //set iteems to dialogue
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which==0)
                {
                    //camera click
                    if (!checkCameraPermission())
                    {
                        requestCameraPermission();
                    }
                    else
                    {
                        pickFromCamera();
                    }


                }
                else if(which==1)
                {
                    //gallery click
                    if (!checkStoragePermission())
                    {
                        requestStoragePermission();
                    }
                    else
                    {
                        pickFromGallery();
                    }
                }
            }
        });
        //create and show dialogue
        builder.create().show();

    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                ==(PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);

        return result && result1;

    }

    private  void requestCameraPermission() {

        ActivityCompat.requestPermissions(getActivity(),cameraPermission,CAMERA_REQUEST_CODE);

    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);

        return result;

    }

    private  void requestStoragePermission() {

        ActivityCompat.requestPermissions(getActivity(),storagePermission,STORAGE_REQUEST_CODE);

    }
    private void pickFromCamera() {
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        image_rui = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_rui);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }

    //handle permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getContext(), "Please enable Camera & Storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getContext(), "Please enable Storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri path = data.getData();
        if (resultCode==RESULT_OK)
        {
            if (requestCode==IMAGE_PICK_GALLERY_CODE)
            {

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),path);
                    profileIv.setImageBitmap(bitmap);
                    uploadData();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (requestCode==IMAGE_PICK_CAMERA_CODE)
            {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),path);
                    profileIv.setImageBitmap(bitmap);
                    uploadData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadData() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        String encodedImage =  Base64.encodeToString(imageInByte,Base64.DEFAULT);

        Call<ImageModel> imageModelCall = apiInterface.saveImage(phone,encodedImage);

        imageModelCall.enqueue(new Callback<ImageModel>() {
            @Override
            public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {

                if (response.isSuccessful() && response.body()!=null)
                {
                    ImageModel imageModel = response.body();

                    if (imageModel.isSuccess())
                    {
                        Toast.makeText(getContext(), "Product Added Successfully!!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), ""+imageModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ImageModel> call, Throwable t) {
                // Toast.makeText(AddProductActivity.this, "Could not Connect", Toast.LENGTH_SHORT).show();
            }
        });


    }



}