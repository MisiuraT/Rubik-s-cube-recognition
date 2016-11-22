package com.tomek.misiura.recognition;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Contour {
	public Point north;
	public Point south;
	public Point east;
	public Point west;
	public Point center;
	public Point[] corners = new Point[5];
	
	public static final double  LEFT_RIGHT_EPSILON = 0.3;
	public static final double FRONT_EPSILON = 0.4;
	public Contour(Point north,Point south, Point east, Point west){
		this.west = west;
		this.east = east;
		this.north = north;
		this.south = south;
		corners[0] = north; corners[1]= south; corners[2] = east; corners[3] = west; corners[4] = north;
		countCenter();
	}
	
	public void countCenter(){
		double x=0,y=0;
		x= ((north.x + south.x) /2 + (west.x + east.x)/2)/2;
		y= ((north.y + south.y) /2 + (west.y + east.y)/2)/2;
		center = new Point(x,y);
	}
	
	public Contour(){
		north = new Point(0,0);
		south = new Point(0,0);
		east = new Point(0,0);
		west = new Point(0,0);
		corners[0] = north; corners[1]= south; corners[2] = east; corners[3] = west; corners[4] = north;
		center = null;
	}
	
	public boolean isRightSide(){
		int epsilon = (int)(LEFT_RIGHT_EPSILON*(south.y-north.y));
		if(Math.abs(north.x-west.x)<=epsilon && Math.abs(south.x-east.x)<=epsilon){
			return true;
		}
		return false;
	}
	
	public boolean isLeftSide(){
		int epsilon = (int)(LEFT_RIGHT_EPSILON*(south.y-north.y));
		if(Math.abs(north.x-east.x)<=epsilon && Math.abs(south.x-west.x)<=epsilon){
			return true;
		}
		return false;
	}
	
	public boolean isFrontSide(){
		int epsilon = (int)(FRONT_EPSILON*(east.x-west.x));
		if(Math.abs(north.x-south.x)<=epsilon && Math.abs(west.y-east.y)<=epsilon){
			return true;
		}
		return false;
	}
	
	public double[][] getColors(Mat img){
		double[][] colors = new double[9][3];
		colors[0] = img.get((int)center.y, (int)center.x);	
		colors[1] = img.get((int)((center.y+north.y)/2),(int)((center.x+north.x)/2));
		colors[2] = img.get((int)((center.y+south.y)/2),(int)((center.x+south.x)/2));
		colors[3] = img.get((int)((center.y+east.y)/2),(int)((center.x+east.x)/2));
		colors[4] = img.get((int)((center.y+west.y)/2),(int)((center.x+west.x)/2));
		colors[5] = img.get((int)((north.y+east.y+center.y)/3),(int)((north.x+east.x+center.x)/3));
		colors[6] = img.get((int)((east.y+south.y+center.y)/3),(int)((east.x+south.x+center.x)/3));
		colors[7] = img.get((int)((west.y+south.y+center.y)/3),(int)((west.x+south.x+center.x)/3));
		colors[8] = img.get((int)((north.y+west.y+center.y)/3),(int)((north.x+west.x+center.x)/3));
		return colors;
	}
	
	
	public static Contour countContour(List<Point> list){
		
		
		
		return null;
	}
	
	public boolean hasCommonPoint(Contour compared){
		if(north.x==compared.north.x && north.y== compared.north.y){
			return true;
		}
		if(south.x==compared.south.x && south.y== compared.south.y){
			return true;
		}
		if(west.x==compared.west.x && west.y== compared.west.y){
			return true;
		}
		if(east.x==compared.east.x && east.y== compared.east.y){
			return true;
		}
		return false;
	}
	
	public boolean isSimilar(Contour compared){
		int tolerance = (int) ((south.y-north.y)*0.3);
	//	System.out.println(tolerance);
		int counter = 0;
		if(Math.abs(north.x-compared.north.x)< tolerance  && Math.abs(north.y-compared.north.y)<tolerance){
			counter++;
		}
		if(Math.abs(south.x-compared.south.x)< tolerance  && Math.abs(south.y-compared.south.y)<tolerance){
			counter++;
		}
		if(Math.abs(west.x-compared.west.x)< tolerance  && Math.abs(west.y-compared.west.y)<tolerance){
			counter++;
		}
		if(Math.abs(east.x-compared.east.x)< tolerance  && Math.abs(east.y-compared.east.y)<tolerance){
			counter++;
		}
//		System.out.println(counter>2);
		return counter>2;
	}
	
	public void drawContour(Mat img, Scalar color){
		Imgproc.line(img,north,east,color,2);
		Imgproc.line(img,east,south,color,2);	
		Imgproc.line(img, south, west, color,2);
		Imgproc.line(img, west, north, color,2);
	}
	
	@Override
	public String toString(){
		StringBuffer res = new StringBuffer("north: y="+north.y+", x="+north.x+"; ");
		res.append("south: y="+south.y+", x="+south.x+"; ");
		res.append("east: y="+east.y+", x="+east.x+"; ");
		res.append("west: y="+west.y+", x="+west.x+"; ");		
		return res.toString();
	}

	public double[] getColor(Mat img) {
		double[] color = new double[3];
		double[][] colors = new double[9][3];
		colors[0] = img.get((int)center.y, (int)center.x);	
		colors[1] = img.get((int)((center.y+north.y)/2),(int)((center.x+north.x)/2));
		colors[2] = img.get((int)((center.y+south.y)/2),(int)((center.x+south.x)/2));
		colors[3] = img.get((int)((center.y+east.y)/2),(int)((center.x+east.x)/2));
		colors[4] = img.get((int)((center.y+west.y)/2),(int)((center.x+west.x)/2));
		colors[5] = img.get((int)((north.y+east.y+center.y)/3),(int)((north.x+east.x+center.x)/3));
		colors[6] = img.get((int)((east.y+south.y+center.y)/3),(int)((east.x+south.x+center.x)/3));
		colors[7] = img.get((int)((west.y+south.y+center.y)/3),(int)((west.x+south.x+center.x)/3));
		colors[8] = img.get((int)((north.y+west.y+center.y)/3),(int)((north.x+west.x+center.x)/3));
		for(int i=0;i<9;i++){
			color[0]+= colors[i][0];
			color[1]+= colors[i][1];
			color[2]+= colors[i][2];
		}
		for(int i=0;i<3;i++)
			color[i] = color[i]/9;
		
		color[0] *=2;
		color[1] = color[1]*100/255;
		color[2] = color[2]*100/255;
		return color;
	}
	
	
	
}
