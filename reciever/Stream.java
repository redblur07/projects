package com.gill.reciever;

import android.app.Service;
//import android.app.ActivityManager;
//import android.app.ActivityManager.MemoryInfo;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.Notification.Builder;


import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;

//import android.annotation.Nullable;

public class Stream extends Service {
NotificationManager notificationManager;
static boolean running=false;
public static String stats="", DataStats="";

PendingIntent pending;

public static MyDatagramReciever reciever;
   

@Override
public IBinder onBind(Intent intent) {
return null;
}

@Override
public int onStartCommand(Intent intent, int flags, int startId) {

Bundle bundle = intent.getExtras();
int[] data = bundle.getIntArray("audio");

start(data);
running=true;

return START_STICKY;
}

@Override
public void onDestroy() {
super.onDestroy();
stop();
running=false;
}

public void start(int[] data){

reciever = new MyDatagramReciever();
reciever.setSettings(data);
reciever.start();

}

public void stop(){
if(reciever != null){
reciever.kill();
reciever = null;
}
}

public static String[] getSettings(){

if(reciever != null && reciever.running){
String[] data=reciever.getSettings();
return data;
}

else
return new String[]{"Empty"};
	
}

public void toast(String s){
Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
}

public void log(String text){
stats = stats + "\n" + text;
}


}
