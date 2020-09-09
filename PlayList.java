import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * playList.java
 * 
 * driver class for MyTunesGUIPanel has an implements of MyTunesPlayListInterface
 * CS 121 Project 5: My Tunes GUI
 * @author adeltouati
 *
 */


public class PlayList implements MyTunesPlayListInterface {

	private String name;
	private ArrayList<Song> songList;
	private Song playing;

	/**
	 * @param name
	 */
	public PlayList(String name) {
		playing = null;
		songList = new ArrayList<Song>();
		this.name = name;
	}
	
	
	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param playing
	 */
	public void setPlaying(Song playing) {
		this.playing = playing;
	}
	/**
	 * @param songList
	 */
	public void setSongList(ArrayList<Song> songList) {
		this.songList = songList;
	}
	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return playing
	 */
	public Song getPlaying() {
		return playing;
	}
	/**
	 * @return songList
	 */
	public ArrayList<Song> getSongList() {
		return songList;
	}
	/**
	 * adding song 
	 */
	public void addSong(Song song) {
		songList.add(song);
	}
	/**
	 * getting songs number
	 * @return songList.size()
	 */
	public int getNumSongs() {
		return songList.size();
	}
	/**
	 * 
	 * getting the song
	 * @param i 
	 * @return songList.get(i)
	 */
	public Song getSong(int i) {
		if (i >= 0 && i < songList.size()) {
			return songList.get(i);
		} else {
			return null;
		}
	}
	/**
	 * removing songs
	 * 
	 * @param i
	 * @return songList.remove(i)
	 */
	public Song removeSong(int i) {
		if (i >= 0 && i < songList.size()) {
			return songList.remove(i);
		} else {
			return null;
		}
	}
	/**
	 *  
	 * @return total
	 */
	public int getTotalPlayTime() {
		int total = 0;
		for (int i = 0; i < songList.size(); i++) {
			Song s = songList.get(i);
			total += s.getPlayTime();
		}
		return total;
	}
	/** 
	 *  playing the song 
	 */
	public void playSong(int i) {
		if (i >= 0 && i < songList.size()) {
			songList.get(i).play();
		}
	}
	/**
	 * getting all the info 
	 * @return string
	 */
	public String getInfo() {
		String string = "";
		ArrayList<Song> temporaryList = songList;
		int total = 0;
		if (temporaryList.size() > 0) {
			Song longest = temporaryList.get(0);
			Song shortest = temporaryList.get(0);
			for (Song temporarySong : temporaryList) {
				total += temporarySong.getPlayTime();
				int SongTime = temporarySong.getPlayTime();
				int LogestSong = longest.getPlayTime();
				int shortestsong = shortest.getPlayTime();
				if (SongTime > LogestSong) {
					longest = temporarySong;
				}
				if (SongTime < shortestsong) {
					shortest = temporarySong;
				}
			}
			double average = (double) total / (double) temporaryList.size();
			DecimalFormat myFormat = new DecimalFormat("0.00");
			string += "The average play time is: " + myFormat.format(average) + " seconds\n";
			string += "The shortest song is: " + shortest + "\n";
			string += "The longest song is: " + longest + "\n";
			string += "Total play time: " + total + " seconds\n";
		} else {
			string += "There are no songs.\n";
		}
		return string;
	}
	public String toString() {
		String string = "";
		ArrayList<Song> tList = songList;
		if (tList.size() > 0) {
			string += "------------------\n";
			string += "Test List (" + tList.size() + " songs)\n";
			string += "------------------\n";
			for (int i = 0; i < tList.size(); i++) {
				string += "(" + i + ") " + tList.get(i) + "\n";
			}
			string += "------------------\n";
		}else {
			string += "------------------\n";
			string += "Test List (0 songs)\n";
			string += "------------------\n";
			string += "There are no songs.\n";
			string += "------------------\n";
		}
		return string;
	}
	/**
	 * loading song info from a file 
	 */
	@Override
	public void loadFromFile(File file) {
		try {
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()) {
				String title = scan.nextLine().trim();
				String artist = scan.nextLine().trim();
				String playtime = scan.nextLine().trim();
				String songPath = scan.nextLine().trim();

				int colon = playtime.indexOf(':');
				int minutes = Integer.parseInt(playtime.substring(0, colon));
				int seconds = Integer.parseInt(playtime.substring(colon+1));
				int playtimesecs = (minutes * 60) + seconds;

				Song song = new Song(title, artist, playtimesecs, songPath);
				this.addSong(song);
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.err.println("Failed to load playlist. " + e.getMessage());
		}

	}
	
	/**
	 *playing the song
	 */
	@Override
	public void playSong(Song song) {
		playing = song;
		playSong(songList.indexOf(song));
	}
	/**
	 * stopping the song 
	 */
	@Override
	public void stop() {
		if (playing != null){
			playing.stop();
			playing = null;
		}
	}
	/**
	 * getting the song array
	 * @return songArray
	 */
	@Override
	public Song[] getSongArray() {
		Song[] songArray= songList.toArray(new Song[songList.size()]);
		return songArray;
	}
	/**
	 * moving up the song
	 * @param index
	 * @return getNumSongs()-1
	 * @return index-1
	 */
	@Override
	public int moveUp(int index) {
		songList.get(index);
		Song s = songList.remove(index);

		if (index==0){
			songList.add(getNumSongs(), s);
			return getNumSongs()-1;
		}else{
			songList.add(index-1, s);
			return index-1;
		}
	}
	/**
	 * moving down the song
	 * @param index
	 * @return index
	 * @return index+1
	 */
	@Override
	public int moveDown(int index) {
		songList.get(index);
		Song s= songList.remove(index);
		if (index==getNumSongs()){
			songList.add(0, s);
			index= songList.indexOf(s);
			return index;
		}else {
			songList.add(index+1, s);
			return index+1;	
		}
	}
	/**
	 * getting 2D array
	 * @return copy1
	 */
	@Override
	public Song[][] getSongSquare() {

		double dimension = Math.ceil(Math.sqrt(songList.size()));
		Song[][] copy1 = new Song[(int)dimension][(int)dimension];
		for (int row=0; row<dimension;row++){
			for (int col=0;col<dimension;col++){
				copy1[row][col]=songList.get((int)(col+row*dimension)%songList.size());
			}
		}
		return copy1;
	}
}
