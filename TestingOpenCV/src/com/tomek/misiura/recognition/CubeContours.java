package com.tomek.misiura.recognition;
import java.util.ArrayList;
import java.util.List;

public class CubeContours {
	public List<Contour> unsortedList;
	public List<Contour> leftSideContours;
	public List<Contour> rightSideContours;
	public List<Contour> frontSideContours;
	public List<Contour> trash;
	public CubeContours(List<Contour> unsortedList){
		this.unsortedList = unsortedList;		
	}
	
	public void splitContours(){
		trash = new ArrayList<Contour>();
		leftSideContours = new ArrayList<Contour>();
		rightSideContours = new ArrayList<Contour>();
		frontSideContours = new ArrayList<Contour>();
		for(Contour item : unsortedList){
			if(item.isFrontSide())
				frontSideContours.add(item);
			else
				if(item.isLeftSide())
					leftSideContours.add(item);
				else
					if(item.isRightSide())
						rightSideContours.add(item);
					else
						trash.add(item);						
		}
		deleteDuplicates(frontSideContours);
		deleteDuplicates(leftSideContours);
		deleteDuplicates(rightSideContours);
		sortRightList();
		sortLeftList();
		sortFrontList();
	}
	
	
	private List<Contour> sortList(List<Contour> toSort, ContourComparator comparator){
		deleteDuplicates(toSort);
		List<Contour> sortedList = new ArrayList<Contour>();
		int items = toSort.size();
		for(int i=0;i<items;i++){
			Contour theBest = toSort.get(0);
			for(Contour item : toSort){
				if(comparator.isBetter(item, theBest)){
					theBest = item;
				}
			}
			sortedList.add(theBest);
			toSort.remove(theBest);
		}
		return sortedList;
	}
	
	private void sortLeftList(){
		leftSideContours = sortList(leftSideContours,new leftListComparator());
	}
	
	
	private void sortFrontList(){		
		frontSideContours = sortList(frontSideContours, new frontListComparator());	
	}
	
	private void sortRightList(){
		rightSideContours = sortList(rightSideContours, new rightListComparator());
	}
	
	
	
	private void deleteDuplicates(List<Contour> contours){
		for(int i=0;i<contours.size()-1;i++){
			Contour inspected = contours.get(i);
			for(int j=i+1;j<contours.size();j++){
				Contour compared = contours.get(j);
				if(inspected.isSimilar(compared)){
					//TODO delete smaller ones
					contours.remove(compared);
					j--;
				}
			}
		}
	}
	
	public void deleteOuter(List<Contour> contours){
		for(int i = 0;i<contours.size();i++){
			Contour item = contours.get(i);
			double sum = 0;
			for(Contour compared : contours){
				double xDist = Math.abs(item.center.x - compared.center.x);
				double yDist = Math.abs(item.center.y - compared.center.y);
				double length = Math.sqrt(xDist*xDist+yDist*yDist);
				double diagonal = item.east.x - item.west.x;
				sum+=(length/diagonal);
			}
			if(sum/contours.size()>1.6){
				contours.remove(i);
				i--;
			}
		}
	}
	
	private class rightListComparator implements ContourComparator{
		@Override
		public boolean isBetter(Contour pretendend, Contour champion){
			return pretendend.center.x<champion.center.x;
		}
	}
	
	private class leftListComparator implements ContourComparator{
		@Override
		public boolean isBetter(Contour pretendend, Contour champion){
			return pretendend.center.x>champion.center.x;
		}
	}
	
	private class frontListComparator implements ContourComparator{
		@Override
		public boolean isBetter(Contour pretendend, Contour champion){
			return pretendend.center.y < champion.center.y;
		}
	}
	
	private interface ContourComparator{
		boolean isBetter(Contour pretendend, Contour champion);
	}
}
