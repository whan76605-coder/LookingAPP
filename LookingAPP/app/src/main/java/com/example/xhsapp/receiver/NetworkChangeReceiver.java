package com.example.xhsapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import com.example.xhsapp.utils.ToastUtils;

/**
 * 网络状态监听 Receiver
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static boolean isOnline = true;

    public static boolean isOnline() {
        return isOnline;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                Network network = cm.getActiveNetwork();
                NetworkCapabilities caps = cm.getNetworkCapabilities(network);
                boolean wasOnline = isOnline;
                isOnline = caps != null &&
                        (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                         caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                         caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));

                if (wasOnline && !isOnline) {
                    ToastUtils.show(context, "网络已断开");
                } else if (!wasOnline && isOnline) {
                    ToastUtils.show(context, "网络已连接");
                }
            }
        }
    }
}
