import java.applet.AudioClip;
import java.awt.Color;
import java.applet.Applet;
import java.io.File;
import java.net.URL;
/**
 *
 * @author adeltouati
 */
public class Song
{
	// Used to play the song.
	private AudioClip clip;

	private String title;
	private String artist;
	private int playTime; // in seconds
	private String filePath;
	private int playCount;

	/**
	 * Constructor: Builds a song using the given parameters.
	 * @param title song's title
	 * @param artist song's artist
	 * @param playTime song's length in seconds
	 * @param filePath song file to load
	 */
	public Song(String title, String artist, int playTime, String filePath)
	{
		this.title = title;
		this.artist = artist;
		this.playTime = playTime;
		this.filePath = filePath;
		this.playCount = 0;

		String fullPath = new File(filePath).getAbsolutePath();
		try {
			this.clip = Applet.newAudioClip(new URL("file:" + fullPath));
		} catch(Exception e) {
			System.out.println("Error loading sound clip for " + fullPath);
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Returns the title of this <code>Song</code>.
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 *  Returns the artist of this <code>Song</code>.
	 * @return the artist
	 */
	
	public void setTitle(String newTitle){
		
		this.title=newTitle;
	}
	
	public String getArtist()
	{
		return artist;
	}
	
	public void setArtist(String newArtist){
		this.artist=newArtist;
	}
	

	
	
	/**
	 *  Returns the play time of this <code>Song</code> in seconds.
	 * @return the playTime
	 */
			

	public int getPlayTime()
	{
		return playTime;
	}
	
	public void setPlayTime(int newPlaytime){
		this.playTime=newPlaytime;
	}

	/**
	 * Returns the file path of this <code>Song</code>.
	 * @return the filePath
	 */
	public String getFilePath()
	{
		return filePath;
	}
	
	public void setFilePath(String newFilePath){
		this.filePath=newFilePath;
	}

	/**
	 * Returns the number of times this song has been played.
	 * @return the count 
	 */
	public int getPlayCount()
	{
		return playCount;
	}

	/**
	 * Plays this song asynchronously.
	 */
	public void play()
	{
		if(clip != null) {
			clip.play();
			playCount++;
			
		}
	}

	/**
	 * Stops this song from playing.
	 */
	public void stop()
	{
		if(clip != null) {
		
			clip.stop();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return String.format("%-20s %-20s %-25s %10d",
				title, artist, filePath, playTime);
	}
}
