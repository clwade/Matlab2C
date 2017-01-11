package cn.edu.buaa.Matlab2C.automata;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Automata {

	private String startStatus;
	private String enterWord;
	private String stackTopEleIn;
	private String jumpStatus;
	private String returnStatus;
	private String stackTopEleOut;
	public static Map<String, List<Automata>> automatas = new HashMap<String, List<Automata>>(); // all
																									// automata

	public Automata(String startStatus, String enterWord, String stackTopEleIn,
			String jumpStatus, String returnStatus, String stackTopEleOut) {
		this.startStatus = startStatus;
		this.enterWord = enterWord;
		this.stackTopEleIn = stackTopEleIn;
		this.jumpStatus = jumpStatus;
		this.returnStatus = returnStatus;
		this.stackTopEleOut = stackTopEleOut;
	}

	public String getstartStatus() {
		return startStatus;
	}

	public void setstartStatus(String startStatus) {
		this.startStatus = startStatus;
	}

	public String getEnterWord() {
		return enterWord;
	}

	public void setEnterWord(String enterWord) {
		this.enterWord = enterWord;
	}

	public String getStackTopEleIn() {
		return stackTopEleIn;
	}

	public void setStackTopEleIn(String stackTopEleIn) {
		this.stackTopEleIn = stackTopEleIn;
	}

	public String getJumpStatus() {
		return jumpStatus;
	}

	public void setJumpStatus(String jumpStatus) {
		this.jumpStatus = jumpStatus;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getStackTopEleOut() {
		return stackTopEleOut;
	}

	public void setStackTopEleOut(String stackTopEleOut) {
		this.stackTopEleOut = stackTopEleOut;
	}

	// 读入下推自动机
	public static void generateAutomata() {
		try {
			BufferedReader file = new BufferedReader(new FileReader(
					"automata/MatlabAutomata.txt"));
			String statement = null;
			statement = file.readLine();
			while (statement != null) {
				statement = statement.trim();
				if (statement.length() > 0) {
					singleAutomata(statement);
				}
				statement = file.readLine();
			}
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		String returnString = "δ(";
		returnString += this.startStatus + ", " + this.enterWord + ", "
				+ this.stackTopEleIn + ")=(";
		if (this.returnStatus == null) {
			returnString += this.jumpStatus + ", " + this.stackTopEleOut + ")";
		} else {
			returnString += "[" + this.jumpStatus + ", " + this.returnStatus
					+ "], " + this.stackTopEleOut + ")";
		}
		return returnString;
	}

	private static void singleAutomata(String statement) {
		if (statement.substring(0, 1).equals("δ")) {
			List<String> elements = stringToAutomata(statement);
			Automata automata;
			if (elements.size() == 5) {
				automata = new Automata(elements.get(0), elements.get(1), elements.get(2),
						elements.get(3), null, elements.get(4));
			} else {
				automata = new Automata(elements.get(0), elements.get(1), elements.get(2),
						elements.get(3), elements.get(4), elements.get(5));
			}
			if (automatas.get(elements.get(0)) == null) {
				List<Automata> temp = new ArrayList<Automata>();
				temp.add(automata);
				automatas.put(elements.get(0), temp);
			} else {
				automatas.get(elements.get(0)).add(automata);
			}

//		System.out.println(automata.toString());
		} else {
			return;
		}
	}
	
	private static List<String> stringToAutomata(String line) {
		List<String> ret = new ArrayList<String>();
		int index = line.indexOf(',');
		ret.add(line.substring(2, index).trim());
		line = line.substring(index+1).trim();
		if(line.startsWith("\"")){
			int i = 1;
			String temp = "";
			char a = line.charAt(i++);
			while(a != '"') {
				if(a == '\\') {
					temp = temp+line.charAt(i++); 
				} else {
					temp = temp+a;
				}
				a  = line.charAt(i++);
			}
			ret.add(temp);
			line = line.substring(i);
			line = line.substring(line.indexOf(',')+1).trim();
		} else {
			ret.add("ε");
			line = line.substring(line.indexOf(',')+1).trim();
		}
		if(line.startsWith("\"")){
			int i = 1;
			String temp = "";
			char a = line.charAt(i++);
			while(a != '"') {
				if(a == '\\') {
					temp = temp+line.charAt(i++); 
				} else {
					temp = temp+a;
				}
				a  = line.charAt(i++);
			}
			ret.add(temp);
			line = line.substring(i);
			line = line.substring(line.indexOf('(')+1).trim();
		} else {
			ret.add("@");
			line = line.substring(line.indexOf('(')+1).trim();
		}
		if(line.charAt(0) == '[') {
			ret.add(line.substring(1, line.indexOf(',')).trim());
			ret.add(line.substring(line.indexOf(',')+1, line.indexOf(']')).trim());
			line = line.substring(line.indexOf("]")+1);
			line = line.substring(line.indexOf(',')+1).trim();
		} else {
			ret.add(line.substring(0, line.indexOf(',')).trim());
			line = line.substring(line.indexOf(',')+1).trim();
		}
		if(line.startsWith("\"")){
			int i = 1;
			String temp = "";
			char a = line.charAt(i++);
			while(a != '"') {
				if(a == '\\') {
					temp = temp+line.charAt(i++); 
				} else {
					temp = temp+a;
				}
				a  = line.charAt(i++);
			}
			ret.add(temp);
		} else {
			ret.add("@");
		}
		return ret;
	}

	public static void main(String args[]) {
		generateAutomata();
		
	}

}
