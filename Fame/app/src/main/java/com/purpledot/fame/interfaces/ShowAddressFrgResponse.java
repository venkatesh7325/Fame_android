package com.purpledot.fame.interfaces;

import com.purpledot.fame.POJO.address.AddressList;

/**
 * Created by venkatesh on 11/10/2017.
 */

public interface ShowAddressFrgResponse {
    void passAddressToActivity(AddressList addressList,int count);
    void passAfterDeleteAddressCount(int count);
    void editAddress(AddressList addressList);
}
