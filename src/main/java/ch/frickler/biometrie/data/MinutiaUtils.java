package ch.frickler.biometrie.data;

public class MinutiaUtils {

	public static MinutiaPoint getNearestPoint(Template t,
			MinutiaPoint current) {

		double minDistance = Integer.MAX_VALUE;
		MinutiaPoint nearest = null;

		for (MinutiaPoint comp : t.getMinutiaPoints()) {
			double dist = Math.sqrt(Math.pow(
					current.getxCoord() - comp.getxCoord(), 2)
					+ Math.pow(current.getyCoord() - comp.getyCoord(), 2));

			if (dist < minDistance && !current.equals(comp)) {
				minDistance = dist;
				nearest = comp;
				// System.out.println("new nearest dist " + dist);
			}
		}
		return nearest;
	}
}