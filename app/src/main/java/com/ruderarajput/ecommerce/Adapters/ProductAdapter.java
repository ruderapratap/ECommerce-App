package com.ruderarajput.ecommerce.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ruderarajput.ecommerce.Activity.ProductDetailActivity;
import com.ruderarajput.ecommerce.Models.Product;
import com.ruderarajput.ecommerce.R;
import com.ruderarajput.ecommerce.databinding.ItemCategoriesBinding;
import com.ruderarajput.ecommerce.databinding.ItemProductBinding;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>
{
    Context context;
    ArrayList<Product> products;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product=products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.demo_img)
                .into(holder.binding.image);
        holder.binding.label.setText(product.getName());
        holder.binding.price.setText(String.valueOf(product.getPrice()));
        //holder.binding.discount.setText(String.valueOf(product.getDiscount()));

        holder.itemView.setOnClickListener(c->{
            Intent intent=new Intent(context, ProductDetailActivity.class);
            intent.putExtra("name",product.getName());
            intent.putExtra("image",product.getImage());
            intent.putExtra("id",product.getId());
            intent.putExtra("price",product.getPrice());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ItemProductBinding.bind(itemView);
        }
    }
}
