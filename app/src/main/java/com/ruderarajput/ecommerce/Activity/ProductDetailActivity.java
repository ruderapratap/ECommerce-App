package com.ruderarajput.ecommerce.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;
import com.ruderarajput.ecommerce.Adapters.ProductAdapter;
import com.ruderarajput.ecommerce.Models.Product;
import com.ruderarajput.ecommerce.R;
import com.ruderarajput.ecommerce.Utils.Constants;
import com.ruderarajput.ecommerce.databinding.ActivityProductDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {
    private ActivityProductDetailBinding binding;

    Product currentProduct;
    ArrayList<Product> products;

    ProductAdapter productAdapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initProducts();


        String name=getIntent().getStringExtra("name");
        String image=getIntent().getStringExtra("image");
        int id=getIntent().getIntExtra("id",0);
        double price=getIntent().getDoubleExtra("price",0);

        getProductDetails(id);

        Glide.with(this)
                .load(image)
                .placeholder(R.drawable.demo_img)
                .into(binding.productImage);

        binding.toolbatText.setText(name);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProductDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        Cart cart= TinyCartHelper.getCart();

        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addItem(currentProduct,1);
               binding.addToCartBtn.setEnabled(false);
               binding.addToCartBtn.setText("Added to cart");
            }
        });

        getSupportActionBar().hide();
    }

    void initProducts(){
        products=new ArrayList<>();
        productAdapter=new ProductAdapter(this,products);

        getRecentProducts();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        //GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        binding.recentRecView.setLayoutManager(linearLayoutManager);
        binding.recentRecView.setAdapter(productAdapter);
    }

    void getRecentProducts(){
        RequestQueue queue=Volley.newRequestQueue(this);
        String url=Constants.GET_PRODUCTS_URL+"?count=8";

        StringRequest request=new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object=new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray productArray=object.getJSONArray("products");
                    for (int i=0;i<productArray.length();i++){
                        JSONObject childObj=productArray.getJSONObject(i);
                        Product product=new Product(
                                childObj.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL+childObj.getString("image"),
                                childObj.getString("status"),
                                childObj.getDouble("price"),
                                childObj.getDouble("price_discount"),
                                childObj.getInt("stock"),
                                childObj.getInt("id")
                        );
                        products.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> {

        });
        queue.add(request);
    }

    void getProductDetails(int id){
        RequestQueue queue= Volley.newRequestQueue(this);
        String url=Constants.GET_PRODUCT_DETAILS_URL+id;
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.getString("status").equals("success")){
                        JSONObject product=object.getJSONObject("product");
                        String description=product.getString("description");
                        binding.productDescription.setText(
                                Html.fromHtml(description)
                        );
                        currentProduct=new Product(
                                product.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL+product.getString("image"),
                                product.getString("status"),
                                product.getDouble("price"),
                                product.getDouble("price_discount"),
                                product.getInt("stock"),
                                product.getInt("id")
                        );
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}