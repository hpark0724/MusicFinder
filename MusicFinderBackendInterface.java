import java.io.FileNotFoundException;

public interface MusicFinderBackendInterface {
  // public MusicFinderBackend(RedBlackTreeInterface<SongInterface> redBlackTree,
  // SongReaderInterface songReader);
  
  //load csv data into rbtree
  public void loadData(String filename) throws FileNotFoundException;
  
  //returns a complete string with title, artist, and duration information
  public String  findSongByTitle(String words) throws IllegalArgumentException, IllegalStateException, NullPointerException;
  
  //returns only the title in a string
  public String  getDurationByTitle(String words) throws IllegalArgumentException, IllegalStateException, NullPointerException;
  
  //returns only the artist in a string
  public String getArtistByTitle(String words) throws IllegalArgumentException, IllegalStateException, NullPointerException;
  
  //adds a song to the tree with song detail input from user
  public boolean addOneSong(String title, String duration, String artist)throws NullPointerException, IllegalArgumentException;
  
  //returns the numbers of songs in the rbtree in a string
  public int displaySongCount();

}
