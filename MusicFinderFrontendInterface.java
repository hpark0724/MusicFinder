/**
 * Frontend interface for Music Finder project.
 *
 */
public interface MusicFinderFrontendInterface {
    public void runCommandLoop();
    public char mainMenuPrompt();
    public void loadDataCommand();
    public void addSong();
    public String searchTitleCommand();
    public void searchDurationByTitle(String title);
    public void searchArtistsByTitle(String title);
    public void displayStatsCommand();
    public void displaySearchHistory();
}

