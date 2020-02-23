package com.kullar;

public class myclass {
	
	public static void print (String... args) {
	String full="";
	for(String text : args){
		full = full + text;
	}	
	System.out.println(full);
	}
}

