package com.trio.app.models;

/**
 * Created by User on 09-Jan-18.
 */

public class BuyFxCoin {

    public boolean status;
    public String message;
    public String last_id;

    public BuyFxCoinData data;

    public class BuyFxCoinData
    {

        public String uid;
        public String username;
        public String name;
        public String ncoin;
        public String ucost;
        public String tcost;
        public String ip;
        public String rdate;
        public String status;


    }

}
