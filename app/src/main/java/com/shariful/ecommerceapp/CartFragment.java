package com.shariful.ecommerceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shariful.ecommerceapp.Adapter.AdapterCart;
import com.shariful.ecommerceapp.Adapter.AdapterCategory;
import com.shariful.ecommerceapp.Model.CategoryModel;
import com.shariful.ecommerceapp.Model.ProductModel;
import com.shariful.ecommerceapp.Retrofit.ApiClient;
import com.shariful.ecommerceapp.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {
    ApiInterface apiInterface;

    private RecyclerView recyclerView;
    List<ProductModel> productList;
    AdapterCart adapterCart;

    String phone;


    public CartFragment() {
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
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        loadUserInfo();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        productList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerViewCart_id);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        loadCartProducts();
        return view;
    }

    private void loadCartProducts() {

        Call<List<ProductModel>> call = apiInterface.loadCartProducts(phone);

        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                productList = response.body();

               // Toast.makeText(getActivity(), ""+productList.size(), Toast.LENGTH_SHORT).show();
                adapterCart = new AdapterCart(getActivity(),productList);
                recyclerView.setAdapter(adapterCart);

            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {

            }
        });
    }
    private void loadUserInfo() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("info", Context.MODE_PRIVATE);
        phone = sharedPreferences.getString("phonekey", "No phone Found !");
    }

}