package CubeFragments;

import java.util.ArrayList;
import java.util.List;

import com.tomek.misiura.recognition.Contour;
import com.tomek.misiura.recognition.RecontructionUnsuccessfulException;

public abstract class CubeWall {
	protected static final double EPSILON_FRACTAL = 0.2;
	protected static final double EPSILON_NEIGHBOUR = 0.5;
	public Contour[][] wall = new Contour[3][3];
	List<int[]> takenPlaces = new ArrayList<int[]>();
	
	
	public void reconstructWall(List<Contour> list) throws RecontructionUnsuccessfulException{
		Contour[][] ground = new Contour[5][5];
		int[] taken = new int[2];
		taken[0] = 2;
		taken[1] = 2;
		takenPlaces.add(taken);
		ground[2][2] = list.remove(0);
		int counter = 0;
		while(list.size()>0){
			Contour tmp = list.remove(0);
			if(!findPlaceAndPut(tmp,ground))
				list.add(tmp);
			counter++;
			if(counter>20)
				break;
		}

		extractWall(ground);
		wasReconstructionSuccesfull();
	}
	
	private void extractWall(Contour[][] ground){
		int startY = 2;
		int startX = 2;
		for(int y=0;y<5;y++)
			for(int x=0;x<5;x++)
				if(ground[y][x]!=null){
					if(y<startY)
						startY = y;
					if(x<startX)
						startX = x;
				}				
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				wall[i][j] = ground[i+startY][j+startX];
			}
		}
	}
	
	private void wasReconstructionSuccesfull() throws RecontructionUnsuccessfulException {
		int nullsOne = 0;
		int nullsTwo = 0;
		for(int i=0;i<3;i=i+2){
			for(int j=0;j<3;j++){
				if(wall[i][j]==null)
					nullsOne++;
				if(wall[j][i]==null)
					nullsTwo++;
			}
			if(nullsOne==3||nullsTwo==3)
				throw new RecontructionUnsuccessfulException("Cannot recontruct wall from given contours");
			else{
				nullsOne = 0;
				nullsTwo = 0;
			}
			
		}
		
	}
	
	private boolean findPlaceAndPut(Contour item, Contour[][] ground) throws RecontructionUnsuccessfulException {
		int items = takenPlaces.size();
		for(int i=items-1;i>-1;i--){
			int[] place = takenPlaces.get(i);
			if(place[0]!=-1){
				if(putContourIfFits(place,item,ground)){
					return true;
				}
			}
		}
		return false;
	}
		
	protected abstract boolean putContourIfFits(int[] place, Contour item, Contour[][] ground) throws RecontructionUnsuccessfulException;
}
