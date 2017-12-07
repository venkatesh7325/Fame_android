package com.purpledot.fame.activites.loginModule;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.purpledot.fame.APIS.APIRequest;
import com.purpledot.fame.APIS.RequestCallBack;
import com.purpledot.fame.POJO.LoginResponse;
import com.purpledot.fame.POJO.OtpValidateModel;
import com.purpledot.fame.R;
import com.purpledot.fame.activites.DashBoardActivity;
import com.purpledot.fame.utils.ApiConstants;
import com.purpledot.fame.utils.Constants;
import com.purpledot.fame.utils.Logger;
import com.purpledot.fame.utils.SharedPrefManager;
import com.purpledot.fame.utils.ToastUtils;
import com.purpledot.fame.utils.Utility;

import java.util.HashMap;

public class OtpValidateActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtOtp;
    Button btnOtp;
    SharedPrefManager prefManager;
    private String TAG = OtpValidateActivity.class.getSimpleName();
    String mobileIMEINumber = "";
    String mSimNumber = "";
    TextView tvResendOtp;
    private int resenOtpCount = 0;
    LinearLayout llRootOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_validate);
        prefManager = new SharedPrefManager(this);
        getIMEINum();
        llRootOtp = (LinearLayout) findViewById(R.id.root_otp_activity);
        edtOtp = (EditText) findViewById(R.id.edt_otp);
        btnOtp = (Button) findViewById(R.id.btn_otp);
        btnOtp.setOnClickListener(this);
        tvResendOtp = (TextView) findViewById(R.id.tv_resend);
        tvResendOtp.setOnClickListener(this);
    }

    private void getIMEINum() {
        try {
            TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // To handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (tMgr != null) {
                mSimNumber = tMgr.getSimSerialNumber();
                mobileIMEINumber = tMgr.getImei(prefManager.getSimSlotNumber());
                Log.i(TAG, "IMEI--" + mobileIMEINumber + "--getDeviceId--" + tMgr.getDeviceId());
                Log.d(TAG, "IMEI--" + mobileIMEINumber + "--Sim number--" + mSimNumber);
                // IMEI--354865076059565
                // Sim number--89918610400020222197
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_otp:
                if (Utility.isConnectingToInternet(this))
                    validateAndOtoValidate(); // Validate the login and call Login API
                else
                    Snackbar.make(llRootOtp, Constants.no_internet_connection, Snackbar.LENGTH_LONG).show();
                break;
            case R.id.tv_resend:
                if (Utility.isConnectingToInternet(this)) {
                    resenOtpCount++;
                    if (resenOtpCount >= 3) {
                        tvResendOtp.setVisibility(View.GONE);
                    } else {
                        tvResendOtp.setVisibility(View.VISIBLE);
                        callApiResendOtp(); // resend OTP to register mail or mobile number
                    }

                } else
                    Snackbar.make(llRootOtp, Constants.no_internet_connection, Snackbar.LENGTH_LONG).show();

                break;
        }
    }

    private void callApiResendOtp() {
        try {
            final HashMap<String, String> hashMap = new HashMap<>();
            try {
                // keys: email, imei, sim_number
                hashMap.put("imei", mobileIMEINumber);
                hashMap.put("sim_number", mSimNumber);
                hashMap.put("email", prefManager.getClickedUser());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Login Activity HASH MAP--" + hashMap);
            new APIRequest(this).postStringRequest(ApiConstants.LOGIN, hashMap, new RequestCallBack() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Login Activity--" + response);
                    Gson gson = new Gson();
                    LoginResponse successResponsePojo = gson.fromJson(response, LoginResponse.class);
                    if ("1".equalsIgnoreCase(successResponsePojo.getStatus())) {
                        ToastUtils.displayToast(successResponsePojo.getMessage(), OtpValidateActivity.this);
                    } else {
                        ToastUtils.displayToast(successResponsePojo.getMessage(), OtpValidateActivity.this);
                    }
                }

                @Override
                public void onFailed(String message) {
                    ToastUtils.displayToast(Constants.something_went_wrong, OtpValidateActivity.this);
                    Log.d(TAG, "Login Error message--" + message + "--jon respons--" + hashMap);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateAndOtoValidate() {
        try {
            //Log.d(TAG,"Dual SIM serial numbers"+getOutput(LoginActivity.this,))
            String otp = edtOtp.getText().toString().trim();
            if (TextUtils.isEmpty(otp)) {
                ToastUtils.displayToast("Please enter OTP", OtpValidateActivity.this);
                return;
            }

            Log.d(TAG, "OTP Current date--" + Utility.getCurrentDateOnly() + "--" + Constants.CURRENT_DATE + "--" + Constants.CURRENT_TIME);
            // Log.d(TAG, "OTP Current Time--" + Utility.getCurrentTime());
           /* if (!Constants.CURRENT_DATE.equals(Utility.getCurrentDateOnly())) {
                ToastUtils.displayToast("Dates are wrong", OtpValidateActivity.this);
                return;
            }*/
            /*if (Utility.getCurrentTime().before(Constants.CURRENT_TIME)) {
                Log.d(TAG, "Before");
            }*/
            /*if (Utility.getCurrentTime().after(Constants.CURRENT_TIME)) {
                Log.d(TAG, "After");
                ToastUtils.displayToast("Entered OTP is Expired", OtpValidateActivity.this);
                return;
            }*/
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            try {
                hashMap.put("imei", mobileIMEINumber);
                hashMap.put("sim_number", mSimNumber);
                hashMap.put("submit_otp", otp);
                hashMap.put("email", prefManager.getClickedUser());  //venkatesh.b@purpledot.in
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "OTP Activity HASH MAP--" + hashMap);
            new APIRequest(this).postStringRequest(ApiConstants.VALIDATE_OTP, hashMap, new RequestCallBack() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "OTP Activity--" + response);
                    //  ToastUtils.displayToast("Result--"+response, OtpValidateActivity.this);
                    Gson gson = new Gson();
                    OtpValidateModel successResponsePojo = gson.fromJson(response, OtpValidateModel.class);
                    if (successResponsePojo.getStatus().equalsIgnoreCase("1")) {
                        ToastUtils.displayToast("Login success", OtpValidateActivity.this);
                        prefManager.saveSuccess(OtpValidateActivity.this, true);
                        prefManager.saveId(OtpValidateActivity.this, successResponsePojo.getId());
                        Intent i = new Intent(OtpValidateActivity.this, DashBoardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        ToastUtils.displayToast(successResponsePojo.getMessage(), OtpValidateActivity.this);
                    }
                }

                @Override
                public void onFailed(String message) {
                    ToastUtils.displayToast(Constants.something_went_wrong, OtpValidateActivity.this);
                    Log.d(TAG, "Login Error message--" + message + "--jon respons--" + hashMap);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
