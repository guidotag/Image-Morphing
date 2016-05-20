import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.Color;

public class ImagePanel extends JPanel {

	private BufferedImage image;

    public ImagePanel() {
        this.image = null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }

    public void setImage(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
        }
        this.repaint();
    }
}