package com.gill.reciever;

import android.os.Environment;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.media.AudioTrack;
import android.media.AudioFormat;
import android.media.AudioManager;

import android.media.MediaFormat;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import java.nio.ByteBuffer;
import java.io.StringWriter;
import java.io.PrintWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileDescriptor;

public class MyDatagramReciever extends Thread {
public static boolean running = false;
public static String stats = "ok";
public static int times = 0;

MulticastSocket socket;
static String ip = "228.5.6.7";
static String serverip = "192.168.1.103";
static int port = 5001;
static int packet_length = 65000;

static int sample_rate = 44100;
static int channels = AudioFormat.CHANNEL_OUT_STEREO;
static int bit_depth = AudioFormat.ENCODING_PCM_16BIT;
static int buffsize = AudioTrack.getMinBufferSize(sample_rate, channels, bit_depth);
static int mulaw = 0;
AudioTrack audio;

public void run() {
log("run");
times = times + 1;

android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
running = true;

audio = new AudioTrack(AudioManager.STREAM_MUSIC, sample_rate, channels, bit_depth, buffsize, AudioTrack.MODE_STREAM);
audio.play();

try {

InetAddress groupip = InetAddress.getByName(ip);
socket = new MulticastSocket(port);
socket.joinGroup(groupip);

//startServer();

byte[] data = new byte[1050];
DatagramPacket packet = new DatagramPacket(data, data.length);
DatagramPacket outpacket;
//socket.receive(packet);
packet_length = 1050;//packet.getLength();
data = new byte[packet_length];
packet = new DatagramPacket(data, data.length);

// --------------------------------------------
	//log("Hey");
	String path = Environment.getExternalStorageDirectory() + "/etc/song.mp3";
	log(path);
	
	
	MediaExtractor extractor = new MediaExtractor();
	//File file = new File(path);
	//FileInputStream fis = null;

    //fis = new FileInputStream(file);
    //FileDescriptor fd = fis.getFD();
    extractor.setDataSource(path);

	//MediaFormat format = MediaFormat.createAudioFormat("audio/mpeg", 44100, 2);
	//String mime = format.getString(MediaFormat.KEY_MIME);
	
	MediaFormat format = extractor.getTrackFormat(0);
	String mime = format.getString(MediaFormat.KEY_MIME);
	extractor.selectTrack(0);
	//int sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
	//int c = extractor.getTrackCount();
	//log("Sample rate = " + sampleRate + " " + c);
	
	MediaCodec codec = MediaCodec.createDecoderByType(mime);
	codec.configure(format, null , null , 0);
	codec.start();
	
	ByteBuffer[] inputBuffers = codec.getInputBuffers();
	ByteBuffer[] outputBuffers = codec.getOutputBuffers();
	
	log("input buffer length = " + inputBuffers[0].remaining() + " " + inputBuffers[0].limit());
	log("output buffer length = " + outputBuffers[0].remaining() + " " + outputBuffers[0].limit());


	BufferInfo info = new BufferInfo();
	boolean inputbool = false;
	boolean outputbool = false;
	long timeout = 10000;
	byte[] tempBuffer = new byte[8192];
	
// ---------------------------------------------

if(mulaw == 1){
Decoder decoder = new G711UCodec();
short[] shortdata = new short[packet_length];
byte[] realdata;
while(running) {
socket.receive(packet);
decoder.decode(shortdata, data, data.length, 0);
realdata = shortToByte(shortdata);
audio.write(realdata, 0, realdata.length);
}
}
else{
	int sampleSize;
	while(running){
	data = null;
	data=new byte[1045];
	socket.receive(packet);
	sampleSize=0;
	int inputIndex = codec.dequeueInputBuffer(timeout);
	//log("index = " + inputIndex);
	//log(" r = " + inputBuffers[inputIndex].remaining());
	if(inputIndex >= 0){
	ByteBuffer buffer = inputBuffers[inputIndex];
	//log(" " + inputBuffers.length + " " + inputBuffers[0].remaining() + " " + buffer.remaining() + " " + buffer.position());
	sampleSize = extractor.readSampleData(buffer, 0);
	//log(" " + inputBuffers.length + " " + inputBuffers[0].remaining() + " " + buffer.remaining() + " " + buffer.position());
	//inputBuffers[inputIndex].put(data, 0, packet.getLength());
	//inputBuffers[inputIndex].flip();
	//buffer.put(data, 0, packet.getLength());
	//buffer.flip();
	//sampleSize = packet.getLength();
	//log("done");
	//log(" " + sampleSize + " " + inputBuffers[0].remaining() + " " + buffer.remaining() + " " + buffer.position() + " " + buffer.limit());
	//buffer.flip();
	//byte[] arr = new byte[buffer.remaining()];
	//buffer.get(arr);
	byte[] arr = new byte[inputBuffers[inputIndex].remaining()];
	//inputBuffers[inputIndex].get(arr);
	
	outpacket = new DatagramPacket(arr, arr.length, packet.getSocketAddress());
	socket.send(outpacket);
	
	log(" " + sampleSize + " " + inputBuffers[0].remaining() + " " + inputBuffers[inputIndex].position() + " " + inputBuffers[inputIndex].limit());
	

	//log("sample size = " + sampleSize + " remainig buffer = " + inputBuffers[inputIndex].remaining());
	
	
	//log("samples read = " + sampleSize);
	
	codec.queueInputBuffer(inputIndex, 0, sampleSize, 0, 0);
	extractor.advance();
	
	int outputIndex = codec.dequeueOutputBuffer(info, timeout);
	if(outputIndex > 0)
	//log(" out index = " + outputIndex + " pos="+outputBuffers[outputIndex].position() + " avail="+outputBuffers[outputIndex].remaining() + " limit=" + outputBuffers[outputIndex].limit());
				switch (outputIndex) {
				case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
					log("DecodeActivity INFO_OUTPUT_BUFFERS_CHANGED");
					outputBuffers = codec.getOutputBuffers();
					break;
					
				case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
					format = codec.getOutputFormat();
					log("DecodeActivity New format " + format);
					audio.setPlaybackRate(format.getInteger(MediaFormat.KEY_SAMPLE_RATE));
					
					break;
				case MediaCodec.INFO_TRY_AGAIN_LATER:
					log("DecodeActivity dequeueOutputBuffer timed out!");
					break;
					
				default:
					ByteBuffer outBuffer = outputBuffers[outputIndex];
					//log("DecodeActivity We can't use this buffer but render it due to the API limit " + outBuffer);
					
					final byte[] chunk = new byte[info.size];
					outBuffer.get(chunk); // Read the buffer all at once
					outBuffer.clear(); // ** MUST DO!!! OTHERWISE THE NEXT TIME YOU GET THIS SAME BUFFER BAD THINGS WILL HAPPEN
					
					audio.write(chunk, 0, chunk.length); // AudioTrack write data
					codec.releaseOutputBuffer(outputIndex, false);
					break;
				}
				//codec.releaseOutputBuffer(outputIndex, false);
	
	
	
	
	
	}
			
	
	
	//socket.receive(packet);
	//audio.write(data, 0, data.length);
	}
}
}
catch(Exception e){
	
	        StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            log(exceptionAsString);
	
}
//stopServer();

if (socket != null) {
socket.close();
audio.stop();
}
}

public static byte[] shortToByte(short[] data){
	byte[] ret = new byte[data.length * 2];
	int i = 0;
	int ii = 0;
	while(i < data.length * 2){
		ret[i] |= data[ii];
		ret[i+1] |= data[ii]>>8; 
		ii = ii + 1;
		i = i + 2;
	}
	return ret;
}

public void kill() {
if(socket != null)
//socket.close();
packet_length = 65000;
running = false;
}

public void startServer(){
  String command = "gst-launch-1.0 alsasrc device=''plughw:1,1'' ! mulawenc ! udpsink host=228.5.6.7 port=5001 &";
  byte data[] = command.getBytes();
  
  try{
  
InetAddress sip = InetAddress.getByName(serverip);
  DatagramPacket startpacket = new DatagramPacket(data, data.length, sip, 5001);
  socket.send(startpacket);
  }
  catch(Exception e){
  log(e.toString());
  }
}

public void stopServer(){
  String command = "killall gst-launch-1.0";
  byte data[] = command.getBytes();
  
  try{
  
InetAddress sip = InetAddress.getByName(serverip);
  DatagramPacket startpacket = new DatagramPacket(data, data.length, sip, 5001);
  socket.send(startpacket);
  }
  catch(Exception e){
  log(e.toString());
  }
}


public void setSettings(int[] data){
	sample_rate = data[0];
	channels = data[1];
	bit_depth = data[2];
	port = data[3];
	mulaw = data[4];
}

public String getStats() {
return stats;
}

public static String[] getSettings(){
	String[] data = new String[]{sample_rate+"", channels+"", bit_depth+"", buffsize+"", packet_length+"", ip, port+"", mulaw+"", times+" times", stats};
	
	return data;
}

public static void log(String text){
stats = stats + "\n" + text;
}

}

