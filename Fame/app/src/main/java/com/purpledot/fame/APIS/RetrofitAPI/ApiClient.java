package com.purpledot.fame.APIS.RetrofitAPI;

import com.purpledot.fame.utils.ApiConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by venkatesh on 11/27/2017.
 */

public class ApiClient {
    public static Retrofit getClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.ORDERS_BASE_API).addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }


}
