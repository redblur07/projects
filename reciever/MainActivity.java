package com.gill.reciever;

import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.content.Intent;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.media.AudioTrack;
import android.media.AudioFormat;
import android.media.AudioManager;

import com.gill.reciever.R;

public class MainActivity extends Activity {
CustomLayout layout;
MyText text, localip;
MyEditText mulawbox, commandbox;
MyButton startButton, stopButton, updateButton;
String stats = "Application SStarted";
String command = "gst-launch-1.0 alsasrc device=''plughw:1,1'' ! mulawenc ! udpsink host=228.5.6.7 port=5001 &";

DatagramSocket socket;
DatagramPacket packet;
InetAddress ip;
byte data[];
String clientip = "192.168.1.103";
Thread thread;

int sample_rate = 44100;
int channels = 12; // 12 for stereo and 4 for mono;
int bit_depth = 2; // 2 for 16 and 3 for 8;
int port = 5001;
int mulaw = 0;

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	createLayout();

}

public void startPressed(){
startStream();
}

public void stopPressed(){
stopStream();
text.setText(Stream.stats);
}

public void updatePressed(){
String data = "Reciever service not started";

if(Stream.running){
data = "";
for(String d : Stream.getSettings())
data = data + d + "\n";
}

text.setText(data);
}

public void startStream(){
if(Stream.running != true){
Bundle bundle = new Bundle();
bundle.putIntArray("audio", getAudioSettings());
Intent intent = new Intent(this, Stream.class);
intent.putExtras(bundle);
startService(intent);
}
}
public void stopStream(){
if(Stream.running == true)
stopService(new Intent(this, Stream.class));
}

public void createLayout(){

layout = new CustomLayout(this);
setContentView(layout);

mulawbox = new MyEditText(this);
mulawbox.setText("0");
layout.addView(mulawbox);

commandbox = new MyEditText(this);
commandbox.setText(command);
layout.addView(commandbox);

startButton = new MyButton(this, "Start"){
  @Override
  public void onClicked(){
    startPressed();
  }
};
layout.addView(startButton);

stopButton = new MyButton(this, "Stop"){
  @Override
  public void onClicked(){
    stopPressed();
  }
};
layout.addView(stopButton);

updateButton = new MyButton(this, "Update"){
  @Override
  public void onClicked(){
    updatePressed();
  }
};

layout.addView(updateButton);

text = new MyText(this, stats);
layout.addView(text);

}

public int[] getAudioSettings(){
mulaw = Integer.parseInt(mulawbox.getText().toString());
	int[] data = new int[]{sample_rate, channels, bit_depth, port, mulaw};
	return data;
}

public void start_udp(){
try{
socket = new DatagramSocket(5003);
ip = InetAddress.getByName(clientip);
}
catch(Exception e){
toast(e.toString());
}
}

public void start_server(){

data = commandbox.getText().toString().getBytes();

thread = new Thread(new Runnable() {
@Override
public void run() {
try  {
packet = new DatagramPacket(data, data.length, ip, 5001);
socket.send(packet);
} catch (Exception e) {
toast(e.toString());
}
}
});

thread.start(); 

}




public void toast(String s){
Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
}

public void log(String text){
stats = "\n" + text;
this.text.setText(stats);
}

}
