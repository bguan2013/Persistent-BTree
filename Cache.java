import java.io.*;


public class Cache {


	int[] blockNumber;
	boolean[] cleaness;  ///if clean, overwrite, if dirty don't overwrite
	byte[][] keysnLinks;

	Flraf file;	
	//File header;

	public Cache(Flraf f) throws IOException{

		blockNumber = new int[]{-1, -1, -1, -1};
		cleaness = new boolean[]{true, true, true, true};
		keysnLinks = new byte[4][];
		file = f;
		
		
	}


	public boolean isClean(int bN){

		int index = -1;
		for(int i = 0; i < 4; i++){

			if(blockNumber[i] == bN){

				index = i;

			}

		}
		if(cleaness[index]){
			return true;
		}
		else{
			return false;
		}


	}
	public int inCache(int bN){

		for(int i = 0; i < 4; i++){

			if(blockNumber[i] == bN){
				return i;
			}

		}
		return -1;

	}

	
	public byte[] read(int bN){

		int pos = inCache(bN);
		if(pos != -1){
			

			return keysnLinks[pos]; 
		}
		else{
			
			return readFromDisc(bN);

		}


	}
	private byte[] readFromDisc(int bN){

		int index = returnBlock();
		keysnLinks[index] = file.read(bN);
		blockNumber[index] = bN;
		cleaness[index] = true;
		return keysnLinks[index];

	} 

	public void write(int bN, byte[] btn){

		//check if cache is full or not, if not full, write to cache, if full, write the top most clean cache to the disc then overwrite the new node to cache
		int index = inCache(bN);
		if(index != -1){

			keysnLinks[index] = btn;
			cleaness[index] = false;
			
		}
		else{
			
			int index1 = returnBlock();
			keysnLinks[index1] = btn;
			cleaness[index1] = false;
			blockNumber[index1] = bN;
			
		}	
	}
	private void writeToDisc(int bN, byte[] btn){

		file.write(btn, bN);

	}

	public void flush(){

		for(int i = 0; i < 4; i++){
			writeToDisc(blockNumber[i], keysnLinks[i]);
		}
		blockNumber = new int[]{-1, -1, -1, -1};
		cleaness = new boolean[]{true, true, true, true};
		keysnLinks = new byte[4][];


	}
	/*private int inCacheNode(byte[] btn){

		for(int i = 0; i < 4; i++){

			if(keysnLinks[i] == btn){

				return i;
			}


		}
		return -1;

	}*/
	private int returnBlock(){
		
		
		//HERE we need to make sure if the cache is LFU or LRU
		for(int i = 0; i < 4; i++){

			if(blockNumber[i] == -1){
				return i;
			}

		}
		for(int j = 0; j < 4; j++){

			if(cleaness[j]){

				return j;
			}
		}
		
		flush();
		return 0;

	}


	

}