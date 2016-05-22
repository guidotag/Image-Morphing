import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JLabel;

public class IMFrame extends JFrame{
	private static final String FRAME_TITLE = "Image Morphing";
	private static final int FRAME_WIDTH = 1280;
	private static final int FRAME_HEIGHT = 960;
	private static final int FRAME_X = 150;
	private static final int FRAME_Y = 50;

	private static final int SPEED_SLIDER_DEFAULT_VALUE = 50;

	private JButton chooseSrcButton;
	private JButton chooseDstButton;
	private IMCouplePanel couplePanel;
	private JSlider speedSlider;

	private String outputDirectory;
	private String segmentsDirectory;
	private String morphBinaryPath;

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
		this.couplePanel.setLocation(0, 100);
		this.couplePanel.setSize(1280, 500);

		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("bmp filter", "bmp");
		fileChooser.setFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setCurrentDirectory(new File("."));

		this.chooseSrcButton = new JButton();
		this.chooseSrcButton.setText("Choose source picture");
		this.chooseSrcButton.setLocation(20, 40);
		this.chooseSrcButton.setSize(300, 40);
		this.chooseSrcButton.addActionListener(new ChooseImageButtonListener(this, fileChooser));

		this.chooseDstButton = new JButton();
		this.chooseDstButton.setText("Choose destination picture");
		this.chooseDstButton.setLocation(660, 40);
		this.chooseDstButton.setSize(300, 40);
		this.chooseDstButton.setVisible(true);
		this.chooseDstButton.addActionListener(new ChooseImageButtonListener(this, fileChooser));

		JButton morphButton = new JButton();
		morphButton.setText("Morph");
		morphButton.setLocation(960, 570);
		morphButton.setSize(300, 80);
		morphButton.addActionListener(new MorphButtonListener(this));

		JLabel sliderLabel = new JLabel();
		sliderLabel.setText("Morph speed: " + IMFrame.SPEED_SLIDER_DEFAULT_VALUE + " frames");
		sliderLabel.setLocation(660, 700);
		sliderLabel.setSize(200, 25);

		this.speedSlider = new JSlider(0, 100, IMFrame.SPEED_SLIDER_DEFAULT_VALUE);
		this.speedSlider.setMajorTickSpacing(10);
		this.speedSlider.setMinorTickSpacing(5);
		this.speedSlider.setPaintTicks(true);
		this.speedSlider.setPaintLabels(true);
		this.speedSlider.setSnapToTicks(true);
		this.speedSlider.setLocation(660, 600);
		this.speedSlider.setSize(280, 50);
		this.speedSlider.addChangeListener(new SpeedSliderListener(this.speedSlider, sliderLabel));

		JButton clearSegmentsButton = new JButton();
		clearSegmentsButton.setText("Clear segments");
		clearSegmentsButton.setLocation(20, 600);
		clearSegmentsButton.setSize(300, 40);
		clearSegmentsButton.addActionListener(new ClearSegmentsButtonListener(this.couplePanel));

		// Add components to the frame.
		Container contentPane = this.getContentPane();
		contentPane.add(this.couplePanel);
		contentPane.add(this.chooseSrcButton);
		contentPane.add(this.chooseDstButton);
		contentPane.add(morphButton);
		contentPane.add(this.speedSlider);
		contentPane.add(sliderLabel);
		contentPane.add(clearSegmentsButton);

		// The rest of the attributes.
		this.outputDirectory = "./output";
		this.segmentsDirectory = "./segments";
		this.morphBinaryPath = "./morph";
	}

	private void loadImage(String path, Object source) {
		try {
			if (source == this.chooseSrcButton) {
				this.couplePanel.loadSrcImage(path);
			} else if (source == this.chooseDstButton) {
				this.couplePanel.loadDstImage(path);
			} else {
				throw new IllegalArgumentException();
			}
		} catch(IOException ex) {
			JOptionPane.showMessageDialog(this, "Cannot load chosen picture.", "Picture loading error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void callMorph() {
		// Check we can call morph.
		if (!this.couplePanel.bothImagesSelected()) {
			JOptionPane.showMessageDialog(this, "Select source and destination pictures.", "Morph input error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!this.couplePanel.bothPositiveSegmentsCount()) {
			JOptionPane.showMessageDialog(this, "A positive number of segments must be provided on each picture.", "Morph input error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!this.couplePanel.bothEqualSegmentsCount()) {
			JOptionPane.showMessageDialog(this, "The number of segments defined on each picture must be the same.", "Morph input error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Do it.
		ArrayList<Segment> srcImageSegments = this.couplePanel.getSrcImageSegments();
		ArrayList<Segment> dstImageSegments = this.couplePanel.getDstImageSegments();

		String srcImagePath = this.couplePanel.getSrcImagePath();
		String dstImagePath = this.couplePanel.getDstImagePath();
		String morphName = IMFrame.getNameWithoutExtension(srcImagePath) + "2" + IMFrame.getNameWithoutExtension(dstImagePath);
		String outputImagePath = this.outputDirectory + "/" + morphName + ".avi";
		String segmentsPath = this.segmentsDirectory + "/" + morphName + ".segments";
		// TODO - Check if segmentsPath already exists. If it does, add a (n).
		int framesCount = this.speedSlider.getValue() + 2;

		this.printSegmentsFile(segmentsPath, srcImageSegments, dstImageSegments);

		Process process;
		try {
			process = new ProcessBuilder(this.morphBinaryPath,
											 srcImagePath,
											 dstImagePath,
											 outputImagePath,
											 segmentsPath,
											 "" + framesCount,
											 "" + 80).start();
		} catch (IOException e) {
			// TODO
			return;
		}

		try {
			process.waitFor();
		} catch (InterruptedException e) {
			// TODO
			return;
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

	private class ChooseImageButtonListener implements ActionListener {
		private IMFrame frame;
		private JFileChooser fileChooser;

		public ChooseImageButtonListener(IMFrame frame, JFileChooser fileChooser) {
			this.frame = frame;
			this.fileChooser = fileChooser;
		}

		public void actionPerformed(ActionEvent e) {
			int result = this.fileChooser.showOpenDialog(this.frame);
			if (result == JFileChooser.APPROVE_OPTION) {
				String path = this.fileChooser.getSelectedFile().getPath();
				this.frame.loadImage(path, e.getSource());
			}
		}
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

	private class ClearSegmentsButtonListener implements ActionListener {
		private IMCouplePanel couplePanel;

		public ClearSegmentsButtonListener(IMCouplePanel couplePanel) {
			this.couplePanel = couplePanel;
		}

		public void actionPerformed(ActionEvent e) {
			this.couplePanel.clearSegments();
		}
	}

	private class SpeedSliderListener implements ChangeListener {
		private JSlider speedSlider;
		private JLabel sliderLabel;

		public SpeedSliderListener(JSlider speedSlider, JLabel sliderLabel) {
			this.speedSlider = speedSlider;
			this.sliderLabel = sliderLabel;
		}

		public void stateChanged(ChangeEvent e) {
			this.sliderLabel.setText("Morph speed: " + speedSlider.getValue() + " frames");
		}
	}
}