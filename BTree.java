import java.util.Comparator;
import java.lang.Comparable;
import java.io.*;
import java.util.ArrayList;
import java.lang.Integer;
import java.lang.NullPointerException;

/**Btree class, compares any tyoe in that extends Comparable, making it Serializable 
 *
 *
 */

public class BTree<E extends Comparable<E>> implements Serializable{


	

	BTN<E> root;
	Cache cache;
	Comparator<E> ordering;
	int size;
	public static ArrayList<Integer> hdrInfo; //first number is the offset blockNumber, second is


	/**No-arg Constructor   uses the natural comparator
	 *
	 *
	 */

	public BTree(Cache c){

		try{
			cache = c;
			root = root.fetch(0);
			ordering = new NaturalComparator<E>();
			size = 0;
		}
		catch(NullPointerException n){
			cache = c;
			root = null;
			ordering = new NaturalComparator<E>();
			size = 0;
		}


	}


	/**one-arg Constructor   uses the comparator you give
	 *
	 *@param o 	The comparator the user passes to 			
	 */	
	public BTree(Comparator<E> o, Cache c){

		ordering = o;
		cache = c;
		root = root.fetch(0);
		size = 0;


	}
	/**The add method 			preventing from calling the actual insertion
	 *
	 *@param value 	 		the input value
	 */

	public void add(String value){


		insert(value, root, null);
		size++;

	}
	/**The remove method 	preventing from calling the actual delete method	
	 *
	 *@param value 	 		the remove value
	 */
	public void remove(String value){


		delete(value, root, null);
		size--;
	}
	/*public void display(){

		System.out.print("Root: ");
		for(int i = 0; i < root.keys.size(); i++){

			System.out.print(root.keys.get(i) + " ");

		}
		System.out.print("\n");
		if(!root.noLinks()){

			for(int i = 0; i < root.links.size(); i++){

				System.out.print("Link " + i + ": ");

				for(int j = 0; j < root.links.get(i).keys.size(); j++){

					System.out.print(root.links.get(i).keys.get(j) + " ");
				}

				System.out.print("\n");
			}

		}

	}*/

	/**The real add method  protected using private
	 *
	 *@param s 				The value that it's adding
	 *@param h 				The current node
	 *@param p 				The parent node
	 */
	//h is current btn, and p is parent btn
	private void insert(String s, BTN<E> h, BTN<E> p){


			int getOut = 0;

			if(h != null && h.isFull()){

				if(h == root){

					h.splitRoot(h);
					
					//root = h;
				}
				else{ //h != root;

					int pos = h.split(p);
					//h = p.links.get(pos);
					
					//should be h
					/*if(ordering.compare(p.keys.get(pos), s) > 0){

						insert(s, h, p);
					}*/
					if(p.keys.get(pos).compareTo(s) < 0){

						insert(s, p.fetch(p.links.get(pos+1)), p);
						getOut = -1;
					}

				}



			}

			if(getOut == 0){
				if(root == null){

					root = new BTN<E>(cache);	
					root.blockNumber = 0;				
					root.keys.add(s);
					root.save();
				}
				else{


					if(h.noLinks()){

						h.keys.add(s);
						//System.out.println(h.keys.lastIndexOf(s));
						while((h.keys.lastIndexOf(s)-1) >= 0 && h.keys.get(h.keys.lastIndexOf(s)).compareTo(h.keys.get(h.keys.lastIndexOf(s)-1)) < 0){



							int n = h.keys.lastIndexOf(s);						
							//System.out.println("Switching in progress!");	
							String temp = h.keys.get(n);
							h.keys.set(n, h.keys.get(n-1));
							h.keys.set(n-1, temp);
							//System.out.println("Switching word: " + (String)h.keys.get(h.keys.lastIndexOf(s)) + " to " + (String)h.keys.get(h.keys.lastIndexOf(s)-1));

						}
						h.save();
					}

					else{
						int i = 0;
						while(i < h.keys.size() && h.keys.get(i).compareTo(s) < 0){

							i++;
						}
						this.insert(s, h.fetch(h.links.get(i)), h);


					}
									
				}
			}


	}

	/**The real remove method  protected using private
	 *
	 *@param s 				The value that we want to remove
	 *@param h 				The current node
	 *@param p 				The parent node
	 */

	private void delete(String value, BTN<E> h, BTN<E> p){

		/////////////HAVEN'T SAVE STATE YET//////////////////

		if(p != null){
			
			if(p.keys.size() != p.links.size()-1){
				System.out.println("Wrong Node!");
			}				
			

		}


		if(h.search(value)){


				int index = -1;
				

				//this is for tracking h at the index of p
			if(p != null && p.links.size() != 0){///used || before && don't know why
				index = p.links.lastIndexOf(new Integer(h.blockNumber));
			}

			if(h == root && root.noLinks()){

				root.keys.remove(value);
			}

			else{
				//leaf	
				if(h.noLinks() && h.keys.size() >= 4){

					//System.out.println("Removing inside its own.");
					h.keys.remove(value);

				}
				//leaf
				else if(h.noLinks() && h.keys.size() < 4){


					if(index+1 < p.links.size() && p.fetch(p.links.get(index+1)).keys.size() >= 4){
						//System.out.println("Stealing from right neighbor.");
							h.keys.remove(value);
							h.keys.add(p.keys.get(index));
							p.keys.set(index, p.fetch(p.links.get(index+1)).keys.get(0));
							p.fetch(p.links.get(index+1)).keys.remove(0);

					}
					else if(index-1 >= 0 && p.fetch(p.links.get(index-1)).keys.size() >= 4){
						//System.out.println("Stealing from left neighbor.");
							h.keys.remove(value);
							h.keys.add(0, p.keys.get(index-1));
							p.keys.set(index-1, p.fetch(p.links.get(index-1)).keys.get(p.fetch(p.links.get(index-1)).keys.size()-1));
							p.fetch(p.links.get(index-1)).keys.remove(p.fetch(p.links.get(index-1)).keys.size()-1);


					}	
					//merge with h+1
					else if(index+1 < p.links.size() && p.fetch(p.links.get(index+1)).keys.size() < 4){


							//System.out.println("Merging with h+1!");
							h.keys.remove(value);
							h.keys.add(p.keys.get(index));

							for(int i = 0; i < p.fetch(p.links.get(index+1)).keys.size(); i++){
							h.keys.add(p.fetch(p.links.get(index+1)).keys.get(i));						
							}
							p.keys.remove(index);
							p.links.remove(index+1);
							if(p.keys.size() < 3){
								if(p == root){
									if(p.keys.size() == 0){
															
										root = h;
									}
								}	
								else{	
									if(p.keys.size()-1 >= index){
										mergeSwitch(p.keys.get(index), root, null);
									}
									else{
										mergeSwitch(p.keys.get(index-1), root, null);
									}
								}
							}
					}
					//merge with h-1
					//this will only happen when you are deleting some key in the last link of the parent
					//actually this will never happen becuase the parent can't be a full node when you are at a leaf
					//unless it's in the root's last link
					else if(index-1 >= 0 && p.fetch(p.links.get(index-1)).keys.size() < 4){


						//Something wrong about this part, not considering merging with the root
							//System.out.println("Merging with h-1!");
							h.keys.remove(value);
							p.fetch(p.links.get(index-1)).keys.add(p.keys.get(index-1));

							for(int i = 0; i < p.fetch(p.links.get(index)).keys.size(); i++){
							p.fetch(p.links.get(index-1)).keys.add(p.fetch(p.links.get(index)).keys.get(i));						
							}
							p.keys.remove(index-1);
							p.links.remove(index);
							if(p.keys.size() < 3){

								if(p == root){

									if(p.keys.size() == 0){
										
										root = p.fetch(p.links.get(index-1));
									}

								}

								else{
									//System.out.println("Something wrong!!!");
									mergeSwitch(p.keys.get(index-2), root, null);
								}
							}

					}

				}

				//internal node	
				else{
					//go down to replace the removed value with the bottom value, 
					//then delete(bottom value, h, p) !!!!NOT delete(bottom value, root, null) because it's also in the internal node
					//I chose the right bottom value
					//System.out.println("Removing from the internal!");
					BTN<E> pTemp = h;
					BTN<E> hTemp = pTemp.fetch(pTemp.links.get(h.keys.lastIndexOf(value)+1));

					

					while(!hTemp.noLinks()){
						pTemp = hTemp;
						hTemp = hTemp.fetch(hTemp.links.get(0));
						//System.out.println("Looping in internal!");
					}
					String debug = hTemp.keys.get(0);
					boolean pass = false;

					/*if(hasElement(debug, root)){
						pass = true;
					}*/


					h.keys.set(h.keys.lastIndexOf(value), debug);
							
					/*	
					if(hasElement(debug, root) && pass){
						System.out.println("Passed!");
					}
					else{
						System.exit(0);
					}*/


					delete(debug, hTemp, pTemp);


					
				}
			}

		}
		else{
			//if can't find it in h, go to it's links to find it
			int i = 0;

			while(i < h.keys.size() && value.compareTo(h.keys.get(i)) > 0){

					i++;

			}
			//System.out.println("Looping to find h!");
			delete(value, h.fetch(h.links.get(i)), h);
			
		}





	}
	/**Searching, insert root first
	 *
	 *@param value			search for the correct value
	 *@param h 				gets the current node
	 */
	public boolean hasElement(String value, BTN<E> h){

		if(h == null){

			System.out.println("Root is empty!");
			return false;

		}
		else if(h.search(value)){

			return true;

		}	
		else{
			if(h.noLinks()){

				return false;
			
			}
			else{
				int i = 0;
				while(i < h.keys.size() && value.compareTo(h.keys.get(i)) > 0){

					i++;

				}

				return hasElement(value, h.fetch(h.links.get(i)));
			}

		}
		
	}

	/**for parent that has been merged and after, keys less than 3 HAVE TO THINK ABOUT ROOT!!!!!
	 *
	 *@param m
	 *@param h
	 *@param p
	 */
	private void mergeSwitch(String m, BTN<E> h, BTN<E> p){

		/////////////HAVEN'T SAVE STATE YET//////////////////


		if(h.search(m)){


			int index = -1;
			if(p != null && p.links.size() != 0){
				index = p.links.lastIndexOf(new Integer(h.blockNumber));
			}

			/*if(h == root){

				if(root.keys.size() == 0){
					root = h;
				}
			}*/

			if(index+1 < p.links.size() && p.fetch(p.links.get(index+1)).keys.size() >= 4){


				h.keys.add(p.keys.get(index));
				p.keys.set(index, p.fetch(p.links.get(index+1)).keys.get(0));
				h.links.add(p.fetch(p.links.get(index+1)).links.get(0));
				p.fetch(p.links.get(index+1)).keys.remove(0);
				p.fetch(p.links.get(index+1)).links.remove(0);



			}
			else if(index-1 >= 0 && p.fetch(p.links.get(index-1)).keys.size() >= 4){


				h.keys.add(0, p.keys.get(index-1));
				p.keys.set(index-1, p.fetch(p.links.get(index-1)).keys.get(p.fetch(p.links.get(index-1)).keys.size()-1));
				h.links.add(0, p.fetch(p.links.get(index-1)).links.get(p.fetch(p.links.get(index-1)).links.size()-1));
				p.fetch(p.links.get(index-1)).keys.remove(p.fetch(p.links.get(index-1)).keys.size()-1);
				p.fetch(p.links.get(index-1)).links.remove(p.fetch(p.links.get(index-1)).links.size()-1);
				

			}
			//merge with h+1
			else if(index+1 < p.links.size() && p.fetch(p.links.get(index+1)).keys.size() < 4){
						
				
				h.keys.add(p.keys.get(index));

				for(int i = 0; i < p.fetch(p.links.get(index+1)).keys.size(); i++){
	
					h.keys.add(p.fetch(p.links.get(index+1)).keys.get(i));

	
				}
				for(int i = 0; i < p.fetch(p.links.get(index+1)).links.size(); i++){
	
					h.links.add(p.fetch(p.links.get(index+1)).links.get(i));
											
	
				}
				p.keys.remove(index);
				p.links.remove(index+1);

				if(p.keys.size() < 3){
					if(p == root){
						if(p.keys.size() == 0){

							root = h;
						}
					}	
					else{	
						if(p.keys.size()-1 >= index){
							mergeSwitch(p.keys.get(index), root, null);
						}
						else{
							mergeSwitch(p.keys.get(index-1), root, null);
						}
					}
				}
				

						

			}
			//merge with h-1
			else if(index-1 >= 0 && p.fetch(p.links.get(index-1)).keys.size() < 4){


				p.fetch(p.links.get(index-1)).keys.add(p.keys.get(index-1));

				for(int i = 0; i < p.fetch(p.links.get(index)).keys.size(); i++){

					p.fetch(p.links.get(index-1)).keys.add(p.fetch(p.links.get(index)).keys.get(i));						
				
				}

				for(int i = 0; i < p.fetch(p.links.get(index)).links.size(); i++){
				
					p.fetch(p.links.get(index-1)).links.add(p.fetch(p.links.get(index)).links.get(i));						
				
				}

				p.keys.remove(index-1);
				p.links.remove(index);
				if(p.keys.size() < 3){
					if(p == root){
						if(p.keys.size() == 0){	
			
							root = p.fetch(p.links.get(index-1));

						}
					}	
					else{
						mergeSwitch(p.keys.get(index-2), root, null);
					}
				}


					
			}

		}
		else{
			int i = 0;
				while(i < h.keys.size() && m.compareTo(h.keys.get(i)) > 0){

					i++;

				}
				mergeSwitch(m, h.fetch(h.links.get(i)), h);

		}
		

	}

	public void saveToFile(File sF)throws IOException{

	 FileOutputStream fos = new FileOutputStream(sF);
     ObjectOutputStream oos = new ObjectOutputStream(fos);
     oos.writeObject(hdrInfo);
     oos.close();
     fos.close();



	}
	public void openFile(File oF)throws IOException, ClassNotFoundException{

		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(oF));
	 		hdrInfo = (ArrayList<Integer>)ois.readObject();
	 		ois.close();
	 	}
	 	catch(EOFException e){
	 		hdrInfo = new ArrayList<Integer>();
	 	}

	}

}

