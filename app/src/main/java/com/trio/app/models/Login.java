package com.trio.app.models;


import java.util.ArrayList;

/**
 * Created by User on 08-Jan-18.
 */

public class Login {

    public boolean status;
    public String message;
    public loginData data;
    public ArrayList<coinData> coin_data;

    public class loginData
    {
        public String id; public String name; public String email; public String phone; public String altno;
        public String dob; public String photo; public String role; public String coin;public String username;
        public String password;public String opassword;public String company;public String address;
        public String pan;public String aadhar; public String bankac;public String ifsc;public String bankname;
        public String otp; public String otp_verify;public String status;public String status_date;
        public String status_agent; public String ip; public String reg_date;
    }
    public class coinData
    {
        public String id; public String value; public String scommission; public String total_coin; public String avl_coin;
        public String remarks;
    }

//    public String LicenseNumber;
//    public String EmailID;
//    public String UserName;
//    public String UserPic;
//    public String contactnum;
//    public String DataVisibility;
//    public String UserType;


}
