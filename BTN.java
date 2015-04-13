import java.util.Comparator;
import java.lang.Comparable;
import java.util.ArrayList;
import java.io.Serializable;
import java.lang.Integer;
import java.nio.ByteBuffer;

public class BTN<E extends Comparable<E>> implements Serializable{


	ArrayList<String> keys;
	ArrayList<Integer> links;
	int blockNumber;
	Cache cacheN;
	//boolean isRoot;

	public BTN(Cache cache){

		keys = new ArrayList<String>(7);
		links = new ArrayList<Integer>(8);
		if(BTree.hdrInfo.size() == 0){
			blockNumber = 0;
			BTree.hdrInfo.add(new Integer(1));
		}
		else if(BTree.hdrInfo.size() == 1){
			blockNumber = BTree.hdrInfo.get(0).intValue();
			int inc = BTree.hdrInfo.get(0).intValue();
			inc++;
			BTree.hdrInfo.set(0, new Integer(inc));
		}
		else{
			blockNumber = BTree.hdrInfo.get(1).intValue();
			BTree.hdrInfo.remove(1);

		}
		cacheN = cache;
		//isRoot = false;


	}

	


	public boolean isFull(){


		if(keys.size() < 7){

			return false;
		}
		else{

			if(links.size() > 0 && links.size() != 8){

				System.out.println("Report! Wrong Node!");
				
			}
			//System.out.println(links.size());
			return true;

		}



	}
	public boolean noLinks(){

		if(links.size() == 0){

			return true;
		}
		else{

			return false;
		}


	}
	private BTN<E> move(){


		BTN<E> move = new BTN<E>(this.cacheN);

		if(this.noLinks()){

			
			move.keys.add(this.keys.get(4));
			move.keys.add(this.keys.get(5));
			move.keys.add(this.keys.get(6));
			
			this.keys.remove(6);
			this.keys.remove(5);
			this.keys.remove(4);

		}
		else{
			move.links.add(this.links.get(4));
			move.keys.add(this.keys.get(4));
			move.links.add(this.links.get(5));
			move.keys.add(this.keys.get(5));
			move.links.add(this.links.get(6));
			move.keys.add(this.keys.get(6));
			move.links.add(this.links.get(7));
			
			this.keys.remove(6);
			this.keys.remove(5);
			this.keys.remove(4);
			this.links.remove(7);
			this.links.remove(6);
			this.links.remove(5);
			this.links.remove(4);
		}

		return move;
	}
	private BTN<E> moveLeftPart(){


		BTN<E> mLP = new BTN<E>(this.cacheN);
		if(this.noLinks()){

			mLP.keys.add(this.keys.get(0));
			mLP.keys.add(this.keys.get(1));
			mLP.keys.add(this.keys.get(2));
			this.keys.remove(2);
			this.keys.remove(1);
			this.keys.remove(0);



		}
		else{
			mLP.links.add(this.links.get(0));
			mLP.keys.add(this.keys.get(0));
			mLP.links.add(this.links.get(1));
			mLP.keys.add(this.keys.get(1));
			mLP.links.add(this.links.get(2));
			mLP.keys.add(this.keys.get(2));
			mLP.links.add(this.links.get(3));
			
			this.keys.remove(2);
			this.keys.remove(1);
			this.keys.remove(0);
			this.links.remove(3);
			this.links.remove(2);
			this.links.remove(1);
			this.links.remove(0);
		}
		return mLP;



	}
	public int split(BTN<E> parent){

		
			int position = parent.links.indexOf(this);		
			parent.keys.add(position, this.keys.get(3));		
			BTN<E> u = this.move();
			this.keys.remove(3);
			parent.links.add(position+1, new Integer(u.blockNumber));

			parent.save();
			this.save();
			u.save();

		//System.out.println("Spliting leaf!");
		return position;
	}
	public void splitRoot(BTN<E> root){

	
		BTN<E> rootLeft;
		BTN<E> rootRight;

		rootRight = root.move();
		rootLeft = root.moveLeftPart();

		
		root.links.add(new Integer(rootLeft.blockNumber));
		root.links.add(new Integer(rootRight.blockNumber));

		root.save();
		rootLeft.save();
		rootRight.save();
		//System.out.println("Spliting root!");

	}








	

	protected boolean search(String s){

		
		int i = 0;

		if(keys == null){

			return false;
		}
		else{

			while(i < keys.size()){

				if(keys.get(i).compareTo(s) == 0){
					return true;
				}
				i++;

			}
			return false;

		}


	}

	private byte[] convertToByte(){


		
		byte[] temp = new byte[228];

		//for keys to bytes
		for(int i = 0; i < keys.size(); i++){
			String s = keys.get(i);
			while(s.length() < 28){

				s = s + " ";
			}
			 byte[] tmp = s.getBytes();
			for(int i1 =0; i1 < 28; i1++){

				temp[i1+i*28] = tmp[i1];
			} 
			 
		}
		//for empty keys to bytes
		if(keys.size() < 7){
			for(int j = keys.size(); j < 7; j++){
				String sp = "                            ";
				byte[] tmp2 = sp.getBytes();	
				for(int j1 =0; j1 < 28; j1++){

				temp[j1+j*28] = tmp2[j1];
			} 


			}	
		}

		//for links ints to bytes
		for(int m = 0; m < links.size(); m++){
			Integer inte = links.get(m);
			byte[] tmp3 = ByteBuffer.allocate(4).putInt(inte.intValue()).array();
			for(int m1 =0; m1 < 4; m1++){

				temp[m1+7*28+m*4] = tmp3[m1];
			} 
		}

		//for no links to "-1" to bytes
		if(links.size() < 8){
			for(int n = links.size(); n < 8; n++){
				byte[] tmp4 = ByteBuffer.allocate(4).putInt(-1).array();
				for(int n1 =0; n1 < 4; n1++){

					temp[n1+7*28+n*4] = tmp4[n1];
				}

			}

		}
		return temp;
	}

	private BTN<E> convertToBTN(byte[] b){

		BTN<E> temp = new BTN<E>(cacheN);
		for(int i = 0; i < 7; i++){
			String s = new String(b, 28*i, 28);
			String s2 = s.trim();
			if(!s2.equals("")){
				temp.keys.add(s2);
			}
		}
		for(int j = 0; j < 8; j++){

			byte[] y = new byte[]{b[28*7+j*4], b[28*7+j*4+1], b[28*7+j*4+2], b[28*7+j*4+3]};
			ByteBuffer bb = ByteBuffer.wrap(y);
			int l = bb.getInt();
			if(l != -1){ // setting no links to -1
				temp.links.add(new Integer(l));		
			}	
		}
		return temp;
	}
	public void save(){

		byte[] temp = convertToByte();
		cacheN.write(blockNumber, temp);

	}

	public BTN<E> fetch(int bN){


		byte[] temp = cacheN.read(bN);
		if(temp == null){
			return null;
		}
		return convertToBTN(temp);

	}
	


}

