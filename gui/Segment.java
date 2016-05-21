import java.awt.Point;

public class Segment {
	private Point from;
	private Point to;

	// TODO - Maybe use firstEndpoint and secondEndpoint.
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