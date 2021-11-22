package com.shariful.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shariful.ecommerceapp.Model.ImageModel;
import com.shariful.ecommerceapp.Model.UserModel;
import com.shariful.ecommerceapp.Retrofit.ApiClient;
import com.shariful.ecommerceapp.Retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginSignUpActivity extends AppCompatActivity {
    TextView goSignupTV,goLoginTv;
    LinearLayout loginLayout,signupLayout;
    private Button loginBtn,signUpBtn;
    private EditText emailEt_l,passwordEt_l,emailEt_s,passwordEt_s,nameEt,phoneEt;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        goSignupTV = findViewById(R.id.goSignUpTv_id);
        goLoginTv = findViewById(R.id.goLoginTv);

        loginLayout = findViewById(R.id.layoutLogin_id);
        signupLayout = findViewById(R.id.layoutSignup_id);
        loginBtn = findViewById(R.id.loginBtn_id);
        signUpBtn = findViewById(R.id.registerBtn_id);

        emailEt_l = findViewById(R.id.emailET_login);
        passwordEt_l = findViewById(R.id.passwordET_login);

        emailEt_s = findViewById(R.id.emailET_id);
        passwordEt_s = findViewById(R.id.passwordET_id);
        nameEt = findViewById(R.id.nameEt_id);
        phoneEt = findViewById(R.id.phoneET_id);


        goLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
            }
        });
        goSignupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginLayout.setVisibility(View.GONE);
                signupLayout.setVisibility(View.VISIBLE);
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });



    }
    public  void loginUser() {

        Call<UserModel> userModelCall = apiInterface.loginUser(emailEt_l.getText().toString(),passwordEt_l.getText().toString());

        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                String email=emailEt_l.getText().toString();
                String pass= passwordEt_l.getText().toString();

                if (email.isEmpty()||pass.isEmpty())
                {

                    Toast.makeText(LoginSignUpActivity.this, "Enter Your Email & Password ", Toast.LENGTH_SHORT).show();
                }

                else if (response.body()!=null)
                {
                    UserModel userModel =response.body();
                    if (userModel.isSuccess())
                    {
                        Toast.makeText(LoginSignUpActivity.this, "Login Successful ", Toast.LENGTH_SHORT).show();

                        String uName = userModel.getName();
                        String uEmail = userModel.getEmail();
                        String uPhone = userModel.getPhone();
                        //writing Shared prefrerence data
                        SharedPreferences sharedPreferences = getSharedPreferences("info", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("namekey",uName);
                        editor.putString("emailkey",uEmail);
                        editor.putString("phonekey",uPhone);
                        editor.commit();

                        Intent intent = new Intent(LoginSignUpActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginSignUpActivity.this, "User Not Found !"+userModel.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(LoginSignUpActivity.this, "Error  !! Could not Connect !!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public  void registerUser() {

        String name = nameEt.getText().toString();
        String email = emailEt_s.getText().toString();
        String phone = phoneEt.getText().toString();
        String password = passwordEt_s.getText().toString();

        Call<UserModel> userModelCall = apiInterface.registerUser(name,email,phone,password,"no pic");

        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                if (response.isSuccessful()&& response.body()!=null)
                {
                    UserModel userModel = response.body();
                    if (userModel.isSuccess())
                    {
                        Toast.makeText(LoginSignUpActivity.this, "User Successfully Registered !!", Toast.LENGTH_SHORT).show();
                     // createImageField();
                        SharedPreferences sharedPreferences = getSharedPreferences("info", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("namekey",name);
                        editor.putString("emailkey",email);
                        editor.putString("phonekey",phone);
                        editor.commit();

                        Intent intent = new Intent(LoginSignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        String message = response.message();
                        Toast.makeText(LoginSignUpActivity.this, "Response: "+message, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

                Toast.makeText(LoginSignUpActivity.this, "Could Not Connect !!", Toast.LENGTH_SHORT).show();
            }
        });

    }


/*
    public void   createImageField() {

        String phone = phoneEt.getText().toString();
        Call<ImageModel> imageModelCall = apiInterface.createImage(phone,"0");

        imageModelCall.enqueue(new Callback<ImageModel>() {
            @Override
            public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {

                if (response.isSuccessful() && response.body()!=null)
                {
                    ImageModel imageModel = response.body();

                    if (imageModel.isSuccess())
                    {
                        // Toast.makeText(RegisterActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(LoginSignUpActivity.this, "Failed !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ImageModel> call, Throwable t) {
                Toast.makeText(LoginSignUpActivity.this, "Could not Connect", Toast.LENGTH_SHORT).show();
            }
        });

    }
*/

}