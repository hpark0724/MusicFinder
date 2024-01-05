import java.io.FileNotFoundException;
import java.util.List;

/**
 *
 */
public class MusicFinderBackend implements MusicFinderBackendInterface {

  private RedBlackTree<SongInterface> tree;
  private SongReaderInterface songReader;
  private int songCount;
  
  
  public MusicFinderBackend(RedBlackTree<SongInterface> tree,
      SongReaderInterface songReader) {
    this.tree = tree;
    this.songReader = songReader;
    this.songCount = 0;
  }
  
  @Override
  public void loadData(String filename) throws FileNotFoundException {
    List<SongInterface> songs = songReader.readMusicFromFile(filename);
    for (SongInterface song : songs) {
        addPostToRedBlackTree(song);
    }
  }
  
  
  /**
   * Helper method extracts individual words from a post's title and body, and
   * stores mapping from each to this post.
   * 
   * @param post contains the data to add to the searchable dataset
   */
  private void addPostToRedBlackTree(SongInterface song) {
    Song newSong = new Song(song.getTitle(), song.getArtist(), song.getDuration());
    tree.insert(newSong);
    songCount++;
  }

  public Song findSong(String title) {
    Song song = new Song(title, "", "");
    return song;
  }

@Override
  public String findSongByTitle(String title) throws IllegalArgumentException, IllegalStateException, NullPointerException {
    String artist = getArtistByTitle(title);
    String duration = getDurationByTitle(title);
    
    return artist + ", " + duration;
  }

@Override
  public String getDurationByTitle(String title) throws IllegalArgumentException, IllegalStateException, NullPointerException {
    Song song = new Song(title, "", "");
    if (!tree.get(song).getDuration().contains(":")) {
      long duration = Long.parseLong(((Song) tree.get(song)).getDuration());
    
    int seconds = (int) (duration / 1000) % 60;
    int minutes = (int) ((duration / (1000*60)) % 60);
    return "" + minutes + ":" + seconds;
    }
    else return tree.get(song).getDuration();
  }

  @Override
  public String getArtistByTitle(String title) throws IllegalArgumentException, IllegalStateException, NullPointerException {
    Song song = new Song(title, "", "");
    return ((Song) tree.get(song)).getArtist();
  }

  @Override
  public int displaySongCount() {
    return songCount;
  }


  @Override
  public boolean addOneSong(String title, String duration, String artist)
      throws NullPointerException, IllegalArgumentException {
      Song song = new Song(title, artist, duration);
    songCount++;
      return tree.insert(song);
  }

}
