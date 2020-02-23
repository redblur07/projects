// Java program to illustrate Server side 
// Implementation using DatagramSocket 
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.InetAddress; 
import java.net.SocketException; 
  
public class udpserver 
{ 
    public static void main(String[] args) throws IOException 
    { 
		int bytesize = 65000;
		int numofpackets = 0;
        // Step 1 : Create a socket to listen at port 1234 
        //DatagramSocket socket = new DatagramSocket(5001);
        MulticastSocket msocket = new MulticastSocket(5001);
        byte[] data = new byte[bytesize]; 
        
        //InetAddress ip = InetAddress.getLocalHost();
        InetAddress group = InetAddress.getByName("228.5.6.7");
        msocket.joinGroup(group);
        //System.out.println("ip=" + ip.getHostAddress());
  
        DatagramPacket packet = null; 
        while (true) 
        { 
  
            // Step 2 : create a DatgramPacket to receive the data. 
            packet = new DatagramPacket(data, data.length); 
  
            // Step 3 : revieve the data in byte buffer. 
            msocket.receive(packet);
            numofpackets = numofpackets + 1;
  
            System.out.println("Client:-" + data(data));
            //printByte(data);
            //System.out.println("ip address = " + packet.getAddress() + " port = " + packet.getPort());
            System.out.println("Byte size = " + data.length + " Packet Size = " + packet.getLength() + " Count = " + numofpackets);
            
            //byte[] send = data(receive).toString().getBytes();
            //DatagramPacket packet = new DatagramPacket(sen, sen.length, DpReceive.getAddress(), DpReceive.getPort());
            //ds.send(packet);
  
            // Exit the server if the client sends "bye" 
            if (data(data).toString().equals("bye")) 
            { 
                System.out.println("Client sent bye.....EXITING"); 
                break; 
            } 
  
            // Clear the buffer after every message. 
            data = new byte[bytesize]; 
        } 
    }
    
    public static void printByte(byte[] data){
		String result = "";
		for(byte bit : data){
			result = result + " " + bit;
		}
		System.out.println(result);
	}
  
    // A utility method to convert the byte array 
    // data into a string representation. 
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
