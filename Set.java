
//
// Task 1. Set<T> class (5%)
//
// A set of a data structure that stores only unique data
// This class is used in DisjointSets<T> to store actual data in the same sets
//

//You cannot import additonal items
import java.util.AbstractCollection;
import java.util.Collection;
// import java.util.ArrayList;
import java.util.Iterator;
//You cannot import additonal items

/**
 * Set class.
 * Implements Set.
 * @author Sarah Fakhry
 */
public class Set<T> extends AbstractCollection<T>
{
	/**
	 * Initializing new node. 
     */
	Node<T> temp = new Node<T>();

	/**
	 * Adding if item is not in set already.
     * @param item
     * @return true or false
     */
	public boolean add(T item)
	{
		Node<T> curr = null;
		// Looping through node
		for (curr = temp.next; curr != null;) {
			// Checking if item exists in set
			if (curr.data == item) {
				return false;
			}
			else {
				// Add to set if node doesn't exist
				curr.next.data = item;
				return true;
			}
		}
		return false;
	}

	/**
	 * Adding eveything from other using iterator to step 
	 * thru into node then return true.
     * @param other
     * @return true or false
     */
	public boolean addAll(Set<T> other)
	{
		return temp.data == other;
	}

	/**
	 * Clearing everything in node.
     */
	public void clear()
	{
		temp = null;
	}

	/**
	 * Geting size of node.
     * @return size of node
     */
	public int size()
	{
		return ((Collection<T>) temp).size();
	}

	/**
	 * Initialize an interator.
     */
	public Iterator<T> iterator()
	{
		int p = -1;
		return new Iterator<T>()
		{
			/**
			 * Getting data at the next node.
			 * @return data
			 */
			public T next()
			{
				// Checking if empty
				if (temp.next == null) {
					return null;
				}
				else {
					// Return data at next node
					return temp.next.data;
				}
			}

			/**
			 * Validating if node exists next to it.
			 * @return true or false
			 */
			public boolean hasNext()
			{
				return (p < (size() -1));
			}
		};
	}

	/**
     * Private node class to use linked list.
     */
	private class Node<T>
	{
		Node(){}
		public T data;
		public Node<T> next;
	}
}

//bd4f0c8da4c44278947c5d1a79d71d1f
