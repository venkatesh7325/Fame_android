package com.purpledot.fame.fragments.dashboardFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.purpledot.fame.APIS.APIRequest;
import com.purpledot.fame.APIS.RequestCallBack;
import com.purpledot.fame.POJO.LoginResponse;
import com.purpledot.fame.POJO.OtpValidateModel;
import com.purpledot.fame.R;
import com.purpledot.fame.activites.DashBoardActivity;
import com.purpledot.fame.activites.loginModule.OtpValidateActivity;
import com.purpledot.fame.utils.ApiConstants;
import com.purpledot.fame.utils.Constants;
import com.purpledot.fame.utils.SharedPrefManager;
import com.purpledot.fame.utils.ToastUtils;
import com.purpledot.fame.utils.Utility;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNewAddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewAddressFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
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
    String altNumber;
    String landMark;
    TextView tvErrorCity;
    String TAG = AddNewAddressFragment.class.getSimpleName();
    SharedPrefManager prefManager;
    ScrollView scrollView;

    public AddNewAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewAddressFragment newInstance(String param1, String param2) {
        AddNewAddressFragment fragment = new AddNewAddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_address, container, false);
        bindAddressViews(view);
        prefManager = new SharedPrefManager(getActivity());
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    void bindAddressViews(View view) {
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
            ToastUtils.displayToast("City Name Minimum 2 Characters Maximum 20 Characters.", getActivity());
            return;
        }
        if (TextUtils.isEmpty(locality)) {
            // tvErrorCity.setVisibility(View.VISIBLE);
            ToastUtils.displayToast("Locality or street filed is required", getActivity());
            return;
        }
        if (locality.length() < 2) {
            ToastUtils.displayToast("Locality or street Name Minimum 2 Characters.", getActivity());
            return;
        }
        if (TextUtils.isEmpty(flatNum)) {
            // tvErrorCity.setVisibility(View.VISIBLE);
            ToastUtils.displayToast("Flat number filed is required", getActivity());
            return;
        }
        if (flatNum.length() < 2) {
            ToastUtils.displayToast("Flat no or Building Name Minimum 2 Characters.", getActivity());
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
        if (state.length() < 2) {
            ToastUtils.displayToast("State Minimum 2 Characters.", getActivity());
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

        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {

            hashMap.put("user_id", prefManager.getId());
            hashMap.put("city", city);
            hashMap.put("street", locality);
            hashMap.put("flat_no", flatNum);
            hashMap.put("pincode", pincode);

            hashMap.put("state", state);
            hashMap.put("landmark", landMark);
            hashMap.put("phone_number", mobileNum);
            hashMap.put("alternate_phone_number", altNumber);
            hashMap.put("name", name);  //venkatesh.b@purpledot.in
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Add new address HASH MAP--" + hashMap);
        new APIRequest(getActivity()).postStringRequest(ApiConstants.ADD_NEW_ADDRESS, hashMap, new RequestCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Add new Address Fragment--" + response);
                //  ToastUtils.displayToast("Result--"+response, OtpValidateActivity.this);
                Gson gson = new Gson();
                LoginResponse successResponsePojo = gson.fromJson(response, LoginResponse.class);
                if (successResponsePojo.getStatus().equalsIgnoreCase("1")) {
                    Log.e(TAG, "Inside Success--" + response);
                    getActivity().getSupportFragmentManager().popBackStack();
                    ToastUtils.displayToast(successResponsePojo.getMessage(), getActivity());
                    //   getActivity().onBackPressed();
                    Utility.hideKeyBoard(getActivity());

                } else {
                    ToastUtils.displayToast(successResponsePojo.getMessage(), getActivity());
                }
            }

            @Override
            public void onFailed(String message) {
                ToastUtils.displayToast(Constants.something_went_wrong, getActivity());
                Log.d(TAG, "Login Error message--" + message + "--jon respons--" + hashMap);
            }
        });
        // ToastUtils.displayToast("Address saved successfully", getActivity());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
