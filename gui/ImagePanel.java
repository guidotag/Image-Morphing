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
    private BufferedImage image;
    private String imagePath;

    public ImagePanel(int width, int height) {
        this.image = null;
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setPreferredSize(new Dimension(width, height));
        this.setSize(width, height);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }

    public void setImage(String path) throws IOException {
        BufferedImage real;

        try {
            real = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw e;
        }

        this.imagePath = path;
        this.realWidth = real.getWidth();
        this.realHeight = real.getHeight();
        this.image = new BufferedImage(this.getWidth(), this.getHeight(), real.getType());
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(this.getWidth() / (double)realWidth, this.getHeight() / (double)realHeight);
        AffineTransformOp scaleTransform = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        scaleTransform.filter(real, this.image);

        this.repaint();
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public boolean hasImage() {
        return this.image != null;
    }

    protected int getRealWidth() {
        return this.realWidth;
    }

    protected int getRealHeight() {
        return this.realHeight;
    }
}