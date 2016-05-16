import javax.swing.*;

public class ImagePanel extends JPanel {

	private BufferedImage image;

    public ImagePanel() {

    }

    public setImage(BufferedImage image) {
    	this.image = image;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    // TODO - ActionListener
    /*
    import java.awt.Graphics;
	import java.awt.image.BufferedImage;
	import java.io.File;
	import java.io.IOException;
	import java.util.logging.Level;
	import java.util.logging.Logger;
	import javax.imageio.ImageIO;
	import javax.swing.JPanel;
	try {
		image = ImageIO.read(new File("path"));
	} catch (IOException e) {
		
	}
    */
}