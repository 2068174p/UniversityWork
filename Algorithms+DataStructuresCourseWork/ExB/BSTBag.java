import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Martina Pironkova
 * Student ID 2068174
 */


public class BSTBag<E extends Comparable<E>> implements Bag<E> {

    private Node<E> root;
	
    int size;

    //Constructor
    public BSTBag() {
        root = null;
        size = 0;
    }

    //Inner node class
    private static class Node<E extends Comparable<E>> {
    	
        protected CountedElement<E> el;
        protected Node<E> left, right;
        protected int counter;

        protected Node(E element) {
            el = new CountedElement<E>(element);
            left = null;
            right = null;
            counter = 1;
        }

        public boolean contains(E element) {
            CountedElement<E> countEl = new CountedElement<E>(element);
            int match = countEl.compareTo(el);
            if (match == 0 && counter > 0)
                return true;
            if (match < 0 && left != null && left.contains(element))
                return true;
            if (match > 0 && right != null && right.contains(element))
                return true;
            return false;
        }
    }

    //adds the element to the appropriate branch of the BST 
    //increment its count
    @Override
    public void add(E element) {
        int direction = 0;
        Node<E> parent = null;
        Node<E> curr = root;

        size++;

        while (true) {
            if (curr == null) {
                Node<E> bee = new Node<E>(element);
                bee.el.setCount(bee.counter);
                if (root == null) {
                    root = bee;
                } else if (direction < 0) {
                    parent.left = bee;
                } else {
                    parent.right = bee;
                }
                return;
            }

            CountedElement<E> elem = new CountedElement<E>(element);
            direction = elem.compareTo(curr.el);
            if (direction == 0) {
                curr.counter++;
                curr.el.setCount(curr.counter);
                return;
            }
            parent = curr;
            if (direction < 0) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
    }

    //isEmpty() checks if the BST is empty 
    @Override
    public boolean isEmpty() {
        if (root == null) {
            return true;
        } else return false;
    }

    //size() returns number of elements
    @Override
    public int size() {
        return size;
    }

    //contains(E element) confirms if element exists in the BST
    @Override
    public boolean contains(E element) {
        if (root == null) {
            return false;
        } else {
            return root.contains(element);
        }
    }

    //equals(Bag<E> that) compares two bags
    @Override
    public boolean equals(Bag<E> that) {
        // an iterator for each bag
        Iterator<E> one = this.iterator();
        Iterator<E> two = that.iterator();

        while (one.hasNext() && two.hasNext()) {

            if (!one.next().equals(two.next())) {
                return false;
            }
        }
        return true;
    }

    //clear() clears the BST 
    @Override
    public void clear() {
        root = null;
    }

    //remove an element
    @Override
    public void remove(E element) {

       Node<E> curr = root;
       int direction = 0;
       size--;
      
     

        while (true) {
            if (curr == null)
                return;

            CountedElement<E> elem = new CountedElement<E>(element);
            direction = elem.compareTo(curr.el);

            if (direction == 0) {
                curr.counter--;
                curr.el.setCount(curr.counter);
                return;
            }

            if (direction < 0)
                curr = curr.left;
            else
                curr = curr.right;
        }
    }

    @Override
    public Iterator iterator() {
        return new Organized();
    }

    //Inner iterator class
    
    private class Organized implements Iterator<E> {
        
    	private Stack<Node<E>> ordered;

        //contains references to nodes 
    	//stores the left most nodes of the BST such that the right sub-trees
		// can be iterated through
       
        private Organized() {
        	
            ordered = new LinkedStack<Node<E>>();
            for (Node<E> curr = (Node<E>) root; curr != null; curr = curr.left) {
                int count = curr.el.getCount();
                
                for (int i = 0; i < count; i++) {
                    if (count > 0)
                        ordered.push(curr);
                }
            }
        }
        
        //check if ordered is empty
        public boolean hasNext() {
            return (!ordered.empty());
        }

        // allows the next element in the iterator to be returned
        public E next() {
        	
            if (ordered.empty())
                throw new NoSuchElementException();
           
            Node<E> place = ordered.pop();
            for (Node<E> curr = place.right; curr != null; curr = curr.left) {
                int count = curr.el.getCount();
                for (int i = 0; i < count; i++) {
                    if (count > 0)
                        ordered.push(curr);
                }

            } return place.el.getElement();
        }
    }
}
