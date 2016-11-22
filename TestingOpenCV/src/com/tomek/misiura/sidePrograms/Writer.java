package com.tomek.misiura.sidePrograms;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Writer {
	static int[] sigmas = {20,40,60};
	static int[] timesposts = {0,1,3};
	static int[] thresh1s = {10,25,40,60,80,110};
	static int[] thresh2s = {40,60,80,110,140,180};
	static int[] morphsizes = {2,4,7,10};
	static int[] morphtimess = {5,10};
	
	
	
	public static void main(String[] args){
		PrintWriter out = null;
		System.out.println("start");
		try {
			 out = new PrintWriter("data.txt");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		System.out.println("start");
		for(int sigma : sigmas){
			for(int timespost : timesposts){
				for(int thresh1 : thresh1s){
					for(int thresh2 : thresh2s){
						
						for(int morphsize : morphsizes){
							for(int morphtimes : morphtimess){
								if(thresh2>thresh1)
								out.println(sigma+" "+timespost+" "+thresh1+" "+thresh2+" "+morphsize+" "+morphtimes);
							}
						}
					}
				}
			}
		}
		out.close();
	}
}
