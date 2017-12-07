package com.purpledot.fame.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purpledot.fame.POJO.orders.OrderList;
import com.purpledot.fame.R;

import java.util.List;

/**
 * Created by venkatesh on 10/30/2017.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    Context context;
    List<OrderList> orderListList;

    public OrdersAdapter(Context context, List<OrderList> orderListList) {
        this.context = context;
        this.orderListList = orderListList;

    }

    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_orders_row, parent, false);
        return new OrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrdersAdapter.ViewHolder holder, int position) {
        try {
            OrderList orderList = orderListList.get(position);
            if (orderList.getOrderId() != null)
                holder.orderId.setText(orderList.getOrderId());
            if (orderList.getOrderItemName() != null)
                holder.productName.setText(orderList.getOrderItemName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return orderListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        TextView orderDate;
        TextView orderStatus;
        TextView orderPrice;
        TextView orderQuantity;
        TextView productName;

        public ViewHolder(View view) {
            super(view);
            orderId = view.findViewById(R.id.order_id);
            orderDate = view.findViewById(R.id.order_Date);
            orderStatus = view.findViewById(R.id.order_status);
            orderPrice = view.findViewById(R.id.order_price);
            orderQuantity = view.findViewById(R.id.order_quantity);
            productName = view.findViewById(R.id.prodcut_name);
        }
    }

}
