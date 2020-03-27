import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;

import static com.kullar.myclass.*;

public class jiofi{

public static void main (String[] args) throws IOException {
	download("http://jiofi.local.html/");
	readFile();
}

public static void readFile(){
	try{
	String content = new String(Files.readAllBytes(Paths.get("page.html")));
	int pos = content.indexOf("batterylevel") + 21;
	String val = content.substring(pos, pos + 2);
	if(content.charAt(pos+2) != '%')
	val = val + content.charAt(pos+2);
	int value = Integer.parseInt(val);
	print("Found at = " + pos + " " + value);
	}
	catch(Exception e){
		print(e+"");
	}

}

public static void download(String urlString) throws IOException {
      URL url = new URL(urlString);
      try(
         BufferedReader reader =  new BufferedReader(new InputStreamReader(url.openStream()));         
         BufferedWriter writer = new BufferedWriter(new FileWriter("page.html"));       
      )  {      
         String line;
         while ((line = reader.readLine()) != null) {
            writer.write(line + "\n");
         }            
         System.out.println("Page downloaded.");
      } 
   }



}
