package com.purpledot.fame.fragments.dashboardFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;

import com.purpledot.fame.POJO.products.productsList.Product;
import com.purpledot.fame.R;
import com.purpledot.fame.adapters.MarketPlaceAdapter;
import com.purpledot.fame.interfaces.AddToCartClickResponse;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MarketPlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarketPlaceFragment extends Fragment implements AddToCartClickResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rcvMarketPlace;
    TextView tvCartCount;
    List<Product> filteredList;
    EditText edtSearchProductsList;
    MarketPlaceAdapter marketPlaceAdapter;

    public MarketPlaceFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public MarketPlaceFragment(TextView tvCartCount) {
        this.tvCartCount = tvCartCount;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MarketPlaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MarketPlaceFragment newInstance(String param1, String param2) {
        MarketPlaceFragment fragment = new MarketPlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_market_place, container, false);
        rcvMarketPlace = view.findViewById(R.id.rcv_market_place_products);
        edtSearchProductsList = view.findViewById(R.id.edt_search);
        edtSearchProductsList.setCursorVisible(false);
        edtSearchProductsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearchProductsList.setCursorVisible(true);
            }
        });
        edtSearchProductsList.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                marketPlaceAdapter.getFilter().filter(arg0.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });
        setData();
        return view;
    }

    private void setData() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("1", "Chudidar", "599.00", "red,blue", "Venkatesh"));
        productList.add(new Product("2", "Long Tops", "659.00", "green,blue", "Shivraj"));
        productList.add(new Product("3", "Long Chudidar", "799.00", "yellow,blue", "Anoop"));
        productList.add(new Product("4", "Short Chudidar", "709.00", "white,red,blue", "PurpleDot"));
        productList.add(new Product("7", "Chudidar 1", "999.00", "red,blue,green", "Sushil"));
        productList.add(new Product("5", "Short Tops", "199.00", "blue", "Vinita"));
        productList.add(new Product("6", "Chudidar 5", "399.00", "red", "venky"));

        productList.add(new Product("8", "Chudidar", "599.00", "red,blue", "Venkatesh"));
        productList.add(new Product("9", "Long Tops", "659.00", "green,blue", "Shivraj"));
        productList.add(new Product("10", "Long Chudidar", "799.00", "yellow,blue", "Anoop"));
        productList.add(new Product("11", "Short Chudidar", "709.00", "white,red,blue", "PurpleDot"));
        productList.add(new Product("12", "Chudidar 1", "999.00", "red,blue,green", "Sushil"));
        productList.add(new Product("13", "Short Tops", "199.00", "blue", "Vinita"));
        productList.add(new Product("32", "Chudidar 5", "399.00", "red", "venky"));

        productList.add(new Product("14", "Chudidar", "599.00", "red,blue", "Venkatesh"));
        productList.add(new Product("232", "Long Tops", "659.00", "green,blue", "Shivraj"));
        productList.add(new Product("42", "Long Chudidar", "799.00", "yellow,blue", "Anoop"));
        productList.add(new Product("45", "Short Chudidar", "709.00", "white,red,blue", "PurpleDot"));
        productList.add(new Product("75", "Chudidar 1", "999.00", "red,blue,green", "Sushil"));
        productList.add(new Product("52", "Short Tops", "199.00", "blue", "Vinita"));
        productList.add(new Product("623", "Chudidar 5", "399.00", "red", "venky"));
        setAdapter(productList);
    }

    private void setAdapter(List<Product> productList) {
        try {
            filteredList = new ArrayList<>();
            filteredList.addAll(productList);
            rcvMarketPlace.setLayoutManager(new LinearLayoutManager(getActivity()));
            marketPlaceAdapter = new MarketPlaceAdapter(getActivity(), this, productList, filteredList);
         //   rcvMarketPlace.setAnimation((Animation) getResources().getAnimation(R.anim.fade_in));
            rcvMarketPlace.setAdapter(marketPlaceAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addToCartCount(int count) {
        Log.d("FRAG", "Fragment count--" + count);
        showCartCount(count);
    }


    private void showCartCount(int cartCount) {
        try {
            if (cartCount > 0) {
                Log.d(TAG, "if Cart count from pref--" + cartCount);
                tvCartCount.setVisibility(View.VISIBLE);
                tvCartCount.setText(String.valueOf(cartCount));
            } else {
                Log.d(TAG, "Else Cart count from pref--" + cartCount);
                tvCartCount.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
