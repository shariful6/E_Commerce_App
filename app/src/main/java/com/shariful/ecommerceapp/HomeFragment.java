package com.shariful.ecommerceapp;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.shariful.ecommerceapp.Adapter.AdapterCategory;
import com.shariful.ecommerceapp.Adapter.AdapterProduct;
import com.shariful.ecommerceapp.Model.CategoryModel;
import com.shariful.ecommerceapp.Model.ProductModel;
import com.shariful.ecommerceapp.Retrofit.ApiClient;
import com.shariful.ecommerceapp.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private List<ProductModel> productList;
    List<CategoryModel> categoryList ;
    RecyclerView recyclerView,recyclerView_cat;
    AdapterProduct adapterProduct;
    AdapterCategory adapterCategory;

    ApiInterface apiInterface;
    ScrollView scrollView;

    TextView catTittleIv;
    ImageView bannerIv;


    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        productList = new ArrayList<>();
        categoryList = new ArrayList<>();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        recyclerView =view.findViewById(R.id.recyclerView_id);
        recyclerView_cat =view.findViewById(R.id.recyclerViewCategory_id);
        scrollView =view.findViewById(R.id.scrollView_id);
        catTittleIv =view.findViewById(R.id.catTiitle_id);
        bannerIv =view.findViewById(R.id.bannerIv_id);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView_cat.setLayoutManager(layoutManager2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView_cat.setItemAnimator(new DefaultItemAnimator());

        loadCategory();
        loadProducts();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                   // recyclerView_cat.setVisibility(View.GONE);
                  // catTittleIv.setVisibility(View.GONE);
                  // bannerIv.setVisibility(View.GONE);
                }
            });
        }

        return view;
    }

    private void loadCategory() {

        Call<List<CategoryModel>> call = apiInterface.categoryList();

        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                categoryList = response.body();


                adapterCategory = new AdapterCategory(getActivity(),categoryList);
                recyclerView_cat.setAdapter(adapterCategory);

            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {

            }
        });

    }


    public  void loadProducts()
    {
        Call<List<ProductModel>> call = apiInterface.productList();

        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {

                productList=response.body();
               // Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                adapterProduct = new AdapterProduct(getActivity(),productList);
                recyclerView.setAdapter(adapterProduct);
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {

                Toast.makeText(getActivity(), "Could Not Connect !!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}