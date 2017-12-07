package com.purpledot.fame.APIS;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.purpledot.fame.utils.ApiConstants;
import com.purpledot.fame.utils.Constants;
import com.purpledot.fame.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASUS on 9/20/2016.
 */
public class APIRequest {

    private static final String TAG = APIRequest.class.getSimpleName();
    Context context;
    View view;
    ProgressDialog progressDialog;

    public APIRequest(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        //  progressDialog.setCancelable(cancelable);
    }

    public APIRequest(View view) {
        this.view = view;
        this.context = view.getContext();
    }

    public APIRequest(View view, boolean cancelable) {
        this.view = view;
        this.context = view.getContext();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(cancelable);
    }

    public APIRequest(View view, String progressBarMessage) {
        this.view = view;
        this.context = view.getContext();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(progressBarMessage);
    }

    public APIRequest(View view, boolean cancelable, String progressBarMessage) {
        this.view = view;
        this.context = view.getContext();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(progressBarMessage);
        progressDialog.setCancelable(cancelable);
    }

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        return header;

    }

    public void postAPI(String method, JSONObject jsonObject, final RequestCallBack requestCallBack) {

        try {

            if (!Utility.isConnectingToInternet(context)) {
                requestCallBack.onFailed(Constants.no_internet_connection);
            } else {
                if (progressDialog != null)
                    progressDialog.show();

                Log.e(TAG, " Constant.MAIN_URL + method) ---" + Constants.MAIN_URL + method);
                Log.e(TAG, "JSON req.toString() ---" + jsonObject);
                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.POST, Constants.MAIN_URL + method, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject json) {
                                try {
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                    if (json != null) {
                                        requestCallBack.onResponse(json.toString());
                                    } else {
                                        requestCallBack.onFailed(Constants.something_went_wrong);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    requestCallBack.onFailed(e.getMessage());
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                                error.printStackTrace();
                                requestCallBack.onFailed(error.getMessage());

                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("X-Authorization", ApiConstants.Authorization);
                        return map;
                    }

                };

                int socketTimeout = 60000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsonRequest.setRetryPolicy(policy);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // MyDialog.snackBar_Error_Bottom(view, e.getMessage());
            requestCallBack.onFailed(e.getMessage());
        }

    }

    public void postJsonRequestMethod(String subUrl, JSONObject jsonObject, final RequestCallBack requestCallBack) {
        try {
            if (!Utility.isConnectingToInternet(context)) {
                requestCallBack.onFailed(Constants.no_internet_connection);
            } else {
                if (progressDialog != null)
                    progressDialog.show();
                Log.e("Url and paramas", ApiConstants.BASE_URL+subUrl + "--------" + jsonObject);
                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.POST, ApiConstants.BASE_URL+ subUrl, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject json) {
                                try {
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                    Log.e("result", "result  " + json);
                                    if (json != null) {
                                        requestCallBack.onResponse(json.toString());
                                    } else {
                                        requestCallBack.onFailed("SOMETHING WENT WRONG");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    requestCallBack.onFailed(e.getMessage());
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                                error.printStackTrace();
                                requestCallBack.onFailed(error.getMessage());
                            }
                        }) {
                   /* @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("X-Authorization", ApiConstants.Authorization);
                        return map;
                    }*/
                };
                int socketTimeout = 60000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsonRequest.setRetryPolicy(policy);
                Volley.newRequestQueue(context).add(jsonRequest);
                //ApplicationController.getInstance().addToRequestQueue(jsonRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //   MyDialog.snackBar_Error_Bottom(view, e.getMessage());
            requestCallBack.onFailed(e.getMessage());
        }

    }

    public  void get(String subURl, JSONObject jsonObject, final RequestCallBack requestCallBack) {
        try {
            if (!Utility.isConnectingToInternet(context)) {
                requestCallBack.onFailed(Constants.no_internet_connection);
            } else {
                if (progressDialog != null)
                    progressDialog.show();
                // HttpsTrustManager.allowAllSSL();
                Log.d("Url and paramas", ApiConstants.BASE_URL + "" + subURl + "--------" + jsonObject);
                String url = /*ApiConstants.BASE_URL +*/ subURl;
                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.GET, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject json) {
                                try {
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                    Log.e("result", "result  " + json);
                                    if (json != null) {
                                        requestCallBack.onResponse(json.toString());
                                    } else {
                                        requestCallBack.onFailed(Constants.something_went_wrong);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    requestCallBack.onFailed(TAG + e.getMessage());
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                                error.printStackTrace();
                                requestCallBack.onFailed(TAG + error.getMessage());
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return getHeader();
                    }
                };
                int socketTimeout = 60000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsonRequest.setRetryPolicy(policy);
                Volley.newRequestQueue(context).add(jsonRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestCallBack.onFailed(TAG + e.getMessage());
        }
    }
    public void postStringRequest(String subURl, final Map<String, String> params, final RequestCallBack requestCallBack) {
        try {
            Log.e(TAG, "POST PARAMS-->" + params);
            String url = ApiConstants.BASE_URL + subURl;
            Log.e(TAG, "POST URL-->" + url + " - " + params);
            if (!Utility.isConnectingToInternet(context)) {
                requestCallBack.onFailed(Constants.no_internet_connection);
            } else {
                if (progressDialog != null) {
                 progressDialog.setMessage("Please wait....");
                    progressDialog.show();
                }
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        try {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            if (json != null) {
                                // Log.e(TAG,"Response in postStringRequest --"+json);
                                requestCallBack.onResponse(json);
                            } else {
                                requestCallBack.onFailed("SOMETHING WENT WRONG");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            requestCallBack.onFailed(e.getMessage());
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        error.printStackTrace();
                        requestCallBack.onFailed(error.getMessage());
                    }
                }) {
                    /*@Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                     //   map.put("Content-Type", "application/json");
                     //   map.put("X-Authorization", ApiConstants.Authorization);
                        return map;
                    }*/

                    protected Map<String, String> getParams() throws AuthFailureError {

                        return params;
                    }
                };
                int socketTimeout = 60000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                Volley.newRequestQueue(context).add(stringRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestCallBack.onFailed(e.getMessage());
        }
    }



}
