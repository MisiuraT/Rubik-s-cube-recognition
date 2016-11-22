import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Scalar;

public class Tester {

	private static String[] files = { "leftblue2.jpg" , "leftblue1.jpg", "rightblue1.jpg", "rightblue2.jpg","frontblue1.jpg","frontblue2.jpg",
			"kostka1.jpg","kostka2.jpg","pawel2.jpg", "pawel3.jpg","pawel4.jpg","pawel7.jpg"};

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		ImageRecognizer2[] recognizer = new ImageRecognizer2[files.length];

		for (int i = 0; i < files.length; i++) {
			recognizer[i] = new ImageRecognizer2();
			recognizer[i].readImage(files[i]);
		}
		
		PrintWriter out = null;
		
		try {
			 out = new PrintWriter("results.txt");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("data.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		int counter = 0;
		System.out.println("Starting loop");
		try {
			line = br.readLine();
			while (line != null) {
				counter++;
				System.out.println("line number"+counter);
				double[] data = toData(line);
				int result = 0;
				for (ImageRecognizer2 item : recognizer) {
					List<Contour> contourList = item.getContours(data);
					CubeContours cubeContours = new CubeContours(contourList);
					cubeContours.matchContours();
					int sum = cubeContours.frontSideContours.size()+cubeContours.leftSideContours.size()+cubeContours.rightSideContours.size();
					result += sum;
					out.println(line+" "+sum+" "+cubeContours.frontSideContours.size()+" "+cubeContours.leftSideContours.size()+" "+cubeContours.rightSideContours.size());
					item.reset();
				}
				out.println("\n wynik ogólny: "+result+"\n");
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("done loop");
		out.close();
	}

	private static double[] toData(String data) {
		double[] res = new double[6];
		String[] parts = data.split(" ");
		for (int i = 0; i < parts.length; i++) {
			res[i] = Double.parseDouble(parts[i]);
		}
		return res;
	}
}
