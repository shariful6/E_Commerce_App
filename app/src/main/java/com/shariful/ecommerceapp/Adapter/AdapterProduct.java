package com.shariful.ecommerceapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shariful.ecommerceapp.Model.ProductModel;
import com.shariful.ecommerceapp.ProductDetailsActivity;
import com.shariful.ecommerceapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.MyViewHolder>  {
    Context context;
    List<ProductModel> productModelsList;

    public AdapterProduct(Context context, List<ProductModel> productModelsList) {
        this.context = context;
        this.productModelsList = productModelsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_products, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String  name = productModelsList.get(position).getName();
        String  price = productModelsList.get(position).getPrice();
        String  imgUrl = productModelsList.get(position).getLocation();
        holder.nameTv.setText(name);
        holder.priceTv.setText("ট "+price);

        holder.productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdapterProduct.this.context, ProductDetailsActivity.class);
                intent.putExtra("url",imgUrl);
                intent.putExtra("name",name);
                intent.putExtra("price",price);
                AdapterProduct.this.context.startActivity(intent);
            }
        });

        try{
            Picasso.get().load("https://sssoftwarehub.xyz/ECommerceApp/"+imgUrl).placeholder(R.drawable.problem_24).into(holder.productImageView);
        }catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return productModelsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        //variable here
        TextView nameTv,priceTv;
        ImageView productImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //find here
            nameTv = itemView.findViewById(R.id.productNameTv_id);
            priceTv = itemView.findViewById(R.id.productPriceTv_id);
            productImageView = itemView.findViewById(R.id.productImageIv_id);



        }
    }
}
