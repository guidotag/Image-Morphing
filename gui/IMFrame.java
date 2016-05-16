import java.awt.*;
import javax.swing.*;

public class IMFrame extends JFrame{
	private static final String DEFAULT_TITLE = "Image-Morphing";
	private static final double DEFAULT_WIDTH_FACTOR = 0.9;
	private static final double DEFAULT_HEIGHT_FACTOR = 0.9;

	public IMFrame() {
		// Basic properties.
		this.setTitle(DEFAULT_TITLE);
		this.setResizable(true);
		this.setLocationByPlatform(false);
		this.setLocation(0, 0);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		this.setSize((int)(screenSize.width * DEFAULT_WIDTH_FACTOR), (int)(screenSize.height * DEFAULT_HEIGHT_FACTOR));
	
		// Components.
		ImagePanel imagePanelSrc = new ImagePanel();

		// https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html

	}

}