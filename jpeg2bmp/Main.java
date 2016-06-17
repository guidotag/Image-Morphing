import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Main {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Wrong number of arguments.");
			return;
		}

		String path = args[0];
		String extension = Main.getExtension(path).toLowerCase();

		if (!extension.equals("jpg") && !extension.equals("jpeg")) {
			System.err.println("Wrong file type. Must be JPEG.");
			return;
		}

		File input = new File(path);
		BufferedImage image;
		try {
			image = ImageIO.read(input);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		File output = new File(Main.changeExtension(path, "bmp"));
		try {
			ImageIO.write(image, "bmp", output);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static String getExtension(String path) {
		return path.substring(path.lastIndexOf(".") + 1, path.length());
	}

	public static String changeExtension(String path, String newExtension) {
		return path.substring(0, path.lastIndexOf(".")) + "." + newExtension;
	}
}