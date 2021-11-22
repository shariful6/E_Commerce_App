package com.shariful.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shariful.ecommerceapp.Model.CategoryModel;
import com.shariful.ecommerceapp.Model.ProductModel;
import com.shariful.ecommerceapp.Retrofit.ApiClient;
import com.shariful.ecommerceapp.Retrofit.ApiInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView nameTv, priceTv,buyNowTv,addToCardTv;

    ApiInterface apiInterface;

    String phone;
    String url;
    String name;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        imageView = findViewById(R.id.imageview_details_id);
        nameTv = findViewById(R.id.productDetailsTv_id);
        priceTv = findViewById(R.id.productPriceTv_id);
        buyNowTv = findViewById(R.id.buyNowTv_id);
        addToCardTv = findViewById(R.id.addToCardTv_id);

        Intent intent = getIntent();
         url =intent.getStringExtra("url");
         name =intent.getStringExtra("name");
         price =intent.getStringExtra("price");

        nameTv.setText(name);
        priceTv.setText("ট "+price);
        loadUserInfo();

        try {
            Picasso.get().load("https://sssoftwarehub.xyz/ECommerceApp/"+url).placeholder(R.drawable.problem_24).into(imageView);
        }catch (Exception e){

        }

        buyNowTv.setOnClickListener(v->{

        });
        addToCardTv.setOnClickListener(v->{
            addToCard();
        });

    }

    private void addToCard() {
        Call<ProductModel> productModelCall = apiInterface.saveToCart(phone,name,price,url);
        productModelCall.enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {

                if (response.isSuccessful() && response.body()!=null)
                {
                    ProductModel productModel = response.body();

                    if (productModel.isSuccess())
                    {
                        Toast.makeText(getApplicationContext(), "Product Added to Cart!!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), ""+productModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Could not Connect", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("info", Context.MODE_PRIVATE);
        phone = sharedPreferences.getString("phonekey", "No phone Found !");
    }

}