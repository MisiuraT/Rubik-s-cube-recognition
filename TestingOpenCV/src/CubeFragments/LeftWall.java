package CubeFragments;

import com.tomek.misiura.recognition.Contour;

public class LeftWall extends UpperWall{

	@Override
	protected boolean isNeighbourWest(Contour item, Contour compared) {
		double epsilon = (item.south.y - item.north.y) * EPSILON_NEIGHBOUR;
		if(Math.abs(item.north.x - compared.west.x)<epsilon&& Math.abs(item.north.y - compared.west.y)<epsilon)
			if(Math.abs(item.east.x - compared.south.x)<epsilon&& Math.abs(item.east.y - compared.south.y)<epsilon)
				return true;
		return false;
	}

	@Override
	protected boolean isNeighbourEast(Contour item, Contour compared) {
		double epsilon = (item.south.y - item.north.y) * EPSILON_NEIGHBOUR;
		if(Math.abs(item.west.x - compared.north.x)<epsilon&& Math.abs(item.west.y - compared.north.y)<epsilon)
			if(Math.abs(item.south.x - compared.east.x)<epsilon&& Math.abs(item.south.y - compared.east.y)<epsilon)
				return true;
		return false;
	}
	
	@Override
	protected boolean isNeighbourSouth(Contour item, Contour compared) {
		double epsilon = (item.south.y - item.north.y) * EPSILON_NEIGHBOUR;
		if(Math.abs(item.north.x - compared.east.x)<epsilon&& Math.abs(item.north.y - compared.east.y)<epsilon)
			if(Math.abs(item.west.x - compared.south.x)<epsilon&& Math.abs(item.east.y - compared.south.y)<epsilon)
				return true;
		return false;
	}
	@Override
	protected boolean isNeighbourNorth(Contour item, Contour compared) {
		double epsilon = (item.south.y - item.north.y) * EPSILON_NEIGHBOUR;
		if(Math.abs(item.east.x - compared.north.x)<epsilon&& Math.abs(item.east.y - compared.north.y)<epsilon)
			if(Math.abs(item.south.x - compared.west.x)<epsilon&& Math.abs(item.south.y - compared.west.y)<epsilon)
				return true;
		return false;
	}

	
	
}
