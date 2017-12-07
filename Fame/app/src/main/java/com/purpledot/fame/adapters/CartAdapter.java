package com.purpledot.fame.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.purpledot.fame.POJO.orders.OrderList;
import com.purpledot.fame.R;
import com.purpledot.fame.activites.DashBoardActivity;
import com.purpledot.fame.activites.loginModule.LoginActivity;
import com.purpledot.fame.utils.SharedPrefManager;

import java.util.List;

/**
 * Created by venkatesh on 11/3/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private SharedPrefManager sharedPrefManager;
    private String TAG = MarketPlaceAdapter.class.getSimpleName();
    int count;
    List<String> orderListList;

    public CartAdapter(Context context, int count, List<String> list) {
        this.context = context;
        this.count = count;
        this.orderListList = list;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_cart_products_row, parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, final int position) {
        try {
            sharedPrefManager = new SharedPrefManager(context);

            holder.btnRemoveProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupToRemoveItem(holder.getAdapterPosition());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return orderListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct, imgProductEye;
        Button btnRemoveProduct;
        LinearLayout llProductsText;

        public ViewHolder(View view) {
            super(view);
            imgProduct = view.findViewById(R.id.img_product);
            imgProductEye = view.findViewById(R.id.imgProductEye);
            btnRemoveProduct = view.findViewById(R.id.btnRemoveProduct);
            llProductsText = view.findViewById(R.id.ll_pd_txt);

        }
    }

    private void popupToRemoveItem(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Remove item");
        alertDialog.setMessage("Are you sure you want to remove this item?");
        alertDialog.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (orderListList.size() > 0)
                    deleteItemFromRecyclerView(position);
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog1 = alertDialog.create();

        if (alertDialog1.getWindow() != null && alertDialog1.getWindow().getAttributes() != null)
            alertDialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;

        alertDialog1.show();
    }

    private void deleteItemFromRecyclerView(int position) {
        try {
            Log.d(TAG, "Before Remove list size--" + orderListList.size());
            // Remove the item on remove/button click
            orderListList.remove(position);
            notifyItemRemoved(position);
            Log.d(TAG, "After Notify Remove list size--" + orderListList.size());
            int originalCount = sharedPrefManager.getCartCount();
            int decCount = originalCount - 1;
            Log.d(TAG, "originalCount--" + originalCount + "--decCount--" + decCount);
            sharedPrefManager.saveCartCount(context, decCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
