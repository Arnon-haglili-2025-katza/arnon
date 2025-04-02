package com.arnonhaglili.myfirstaapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class MyWiFiBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (intent.getAction().equals(WifiManager.EXTRA_NETWORK_INFO)) {
            // Get the network info
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

            if (networkInfo != null && networkInfo.isConnected()) {
                // If Wi-Fi is connected, get connection info
                WifiInfo currentConnection = wifiManager.getConnectionInfo();
                if (currentConnection != null) {
                    String ssid = currentConnection.getSSID();
                    // Display the connected SSID
                    Toast.makeText(context, "Wi-Fi Connected: " + ssid, Toast.LENGTH_SHORT).show();
                    Log.d("BroadcastReceiver", "Wi-Fi Connected: " + ssid);
                }
            } else {
                // If Wi-Fi is disconnected
                Toast.makeText(context, "Wi-Fi Disconnected", Toast.LENGTH_SHORT).show();
                Log.d("BroadcastReceiver", "Wi-Fi Disconnected");
            }
        }
    }
}