

public class testMethodParameter{


	String a;




public void test(String h){


	a = "def";
	System.out.println(h);

}





public static void main(String[] args){


	testMethodParameter nba = new testMethodParameter();
	
	nba.a = "abc";
	nba.test(nba.a);
	System.out.println(nba.a);




}
}