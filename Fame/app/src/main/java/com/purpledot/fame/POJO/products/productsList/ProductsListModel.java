
package com.purpledot.fame.POJO.products.productsList;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductsListModel implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("products")
    @Expose
    private List<Product> products = new ArrayList<>();

    protected ProductsListModel(Parcel in) {
        status = in.readString();
        message = in.readString();
        products = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Creator<ProductsListModel> CREATOR = new Creator<ProductsListModel>() {
        @Override
        public ProductsListModel createFromParcel(Parcel in) {
            return new ProductsListModel(in);
        }

        @Override
        public ProductsListModel[] newArray(int size) {
            return new ProductsListModel[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(message);
        dest.writeTypedList(products);
    }
}
