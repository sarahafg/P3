
// Task 1. DisjointSets class (10%)

// hint: you can use the DisjointSets from your textbook

import java.util.ArrayList;

/**
 * Disjoint sets class, using union by size and path compression.
 * @author Sarah Fakhry
 */
public class DisjointSets<T>
{
  /**
   * Constructor.
   * @param data
   */
  public DisjointSets( ArrayList<T> data  )
  {
    s = new int [data.size()];
    // Looping through data
    for (int i = 0; i < data.size(); i++) {
      // Putting element i in data at index i of the new arraylist
      s[i] = i;
    }
  }

  /**
   * Getting union of roots given.
   * @param root1
   * @param root2
   * @return id of new root
   */
  public int union( int root1, int root2 )
  {
    // Using path compression to get the union of root 1 and 2
    int x = find(root1);       
    int y = find(root2);  
    // Root id
    return s[x] = y;
  }
  
  /**
   * Implementing path compression to find root.
   * @param x
   * @return root
   */
  public int find( int x )
  {
    // Looping through arraylist by checking inside of arraylist
    while(s[x] != x) { 
      // Setting x to index of arraylist
      x = s[x];
    }
    return x;
  }

  /**
   * Getting all data in the same set.
   * @param root
   * @return set
   */
  public Set<T> get( int root )
  {
    return sets.get(root);
  }

  /**
   * Getting the number of disjoint sets remaining.
   * @return number of remaining disjoint sets
   */
  public int getNumSets()
  {
    return sets.size(); 
  }

  /**
   * Data.
   */
  private int [ ] s;

  /**
   * An array of sets, each set stores unique values in the set 
   * such as a set of pixels.
   */
  private ArrayList<Set<T>> sets;
}
