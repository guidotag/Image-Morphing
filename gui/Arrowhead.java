import java.awt.Point;
import java.awt.Polygon;

public class Arrowhead extends Polygon{

	public Arrowhead(Segment shaft, int height, int base) {
		// Initially locate the heads points laying over the positive x axis.
		Point h1 = new Point(0, 0);
		Point h2 = new Point(height, base / 2);
		Point h3 = new Point(height, -base / 2);

		// Compute how much we need to rotate it to fit with the shaft.
		Point p1 = new Point(1, 0);
		Point p2 = new Point((int)(shaft.getFrom().getX() - shaft.getTo().getX()),
							(int)(shaft.getFrom().getY() - shaft.getTo().getY()));
		double r = Arrowhead.angle(p1, p2);

		h2 = Arrowhead.rotate(h2, r);
		h3 = Arrowhead.rotate(h3, r);

		// Finally we move the head points to its final location.
		h1.translate((int)shaft.getTo().getX(), (int)shaft.getTo().getY());
		h2.translate((int)shaft.getTo().getX(), (int)shaft.getTo().getY());
		h3.translate((int)shaft.getTo().getX(), (int)shaft.getTo().getY());

		super.addPoint((int)h1.getX(), (int)h1.getY());
		super.addPoint((int)h2.getX(), (int)h2.getY());
		super.addPoint((int)h3.getX(), (int)h3.getY());
	}

	// Directed angle between two points.
	public static double angle(Point p1, Point p2) {
		double r = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
		if (r < 0) {
			r += 2 * Math.PI;
		}
		return r;
	}

	// Counter clockwise rotation of a point.
	public static Point rotate(Point p, double r) {
		return new Point((int)(Math.cos(r) * p.getX() - Math.sin(r) * p.getY()),
						(int)(Math.sin(r) * p.getX() + Math.cos(r) * p.getY()));
	}
}