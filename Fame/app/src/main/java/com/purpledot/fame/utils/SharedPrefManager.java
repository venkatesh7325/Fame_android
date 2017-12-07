    package com.purpledot.fame.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Gino Osahon on 04/03/2017.
 */
public class SharedPrefManager {

    SharedPreferences sharedPreferences;
    Context mContext;
    // shared pref mode
    int PRIVATE_MODE = 0;
    // Shared preferences file name
    private static final String PREF_NAME = "famePref";
    SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }



    public void saveIsLoggedIn(Context context, Boolean isLoggedIn) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IS_LOGGED_IN", isLoggedIn);
        editor.commit();

    }

    public boolean getISLogged_IN() {
        //mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false);
    }

    public void saveToken(Context context, String toke) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ID_TOKEN", toke);
        editor.commit();
    }

    public void saveId(Context context, String id) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ID", id);
        editor.commit();
    }
    public String getId() {
        //mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("ID", "");
    }
    public void saveSuccess(Context context, boolean id) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("success", id);
        editor.apply();
    }

    public boolean getLoginSuccess() {
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getBoolean("success", false);
    }



    public String getUserToken() {
        //mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("ID_TOKEN", "");
    }

    public void saveEmail(Context context, String email) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EMAIL", email);
        editor.commit();
    }

    public String getUserEmail() {
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("EMAIL", null);
    }


    public void saveName(Context context, String name) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NAME", name);
        editor.commit();
    }

    public String getName() {
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("NAME", "");
    }

    public void savePhoto(Context context, String photo) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PHOTO", photo);
        editor.commit();
    }

    public void saveUUID(Context context, String uuid) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UUID", uuid);
        editor.commit();
    }



    public void saveMemberInvitationFamilyCode(Context context, String familyCode) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor.putString("InvitationCode", familyCode);
        editor.apply();
    }

    public String getMemberInvitationFamilyCode() {
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("InvitationCode", "");
    }
//    public  List<UsersDetails> getSelectedUser() {
//
//        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
//        String gson = sharedPreferences.getString("LONGITUDE", null);
//        return getList(gson);
//
//    }

//    private List<UsersDetails> getList(String gson) {
//        try {
//            JSONObject  jsonObject = new JSONObject(gson);
//            JsonArray jsonElements = new JsonArray(jsonObject);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    public String getNotificationSettings() {
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("notification", "");
    }


    public void saveNotificationSettings(Context context, String mode) {
        mContext = context;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        editor.putString("notification", mode);
        editor.apply();
    }

    public void saveCartCount(Context context,int count){
        mContext = context;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor.putInt("cart_count",count);
        editor.apply();
    }

    public int getCartCount(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getInt("cart_count",0);

    }
    public void saveSimSlotNum(Context context,int count){
        mContext = context;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor.putInt("sim_slot_num",count);
        editor.apply();
    }

    public int getSimSlotNumber(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getInt("sim_slot_num",0);

    }
    public String getClickedUser() {
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("clicknumber", "");
    }

    public void saveClickedUserNum(Context context, String num) {
        mContext = context;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor.putString("clicknumber", num);
        editor.apply();
    }


    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


}
