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
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

public class Classic {
	public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ImageRecognizer recognizer = new ImageRecognizer();
        recognizer.readImage("images//leftblue2.jpg");
        System.out.println("Pre");
        List<Contour> contourList = recognizer.getContours();
        System.out.println("post");
        for(Contour item : contourList){
        	item.drawContour(recognizer.image, new Scalar(0,255,255));
        }
        CubeContours cubeContours = new CubeContours(contourList);
        cubeContours.matchContours();
        cubeContours.sortFrontList();
        
    /*    cubeContours.frontSideContours.remove(8);
        cubeContours.frontSideContours.remove(7);
        cubeContours.frontSideContours.remove(6);
        cubeContours.frontSideContours.remove(5);
        cubeContours.frontSideContours.remove(2);
        cubeContours.frontSideContours.remove(1);
        cubeContours.frontSideContours.remove(0);*/
        
        //cubeContours.deleteOuter(cubeContours.frontSideContours);
        
        for(Contour item : cubeContours.frontSideContours){
        	System.out.print(item.center);
        }
        
        FrontWall wall = new FrontWall();
        
        try {
			wall.reconstructWall(cubeContours.frontSideContours);
		} catch (RecontructionUnsuccessfulException e1) {
			System.out.println(e1);
			e1.printStackTrace();
			return;
		}
       
        List<Contour> lista = new ArrayList<Contour>();
        System.out.println("----");
        Contour[][] reconstructed = wall.wall;
        for(int i=0;i<3;i++){
        	for(int j=0;j<3;j++){
        		if(reconstructed[i][j]!=null){
        			System.out.print(reconstructed[i][j].center+"  ");
        			lista.add(reconstructed[i][j]);
        		}
        		else
        			System.out.print("null  ");
        	}
        	System.out.println("");
        }
        		
  //      ColorsRGBValues.boundingColors(cubeContours.rightSideContours, recognizer.imageHLS);
        
        
        
   /*     List<List<Contour>> listlev = CubeFrontWall.assignLevel(cubeContours.frontSideContours);
        int counter = -1;
        for(List<Contour> level : listlev){
        	counter++;    	
        	for(Contour item : level){
        		System.out.print(item.center);
        		item.drawContour(recognizer.image, new Scalar(255,0,0));
        	}
        	System.out.println("");
        	
        	if(level.size()!=0)
        		level.get(level.size()-1).drawContour(recognizer.image, new Scalar(255,0,0));
        }*/
   //     System.out.println(color[2]+", "+color[1]+", "+color[0]);
        
        List<Contour> list= cubeContours.frontSideContours;
 //       for(Contour item : leftList){
 //       	System.out.println(item);
 //       }
        for(Contour item : lista){
        	Color color = null;
			try {
				color = ColorsValues.whatColorIs(item.getColor(recognizer.imageHLS));
			} catch (UnknownColorException e) {
				
			}
        	System.out.println(color);
        }
        recognizer.drawLines(lista);
        recognizer.saveImages("kostka");      
	}
		
	public static void HoughLinesP(Mat mat, Mat gray){
		
		Imgproc.GaussianBlur(gray, gray, new Size(11,11), 1.5,1.5);
               
        Mat binary = new Mat();
        Imgproc.Canny(gray, binary, 0, 20, 3,true);
        Mat lines = new Mat();
        int threshold = 80;
        int minLineSize = 50;
        int lineGap = 30;
        
		Imgproc.HoughLinesP(binary, lines, 1, Math.PI/180, threshold, minLineSize, lineGap);
        
 //       System.out.println(lines.rows());
        
        for (int x = 0; x < lines.rows(); x++) 
        {
              double[] vec = lines.get(x, 0);
              double x1 = vec[0], 
                     y1 = vec[1],
                     x2 = vec[2],
                     y2 = vec[3];
              Point start = new Point(x1, y1);
              Point end = new Point(x2, y2);

              Imgproc.line(mat, start, end,new Scalar(255,0,0), 2);
              
        }
        
        Imgcodecs.imwrite("kostakGray.jpg", gray);
        Imgcodecs.imwrite("kostakCanny.jpg", binary);
        Imgcodecs.imwrite("kostakLines.jpg", mat);
	}
	
	public static void HoughLines(Mat mat, Mat gray){
		Imgproc.GaussianBlur(gray, gray, new Size(45,45), 1.5,1.5);
        
        Mat binary = new Mat();
        Imgproc.Canny(gray, binary, 0, 30, 3,true);
		Mat lines = new Mat();
        int threshold = 230;
        
        Imgproc.HoughLines(binary, lines, 1, Math.PI/180, threshold);
        
        for (int i = 0; i < lines.rows(); i++){
			double data[] = lines.get(i, 0);
			double rho = data[0];
			double theta = data[1];
			double angle = Math.toDegrees(theta);			
			double cosTheta = Math.cos(theta);
			double sinTheta = Math.sin(theta);
			double x0 = cosTheta * rho;
			double y0 = sinTheta * rho;
			Point pt1 = new Point(x0 + 10000 * (-sinTheta), y0 + 10000 * cosTheta);
			Point pt2 = new Point(x0 - 10000 * (-sinTheta), y0 - 10000 * cosTheta);
			Imgproc.line(mat, pt1, pt2, new Scalar(200, 0, 0), 4);
			
		} 
        
        Imgcodecs.imwrite("kostakGray.jpg", gray);
        Imgcodecs.imwrite("kostakCanny.jpg", binary);
        Imgcodecs.imwrite("kostakLines.jpg", mat);

        
	}
	
	
}



















































