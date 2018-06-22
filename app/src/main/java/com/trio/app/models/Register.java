package com.trio.app.models;

/**
 * Created by User on 08-Jan-18.
 */

public class Register {

    public boolean status;
    public String message;
    private registrationData registrationData;
    String last_id;

    public class registrationData
    {
        String name;
        String email;
        String phone;
        String username;
        String password;
        String opassword;
        String role;
        String reg_date;
        String ip;
        String otp;
    }
}
