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
	private static final int FRAME_HEIGHT = 750;
	private static final int FRAME_X = 150;
	private static final int FRAME_Y = 50;

	private static final int FRAMES_COUNT_SLIDER_DEFAULT_VALUE = 50;
	private static final int FPS_SLIDER_DEFAULT_VALUE = 30;

	private JButton chooseSrcButton;
	private JButton chooseDstButton;
	private IMCouplePanel couplePanel;
	private JSlider framesCountSlider;
	private JSlider fpsSlider;

	private String outputDirectory;
	private String segmentsDirectory;
	private String morphBinaryPath;

	public IMFrame() {
		// Frame properties.
		this.setLayout(null);
		this.setTitle(IMFrame.FRAME_TITLE);
		this.setResizable(false);
		this.setLocation(IMFrame.FRAME_X, IMFrame.FRAME_Y);

		//Toolkit toolkit = Toolkit.getDefaultToolkit();
		//Dimension screenSize = toolkit.getScreenSize();
		this.setSize(IMFrame.FRAME_WIDTH, IMFrame.FRAME_HEIGHT);

		// Setup components.
		this.couplePanel = new IMCouplePanel();
		this.couplePanel.setLocation(0, 100);
		this.couplePanel.setSize(1280, 475);

		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("bmp filter", "bmp");
		fileChooser.setFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setCurrentDirectory(new File("."));

		this.chooseSrcButton = new JButton();
		this.chooseSrcButton.setText("Choose source picture");
		this.chooseSrcButton.setLocation(20, 30);
		this.chooseSrcButton.setSize(290, 50);
		this.chooseSrcButton.addActionListener(new ChooseImageButtonListener(this, fileChooser));

		this.chooseDstButton = new JButton();
		this.chooseDstButton.setText("Choose destination picture");
		this.chooseDstButton.setLocation(660, 30);
		this.chooseDstButton.setSize(290, 50);
		this.chooseDstButton.setVisible(true);
		this.chooseDstButton.addActionListener(new ChooseImageButtonListener(this, fileChooser));

		JButton morphButton = new JButton();
		morphButton.setText("Morph");
		morphButton.setLocation(970, 590);
		morphButton.setSize(290, 110);
		morphButton.addActionListener(new MorphButtonListener(this));

		JButton clearSegmentsButton = new JButton();
		clearSegmentsButton.setText("Clear segments");
		clearSegmentsButton.setLocation(660, 590);
		clearSegmentsButton.setSize(290, 50);
		clearSegmentsButton.addActionListener(new ClearSegmentsButtonListener(this.couplePanel));

		JButton clearImagesButton = new JButton();
		clearImagesButton.setText("Clear pictures");
		clearImagesButton.setLocation(660, 650);
		clearImagesButton.setSize(290, 50);
		clearImagesButton.addActionListener(new ClearImagesButtonListener(this.couplePanel));

		JLabel framesCountSliderLabel = new JLabel();
		framesCountSliderLabel.setText("Frames count: " + IMFrame.FRAMES_COUNT_SLIDER_DEFAULT_VALUE);
		framesCountSliderLabel.setLocation(365, 600);
		framesCountSliderLabel.setSize(200, 25);

		JLabel fpsSliderLabel = new JLabel();
		fpsSliderLabel.setText("FPS: " + IMFrame.FPS_SLIDER_DEFAULT_VALUE);
		fpsSliderLabel.setLocation(365, 675);
		fpsSliderLabel.setSize(200, 25);

		this.framesCountSlider = new JSlider(0, 200, IMFrame.FRAMES_COUNT_SLIDER_DEFAULT_VALUE);
		this.framesCountSlider.setMajorTickSpacing(20);
		this.framesCountSlider.setMinorTickSpacing(10);
		this.framesCountSlider.setPaintTicks(true);
		this.framesCountSlider.setPaintLabels(true);
		this.framesCountSlider.setSnapToTicks(true);
		this.framesCountSlider.setLocation(20, 600);
		this.framesCountSlider.setSize(320, 45);
		this.framesCountSlider.addChangeListener(new FramesCountSliderListener(this.framesCountSlider, framesCountSliderLabel));

		this.fpsSlider = new JSlider(0, 80, IMFrame.FPS_SLIDER_DEFAULT_VALUE);
		this.fpsSlider.setMajorTickSpacing(10);
		this.fpsSlider.setMinorTickSpacing(5);
		this.fpsSlider.setPaintTicks(true);
		this.fpsSlider.setPaintLabels(true);
		this.fpsSlider.setSnapToTicks(true);
		this.fpsSlider.setLocation(20, 675);
		this.fpsSlider.setSize(320, 45);
		this.fpsSlider.addChangeListener(new FPSSliderListener(this.fpsSlider, fpsSliderLabel));

		// Add components to the frame.
		Container contentPane = this.getContentPane();
		contentPane.add(this.couplePanel);
		contentPane.add(this.chooseSrcButton);
		contentPane.add(this.chooseDstButton);
		contentPane.add(morphButton);
		contentPane.add(this.framesCountSlider);
		contentPane.add(this.fpsSlider);
		contentPane.add(framesCountSliderLabel);
		contentPane.add(fpsSliderLabel);
		contentPane.add(clearSegmentsButton);
		contentPane.add(clearImagesButton);

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
				throw new IllegalArgumentException("source");
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Cannot load chosen picture.", "Picture loading error", JOptionPane.ERROR_MESSAGE);
		} catch (MismatchingImageSizesException e) {
			JOptionPane.showMessageDialog(this, "Both pictures must be the same size.", "Picture size error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean checkMorphInput() {
		if (!this.couplePanel.bothImagesSelected()) {
			JOptionPane.showMessageDialog(this, "Select source and destination pictures.", "Morph input error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!this.couplePanel.bothPositiveSegmentsCount()) {
			JOptionPane.showMessageDialog(this, "A positive number of segments must be provided on each picture.", "Morph input error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!this.couplePanel.bothEqualSegmentsCount()) {
			JOptionPane.showMessageDialog(this, "The number of segments defined on each picture must be the same.", "Morph input error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	private void callMorph() {
		if (!checkMorphInput()) {
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
		// TODO - Check if segmentsPath already exists. If it does, add an (n).
		int framesCount = this.framesCountSlider.getValue();
		int fps = this.fpsSlider.getValue();

		this.printSegmentsFile(segmentsPath, srcImageSegments, dstImageSegments);

		Process process;
		try {
			process = new ProcessBuilder(this.morphBinaryPath,
											 srcImagePath,
											 dstImagePath,
											 outputImagePath,
											 segmentsPath,
											 "" + framesCount,
											 "" + fps).start();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "A problem occurred while executing the morph.", "Morph error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			process.waitFor();
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(this, "A problem occurred while executing the morph.", "Morph error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this, "Morph finished.", "Morph information", JOptionPane.INFORMATION_MESSAGE);
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

	private class ClearImagesButtonListener implements ActionListener {
		private IMCouplePanel couplePanel;

		public ClearImagesButtonListener(IMCouplePanel couplePanel) {
			this.couplePanel = couplePanel;
		}

		public void actionPerformed(ActionEvent e) {
			this.couplePanel.clearImages();
		}
	}

	private class FramesCountSliderListener implements ChangeListener {
		private JSlider framesCountSlider;
		private JLabel sliderLabel;

		public FramesCountSliderListener(JSlider framesCountSlider, JLabel sliderLabel) {
			this.framesCountSlider = framesCountSlider;
			this.sliderLabel = sliderLabel;
		}

		public void stateChanged(ChangeEvent e) {
			if (this.framesCountSlider.getValue() == 0) {
				this.framesCountSlider.setValue(5);
			}

			this.sliderLabel.setText("Frames count: " + this.framesCountSlider.getValue());
		}
	}

	private class FPSSliderListener implements ChangeListener {
		private JSlider fpsSlider;
		private JLabel sliderLabel;

		public FPSSliderListener(JSlider fpsSlider, JLabel sliderLabel) {
			this.fpsSlider = fpsSlider;
			this.sliderLabel = sliderLabel;
		}

		public void stateChanged(ChangeEvent e) {
			if (this.fpsSlider.getValue() == 0) {
				this.fpsSlider.setValue(5);
			}

			this.sliderLabel.setText("FPS: " + this.fpsSlider.getValue());
		}
	}
}