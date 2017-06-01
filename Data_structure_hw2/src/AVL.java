public class AVL extends BST {

	public AVL() {
	}
	
	public void insert(String key) {
		T = inserthelpAVL(T, key);
	}

	public Node inserthelpAVL(Node t, String key) {
		if (t == null) {
			t = new Node();
			t.key = key;
			t.frequency++;
		} else {
			if (t.key.compareTo(key) > 0) {
				t.left = inserthelpAVL(t.left, key);
				if (height(t.left) - height(t.right) == 2) {
					if (t.left.key.compareTo(key) > 0) {
						t = switchright(t);
					} else {
						t = switchleftright(t);
					}
				}
			} else if (t.key.compareTo(key) < 0) {
				t.right = inserthelpAVL(t.right, key);
				if (height(t.right) - height(t.left) == 2) {
					if (t.right.key.compareTo(key) < 0) {
						t = switchleft(t);
					} else {
						t = switchrightleft(t);
					}
				}
			} else {
				t.frequency++;
			}
		}
		t.height = Math.max(height(t.left), height(t.right)) + 1;
		return t;
	}

	public int height(Node t) {
		int height = 0;
		if (t == null)
			height = 0;
		else {
			height = t.height;
		}
		return height;
	}

	public Node switchright(Node t) {
		Node temp = t.left;
		t.left = temp.right;
		temp.right = t;
		t.height =  Math.max(height(t.left), height(t.right)) + 1;
		temp.height =  Math.max(height(temp.left), height(t)) +1 ;
		return temp;
	}

	public Node switchleftright(Node t) {
		t.left = switchleft(t.left);
		return switchright(t);
	}

	public Node switchleft(Node t) {
		Node temp = t.right;
		t.right = temp.left;
		temp.left = t;
		t.height =  Math.max(height(t.left), height(t.right)) + 1;
		temp.height = Math.max(height(temp.right), height(t)) + 1;
		return temp;
	}

	public Node switchrightleft(Node t) {
		t.right = switchright(t.right);
		return switchleft(t);
	}

}
