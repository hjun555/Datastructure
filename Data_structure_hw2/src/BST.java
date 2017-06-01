// Bongki Moon (bkmoon@snu.ac.kr), Mar/27/2017
class Node {
	String key = "";
	Node left;
	Node right;
	int frequency = 0;
	int accesscount = 0;
	int height = 0;
}

public class BST { // Binary Search Tree implementation
	Node T = null;

	public BST() {
	}

	public void insert(String key) {
		T = inserthelpBST(T, key);
	}

	public Node inserthelpBST(Node t, String key) {
		if (t == null) {
			t = new Node();
			t.key = key;
			t.frequency++;
		} else {
			if (t.key.compareTo(key) > 0) {
				t.left = inserthelpBST(t.left, key);
			}
			else if(t.key.compareTo(key) < 0){
				t.right = inserthelpBST(t.right, key);
			}
			else{
				t.frequency++;
			}
		}
		return t;
	}

	public boolean find(String key) {
		return findhelp(T, key);
	}
	public boolean findhelp(Node t, String key) {
		boolean check = false;
		if (t == null) {
			check = false;
		} else {
			if (t.key.compareTo(key) > 0) {
				t.accesscount++;
				check = findhelp(t.left, key);
			}
			else if(t.key.compareTo(key) < 0){
				t.accesscount++;
				check = findhelp(t.right, key);
			}
			else{
				t.accesscount++;
				check = true;
			}
		}
		return check;
	}

	public int size() {
		return sizehelper(T);
	}
	public int sizehelper(Node t){
		int sizecount = 0;
		if(t != null ){
			sizecount = 1;
			sizecount = sizehelper(t.left) + sizehelper(t.right) + sizecount;
		}
		return sizecount;
	}

	public int sumFreq() {
		return sumFreqhelp(T); 
	}
	public int sumFreqhelp(Node t){
		int sum = 0;
		if(t != null){
			sum =  sumFreqhelp(t.left) + sumFreqhelp(t.right) + t.frequency ;
		}
		return sum;
	}

	public int sumProbes() {
		return sumProbeshelp(T);
	}
	public int sumProbeshelp(Node t){
		int sum = 0;
		if(t != null){
			sum = sumProbeshelp(t.left) + sumProbeshelp(t.right) + t.accesscount;
		}
		return sum;
	}

	public void resetCounters() {
		resethelper(T);
	}	
	public void resethelper(Node t){
		if(t != null){
			t.frequency = 1;
			t.accesscount = 0;
			resethelper(t.left);
			resethelper(t.right);
		}		
	}

	public void print() {
		printhelper(T);
	}
	public void printhelper(Node t){
		if(t != null){
			printhelper(t.left);
			System.out.println("[" + t.key + ":" + t.frequency + ":" + t.accesscount + "]");
			printhelper(t.right);
		}

	}

}