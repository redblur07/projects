// Java program to illustrate Client side 
// Implementation using DatagramSocket 
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.util.Scanner; 
  
public class udpclient 
{ 
    public static void main(String args[]) throws IOException 
    { 
        Scanner sc = new Scanner(System.in); 
  
        // Step 1:Create the socket object for 
        // carrying the data. 
        DatagramSocket ds = new DatagramSocket(5002); 
  
        InetAddress ip = InetAddress.getByName(args[0]);
        byte buf[] = new byte[50], bbuf[] = buf; 
  
        // loop while user not enters "bye" 
        while (true) 
        { 
            String inp = sc.nextLine(); 
  
            // convert the String input into the byte array. 
            buf = inp.getBytes(); 
            
            // Step 2 : Create the datagramPacket for sending 
            // the data. 
            DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, 5001); 
            
            System.out.println("Byte size = " + buf.length + " Packet Size = " + packet.getLength());
            
  
            // Step 3 : invoke the send call to actually send 
            // the data. 
            ds.send(packet); 
            //DatagramPacket ppacket = new DatagramPacket(bbuf, bbuf.length); 
            //ds.receive(ppacket);
            //System.out.println("message = " + data(bbuf) + "Byte size = " + bbuf.length + " Packet Size = " + ppacket.getLength());
           
            
  
            // break the loop if user enters "bye" 
            if (inp.equals("bye")) 
                break; 
        } 
    } 
    
        public static StringBuilder data(byte[] a) 
    { 
        if (a == null) 
            return null; 
        StringBuilder ret = new StringBuilder();
        int i = 0; 
        while (i < a.length && a[i] != 0) 
        { 
            ret.append((char) a[i]); 
            i++; 
        } 
        return ret; 
    } 
} 
