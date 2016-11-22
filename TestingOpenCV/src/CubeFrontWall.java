import java.util.ArrayList;
import java.util.List;

public class CubeFrontWall {
	Contour[][] wall = new Contour[3][3];
	public static final int EPSILON = 15;	
	public CubeFrontWall(){
		/*for(int i=0;i<3;i++)
			for(int j=0;j<3;j++){
				wall[i][j] = 
			}*/
	}
	
	public void reconstructWall(List<Contour> sortedList) throws CantRecognizeException{
		if(sortedList.size()!=9) 
			throw new CantRecognizeException("sortedList has not 9 elements");
		List<List<Contour>> levels = assignLevel(sortedList);
		int emptyLevels = 0;
		for(List<Contour> item : levels){
			if(item.size()==0)
				emptyLevels++;
		}
		boolean ready = emptyLevels==0;
		if(!ready){
			ready = reorderLevels(emptyLevels,levels);
		}
		
		
	}
	
	public boolean reorderLevels(int emptyLevels, List<List<Contour>> levels) {
		
		if(emptyLevels == 1){
			findOneGap(levels);
			return true;
		}
		
		int[] gaps  = new int[3];
		for(int i=0;i<levels.size()-emptyLevels-1;i++){
			List<Contour> lvl = levels.get(i);
			List<Contour> next = levels.get(i+1);
			double height = lvl.get(0).north.y - lvl.get(0).south.y;
			if(next.get(0).center.y-lvl.get(0).center.y<0.75*height){
				gaps[i] = 1;
			}else{
				
			}
		}
		return false;
	}

	private void findOneGap(List<List<Contour>> levels) {
		if(levels.get(0).size()==2){
			levels.add(0, levels.get(4));
			levels.remove(5);
			return;
		}
		if(levels.get(3).size()==2){
			return;
		}
		
		
		
	}

	public static List<List<Contour>> assignLevel(List<Contour> sortedList){
		System.out.println("sortedList.size = "+sortedList.size());
		List<List<Contour>> result = new ArrayList<List<Contour>>();
		for(int i=0;i<5;i++){
			result.add(new ArrayList<Contour>());
		}
		int currentLevel = 0,items = sortedList.size();
		for(int i=0;i<items;i++){
			if(currentLevel>4)
				break;
			Contour item = sortedList.get(i);
			result.get(currentLevel).add(item);
			while(i<items-1){				
				Contour compared = sortedList.get(i+1);
				if(Math.abs(item.center.y - compared.center.y)<EPSILON){
					result.get(currentLevel).add(compared);
					i++;
				}else{
					currentLevel++;
					break;
				}
			}
		}
		sortLevels(result);
		return result;
	}
	
	public static void sortLevels(List<List<Contour>> levels){
		for(List<Contour> list : levels){
			int items = list.size();
			for(int i=0;i<items-1;i++)
				for(int j=0;j<items-1;j++)
					if(list.get(j).center.x>list.get(j+1).center.x){
						list.add(j,list.get(j+1));
						list.remove(j+2);
					}
		}
				
			
			
		
	}
}
