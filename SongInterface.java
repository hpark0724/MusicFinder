public interface SongInterface extends Comparable<SongInterface> {
    public String getTitle();

    public String getArtist();

    public String getDuration();

    public int compareTo(SongInterface newSong);

    public String toString();
}
