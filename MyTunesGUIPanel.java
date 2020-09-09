import java.awt.BorderLayout;   
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;



import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;




/**
 * MyTunesGUIPanel.java
 * CS 121 Project 5: My Tunes GUI
 * 
 * this is a Graphical User Interface (GUI) program that manipulate a song list and square 
 * buttons list and include a heating map
 * 
 * @author adeltouati
 *
 */

public class MyTunesGUIPanel extends JPanel {


	private JButton downButton;
	private JButton upButton;
	private JButton playButton;
	private JButton forwardButton;
	private JButton backwardButton;
	private JButton stopButton;
	private JButton addSong;
	private JButton removeSong;
	private PlayList playList;
	private JList<Song> list;
	private JLabel titleInfo;
	private JLabel title;
	private int songNum;
	private JPanel centerPanel;
	private Song[][] songSquare;
	private JButton[][] songSquareButtons;
	private JLabel infoPlatingLab;
	private JLabel emptyLabel;
	private JButton openFileButton;
	private JTextField filePathFiled;
	private JButton songButton;
	private Timer timer;
	private squareButtonlist li;
	
	
	public MyTunesGUIPanel(PlayList playList){
		this.playList=playList;
		//Creating BoxLayout Y axes in the west borderlayout
		setLayout(new BorderLayout());
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createVerticalGlue());
		add(leftPanel,BorderLayout.WEST);
		
		//Creating up and down buttons and set icons 
		downButton= new JButton();
		try {
			ImageIcon downIcon = new ImageIcon(ImageIO.read(getClass().getResource("images/move-down-24.gif")));
			downButton.setIcon(downIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		upButton=new JButton();
		try {
			ImageIcon upIcon = new ImageIcon(ImageIO.read(getClass().getResource("images/move-up-24.gif")));

			upButton.setIcon(upIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// creating timer
		timer = new Timer(0, new TimerListener());
		timer.setRepeats(false);
		
		// creating JList from the ArrayList
		list = new JList<Song>(playList.getSongArray());
		list.setSelectedIndex(0);
		// Setting the Font of JList
		list.setFont(new Font("monospaced", Font.LAYOUT_LEFT_TO_RIGHT, 10));
		//Creating button Panel and add up and down buttons to the panel
		JPanel buttonSubPanel = new JPanel();
		buttonSubPanel.setLayout(new BoxLayout(buttonSubPanel, BoxLayout.X_AXIS));
		buttonSubPanel.add(upButton);
		buttonSubPanel.add(downButton);
		// Adding the listener to up and down buttons 
		changeOrderListener l = new changeOrderListener();
		upButton.addActionListener(l);
		downButton.addActionListener(l);
		// Adding the button Panel to the left panel 
		leftPanel.add(buttonSubPanel);
		// Creating list Panel and add the JList to the Panel
		JPanel listSubPanel = new JPanel();
		listSubPanel.add(list);
		leftPanel.add(listSubPanel);
		//Creating scroll Panel 
		JScrollPane scrollPane = new JScrollPane(list,  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		leftPanel.add(scrollPane);
		// creating add and remove buttons and add it to a panel and add to it a listener 
		addSong = new JButton("add song");
		removeSong= new JButton("remove song");
		JPanel arPanel = new JPanel();
		addremListener listne = new  addremListener ();
		addSong.addActionListener(listne);
		removeSong.addActionListener(listne);
		arPanel.add(addSong);
		arPanel.add(removeSong);
		leftPanel.add(arPanel);
		// creating title label with all the info and add it to a panel and add the panel to Borderlayout north 
		songNum= playList.getSongArray().length;
		String songs = new String (Integer.toString(songNum));
		int totalSeconds = playList.getTotalPlayTime();
		int minutes = totalSeconds/60;
		int seconds = totalSeconds%60;
		String info = songs+ " " + "Songs-"+ " "+ minutes+":"+seconds +" "+ "Total Play Time.";
		title= new JLabel("Adel`s List");
		titleInfo = new JLabel(info);
		JPanel topPanel = new JPanel();
		JPanel titlePanel = new JPanel();
		titlePanel.add(title);
		topPanel.add(titleInfo);
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout( northPanel,BoxLayout.Y_AXIS));
		northPanel.add(titlePanel);
		northPanel.add(topPanel);
		add(northPanel,BorderLayout.NORTH);
		
		// creating the grid of buttons and add titles and heatmap and listener and add the gridpanel to borderlayout center
		double listSize= Math.ceil(Math.sqrt(songNum));
		songSquareButtons= new JButton[(int)listSize][(int)listSize];
		centerPanel = new JPanel ();
		centerPanel.setLayout((new GridLayout((int)listSize, (int)listSize)));
		songSquare=playList.getSongSquare();
		li = new squareButtonlist();
		for (int i=0; i<listSize;i++){
			for(int j=0; j<listSize;j++){
				String title;
				title = new String(songSquare[i][j].getTitle());
				int plays = songSquare[i][j].getPlayCount();
				getHeatMapColor(plays);
				songButton =new JButton();	
				songButton.setText(title);
				songButton.setBackground(getHeatMapColor(plays));
				songSquareButtons[i][j]=songButton;

				songButton.addActionListener(li);
				centerPanel.add(songButton);
			}
		}
		add(centerPanel,BorderLayout.CENTER);
		// creating now playing JPanel and add it to the leftPanel
		JLabel nowPlaying = new JLabel("Now Playing ");
		JPanel nowPlayingPane = new JPanel();
		nowPlayingPane.add(nowPlaying);
		leftPanel.add(nowPlayingPane);
		//creating info of the song playing panel when is empty and when there is a song playing 
		String emptyInfo = "(nothing)"+" " + "by"+" "+ "(nobody)";
		emptyLabel = new JLabel(emptyInfo);
		JPanel emptyPane = new JPanel();
		emptyPane.add(emptyLabel);
		leftPanel.add(emptyPane);
		String artistName = new String (list.getSelectedValue().getArtist());
		String songName = new String (list.getSelectedValue().getTitle());
		String infoPlaying = songName+ " "+ "by"+ " "+ artistName;
		infoPlatingLab = new JLabel(infoPlaying);
		JPanel infoPlayPane = new JPanel();
		infoPlayPane.add(infoPlatingLab);
		// creating play,stop,forward,backward buttons and set icons to each button
		playButton= new JButton();
		try {
			ImageIcon playIcon = new ImageIcon(ImageIO.read(getClass().getResource("images/play-48.gif")));

			playButton.setIcon(playIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}

		stopButton= new JButton();
		try {
			ImageIcon stopIcon = new ImageIcon(ImageIO.read(getClass().getResource("images/stop-48.gif")));

			stopButton.setIcon(stopIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		forwardButton= new JButton();
		try {
			ImageIcon forwardIcon = new ImageIcon(ImageIO.read(getClass().getResource("images/media-skip-forward-48.gif")));
			forwardButton.setIcon(forwardIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		backwardButton= new JButton();
		try {
			ImageIcon backwardIcon = new ImageIcon(ImageIO.read(getClass().getResource("images/media-skip-backward-48.gif")));
			backwardButton.setIcon(backwardIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// adding the buttons play,stop,forward,backward to a panel and add listeners to the buttons 
		JPanel manipulatingPane = new JPanel();
		manipulatingPane.setLayout(new BoxLayout(manipulatingPane, BoxLayout.X_AXIS));
		manipulatingPane.add(backwardButton);
		manipulatingPane.add(playButton);
		manipulatingPane.add(stopButton);
		manipulatingPane.add(forwardButton);
		SkipListener listn = new SkipListener();
		forwardButton.addActionListener(listn);
		backwardButton.addActionListener(listn);
		PlayListener playlis = new PlayListener();
		playButton.addActionListener(playlis);
		stopButton.addActionListener(playlis);
		leftPanel.add(manipulatingPane);
	}

	/**
	 * Given the number of times a song has been played, this method will
	 * return a corresponding heat map color.
	 *
	 * Sample Usage: Color color = getHeatMapColor(song.getTimesPlayed());
	 *
	 * This algorithm was borrowed from:
	 * http://www.andrewnoske.com/wiki/Code_-_heatmaps_and_color_gradients
	 *
	 * @param plays The number of times the song that you want the color
	for has been played.
	 * @return The color to be used for your heat map.
	 */


	public Color getHeatMapColor(int plays)
	{

		double minPlays = 0, maxPlays = 100;
		double value = (plays - minPlays) / (maxPlays - minPlays);
		Color[] colors = { Color.CYAN, Color.GREEN, Color.YELLOW,
				Color.ORANGE, Color.RED };
		int index1, index2;
		float dist = 0;
		if (value <= 0) {
			index1 = index2 = 0;
		} else if (value >= 1) {
			index1 = index2 = colors.length - 1;
		} else {
			value = value * (colors.length - 1);
			index1 = (int) Math.floor(value);
			index2 = index1 + 1;
			dist = (float) value - index1;
		}
		int r = (int)((colors[index2].getRed() - colors[index1].getRed())
				* dist)
				+ colors[index1].getRed();
		int g = (int)((colors[index2].getGreen() -
				colors[index1].getGreen()) * dist)
				+ colors[index1].getGreen();
		int b = (int)((colors[index2].getBlue() -
				colors[index1].getBlue()) * dist)
				+ colors[index1].getBlue();
		return new Color(r, g, b);
	}
	
	/**
	 * setting the timer delay to song play time and start the timer 
	 */
	private void startTimer()
	{
		Song song = playList.getPlaying();
		if (song != null){	
		timer.setInitialDelay(song.getPlayTime()*1000);
			timer.start();
		}	
	}
	/**
	 * stop the timer and stop the song playing 
	 */
	private void stopTimer()
	{
		timer.stop();	
		playList.stop();
		
	}	
    /**
     * stopping the timer
     */
	private class TimerListener implements ActionListener
	{
		
		@Override
		public void actionPerformed(ActionEvent e){
	
			stopTimer();
		}
	}
	/**
	 * updating the label to the value of the song playing every time another song is playing 
	 * 
	 */
	private void updateLable(){

		songNum= playList.getSongArray().length;
		String songs = new String (Integer.toString(songNum));
		int totalSeconds = playList.getTotalPlayTime();
		int minutes = totalSeconds/60;
		int seconds = totalSeconds%60;
		String info = songs+ " " + "Songs-"+ " "+ minutes+":"+seconds +" "+ "Total Play Time.";
		titleInfo.setText(info);
		if (list.getSelectedValue()!=null){
			String artistName = new String (list.getSelectedValue().getArtist());
			String songName = new String (list.getSelectedValue().getTitle());
			String infoPlaying = songName+ " "+ "by"+ " "+ artistName;
			emptyLabel.setText(infoPlaying);
		}
	}
	/**
	 * updating the song square grid every time there is a change in the list of songs
	 */
	private void updateSongSquare(){
		int songNum= playList.getSongArray().length;
		double listSize= Math.ceil(Math.sqrt(songNum));
		songSquareButtons= new JButton[(int)listSize][(int)listSize];
		centerPanel.removeAll();
		if(songNum>0){
		centerPanel.setLayout((new GridLayout((int)listSize, (int)listSize)));
		songSquare=playList.getSongSquare();
		
	
		
		for (int i=0; i<listSize;i++){
			for(int j=0; j<listSize;j++){
				String title;
				title = new String(songSquare[i][j].getTitle());
				int plays = songSquare[i][j].getPlayCount();
				getHeatMapColor(plays);
				JButton songButton =new JButton();	
				songButton.setBackground(getHeatMapColor(plays));
				songButton.setText(title);
				songSquareButtons[i][j]=songButton;
				songSquareButtons[i][j].addActionListener(li);
				centerPanel.add(songButton);
			}
		}
		}
		centerPanel.revalidate();
	}
	/**
	 * updating the list every time there is changes in the list 
	 * adding update label and square song methods to updating list to update everything on same time 
	 */
	private void updatList(){
		list.setListData(playList.getSongArray());
		updateLable();
		updateSongSquare();
	}
	/**
	 * skip the song forward and backward when is clicked 
	 */
	private class SkipListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {

			int index = list.getSelectedIndex();
			if (event.getSource()==forwardButton){
			
				if(playList.getPlaying() != null){
					playList.stop();
					stopTimer();
				}
				if (index== playList.getNumSongs()-1){
					index=0;
					
				}else{
					index++;
		
				}
				startTimer();
				updateLable();
			} else if (event.getSource()==backwardButton){
				
				if(playList.getPlaying() != null){
					playList.stop();
					stopTimer();
				}			
				if (index==0){
					index=playList.getNumSongs()-1;
				}else {
					index--;
				}
			}			
			list.setSelectedIndex(index);
			
			playList.playSong(list.getSelectedValue());
			startTimer();
			
			updateLable();
		}
	}
/**
 * play and stop the song when is clicked 
 */
	private class PlayListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource()== playButton){
			
				playList.stop();
				playList.playSong(list.getSelectedValue());
				updateLable();
				updateSongSquare();
				startTimer();
			}
			if (e.getSource()==stopButton){
				
				if(playList.getPlaying() != null){
					stopTimer();
					playList.stop();
					String emptyInfo = "(nothing)"+" " + "by"+" "+ "(nobody)";
					emptyLabel.setText(emptyInfo);
					
				}
			}
		}
	}

	/**
	 * add song when add song button is clicked JOption panel pop up and ask the user for data and choose a file 
	 * remove song when remove song button is clicked and JOption panel pop up making sure that the user want to remove the song
	 */
	private class addremListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource()== addSong){
				JPanel formInputPanel = new JPanel();
				formInputPanel.setLayout(new BoxLayout(formInputPanel, BoxLayout.Y_AXIS));
				JPanel fileChooserPane= new JPanel();
				JPanel artistPane = new JPanel();
				JPanel titlePane = new JPanel();
				JPanel timePane = new JPanel();		
				fileChooserPane.setLayout(new BoxLayout(fileChooserPane, BoxLayout.X_AXIS));
				artistPane.setLayout(new BoxLayout(artistPane, BoxLayout.X_AXIS));
				titlePane.setLayout(new BoxLayout(titlePane, BoxLayout.X_AXIS));
				timePane.setLayout(new BoxLayout(timePane, BoxLayout.X_AXIS));
				JTextField artistField = new JTextField(20);
				JTextField titleField = new JTextField(20);
				JTextField timeFiled = new JTextField(10);
				openFileButton = new JButton("Select File");
				filePathFiled = new JTextField(10);
				artistPane.add(new JLabel("Artist: "));
				artistPane.add(artistField);
				titlePane.add(new JLabel("Title: "));
				titlePane.add(titleField);
				timePane.add(new JLabel("Play Time (seconds): "));
				timePane.add(timeFiled);
				formInputPanel.add(artistPane);
				formInputPanel.add(titlePane);
				formInputPanel.add(timePane);
				fileChooserPane.add(openFileButton);
				fileChooserPane.add(filePathFiled);
				openFileButton.addActionListener(new ButtonActionListener());
				formInputPanel.add(fileChooserPane);
				int addResult = JOptionPane.showConfirmDialog(null, formInputPanel, "Add Song",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

				if (addResult == JOptionPane.OK_OPTION)
				{
					String artist = artistField.getText();
					String title = titleField.getText();
					String filePath= filePathFiled.getText();
					int time = 0;
					try
					{
						time = Integer.parseInt(timeFiled.getText());
						if(time < 0) {
							JOptionPane.showMessageDialog(null, "Add a positive time in seconds.");
						}
					}
					catch (NumberFormatException ex)
					{
						JOptionPane.showMessageDialog(null, "Time needs to be a number.");
					}
					Song newSong = new Song(artist,title, time, filePath);
					playList.addSong(newSong);
					updatList();
				}	
			}
			if (e.getSource()==removeSong){
				JPanel confirmationPane = new JPanel();
				confirmationPane.add(new JLabel("Are you sure you want to remove this song."));
				int removeResult =JOptionPane.showConfirmDialog(null, confirmationPane, "Remove Song",
						JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (removeResult==JOptionPane.YES_OPTION){
					int index=list.getSelectedIndex();
					if (index==list.getModel().getSize()-1){
						index--;
					}
					playList.removeSong(list.getSelectedIndex());
					playList.stop();
					updatList();
					list.setSelectedIndex(index);
				}
			}
		}
	}
	/**
	 * change the oder of the song selected in the list when up or down buttons are clicked 
	 */
	private class changeOrderListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {


			int index = list.getSelectedIndex();
			if(e.getSource()==upButton){
				int i = playList.moveUp(index);
				updatList();
				list.setSelectedIndex(i);
			}	
			if(e.getSource()==downButton){
				int ind=playList.moveDown(index);
				updatList();
				list.setSelectedIndex(ind);
			}
		}
	}
	/**
	 * choosing a file and get the name of the file selected 
	 */
	private class ButtonActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			JFileChooser chooser = new JFileChooser(".");

			int status = chooser.showOpenDialog(null);

			if (status != JFileChooser.APPROVE_OPTION) {
				filePathFiled.setText("No File Chosen");
			} else {
				File file = chooser.getSelectedFile();		
				filePathFiled.setText("sounds/" +file.getName());		
				
			}
		}
	}
	
	/**
	 * play and stop and change the song every time a button from the song square is clicked
	 */
	private class squareButtonlist implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int songNum= playList.getSongArray().length;
			double listSize= Math.ceil(Math.sqrt(songNum));
			songButton=(JButton) e.getSource();				
			for (int i=0; i<listSize;i++){
				for(int j=0; j<listSize;j++){
					if (songSquareButtons[i][j].equals(songButton))
					{
						Song song = songSquare[i][j];
						playList.stop();
						stopTimer();
						playList.playSong(song);
						startTimer();
						int index = playList.getSongList().indexOf(song);
						list.setSelectedIndex(index);
						updateLable();
						int plays = songSquare[i][j].getPlayCount();
						getHeatMapColor(plays);
						songButton.setBackground(getHeatMapColor(plays));
					}
				}
			}
		}
	}
}



