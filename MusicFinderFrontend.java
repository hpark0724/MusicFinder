import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This is the Frontend class for the Music Finder Project. It implements the
 * MusicFinderFrontendInterface and utilizes the MusicFinderBackendInterface developed by another
 * team member for it's implementation.
 * 
 */
public class MusicFinderFrontend implements MusicFinderFrontendInterface {
  private Scanner userInput; // to get user's input
  private MusicFinderBackendInterface backend; // to use methods from backend
  private String title; // to store most recently search title
  private List<String> history; // stores song search history

  /**
   * Constructor to initialize instance variables
   * 
   * @param userInput - to read user input/ files
   * @param backend   - backend implementation
   */
  public MusicFinderFrontend(Scanner userInput, MusicFinderBackendInterface backend) {
    this.userInput = userInput;
    this.backend = backend;
    this.title = "";
    this.history = new ArrayList<String>();
  }

  /**
   * Interactive user interface, loops until user quits by inputting "Q"/"q"
   */
  @Override
  public void runCommandLoop() {
    System.out.println("================================================");
    System.out.println("Welcome to the Music Finder App!");
    System.out.println("================================================");

    char command = '\0';
    while (command != 'Q') { // main loop continues until user chooses to quit
      command = this.mainMenuPrompt();
      switch (command) {
        case 'L': // [L]oads playlist from file
          loadDataCommand();
          break;
        case '+': // Add[+] Song
          addSong();
          break;
        case 'D': // Search Song [D]uration
          title = searchTitleCommand();
          searchDurationByTitle(title);
          break;
        case 'A': // Search Song [A]rtist
          title = searchTitleCommand();
          searchArtistsByTitle(title);
          break;
        case 'T': // Search Song by title


        case 'I': // Display Playlist [I]nformation
          displayStatsCommand();
          break;
        case 'H': // Display Search [H]istory
          displaySearchHistory();
          break;
        case 'Q': // [Q]uit
          System.out.println("Thanks for using Music Finder!");
          break;
        default:
          System.out.println(
              "Unrecognizable command. Pick a command by selecting one of the letters within []s.");
          break;
      }
    }
  }

  /**
   * Main prompt for the user to interact with.
   */
  @Override
  public char mainMenuPrompt() {
    // prints prompt
    System.out.println("Pick a command from the list below!\n" + "    [L]oads playlist from file\n"
        + "    Add Songs [+]\n" + "    Search Song [D]uration\n" + "    Search Song [A]rtist\n"
        + "    Search Song [I]nformation\n" + "    Display Search [H]istory\n" + "    [Q]uit\n");
    System.out.print("Enter command: ");

    String input = userInput.nextLine().trim();
    if (input.length() == 0) // if user's choice is blank, return null character
      return '\0';
    // otherwise, return an uppercase version of the first character in input
    return Character.toUpperCase(input.charAt(0));
  }

  /**
   * Loads data from a csv file to the playlist. Error will be thrown when file is not found or
   * fails to load.
   */
  @Override
  public void loadDataCommand() {
    System.out.print("Enter the name of the file to load: ");
    String filename = userInput.nextLine().trim();
    try {
      backend.loadData(filename);
      System.out.println(filename + " loaded successfully!");
    } catch (FileNotFoundException e) {
      System.out.println("Error: Could not find or load file " + filename);
    }
  }

  /**
   * Prompts user to input the song to be added based on this format [title, duration, artist],
   * errors will be thrown if empty string is entered or doesn't follow format. [/] is used to split
   * instead of [,] because some songs may contain commas.
   */
  @Override
  public void addSong() {
    System.out.println("Add your song in this format [title/duration/artist], press [Q] to exit:");
    String input = userInput.nextLine().trim();
    // checks if user included brackets in the input
    if(input.startsWith("[") && input.endsWith("]")) {
      input.replaceAll("[]", "");
    }

    String[] details = input.split("/"); // splits String into it's respective categories

    if (details[0].toUpperCase().equals("Q") && details.length == 1) { // check if user wants to
      // quit
      return;
    }

    while (details.length != 3) { // checks if 3 details are included
      System.out.println("Invalid data/format entered! Try again.");
      System.out
          .println("Add your song in this format [title/duration/artist], press [Q] to exit:");
      input = userInput.nextLine().trim();
      details = input.split("/");

      if (details[0].toUpperCase().equals("Q") && details.length == 1) {
        return;
      }
    }
    // checks if duration consists of numbers and/or : only
    if(!details[1].trim().matches("[0-9:]+")) {
      System.out.println("Ensure duration entered is either in ms or min:sec format");
      addSong();
      return;
    }
    try {
      backend.addOneSong(details[0].trim(), details[1].trim(), details[2].trim());
      System.out.println("Song successfully added!");
    } catch (NullPointerException npe) { // missing/empty parts
      System.out
          .println("Missing data! Make sure the song is in this format [title/duration/artist]."
              + "\nTry again.");
      addSong();
    } catch (IllegalArgumentException iae) { // duplicate song
      System.out.println("Song already exists! Try again.");
      addSong();
    }
  }

  /**
   * Command to collect song title input from user. Uses recursion if an empty title is detected.
   * 
   * @return a non-empty song title.
   */
  @Override
  public String searchTitleCommand() {
    System.out.print("Enter the song title: ");
    String input = userInput.nextLine().trim();
    if (input.length() == 0) { // checks if user inputs empty string
      System.out.println("Title can't be empty. Try again.");
      return searchTitleCommand(); // recursion until non-empty string is inputted
    }

    return input;
  }

  /**
   * Searches for the song duration based on their song title.
   */
  @Override
  public void searchDurationByTitle(String words) {
    try {
      String result = backend.getDurationByTitle(words);

      if (result == null) {// if result is null then the song doesn't exist
        System.out.println("Song title doesn't exist.");
      } else {
        storeSong(words);
        System.out.println("Duration of " + words + ": " + result);
      }
    } catch (Exception e) {
      System.out.println("Song title doesn't exist.");
    }
  }

  /**
   * Searches for an artist based on their song title.
   */
  @Override
  public void searchArtistsByTitle(String words) {
    try {
      String result = backend.getArtistByTitle(words);

      if (result == null) { // if result is null then the song doesn't exist
        System.out.println("Song title doesn't exist.");
      } else {
        storeSong(words);
        System.out.println("Artist of " + words + ": " + result);
      }
    } catch (Exception e) {
      System.out.println("Song title doesn't exist.");
    }
  }

  /**
   * Displays statistics/information of the song searched. It should include the title, duration and
   * artist.
   */
  @Override
  public void displayStatsCommand() {
    title = searchTitleCommand();
    try {
      String stats = backend.findSongByTitle(title);

      if (stats == null) { // if stats is null then the song doesn't exist
        System.out.println("Song title doesn't exist.");
      } else {
        storeSong(title); // adds to search history
        System.out.println(title + "\n" + stats);
      }
    } catch (Exception e) {
      System.out.println("Song title doesn't exist.");
    }
  }

  /**
   * Displays list of song search history (previously searched songs either from duration, artist or
   * information)
   */
  @Override
  public void displaySearchHistory() {
    if (history.isEmpty()) { // no song is searched yet
      System.out.println("Search history is empty.");
    } else {
      System.out.println("Total songs in playlist: " + backend.displaySongCount()
          + "\nTotal searched songs: " + history.size());
      int i = 1;
      for (String s : history) {
        System.out.println(i++ + ". " + s);
      }
    }
  }

  /**
   * Helper method to store song search history with title, artist & duration
   * 
   * @param title - of the song to be stored
   */
  private void storeSong(String title) {
    try {
      // formats the string to be stored by title, then details
	String format = title + " by " + backend.findSongByTitle(title);
      // checks if the song has already been searched before
      if (!history.contains(format)) {
        history.add(format);
      }
    } catch (Exception e) {
    }
  }

  public static void main(String[] args) {
    RedBlackTree<SongInterface> rbt = new RedBlackTree<SongInterface>();
    MusicFinderBackendInterface back = new MusicFinderBackend(rbt, new SongReader());
    Scanner sc = new Scanner(System.in);
    MusicFinderFrontendInterface front = new MusicFinderFrontend(sc, back);
    
    front.runCommandLoop();
  }
}


