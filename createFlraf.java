import java.io.*;
import java.util.Scanner;


public class createFlraf{





public static void main(String[] args)throws IOException, ClassNotFoundException{

	File btr = new File("flraf.btr");
	
	if(!btr.exists()){
		try{
			btr.createNewFile();
			
		}
		catch(IOException e){

			e.printStackTrace();

		}	

	}
	File hdr = new File("flraf.hdr");
	
	if(!hdr.exists()){
		try{
			hdr.createNewFile();
			
		}
		catch(IOException e){

			e.printStackTrace();

		}	

	}
	File word = new File("words.txt");


	


	try{	
		
		Flraf flraf = new Flraf(btr, 228);
		Cache cache = new Cache(flraf);
		BTree<String> bTree = new BTree<String>(cache);
		bTree.openFile(hdr);
		Scanner scan = new Scanner(word);

		
      	
      	while(scan.hasNext()){

      		bTree.add(scan.next());
      		
        	
    	}
    	cache.flush();
    	bTree.saveToFile(hdr);
    	
    	
	}
	catch(IOException e){

		e.printStackTrace();

	}





}





}