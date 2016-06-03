import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class IMCoupleCoordinator {
	private IMImagePanel srcImagePanel;
	private IMImagePanel dstImagePanel;

	public IMCoupleCoordinator(IMImagePanel srcImagePanel, IMImagePanel dstImagePanel) {
		this.srcImagePanel = srcImagePanel;
		this.dstImagePanel = dstImagePanel;

		this.srcImagePanel.setCoordinator(this);
		this.dstImagePanel.setCoordinator(this);
	}

	public void notifySegmentFinished(IMImagePanel source) {
		IMImagePanel other;

		if (source == this.srcImagePanel) {
			other = this.dstImagePanel;
		} else if (source == this.dstImagePanel) {
			other = this.srcImagePanel;
		} else {
			throw new IllegalArgumentException("source");
		}

		source.disableMouseListener();
		other.enableMouseListener();
	}

	public void loadSrcImage(String path) throws IOException, MismatchingImageSizesException {
		this.loadImage(this.srcImagePanel, path);
	}

	public void loadDstImage(String path) throws IOException, MismatchingImageSizesException {
		this.loadImage(this.dstImagePanel, path);
	}

	public void clearSegments() {
		this.srcImagePanel.disableMouseListener();
		this.dstImagePanel.disableMouseListener();

		this.srcImagePanel.removeAllSegments();
		this.dstImagePanel.removeAllSegments();

		if (this.srcImagePanel.hasImage() && this.dstImagePanel.hasImage()) {
			this.srcImagePanel.enableMouseListener();
		}
	}

	public void clearImages() {
		this.srcImagePanel.clearImage();
		this.dstImagePanel.clearImage();
		this.clearSegments();
	}

	private void loadImage(ImagePanel imagePanel, String path) throws IOException, MismatchingImageSizesException {
		if (imagePanel != this.srcImagePanel && imagePanel != this.dstImagePanel) {
			throw new IllegalArgumentException("imagePanel");
		}

		ImagePanel theOther = (imagePanel == this.srcImagePanel) ? this.dstImagePanel : this.srcImagePanel;

		if (theOther.hasImage()) {
			BufferedImage image = ImageIO.read(new File(path));

			if (image.getWidth() != theOther.getImageWidth() || image.getHeight() != theOther.getImageHeight()) {
				throw new MismatchingImageSizesException();
			}
		}
		
		imagePanel.setImage(path);
		this.clearSegments();
	}
}