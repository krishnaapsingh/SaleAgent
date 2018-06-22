package com.trio.app.models;

import java.util.List;

/**
 * Created by User on 09-Jan-18.
 */

public class UserAvailableFxCoin {

    public boolean status;
    public String message;
    public List<UserAvailableFxCoinData> data;


    public class UserAvailableFxCoinData{
        public String coin;
    }
}
