package com.purpledot.fame.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purpledot.fame.POJO.products.productsList.Product;
import com.purpledot.fame.R;
import com.purpledot.fame.activites.productModule.ProductDetailsActivity;
import com.purpledot.fame.interfaces.AddToCartClickResponse;
import com.purpledot.fame.utils.SharedPrefManager;
import com.purpledot.fame.utils.ToastUtils;

import java.util.List;

/**
 * Created by venkatesh on 11/2/2017.
 */

public class MarketPlaceAdapter extends RecyclerView.Adapter<MarketPlaceAdapter.ViewHolder> implements Filterable {
    private Context context;
    private SharedPrefManager sharedPrefManager;
    private String TAG = MarketPlaceAdapter.class.getSimpleName();
    AddToCartClickResponse addToCartClickResponse;
    private List<Product> productList;
    private MarketPlaceAdapter.CustomFilter mFilter;
    List<Product> filteredList;

    public MarketPlaceAdapter(Context context, AddToCartClickResponse addToCartClickResponse, List<Product> productList, List<Product> filteredList) {
        this.context = context;
        this.addToCartClickResponse = addToCartClickResponse;
        this.productList = productList;
        this.filteredList = filteredList;
        mFilter = new MarketPlaceAdapter.CustomFilter(MarketPlaceAdapter.this);
    }

    @Override
    public MarketPlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_products_row, parent, false);
        return new MarketPlaceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MarketPlaceAdapter.ViewHolder holder, int position) {
        try {
            final Product product = filteredList.get(position);
            sharedPrefManager = new SharedPrefManager(context);
            if (position == 1) {
                holder.imgProduct.setImageResource(R.drawable.p4);
            }
            if (position == 3) {
                holder.imgProduct.setImageResource(R.drawable.p3);
            }
            holder.btnBuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.displayToast("Work in progress", context);
                }
            });
            holder.imgAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.displayToast("Product added into cart", context);
                    int cartCount = sharedPrefManager.getCartCount();
                    Log.d(TAG, "Before Increment Cart count--" + cartCount);
                    cartCount = cartCount + 1;
                    Log.d(TAG, "After Increment Cart count--" + cartCount);
                    sharedPrefManager.saveCartCount(context, cartCount);
                    Log.d(TAG, "From Pref Cart count--" + cartCount);
                    addToCartClickResponse.addToCartCount(cartCount);

                }
            });
            holder.imgProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "1 Product ID--" + product.getProductId() + "--Product name--" + product.getProductName());
                    Bundle b = new Bundle();
                    b.putParcelable("product", product);
                    Intent i = new Intent(context, ProductDetailsActivity.class);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
            holder.llProductsText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "2 Product ID--" + product.getProductId() + "--Product name--" + product.getProductName());
                    Bundle b = new Bundle();
                    b.putParcelable("product", product);
                    Intent i = new Intent(context, ProductDetailsActivity.class);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });

            holder.imgProductEye.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "3 Product ID--" + product.getProductId() + "--Product name--" + product.getProductName());

                    Bundle b = new Bundle();
                    b.putParcelable("product", product);
                    Intent i = new Intent(context, ProductDetailsActivity.class);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
            holder.seller.setText(product.getProductSellerName());
            holder.productName.setText(product.getProductName());
            holder.productPrice.setText(product.getProductPrice() + " Rs");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct, imgProductEye, imgAddCart;
        Button btnBuyNow;
        LinearLayout llProductsText;
        TextView productName, productPrice, seller;

        public ViewHolder(View view) {
            super(view);
            imgProduct = view.findViewById(R.id.img_product);
            imgProductEye = view.findViewById(R.id.imgProductEye);
            btnBuyNow = view.findViewById(R.id.btnStoreBuyIn);
            llProductsText = view.findViewById(R.id.ll_pd_txt);
            productName = view.findViewById(R.id.txtProductName);
            productPrice = view.findViewById(R.id.txtStoreEPrice);
            seller = view.findViewById(R.id.txtStoreSellerBy);
            imgAddCart = view.findViewById(R.id.imgProductAddCart);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + productName.getText() + "'";
        }

    }

    public class CustomFilter extends Filter {
        private MarketPlaceAdapter mAdapter;

        private CustomFilter(MarketPlaceAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredList.addAll(productList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Product mWords : productList) {
                    if (mWords.getProductName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(mWords);
                    }
                }
            }
            System.out.println("Count Number " + filteredList.size());
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            int searchCount = ((List<Product>) results.values).size();
            System.out.println("Count Number 2 " + searchCount);
            if (searchCount == 0)
                ToastUtils.displayToast("No products", context);
            else
                this.mAdapter.notifyDataSetChanged();
        }
    }
}
