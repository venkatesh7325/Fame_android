package com.purpledot.fame.utils;

/**
 * Created by drkranga on 9/21/2017.
 */

public class ApiConstants {
    //http://augustairbay.com/test/index.php/login/validate
// http://worldonhire.com/fame-dashboard/html/fame/fame-dash/fame_upload/index.php/login/validate_otp
    public static final String BASE_URL = "http://worldonhire.com/fame-dashboard/html/fame/fame-dash/fame_upload/index.php/"; // class time url
    // public static final String BASE_URL = "http://192.168.1.19/classtime/public/api/"; // local url
    public static final String LOGIN = "login/otp";
    public static final String VALIDATE_OTP = "login/validate_otp";
    public static final String USER_DETAILS = "login/get_user_details";
    public static final String Authorization = "e3b392cea2b19a23deee95f2e4e0057f21d1d061"; // live
    //Order list API : http://worldonhire.com/fame-dashboard/html/fame/fame-dash/api/order/read.php
    public static final String ORDERS_BASE_API = "http://worldonhire.com/fame-dashboard/html/fame/fame-dash/api/";
    public static final String MY_ORDERS = "order/read.php";
    public static final String ORDERS_API = "http://worldonhire.com/fame-dashboard/html/fame/fame-dash/api/order/read.php";
    public static final String ADD_NEW_ADDRESS = "login/add_address";
    public static final String GET_ADDRESS_LIST = "login/get_address";
    public static final String DELETE_ADDRESS = "login/delete_address";
    public static final String UPDATE_ADDRESS = "login/update_address";
}
