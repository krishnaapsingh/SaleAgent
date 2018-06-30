package com.trio.app.appcontrollers;

import com.orhanobut.hawk.Hawk;
import com.trio.app.models.LoginModel;
import com.trio.app.models.RouteModel;

import java.util.List;


/**
 * Created by User on 08-Jan-18.
 */

public class SavePref {

    public static void saveLogin(boolean b) {
        Hawk.put("LOGIN_STATUS", b);
    }

    public static boolean getLogin() {
        return Hawk.get("LOGIN_STATUS", false);
    }

    public static LoginModel getLoginData() {
        return Hawk.get("Login_Data", null);
    }

    public static void setLoginData(LoginModel loginObj) {
        Hawk.put("Login_Data", loginObj);
    }

    public static void saveMappedRoute(List<RouteModel> obj) {
        Hawk.put("Route", obj);
    }

    public static List<RouteModel> fetchMappedRoute() {
        return Hawk.get("Route");
    }

    public static void saveAchieveSale(String achievedAmount) {
        Hawk.put("achieveamt", achievedAmount);
    }

    public static String fetchAchieveSale() {
        return Hawk.get("achieveamt", "0");
    }


    public static void saveShopName(String shopname) {
        Hawk.put("shopname", shopname);
    }

    public static String fetchShopName() {
        return Hawk.get("shopname", "");
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
}
