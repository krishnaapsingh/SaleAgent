package com.trio.app.models;

import java.util.List;

/**
 * Created by User on 15-Jan-18.
 */

public class SaleConfirmData {

    public boolean status;
    public String message;

    public List<InnerData> data;

    public class InnerData{
        public String sale_id, uid, username, name, scoin, ucost, commission, tcommission, tscost,
                status, rdate, ip, approveagent, approvedate;

    }
}
