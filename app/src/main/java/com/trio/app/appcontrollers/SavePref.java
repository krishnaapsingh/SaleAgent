package com.trio.app.appcontrollers;

import android.util.Log;

import com.trio.app.models.FxCoinValue;
import com.trio.app.models.Login;
import com.trio.app.models.BuyFxCoin;
import com.trio.app.models.LoginModel;
import com.trio.app.models.Register;
import com.trio.app.models.SaleConfirmData;
import com.orhanobut.hawk.Hawk;


/**
 * Created by User on 08-Jan-18.
 */

public class SavePref {
    static String coinValue, totalAvalCoin;


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

    public static Register getRegisterData() {
        return Hawk.get("Register_Data", null);
    }

    public static void setRegisterData(Register RegisterObj) {
        Hawk.put("Register_Data", RegisterObj);
    }

    public static void saveBuyCoinData(BuyFxCoin fxCoin) {
        Hawk.put("Buy_FxCoin_Data", fxCoin);
    }

    public static void saveCoinRate(String value) {
        Hawk.put("COIN_RATE", value);
    }

    public static void saveTotalCoin(String total_coin) {
        Hawk.put("TOTAL_COIN", total_coin);
    }

    public static String fetchCoinRate() {
        coinValue =  Hawk.get("COIN_RATE","1");
        return "Live: 1FX Coin = "+coinValue;
    }

    public static String fetchTotalAvailCoin() {
        totalAvalCoin = Hawk.get("TOTAL_AVAIL_COIN","500000000" );
        return "Total Available Coin = "+totalAvalCoin;

    }

    public static void saveSaleConfirmDataList(SaleConfirmData saleConfirmData) {
        Hawk.put("SALE_CONFIRM_DATA", saleConfirmData);
    }

    public static SaleConfirmData fetchSaleConfirmDataList() {
       return Hawk.get("SALE_CONFIRM_DATA", null);
    }

    public static void saveStatusCode(boolean b) {
        Hawk.put("STATUS_CODE",b);
    }

    public static boolean getStatusCode() {
        return Hawk.get("STATUS_CODE", false);
    }

    public static void saveRegisterData() {

    }

    public static void saveCoinValue(FxCoinValue fxCoinValue) {
        Hawk.put("FX_COIN_DATA", fxCoinValue);
    }

    public static FxCoinValue fetchCoinValue() {
       return Hawk.get("FX_COIN_DATA",null);
    }

    public static void saveImage(String fxCoinValue) {
        Hawk.put("ImageUser", fxCoinValue);
    }

    public static String fetchImage() {
        return Hawk.get("ImageUser",null);
    }

    public static void saveLoginData(Login login) {
        Hawk.put("LOGINDATA", login);
    }

    public static Login fetchLoginData() {
        return Hawk.get("LOGINDATA");
    }

    public static void saveUserName(String userName) {
        Hawk.put("Name", userName);
    }

    public static String getUserName( ) {
        return Hawk.get("Name", "");
    }

    public static void saveLicenseNumber(String licenseNumber) {
        Hawk.put("licenseNumber", licenseNumber);
    }

    public static String fetchLicenseNumber() {
        return Hawk.get("licenseNumber", "");
    }

    public static void saveEmailID(String emailID) {
        Hawk.put("emailID", emailID);
    }
    public static String fetchEmailID() {
        return Hawk.get("emailID", "");
    }

    public static void saveUserPic(String userPic) {
        Hawk.put("userPic", userPic);
    }
    public static String fetchUserPic( ) {
        return Hawk.get("userPic","");
    }

    public static void savecontactnum(String contactnum) {
        Hawk.put("contactnum", contactnum);
    }

    public static String fetchcontactnum() {
        return Hawk.get("contactnum", "");
    }

    public static void saveUserType(String userType) {
        Hawk.put("userType", userType);
    }

    public static String fetchUserType() {
        return Hawk.get("userType", "");
    }
}
