import java.net.*;
import java.io.*;
import java.util.Scanner;

public class SimpleService {



    static final int PORT = 2398;  // use the 2-digit number you selected in class for "47"




  public static void main(String[] args) {
    try {
      ServerSocket serverSocket = new ServerSocket(PORT);

      //initialize btree with words.txt
      Flraf flraf = new Flraf(new File("flraf.btr"), 228);
      Flraf header = new Flraf(new File("header.hdr"), 4);
      Cache cache = new Cache(flraf, header);
      BTree<String> bTree = new BTree<String>(cache);
      
      

      for (;;) {
        Socket client = serverSocket.accept();
        
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        System.out.println("Port initialized!");
        String cmd = in.readLine();
        String rpy;

        System.out.println(cmd);
        String[] stringArray = cmd.split(" ");
        String prototype = stringArray[1];
        String input = prototype.substring(1);

        if(input.isEmpty()){

          rpy = "Nothing input!";

        }
        else{
              if(input.substring(0,1).equals("-")){

                if(input.substring(1).equals("") || input.substring(1).equals(" ")){

                  rpy = "No string input to remove.";

                }
                else{

                  if(btree.hasElement(input.substring(1), btree.root)){

                    btree.remove(input.substring(1));
                    rpy = "String " + input.substring(1) + " has been removed!";

                  }
                  else{

                    rpy = "String " + input.substring(1) + " hasn't been found!";

                  }

                }


              }
              else{

                
                boolean spelling = input.matches("[a-zA-Z]+");


                if(spelling){

                  if(btree.hasElement(input, btree.root)){

                    rpy = "Already has the string " + input + ".";
                  }
                  else{

                    btree.add(input);
                    rpy = "String " + input + " has been added!";

                  }

                }
                else{

                  rpy = "Not a word! Can't be added to the BTree!";
                }

              }
        }      
        //System.out.println(input);
        
        

        

        
        String reply = "<html>\n" +
          "<head><title>Testing</title></head>\n" + 
          "<body><h1>Hello World!</h1></body>\n" +
          "Got request:<br>\n " + 
          cmd + ": " + rpy + 
          "\n</html>\n";

        int len = reply.length();

        out.println("HTTP/1.0 200 OK");
        out.println("Content-Length: " + len);
        out.println("Content-Type: text/html\n");
        out.println(reply);

        out.close();
        in.close();
        client.close();
      }
    }
    catch (IOException ex) {
      ex.printStackTrace();
      System.exit(-1);
    }
  }
}
