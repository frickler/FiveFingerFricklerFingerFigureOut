package ch.frickler.biometrie.data;

public class MinutiaNighbourPair implements Comparable<MinutiaNighbourPair> {
	/**
	 * 
	 */
	private MinutiaPoint first;
	private MinutiaPoint second;

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
	

}