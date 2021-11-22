package com.shariful.ecommerceapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shariful.ecommerceapp.Model.ProductModel;
import com.shariful.ecommerceapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.Myholder> {
    Context context;
    List<ProductModel> productList;

    public AdapterCart(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterCart.Myholder(LayoutInflater.from(context).inflate(R.layout.cart_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        String name = productList.get(position).getName();
        String price = productList.get(position).getPrice();
        String url = productList.get(position).getLocation();
        holder.nameTv.setText(name);
        holder.priceTv.setText("ট "+price);

        try{
            Picasso.get().load("https://sssoftwarehub.xyz/ECommerceApp/"+url).placeholder(R.drawable.problem_24).into(holder.imageView);
        }catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTv,priceTv;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productIv_row);
            nameTv = itemView.findViewById(R.id.nameTv_row);
            priceTv = itemView.findViewById(R.id.priceTv_row);

        }
    }
}
