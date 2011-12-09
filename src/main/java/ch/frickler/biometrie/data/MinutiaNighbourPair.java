package ch.frickler.biometrie.data;

public class MinutiaNighbourPair implements Comparable {
	private MinutiaPoint first;
	private MinutiaPoint second;

	public MinutiaNighbourPair(MinutiaPoint first, MinutiaPoint second) {
		super();
		this.first = first;
		this.second = second;
	}

	public int hashCode() {
		int hashFirst = first != null ? first.hashCode() : 0;
		int hashSecond = second != null ? second.hashCode() : 0;

		return (hashFirst + hashSecond) * hashSecond + hashFirst;
	}

	public boolean equals(Object other) {
		if (other instanceof MinutiaNighbourPair) {
			MinutiaNighbourPair otherPair = (MinutiaNighbourPair) other;
			return ((this.first == otherPair.first || (this.first != null
					&& otherPair.first != null && this.first
						.equals(otherPair.first))) && (this.second == otherPair.second || (this.second != null
					&& otherPair.second != null && this.second
						.equals(otherPair.second))));
		}

		return false;
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

	public int getAngle() {
		return first.getAngle() - second.getAngle();
	}

	@Override
	public int compareTo(Object arg1) {
		MinutiaNighbourPair mnp = (MinutiaNighbourPair) arg1;
		return this.getAngle() - mnp.getAngle();
	}

}