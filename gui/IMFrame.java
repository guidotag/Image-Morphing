import java.awt.*;
import javax.swing.*;

public class IMFrame extends JFrame{
	private static final String FRAME_TITLE = "Image Morphing";
	private static final int FRAME_WIDTH = 1280;
	private static final int FRAME_HEIGHT = 960;
	private static final int FRAME_X = 150;
	private static final int FRAME_Y = 50;

	private static final int IMAGE_PANEL_WIDTH = 600;
	private static final int IMAGE_PANEL_HEIGHT = 450;

	public IMFrame() {
		// Frame properties.
		this.setLayout(null);
		this.setTitle(FRAME_TITLE);
		this.setResizable(false);
		this.setLocation(FRAME_X, FRAME_Y);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
	
		// Components.
		ImagePanel srcImagePanel = new ImagePanel();
		srcImagePanel.setLocation(20, 100);
		srcImagePanel.setSize(IMAGE_PANEL_WIDTH, IMAGE_PANEL_HEIGHT);
		srcImagePanel.setBackground(Color.BLACK);
		srcImagePanel.setImage("bill.bmp");

		ImagePanel dstImagePanel = new ImagePanel();
		dstImagePanel.setLocation(660, 100);
		dstImagePanel.setSize(IMAGE_PANEL_WIDTH, IMAGE_PANEL_HEIGHT);
		dstImagePanel.setBackground(Color.BLACK);

		JButton chooseSrcButton = new JButton();
		chooseSrcButton.setText("Choose source picture");
		chooseSrcButton.setLocation(20, 40);
		chooseSrcButton.setSize(300, 40);

		JButton chooseDstButton = new JButton();
		chooseDstButton.setText("Choose destination picture");
		chooseDstButton.setLocation(660, 40);
		chooseDstButton.setSize(300, 40);

		JButton morphButton = new JButton();
		morphButton.setText("Morph");
		morphButton.setLocation(960, 570);
		morphButton.setSize(300, 80);

		// Add the components to the frame.
		Container contentPane = this.getContentPane();
		contentPane.add(srcImagePanel);
		contentPane.add(dstImagePanel);
		contentPane.add(chooseSrcButton);
		contentPane.add(chooseDstButton);
		contentPane.add(morphButton);
	}

}