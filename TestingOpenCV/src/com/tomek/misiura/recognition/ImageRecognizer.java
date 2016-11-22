package com.tomek.misiura.recognition;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

public class ImageRecognizer {

	Mat image;
	Mat imageGray;
	Mat imageBinary;
	Mat imageHLS = new Mat();
	
	public void  readImage(String path){
		File input = new File(path);
        BufferedImage bufferedImage = null;
        try {
        	bufferedImage = ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}              
        // putting image in Mat
        image = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(),CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        image.put(0, 0, data);        
        // imageIO reads image rotated, flipping it back 
        Core.transpose(image, image);
        Core.flip(image, image, 1);
	}
	
	public void saveImages(String pathStart){
		if(imageGray!=null)
			Imgcodecs.imwrite(pathStart+"Gray.jpg", imageGray);
		if(imageBinary!=null)
			Imgcodecs.imwrite(pathStart+"Binary.jpg", imageBinary);
		if(image!=null)
			Imgcodecs.imwrite(pathStart+"Image.jpg", image);
	}
	
	public void drawLines(List<Contour> list){
		for(Contour item : list){
			Imgproc.line(image,item.north,item.south,new Scalar(255,0,0));
			Imgproc.line(image,item.east,item.west,new Scalar(0,0,255));			
		}
	}
	
	public List<Contour> getContours(){ 
	//	resize(image,700);
		countGray();
   //     System.out.println("Pre");
        sharpen(imageGray); 
  //      System.out.println("Post");
        countBinary(imageGray);
        closeMorph(imageBinary,2,5);
         
        Imgproc.cvtColor(image, imageHLS, Imgproc.COLOR_BGR2HLS);
        
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();        
        Imgproc.findContours(imageBinary, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        
        List<Contour> squaresContours = new ArrayList<Contour>(); 
    //    Imgproc.drawContours(image, contours, -1, new Scalar(0,255,255),3); 
        for(int i =0;i<contours.size();i++){
        	MatOfPoint approx = approximateContour(contours.get(i)); 
        	if(approx.size().height==4)
      //  		System.out.println(Imgproc.contourArea(approx));
        	if(approx.size().height==4 && Imgproc.isContourConvex(approx)&&Imgproc.contourArea(approx)>20000){        	    
       	  		Imgproc.drawContours(image, contours, i, new Scalar(0,255,0),3);                		
        		squaresContours.add(contourFromList(approx));       		
        	}
        }                
    	return squaresContours;
	}
	
	
	private MatOfPoint approximateContour(MatOfPoint contour){
		MatOfPoint2f contour2f = new MatOfPoint2f();
    	MatOfPoint2f approx2f = new MatOfPoint2f();
    	MatOfPoint approx = new MatOfPoint();
    	
    	contour.convertTo(contour2f, CvType.CV_32FC2);
    	Imgproc.approxPolyDP(contour2f,approx2f, 30, true);    	
    	approx2f.convertTo(approx, CvType.CV_32S);    	
    	return approx;
	}
	
	private Contour contourFromList(MatOfPoint approx){
		List<Point> list = new ArrayList<Point>();         		
		Converters.Mat_to_vector_Point(approx, list);
		Contour result = new Contour(list.get(0),list.get(0),list.get(0),list.get(0));
		for(Point item : list){
			if(item.y>result.south.y)
				result.south = item;
			if(item.y<result.north.y)
				result.north = item;
		}
		list.remove(result.north);
		list.remove(result.south);
		if(list.get(0).x<list.get(1).x){
			result.east = list.get(1);
			result.west = list.get(0);
		}else{
			result.east = list.get(0);
			result.west = list.get(1);
		}
		result.countCenter();
		return result;
	}
	
	private void resize(Mat img, int width){
		double ratio = (double)img.height() / (double)img.width();
        int height = (int) (width*ratio);
        Size s = new Size(width,height);
        Imgproc.resize(img, img, s);
	}
	
	private void countBinary(Mat img){
		imageBinary = new Mat();
        Imgproc.Canny(img, imageBinary, 80, 110);
	}
	
	private void countGray(){
		imageGray=new Mat(image.height(), image.width(), CvType.CV_8UC3);        
        Imgproc.cvtColor(image, imageGray, Imgproc.COLOR_BGR2GRAY);
	}
	
	private void sharpen(Mat img){
		Mat blurred = new Mat();
        Imgproc.GaussianBlur(img, blurred, new Size(0,0), 20);
        Core.addWeighted(img, 1.5, blurred, -0.5, 0, img);
       Core.addWeighted(img, 1.5, blurred, -0.5, 0, img);
       Core.addWeighted(img, 1.5, blurred, -0.5, 0, img);
	}
	
	private void closeMorph(Mat img, int erosionSize, int times){
		Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*erosionSize + 1, 2*erosionSize+1));
		for(int i =0 ;i<times;i++){
        	Imgproc.dilate(img, img, element);
         	Imgproc.erode(img, img,element );
         }
	}
	
}
