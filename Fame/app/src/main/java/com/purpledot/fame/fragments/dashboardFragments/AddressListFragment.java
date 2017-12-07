package com.purpledot.fame.fragments.dashboardFragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.purpledot.fame.APIS.APIRequest;
import com.purpledot.fame.APIS.RequestCallBack;
import com.purpledot.fame.POJO.LoginResponse;
import com.purpledot.fame.POJO.address.AddressList;
import com.purpledot.fame.POJO.address.AddressListModel;
import com.purpledot.fame.R;
import com.purpledot.fame.adapters.AddressListAdapter;
import com.purpledot.fame.interfaces.ShowAddressFrgResponse;
import com.purpledot.fame.utils.ApiConstants;
import com.purpledot.fame.utils.Constants;
import com.purpledot.fame.utils.SharedPrefManager;
import com.purpledot.fame.utils.ToastUtils;
import com.purpledot.fame.utils.Utility;

import java.util.HashMap;

import static com.purpledot.fame.activites.DashBoardActivity.InflateFirstTimeFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AddressListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressListFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = AddressListFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int mParam2;
    RecyclerView rcvAddList;
    SharedPrefManager prefManager;
    private static FragmentManager fragmentManager;
    TextView tvAddCount;

    EditText edtCity;
    EditText edtHouseNum;
    EditText edtLocality;
    EditText edtPinCode;
    EditText edtState;
    EditText edtName;
    EditText edtPhoneNumber;
    EditText edtAltNum;
    EditText edtLandmark;
    Button btnSaveAddress;
    String altNumber = "";
    String landMark = "";
    TextView tvErrorCity;
    ScrollView scrollView;
    String addressId = "";


    public AddressListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressListFragment newInstance(String param1, int param2) {
        AddressListFragment fragment = new AddressListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address_list, container, false);
        try {
            prefManager = new SharedPrefManager(getActivity());
            bindAddressViews(view);
            bindUpdateAddressViews(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    void bindUpdateAddressViews(View view) {
        scrollView = view.findViewById(R.id.root_scv_addNewAddress);
        edtCity = view.findViewById(R.id.edt_address_city);
        edtHouseNum = view.findViewById(R.id.edt_address_flat_no);
        edtLocality = view.findViewById(R.id.edt_address_locality);
        edtLandmark = view.findViewById(R.id.edt_address_landmark);
        edtName = view.findViewById(R.id.edt_address_name);
        edtPhoneNumber = view.findViewById(R.id.edt_address_phone_no);
        edtAltNum = view.findViewById(R.id.edt_address_alt_phone_num);
        edtPinCode = view.findViewById(R.id.edt_address_pincode);
        edtState = view.findViewById(R.id.edt_address_state);
        btnSaveAddress = view.findViewById(R.id.btn_save);
        btnSaveAddress.setOnClickListener(this);
        tvErrorCity = view.findViewById(R.id.tv_city_error);
    }

    void bindAddressViews(View view) {
        try {
            tvAddCount = view.findViewById(R.id.tv_address_count);
            rcvAddList = view.findViewById(R.id.rcv_addresses_list);
            if (Utility.isConnectingToInternet(getActivity()))
                getAddressApiList();
            else
                ToastUtils.displayToast(Constants.no_internet_connection, getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getAddressApiList() {
        try {
            final HashMap<String, String> hashMap = new HashMap<>();
            try {
                hashMap.put("user_id", prefManager.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Address Fragment HASH MAP--" + hashMap);
            new APIRequest(getActivity()).postStringRequest(ApiConstants.GET_ADDRESS_LIST, hashMap, new RequestCallBack() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Address Fragment--" + response);
                    Gson gson = new Gson();
                    AddressListModel addressListModel = gson.fromJson(response, AddressListModel.class);
                    if ("1".equalsIgnoreCase(addressListModel.getStatus())) {
                        //ToastUtils.displayToast(addressListModel.getMessage(), getActivity());
                        if (addressListModel.getAddressList() == null && addressListModel.getAddressList().size() == 0) {
                            return;
                        }

                        Log.d(TAG, "Address List size--" + addressListModel.getAddressList().size());
                        tvAddCount.setVisibility(View.VISIBLE);
                        tvAddCount.setText(addressListModel.getAddressList().size() + " SAVED ADDRESSES");
                        rcvAddList.setLayoutManager(new LinearLayoutManager(getActivity()));

                        rcvAddList.setAdapter(new AddressListAdapter(getActivity(), addressListModel.getAddressList(), new ShowAddressFrgResponse() {
                            @Override
                            public void passAddressToActivity(AddressList addressList, int count) {
                                Log.d(TAG, "Address list size--" + addressList.getName());
                                if (count > 0) {
                                    tvAddCount.setVisibility(View.VISIBLE);
                                    if (count == 1)
                                        tvAddCount.setText(count + " SAVED ADDRESS");
                                    else
                                        tvAddCount.setText(count + " SAVED ADDRESSES");
                                }
                            }

                            @Override
                            public void passAfterDeleteAddressCount(int count) {
                                if (count > 0) {
                                    tvAddCount.setVisibility(View.VISIBLE);
                                    tvAddCount.setText(count + " SAVED ADDRESSES");
                                } else {
                                    tvAddCount.setVisibility(View.VISIBLE);
                                    tvAddCount.setText("No address for this user");
                                }
                            }

                            @Override
                            public void editAddress(AddressList addressList) {
                                setAddressToView(addressList);
                            }
                        }));
                    } else {
                        tvAddCount.setVisibility(View.VISIBLE);
                        tvAddCount.setText(addressListModel.getMessage());
                        // ToastUtils.displayToast(addressListModel.getMessage(), getActivity());
                    }
                }

                @Override
                public void onFailed(String message) {
                    ToastUtils.displayToast(Constants.something_went_wrong, getActivity());
                    Log.d(TAG, " Error message--" + message + "--jon respons--" + hashMap);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAddressToView(AddressList addressListPojo) {
        rcvAddList.setVisibility(View.GONE);
        tvAddCount.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
        addressId = addressListPojo.getAddressId();
        if (addressListPojo.getCity() != null)
            edtCity.setText(addressListPojo.getCity());

        if (addressListPojo.getStreet() != null)
            edtLocality.setText(addressListPojo.getStreet());

        if (addressListPojo.getFlatNo() != null)
            edtHouseNum.setText(addressListPojo.getFlatNo());

        if (addressListPojo.getPincode() != null)
            edtPinCode.setText(addressListPojo.getPincode());

        if (addressListPojo.getState() != null)
            edtState.setText(addressListPojo.getState());

        if (addressListPojo.getLandmark() != null)
            edtLandmark.setText(addressListPojo.getLandmark());

        if (addressListPojo.getName() != null)
            edtName.setText(addressListPojo.getName());

        if (addressListPojo.getPhoneNumber() != null)
            edtPhoneNumber.setText(addressListPojo.getPhoneNumber());

        if (addressListPojo.getAlternatePhoneNumber() != null)
            edtAltNum.setText(addressListPojo.getAlternatePhoneNumber());


    }

    public void InflateFirstFragment(Fragment fragment, String fragment_name, boolean addfragment) {
        try {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Log.d(TAG, "setFragment--" + fragmentManager.getBackStackEntryCount());
            fragmentTransaction.replace(mParam2, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                if (Utility.isConnectingToInternet(getActivity()))
                    validationsToAddress();
                else
                    Snackbar.make(scrollView, Constants.no_internet_connection, Snackbar.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    private void validationsToAddress() {
        String city = edtCity.getText().toString().trim();
        String locality = edtLocality.getText().toString().trim();
        String flatNum = edtHouseNum.getText().toString().trim();
        landMark = edtLandmark.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String mobileNum = edtPhoneNumber.getText().toString().trim();
        altNumber = edtAltNum.getText().toString().trim();
        String pincode = edtPinCode.getText().toString().trim();
        String state = edtState.getText().toString().trim();
        if (TextUtils.isEmpty(city)) {
            // tvErrorCity.setVisibility(View.VISIBLE);
            ToastUtils.displayToast("City filed is required", getActivity());
            return;
        }
        if (city.length() < 2) {
            ToastUtils.displayToast("City Name Minimum 2 Characters", getActivity());
            return;
        }
        if (TextUtils.isEmpty(locality)) {
            // tvErrorCity.setVisibility(View.VISIBLE);
            ToastUtils.displayToast("Locality or street filed is required", getActivity());
            return;
        }
        if (locality.length() < 2) {
            ToastUtils.displayToast("Locality or street Name Minimum 2", getActivity());
            return;
        }
        if (TextUtils.isEmpty(flatNum)) {
            // tvErrorCity.setVisibility(View.VISIBLE);
            ToastUtils.displayToast("Flat number filed is required", getActivity());
            return;
        }
        if (flatNum.length() < 2) {
            ToastUtils.displayToast("Flat no or Building Name Minimum 2 Characters", getActivity());
            return;
        }
        if (TextUtils.isEmpty(pincode)) {
            // tvErrorCity.setVisibility(View.VISIBLE);
            ToastUtils.displayToast("Pincode filed is required", getActivity());
            return;
        }
        if (pincode.length() != 6) {
            ToastUtils.displayToast("Please enter valid Pincode", getActivity());
            return;
        }
        if (TextUtils.isEmpty(state)) {
            // tvErrorCity.setVisibility(View.VISIBLE);
            ToastUtils.displayToast("State filed is required", getActivity());
            return;
        }
        if (TextUtils.isEmpty(name)) {
            // tvErrorCity.setVisibility(View.VISIBLE);
            ToastUtils.displayToast("Name filed is required", getActivity());
            return;
        }
        if (name.length() < 3 || name.length() > 30) {
            ToastUtils.displayToast("Name Minimum 3 Characters Maximum 30 Characters.", getActivity());
            return;
        }
        if (TextUtils.isEmpty(mobileNum)) {
            // tvErrorCity.setVisibility(View.VISIBLE);
            ToastUtils.displayToast("Mobile number filed is required", getActivity());
            return;
        }
        if (mobileNum.length() != 10) {
            ToastUtils.displayToast("Please enter valid Phone number", getActivity());
            return;
        }

        final HashMap<String, String> hashMap = new HashMap<>();
        try {

            hashMap.put("user_id", prefManager.getId());
            hashMap.put("address_id", addressId);

            hashMap.put("city", city);
            hashMap.put("street", locality);
            hashMap.put("flat_no", flatNum);
            hashMap.put("pincode", pincode);

            hashMap.put("state", state);
            hashMap.put("landmark", landMark);
            hashMap.put("phone_number", mobileNum);
            hashMap.put("alternate_phone_number", altNumber);
            hashMap.put("name", name);  //venkatesh.b@purpledot.in

           /*user_id:5
flat_no:001
street:sarjapur
landmark:hdfc bank
pincode:560100
city:Bangalore
state:Wes
name:Mitraaaa
alternate_phone_number:12
phone_number:9051652707
address_id:46*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Update address HASH MAP--" + hashMap);
        new APIRequest(getActivity()).postStringRequest(ApiConstants.UPDATE_ADDRESS, hashMap, new RequestCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Address Fragment--" + response);
                //  ToastUtils.displayToast("Result--"+response, OtpValidateActivity.this);
                Gson gson = new Gson();
                LoginResponse successResponsePojo = gson.fromJson(response, LoginResponse.class);
                if (successResponsePojo.getStatus().equalsIgnoreCase("1")) {
                    Log.e(TAG, "Update Inside Success--" + response);
                    ToastUtils.displayToast("Address updated successfully", getActivity());
                    scrollView.setVisibility(View.GONE);
                    rcvAddList.setVisibility(View.VISIBLE);
                    tvAddCount.setVisibility(View.VISIBLE);
                    Utility.hideKeyBoard(getActivity());
                    getAddressApiList();
                } else {
                    ToastUtils.displayToast(successResponsePojo.getMessage(), getActivity());
                }
            }

            @Override
            public void onFailed(String message) {
                ToastUtils.displayToast(Constants.something_went_wrong, getActivity());
                Log.d(TAG, "Update Address message--" + message + "--jon respons--" + hashMap);
            }
        });
    }
}
