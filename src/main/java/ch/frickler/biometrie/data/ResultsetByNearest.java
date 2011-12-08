package ch.frickler.biometrie.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ResultsetByNearest {

	List<MinutiaPoint> list = new ArrayList<MinutiaPoint>();
	
	
	public void addPair(MinutiaPoint a, MinutiaPoint b){
		
		list.add(a);
		list.add(b);
		
	}
	
	public int getSize(){
		return list.size() / 2;
	}
	
	public List<MinutiaPoint> getPair(int i){
		
		List<MinutiaPoint> retur = new ArrayList<MinutiaPoint>();
		
		retur.add(list.get(i*2));
		retur.add(list.get(i*2+1));
		
		return retur;
	}
	
	public ResultsetByNearest(Template template){
		
		for (MinutiaPoint mp : template.getMinutiaPoints()) {
		 MinutiaPoint near = getNearestPoint(template, mp);
		 	addPair(mp, near);
		}
		
	}
	
    public MinutiaPoint getNearestPoint(Template t,MinutiaPoint current){
    	
		double minDistance = Integer.MAX_VALUE;
		MinutiaPoint nearEst = null;
				
		for (MinutiaPoint comp :  t.getMinutiaPoints()) {
			//System.out.println("j "+j+" i "+i+" max "+max);
			double dist = Math.sqrt(Math.pow(current.getxCoord()-comp.getxCoord(),2)+Math.pow(current.getyCoord()-comp.getyCoord(),2));
			//System.out.println(dist);
			
			if(dist < minDistance && !current.equals(comp)){
				minDistance = dist;
				nearEst = comp;
				System.out.println("new nearest dist "+dist);
			}
		}
		return nearEst;
    }
    
    public String toStringByAngle(boolean newLine){
    	List<Integer> angles = new ArrayList<Integer>();
    	for (int i = 0; i < getSize();i++) {
    		List<MinutiaPoint> points = getPair(i);
    		angles.add(points.get(0).getAngle()-points.get(1).getAngle());
    		
    		System.out.println(points.get(0).getAngle()-points.get(1).getAngle());
		}
    	
    	Collections.sort(angles);
    	StringBuilder strBuild = new StringBuilder();
    	for (Integer angl : angles) {
    		strBuild.append(angl+ (newLine ? "\n" : ";"));
    		
    			
		}
    	
    	return strBuild.toString();
    }
	
}
