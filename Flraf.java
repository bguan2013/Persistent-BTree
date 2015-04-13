import java.io.*;

/**CLass Flraf creating a RAF to save the BTree
 *
 *
 */


public class Flraf extends RandomAccessFile{

	int blockSize; // 28 bytes for strings in word.txt
	File directory;
	int lengthSize;
	
	
	/**constructor that takes a file
 	 *
     *@param size    Takes in the blockSize
     */

	

	public Flraf(int size) throws IOException{

			super(new File("PTree.flraf"),"rw");
			directory = new File("PTree.flraf");
			blockSize = size;
			lengthSize = 0;
			
		
	}

	/**constructor with one argument uses it's own file
 	 *
 	 *@param f 		 Takes in a file to manipulate
     *@param size    Takes in the blockSize
     *
     */	
	public Flraf(File f, int size)throws IOException{

			super(f,"rw");

			blockSize = size;
			directory = f;
			lengthSize = 0;
		
	}

	/**An extension of the super read, it reads the word at a certain block number
 	 *
     *@param blockNumber    finds the correct block 
     *return byte[] temp or null   This returns a byte array that it reads			
     */
	public byte[] read(int blockNumber){


		byte[] temp = new byte[blockSize];
		int offset = blockNumber*blockSize;
		try{
			
			super.seek(offset);
			super.read(temp);
			
			return temp;
		}
		catch(IOException e){

			e.printStackTrace();
			return null;
		}


	}
	/**An extension of the super write, it writes the word at a certain block number
 	 *
 	 *@param x				write the byte[] x to the block		
     *@param blockNumber    finds the correct block 
     */
	public void write(byte[] x, int blockNumber){

		
		int offset = blockNumber*blockSize;
		try{
			//System.out.println(y.length);
			//System.out.println(blockSize);
			//System.out.println(offset);

	///We assume that x is at the correct length, we are putting spacing in in simple service class

			super.seek(offset);
			super.write(x);
			
			lengthSize++;
		}
		catch(IOException e){

			e.printStackTrace();
		}	
	}

	
	public void readHeader(){

	}
	


}