package com.tomek.misiura.recognition;
import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Mat;

public class ColorsValues {
	public static final double[][] whiteBounds = { { 0, 0 }, { 0, 0 }, { 0, 0 } };
	public static final double[][] yellowBounds = { { 0, 0 }, { 0, 0 }, { 0, 0 } };
	public static final double[][] blueBounds = { { 0, 0 }, { 0, 0 }, { 0, 0 } };
	public static final double[][] greenBounds = { { 0, 0 }, { 0, 0 }, { 0, 0 } };
	public static final double[][] redBounds = { { 0, 0 }, { 0, 0 }, { 0, 0 } };
	public static final double[][] orangeBounds = { { 185, 215 }, { 95, 125 }, { 5, 35 } };

	public static double[][] colorsBoundaries(List<Contour> leftSideContours, Mat img) {
		double[][] boundaries = { { 255d, 0d }, { 255d, 0d }, { 255d, 0d } };
		for (Contour item : leftSideContours) {
			double[][] colors = item.getColors(img);
			System.out.println("Cube north : " + item.north.y);
			for (int i = 0; i < colors.length; i++) {
				System.out.println(colors[i][2] + ", " + colors[i][1] + ", " + colors[i][0]);
				for (int j = 0; j < 3; j++) {
					if (colors[i][2 - j] < boundaries[j][0])
						boundaries[j][0] = colors[i][2 - j];
					if (colors[i][2 - j] > boundaries[j][1])
						boundaries[j][1] = colors[i][2 - j];
				}
			}
			System.out.println("");
		}
		for (int i = 0; i < 3; i++) {
			System.out.println("Lower= " + boundaries[i][0] + ", Upper= " + boundaries[i][1]);
		}
		return boundaries;
	}
	public static List<double[]> boundingColors(List<Contour> leftSideContours, Mat img) {
		double[][] boundaries = { { 255d, 0d }, { 255d, 0d }, { 255d, 0d } };
		List<double[]> bounding = new LinkedList<double[]>();
		for (Contour item : leftSideContours) {
			double[][] colors = item.getColors(img);
			System.out.println("Cube north : " + item.north.y);
			for (int i = 0; i < colors.length; i++) {
				boolean flag = true;
				System.out.println(colors[i][0]*2 + ", " + colors[i][1]*100/255d + ", " + colors[i][2]*100/255d);
				for (int j = 0; j < 3; j++) {
					if (colors[i][2 - j] < boundaries[j][0]){
						boundaries[j][0] = colors[i][2 - j];
						if(flag){
							flag = false;
							bounding.add(colors[i]);
						}
					}
					if (colors[i][2 - j] > boundaries[j][1]){
						boundaries[j][1] = colors[i][2 - j];
						if(flag){
							flag = false;
							bounding.add(colors[i]);
						}
					}
				}
			}
			System.out.println("");
		}
		for (double[] item : bounding) {
			System.out.println("Red= "+item[2]+", Green= "+item[1]+", Blue= "+item[0]);
		}
		return bounding;
	}
	
	public static void colorsRatio(List<Contour> leftSideContours, Mat img) {
		for (Contour item : leftSideContours) {
			double[][] colors = item.getColors(img);
			System.out.println("Cube north : " + item.north.y);
			for (int i = 0; i < colors.length; i++) {		
				double red = colors[i][2]/colors[i][0]*100;
				
				double green = colors[i][1]/colors[i][0]*100;
				//29-56
				double blue = colors[i][0]/colors[i][0]*100;
				//15-40
				System.out.println(red + ", " + green + ", " + blue);
			}
			System.out.println("");
		}

	}
	public static Color whatColorIs(double[] hls) throws UnknownColorException{
		for(Color color : Color.values()){
			if(isColor(hls,color))
				return color;
		}
		throw new UnknownColorException("Can't match color to any known");
	}
	
	public static boolean isColor(double[] hls, Color color){
		switch(color){
		case BLUE: 		return isBlue(hls);
		case RED:		return isRed(hls);
		case YELLOW:	return isYellow(hls);
		case WHITE:		return isWhite(hls);
		case GREEN:		return isGreen(hls);
		case ORANGE:	return isOrange(hls);
		default:		return false;
		}
	}
	
	private static boolean isBlue(double[] hls){
				
		return false;
	}
	private static boolean isRed(double[] hls){
//		System.out.println(hls[0]+", "+hls[1]+", "+hls[2]);
		if(hls[0]<20&&hls[1]>30&&hls[1]<75&&hls[2]>40)
			return true;
		return false;
	}
	private static boolean isGreen(double[] rgb){
		return false;
	}
	private static boolean isOrange(double[] rgb){
		return false;
	}
	private static boolean isWhite(double[] rgb){
		return false;
	}
	private static boolean isYellow(double[] rgb){
		return false;
	}
}
