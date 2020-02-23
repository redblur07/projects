import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;

import static com.kullar.myclass.*;

public class recovery{

	public static void main(String[] args) {
	
	try{
		File file=new File("/root/projects/java/recovery.log");  
		FileInputStream fis = new FileInputStream(file);
		int totalbits=0;
	
		print("hello");
		BufferedInputStream in = new BufferedInputStream(fis);
		byte[] bbuf = new byte[1024];
		int len;
		int fileLength = in.available();
		in.skip(fileLength - 1025);
		in.read(bbuf);
		//while ((len = in.read(bbuf)) != -1) {
			// process data here: bbuf[0] thru bbuf[len - 1]
		//	totalbits=totalbits+1;
		//}
	
		print("Total Bytes = " + bbuf.length);	
		
		File ofile=new File("/root/projects/java/recover.txt");
		FileOutputStream os = new FileOutputStream(ofile);
		os.write(bbuf);
		print("done");
}

	catch(Exception e){
		e.printStackTrace();
	}
	
	}

}
