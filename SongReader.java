import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This class will read the data file
 * 
 */
public class SongReader implements SongReaderInterface {

    /**
     * this is the method that add the song files data to the song
     * 
     * @param fileName file name will be "./songsReader.csv"
     * @exception FileNotFoundException if the file does not exist
     * @return musics array list which contains all data of the song files
     */
    public List<SongInterface> readMusicFromFile(String fileName) throws FileNotFoundException {
        List<SongInterface> songs = new ArrayList<>();

        try {
            FileInputStream input = new FileInputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            e.getMessage();
        }

        // scan the file
        Scanner in = new Scanner(new File(fileName));

        // it will jump the title and directly move to the body
        in.nextLine();

        // it will run while there is next line
        while (in.hasNextLine()) {
            // for each line in the file being read:
            String line = in.nextLine();
            // split that line into parts around around the delimiter: ,
            String[] parts = line.split(",");
            
            // add musics to the list
            songs.add(new Song(parts[1], parts[0], parts[2]));
            // then close the scanner before returning the list of posts
        }
        in.close();
        return songs;
    }

}
