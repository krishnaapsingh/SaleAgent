package com.trio.app.models;

import java.util.List;

/**
 * Created by User on 15-Jan-18.
 */

public class TransferConfirmData {

    public boolean status;
    public String message;

    public List<InneTransferData> data = null;

    public class InneTransferData {

        public String trans_id;
        public String uid;
        public String username;
        public String name;
        public String tcoin;
        public String ucost;
        public String tcost;
        public String tid;
        public String tname;
        public String tusername;
        public String status;
        public String rdate;
        public String ip;
        public String confirmagent;
        public String confirmdate;

    }
}
