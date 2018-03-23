import java.util.*;
import java.io.*;

public class DFA{
	static int max = Integer.MAX_VALUE;
	public static void main(String args[]) throws IOException, ClassNotFoundException{
		Scanner scan = new Scanner(System.in);
		int fin = 0;
		int j = 0;
		String DFA = new String("");
		String Final = new String("");
		String Alp = new String("");

		System.out.println("\nChoose option:\n\nLoad DFA: L\nCreate DFA: C\nDelete DFA: D\nQuit: Q\n\n");     								// Choose to load DFA
		String masterIn = scan.next();

		//Main page loop
		while (!(masterIn.equals("Q")|masterIn.equals("q"))) {     //QUIT
			if (masterIn.equals("L")|masterIn.equals("l")) {      //LOAD (ACCESS Z3 THROUGH HERE!)
				load(scan,max,fin,j,DFA,Final,Alp);
			}
			else if (masterIn.equals("C")|masterIn.equals("c")) { //CREATE
				mk(scan,max,fin,j,DFA,Final,Alp);
			}
			else if (masterIn.equals("D")|masterIn.equals("d")) { //DELETE
				delete(scan,max,fin,j,DFA,Final,Alp);
			}
			else {
				System.out.println("INVAID INPUT.");
			}
			System.out.println("\nChoose option:\n\nLoad DFA: L\nCreate DFA: C\nDelete DFA: D\nQuit: Q\n\n");
			masterIn = scan.next();
		}

		scan.close();
	}
	private static void delete(Scanner scan,int max,int fin,int j,String DFA,String Final,String Alp) {
		File folder = new File("Dfa");
		File[] listOfFiles = folder.listFiles();
		System.out.println("");
		int numOfDFAs=0;
		System.out.println("\nChoose file to delete:    'b' for back \n");
		for (int k=0;k<listOfFiles.length;k++) {									// List DFAs
			if(listOfFiles[k].getName().contains("DFA")) {
				System.out.println("File "+ listOfFiles[k].getName());
				numOfDFAs++;
			}
		}
		System.out.println();
		String fileNum = scan.next();											// Select DFA
		System.out.println();

		while ((!fileNum.equals("b")&&!strIsInt(fileNum,true))|(strIsInt(fileNum,false)&&Integer.parseInt(fileNum)>numOfDFAs)){
			if(strIsInt(fileNum,false))
				System.out.println("File doesn't exist");
			else
				System.out.println("\nChoose file to delete:    'b' for back \n");						    // Validity check
			fileNum = scan.next();
		}

		if(fileNum.equals("b")) 															// Return to main
			return;

		for (int k=0;k<listOfFiles.length;k++) {
			if(listOfFiles[k].getName().contains(fileNum)) {						// Get file names
				if(listOfFiles[k].getName().contains("DFA"))
					DFA=listOfFiles[k].getName();
				if(listOfFiles[k].getName().contains("final_States"))
					Final=listOfFiles[k].getName();
				if(listOfFiles[k].getName().contains("Alphabet"))
					Alp=listOfFiles[k].getName();
			}
		}

		File file = new File("Dfa/"+DFA); 				// Delete DFA data
		File filea = new File("Dfa/"+Final);
		File fileb = new File("Dfa/"+Alp);
		if(file.delete()&&filea.delete()&&fileb.delete())
		{
			System.out.println("\n"+DFA+" deleted successfully");
		}
		else
		{
			System.out.println("\nFailed to delete "+DFA);
		}
		return;
	}
	private static void save(Scanner scan,int max,int [][]Dfa, char[] alpha,int[] fina,int fin,int j,String DFA,String Final,String Alp) throws IOException, ClassNotFoundException {
		File folder = new File("Dfa");												// Get appropriate DFA number
		File[] listOfFiles = folder.listFiles();
		int fileNum = 0;
		int shouldbe = 1;
		top:
			for (int k=0;k<listOfFiles.length;k++) {
				if(listOfFiles[k].getName().contains("DFA")) {
					if((int)listOfFiles[k].getName().charAt(0)-48!=shouldbe) {      // Incase there are gaps in file numbers
						fileNum=shouldbe-1;
						break top;
					}
					if((int)listOfFiles[k].getName().charAt(0)-48>fileNum)
						fileNum=(int)listOfFiles[k].getName().charAt(0)-48;
					shouldbe++;
				}
			}
		fileNum++;

		if (fileNum>9) {																		// Too many DFAs
			System.out.println("\nWARNING! delete a DFA, only 9 can be stored at a time; DFA LOST");
			return;
		}

		System.out.println("Enter name for DFA(no spaces or numbers!): ");                     // Name DFA
		String dfaSaveName = scan.next();

		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Dfa/"+fileNum+"_DFA_"+dfaSaveName+".ser"));
		out.writeObject(Dfa);
		out.flush();
		out.close();
		out = new ObjectOutputStream(new FileOutputStream("Dfa/"+fileNum+"_final_States.ser"));
		out.writeObject(fina);
		out.flush();
		out.close();
		out = new ObjectOutputStream(new FileOutputStream("Dfa/"+fileNum+"_Alphabet.ser"));
		out.writeObject(alpha);
		out.flush();
		out.close();
		System.out.println(DFA+" saved.");
		return;
	}
	private static void mk(Scanner scan,int max,int fin,int j,String DFA,String Final,String Alp) throws IOException, ClassNotFoundException{
		int Dfa[][];
		char alpha[];
		int fina[];
		String testDFA="";

		System.out.println("Enter number of states");					// Take alphabet and number of states
		int num_states=max;
		num_states = GreaterThanZero(num_states, scan);

		System.out.println("Enter number of alphabet symbols");
		int i = max;
		i=GreaterThanZero(i, scan);

		j=i;
		alpha = new char [i];
		char candidate;
		while (i>0){
			System.out.println("Enter alphabet symbols, NO MISTAKES!!");
			candidate = scan.next().charAt(0);
			if (checkDistinctChar(alpha,candidate)) {
				alpha[i-1]=candidate;
				i--;
			}
			else {
				System.out.println(candidate+" already given");
			}
		}
		System.out.println(Arrays.toString(alpha));

		Dfa = new int [num_states][j];                          			 // Slot filler for DFA
		for (int x=0; x<num_states; x++){
			for (int y=0; y<j; y++){
				Dfa[x][y] = max;
			}
		}

		int z = max;                                                       // Take transition functions
		System.out.println("\n\nTransition functions.....\n\n\n");
		for (int x=0; x<num_states; x++){
			for (int y=0; y<j; y++){
				System.out.println(" deltaFunc("+x+","+alpha[y]+") : ");
				Dfa[x][y] = validateState(z,num_states,scan);;
			}
		}

		System.out.print("\nX|");                                          // Show transition table
		for(int x=0; x<j; x++)
			System.out.print(alpha[x]+",");
		System.out.println();
		for (int x=0; x<num_states; x++){
			System.out.print(x+"|");
			for (int y=0; y<j; y++){
				System.out.print(Dfa[x][y]+",");
			}System.out.println();
		}

		System.out.println("\nGive the number final states:");             // Final states
		fin = max;
		do {
			if (fin>num_states&&fin!=max)
				System.out.println("There aren't that many available states");
			while (!scan.hasNextInt()) {
				System.out.println("INVALID INPUT.");
				scan.next();
			}
			fin = scan.nextInt();
		} while (fin > num_states);

		fina = new int[fin];
		for (int x=0;x<fin; x++){
			System.out.println("Give final state:");
			fina[x]= validateState(fina[x] , num_states, scan);

		}

		System.out.println("Save DFA? Y/N");                                        // Choose save DFA
		String Save = scan.next();

		if (Save.equals("Y")|Save.equals("y")) {									// Save DFA
			save(scan,max,Dfa,alpha,fina,fin,j,DFA,Final,Alp);
		}
		else
			System.out.println("Not Saved");

		System.out.println("\nTest your DFA?:");								// Test Mode
		testDFA=scan.next();
		if(testDFA.equals("Y")|testDFA.equals("y"))
			test(scan,max,Dfa,alpha,fina,fin,j,DFA,Final,Alp);
		return;
	}
	private static void load(Scanner scan,int max,int fin,int j,String DFA,String Final,String Alp)throws IOException, ClassNotFoundException {
		int Dfa[][];
		char alpha[] ;
		int fina[];
		String testDFA = "";

		File folder = new File("Dfa");
		File[] listOfFiles = folder.listFiles();
		System.out.println("");

		int numOfDFAs = 0;
		System.out.println("\nChoose file to load:    'b' for back \n");
		for (int k=0;k<listOfFiles.length;k++) {									// List DFAs
			if(listOfFiles[k].getName().contains("DFA")) {
				System.out.println("File "+ listOfFiles[k].getName());
				numOfDFAs++;
			}
		}
		System.out.println();
		String fileNum = scan.next();											// Select DFA

		while ((!fileNum.equals("b")&&!(strIsInt(fileNum,true)))|(strIsInt(fileNum,false)&&Integer.parseInt(fileNum)>numOfDFAs)){
			if(strIsInt(fileNum,false))
				System.out.println("File doesn't exist");
			else
				System.out.println("\nChoose file to load:    'b' for back \n");						    // Validity check
			fileNum = scan.next();
		}

		if(fileNum.equals("b"))														// Back to main menu
			return;

		for (int k=0;k<listOfFiles.length;k++) {
			if(listOfFiles[k].getName().contains(fileNum)) {						// Get file names
				if(listOfFiles[k].getName().contains("DFA"))
					DFA=listOfFiles[k].getName();
				if(listOfFiles[k].getName().contains("final_States"))
					Final=listOfFiles[k].getName();
				if(listOfFiles[k].getName().contains("Alphabet"))
					Alp=listOfFiles[k].getName();
			}
		}

		ObjectInputStream in = new ObjectInputStream(new FileInputStream("Dfa/"+DFA));    // Load DFA
		Dfa = (int[][]) in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream("Dfa/"+Final));
		fina = (int[]) in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream("Dfa/"+Alp));
		alpha= (char[]) in.readObject();
		in.close();

		fin=fina.length;															// DFA meta data
		int num_states = Dfa.length;
		j = Dfa[0].length;

		System.out.println();
		System.out.println(DFA+": ");
		System.out.print("\nX|");                                               // Show transition table
		for(int x=0; x<j; x++)
			System.out.print(alpha[x]+",");
		System.out.println();
		for (int x=0; x<num_states; x++){
			System.out.print(x+"|");
			for (int y=0; y<j; y++){
				if (Dfa[x][y]==max)
					System.out.print('-'+",");
				else
					System.out.print(Dfa[x][y]+",");
			}System.out.println();
		}
		System.out.println("\nFinal states:" + Arrays.toString(fina));

		System.out.println("\nGenerate string?: y/n");						// Z3 STRING GENERATOR
		testDFA=scan.next();
		if(testDFA.equals("Y")|testDFA.equals("y"))
			z3(scan,max,Dfa,alpha,fina,fin,j,DFA,Final,Alp);
		System.out.println("Test your DFA?: y/n");							// Go to test mode
		testDFA=scan.next();
		if(testDFA.equals("Y")|testDFA.equals("y"))
			test(scan,max,Dfa,alpha,fina,fin,j,DFA,Final,Alp);
		return;
	}
	private static void z3(Scanner scan,int max,int [][]Dfa, char[] alpha,int[] fina,int fin,int j,String DFA,String Final,String Alp) {
		 String s = null;
		 System.out.println("Enter length of string to be generated: ");   // STRING LENGTH
		 int stringLength = max;
		 stringLength = GreaterThanZero(stringLength, scan);
		 System.out.println("Enter number of strings to be generated: ");  // NUMBER OF STRINGS
		 int numOfStr = max;
		 numOfStr = GreaterThanZero(numOfStr, scan);
	        try {                                                          // START Z3 CYCLONE PYTHON SCRIPT WITH CHOSEN DFA
	            Process p = Runtime.getRuntime().exec("python3 Cyclone.py "+DFA+" "+stringLength+" "+numOfStr);
	            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
	            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

	            // read the output from the command
	            while ((s = stdInput.readLine()) != null) {
	                System.out.println(s);
	            }

	            // read any errors from the attempted command
	            while ((s = stdError.readLine()) != null) {
	                System.out.println("Error: "+s);
	            }
	        }
	        catch (IOException e) {
	            System.out.println("Exception: ");
	            e.printStackTrace();
	            System.exit(-1);
	        }
		return;
	}
	private static void test(Scanner scan,int max,int [][]Dfa, char[] alpha,int[] fina,int fin,int j,String DFA,String Final,String Alp) {
		System.out.println("\n\nTest your strings..\n\nEnter String:    or '_b' to exit test mode");
		String str = scan.next();

		while(!str.equals("_b")){                                          // Test the DFA on strings

			int curstate = 0;
			boolean passed = false;

			out:
				for(int x=0; x<str.length(); x++){
					for(int y=0; y<j; y++){
						if (str.charAt(x)==alpha[y]){
							if (curstate==max)
								break out;
							curstate = Dfa[curstate][y];
							break;
						}
						else if (str.charAt(x)==max) {
							System.out.println("-No Transition for \'"+str.charAt(x)+"\' in state: "+curstate);
							curstate = max;
							break out;
						}
						else if (y==j-1) {
							System.out.println("-No Transition for \'"+str.charAt(x)+"\' in state: "+curstate);
							curstate = max;
							break out;
						}
					}
				}
			for (int x=0;x<fin; x++){
				if(fina[x]==curstate)
					passed=true;
			}
			if(passed)
				System.out.println("-->Accepted");
			else
				System.out.println("-->Rejected");

			System.out.println("\nNext String:");
			str = scan.next();
		}
		return;
	}
	private static boolean strIsInt(String s,boolean b) {
		boolean amIValid = false;
		try {
			Integer.parseInt(s);
			amIValid = true;
		} catch (NumberFormatException e) {
			if (b)
				System.out.println("INVALID INPUT.");
		}
		return amIValid;
	}
	private static boolean checkDistinctChar(char [] x, char y) {
		boolean res = true;
		for (int i=0; i<x.length; i++) {
			if (x[i]==y)
				res = false;
		}
		return res;
	}
	private static int validateState( int z, int num_states,Scanner scan){
		do {
			if (z>num_states-1&&z!=max)
				System.out.println("State doesn't exist.");
			while (!scan.hasNextInt()) {
				System.out.println("INVALID INPUT.");
				scan.next();
			}
			z = scan.nextInt();
		} while (z > num_states-1);
		return z;
	}
	private static int GreaterThanZero(int z,Scanner scan){
		do {
			if (z<1&&z!=max)
				System.out.println("Must be greater than zero.");
			while (!scan.hasNextInt()) {
				System.out.println("INVALID INPUT.");
				scan.next();
			}
			z = scan.nextInt();
		} while (z < 1);
		return z;
	}
}
