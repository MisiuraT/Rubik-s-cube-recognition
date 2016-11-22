package CubeFragments;

import com.tomek.misiura.recognition.Contour;
import com.tomek.misiura.recognition.RecontructionUnsuccessfulException;

public class FrontWall extends CubeWall {

	@Override
	protected boolean putContourIfFits(int[] place, Contour item, Contour[][] ground) throws RecontructionUnsuccessfulException {
		Contour compared = ground[place[0]][place[1]];
		int[] newPlace = null;
		if(isSameHeight(item,compared)){
			newPlace = sameHeight(item,compared,place);
		}else{
			if(isSameWidth(item,compared)){
				newPlace = sameWidth(item,compared,place);
			}else{
				newPlace = neighbour(item,compared,place);				
			}
		}
				
		if(newPlace!=null){
			if(newPlace[0]>4||newPlace[1]>4)
				throw new RecontructionUnsuccessfulException("Indicies exceeding array, probably too much contours");
			ground[newPlace[0]][newPlace[1]] = item;
			takenPlaces.add(newPlace);
			return true;
		}
		return false;
	}

	private int[] sameWidth(Contour item, Contour compared, int[] place) {
		int[] result = new int[2];
		int epsilon = (int) ((item.south.y - item.north.y) * EPSILON_NEIGHBOUR);
		if ( item.north.y - compared.south.y < epsilon) {
			result[0] = place[0] + 1;
			result[1] = place[1] + 1;
		} else {
			result[0] = place[0] + 2;
			result[1] = place[1] + 2;
		}
		return result;
	}
	private int[] neighbour(Contour item, Contour compared,int[] place) {
		int[] newPlace = new int[2]; 
		if(isNeighbourWest(item,compared)){
			newPlace[0] = place[0]+1;
			newPlace[1] = place[1];
			return newPlace;
		}
		if(isNeighbourEast(item,compared)){
			newPlace[0] = place[0];
			newPlace[1] = place[1]+1;
			return newPlace;
		}
		return null;
	}
	
	private boolean isNeighbourWest(Contour item, Contour compared) {
		int epsilon = (int) ((item.south.y - item.north.y)*EPSILON_NEIGHBOUR);
		if(Math.abs(item.north.x - compared.west.x)<epsilon&&Math.abs(item.north.y - compared.west.y)<epsilon){
			if(Math.abs(item.east.x - compared.south.x)<epsilon&&Math.abs(item.east.y - compared.south.y)<epsilon)
				return true;
		}		
		return false;
	}
	private boolean isNeighbourEast(Contour item, Contour compared) {
		int epsilon = (int) ((item.south.y - item.north.y)*EPSILON_NEIGHBOUR);
		if(Math.abs(item.north.x - compared.east.x)<epsilon&&Math.abs(item.north.y - compared.east.y)<epsilon){
			if(Math.abs(item.west.x - compared.south.x)<epsilon&&Math.abs(item.west.y - compared.south.y)<epsilon)
				return true;
		}		
		return false;
	}
	private boolean isSameWidth(Contour item, Contour compared) {
		int epsilon = (int) ((item.east.y - item.west.y)*EPSILON_FRACTAL);
		if(Math.abs(item.center.x - compared.center.x)<epsilon){
			return true;
		}
		return false;
	}
	private boolean isSameHeight(Contour item, Contour compared) {
		int epsilon = (int) ((item.south.y - item.north.y)*EPSILON_FRACTAL);
		if(Math.abs(item.center.y - compared.center.y)<epsilon){
			return true;
		}
		return false;
	}
	
	private int[] sameHeight(Contour item, Contour compared, int[] place) {
		int[] result = new int[2];		
		int epsilon = (int) ((item.east.x - item.west.x) * EPSILON_NEIGHBOUR);
		if (item.center.x > compared.center.x) {
			if (item.west.x - compared.east.x < epsilon) {
				result[0] = place[0] - 1;
				result[1] = place[1] + 1;
			} else {
				result[0] = place[0] - 2;
				result[1] = place[1] + 2;
			}
		} else {
			if (compared.west.x - item.east.x < epsilon) {
				result[0] = place[0] + 1;
				result[1] = place[1] - 1;
			} else {
				result[0] = place[0] + 2;
				result[1] = place[1] - 2;
			}
		}
		return result;
	}
	
}
