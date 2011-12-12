package ch.frickler.biometrie.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultsetByNearest {

	List<MinutiaNighbourPair> minutiaList = new ArrayList<MinutiaNighbourPair>();

	public int getSize() {
		return minutiaList.size();
	}

	public MinutiaNighbourPair getPair(int i) {
		return minutiaList.get(i);
	}
	
	public List<MinutiaNighbourPair> getPairs() {
		return minutiaList;
	}

	public ResultsetByNearest(Template template) {

		for (MinutiaPoint mp : template.getMinutiaPoints()) {
			MinutiaPoint near = getNearestPoint(template, mp);
			MinutiaNighbourPair np = new MinutiaNighbourPair(mp, near);
			minutiaList.add(np);
		}

	}

	private MinutiaPoint getNearestPoint(Template t, MinutiaPoint current) {

		double minDistance = Integer.MAX_VALUE;
		MinutiaPoint nearEst = null;

		for (MinutiaPoint comp : t.getMinutiaPoints()) {
			double dist = Math.sqrt(Math.pow(
					current.getxCoord() - comp.getxCoord(), 2)
					+ Math.pow(current.getyCoord() - comp.getyCoord(), 2));

			if (dist < minDistance && !current.equals(comp)) {
				minDistance = dist;
				nearEst = comp;
				//System.out.println("new nearest dist " + dist);
			}
		}
		return nearEst;
	}

	public String toStringByAngle(boolean newline) {
		StringBuilder sb = new StringBuilder();
		
		Collections.sort(minutiaList);
		for (MinutiaNighbourPair np : minutiaList) {
			
			sb.append(np.getAngle() + " Typ 1: " + np.getFirst().getType() + " Typ 2: " + np.getSecond().getType() + (newline ? "\n" : ";"));
		}

		return sb.toString();
	}


}
