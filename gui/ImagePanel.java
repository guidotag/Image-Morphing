import java.io.File;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {
    private int realWidth;
    private int realHeight;
    private BufferedImage scaledImage;
    private String imagePath;

    public ImagePanel(int width, int height) {
        this.scaledImage = null;
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setPreferredSize(new Dimension(width, height));
        this.setSize(width, height);
        this.imagePath = null;
        this.realWidth = 0;
        this.realHeight = 0;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.scaledImage != null) {
            g.drawImage(scaledImage, 0, 0, null);
        }
    }

    public void clearImage() {
        this.scaledImage = null;
        this.imagePath = null;
        this.realWidth = 0;
        this.realHeight = 0;

        this.repaint();
    }

    public void setImage(String path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path");
        }

        BufferedImage image = ImageIO.read(new File(path));
        this.imagePath = path;
        this.realWidth = image.getWidth();
        this.realHeight = image.getHeight();
        this.scaledImage = new BufferedImage(this.getWidth(), this.getHeight(), image.getType());
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(this.getWidth() / (double)realWidth, this.getHeight() / (double)realHeight);
        AffineTransformOp scaleTransform = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        scaleTransform.filter(image, this.scaledImage);

        this.repaint();
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public boolean hasImage() {
        return this.scaledImage != null;
    }

    public int getImageWidth() {
        return this.realWidth;
    }

    public int getImageHeight() {
        return this.realHeight;
    }
}