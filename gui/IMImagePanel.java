import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class IMImagePanel extends ImagePanel {
	private static final int SEGMENT_ENDPOINT_RADIUS = 4;
	private static final int SEGMENT_ARROWHEAD_BASE = 8;
	private static final int SEGMENT_ARROWHEAD_HEIGHT = 10;

	private int width;
	private int height;

	private Point cursor;
	private Segment currentSegment;
	private ArrayList<Segment> segments;

	private IMCoupleCoordinator coordinator;
	private IMImagePanelMouseListener listener;

	public IMImagePanel(int width, int height) {
		super(width, height);

		this.width = width;
		this.height = height;

		this.cursor = null;
		this.currentSegment = null;
		this.segments = new ArrayList<Segment>();

		this.coordinator = null;
		this.listener = new IMImagePanelMouseListener(this);
	}

	public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLUE);
        for (Segment s: this.segments) {
        	IMImagePanel.drawSegment(g, s);
        }

        if (this.currentSegment != null) {
        	g.setColor(Color.RED);
        	IMImagePanel.drawSegment(g, this.currentSegment);
        }

        if (this.cursor != null) {
        	g.setColor(Color.ORANGE);
        	IMImagePanel.drawPoint(g, this.cursor);
        }
    }

	public void startSegment(Point endpoint) {
		this.currentSegment = new Segment(endpoint, endpoint);
        this.repaint();
    }

    public void updateCurrentSegment(Point endpoint) {
    	this.currentSegment = new Segment(this.currentSegment.getFrom(), endpoint);
    	this.truncateCurrentSegment();
    	this.repaint();
    }

    public void endCurrentSegment(Point endpoint) {
    	this.currentSegment = new Segment(this.currentSegment.getFrom(), endpoint);
    	this.truncateCurrentSegment();
    	this.segments.add(this.currentSegment);
    	this.currentSegment = null;
    	this.repaint();

    	this.coordinator.notifySegmentFinished(this);
    }

    public void drawCursor(Point cursor) {
    	this.cursor = cursor;
    	this.repaint();
    }

    public void removeCursor() {
    	this.cursor = null;
    	this.repaint();
    }

    public void enableMouseListener() {
    	this.addMouseListener(this.listener);
    	this.addMouseMotionListener(this.listener);
    }

    public void disableMouseListener() {
    	this.removeMouseListener(this.listener);
    	this.removeMouseMotionListener(this.listener);
    }

    public void setCoordinator(IMCoupleCoordinator coordinator) {
    	this.coordinator = coordinator;
    }

    public ArrayList<Segment> getSegments() {
    	ArrayList<Segment> scaledSegments = new ArrayList<Segment>();

    	for (Segment s: this.segments) {
    		scaledSegments.add(this.scaleSegment(s));
    	}

    	return scaledSegments;
    }

   	private void truncateCurrentSegment() {
   		// TODO
   	}

   	private Segment scaleSegment(Segment segment) {
    	double xScale = super.getRealWidth() / (double)this.width;
    	double yScale = super.getRealHeight() / (double)this.height;

    	Point fromScaled = new Point((int)(segment.getFrom().getX() * xScale),
    										(int)(segment.getFrom().getY() * yScale));
    	Point toScaled = new Point((int)(segment.getTo().getX() * xScale),
    										(int)(segment.getTo().getY() * yScale));
    	return new Segment(fromScaled, toScaled);
    }

    private static void drawPoint(Graphics g, Point point) {
    	g.fillOval((int)point.getX() - IMImagePanel.SEGMENT_ENDPOINT_RADIUS,
    		(int)point.getY() - IMImagePanel.SEGMENT_ENDPOINT_RADIUS,
    		2 * IMImagePanel.SEGMENT_ENDPOINT_RADIUS,
    		2 * IMImagePanel.SEGMENT_ENDPOINT_RADIUS);
    }

    private static void drawArrowhead(Graphics g, Segment segment) {
    	g.fillPolygon(new Arrowhead(segment, IMImagePanel.SEGMENT_ARROWHEAD_HEIGHT, IMImagePanel.SEGMENT_ARROWHEAD_BASE));
    }

    private static void drawSegment(Graphics g, Segment segment) {
    	g.drawLine((int)segment.getFrom().getX(),
    		(int)segment.getFrom().getY(),
    		(int)segment.getTo().getX(),
    		(int)segment.getTo().getY());
    	IMImagePanel.drawPoint(g, segment.getFrom());
    	IMImagePanel.drawArrowhead(g, segment);
    }

    private class IMImagePanelMouseListener implements MouseListener, MouseMotionListener {
		private IMImagePanel imagePanel;
		private boolean leftClicked;

		public IMImagePanelMouseListener(IMImagePanel imagePanel) {
			this.imagePanel = imagePanel;
			this.leftClicked = false;
		}

		// MouseListener methods.
		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			if (e.getButton() != MouseEvent.BUTTON1) {
				return;
			}

			this.leftClicked = true;
			this.imagePanel.removeCursor();
			this.imagePanel.startSegment(e.getPoint());
		}

		public void mouseReleased(MouseEvent e) {
			if (e.getButton() != MouseEvent.BUTTON1) {
				return;
			}

			this.leftClicked = false;
			this.imagePanel.endCurrentSegment(e.getPoint());
		}

		public void mouseEntered(MouseEvent e) {
		}
 
 		public void mouseExited(MouseEvent e) {
 			if (this.leftClicked) {
 				return;
 			}

 			this.imagePanel.removeCursor();
 		}

 		// MouseMotionListener methods.
 		public void mouseDragged(MouseEvent e) {
 			if (!this.leftClicked) {
 				return;
 			}

			this.imagePanel.updateCurrentSegment(e.getPoint());
 		}

 		public void mouseMoved(MouseEvent e) {
 			if (this.leftClicked) {
 				return;
 			}

 			this.imagePanel.drawCursor(e.getPoint());
 		}
	}
}