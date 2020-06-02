package com.example.wlanservice;

import android.app.NotificationChannel;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Binder;
//import android.util.Log;
import android.view.Display;
import android.widget.Toast;

public class WlanService extends Service {
    public WlanService() {
    }
    DisplayManager dm;
    WifiManager wifiManager;
    static boolean laeuft = false;
    private NotificationManager mNM;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.wlan_service_started;
    String channelId = "WlanServiceChannel";

    public class LocalBinder extends Binder {
        WlanService getService() {
            return null;
        }
    }

    @Override
    public void onCreate() {

        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        CharSequence channelName = "WlanServiceChannel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        mNM.createNotificationChannel(notificationChannel);

        dm = (DisplayManager) getApplicationContext().getSystemService(Context.DISPLAY_SERVICE);
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        showNotification();
        // Display a notification about us starting.  We put an icon in the status bar.
       // Log.i("WlanService", "Service onCreate() ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION, makeNotifcation());
        Toast toast = Toast.makeText(getApplicationContext(), "Wlan-Service Start", Toast.LENGTH_SHORT);
        toast.show();
        //Log.i("WlanService", "Service onStartCommand()");
        if (!laeuft) {
            laeuft = true;
            Thread thread = new Thread() {
                public void run() {
                    while (laeuft) {

                        if(!displayOn()){
                            if(wifiManager.isWifiEnabled()) {
                                wifiManager.setWifiEnabled(false);
                                //Log.i("WlanService","WLAN aus");
                                wifiManager.setWifiEnabled(true);
                                //Log.i("WlanService","WLAN an");
                            }
                        }

                        try {
                            sleep(270000);
                            //sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        laeuft = false;
        Toast toast = Toast.makeText(getApplicationContext(), "Wlan-Service Stopp", Toast.LENGTH_SHORT);
        toast.show();
        //Log.i("WlanService", "Service onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();


    private Notification makeNotifcation(){
        CharSequence text = getText(R.string.wlan_service_started);

        // The PendingIntent to launch the activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        return new Notification.Builder(this, channelId)
                .setSmallIcon(R.mipmap.logo_foreground)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(getText(R.string.service_name))  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

    }

    private void showNotification() {
        // Send the notification.
        mNM.notify(NOTIFICATION, makeNotifcation());
    }

    private boolean displayOn(){
        for (Display display : dm.getDisplays()) {
            if (display.getState() != Display.STATE_OFF) {
                return true;
            }
        }
        return false;
    }

}



