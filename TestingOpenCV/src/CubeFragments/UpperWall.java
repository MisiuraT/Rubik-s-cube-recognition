package CubeFragments;

import com.tomek.misiura.recognition.Contour;
import com.tomek.misiura.recognition.RecontructionUnsuccessfulException;

public abstract class UpperWall extends CubeWall {
	@Override
	protected boolean putContourIfFits(int[] place, Contour item, Contour[][] ground) throws RecontructionUnsuccessfulException {
		Contour compared = ground[place[0]][place[1]];
		int[] newPlace = null;
		if(isSameRow(item,compared)){
				newPlace = sameRow(item,compared,place);
		}else{
			if(isSameColumn(item,compared)){
				newPlace = sameColumn(item,compared,place);
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

	private int[] neighbour(Contour item, Contour compared, int[] place) {
		int[] newPlace = new int[2];
		if(isNeighbourNorth(item,compared)){
			newPlace[0] = place[0]-1;
			newPlace[1] = place[1];
			return newPlace;
		}
		if(isNeighbourSouth(item,compared)){
			newPlace[0] = place[0]+1;
			newPlace[1] = place[1];
			return newPlace;
		}
		if(isNeighbourWest(item,compared)){
			newPlace[0] = place[0];
			newPlace[1] = place[1]-1;
			return newPlace;
		}
		if(isNeighbourEast(item,compared)){
			newPlace[0] = place[0];
			newPlace[1] = place[1]+1;
			return newPlace;
		}
		return null;
	}

	private int[] sameRow(Contour item, Contour compared, int[] place) {
		int[] newPlace = new int[2];
		newPlace[1] = place[1];
		double epsilon = (item.east.y - item.north.y)* EPSILON_NEIGHBOUR;
		if(item.center.y>compared.center.y){
			if(isNeighbourSouth(item,compared))
				newPlace[0] = place[0]+1;
			else
				newPlace[0] = place[0]+2;
		}else{
			if(isNeighbourNorth(item,compared))
				newPlace[0] = place[0]-1;
			else
				newPlace[0] = place[0]-2;
		}
		return newPlace;
	}

	protected abstract boolean isNeighbourWest(Contour item, Contour compared);

	protected abstract boolean isNeighbourSouth(Contour item, Contour compared);

	protected abstract boolean isNeighbourNorth(Contour item, Contour compared);
	
	protected abstract boolean isNeighbourEast(Contour item, Contour compared);

	protected boolean isSameColumn(Contour item, Contour compared) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private int[] sameColumn(Contour item, Contour compared, int[] place) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean isSameRow(Contour item, Contour compared) {
		if(item.center.x<compared.north.x&&item.center.x>compared.south.x)
			if(compared.center.x<item.north.x&&compared.center.x>item.south.x)
				return true;
		return false;
	}



	

}
