import java.awt.EventQueue;
import java.awt.Color;
import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			public void run() {
				IMFrame frame = new IMFrame();
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}