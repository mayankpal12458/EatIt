package com.example.dell.eatit.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by dell on 2/22/2018.
 */

public class Common {

    public static final String DELETE="Delete";
    public static final String PWD_KEY="Password";
    public static final String email_KEY="Email";

    public static String convertstatus(String status) {
        if(status.equals("0"))
            return "Shipped";
        else if(status.equals("1"))
            return "On The Way";
        else
            return "Placed";

    }

    public static boolean isConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null) {
                for (int i = 0; i < info.length; i++) {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
