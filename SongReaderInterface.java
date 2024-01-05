import java.io.FileNotFoundException;
import java.util.List;

public interface SongReaderInterface {
    public List<SongInterface> readMusicFromFile(String fileName) throws FileNotFoundException;
}
