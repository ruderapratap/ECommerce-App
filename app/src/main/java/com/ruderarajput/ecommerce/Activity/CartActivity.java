package com.ruderarajput.ecommerce.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;
import com.ruderarajput.ecommerce.Adapters.CartAdapter;
import com.ruderarajput.ecommerce.Models.Product;
import com.ruderarajput.ecommerce.R;
import com.ruderarajput.ecommerce.databinding.ActivityCartBinding;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        products=new ArrayList<>();

        Cart cart= TinyCartHelper.getCart();

        for (Map.Entry<Item,Integer> item:cart.getAllItemsWithQty().entrySet()){
            Product product=(Product) item.getKey();
            int quantity=item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }

        adapter=new CartAdapter(this, products, new CartAdapter.CartListner() {
            @Override
            public void onQuantityChanged() {
                binding.cartPrice.setText(String.format("IND %.2f",cart.getTotalPrice()));
            }
        });



        binding.recViewCart.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewCart.setAdapter(adapter);

        binding.cartPrice.setText(String.format("IND %.2f",cart.getTotalPrice()));

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CartActivity.this,CheckOutActivity.class);
                startActivity(intent);
            }
        });


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CartActivity.this,ProductDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });



    }
}