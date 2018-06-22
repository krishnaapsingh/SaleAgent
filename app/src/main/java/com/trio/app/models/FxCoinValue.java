package com.trio.app.models;

import java.util.ArrayList;

/**
 * Created by User on 09-Jan-18.
 */

public class FxCoinValue {
    public boolean status;
    public String message;

    public ArrayList<CoinValueData> data;
    public  ArrayList<UserCoin> user_coin;

    public class CoinValueData{

        public String id;
        public String value;
        public String scommission;
        public String total_coin;
        public String avl_coin;
        public String remarks;
    }

    public class UserCoin{
        public String coin;
    }
}
