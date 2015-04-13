import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.lang.NumberFormatException;
import java.lang.Integer;

/**The simple service class, creates a website
  *
  */

public class SimpleService {



    static final int PORT = 2398;  // use the 2-digit number you selected in class for "47"


  /**The main method, keeps the website alive using while
   *
   */

  public static void main(String[] args){
    try {
      ServerSocket serverSocket = new ServerSocket(PORT);

      File fos = new File("tester.flraf");
      if(!fos.exists()){
        try{
          fos.createNewFile();
        }
        catch(IOException e){

          e.printStackTrace();

        } 

      }

      Flraf test = new Flraf(fos, 32);
      //File word = new File("words.txt");
      //initialize btree with words.txt
      
      

      for (;;) {
        Socket client = serverSocket.accept();
        
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        System.out.println("Port initialized!");
        String cmd = in.readLine();
        String rpy = "";

        //System.out.println(cmd);
        String[] stringArray = cmd.split(" ");
        String prototype = stringArray[1];
        String input = prototype.substring(1);
        String reply = "";
        System.out.println("!"+input+"!");
        //I am setting the range to the formate of XXXX-XXXX

        if(input.isEmpty() || input.equals("favicon.ico")){

          rpy = "Nothing input!";

                

        }
        else{
            
            String[] stringArray1 = input.split("-");
            String indexAS = stringArray1[0];
            String indexBS = "";
            if(stringArray1.length == 2){
              
              indexBS = stringArray1[1];

            }
            int indexA = Integer.parseInt(indexAS);
            int indexB = -1;
            if(!indexBS.equals("")){

              indexB = Integer.parseInt(indexBS);


            }
            if(indexB != -1){
              while(indexA != indexB){


                String b = new String(test.read(indexA));
                rpy = rpy + b;
                indexA++;
              }

            }
            else{

              rpy = new String(test.read(indexA));

            }


        }      
        //System.out.println(input);
        
        

        reply = "<html>\n" +
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
