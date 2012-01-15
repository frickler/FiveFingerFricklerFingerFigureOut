package ch.frickler.biometrie.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultsetByNearest {

	List<MinutiaNighbourPair> minutiaPairList = new ArrayList<MinutiaNighbourPair>();

	public int getSize() {
		return minutiaPairList.size();
	}

	public MinutiaNighbourPair getPair(int i) {
		return minutiaPairList.get(i);
	}
	
	public List<MinutiaNighbourPair> getPairs() {
		return minutiaPairList;
	}

	public ResultsetByNearest(Template template) {

		for (MinutiaPoint mp : template.getMinutiaPoints()) {
			MinutiaPoint near = MinutiaUtils.getNearestPoint(template, mp);
			MinutiaNighbourPair np = new MinutiaNighbourPair(mp, near);
			minutiaPairList.add(np);
		}

	}



	public String toStringByAngle(boolean newline) {
		StringBuilder sb = new StringBuilder();
		
		Collections.sort(minutiaPairList);
		for (MinutiaNighbourPair np : minutiaPairList) {
			
			sb.append(np.getAngleInDegree() + " Typ 1: " + np.getFirst().getType() + " Typ 2: " + np.getSecond().getType() + (newline ? "\n" : ";"));
		}

		return sb.toString();
	}


}