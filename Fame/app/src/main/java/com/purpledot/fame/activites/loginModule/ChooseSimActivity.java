package com.purpledot.fame.activites.loginModule;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purpledot.fame.R;
import com.purpledot.fame.utils.PermissionClass;
import com.purpledot.fame.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class ChooseSimActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ChooseSimActivity.class.getSimpleName();
    LinearLayout llSimOne;
    LinearLayout llSimTwo;
    SharedPrefManager prefManager;
    TextView tvSim1, tvSim2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sim);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefManager = new SharedPrefManager(this);
        tvSim1 = findViewById(R.id.tv_sim1);
        tvSim2 = findViewById(R.id.tv_sim2);
        llSimOne = findViewById(R.id.ll_sim1);
        llSimTwo = findViewById(R.id.ll_sim2);
        llSimOne.setOnClickListener(this);
        llSimTwo.setOnClickListener(this);
        getIMEINum();
        getNetworkOperator(ChooseSimActivity.this);
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
                Log.d(TAG, "Inside  --" + tMgr.getNetworkOperatorName() + "-Number-" + tMgr.getLine1Number());

                // IMEI--354865076059565
                // Sim number--89918610400020222197
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getNetworkOperator(final Context context) {
        // Get System TELEPHONY service reference
        List<String> carrierNames = new ArrayList<>();
        try {
            final String permission = Manifest.permission.READ_PHONE_STATE;
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) && (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)) {
                final List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                for (int i = 0; i < subscriptionInfos.size(); i++) {
                    carrierNames.add(subscriptionInfos.get(i).getCarrierName().toString());
                    Log.d(TAG, "Operator name--" + subscriptionInfos.get(i).getCarrierName().toString());
                    if (subscriptionInfos.get(0) != null && subscriptionInfos.get(0).getCarrierName() != null)
                        tvSim1.setText("SIM 1 \n" + subscriptionInfos.get(0).getCarrierName().toString());
                    if (subscriptionInfos.get(1) != null && subscriptionInfos.get(1).getCarrierName() != null)
                        tvSim2.setText("SIM 2 \n" + subscriptionInfos.get(1).getCarrierName().toString());
                }

            } else {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                // Get carrier name (Network Operator Name)
                carrierNames.add(telephonyManager.getNetworkOperatorName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "carrierNames.size--" + carrierNames.size());
        return carrierNames;
    }

    void callPermissionClass() {
        if (PermissionClass.checkPermission(this)) {
        } else {
            PermissionClass.requestPermission(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()) {
            case R.id.ll_sim1:
                llSimOne.setBackground(getResources().getDrawable(R.drawable.border_with_color));
                llSimTwo.setBackground(getResources().getDrawable(R.drawable.rounded_corners));
                prefManager.saveSimSlotNum(ChooseSimActivity.this, 0);
                i = new Intent(ChooseSimActivity.this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.ll_sim2:
                llSimTwo.setBackground(getResources().getDrawable(R.drawable.border_with_color));
                llSimOne.setBackground(getResources().getDrawable(R.drawable.rounded_corners));
                prefManager.saveSimSlotNum(ChooseSimActivity.this, 1);
                i = new Intent(ChooseSimActivity.this, LoginActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
