


import java.io.*;
import java.lang.Iterable;
import java.util.Iterator;
import java.util.Comparator;
import java.lang.Comparable;
import java.io.Serializable;


/** Class Natural Comparator, work as a normal comparator 
 *	like the one in java.util.Comparator.
 */

public class NaturalComparator<T extends Comparable<T>> implements Comparator<T>, Serializable{



	//creates a default constructor;

	/** Works like the compareTo() method in Comparator class
	 *
	 * @param a				The first element it takes, can be any type that Comparable class takes	
 	 * @param b 			The second element it takes, can be any type that Comparable class takes
 	 * @return 				Returns an int that is either positive, negative or zero if the value of a is smaller, bgger or equal to b
 	 */

	public int compare(T a, T b){

		return a.compareTo(b);


	}


	/** Overwriting the method in Comparator class equals()
	 *	
	 *@param obj 			- the reference object with which to compare.
	 *@return 				True only if the specified object is also a comparator and it imposes the same ordering as this comparator.
	 */	
	public boolean equals(Object obj){


		return (obj != null) && (obj instanceof NaturalComparator);


	}



}