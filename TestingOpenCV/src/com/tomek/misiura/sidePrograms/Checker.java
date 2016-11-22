package com.tomek.misiura.sidePrograms;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Checker {
	public static void main(String[] args) {
		PrintWriter out = null;

		try {
			out = new PrintWriter("resultsSorted.txt");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("results.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String line = " ";
		int counter = 0; 
		while(line!=null){
			counter++;
			int mark = 0;
			System.out.println(counter);
			StringBuilder build = new StringBuilder();
			for(int i=0;i<12;i++){
				try {
					line  = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
					out.close();
					return;					
				}
				if(line==null) {
					out.close();
					return;
				}
				String[] parts = line.split(" ");
				if(Integer.parseInt(parts[7])>4&&Integer.parseInt(parts[8])>4&&Integer.parseInt(parts[9])>4)
					mark++;
				build.append(line+"\n");
			}
			try {
				line = br.readLine();
				line = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(line==null) {
				out.close();
				return;
			}
	//		System.out.println(line);
	//		String[] parts = line.split(" ");
			
	//		for(String item : parts){
	//			System.out.println(item);
	//		}

			if(mark>7){
				build.append(line);
				out.println(build.toString());
				out.println("mark = "+mark);
			}
			try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		out.close();
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
