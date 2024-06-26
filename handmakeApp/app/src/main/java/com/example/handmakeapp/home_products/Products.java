package com.example.handmakeapp.home_products;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.handmakeapp.R;
import com.example.handmakeapp.account.Account;
import com.example.handmakeapp.callAPI.CallAPI;
import com.example.handmakeapp.CartActivity;
import com.example.handmakeapp.detail_product.DetailActivity;

import com.example.handmakeapp.model.Category;
import com.example.handmakeapp.home_products.adapter.ProductListArrayAdapter;
import com.example.handmakeapp.home_products.mapping.ProductMapping;
import com.example.handmakeapp.model.ProductDetail;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Products extends AppCompatActivity {
    SearchView sv;
    GridView gv;
    Spinner filterProduct;
    List<Category> categories = new ArrayList<>();
    List<String> options = new ArrayList<>();
    HashMap<Integer, Integer> categoryIdMapping = new HashMap<>();//vị trí - categoryId

    List<ProductDetail> allProducts;
    List<ProductDetail> filterList;
    ProductListArrayAdapter gridViewAdapter;
    BottomNavigationView bottomNavigation;

    ImageButton btn_toCart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);
        gv = findViewById(R.id.gv);
        sv = findViewById(R.id.searchview);
        btn_toCart = findViewById(R.id.btn_toCart);
        filterProduct = findViewById(R.id.filterProductList);
        options.add("Tất cả sản phẩm");
        sv.clearFocus();
        // Execute network task to fetch data
        new NetworkTask().execute();

//        btn_toCart = findViewById(R.id.btn_toCart);

        btn_toCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Products.this, CartActivity.class);
                startActivity(intent);
            }
        });

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterText(newText);
                return true;
            }
        });
//        filter
        filterProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectOption = options.get(i);//tên danh mục
                filterList = null;
                if (i == 0) {
                    filterList = allProducts;
                    filterCategory(filterList);
                } else if (categoryIdMapping != null && i != 0) {
                    int categotyId = categoryIdMapping.get(i);
//                    >= API 24
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        filterList = allProducts.stream()
                                .filter(product -> product.getCategoryId() == categotyId)
                                .collect(Collectors.toList());
                    }
                    filterCategory(filterList);
                }
                sv.setQuery("", false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Xử lý sự kiện khi không có mục nào được chọn

            }
        });
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int idt = (filterList!=null && !filterList.isEmpty()) ? filterList.get(position).getId() : allProducts.get(position).getId();

                getProductById(idt);
            }
        });
        actionNavigationBottom();
    }

    private void filterCategory(List<ProductDetail> filterList) {
        gridViewAdapter = new ProductListArrayAdapter(Products.this, R.layout.activity_product_item, filterList);
        gv.setAdapter(gridViewAdapter);
    }

    private void filterText(String newText) {
        filterList = new ArrayList<>();
        if (allProducts != null) {
            for (ProductDetail p : allProducts) {
                if (p.getName().toLowerCase().contains(newText.toLowerCase())) {
                    filterList.add(p);
                }
            }
            if (filterList.isEmpty()) {
                Log.e("filterList 1", filterList.size()+"");
                gridViewAdapter.setFilterList(filterList);
                Toast.makeText(this, "Không có sản phẩm tương ứng", LENGTH_LONG).show();
            } else {
                Log.e("filterList 2", filterList.size()+"");
                gridViewAdapter.setFilterList(filterList);
            }
        }
    }

    private class NetworkTask extends AsyncTask<Void, Void, List<ProductDetail>> {
        @Override
        protected List<ProductDetail> doInBackground(Void... voids) {
            categories = ProductMapping.getInstance().getCategories();
            allProducts = ProductMapping.getInstance().getAllProduct();
            if (categories != null && !categories.isEmpty()) {
                for (Category c : categories) {
                    options.add(c.getName());
                    categoryIdMapping.put(options.size() - 1, c.getId());
                }
            }
            return allProducts;
        }

        @Override
        protected void onPostExecute(List<ProductDetail> products) {
            gridViewAdapter = new ProductListArrayAdapter(Products.this, R.layout.activity_product_item, products);
            gv.setAdapter(gridViewAdapter);

            ArrayAdapter<String> adapterFilter = new ArrayAdapter<>(Products.this, android.R.layout.simple_spinner_item, options);
            adapterFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            filterProduct.setAdapter(adapterFilter);
        }
    }
    public void actionNavigationBottom() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.list);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.list) {
                    return true;
                } else if (id == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (id == R.id.account) {
                    startActivity(new Intent(getApplicationContext(), Account.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (id == R.id.cart) {
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });
    }
    private void getProductById(int id) {
        CallAPI.api.getPDById("getProductDetailsById", id).enqueue(new Callback<ProductDetail>() {
            @Override
            public void onResponse(Call<ProductDetail> call, Response<ProductDetail> response) {
                Log.e("Kien", "onSuccess");
                ProductDetail p = response.body();
                if(p != null) {
                    Log.e("Kien", p.toString());

                    //Chuyển sang DetailActivity truyền ProductDetail.
                    Intent intent = new Intent(Products.this, DetailActivity.class);
                    intent.putExtra("productDetail", p);
                    startActivity(intent);

                }

                else {
                    Log.e("Kien", response.code()+ "");
                }
            }
            @Override
            public void onFailure(Call<ProductDetail> call, Throwable t) {
                Log.e("Kien", "Failure" + t.getMessage(), t);
            }
        });
    }
}