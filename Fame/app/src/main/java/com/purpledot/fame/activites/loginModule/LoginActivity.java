package com.purpledot.fame.activites.loginModule;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import com.purpledot.fame.utils.PermissionClass;
import com.purpledot.fame.utils.SharedPrefManager;
import com.purpledot.fame.utils.ToastUtils;
import com.purpledot.fame.utils.Utility;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtMobile;
    Button btnLogin;
    private String TAG = LoginActivity.class.getSimpleName();
    String mobileIMEINumber = "";
    String mSimNumber = "";
    SharedPrefManager prefManager;
    ScrollView scrollView;
    int simSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefManager = new SharedPrefManager(this);
        Log.d(TAG, "Sim slot Number--" + prefManager.getSimSlotNumber());
        Utility.hideKeyBoard(LoginActivity.this);
        bindViews();
        callPermissionClass();

    }

    // Bind the views
    private void bindViews() {
        try {
            scrollView = findViewById(R.id.root_scv_login);
            edtMobile = (EditText) findViewById(R.id.edt_login_mobile);
            btnLogin = (Button) findViewById(R.id.btn_login);
            btnLogin.setOnClickListener(this);
            if (!Utility.isConnectingToInternet(this))
                Snackbar.make(scrollView, Constants.no_internet_connection, Snackbar.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                Log.d(TAG, "Inside tm slot Number --" + prefManager.getSimSlotNumber());
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

    void callPermissionClass() {
        if (PermissionClass.checkPermission(this)) {
        } else {
            PermissionClass.requestPermission(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (Utility.isConnectingToInternet(this))
                    validateAndLogin(); // Validate the login and call Login API
                else
                    Snackbar.make(scrollView, Constants.no_internet_connection, Snackbar.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    private void validateAndLogin() {
        try {
            getIMEINum();
            //Log.d(TAG,"Dual SIM serial numbers"+getOutput(LoginActivity.this,))
            final String mobile = edtMobile.getText().toString().trim();
            if (TextUtils.isEmpty(mobile)) {
                ToastUtils.displayToast("Please enter user name or mobile", LoginActivity.this);
                return;
            }

            //ToastUtils.displayToast("Success", this);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            try {
                // keys: email, imei, sim_number
                hashMap.put("imei", mobileIMEINumber);
                hashMap.put("sim_number", mSimNumber);
                hashMap.put("email", mobile);
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
                        prefManager.saveClickedUserNum(LoginActivity.this, mobile);
                        ToastUtils.displayToast(successResponsePojo.getMessage(), LoginActivity.this);
                        startActivity(new Intent(LoginActivity.this, OtpValidateActivity.class));
                    } else {
                        if (successResponsePojo.getMessage().equalsIgnoreCase("Please login from your registered device!")) {
                            ToastUtils.displayToast("Your selected sim was not linked with FAME", LoginActivity.this);
                        } else
                            ToastUtils.displayToast(successResponsePojo.getMessage(), LoginActivity.this);
                    }
                }
                @Override
                public void onFailed(String message) {
                    ToastUtils.displayToast(Constants.something_went_wrong, LoginActivity.this);
                    Log.d(TAG, "Login Error message--" + message + "--jon respons--" + hashMap);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getOutput(Context context, String methodName, int slotId) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> telephonyClass;
        String reflectionMethod = null;
        String output = null;
        try {
            telephonyClass = Class.forName(telephony.getClass().getName());
            for (Method method : telephonyClass.getMethods()) {
                String name = method.getName();
                if (name.contains(methodName)) {
                    Class<?>[] params = method.getParameterTypes();
                    if (params.length == 1 && params[0].getName().equals("int")) {
                        reflectionMethod = name;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (reflectionMethod != null) {
            try {
                output = getOpByReflection(telephony, reflectionMethod, slotId, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return output;
    }

    private static String getOpByReflection(TelephonyManager telephony, String predictedMethodName, int slotID, boolean isPrivate) {

        //Log.i("Reflection", "Method: " + predictedMethodName+" "+slotID);
        String result = null;

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID;
            if (slotID != -1) {
                if (isPrivate) {
                    getSimID = telephonyClass.getDeclaredMethod(predictedMethodName, parameter);
                } else {
                    getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
                }
            } else {
                if (isPrivate) {
                    getSimID = telephonyClass.getDeclaredMethod(predictedMethodName);
                } else {
                    getSimID = telephonyClass.getMethod(predictedMethodName);
                }
            }

            Object ob_phone;
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            if (getSimID != null) {
                if (slotID != -1) {
                    ob_phone = getSimID.invoke(telephony, obParameter);
                } else {
                    ob_phone = getSimID.invoke(telephony);
                }

                if (ob_phone != null) {
                    result = ob_phone.toString();

                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        //Log.i("Reflection", "Result: " + result);
        return result;
    }

    void loginWithRegMobilePopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage("Please Login with register Mobile or Email with FAME");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }


}
