import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;

import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 

import static com.kullar.myclass.*;

public class decoder{
static int buffersize = 10;
static int offset = 0;
static int length = 10;
static int totalbytes = 0;
static int totalFrameHeaders = 0;
static int totalpackets = 0;
static int oldpos = 0;
static int lastheaderpos = 0;
static boolean head = false;

static File file, sfile;
static FileInputStream fis, sfis;
static BufferedInputStream in;

static DatagramSocket socket;
static InetAddress ip;
static DatagramPacket packet, rpacket;
static byte buff[] = new byte[50], bbuf[] = buff;

public static void main(String[] args) throws IOException{
	buffersize = Integer.parseInt(args[0]);
	offset = Integer.parseInt(args[1]);
	length = Integer.parseInt(args[2]);
	totalbytes = buffersize * length;
	bbuf = new byte[buffersize];
	print(" off " + offset + " length " + length);
	start_udp(args[3]);
	readFile();
	readFileForHeader();
}

public static void start_udp(String ipadd){
	try{
		socket = new DatagramSocket(5002); 
		ip = InetAddress.getByName(ipadd);
		rpacket = new DatagramPacket(bbuf, bbuf.length);
	}
	catch(Exception e){
	}
}

public static void send_data(byte[] data){
	packet = new DatagramPacket(data, data.length, ip, 5001);
	//socket.send(packet);
}

public static void readFile(){
	try{
		file = new File("/root/song.mp3");  
		fis = new FileInputStream(file);
		in = new BufferedInputStream(fis);
		
		sfile = new File("/root/songg.mp3");
		sfis = new FileInputStream(sfile);
	}
	catch(Exception e){
	}
}

public static void sendPacket(int i, int len){
	try{
		//print(" " + off + " " + len);
		buff = new byte[len];
		//sfis.skip(off);
		if(sfis.read(buff) != -1 && totalFrameHeaders > 1){
			packet = new DatagramPacket(buff, len, ip, 5001);
			socket.send(packet);
			//printBytes(buff, 0, 10);
			socket.receive(rpacket);
			//printBytes(buff, 0, 20);
			//printBytes(bbuf, 0, 20);
			//print("-------------------");
			Thread.sleep(50);
			buff=null;
		}
		
	}
	catch(Exception e){
		print(e.toString());
	}

}

public static void readFileForHeader(){
	try{
		byte[] buffer = new byte[buffersize];
		int x = 0, len;
		while ((len = in.read(buffer)) != -1 && x < length) {
			getFrameHeaders(buffer);
			x = x + 1;
		}
		print("ok = " + x + " " + len);
	}
	catch(Exception e){
		print(e.toString());
	}
}

public static void getFrameHeaders(byte[] data){
try{
	int i = 0;
	int pos=0;
	while(i < data.length & i < totalbytes){
		if(0xFF == (data[i] & 0xFF)){
			//print("hey " + i + " " +  getBits(data[i]));
			head = true;
		}
		else{
			if(head == true){
				if((data[i] & 0xF0) == 0xF0){
					pos = oldpos + i - 1;
					//print("Frame header at = " + pos + " " + getBits(data[i]) + " " + getBits(data[i + 1]));
					//if(i > 0)
					sendPacket(i, pos - lastheaderpos);
					//printBytes()
					lastheaderpos = pos;
					totalFrameHeaders = totalFrameHeaders + 1;
				}
				//head = false;
			}
			head = false;
		}
		i = i + 1;
	}
	oldpos = oldpos + i;
	//print("Total Frame Headers = " + totalFrameHeaders);
}
catch(Exception e){
	print(e.toString());
}
}

public static void printBytes(byte[] data, int offset, int num){
	int bytesinrow = 5;
	int xx = 1;
	int x = offset;
	num = num + offset;
	StringBuilder str = new StringBuilder();
	while(x < num && x <= data.length){
		str.append(getBits(data[x]) + " ");
		if(xx == bytesinrow){
			print(str.toString());
			str.setLength(0);
			xx = 0;
		}
		xx = xx + 1;
		x = x + 1;
	}
	print(str.toString());
}

public static String getBits(byte b) {
    StringBuilder sb = new StringBuilder();
    for (int i = 7; i >= 0; --i) {
        sb.append(b >>> i & 1);
    }
    return sb.toString();
}

public static void readSong(){
	print("hello;");
}



}
