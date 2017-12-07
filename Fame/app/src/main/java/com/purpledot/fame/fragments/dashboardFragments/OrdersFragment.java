package com.purpledot.fame.fragments.dashboardFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.purpledot.fame.APIS.APIRequest;
import com.purpledot.fame.APIS.RequestCallBack;
import com.purpledot.fame.APIS.RetrofitAPI.ApiClient;
import com.purpledot.fame.APIS.RetrofitAPI.APIInterfaces;
import com.purpledot.fame.POJO.orders.OrderList;
import com.purpledot.fame.POJO.orders.OrdersModel;
import com.purpledot.fame.R;
import com.purpledot.fame.adapters.OrdersAdapter;
import com.purpledot.fame.utils.ApiConstants;
import com.purpledot.fame.utils.Constants;
import com.purpledot.fame.utils.ToastUtils;
import com.purpledot.fame.utils.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class OrdersFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = OrdersFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrdersFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        progressBar = view.findViewById(R.id.pb_hr);

        // Set the adapter
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.rcv_orders);
        if (Utility.isConnectingToInternet(getActivity()))
            callOrdersApi();
        else
            ToastUtils.displayToast(Constants.no_internet_connection, getActivity());

        // callRetrofitOrderApi();
        return view;
    }


    private void setOrdersAdapter(List<OrderList> orderListList) {
        try {
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
            }
            recyclerView.setAdapter(new OrdersAdapter(getActivity(), orderListList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callOrdersApi() {
        try {
            new APIRequest(getActivity()).get(ApiConstants.ORDERS_API, null, new RequestCallBack() {
                @Override
                public void onResponse(String jsonObject) {
                    Log.d(TAG, "Success message--" + jsonObject);
                    Gson gson = new Gson();
                    OrdersModel ordersModel = gson.fromJson(jsonObject, OrdersModel.class);
                    Log.d(TAG, "Order Model SIZE--" + ordersModel.getOrderList().size());
                    if (ordersModel != null && ordersModel.getOrderList() != null && ordersModel.getOrderList().size() > 0) {
                        setOrdersAdapter(ordersModel.getOrderList());
                    }
                }

                @Override
                public void onFailed(String message) {
                    Log.d(TAG, "Error message--" + message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callRetrofitOrderApi() {
        progressBar.setVisibility(View.VISIBLE);
        APIInterfaces orderApiInterFace = ApiClient.getClient().create(APIInterfaces.class);
        Call<OrdersModel> ordersModelCall = orderApiInterFace.listOrder();
        ordersModelCall.enqueue(new Callback<OrdersModel>() {
            @Override
            public void onResponse(Call<OrdersModel> call, Response<OrdersModel> response) {
                progressBar.setVisibility(View.GONE);
                OrdersModel orderLists = response.body();
                Log.d(TAG, "Number of order received: " + orderLists.getOrderList().size());
            }

            @Override
            public void onFailure(Call<OrdersModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                // Log error here since request failed
                Log.e(TAG, "callRetrofitOrderApi--" + t.toString());

            }
        });

    }
}
