package com.purpledot.fame.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.purpledot.fame.APIS.APIRequest;
import com.purpledot.fame.APIS.RequestCallBack;
import com.purpledot.fame.POJO.LoginResponse;
import com.purpledot.fame.POJO.address.AddressList;
import com.purpledot.fame.R;
import com.purpledot.fame.interfaces.ShowAddressFrgResponse;
import com.purpledot.fame.utils.ApiConstants;
import com.purpledot.fame.utils.Constants;
import com.purpledot.fame.utils.SharedPrefManager;
import com.purpledot.fame.utils.ToastUtils;
import com.purpledot.fame.utils.Utility;

import java.util.HashMap;
import java.util.List;

/**
 * Created by venkatesh on 11/10/2017.
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {
    private static final String TAG = AddressListAdapter.class.getSimpleName();
    Context context;
    List<AddressList> addressLists;
    SharedPrefManager prefManager;
    ShowAddressFrgResponse showAddressFrgResponse;

    public AddressListAdapter(Context context, List<AddressList> addressLists, ShowAddressFrgResponse showAddressFrgResponse) {
        this.context = context;
        this.addressLists = addressLists;
        this.showAddressFrgResponse = showAddressFrgResponse;

    }

    /*public AddressListAdapter(FragmentActivity activity, List<AddressList> addressList,  ShowAddressFrgResponse showAddressFrgResponse) {
        this.context = activity;
        this.addressLists = addressList;
        this.showAddressFrgResponse = showAddressFrgResponse;
    }*/

    @Override
    public AddressListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_address_row, parent, false);
        return new AddressListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AddressListAdapter.ViewHolder holder, final int position) {
        try {
            prefManager = new SharedPrefManager(context);
            final AddressList addressList = addressLists.get(position);
            if (addressList.getName() != null)
                holder.nameOfAddress.setText(addressList.getName());
            String address = addressList.getFlatNo() + ", " + addressList.getStreet() + ", "
                    + addressList.getCity() + ", " + addressList.getState() + "- " + addressList.getPincode();
            holder.tvAddress.setText(address);
            if (addressList.getPhoneNumber() != null)
                holder.tvAddressMobile.setText(addressList.getPhoneNumber());

            holder.imgEditAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddressFrgResponse.passAddressToActivity(addressLists.get(position), addressLists.size());
                    showAddressFrgResponse.editAddress(addressLists.get(position));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return addressLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameOfAddress;
        TextView tvAddress;
        TextView tvAddressMobile;
        ImageView imgEditAddress;
        ImageView imgDeleteAddress;


        public ViewHolder(View view) {
            super(view);
            nameOfAddress = view.findViewById(R.id.tv_address_name);
            tvAddress = view.findViewById(R.id.tv_address);
            tvAddressMobile = view.findViewById(R.id.tv_address_mobile_number);
            imgEditAddress = view.findViewById(R.id.img_edit_address);
            imgDeleteAddress = view.findViewById(R.id.img_delete_address);
            imgDeleteAddress.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_delete_address:
                    popupToRemoveAddress(getAdapterPosition()); // Show a popup to delete address or not
                    break;
               /* case R.id.img_edit_address:
                    ToastUtils.displayToast("Edit address", context);
                    showAddressFrgResponse.editAddress(addressLists.get(getAdapterPosition()));
                    break;*/
            }
        }
    }

    /**
     * Show a popup to delete address or not
     *
     * @param position
     */
    private void popupToRemoveAddress(final int position) {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Remove Address");
            alertDialog.setMessage("Are you sure you want to remove this address?");
            alertDialog.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (addressLists.size() == 0)
                        return;

                    Log.d(TAG, "User ID--" + prefManager.getId() + "Address ID--" + addressLists.get(position).getAddressId());
                    if (Utility.isConnectingToInternet(context))
                        deleteAddressAPI(prefManager.getId(), addressLists.get(position).getAddressId(), position); // Call API to delete the address in server
                    else
                        ToastUtils.displayToast(Constants.no_internet_connection, context);
                }
            });
            alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog alertDialog1 = alertDialog.create();

            if (alertDialog1.getWindow() != null && alertDialog1.getWindow().getAttributes() != null)
                alertDialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;

            alertDialog1.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteItemFromRecyclerView(int position) {
        try {
            Log.d(TAG, "Before Remove list size--" + addressLists.size());
            // Remove the item on remove/button click
            addressLists.remove(position);
            notifyItemRemoved(position);
            Log.d(TAG, "After Notify Remove list size--" + addressLists.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteAddressAPI(String userId, String addressId, final int position) {
        try {
            final HashMap<String, String> hashMap = new HashMap<>();
            try {
                hashMap.put("user_id", userId);
                hashMap.put("address_id", addressId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Delete  HASH MAP--" + hashMap);
            new APIRequest(context).postStringRequest(ApiConstants.DELETE_ADDRESS, hashMap, new RequestCallBack() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Delete Response--" + response);
                    Gson gson = new Gson();
                    LoginResponse successResponsePojo = gson.fromJson(response, LoginResponse.class);
                    if ("1".equalsIgnoreCase(successResponsePojo.getStatus())) {
                        Log.d(TAG, "Inside Delete Response--" + response);
                        ToastUtils.displayToast(successResponsePojo.getMessage(), context);
                        if (addressLists.size() > 0) {
                            deleteItemFromRecyclerView(position); // Update the adapter(UI) once address is delete in server.
                            showAddressFrgResponse.passAfterDeleteAddressCount(addressLists.size());
                        } else {
                            showAddressFrgResponse.passAfterDeleteAddressCount(0);
                        }
                    } else {
                        ToastUtils.displayToast(successResponsePojo.getMessage(), context);
                    }
                }

                @Override
                public void onFailed(String message) {
                    ToastUtils.displayToast(Constants.something_went_wrong, context);
                    Log.d(TAG, " Error message--" + message + "--jon respons--" + hashMap);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
