package com.ruderarajput.ecommerce.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;
import com.ruderarajput.ecommerce.Models.Product;
import com.ruderarajput.ecommerce.R;
import com.ruderarajput.ecommerce.databinding.CartItemBinding;
import com.ruderarajput.ecommerce.databinding.QuantityDialogBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>
{
    Context context;
    ArrayList<Product> products;
    CartListner cartListner;
    Cart cart;
    public interface CartListner{
        public void onQuantityChanged();
    }
    public CartAdapter(Context context,ArrayList<Product> products,CartListner cartListner){
        this.context=context;
        this.products=products;
        this.cartListner=cartListner;
        cart= TinyCartHelper.getCart();
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product=products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.demo_img)
                .into(holder.binding.cartImage);

        holder.binding.cartName.setText(product.getName());
        holder.binding.cartPrice.setText(""+product.getPrice());
        holder.binding.cartQuantity.setText(""+product.getQuantity());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuantityDialogBinding quantityDialogBinding=QuantityDialogBinding.inflate(LayoutInflater.from(context));
                AlertDialog dialog=new AlertDialog.Builder(context)
                        .setView(quantityDialogBinding.getRoot())
                        .create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

                quantityDialogBinding.productName.setText(product.getName());
                quantityDialogBinding.productStock.setText("Stock: "+ product.getStock());
                quantityDialogBinding.quantity.setText(String.valueOf(product.getQuantity()));

                int stock=product.getStock();

                quantityDialogBinding.plusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity=product.getQuantity();
                        quantity++;
                        if(quantity>product.getStock()){
                            Toast.makeText(context, "Max Stock Available:"+product.getStock(), Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            product.setQuantity(quantity);
                            quantityDialogBinding.quantity.setText(String.valueOf(quantity));
                        }
                        notifyDataSetChanged();
                        cart.updateItem(product,product.getQuantity());
                        cartListner.onQuantityChanged();
                    }
                });
                quantityDialogBinding.minusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity=product.getQuantity();
                        if (quantity>1){
                            quantity--;
                            product.setQuantity(quantity);
                            quantityDialogBinding.quantity.setText(String.valueOf(quantity));

                            notifyDataSetChanged();
                            cart.updateItem(product,product.getQuantity());
                            cartListner.onQuantityChanged();
                        }
                    }
                });
                quantityDialogBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                   /*     notifyDataSetChanged();
                        cart.updateItem(product,product.getQuantity());
                        cartListner.onQuantityChanged();*/

                    }
                });
                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        CartItemBinding binding;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CartItemBinding.bind(itemView);
        }
    }
}
