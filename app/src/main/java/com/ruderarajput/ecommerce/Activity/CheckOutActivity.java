package com.ruderarajput.ecommerce.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;
import com.ruderarajput.ecommerce.Adapters.CartAdapter;
import com.ruderarajput.ecommerce.Models.Product;
import com.ruderarajput.ecommerce.R;
import com.ruderarajput.ecommerce.Utils.Constants;
import com.ruderarajput.ecommerce.databinding.ActivityCheckOutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {

    ActivityCheckOutBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;
    double totalPrice=0;
    final int tax=0;
    Cart cart;
    ProgressDialog Dialog;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCheckOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Dialog=new ProgressDialog(this);
        Dialog.setCancelable(false);
       Dialog.setMessage("Processing...");


        getSupportActionBar().hide();

        binding.etShipingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CheckOutActivity.this,ProductDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        products=new ArrayList<>();

        cart= TinyCartHelper.getCart();

        for (Map.Entry<Item,Integer> item:cart.getAllItemsWithQty().entrySet()){
            Product product=(Product) item.getKey();
            int quantity=item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }

        adapter=new CartAdapter(this, products, new CartAdapter.CartListner() {
            @Override
            public void onQuantityChanged() {
                binding.productPrice.setText(String.format("IND %.2f",cart.getTotalPrice()));
            }
        });


        binding.recViewCheckout.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewCheckout.setAdapter(adapter);

        binding.productPrice.setText(String.format("IND %.2f",cart.getTotalPrice()));

        totalPrice=(cart.getTotalPrice().doubleValue()*tax/100)+cart.getTotalPrice().doubleValue();
        binding.totalPriceProduct.setText(""+ totalPrice);

        binding.btnProcessCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=binding.etFullName.getText().toString();
                String email=binding.etEmail.getText().toString();
                String number=binding.etPhoneNumber.getText().toString();
                String address=binding.etAddress.getText().toString();
                String date=binding.etShipingDate.getText().toString();
                String comments=binding.etComments.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(CheckOutActivity.this, "Please Enter Your Full Name", Toast.LENGTH_SHORT).show();
                    binding.etFullName.requestFocus();
                }else if (email.isEmpty()){
                    Toast.makeText(CheckOutActivity.this, "Please Enter Your Email Address", Toast.LENGTH_SHORT).show();
                    binding.etEmail.requestFocus();
                }else if (number.isEmpty()){
                    Toast.makeText(CheckOutActivity.this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                    binding.etPhoneNumber.requestFocus();
                }else if (address.isEmpty()){
                    Toast.makeText(CheckOutActivity.this, "Please Enter Your Full Address", Toast.LENGTH_SHORT).show();
                    binding.etAddress.requestFocus();
                }else if (date.isEmpty()){
                    Toast.makeText(CheckOutActivity.this, "Please Enter Your Current Date", Toast.LENGTH_SHORT).show();
                    binding.etShipingDate.requestFocus();
                }else if (comments.isEmpty()){
                    Toast.makeText(CheckOutActivity.this, "Please Enter Comments", Toast.LENGTH_SHORT).show();
                    binding.etComments.requestFocus();
                }else {
                    processOrder();
                }
            }
        });

    }

    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Handle the selected date
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        binding.etShipingDate.setText(selectedDate);
                    }
                },
                year,
                month,
                day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    void processOrder() {
        Dialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject productOrder = new JSONObject();
        JSONObject dataObject = new JSONObject();
        try {
            productOrder.put("address", binding.etAddress.getText().toString());
            productOrder.put("buyer", binding.etFullName.getText().toString());
            productOrder.put("comment", binding.etComments.getText().toString());
            productOrder.put("created_at", Calendar.getInstance().getTimeInMillis());
            productOrder.put("date_ship", Calendar.getInstance().getTimeInMillis());
            productOrder.put("updated_at", Calendar.getInstance().getTimeInMillis());
            productOrder.put("email", binding.etEmail.getText().toString());
            productOrder.put("phone", binding.etPhoneNumber.getText().toString());
            productOrder.put("serial", "123456");
            productOrder.put("shipping", "");
            productOrder.put("shipping_location", "");
            productOrder.put("shipping_rate", "20");
            productOrder.put("status", "");
            productOrder.put("tax", tax);
            productOrder.put("total_fees", totalPrice);

            JSONArray product_order_detail = new JSONArray();
            for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
                Product product = (Product) item.getKey();
                int quantity = item.getValue();
                product.setQuantity(quantity);

                products.add(product);

                JSONObject productObj = new JSONObject();

                productObj.put("amount", quantity);
                productObj.put("product_id", product.getId());
                productObj.put("price_item", product.getPrice());
                productObj.put("product_name", product.getName());
                product_order_detail.put(productObj);
            }
            dataObject.put("product_order", productOrder);
            dataObject.put("product_order_detail", product_order_detail);


        } catch (JSONException e) {

        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_URL, dataObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("failed")) {
                        Toast.makeText(CheckOutActivity.this, "Succesfully Order Your Favroute Product", Toast.LENGTH_SHORT).show();
                        String orderNum = response.getJSONObject("data").getString("code");
                        new AlertDialog.Builder(CheckOutActivity.this)
                                .setIcon(R.drawable.order_succes)
                                .setTitle("Order Succesfully")
                                .setCancelable(false)
                                .setMessage("your order number is : " + orderNum)
                                .setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(CheckOutActivity.this, PaymentActivity.class);
                                        intent.putExtra("orderCode", orderNum);
                                        startActivity(intent);
                                    }
                                }).show();
                    } else {
                        new AlertDialog.Builder(CheckOutActivity.this)
                                .setTitle("Order Failed")
                                .setCancelable(false)
                                .setMessage("something went wrong, please try again.")
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                        Toast.makeText(CheckOutActivity.this, "Failed Order. Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("res", response.toString());
                    Dialog.dismiss();
                } catch (Exception e) {
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Security", "secure_code");
                return headers;
            }
        };
        queue.add(request);
    }
}