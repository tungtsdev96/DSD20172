package com.hust.edu.dsd;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;
import com.hust.edu.dsd.model.staff.Staff;

import java.io.UnsupportedEncodingException;

/**
 * Created by tungts on 11/23/2017.
 */

public class AccountUtil {

    private final String STAFF = "user";
    private final String ACCESSMAP = "map";
    private final String VOLUNTEER = "volunteer";
    private final String IS_LOGIN = "is_login";
    private static final String TOKEN = "token";
    private static AccountUtil accountUtil;

    private AccountUtil() {
    }

    public static SharedPreferences PREF;
    private static SharedPreferences.Editor editor;

    public static AccountUtil getInstance(Context context) {
        if (accountUtil == null)
            accountUtil = new AccountUtil();
        if (PREF == null) {
            PREF = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        }
        return accountUtil;
    }

    //check currentLocation
    public void setIsAccessMap(boolean isAccessMap){
        editor = PREF.edit();
        editor.putBoolean(ACCESSMAP, isAccessMap);
        editor.apply();
    }

    public boolean isAccessMap(){
        return PREF.getBoolean(ACCESSMAP, false);
    }

    //check volunteer
    public void setVolunteer(boolean isVolunteer){
        editor = PREF.edit();
        editor.putBoolean(VOLUNTEER, isVolunteer);
        editor.apply();
    }

    public boolean isVolunteer(){
        return PREF.getBoolean(VOLUNTEER, false);
    }

    //staff
    public void setUsers(Staff staff) {
        editor = PREF.edit();
        editor.putString(STAFF, new Gson().toJson(staff));
        editor.putBoolean(IS_LOGIN, true);
        editor.apply();
    }

    public boolean isLogin() {
        return PREF.getBoolean(IS_LOGIN, false);
    }

    public Staff getStaff() {
        return new Gson().fromJson(PREF.getString(STAFF, ""), Staff.class);
    }

    public void setToken(String token){
        editor = PREF.edit();
        editor.putString(TOKEN,token);
        editor.apply();
    }

    public static String getToken(){
        return PREF.getString(TOKEN,null);
    }

    public static String getAuthorization() {
        if (getToken() == null) return null;
        try {
            String text = getToken();
            return Base64.encodeToString(text.getBytes("UTF-8"), Base64.NO_WRAP | Base64.URL_SAFE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void logout() {
        editor = PREF.edit();
        editor.clear();
        editor.apply();
    }

    private static Staff c;

    public static Staff fakeUsers() {
        if (c == null) {
            c = new Staff();

        }
        return c;
    }
}
