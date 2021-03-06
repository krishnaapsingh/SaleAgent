package com.trio.app.appcontrollers;

import com.orhanobut.hawk.Hawk;
import com.trio.app.models.LoginModel;
import com.trio.app.models.RouteModel;

import java.util.List;


/**
 * Created by User on 08-Jan-18.
 */

public class SavePref {

    public static LoginModel getLoginData() {
        return Hawk.get("Login_Data", null);
    }

    public static void setLoginData(LoginModel loginObj) {
        Hawk.put("Login_Data", loginObj);
    }



    public static void saveRoute(String route) {
        Hawk.put("routename", route);
    }

    public static String fetchRoute() {
        return Hawk.get("routename", "");
    }

    public static void saveShopAddress(String s) {
        Hawk.put("address", s);
    }

    public static String fetchShopAddress() {
        return Hawk.get("address", "");
    }

    public static void isUserLogin(boolean b) {
        Hawk.put("login", b);
    }

    public static boolean getIsUserLogin() {
        return Hawk.get("login", false);
    }


    public static void saveShopId(String shopid) {
        Hawk.put("shopid", shopid);
    }

    public static String fetchShopId() {
        return Hawk.get("shopid","");
    }

    public static void saveDistributorId(String id) {
        Hawk.put("disId", id);
    }
    public static String fetchDistributorId() {
        return Hawk.get("disId", null);
    }

    public static void saveShopName(String route) {
        Hawk.put("shopname", route);
    }

    public static String fetchShopName() {
        return Hawk.get("shopname", "");
    }


    public static void saveActName(String order) {
        Hawk.put("actnamee", order);
    }
    public static String getActName() {
        return Hawk.get("actnamee");
    }
}
