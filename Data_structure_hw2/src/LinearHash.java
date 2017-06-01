
// In-memory adaptation of Linear Hashing for Strings.
//
// Bongki Moon (bkmoon@snu.ac.kr), Mar/27/2017

import java.util.*;

public class LinearHash {
	ArrayList<ArrayList<String>> hashTable;
	long colli_count;
	int size;
	int extendedsize;
	String temp = "";
	
	
	public LinearHash(int HTinitSize) {
		hashTable = new ArrayList<ArrayList<String>>();
		for(int i=0; i< HTinitSize; i++){
			hashTable.add(new ArrayList<String>());
		}
		size = HTinitSize;
		extendedsize = size*2;
		colli_count = 0;
	}

	public int insertUnique(String word) {
		long hf = MyUtil.ELFhash(word, size);
		long hf_next = MyUtil.ELFhash(word, extendedsize);
		ArrayList<String> new_entry = new ArrayList<String>();
		
		if(hf < colli_count){
			for(int i=0; i<hashTable.get((int)hf_next).size(); i++){
				if (hashTable.get((int)hf_next).get(i).equals(word)){
					return -1;
				}
			}
		}
		else {
			for(int i=0; i<hashTable.get((int)hf).size(); i++){
				if (hashTable.get((int)hf).get(i).equals(word)){
					return -1;
				}
			}
		}
		
		if(hf > colli_count-1){ //just insert
			hashTable.get((int)hf).add(word);
			if(hashTable.get((int)hf).size() >= 2){ //collision!
				hashTable.add(new_entry);
				for(int i = 0; i<hashTable.get((int)colli_count).size() ; i++){
					temp = hashTable.get((int)colli_count).get(i);
					long split = MyUtil.ELFhash(temp, size);
					long split_ex = MyUtil.ELFhash(temp, extendedsize);
					if(split != split_ex){
						hashTable.get((int)split_ex).add(temp);
						hashTable.get((int)split).remove(temp);
						i--;
					}
					temp = "";
				}
				colli_count++;
				if(colli_count == size){
					size = extendedsize;
					extendedsize = size*2;
					colli_count = 0;
				}
			}
			return (int)hf;
		}
		else {  // extend
			hashTable.get((int)hf_next).add(word);
			if(hashTable.get((int)hf_next).size() >= 2){
				hashTable.add(new_entry);
				for(int i = 0; i<hashTable.get((int)colli_count).size() ; i++){
					temp = hashTable.get((int)colli_count).get(i);
					long split = MyUtil.ELFhash(temp, size);
					long split_ex = MyUtil.ELFhash(temp, extendedsize);
					if(split != split_ex){
						hashTable.get((int)split_ex).add(temp);
						hashTable.get((int)split).remove(temp);
						i--;
					}
					temp = "";
				}
				colli_count++;
				if(colli_count == size){
					size = extendedsize;
					extendedsize = size*2;
					colli_count = 0;
				}
			}
			return (int)hf_next;
		}	
	} // insert `word' to the Hash table.

	public int lookup(String word) {
		long hf = MyUtil.ELFhash(word, size);
		int num_of_words = hashTable.get((int)hf).size();
		if(hf <= colli_count-1){
			hf = MyUtil.ELFhash(word, extendedsize);
			num_of_words = hashTable.get((int)hf).size();
		}
		
		for(int i=0; i<num_of_words; i++){
			if (hashTable.get((int)hf).get(i).equals(word)){
				return num_of_words;
			}
		}
		
		return (-1)*num_of_words;
		
	} // look up `word' in the Hash table.

	public int wordCount() {
		int wordcount = 0;
		for(int i=0; i<hashTable.size() ; i++){
			wordcount += hashTable.get(i).size(); 
		}
		return wordcount;
	}

	public int emptyCount() {
		int emptycount = 0;
		for(int i=0; i<hashTable.size() ; i++){
			if(hashTable.get(i).size() == 0){
				emptycount++;
			}
		}
		return emptycount;
	}

	public int size() {
		return hashTable.size();
	} // 2^k + collisions in the current round
	
	public void print(){
		int i = 0;
		while(i < hashTable.size()){
			Collections.sort(hashTable.get(i));
			System.out.print("[" + i + ":");
			for(int j=0; j < hashTable.get(i).size() ; j++){
				System.out.print(" "+hashTable.get(i).get(j));
			}
			System.out.println("]");
			i++;
		}
	}

}
