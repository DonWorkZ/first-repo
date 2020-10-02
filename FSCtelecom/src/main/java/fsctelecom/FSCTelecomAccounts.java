package fsctelecom;

public class FSCTelecomAccounts {

	// Defining Node class
	class Node {
		Student st;
		Node next;
		Node prev;

		public Node(Student student) {
			this.st = student;
			this.next = null;
		}
	}

	// Defining head tail and size
	public Node head = null;
	public Node tail = null;
	private int size = 0;

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		if (head == null) {
			return true;
		}
		return false;
	}

	/**
	 * Checks student object in list
	 * 
	 * @param o
	 * @return true if it contains else false
	 */
	public boolean containsStudent(int o) {

		boolean res = false;
		Node current = head;

		while (current != null) {
			if (current.st.getID() == o) {
				res = true;
				break;
			}
			current = current.next;
		}
		return res;
	}

	/**
	 * add new student node in list sorted manner
	 * 
	 * @param stud
	 * @return
	 */
	public boolean add(Student stud) {
		Node newNode = new Node(stud);

		Node current;
		if (head == null) {
			head = newNode;
			tail = newNode;
		} else if (head.st.getID() >= newNode.st.getID()) {
			newNode.next = head;
			newNode.next.prev = newNode;
			head = newNode;
		} else {
			current = head;

			while (current.next != null && current.next.st.getID() < newNode.st.getID()) {
				current = current.next;
			}

			newNode.next = current.next;
			if (current.next != null) {
				newNode.next.prev = newNode;
			}

			current.next = newNode;
			newNode.prev = current;
		}
		size++;

		return true;
	}

	/**
	 * remove student node from list if contains
	 * 
	 * @param st
	 * @return
	 */
	public boolean remove(Student st) {
		Node temp = head, prev = null;

		// If head node itself holds the key to be deleted
		if (temp != null && temp.st.getID() == st.getID()) {
			head = temp.next; // Changed head
			return true;
		}

		while (temp != null && temp.st.getID() != st.getID()) {
			prev = temp;
			temp = temp.next;
		}

		if (temp == null) {
			return false;
		}

		prev.next = temp.next;
		if (temp.next != null) {
			temp.next.prev = prev;
		}
		size--;
		return true;
	}

	/**
	 * returns student object
	 * 
	 * @param id
	 * @return
	 */
	public Student getStudentById(int id) {
		Student res = null;
		Node current = head;

		while (current != null) {
			if (current.st.getID() == id) {
				res = current.st;
				break;
			}
			current = current.next;
		}
		return res;
	}

	/**
	 * updates student node
	 * 
	 * @param st
	 */
	public void updateStudent(Student st) {
		Node current = head;

		while (current != null) {
			if (current.st.getID() == st.getID()) {
				current.st = st;
				break;
			}
			current = current.next;
		}
	}

	/**
	 * prints linked list
	 */
	public void printList() {
		Node currNode = head;

		System.out.print("LinkedList: ");

		// Traverse through the LinkedList
		while (currNode != null) {
			// Print the data at current node
			System.out.println(currNode.st + " ");

			// Go to next node
			currNode = currNode.next;
		}
	}

}
