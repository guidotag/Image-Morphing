import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JButton;

public class IMCouplePanel extends JPanel {
	private static final int IMAGE_PANEL_WIDTH = 600;
	private static final int IMAGE_PANEL_HEIGHT = 450;

	private IMImagePanel srcImagePanel;
	private IMImagePanel dstImagePanel;

	private IMCoupleCoordinator coordinator;

	public IMCouplePanel() {
		this.setLayout(null);

		// Setup components.
		this.srcImagePanel = new IMImagePanel(IMCouplePanel.IMAGE_PANEL_WIDTH, IMCouplePanel.IMAGE_PANEL_HEIGHT);
		this.srcImagePanel.setLocation(20, 10);

		this.dstImagePanel = new IMImagePanel(IMCouplePanel.IMAGE_PANEL_WIDTH, IMCouplePanel.IMAGE_PANEL_HEIGHT);
		this.dstImagePanel.setLocation(660, 10);

		coordinator = new IMCoupleCoordinator(this.srcImagePanel, this.dstImagePanel);

		// Add components to the panel.
		this.add(this.srcImagePanel);
		this.add(this.dstImagePanel);
	}

	public boolean bothImagesSelected() {
		return this.srcImagePanel.hasImage() && this.dstImagePanel.hasImage();
	}

	public boolean bothPositiveSegmentsCount() {
		return this.srcImagePanel.getSegmentsCount() > 0 && this.dstImagePanel.getSegmentsCount() > 0;
	}

	public boolean bothEqualSegmentsCount() {
		return this.srcImagePanel.getSegmentsCount() == this.dstImagePanel.getSegmentsCount();
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

	public void loadSrcImage(String path) throws IOException, MismatchingImageSizesException {
		this.coordinator.loadSrcImage(path);
	}

	public void loadDstImage(String path) throws IOException, MismatchingImageSizesException {
		this.coordinator.loadDstImage(path);
	}

	public void clearSegments() {
		this.coordinator.clearSegments();
	}

	public void clearImages() {
		this.coordinator.clearImages();
	}
}