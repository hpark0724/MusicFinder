/**
 * This class is set the artists, title and duration of the song
 */
public class Song implements SongInterface {

    private String title;
    private String artist;
    private String duration;

    /**
     * 
     * this method set three arguments artists, title and duration
     * 
     * @param artist   artist name of the song
     * @param title    title of the song
     * @param duration duration of the song
     */
    public Song(String title, String artist, String duration) {
        this.artist = artist;
        this.title = title;
        this.duration = duration;
    }

    /**
     * get artist name of the song
     * 
     */
    public String getArtist() {
        return artist;
    }

    /**
     * get title name of the song
     * 
     */
    public String getTitle() {
        return title;
    }

    /**
     * get duration of the song
     * 
     */
    public String getDuration() {
        return duration;
    }

    /**
     * this method compare two song's duration
     * 
     * @param newSong song which is to be compared
     * @return it will return the positive or negative value when the new song
     *         duration is greater or less than the song duration
     */
    @Override
    public int compareTo(SongInterface newSong) {
        return this.title.compareTo(newSong.getTitle());
    }

    /**
     * this method will make song as a stirng
     * 
     * @return it will return the artise, title and duration as a string
     */
    @Override
    public String toString() {
        return title + " by " + artist + ", (" + duration + ")";
    }

}
