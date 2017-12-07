package com.purpledot.fame.activites.productModule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.purpledot.fame.POJO.products.productsList.Product;
import com.purpledot.fame.R;


public class ProductDetailsActivity extends AppCompatActivity {

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();
    TextView tvProductName, tvProductPrice, tvProductSeller;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        init();
        getProductDetailsFromPreviousActivity();
        Handler handler1 = new Handler();
    }

    private void init() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  getSupportActionBar().setDisplayShowHomeEnabled(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.p4);
                Palette.generateAsync(bitmap,
                        new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                Palette.Swatch vibrant =
                                        palette.getVibrantSwatch();
                                int mutedColor = palette.getVibrantSwatch().getRgb();
                                if (vibrant != null) {
                                    // If we have a vibrant color
                                    // update the title TextView
                                    collapsingToolbarLayout.setBackgroundColor(mutedColor);
                                    //  mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                                    collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(mutedColor));
                                    collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(mutedColor));

                                }
                            }
                        });
            }
        });
        tvProductName = (TextView) findViewById(R.id.txt_pd_ProductName);
        tvProductPrice = (TextView) findViewById(R.id.txt_pd_StoreEPrice);
        tvProductSeller = (TextView) findViewById(R.id.txt_pd_StoreSellerName);
    }

    private void getProductDetailsFromPreviousActivity() {
        if (getIntent() == null && getIntent().getExtras() == null)
            return;
        Bundle b = getIntent().getExtras();
        if (b != null && b.getParcelable("product") != null) {
            Product product = b.getParcelable("product");
            if (product == null)
                return;
            Log.d(TAG, "Product name --" + product.getProductName() + "--pID" + product.getProductId());
            tvProductName.setText(product.getProductName());
            tvProductPrice.setText(product.getProductPrice());
            tvProductSeller.setText(product.getProductSellerName());
            collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.app_white_buttons_bg_color));
            collapsingToolbarLayout.setTitle(product.getProductName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    /*public void onClick(View v) {
    if (v == start) {
        for (int a = 0; a<4 ;a++) {
            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                ImageButton[] all= {btn1, btn2, btn3, btn4};
                @Override
                public void run() {
                    btn5 = all[random.nextInt(all.length)];
                    btn5.setBackgroundColor(Color.RED);
                }
            }, 1000);
        }
    }
}
Example for Delay :

final Handler handler = new Handler();
handler.postDelayed(new Runnable() {
    @Override
    public void run() {
        // Do something after 5s = 5000ms
        buttons[inew][jnew].setBackgroundColor(Color.Red);
    }
}, 5000);*/
}
