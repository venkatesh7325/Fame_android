package com.purpledot.fame.activites.productModule;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.purpledot.fame.R;

public class ProductsFilterActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvFilterApply;
    int APPLY_REQUEST_CODE =95;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_filter);

        tvFilterApply = findViewById(R.id.tv_filter_apply);
        tvFilterApply.setOnClickListener(this);

        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekBar = findViewById(R.id.rangeSeekbar1);
        // get min and max text view
        final TextView tvMin = findViewById(R.id.tv_min);
        final TextView tvMax = findViewById(R.id.tv_max);
        // set listener
        rangeSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });
        // set final value listener
        rangeSeekBar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("-- CRS ===>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_filter_apply:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", "From Filter Activity");
                setResult(94, returnIntent);
                finish();
                break;
            default:
                break;
        }
    }
}
