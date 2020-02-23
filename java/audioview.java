import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Canvas;
import javax.swing.JFrame;

import static com.kullar.myclass.*;

public class audioview {
	public static int w = 500, h = 500, sampleRate = 44100, channels = 2, bitRate = 16;
	public static Clip clip;
    public static void main(String[] args) {
	readFile();
	//createView();
}

public static void readFile(){
	try{
	File file=new File("/root/test.wav");  
	FileInputStream fis = new FileInputStream(file);
	int totalbits=0;
	
	int bytesPerSecond=sampleRate * channels * (bitRate / 8);
	int sec = 1;

	BufferedInputStream in = new BufferedInputStream(fis);
	byte[] bbuf = new byte[bytesPerSecond * sec];
	int len;
	while ((len = in.read(bbuf)) != -1) {
	// process data here: bbuf[0] thru bbuf[len - 1]
	totalbits=totalbits+1;
	if(totalbits == 15)
		break;
	}
	
	print("Total Bytes = " + bbuf.length);	
	//playBuff(bbuf);
	//readBuff(bbuf);
	
	int num = bbuf.length;
	short[] data=readBuff(bbuf, num/2);
	
	int[] ldata = getLeftSamples(data);
	int[] rldata = roundData(ldata, h);
	
	createView(rldata);
	
	playBuff(shortToByte(data));
	//playBuff(shortToByte(idata));

}

	catch(Exception e){
	}
}

public static void saveBytesToFile(byte[] data, int num, String name){
	try{
		File ofile=new File("/root/projects/java/" + name);
		FileOutputStream os = new FileOutputStream(ofile);
		os.write(data, 0, num);
		print(num + " bytes saved to /root/projects/java/"+name);
	}
	catch(Exception e){
	}
}

public static short[] invertAudio(short[] data){
	short[] ret = new short[data.length];
	int i = 0;
	while(i < data.length){
		ret[i] = (short)-data[i];
		i = i + 1;
	}
	return ret;
}

public static short[] changeVolume(short[] data, int num){
	short[] ret = new short[data.length];
	short val = (short)((num * 32676) / 100);
	int i = 0;
	while(i < data.length){
		
		if(data[i] < 0){
			if(data[i] - val > -32676)
			ret[i] = (short)(data[i] - val);
			else
			ret[i] = -32676;
		}
		else{
			if(data[i] + val < 32676)
			ret[i] = (short)(data[i] + val);
			else
			ret[i] = 32676;
		}
		
		i = i + 1;
	}
	return ret;
}

public static int[] roundData(int[] data, int h){
	int[] ret = new int[data.length];
	int i = 0;
	int num = h/2;
	int val=0;
	while(i < data.length){
		val = (data[i] * num) / 32676;
		val = num - val;
		ret[i] = val;
		i = i +1;
	}
	return ret;
}

public static int[] getLeftSamples(short[] data){
	int[] ret = new int[data.length/2];
	int i = 0;
	int ii = 0;
	while(i < data.length/2){
		ret[i] = data[ii];
		ii = ii + 2;
		i = i + 1;
	}
	return ret;
}

public static int[] getRightSamples(short[] data){
	int[] ret = new int[data.length/2];
	int i = 0;
	int ii = 1;
	while(i < data.length/2){
		ret[i] = data[ii];
		ii = ii + 2;
		i = i + 1;
	}
	return ret;
}

public static void printShortArray(short[] data, int num){
	int i = 0;
	while(i < num){
		print("Value = " + data[i]);
		i = i + 1;
	}
}

public static short[] readBuff(byte[] data, int num){
	if(num > data.length/2)
	num = data.length / 2;
	short[] ret = new short[num];
	int i = 0;
	int ii=0;
	short val = 0;
	while(i < num * 2){
		val = 0;
		val = (short)((val | data[i+1] << 8) | (data[i] & 0xff));
		//print("Bits = " + getBits(data[i]) + " " + getBits(data[i+1]));
		//print("Int bits = " + getBits(val));
		ret[ii] = val;
		ii=ii+1;
		i = i+2;
	}
	return ret;
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

public static String getBits(byte b) {
    StringBuilder sb = new StringBuilder();
    for (int i = 7; i >= 0; --i) {
        sb.append(b >>> i & 1);
    }
    return sb.toString();
}

public static String getBits(int b) {
    StringBuilder sb = new StringBuilder();
    for (int i = 31; i >= 0; --i) {
        sb.append(b >>> i & 1);
    }
    return sb.toString();
}

public static String getBits(short b) {
    StringBuilder sb = new StringBuilder();
    for (int i = 15; i >= 0; --i) {
        sb.append(b >>> i & 1);
    }
    return sb.toString();
}

public static void playBuff(byte[] data){
try{

	AudioFormat af = new AudioFormat(44100, 16, 2, true, false);
	DataLine.Info info = new DataLine.Info(Clip.class, af);
	//DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
	//SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
	
	LineListener listener = new LineListener(){
		public void update(LineEvent event){
			if(event.getType() == LineEvent.Type.START)
			print("Event Started");
		}
	};
	
	Clip clip = (Clip) AudioSystem.getLine(info);
	clip.addLineListener(listener);
	clip.open(af, data, 0, data.length);
	clip.start();
	Scanner k = new Scanner(System.in);
	k.nextInt();
	print(" " + clip.getMicrosecondLength() +" "+ clip.available());
	
	clip.addLineListener(listener);

	//line.open(af, 10000);
	//line.start();
	//line.write(data, 0, data.length);
	

	
	//print(" " + line.getFramePosition());

}
catch(Exception e){
	print(" " + e);
}
	
}


public static void createView(int[]... data) {
	
	mycanvas m=new mycanvas();
	m.setData(data);
	JFrame f=new JFrame();
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	f.add(m);  
	f.setSize(w, h);  
	//f.setLayout(null);  
	f.setVisible(true); 
	
	int i = 5;
	while(i < w){
	m.updateAudioLine(i);
	f.repaint();
	i = i + 5;
	}
	print(" " + m.audioLine);
}  

public static class mycanvas extends Canvas{
	int[][] data;
	boolean isDataSet = false;
	int audioLine = 1;
	
	public void paint(Graphics g){
		
	g.drawLine(audioLine, 0, audioLine, h);
	g.drawLine(0,h/2,w,h/2);
	
	if(isDataSet){
		int i = 0;
		for(int[] d : data){
		if(i == 1)
		g.setColor(Color.RED);
		createWave(g, d);
		i = i + 1;
		}
	}
	//g.drawString("Hello",40,40);  
	//setBackground(Color.WHITE);  
	//g.fillRect(130, 30,100, 80);  
	//g.drawOval(30,130,50, 60);  
	//setForeground(Color.RED);  
	//g.fillOval(130,130,50, 60);  
	//g.drawArc(30, 200, 40,50,90,60);  
	//g.fillArc(30, 130, 40,50,180,40);
	}
	
	public void setData(int[]... d){
		this.data = new int[d.length][];
		int i = 0;
		while(i < d.length){
			this.data[i]=d[i];
			i = i + 1;
		}
		isDataSet=true;
	}
	
	public void createWave(Graphics g, int[] data){
	int i = 0;
	int ii = 0;
	int xivalue = 1; // x increase by 1 point
	int skip_value = data.length / w;
	
	if(skip_value < 1){
		skip_value = 1;
		int varx = w / data.length; // to get how small data is compared to width
	
		if(varx > 1)
		xivalue	= varx; 
	}
	
	int x = 0;
	int lastval = h/2;
	System.out.println("printig "+data.length+" bytes while skipping " + skip_value);
	while(i < w & ii < data.length){
	g.drawLine(x, lastval, x+xivalue, data[ii]);
	lastval = data[ii];
	x=x+xivalue;
	i = i + 1;
	ii = ii + skip_value;
	}
	}
	
	public void updateAudioLine(int data){
		this.audioLine = data;
	}
	
}
    
}
