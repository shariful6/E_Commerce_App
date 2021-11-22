package com.shariful.ecommerceapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shariful.ecommerceapp.Model.CategoryModel;
import com.shariful.ecommerceapp.R;

import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.Myholder> {
    Context context;
    List<CategoryModel> categoryList;

    public AdapterCategory(Context context, List<CategoryModel> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterCategory.Myholder(LayoutInflater.from(context).inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        String name = categoryList.get(position).getCat_name();
        holder.nameTv.setText("#"+name);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView nameTv;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.categoryTv_row);
        }
    }
}

