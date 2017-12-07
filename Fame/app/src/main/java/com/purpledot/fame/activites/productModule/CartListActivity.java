package com.purpledot.fame.activites.productModule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.purpledot.fame.R;
import com.purpledot.fame.adapters.CartAdapter;
import com.purpledot.fame.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class CartListActivity extends AppCompatActivity {
    RecyclerView rcvCartList;
    SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        prefManager = new SharedPrefManager(this);
        rcvCartList = findViewById(R.id.rcv_cart_list);
        setAdapter();
        /*String compactJws = Jwts.builder().claim("email",username).claim("password",password)
                .signWith(SignatureAlgorithm.HS256, "secret".getBytes())
                .compact();
*/
    }

    private void setAdapter() {
        try {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < prefManager.getCartCount(); i++)
                list.add("adcd");

            rcvCartList.setLayoutManager(new LinearLayoutManager(CartListActivity.this));
            rcvCartList.setAdapter(new CartAdapter(CartListActivity.this, prefManager.getCartCount(), list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
