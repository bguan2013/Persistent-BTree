import java.io.*;
import java.util.Scanner;


public class tester{



/** This tester class writes everything to the  flraf then reads everything to the command line
 *
 */

	
public static void main(String[] args){




	
	File fos = new File("tester.flraf");
	
	if(!fos.exists()){
		try{
			fos.createNewFile();
			
		}
		catch(IOException e){

			e.printStackTrace();

		}	

	}
	File word = new File("words.txt");
	
	

	try{	
		Flraf test = new Flraf(fos, 32);
		Scanner scan = new Scanner(word);

		int i = 0;
      	
      	while(scan.hasNext()){

      	
        	String temp = scan.next();
        	byte[] b = temp.getBytes();
        	test.write(b, i); 
        	i++;
    	}

    	
    	for(int j = 0;j < test.lengthSize; j++){
	    	
	    	String a = new String(test.read(j));
	    	System.out.println(a);
    	}
	}
	catch(IOException e){

		e.printStackTrace();

	}
}





	
}