package com.purpledot.fame.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purpledot.fame.POJO.orders.OrderList;
import com.purpledot.fame.R;
import com.purpledot.fame.activites.productModule.ProductDetailsActivity;

import java.util.List;

/**
 * Created by venkatesh on 10/31/2017.
 */

public class NewArrivalesAdapter extends RecyclerView.Adapter<NewArrivalesAdapter.ViewHolder> {
    Context context;

    public NewArrivalesAdapter(Context context) {
        this.context = context;

    }

    @Override
    public NewArrivalesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_new_arravilas_row, parent, false);
        return new NewArrivalesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewArrivalesAdapter.ViewHolder holder, int position) {
        try {
            if(position==1){
                holder.imgProduct.setImageResource(R.drawable.p4);
            }
            if(position==2){
                holder.imgProduct.setImageResource(R.drawable.p3);
            }

            holder.llNewArrivals.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ProductDetailsActivity.class);
                    context.startActivity(i);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        /* TextView orderId
         TextView orderDate;
         TextView orderStatus;
         TextView orderPrice;
         TextView orderQuantity;
         TextView productName;*/
        ImageView imgProduct;
        LinearLayout llNewArrivals;

        public ViewHolder(View view) {
            super(view);
           /* orderId = view.findViewById(R.id.order_id);
            orderDate = view.findViewById(R.id.order_Date);
            orderStatus = view.findViewById(R.id.order_status);
            orderPrice = view.findViewById(R.id.order_price);
            orderQuantity = view.findViewById(R.id.order_quantity);
            productName = view.findViewById(R.id.prodcut_name);*/
           imgProduct = view.findViewById(R.id.img_product);
           llNewArrivals = view.findViewById(R.id.ll_new_arrivals);

        }
    }
}
