import java.util.List;

public interface RedBlackTreeInterface<T extends Comparable<T>> extends SortedCollectionInterface<T> {

    // public RedBlackTreeInterface();

    // the method to insert a song into the RBT while maintaining RBT properties
    public boolean insert(T data) throws NullPointerException, IllegalArgumentException;

    // the method that checks whether the song is in the RBT
    public boolean contains(T data);

    // the method that removes a song from the RBT 
    // properties
    public boolean remove(T data) throws NullPointerException, IllegalArgumentException;

    // the method that returns the number of songs within the RBT
    public int size();

    // checks whether the RBT is empty
    public boolean isEmpty();

    // get a Song object by using just the title
    public T get(T data) throws IllegalArgumentException, IllegalStateException, NullPointerException;

    // empties the RBT of all data
    public void clear();

    // return a list of songs that start with what the user inputted
    public List<T> findByInput(String key) throws NullPointerException;

    // gets the number of black nodes within the RBT
    public int getNumBlackNodes();

    // gets the number of the red nodes within the RBT
    public int getNumRedNodes();

}
