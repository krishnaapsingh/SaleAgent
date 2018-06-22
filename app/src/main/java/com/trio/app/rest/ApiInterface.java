package com.trio.app.rest;


import com.trio.app.models.AchievedModel;
import com.trio.app.models.BuyFxCoin;
import com.trio.app.models.ChangePasswordModel;
import com.trio.app.models.ConfirmSaleByUser;
import com.trio.app.models.DistributorsModel;
import com.trio.app.models.ForgetPassword;
import com.trio.app.models.FxCoinValue;
import com.trio.app.models.Login;
import com.trio.app.models.LoginModel;
import com.trio.app.models.OtpVerification;
import com.trio.app.models.Register;
import com.trio.app.models.SaleConfirmData;
import com.trio.app.models.SaleFxCoin;
import com.trio.app.models.TargetModel;
import com.trio.app.models.TransferConfirmData;
import com.trio.app.models.TransferFxCoin;
import com.trio.app.models.UpdateFxCoin;
import com.trio.app.models.UpdateProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by User on 08-Jan-18.
 */


public interface ApiInterface {

    //    @FormUrlEncoded
//    @POST("Mobile/api.php?getuserdetail&&EMAILID&&PASSWORD")
    Call<Login> userLogIn(@Field("type") String type, @Field("username") String username, @Field("password") String password, @Field("pin") String pin);

    //        @GET
//        Call<Users> getUsers(@Url String url);
    @FormUrlEncoded
    @POST("api/example/register")
    Call<Register> userRegister(@Field("name") String name, @Field("email") String email,
                                @Field("phone") String phone, @Field("txtCaptcha") String captcha, @Field("ip") String Ip);

    @FormUrlEncoded
    @POST("api/example/verify_otp")
    Call<OtpVerification> userOtpVerification(@Field("otp") String otp);

    @FormUrlEncoded
    @POST("api/example/getCoinvalue")
    Call<FxCoinValue> fxCoinValue(@Field("uid") String uid);


    @FormUrlEncoded
    @POST("api/example/buy_coin")
    Call<BuyFxCoin> buyFxCoin(@Field("uid") String uid, @Field("username") String username, @Field("name") String name,
                              @Field("ncoin") String ncoin, @Field("uvalue") String uvalue, @Field("ip") String ip);

    @FormUrlEncoded
    @POST("api/example/sale_coin")
    Call<SaleFxCoin> saleFxCoin(@Field("uid") String uid, @Field("username") String username, @Field("name")
            String name, @Field("commission") String commission, @Field("scoin") String scoin,
                                @Field("uvalue") String uvalue, @Field("ip") String ip);

    @FormUrlEncoded
    @POST("api/example/transfer_coin")
    Call<TransferFxCoin> transferFxCoin(@Field("uid") String uid, @Field("email") String email, @Field("name") String name,
                                        @Field("tcoin") String tcoin, @Field("uvalue") String uvalue,
                                        @Field("phone") String phone, @Field("tname") String tname);

    @FormUrlEncoded
    @POST("api/example/update_tcoin")
    Call<UpdateFxCoin> updateFxCoin(@Field("otp") String otp, @Field("uid") String uid, @Field("username") String username,
                                    @Field("acoin") String acoin, @Field("tcoin") String tcoin, @Field("uvalue") String uvalue,
                                    @Field("ip") String ip, @Field("name") String name, @Field("tname") String tname);

    @FormUrlEncoded
    @POST("api/example/forgot_password")
    Call<ForgetPassword> forgetPAssword(@Field("phone") String mobile);

    @FormUrlEncoded
    @POST("api/example/update_profile")
    Call<UpdateProfile> updateProfile(@Field("uid") String uid, @Field("profile_pic") String profile, @Field("name") String name, @Field("email") String email,
                                      @Field("phone") String phone, @Field("altno") String altno, @Field("dob") String dob, @Field("address") String address,
                                      @Field("company") String company, @Field("pan") String pan, @Field("aadhar") String aadhar, @Field("bankac") String bankac,
                                      @Field("ifsc") String ifsc, @Field("bankname") String bankname);

    @FormUrlEncoded
    @POST("api/example/confirm_sale")
    Call<ConfirmSaleByUser> saleConfirmByUser(@Field("uid") String uid, @Field("scoin") String scoin,
                                              @Field("sale_id") String sale_id, @Field("approveagent") String approveagent);

    @FormUrlEncoded
    @POST("api/example/sale_coinlist")
    Call<SaleConfirmData> saleConfirmData(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("api/example/transfer_coinlist")
    Call<TransferConfirmData> transferConfirmData(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("api/example/change_password")
    Call<ChangePasswordModel> changePassword(@Field("uid") String uid, @Field("old_password") String oldPassword, @Field("new_password") String newPassword);

    @FormUrlEncoded
    @POST("api/example/generate_pin")
    Call<ChangePasswordModel> generateMpin(@Field("mob") String mob, @Field("username") String username, @Field("pin") String pin);

    @GET
    Call<LoginModel> loginUser(@Url String url);

    @GET
    Call<TargetModel> getUserTarget(@Url String url);

    @GET
    Call<AchievedModel> getUserAchieved(@Url String url);
    @GET
    Call<List<DistributorsModel>> getDistributors(@Url String url);
}


