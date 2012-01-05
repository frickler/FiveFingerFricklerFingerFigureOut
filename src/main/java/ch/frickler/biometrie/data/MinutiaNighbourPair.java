package ch.frickler.biometrie.data;

public class MinutiaNighbourPair implements Comparable<MinutiaNighbourPair> {
	/**
	 * 
	 */
	private MinutiaPoint first;
	private MinutiaPoint second;

	private double ANGLETOLERANCE = 4;
	private double TYPETOLERANCE = 1;
	private double DISTANCETOLERANCE = 10;
	
	public MinutiaNighbourPair(MinutiaPoint first, MinutiaPoint second) {
		super();
		this.first = first;
		this.second = second;
	}



	@Override
	public String toString() {
		return "MinutiaNighbourPair [first=" + first + ", second=" + second
				+ "]";
	}

	public MinutiaPoint getFirst() {
		return first;
	}

	public MinutiaPoint getSecond() {
		return second;
	}

	public int getAngleInDegree() {
		return Math.abs(first.getAngle() - second.getAngle());
	}
	

	@Override
	public int compareTo(MinutiaNighbourPair mnp) {
		return this.getAngleInDegree() - mnp.getAngleInDegree();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		MinutiaNighbourPair other = (MinutiaNighbourPair)obj;
		
		if (first.getType() == other.first.getType() && second.getType() == other.second.getType()) {
			//make it more flexible, like += 3 degres...
			if (this.getAngleInDegree() == other.getAngleInDegree()) {
				return true;
			}
		}
		
		
		return false;
	}



	public void setFirst(MinutiaPoint first) {
		this.first = first;
	}



	public void setSecond(MinutiaPoint second) {
		this.second = second;
	}



	public double compareFuzzy(MinutiaNighbourPair obj, boolean bComparePosition) {
		if (obj == null)
			return 0;
		if (getClass() != obj.getClass())
			return 0;
		
		double angle = Math.abs(obj.getAngleInDegree() - this.getAngleInDegree());
		
		double anglescore = angle > ANGLETOLERANCE ? 0 : 1- angle/ANGLETOLERANCE;
		
		double typescore = 0;
				
		if (first.getType() == obj.first.getType() && second.getType() == obj.second.getType()) {
			typescore = 1;
		}else if(first.getType() == obj.second.getType() && second.getType() == obj.first.getType()) {
			typescore = 1;
		}else{
			//TODO this is not so a fuzzy value, any ideas?
			typescore = 0.3;
		}
		
		double[] weight = new double[] {0.9, 0.1};
		
		double[] scores = new double[] {anglescore,typescore};
		
		try {
			return FuzzyMachine.geneateFuzzyValue(weight,scores);
		} catch (FuzzyMachineException e) {
			e.printStackTrace();
			//TODO not so nice programmed
			return 0;
		}
		
	}
	

}