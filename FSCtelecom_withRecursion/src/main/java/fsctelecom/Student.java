package fsctelecom;

public class Student {

	private int ID;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private double balance;
	private String[] calledNumbers;
	private Integer[] callDuration;
	private String[] textedNumbers;
	private static int numStudents;
	private Student next;

	public Student() {

	}

	public Student(int iD, String firstName, String lastName, String phoneNumber, double balance) {
		ID = iD;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.balance = balance;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String[] getCalledNumbers() {
		return calledNumbers;
	}

	public void setCalledNumbers(String[] calledNumbers) {
		this.calledNumbers = calledNumbers;
	}

	public Integer[] getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(Integer[] callDuration) {
		this.callDuration = callDuration;
	}

	public String[] getTextedNumbers() {
		return textedNumbers;
	}

	public void setTextedNumbers(String[] textedNumbers) {
		this.textedNumbers = textedNumbers;
	}

	public static int getNumStudents() {
		return numStudents;
	}

	public static void setNumStudents(int numStudents) {
		Student.numStudents = numStudents;
	}

	public Student getNext() {
		return next;
	}

	public void setNext(Student next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "Student [ID=" + ID + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber="
				+ phoneNumber + ", balance=" + balance + "]";
	}
}
