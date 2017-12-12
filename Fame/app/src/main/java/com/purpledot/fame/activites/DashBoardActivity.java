package com.purpledot.fame.activites;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.purpledot.fame.APIS.APIRequest;
import com.purpledot.fame.APIS.RequestCallBack;
import com.purpledot.fame.POJO.LoginResponse;
import com.purpledot.fame.POJO.UserDetailsModel;
import com.purpledot.fame.R;
import com.purpledot.fame.activites.loginModule.LoginActivity;
import com.purpledot.fame.activites.loginModule.OtpValidateActivity;
import com.purpledot.fame.activites.productModule.CartListActivity;
import com.purpledot.fame.activites.productModule.ProductsFilterActivity;
import com.purpledot.fame.adapters.SildePageAdapter;
import com.purpledot.fame.fragments.dashboardFragments.AddNewAddressFragment;
import com.purpledot.fame.fragments.dashboardFragments.AddressListFragment;
import com.purpledot.fame.fragments.dashboardFragments.DashBoardFragment;
import com.purpledot.fame.fragments.dashboardFragments.MarketPlaceFragment;
import com.purpledot.fame.fragments.dashboardFragments.NewArrivalsFragment;
import com.purpledot.fame.fragments.dashboardFragments.OrdersFragment;
import com.purpledot.fame.fragments.dashboardFragments.StatisticsFragment;
import com.purpledot.fame.interfaces.AddToCartClickResponse;
import com.purpledot.fame.utils.ApiConstants;
import com.purpledot.fame.utils.Constants;
import com.purpledot.fame.utils.FileUtils;
import com.purpledot.fame.utils.SharedPrefManager;
import com.purpledot.fame.utils.ToastUtils;
import com.purpledot.fame.utils.Utility;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = DashBoardActivity.class.getSimpleName();
    private static final int SELECT_FILE = 101;
    private static final int REQUEST_CAMERA = 102;
    ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.women1, R.drawable.wmn2, R.drawable.wmn3, R.drawable.women1};
    private ArrayList<Integer> ImagesArray = new ArrayList<>();
    private static FragmentManager fragmentManager;
    Toolbar toolbar;
    LinearLayout llBanners;
    SharedPrefManager prefManager;
    TextView tvCartCount;
    RelativeLayout rlvCart;
    RelativeLayout rlAddNewAddress;
    TextView tvHeaderUserEmail;
    TextView tvHeaderUserName;
    private String userChoosenTask;
    CircularImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        prefManager = new SharedPrefManager(this);

        bindToolbar();  // set up toolbar
        initSlideImages(); //  slide images
        setUpNavigationDrawer(); // side menu

        if (Utility.isConnectingToInternet(this))
            callApiToGetUserDetails(); // call API to get user details
        else
            ToastUtils.displayToast(Constants.no_internet_connection, this);

        //getIMEINum();
        int cartCount = prefManager.getCartCount(); // get the cart count from pref
        showCartCount(cartCount); // Show cart count in toolbar
        llBanners.setVisibility(View.GONE);
        InflateFirstTimeFragment(MarketPlaceFragment.newInstance("", ""), "0", true);
        // IMEI 0--354865076059565--IMEI 1--354865076059573

    }

    @Override
    protected void onResume() {
        super.onResume();
        int cartCount = prefManager.getCartCount(); // get the cart count from pref
        showCartCount(cartCount); // Show cart count in toolbar
    }

    private void setUpNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        tvHeaderUserName = hView.findViewById(R.id.tv_userName);
        tvHeaderUserEmail = hView.findViewById(R.id.tv_userEmail);
        userImage = hView.findViewById(R.id.imageView);
        Bitmap bmImgFromSdCard = FileUtils.getImageFromSDCard(this, getResources().getString(R.string.app_name) + "_" + prefManager.getId() + ".jpg");
        if (bmImgFromSdCard != null)
            userImage.setImageBitmap(bmImgFromSdCard);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }
    void bindToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.dashboard));
        tvCartCount = toolbar.findViewById(R.id.tv_cartCount);
        rlvCart = toolbar.findViewById(R.id.rl_cart);
        rlvCart.setOnClickListener(this);
        rlAddNewAddress = (RelativeLayout) findViewById(R.id.rl_add_new_address);
        rlAddNewAddress.setOnClickListener(this);
    }

    private void initSlideImages() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);
        mPager = (ViewPager) findViewById(R.id.pager);
        llBanners = (LinearLayout) findViewById(R.id.ll_banner);
        SildePageAdapter sildePageAdapter = new SildePageAdapter(this, ImagesArray);
        mPager.setAdapter(sildePageAdapter);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.setVisibility(View.VISIBLE);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        NUM_PAGES = IMAGES.length;
        Log.d("image", "Images size" + NUM_PAGES);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, 1000);
        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

                //  Empty

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

                //    Empty

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if (f != null && f.getClass() != null && f.getClass().getSimpleName() != null) {
            Log.d(TAG, "Fragment name--" + f.getClass().getSimpleName());
            if (f.getClass().getSimpleName() != null)
                setFragmentTitles(f.getClass().getSimpleName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            int backStackCount = fragmentManager.getBackStackEntryCount();//check currently how many frags loaded
            if (backStackCount > 0) {
                fragmentManager.popBackStack(); //go back to previously loaded fragment
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_dash) {
            toolbar.setTitle(getResources().getString(R.string.dashboard));
            llBanners.setVisibility(View.VISIBLE);
            rlAddNewAddress.setVisibility(View.GONE);
            InflateFirstFragment(DashBoardFragment.newInstance("", ""), "0", true);
        } else if (id == R.id.nav_market_place) {
            llBanners.setVisibility(View.GONE);
            rlAddNewAddress.setVisibility(View.GONE);
            toolbar.setTitle(getResources().getString(R.string.market_place));
            MarketPlaceFragment marketPlaceFragment = new MarketPlaceFragment(tvCartCount);
            InflateFirstFragment(marketPlaceFragment, "0", true);
            //setFragment(marketPlaceFragment);
        } else if (id == R.id.nav_orders) {
            toolbar.setTitle(getResources().getString(R.string.orders));
            rlAddNewAddress.setVisibility(View.GONE);
            InflateFirstFragment(OrdersFragment.newInstance("", ""), "0", true);
        } else if (id == R.id.nav_address) {
            llBanners.setVisibility(View.GONE);
            rlAddNewAddress.setVisibility(View.VISIBLE);
            toolbar.setTitle(getResources().getString(R.string.address));
            InflateFirstFragment(AddressListFragment.newInstance("", R.id.container), "0", true);
        } else if (id == R.id.nav_account_details) {
            llBanners.setVisibility(View.GONE);
            rlAddNewAddress.setVisibility(View.GONE);
            toolbar.setTitle(getResources().getString(R.string.account_details));
        } else if (id == R.id.nav_pre_orders) {
            toolbar.setTitle(getResources().getString(R.string.pre_order));
            rlAddNewAddress.setVisibility(View.GONE);
            ToastUtils.displayToast("Coming soon...", DashBoardActivity.this);
        } else if (id == R.id.nav_logout) {
            popupToLogOut();
        } else if (id == R.id.nav_fav) {
            toolbar.setTitle(getResources().getString(R.string.fav));
            rlAddNewAddress.setVisibility(View.GONE);
            ToastUtils.displayToast("Coming soon...", DashBoardActivity.this);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    {
        fragmentManager = getSupportFragmentManager();
    }

    public static void InflateFirstTimeFragment(Fragment fragment, String fragment_name, boolean addfragment) {
        try {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Log.d(TAG, "setFragment--" + fragmentManager.getBackStackEntryCount());
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void InflateFirstFragment(Fragment fragment, String fragment_name, boolean addfragment) {
        try {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Log.d(TAG, "setFragment--" + fragmentManager.getBackStackEntryCount());
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void popupToLogOut() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Logout");
        alertDialog.setMessage("Are you sure want to logout?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prefManager.clear();
                startActivity(new Intent(DashBoardActivity.this, LoginActivity.class));
                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog1 = alertDialog.create();
        if (alertDialog1.getWindow() != null && alertDialog1.getWindow().getAttributes() != null)
            alertDialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;

        alertDialog1.show();
    }

    private void showCartCount(int cartCount) {
        try {
            if (cartCount > 0) {
                Log.d(TAG, "if Cart count from pref--" + cartCount);
                tvCartCount.setVisibility(View.VISIBLE);
                tvCartCount.setText(String.valueOf(cartCount));
            } else {
                Log.d(TAG, "Else Cart count from pref--" + cartCount);
                tvCartCount.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_cart:
                navigateToCartCount();
                break;
            case R.id.rl_add_new_address:
                rlAddNewAddress.setVisibility(View.GONE);
                InflateFirstFragment(new AddNewAddressFragment(), "0", true);
                break;
            default:
                break;
        }
    }

    private void navigateToCartCount() {
        int cartCount = prefManager.getCartCount();
        if (cartCount > 0) {
            Intent i = new Intent(DashBoardActivity.this, CartListActivity.class);
            startActivity(i);
        } else {
            ToastUtils.displayToast("Your Shopping Cart is Empty!", this);
        }
    }

    private void setFragmentTitles(String fragmentName) {
        try {
            switch (fragmentName) {
                case "DashBoardFragment":
                    toolbar.setTitle(getResources().getString(R.string.dashboard));
                    llBanners.setVisibility(View.VISIBLE);
                    rlAddNewAddress.setVisibility(View.GONE);
                    break;
                case "MarketPlaceFragment":
                    llBanners.setVisibility(View.GONE);
                    rlAddNewAddress.setVisibility(View.GONE);
                    toolbar.setTitle(getResources().getString(R.string.market_place));
                    MarketPlaceFragment marketPlaceFragment = new MarketPlaceFragment(tvCartCount);
                    //  setFragment(marketPlaceFragment);
                    InflateFirstFragment(marketPlaceFragment, "0", true);
                    break;
                case "AddressListFragment":
                    llBanners.setVisibility(View.GONE);
                    rlAddNewAddress.setVisibility(View.VISIBLE);
                    toolbar.setTitle(getResources().getString(R.string.address));
                    break;
                case "OrdersFragment":
                    llBanners.setVisibility(View.GONE);
                    rlAddNewAddress.setVisibility(View.GONE);
                    toolbar.setTitle(getResources().getString(R.string.orders));
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callApiToGetUserDetails() {
        final HashMap<String, String> hashMap = new HashMap<>();
        try {
            hashMap.put("user_id", prefManager.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Dash Activity HASH MAP--" + hashMap);
        new APIRequest(this).postStringRequest(ApiConstants.USER_DETAILS, hashMap, new RequestCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Dash Activity--" + response);
                Gson gson = new Gson();
                UserDetailsModel userDetailsModel = gson.fromJson(response, UserDetailsModel.class);
                if ("1".equalsIgnoreCase(userDetailsModel.getStatus())) {
                    if (userDetailsModel.getEmail() == null)
                        return;

                    Log.d(TAG, " -- EMAIL -- < -- > " + userDetailsModel.getEmail() + "--Mobile--" + userDetailsModel.getMobile());
                    prefManager.saveEmail(DashBoardActivity.this, userDetailsModel.getEmail());
                    tvHeaderUserEmail.setText(userDetailsModel.getEmail());
                    tvHeaderUserName.setText(userDetailsModel.getName());
                } else {
                    ToastUtils.displayToast(userDetailsModel.getMessage(), DashBoardActivity.this);
                }
            }

            @Override
            public void onFailed(String message) {
                ToastUtils.displayToast(Constants.something_went_wrong, DashBoardActivity.this);
                Log.d(TAG, "Login Error message--" + message + "--jon respons--" + hashMap);
            }
        });
    }

    private void selectImage() {
        try {
            final CharSequence[] items = {"Camera", "Gallery"};
            AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Camera")) {
                        userChoosenTask = "Camera";
                        cameraIntent();

                    } else if (items[item].equals("Gallery")) {
                        userChoosenTask = "Gallery";
                        galleryIntent();

                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cameraIntent() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void galleryIntent() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        try {
            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    Log.d(TAG, "Gallery Bitmap--" + bm);
                    boolean imgSaveFlag = FileUtils.saveImageIntoSdcard(DashBoardActivity.this, bm, getResources().getString(R.string.app_name) + "_" + prefManager.getId() + ".jpg");
                    Log.d(TAG, "Camera Bitmap--" + imgSaveFlag);
                    if (imgSaveFlag)
                        userImage.setImageBitmap(bm);
                    else
                        ToastUtils.displayToast("Image not saved in SD CARD", DashBoardActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            userImage.setImageBitmap(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCaptureImageResult(Intent data) {
        try {
            if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                if (thumbnail == null) {
                    Log.d(TAG, "Camera Bitmap NULL");
                    return;
                }
                Log.d(TAG, "Camera Bitmap--" + thumbnail);
                boolean imgSaveFlag = FileUtils.saveImageIntoSdcard(DashBoardActivity.this, thumbnail, getResources().getString(R.string.app_name) + "_" + prefManager.getId() + ".jpg");
                Log.d(TAG, "Save Flag --" + imgSaveFlag);
                if (imgSaveFlag)
                    userImage.setImageBitmap(thumbnail);
                else
                    ToastUtils.displayToast("Image not saved in SD CARD", DashBoardActivity.this);
            } else
                Log.d(TAG, "DATA NULL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

