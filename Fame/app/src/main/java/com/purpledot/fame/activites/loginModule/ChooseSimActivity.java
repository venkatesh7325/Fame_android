package com.purpledot.fame.activites.loginModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.purpledot.fame.R;
import com.purpledot.fame.utils.SharedPrefManager;

public class ChooseSimActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout llSimOne;
    LinearLayout llSimTwo;
    SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sim);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefManager = new SharedPrefManager(this);
        llSimOne = findViewById(R.id.ll_sim1);
        llSimTwo = findViewById(R.id.ll_sim2);
        llSimOne.setOnClickListener(this);
        llSimTwo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()) {
            case R.id.ll_sim1:
                llSimOne.setBackground(getResources().getDrawable(R.drawable.border_with_color));
                llSimTwo.setBackground(getResources().getDrawable(R.drawable.rounded_corners));
                prefManager.saveSimSlotNum(ChooseSimActivity.this,0);
                i = new Intent(ChooseSimActivity.this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.ll_sim2:
                llSimTwo.setBackground(getResources().getDrawable(R.drawable.border_with_color));
                llSimOne.setBackground(getResources().getDrawable(R.drawable.rounded_corners));
                prefManager.saveSimSlotNum(ChooseSimActivity.this,1);
                i = new Intent(ChooseSimActivity.this, LoginActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
