package com.shariful.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.shariful.ecommerceapp.Model.CategoryModel;
import com.shariful.ecommerceapp.Model.ImageModel;
import com.shariful.ecommerceapp.Model.ProductModel;
import com.shariful.ecommerceapp.Retrofit.ApiClient;
import com.shariful.ecommerceapp.Retrofit.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    private Button saveBtn,categoryBtn;
    private EditText productNameEt,productPriceEt,categoryEt,productCatEt;
    private ImageView productImage;

    private  static  final  int CAMERA_REQUEST_CODE=10;
    private  static  final  int STORAGE_REQUEST_CODE=20;
    private  static  final  int IMAGE_PICK_CAMERA_CODE=30;
    private  static  final  int IMAGE_PICK_GALLERY_CODE=40;

    String cameraPermission[] ;
    String storagePermission[];
    Uri image_rui=null;
    ProgressDialog pd;

    private Bitmap bitmap;
    ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        saveBtn = findViewById(R.id.saveBtn_id);
        productNameEt = findViewById(R.id.productNameEtAdmin_id);
        productPriceEt = findViewById(R.id.productPriceEtAdmin_id);
        productImage = findViewById(R.id.productImageIvAdmin_id);
        categoryBtn = findViewById(R.id.categoryBtn_id);
        categoryEt = findViewById(R.id.categoryEt_id);
        productCatEt = findViewById(R.id.categoryEtAdmin_id);

        pd = new ProgressDialog(this);

        //init permission
        cameraPermission = new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};


        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicDialog();
            }
        });

    } //end onCreate Method

    private void addCategory() {
        String name = categoryEt.getText().toString();

        Call<CategoryModel> categoryModelCall = apiInterface.saveCategory(name,"0");

        categoryModelCall.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {

                if (response.isSuccessful() && response.body()!=null)
                {
                    CategoryModel categoryModel = response.body();

                    if (categoryModel.isSuccess())
                    {
                        Toast.makeText(AddProductActivity.this, "Product Added Successfully!!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(AddProductActivity.this, ""+categoryModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, "Could not Connect", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void uploadData() {

        String name = productNameEt.getText().toString();
        String price = productPriceEt.getText().toString();
        String category = productCatEt.getText().toString();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        String encodedImage =  Base64.encodeToString(imageInByte,Base64.DEFAULT);

        Call<ProductModel> productModelCall = apiInterface.saveProducts(name,price,encodedImage,category);

        productModelCall.enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {

                if (response.isSuccessful() && response.body()!=null)
                {
                    ProductModel productModel = response.body();

                    if (productModel.isSuccess())
                    {
                        Toast.makeText(AddProductActivity.this, "Product Added Successfully!!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(AddProductActivity.this, ""+productModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
               // Toast.makeText(AddProductActivity.this, "Could not Connect", Toast.LENGTH_SHORT).show();
            }
        });


      }

    //permission
    private void showImagePicDialog() {

        String option[]= {"Camera","Gallery"};
        //create aletdialouge
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
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
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                ==(PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);

        return result && result1;

    }

    private  void requestCameraPermission() {

        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE);

    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);

        return result;

    }

    private  void requestStoragePermission() {

        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);

    }
    private void pickFromCamera() {
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        image_rui = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

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
                        Toast.makeText(this, "Please enable Camera & Storage permission", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, "Please enable Storage permission", Toast.LENGTH_SHORT).show();
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
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                    productImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (requestCode==IMAGE_PICK_CAMERA_CODE)
            {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                    productImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }





}