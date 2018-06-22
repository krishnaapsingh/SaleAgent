package com.trio.app.models;

/**
 * Created by User on 09-Jan-18.
 */

public class SaleFxCoin {

    public boolean status;
    public String message;
    public String last_id;

    public SaleFxCoinData data;

    public class SaleFxCoinData {

        public String uid;
        public String username;
        public String name;
        public String scoin;
        public String ucost;
        public String tcost;
        public String commission;
        public String tcommission;
        public String ip;
        public String rdate;
        public String status;
    }


}
