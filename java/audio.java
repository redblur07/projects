import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.net.SocketException; 

public class audio {

    public static void main(String[] args) {
        try {
            // select audio format parameters
            int bsize = 65000;
            AudioFormat af = new AudioFormat(44100, 16, 2, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            // generate some PCM data (a sine wave for simplicity)
            byte[] buffer = new byte[64];
            double step = Math.PI / buffer.length;
            double angle = Math.PI * 2;
            int i = buffer.length;
            while (i > 0) {
                double sine = Math.sin(angle);
                int sample = (int) Math.round(sine * 32767);
                buffer[--i] = (byte) (sample >> 8);
                buffer[--i] = (byte) sample;
                angle -= step;
            }
            
       // prepare audio output
       line.open(af, 10000);
       line.start();
            
            
            
        // Step 1 : Create a socket to listen at port 1234 
        DatagramSocket ds = new DatagramSocket(5001); 
        buffer = new byte[bsize]; 
  
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length); 
        ds.receive(packet);
        buffer = new byte[packet.getLength()];
        packet =  new DatagramPacket(buffer, buffer.length);
        
        
        int ii= 0;
        while (true) 
        { 
 
            ds.receive(packet);            
            line.write(buffer, 0, buffer.length);
  
        } 
            
            
            
            
            

          
            // output wave form repeatedly
            //for (int n=0; n<500; ++n) {
            //    line.write(buffer, 0, buffer.length);
            //}
            // shut down audio
            //line.drain();
            //line.stop();
            //line.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
  
    // A utility method to convert the byte array 
    // data into a string representation. 
    public static StringBuilder data(byte[] a) 
    { 
        if (a == null) 
            return null; 
        StringBuilder ret = new StringBuilder(); 
        int i = 0; 
        while (a[i] != 0) 
        { 
            ret.append((char) a[i]); 
            i++; 
        } 
        return ret; 
    } 
    
    
    
    

}
