package com.tomek.misiura.recognition;

public class RecontructionUnsuccessfulException extends Exception {
	Contour[][] ground;
	public RecontructionUnsuccessfulException(String message) {
		super(message);
	}
	
	public RecontructionUnsuccessfulException(String message, Contour[][] ground) {
		super(message);
		for(int i=0;i<5;i++){
        	for(int j=0;j<5;j++){
        		if(ground[i][j]!=null){
        			System.out.print(ground[i][j].center+"  ");
        		}
        		else
        			System.out.print("null  ");
        	}
        	System.out.println("");
        }
	}
}
