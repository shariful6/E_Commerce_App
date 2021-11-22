package com.shariful.ecommerceapp.Retrofit;

import com.shariful.ecommerceapp.Model.CategoryModel;
import com.shariful.ecommerceapp.Model.ImageModel;
import com.shariful.ecommerceapp.Model.ProductModel;
import com.shariful.ecommerceapp.Model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface
{
    //For Login
    @FormUrlEncoded
    @POST("loginuser.php")
    Call<UserModel> loginUser(@Field("email") String email, @Field("password") String password);
     //For register
    @FormUrlEncoded
    @POST("registeruser.php")
    Call<UserModel> registerUser(@Field("name") String name, @Field("email") String email,
                                 @Field("phone") String phone, @Field("password") String password,@Field("profile_pic") String profile_pic);

    @FormUrlEncoded
    @POST("saveproducts.php")
    Call<ProductModel> saveProducts(@Field("name") String name, @Field("price") String price,
                                    @Field("EN_IMAGE") String encodedImage,@Field("category") String category);


    //for save categor
    @FormUrlEncoded
    @POST("savecategory.php")
    Call<CategoryModel> saveCategory(@Field("cat_name") String cat_name, @Field("total_product") String total_product);


    //for retriving all products
    @GET("retriveproduct.php")
    Call<List<ProductModel>> productList();

    //for retriving all products
    @GET("retrivecategory.php")
    Call<List<CategoryModel>> categoryList();

    @FormUrlEncoded
    @POST("load_profile_image.php")
    Call<ImageModel> loadPicture(@Field("email") String email);//////////////////////

    @FormUrlEncoded
    @POST("save_profile_pic.php")
    Call<ImageModel> saveImage(@Field("phone") String phone, @Field("profile_pic") String encodedImage);

    @FormUrlEncoded
    @POST("savetocart.php")
    Call<ProductModel> saveToCart(@Field("phone") String phone, @Field("name") String name, @Field("price") String price, @Field("image") String image);


    //for retriving all products
    @FormUrlEncoded
    @POST("loadcartproducts.php")
    Call<List<ProductModel>> loadCartProducts(@Field("phone") String phone);

       /*
    @FormUrlEncoded
    @POST("retrivepoints.php")
    Call<UserModel> retrivePoints(@Field("email") String email);  */

    /*
       @FormUrlEncoded
    @POST("createpoints.php")
    Call<BalanceModel> save(@Field("email") String email, @Field("points") String points);

    //for retriving all redemList
    @GET("retriveredemlist.php")
    Call<List<RedemModel>> redeemList();


    //for retriving text
    @GET("retrivetext.php")
    Call<MessageModel> retriveText();

    //for updating points
    @FormUrlEncoded
    @POST("savepoints.php")
    Call<BalanceModel> savePoints(@Field("points") String points, @Field("phone") String phone);

    @FormUrlEncoded
    @POST("loadimage.php")
    Call<ImageModel> loadPicture(@Field("phone") String phone);//////////////////////


    @FormUrlEncoded
    @POST("redemrequest.php")
    Call<RedemModel> redem(@Field("name") String name, @Field("phone") String phone, @Field("method") String method,
                           @Field("amount") String amount, @Field("acount") String acount, @Field("status") String status);

    @FormUrlEncoded
    @POST("saveimage.php")
    Call<ImageModel> updateImage(@Field("phone") String phone, @Field("EN_IMAGE") String encodedImage);
     */


}
