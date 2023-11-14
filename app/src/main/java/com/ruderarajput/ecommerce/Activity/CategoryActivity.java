package com.ruderarajput.ecommerce.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ruderarajput.ecommerce.Adapters.ProductAdapter;
import com.ruderarajput.ecommerce.Models.Product;
import com.ruderarajput.ecommerce.Utils.Constants;
import com.ruderarajput.ecommerce.databinding.ActivityCategoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    ActivityCategoryBinding binding;
    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        products=new ArrayList<>();
        productAdapter=new ProductAdapter(this,products);

        int catId=getIntent().getIntExtra("catId",0);
        String categoryName=getIntent().getStringExtra("categoryName");
        binding.toolbatText.setText(categoryName);

        getProducts(catId);

        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        binding.recViewCateItem.setLayoutManager(layoutManager);
        binding.recViewCateItem.setAdapter(productAdapter);

    }
    void getProducts(int catId){
        RequestQueue queue= Volley.newRequestQueue(this);
        String url= Constants.GET_PRODUCTS_URL+"?category_id="+catId;

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
}