import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class IMFrame extends JFrame{
	private static final String FRAME_TITLE = "Image Morphing";
	private static final int FRAME_WIDTH = 1280;
	private static final int FRAME_HEIGHT = 960;
	private static final int FRAME_X = 150;
	private static final int FRAME_Y = 50;

	private IMCouplePanel couplePanel;

	public IMFrame() {
		// Frame properties.
		this.setLayout(null);
		this.setTitle(IMFrame.FRAME_TITLE);
		this.setResizable(false);
		this.setLocation(IMFrame.FRAME_X, IMFrame.FRAME_Y);

		// Setup components.
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		this.setSize(IMFrame.FRAME_WIDTH, IMFrame.FRAME_HEIGHT);

		this.couplePanel = new IMCouplePanel();
		this.couplePanel.setLocation(0, 0);
		this.couplePanel.setSize(1280, 550);

		JButton morphButton = new JButton();
		morphButton.setText("Morph");
		morphButton.setLocation(960, 570);
		morphButton.setSize(300, 80);
		morphButton.addActionListener(new MorphButtonListener(this));		

		// Add components to the frame.
		Container contentPane = this.getContentPane();
		contentPane.add(this.couplePanel);
		contentPane.add(morphButton);
	}

	public void callMorph() {
		ArrayList<Segment> srcImageSegments = this.couplePanel.getSrcImageSegments();
		ArrayList<Segment> dstImageSegments = this.couplePanel.getDstImageSegments();

		// Check we can morph yet.
		if (srcImageSegments.size() != dstImageSegments.size()) {
			JOptionPane.showMessageDialog(this, "The number of segments defined on each picture must be the same.", "Morph input error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String srcImagePath = this.couplePanel.getSrcImagePath();
		String dstImagePath = this.couplePanel.getDstImagePath();
		String morphName = IMFrame.getNameWithoutExtension(srcImagePath) + "2" + IMFrame.getNameWithoutExtension(dstImagePath);
		String outputImagePath = "output/" + morphName + ".avi";
		String segmentsPath = "input/" + morphName + ".segments";
		int numberOfFrames = 100;

		this.printSegmentsFile(segmentsPath, srcImageSegments, dstImageSegments);

		try {
			Process process = new ProcessBuilder("./morph",
											 srcImagePath,
											 dstImagePath,
											 outputImagePath,
											 segmentsPath,
											 "" + numberOfFrames).start();

			System.out.println("morph generated at " + outputImagePath);
		} catch (IOException e) {
			// TODO
			System.err.println(e.getMessage());
		}
	}

	private void printSegmentsFile(String segmentsPath, ArrayList<Segment> srcImageSegments, ArrayList<Segment> dstImageSegments) {
		PrintWriter writer;

		try {
			writer = new PrintWriter(segmentsPath, "UTF-8");
		} catch (Exception e) {
			// TODO
			return;
		}

		writer.printf("%d%n", srcImageSegments.size());
		IMFrame.printSegments(writer, srcImageSegments);
		IMFrame.printSegments(writer, dstImageSegments);
		writer.close();
	}

	private static void printSegments(PrintWriter writer, ArrayList<Segment> segments) {
		for (Segment s: segments) {
			writer.printf("%d %d %d %d%n", 
				(int)s.getFrom().getX(),
				(int)s.getFrom().getY(),
				(int)s.getTo().getX(),
				(int)s.getTo().getY());
		}
	}

	private static String getNameWithoutExtension(String path) {
		String name = new File(path).getName();
		return name.substring(0, name.lastIndexOf("."));
	}

	private class MorphButtonListener implements ActionListener {
		private IMFrame frame;

		public MorphButtonListener(IMFrame frame) {
			this.frame = frame;
		}

		public void actionPerformed(ActionEvent e) {
			this.frame.callMorph();
		}
	}
}