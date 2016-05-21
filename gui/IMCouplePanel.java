import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;
import javax.swing.JButton;

public class IMCouplePanel extends JPanel {
	private static final int IMAGE_PANEL_WIDTH = 600;
	private static final int IMAGE_PANEL_HEIGHT = 450;

	private IMImagePanel srcImagePanel;
	private IMImagePanel dstImagePanel;

	public IMCouplePanel() {
		this.setLayout(null);

		// Setup components.
		this.srcImagePanel = new IMImagePanel(IMCouplePanel.IMAGE_PANEL_WIDTH, IMCouplePanel.IMAGE_PANEL_HEIGHT);
		this.srcImagePanel.setLocation(20, 100);

		this.dstImagePanel = new IMImagePanel(IMCouplePanel.IMAGE_PANEL_WIDTH, IMCouplePanel.IMAGE_PANEL_HEIGHT);
		this.dstImagePanel.setLocation(660, 100);

		IMCoupleCoordinator coordinator = new IMCoupleCoordinator(this.srcImagePanel, this.dstImagePanel);

		JButton chooseSrcButton = new JButton();
		chooseSrcButton.setText("Choose source picture");
		chooseSrcButton.setLocation(20, 40);
		chooseSrcButton.setSize(300, 40);

		JButton chooseDstButton = new JButton();
		chooseDstButton.setText("Choose destination picture");
		chooseDstButton.setLocation(660, 40);
		chooseDstButton.setSize(300, 40);

		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("bmp filter", "bmp");
		fileChooser.setFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setCurrentDirectory(new File("."));
		chooseSrcButton.addActionListener(new ChooseImageButtonListener(this, fileChooser, this.srcImagePanel));
		chooseDstButton.addActionListener(new ChooseImageButtonListener(this, fileChooser, this.dstImagePanel));

		// Add components to the panel.
		this.add(this.srcImagePanel);
		this.add(this.dstImagePanel);
		this.add(chooseSrcButton);
		this.add(chooseDstButton);
	}

	public ArrayList<Segment> getSrcImageSegments() {
		return this.srcImagePanel.getSegments();
	}

	public ArrayList<Segment> getDstImageSegments() {
		return this.dstImagePanel.getSegments();
	}

	public String getSrcImagePath() {
		return this.srcImagePanel.getImagePath();
	}

	public String getDstImagePath() {
		return this.dstImagePanel.getImagePath();
	}

	private class ChooseImageButtonListener implements ActionListener {
		private Container parent;
		private JFileChooser fileChooser;
		private ImagePanel imagePanel;

		public ChooseImageButtonListener(Container parent, JFileChooser fileChooser, ImagePanel imagePanel) {
			this.parent = parent;
			this.fileChooser = fileChooser;
			this.imagePanel = imagePanel;
		}

		public void actionPerformed(ActionEvent event) {
			int result = this.fileChooser.showOpenDialog(parent);
			if (result == JFileChooser.APPROVE_OPTION) {
				String path = fileChooser.getSelectedFile().getPath(); 
				
				try {
					imagePanel.setImage(path);
				} catch(IOException e) {
					JOptionPane.showMessageDialog(this.parent, "Cannot load chosen picture.", "Picture error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				imagePanel.repaint();
			}
		}
	}
}