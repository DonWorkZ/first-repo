package fsctelecom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class FSCTelecom {
	private static DecimalFormat df2 = new DecimalFormat("0.00");

	public static void main(String[] args) {

		FSCTelecomAccounts users = new FSCTelecomAccounts();

		try (BufferedReader br = new BufferedReader(new FileReader("src/FSCTelecom.in"));
				FileWriter fw = new FileWriter("src/FSCTelecom.out")) {

			String line = br.readLine();
			boolean quitFound = false;
//			while (line != null && !quitFound) {}
			process(line, users, fw, quitFound, br);
			
			System.out.println("Successfully Completed");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void process(String line, FSCTelecomAccounts users, FileWriter fw, boolean quitFound, BufferedReader br) throws IOException
	{
		if(line == null && quitFound) {
			return;
		}
		String[] dtls = line.split("\\s");
		if (dtls[0].equals("ADDACCOUNT")) {
			addAccount(users, dtls);
			writeIntoOutFile(fw, dtls, null, null);
		} else if (dtls[0].equals("MAKECALL")) {
			makeCall(users, dtls, fw);
		} else if (dtls[0].equals("SENDTEXT")) {
			sendText(users, dtls, fw);
		} else if (dtls[0].equals("USEDATA")) {
			useData(users, dtls, fw);
		} else if (dtls[0].equals("RECHARGE")) {
			recharge(users, dtls, fw);
		} else if (dtls[0].equals("DELETEACCOUNT")) {
			deleteAccount(users, dtls, fw);
		} else if (dtls[0].equals("SEARCH")) {
			search(users, dtls, fw);
		} else if (dtls[0].equals("DISPLAYDETAILS")) {
			displayDetails(users, dtls, fw);
		} else if (dtls[0].equals("QUIT")) {
			quitFound = true;
			writeIntoOutFile(fw, dtls, null, null);
		}

		line = br.readLine();
		
		process(line, users, fw, quitFound, br);
	
	}

	private static void displayDetails(FSCTelecomAccounts users, String[] dtls, FileWriter fw) throws IOException {

		if (users.containsStudent(Integer.parseInt(dtls[1]))) {
			Student st = users.getStudentById(Integer.parseInt(dtls[1]));
			writeIntoOutFile(fw, dtls, null, st);
		} else {
			writeIntoOutFile(fw, dtls, "Cannot perform DISPLAYDETAILS. Account does not exist in FSC Telecom System.",
					null);
		}

	}

	private static void search(FSCTelecomAccounts users, String[] dtls, FileWriter fw) throws IOException {
		if (users.containsStudent(Integer.parseInt(dtls[1]))) {
			Student st = users.getStudentById(Integer.parseInt(dtls[1]));
			writeIntoOutFile(fw, dtls, null, st);
		} else {
			writeIntoOutFile(fw, dtls, "Cannot perform SEARCH. Account does not exist in FSC Telecom System.", null);
		}
	}

	private static void deleteAccount(FSCTelecomAccounts users, String[] dtls, FileWriter fw) throws IOException {
		if (users.containsStudent(Integer.parseInt(dtls[1]))) {
			Student st = users.getStudentById(Integer.parseInt(dtls[1]));
			users.remove(st);
			writeIntoOutFile(fw, dtls, null, st);
		} else {
			writeIntoOutFile(fw, dtls, "Cannot perform DELETEACCOUNT. Account does not exist in FSC Telecom System.",
					null);
		}
	}

	private static void recharge(FSCTelecomAccounts users, String[] dtls, FileWriter fw) throws IOException {
		if (users.containsStudent(Integer.parseInt(dtls[1]))) {
			Student st = users.getStudentById(Integer.parseInt(dtls[1]));

			writeIntoOutFile(fw, dtls, null, st);
			st.setBalance(Double.parseDouble(df2.format(st.getBalance() + Double.parseDouble(dtls[2]))));
			users.updateStudent(st);
		} else {
			writeIntoOutFile(fw, dtls, "Cannot perform RECHARGE. Account does not exist in FSC Telecom System.", null);
		}
	}

	private static void useData(FSCTelecomAccounts users, String[] dtls, FileWriter fw) throws IOException {

		if (users.containsStudent(Integer.parseInt(dtls[1]))) {
			Student st = users.getStudentById(Integer.parseInt(dtls[1]));

			double prevBalance = st.getBalance();
			double requiredBalance = Double.parseDouble(dtls[2]) / 102400;
			requiredBalance = requiredBalance * 100;
			requiredBalance = Math.floor(requiredBalance) / 100;

			if (prevBalance < requiredBalance) {
				writeIntoOutFile(fw, dtls, "Cannot perform USEDATA. Account does not have enough balance.", null);
			} else {
				writeIntoOutFile(fw, dtls, null, st);
				st.setBalance(Double.parseDouble(df2.format(st.getBalance() - requiredBalance)));
				users.updateStudent(st);
			}
		} else {
			writeIntoOutFile(fw, dtls, "Cannot perform USEDATA. Account does not exist in FSC Telecom System.", null);
		}

	}

	private static void sendText(FSCTelecomAccounts users, String[] dtls, FileWriter fw) throws IOException {
		if (users.containsStudent(Integer.parseInt(dtls[1]))) {
			Student st = users.getStudentById(Integer.parseInt(dtls[1]));
			double prevBalance = st.getBalance();
			if (prevBalance < 0.02) {
				writeIntoOutFile(fw, dtls, "Cannot perform SENDTEXT. Account has zero balance.", null);
			} else {
				writeIntoOutFile(fw, dtls, null, st);
				st.setBalance(Double.parseDouble(df2.format(st.getBalance() - 0.02)));

				if (st.getTextedNumbers() == null) {
					st.setTextedNumbers(new String[] { dtls[2] });
				} else {
					List<String> cn = Arrays.asList(st.getTextedNumbers());
					int limit = cn.size() + 1 > 10 ? 10 : cn.size();
					String[] arr = new String[limit + 1];

					arr[0] = dtls[2];
					for (int i = 0; i < limit; i++) {
						arr[i + 1] = cn.get(i);
					}
					st.setTextedNumbers(arr);

				}
				users.updateStudent(st);
			}
		} else {
			writeIntoOutFile(fw, dtls, "Cannot perform SENDTEXT. Account does not exist in FSC Telecom System.", null);
		}
	}

	private static void makeCall(FSCTelecomAccounts users, String[] dtls, FileWriter fw) throws IOException {
		if (users.containsStudent(Integer.parseInt(dtls[1]))) {
			// in cents
			double requiredBalance = Integer.parseInt(dtls[3]) * 5;
			Student st = users.getStudentById(Integer.parseInt(dtls[1]));

			int callDuration;
			int callCost;
			double prevBalance = st.getBalance() * 100;
			if (prevBalance < 5.0) {
				// insufficient balance
				writeIntoOutFile(fw, dtls, "Cannot perform MAKECALL. Account has insufficient balance.", false, st);
			} else if (prevBalance >= requiredBalance) {
				// success
				writeIntoOutFile(fw, dtls, null, false, st);
				callDuration = Integer.parseInt(dtls[3]);
				callCost = callDuration * 5;
				st.setBalance(Double.parseDouble(df2.format((prevBalance - requiredBalance) / 100)));
				if (st.getCalledNumbers() == null) {
					st.setCalledNumbers(new String[] { dtls[2] });
				} else {
					List<String> cn = Arrays.asList(st.getCalledNumbers());

					int limit = cn.size() + 1 > 10 ? 10 : cn.size();
					String[] arr = new String[limit + 1];

					arr[0] = dtls[2];
					for (int i = 0; i < limit; i++) {
						arr[i + 1] = cn.get(i);
					}
					st.setCalledNumbers(arr);

				}

				if (st.getCallDuration() == null) {
					st.setCallDuration(new Integer[] { callDuration });
				} else {
					List<Integer> cn = Arrays.asList(st.getCallDuration());

					int limit = cn.size() + 1 > 10 ? 10 : cn.size();
					Integer[] arr = new Integer[limit + 1];
					arr[0] = callDuration;
					for (int i = 0; i < limit; i++) {
						arr[i + 1] = cn.get(i);
					}
					st.setCallDuration(arr);

				}

				users.updateStudent(st);
			} else {
				// call terminate
				writeIntoOutFile(fw, dtls, "***Call terminated due to low balance.", true, st);

				callDuration = (int) ((st.getBalance() * 100) / 5);
				callCost = callDuration * 5;
				st.setBalance(Double.parseDouble(df2.format((prevBalance - callCost) / 100)));

				if (st.getCalledNumbers() == null) {
					st.setCalledNumbers(new String[] { dtls[2] });
				} else {
					List<String> cn = Arrays.asList(st.getCalledNumbers());

					int limit = cn.size() + 1 > 10 ? 10 : cn.size();
					String[] arr = new String[limit + 1];

					arr[0] = dtls[2];
					for (int i = 0; i < limit; i++) {
						arr[i + 1] = cn.get(i);
					}
					st.setCalledNumbers(arr);

				}

				if (st.getCallDuration() == null) {
					st.setCallDuration(new Integer[] { callDuration });
				} else {
					List<Integer> cn = Arrays.asList(st.getCallDuration());

					int limit = cn.size() + 1 > 10 ? 10 : cn.size();
					Integer[] arr = new Integer[limit + 1];
					arr[0] = callDuration;
					for (int i = 0; i < limit; i++) {
						arr[i + 1] = cn.get(i);
					}
					st.setCallDuration(arr);

				}

				users.updateStudent(st);
			}

		} else {
			// could not found
			writeIntoOutFile(fw, dtls, "Cannot perform MAKECALL. Account does not exist in FSC Telecom System.", false,
					null);
		}

	}

	private static void addAccount(FSCTelecomAccounts users, String[] dtls) {
		Student student = new Student(Integer.parseInt(dtls[1]), dtls[2], dtls[3], dtls[4], 20.0);
		users.add(student);
	}

	private static void writeIntoOutFile(FileWriter fw, String[] dtls, String msg, Student st) throws IOException {

		if (dtls[0].equals("ADDACCOUNT")) {
			fw.write("Command: ADDACCOUNT");
			fw.write("\r\t");
			fw.write("Name:          " + dtls[2] + " " + dtls[3]);
			fw.write("\r\t");
			fw.write("Student ID:    " + dtls[1]);
			fw.write("\r\t");
			fw.write("Phone Number:  " + dtls[4]);
			fw.write("\r\t");
			fw.write("Balance:       $20.00");
			fw.write("\r\r");
		} else if (dtls[0].equals("SENDTEXT")) {
			fw.write("Command: SENDTEXT");
			fw.write("\r\t");
			if (msg != null) {
				fw.write(msg);
				fw.write("\r\r");
			} else {
				fw.write("Name:           " + st.getFirstName() + " " + st.getLastName());
				fw.write("\r\t");
				fw.write("Phone Number:   " + st.getPhoneNumber());
				fw.write("\r\t");
				fw.write("Number Texted:  " + dtls[2]);
				fw.write("\r\t");
				fw.write("Prev Balance:   $" + df2.format(st.getBalance()));
				fw.write("\r\t");
				fw.write("Text Cost:      $0.02");
				fw.write("\r\t");
				fw.write("New Balance:    $" + df2.format((st.getBalance() - 0.02)));
				fw.write("\r\r");
			}
		} else if (dtls[0].equals("USEDATA")) {

			double requiredBalance = Double.parseDouble(dtls[2]) / 102400;
			requiredBalance = requiredBalance * 100;
			requiredBalance = Math.floor(requiredBalance) / 100;
			fw.write("Command: USEDATA");
			fw.write("\r\t");
			if (msg != null) {
				fw.write(msg);
				fw.write("\r\r");
			} else {
				fw.write("Name:           " + st.getFirstName() + " " + st.getLastName());
				fw.write("\r\t");
				fw.write("Phone Number:   " + st.getPhoneNumber());
				fw.write("\r\t");
				fw.write("Kilobytes:      " + dtls[2]);
				fw.write("\r\t");
				fw.write("Prev Balance:   $" + df2.format(st.getBalance()));
				fw.write("\r\t");
				fw.write("Data Cost:      $" + df2.format(requiredBalance));
				fw.write("\r\t");
				fw.write("New Balance:    $" + df2.format((st.getBalance() - requiredBalance)));
				fw.write("\r\r");
			}
		} else if (dtls[0].equals("RECHARGE")) {

			fw.write("Command: RECHARGE");
			fw.write("\r\t");
			if (msg != null) {
				fw.write(msg);
				fw.write("\r\r");
			} else {
				fw.write("Name:            " + st.getFirstName() + " " + st.getLastName());
				fw.write("\r\t");
				fw.write("Phone Number:    " + st.getPhoneNumber());
				fw.write("\r\t");
				fw.write("Recharge Amount: $" + df2.format(Double.parseDouble(dtls[2])));
				fw.write("\r\t");
				fw.write("New Balance:     $" + df2.format((st.getBalance() + Double.parseDouble(dtls[2]))));
				fw.write("\r\r");
			}
		} else if (dtls[0].equals("DELETEACCOUNT")) {

			fw.write("Command: DELETEACCOUNT");
			fw.write("\r\t");
			if (msg != null) {
				fw.write(msg);
				fw.write("\r\r");
			} else {
				fw.write("Name:           " + st.getFirstName() + " " + st.getLastName());
				fw.write("\r\t");
				fw.write("Student ID:     " + st.getID());
				fw.write("\r\t");
				fw.write("Phone Number:   " + st.getPhoneNumber());
				fw.write("\r\t");
				fw.write("Balance:        $" + df2.format(st.getBalance()));
				fw.append("\r\t");
				fw.write("***Account has been deleted.");
				fw.write("\r\r");
			}
		} else if (dtls[0].equals("SEARCH")) {

			fw.write("Command: SEARCH");
			fw.write("\r\t");
			if (msg != null) {
				fw.write(msg);
				fw.write("\r\r");
			} else {
				fw.write("Name:          " + st.getFirstName() + " " + st.getLastName());
				fw.write("\r\t");
				fw.write("Student ID:    " + st.getID());
				fw.write("\r\t");
				fw.write("Phone Number:  " + st.getPhoneNumber());
				fw.write("\r\t");
				fw.write("Balance:       $" + df2.format(st.getBalance()));
				fw.write("\r\r");
			}
		} else if (dtls[0].equals("DISPLAYDETAILS")) {

			fw.write("Command: DISPLAYDETAILS");
			fw.write("\r\t");
			if (msg != null) {
				fw.write(msg);
				fw.write("\r\r");
			} else {
				fw.write("Name:          " + st.getFirstName() + " " + st.getLastName());
				fw.write("\r\t");
				fw.write("Student ID:    " + st.getID());
				fw.write("\r\t");
				fw.write("Phone Number:  " + st.getPhoneNumber());
				fw.write("\r\t");
				fw.write("Called Numbers and Duration:");
				fw.write("\r\t");
				if (st.getCalledNumbers() != null) {

					int limit = st.getCalledNumbers().length > 10 ? 10 : st.getCalledNumbers().length;
					for (int i = 0; i < limit; i++) {
						fw.write("\t" + st.getCalledNumbers()[i] + " (" + st.getCallDuration()[i] + ")");
						fw.write("\r\t");
					}
				} else {
					fw.write("\t(user has not made any calls yet)");
					fw.write("\r\t");
				}
				fw.write("Texted Numbers:");
				fw.write("\r\t");
				if (st.getTextedNumbers() != null) {
					int limit = st.getTextedNumbers().length > 10 ? 10 : st.getTextedNumbers().length;
					for (int i = 0; i < limit; i++) {
						fw.write("\t" + st.getTextedNumbers()[i]);

						if (i == (limit - 1)) {
							fw.write("\r\r");
						} else {
							fw.write("\r\t");
						}
					}
				} else {
					fw.write("\t(user has not made any texts yet)");
					fw.write("\r\r");
				}

			}
		} else if (dtls[0].equals("QUIT")) {
			fw.write("Command: QUIT.");
			fw.write("\r\t");
			fw.write("Exiting the FSC Telecom System...");
			fw.write("\r\t");
			fw.write("Goodbye.");

		}

	}

	private static void writeIntoOutFile(FileWriter fw, String[] dtls, String msg, Boolean makeCallTerminate,
			Student st) throws IOException {

		fw.write("Command: MAKECALL");
		fw.write("\r\t");
		if (msg != null && makeCallTerminate) {

			int callDuration = (int) ((st.getBalance() * 100) / 5);
			double callCost = ((double) callDuration * 5) / 100;
			fw.write("Name:           " + st.getFirstName() + " " + st.getLastName());
			fw.write("\r\t");
			fw.write("Phone Number:   " + st.getPhoneNumber());
			fw.write("\r\t");
			fw.write("Number Called:  " + dtls[2]);
			fw.write("\r\t");
			fw.write("Call Duration:  " + callDuration);
			fw.write("\r\t");
			fw.write("Prev Balance:   $" + df2.format(st.getBalance()));
			fw.write("\r\t");
			fw.write("Call Cost:      $" + df2.format(callCost));
			fw.write("\r\t");
			fw.write("New Balance:    $" + df2.format((st.getBalance() - callCost)));
			fw.write("\r\t");
			fw.write(msg);
			fw.write("\r\r");
		} else if (msg != null) {
			fw.write(msg);
			fw.write("\r\r");
		}

		else {
			int callDuration = Integer.parseInt(dtls[3]);
			double callCost = ((double) callDuration * 5) / 100;
			fw.write("Name:           " + st.getFirstName() + " " + st.getLastName());
			fw.write("\r\t");
			fw.write("Phone Number:   " + st.getPhoneNumber());
			fw.write("\r\t");
			fw.write("Number Called:  " + dtls[2]);
			fw.write("\r\t");
			fw.write("Call Duration:  " + callDuration + " minutes");
			fw.write("\r\t");
			fw.write("Prev Balance:   $" + df2.format(st.getBalance()));
			fw.write("\r\t");
			fw.write("Call Cost:      $" + df2.format(callCost));
			fw.write("\r\t");
			fw.write("New Balance:    $" + df2.format((st.getBalance() - callCost)));
			fw.write("\r\r");
		}

	}

}
