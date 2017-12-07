
package com.purpledot.fame.POJO.orders;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdersModel {

    @SerializedName("order_list")
    @Expose
    private List<OrderList> orderList = new ArrayList<>();

    public List<OrderList> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderList> orderList) {
        this.orderList = orderList;
    }

}
