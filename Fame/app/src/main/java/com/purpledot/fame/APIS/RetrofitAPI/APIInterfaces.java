package com.purpledot.fame.APIS.RetrofitAPI;


import com.purpledot.fame.POJO.orders.OrderList;
import com.purpledot.fame.POJO.orders.OrdersModel;
import com.purpledot.fame.utils.ApiConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by venkatesh on 11/27/2017.
 */

public interface APIInterfaces {

    /*Take a look to other annotations:

@Path – variable substitution for the API endpoint. For example movie id will be swapped for{id} in the URL endpoint.

@Query – specifies the query key name with the value of the annotated parameter.

@Body – payload for the POST call

@Header – specifies the header with the value of the annotated parameter*/
/* @FormUrlEncoded
    @POST("/api/userlogin")
    Call<ResponseBody>  getUserLogin(
            @Field("client_id") String id,
            @Field("client_secret") String secret,
            @Field("username") String uname,
            @Field("password") String password
    );*/
    /*@POST("api/login")
Call<ApiResponse> loginUser(@Body HashMap<String, String> user);*/


   /* //Convert your data in object

    public class Credentials
    {
        public String email;
        public String password;
    }
  //  Set the data to object

    Credentials loginCredentials = new Credentials();
    loginCredentials.email = "test@gmail.com";
    loginCredentials.password = "password";
    Call your api

    @POST("api/login")
    Call<ApiResponse> loginUser(@Body Credentials credentials);*/

    @GET(ApiConstants.MY_ORDERS)
    Call<OrdersModel> listOrder(/*@Path("user_id") String userId*/);
}
