
/**
 * @author Martina Pironkova
 * Student ID - 2068174
 */


public class CountedElement<E extends Comparable<E>> implements Comparable<CountedElement<E>> {
	private E element;
	private int count;

	public CountedElement(E e, int count){
		this.element = e;
		this.count = count;
	}
	
	public CountedElement(E e){
		this.element = e;
	}

	//add getters and setters
	
	public E getElement() {
		return element;
	}

	public int getCount() {
		return count;
	}

	public void setElement(E ele) {
	    this.element = ele;
    }

    public void setCount(int anotherCount) {
	    this.count = anotherCount;
    }

	//add toString() method
    
	public String toString() {
		return element.toString()+", "+count;
	}
	
	
	public int compareTo(CountedElement<E> sC1) {
    
		if(getElement().compareTo(sC1.getElement()) < 0) {
		    return -1;
        } else if(getElement().compareTo(sC1.getElement()) > 0) {
		    return 1;
        } else {
		    return 0;
        }
	}

}
