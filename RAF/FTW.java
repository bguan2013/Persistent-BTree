import java.io.*;
import java.util.Scanner;

public class FTW {




public static void main(String[] args){


	try{
	    File words = new File("words.txt");
      Scanner scan = new Scanner(words);

      	String haha = "a";
      while(scan.hasNext()){

      	
        String temp = scan.next();
        if(temp.length() > haha.length()){

        	haha = temp;

        }
        
        
      }
  		System.out.println(haha);
  	}
  	catch(IOException ex){

  		System.out.println("IOException");

  	}
}






}