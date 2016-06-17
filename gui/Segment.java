import java.awt.Point;

public class Segment {
	private Point from;
	private Point to;

	public Segment(Point from, Point to) {
		this.from = from;
		this.to = to;
	}

	public Point getFrom() {
		return this.from;
	}

	public Point getTo() {
		return this.to;
	}
}