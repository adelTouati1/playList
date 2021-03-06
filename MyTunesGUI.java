import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * The driver class for MyTunes GUI.
 * 
 * @version Spring 2017
 * @author adeltouati
 */

public class MyTunesGUI
{
	private PlayList playList;
	/**
	 * Creates a JFrame and adds the main JPanel to the JFrame.
	 * @param args (unused)
	 */
	public static void main(String args[])
	{
		// So it looks consistent on Mac/Windows/Linux
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File file = new File (args[0]);
		PlayList playList = new PlayList("adelPlaylist");
		playList.loadFromFile(file);
		JFrame frame = new JFrame("MyTunes");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MyTunesGUIPanel(playList));
		frame.setPreferredSize(new Dimension(1200, 650));
		frame.pack();
		frame.setVisible(true);
	}
}
