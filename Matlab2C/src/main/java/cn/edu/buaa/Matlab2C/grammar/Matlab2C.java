package cn.edu.buaa.Matlab2C.grammar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.buaa.Matlab2C.automata.Automata;
import cn.edu.buaa.Matlab2C.grammar.MatNode;
import cn.edu.buaa.Matlab2C.exception.Matlab2CException;
import cn.edu.buaa.Matlab2C.exception.NoteException;
import cn.edu.buaa.Matlab2C.exception.GrammarException;
import cn.edu.buaa.Matlab2C.exception.SemanticsException;

public class Matlab2C {

	public static String baseRoot = "/home/chris/Documents/Matlab2C/";

	private FileReader MatlabFile;
	private int row = 0; // 行号
	private int noteFlag = 0; // 是否是多段注释
	private String fileName; // 文件名称
	private Stack<Automata> analysisStack = new Stack<Automata>(); // analysis
																	// automata
	private Stack<Automata> rollBackStack = new Stack<Automata>(); // rollback
																	// automatas
	private MatNode matNode = new MatNode(0);
	private MatNode nowNode = matNode;
	private MatNode rootNode = matNode;
	private ArrayList<String> allWords = new ArrayList<String>(); // 存储所有拆分好的字符串
	private ArrayList<Integer> wordsPosition = new ArrayList<Integer>();// 拆分好的字符串所处的行
	private int wordNum = 0;
	private int rollBackRow = 0; // 标记不能匹配的位置，方便标记错误出现的地方
	private Stack<Integer> nowStatusRollNums = new Stack<Integer>(); // 当前确定状态回滚栈中自动机的数目，方便不匹配时的回滚
	private Stack<Integer> nowNewStatusNums = new Stack<Integer>(); // 当前新自动机在分析栈中自动机的数目
	private int nextIsPlain = 1; // 拆分后接下来的字符是否是空白字符，主要是问了判断'.'两边是否是空白

	public Matlab2C() {
		MatlabFile = null;
	}
	
	/**
	 * 识别算法
	 * 
	 * @param fileName
	 * @throws SemanticsException
	 * @throws Matlab2CException
	 */
	public Matlab2C(String fileName) {
		// 生成自动机
		this.fileName = fileName;
		matNode.setFileName(this.fileName);
	}

	/**
	 * 编译入口
	 */
	public void compile() {
		try {
			if (Automata.automatas.isEmpty()) {
				Automata.generateAutomata();
			}
			addStack("Mat");
			nowNewStatusNums.push(1);
			MatlabFile = new FileReader(baseRoot + "/" + fileName);
			indentification();
			deleteNullNode(this.rootNode); // 删除节点中的空节点
//			functionNode.typeCheck();
			printNode(this.rootNode, 0);
			// System.out.println(fileName + " success!");
			//functionNode.toCCode();
			//functionNode.functionC.toFile();
//			System.out.println(fileName + " success!");
		} catch (Matlab2CException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

//	public MatNode getfunctionNode() {
//		return functionNode;
//	}

	/**
	 * 词法分析以及语法分析(下推自动机解析)
	 */
	private void indentification() throws Matlab2CException {
		BufferedReader bufferedReader = new BufferedReader(MatlabFile);
		// 相当于词法分析
		try {
			do {
				++this.row;
				String line = bufferedReader.readLine();
				if (line == null) {
					break;
				}
				line = line.replace('\t', ' ');// tab缩进
				line = line.trim();
				if (line.length() == 0) {
					continue;
				}
				if (this.judgeNote(line)) {
					continue;
				} else {
					line = removeNote(line);
				}
				if (line.length() == 0) {
					continue;
				} else {
					splitLine(line);
				}
			} while (true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (wordNum < allWords.size()) {
			Automata automata = analysisStack.peek();
			if (automata.getEnterWord().equals("ε")) {
				if (automata.getReturnStatus() != null) {
					jumpNewStatus();
				} else if (automata.getJumpStatus().equals("RET")) {
					retStack();
				} else {
					int num = nowNewStatusNums.pop();
					nowNewStatusNums.push(num + 1);
					addStack(automata.getJumpStatus());
				}
			} else {
				if (allWords.get(wordNum).equals(automata.getEnterWord())) {
					if (nowNode == matNode){
						switch(automata.getstartStatus()) {
						case "Func"  : nowNode = new FunctionNode(0); rootNode = nowNode; break;
						case "Conf"  : nowNode = new ConfigNode(0); rootNode = nowNode; break;
						case "Clas"  : nowNode = new ClassDefineNode(0); rootNode = nowNode; break;
						}
					}
					MatNode MatNode = new StringNode(allWords.get(wordNum),
							wordsPosition.get(wordNum++));
					nowNode.addChildNode(MatNode);
					MatNode.setFatherNode(nowNode);
					if (automata.getReturnStatus() != null) {
						jumpNewStatus();
					} else {
						if (automata.getJumpStatus().equals("FIN")) {
							if (wordNum < allWords.size()) {
								throw new GrammarException(fileName,
										wordsPosition.get(wordNum)
												+ ":FUNCTION is over!");
							}
						} else if (automata.getJumpStatus().equals("RET")) {
							retStack();
						} else {
							int num = nowNewStatusNums.pop();
							nowNewStatusNums.push(num + 1);
							addStack(automata.getJumpStatus());
						}
					}
				} else {
					if (rollBackRow < wordsPosition.get(wordNum)) {
						rollBackRow = wordsPosition.get(wordNum);
					}
					rollBack(0);
				}
			}
		}
		if (!analysisStack.peek().getJumpStatus().equals("FIN")) {
			throw new GrammarException(fileName, "END is lost");
		}
	}

	/**
	 * 跳转到一个新的状态
	 * 
	 * @throws GrammarException
	 */
	private void jumpNewStatus() throws GrammarException {
		Automata automata = analysisStack.peek();
		if (automata.getJumpStatus().equals("Id")) {
			// Id匹配
			if (Pattern.matches("[a-zA-Z_](\\w)*", allWords.get(wordNum))
					&& !isKeyWord(allWords.get(wordNum))) {
				MatNode idNode = new IdNode(allWords.get(wordNum),
						wordsPosition.get(wordNum++));
				idNode.setFatherNode(nowNode);
				nowNode.addChildNode(idNode);
				if (automata.getReturnStatus().equals("RET")) {
					retStack();
				} else {
					int num = nowNewStatusNums.pop();
					nowNewStatusNums.push(num + 1);
					addStack(automata.getReturnStatus());
				}
			} else {
				// 回滚
				if (rollBackRow < wordsPosition.get(wordNum)) {
					rollBackRow = wordsPosition.get(wordNum);
				}
				if (automata.getEnterWord().equals("ε")) {
					rollBack(0);
				} else {
					rollBack(1);
				}
			}
		} 
		else if (automata.getJumpStatus().equals("ChaL")) {
			// 字符匹配
			if (allWords.get(wordNum).startsWith("'")
					&& allWords.get(wordNum).endsWith("'")) {
				MatNode charNode = new CharLitNode(allWords.get(wordNum)
						.charAt(1), wordsPosition.get(wordNum++));
				charNode.setFatherNode(nowNode);
				nowNode.addChildNode(charNode);
				if (automata.getReturnStatus().equals("RET")) {
					retStack();
				} else {
					int num = nowNewStatusNums.pop();
					nowNewStatusNums.push(num + 1);
					addStack(automata.getReturnStatus());
				}
			} else {
				// 回滚
				if (rollBackRow < wordsPosition.get(wordNum)) {
					rollBackRow = wordsPosition.get(wordNum);
				}
				if (automata.getEnterWord().equals("ε")) {
					rollBack(0);
				} else {
					rollBack(1);
				}
			}
		} else if (automata.getJumpStatus().equals("StrL")) {
			// 字符串匹配
			if (allWords.get(wordNum).startsWith("\"")
					&& allWords.get(wordNum).endsWith("\"")) {
				MatNode stringLitNode = new StringLitNode(allWords.get(wordNum)
						.substring(1, allWords.get(wordNum).length() - 1),
						wordsPosition.get(wordNum++));
				nowNode.addChildNode(stringLitNode);
				stringLitNode.setFatherNode(nowNode);
				if (automata.getReturnStatus().equals("RET")) {
					retStack();
				} else {
					int num = nowNewStatusNums.pop();
					nowNewStatusNums.push(num + 1);
					addStack(automata.getReturnStatus());
				}
			} else {
				// 回滚
				if (rollBackRow < wordsPosition.get(wordNum)) {
					rollBackRow = wordsPosition.get(wordNum);
				}
				if (automata.getEnterWord().equals("ε")) {
					rollBack(0);
				} else {
					rollBack(1);
				}
			}
		} else if (automata.getJumpStatus().equals("UinL")) {
			// 无符号整形匹配
			if (allWords.get(wordNum).equals("0")
					|| Pattern.matches("[1-9]([0-9])*", allWords.get(wordNum))) {
				MatNode intNode;
				try {
					intNode = new UIntLitNode(Integer.parseInt(allWords
							.get(wordNum)), wordsPosition.get(wordNum++));
				} catch (Exception e) {
					intNode = new UIntLitNode(Long.parseLong(allWords
							.get(wordNum)), wordsPosition.get(wordNum++));
				}
				intNode.setFatherNode(nowNode);
				nowNode.addChildNode(intNode);
				if (automata.getReturnStatus().equals("RET")) {
					retStack();
				} else {
					int num = nowNewStatusNums.pop();
					nowNewStatusNums.push(num + 1);
					addStack(automata.getReturnStatus());
				}
			} else if (Pattern.matches("0x([0-9a-f])*", allWords.get(wordNum))) {
				MatNode intNode = new UIntLitNode(allWords.get(wordNum),
						wordsPosition.get(wordNum++));
				intNode.setFatherNode(nowNode);
				nowNode.addChildNode(intNode);
				if (automata.getReturnStatus().equals("RET")) {
					retStack();
				} else {
					int num = nowNewStatusNums.pop();
					nowNewStatusNums.push(num + 1);
					addStack(automata.getReturnStatus());
				}
			} else {
				// 回滚
				if (rollBackRow < wordsPosition.get(wordNum)) {
					rollBackRow = wordsPosition.get(wordNum);
				}
				if (automata.getEnterWord().equals("ε")) {
					rollBack(0);
				} else {
					rollBack(1);
				}
			}
		} else if (automata.getJumpStatus().equals("UflL")) {
			// 无符号浮点型匹配
			if ((allWords.get(wordNum).equals("0") || Pattern.matches(
					"[1-9]([0-9])*", allWords.get(wordNum)))
					&& allWords.get(wordNum + 1).equals(".")
					&& Pattern.matches("([0-9])+", allWords.get(wordNum + 2))) {
				MatNode floatNode = new UFloatLitNode(Float.valueOf(allWords
						.get(wordNum) + '.' + allWords.get(wordNum + 2)),
						wordsPosition.get(wordNum));
				floatNode.setFatherNode(nowNode);
				nowNode.addChildNode(floatNode);
				wordNum += 3;
				if (automata.getReturnStatus().equals("RET")) {
					retStack();
				} else {
					int num = nowNewStatusNums.pop();
					nowNewStatusNums.push(num + 1);
					addStack(automata.getReturnStatus());
				}
			} else {
				// 回滚
				if (rollBackRow < wordsPosition.get(wordNum)) {
					rollBackRow = wordsPosition.get(wordNum);
				}
				if (automata.getEnterWord().equals("ε")) {
					rollBack(0);
				} else {
					rollBack(1);
				}
			}
		} else if (automata.getJumpStatus().equals("TruL")) {
			// 命题true
			if (allWords.get(wordNum).equals("true")) {
				MatNode trueNode = new TrueLitNode(wordsPosition.get(wordNum++));
				trueNode.setFatherNode(nowNode);
				nowNode.addChildNode(trueNode);
				if (automata.getReturnStatus().equals("RET")) {
					retStack();
				} else {
					int num = nowNewStatusNums.pop();
					nowNewStatusNums.push(num + 1);
					addStack(automata.getReturnStatus());
				}
			} else {
				// 回滚
				if (rollBackRow < wordsPosition.get(wordNum)) {
					rollBackRow = wordsPosition.get(wordNum);
				}
				if (automata.getEnterWord().equals("ε")) {
					rollBack(0);
				} else {
					rollBack(1);
				}
			}
		} else if (automata.getJumpStatus().equals("FalL")) {
			// 命题false
			if (allWords.get(wordNum).equals("false")) {
				MatNode falseNode = new FalseLitNode(wordsPosition.get(wordNum++));
				falseNode.setFatherNode(nowNode);
				nowNode.addChildNode(falseNode);
				if (automata.getReturnStatus().equals("RET")) {
					retStack();
				} else {
					int num = nowNewStatusNums.pop();
					nowNewStatusNums.push(num + 1);
					addStack(automata.getReturnStatus());
				}
			} else {
				// 回滚
				if (rollBackRow < wordsPosition.get(wordNum)) {
					rollBackRow = wordsPosition.get(wordNum);
				}
				if (automata.getEnterWord().equals("ε")) {
					rollBack(0);
				} else {
					rollBack(1);
				}
			}
		} else {
			nowNewStatusNums.push(1);
			MatNode matNode = MatNode.getNode(automata.getJumpStatus(),
					wordsPosition.get(wordNum));
			matNode.setFatherNode(nowNode);
			nowNode.addChildNode(matNode);
			nowNode = matNode;
			addStack(automata.getJumpStatus());
		}
	}

	/**
	 * 当不能匹配自动机的时候需要回滚
	 * 
	 * @param flag
	 *            标记是初始的回滚自动机否匹配了输入字符，只有在回滚刚开始发生的自动机才会出现，因为当前自动机有可能匹配了输入字符，
	 *            但是没有匹配跳转状态中的Id等,1表示匹配了，0表示为”ε“,2表示不是起始的回滚自动机
	 * @throws GrammarException
	 */
	private void rollBack(int flag) throws GrammarException {
		Automata automata = analysisStack.peek();
		if (flag == 1) {
			// 初始的回滚自动机匹配了输入字符
			nowNode.removeLastChildNode();
			wordNum--;
		} else if (flag == 0) {
			// 初始的回滚自动机没有匹配输入字符
			;
		} else {
			// flag = 2表示自动机不是初始的回滚自动机
			if (automata.getReturnStatus() != null) {
				nowNode.removeLastChildNode();
				if (automata.getJumpStatus().equals("Id")
						|| automata.getJumpStatus().equals("UinL")
						|| automata.getJumpStatus().equals("ChaL")
						|| automata.getJumpStatus().equals("StrL")) {
					wordNum--;
				} else if (automata.getJumpStatus().equals("UflL")) {
					wordNum -= 3;
				}
			}
			if (!automata.getEnterWord().equals("ε")) {
				nowNode.removeLastChildNode();
				wordNum--;
			}
		}
		if (rollBackStack.isEmpty()) {
			throw new GrammarException(fileName, rollBackRow + "");
		}
		if (nowStatusRollNums.peek() > 0) {
			analysisStack.pop();
			analysisStack.push(rollBackStack.pop());
			int num = nowStatusRollNums.pop();
			nowStatusRollNums.push(num - 1);
		} else {
			nowStatusRollNums.pop();
			analysisStack.pop();
			int num = nowNewStatusNums.pop();
			if (num != 1) {
				nowNewStatusNums.push(num - 1);
				if (analysisStack.peek().getReturnStatus() != null) {
					// 新状态已经匹配完毕
					if (!analysisStack.peek().getJumpStatus().equals("Id")
							&& !analysisStack.peek().getJumpStatus()
									.equals("UinL")
							&& !analysisStack.peek().getJumpStatus()
									.equals("UflL")
							&& !analysisStack.peek().getJumpStatus()
									.equals("ChaL")
							&& !analysisStack.peek().getJumpStatus()
									.equals("StrL")) {
						rollBackNum(nowNode.getLastChildNode());
					}
				}
			} else {
				nowNode = nowNode.fatherNode;
			}
			rollBack(2);
		}
	}

	/**
	 * 回滚时，若已经匹配完一个完整地之状态，则需要将子状态中所有的word退栈
	 * 
	 * @param MatNode
	 */
	private void rollBackNum(MatNode MatNode) {
		for (MatNode node : MatNode.getChildNodes()) {
			if ((node instanceof IdNode) || (node instanceof UIntLitNode)
					|| (node instanceof CharLitNode)
					|| (node instanceof StringLitNode)
					|| (node instanceof StringNode)) {
				wordNum--;
			} else if ((node instanceof UFloatLitNode)) {
				wordNum -= 3;
			} else {
				rollBackNum(node);
			}
		}
	}

	/**
	 * 当跳转状态为"RET",分析栈和回滚栈将相应的自动机出栈
	 */
	private void retStack() {
		Automata automata = analysisStack.peek();
		while (true) {
			nowNode = nowNode.fatherNode;
			for (int i = 0, num = nowNewStatusNums.pop(); i < num; i++) {
				analysisStack.pop();
				for (int j = 0, numRoll = nowStatusRollNums.pop(); j < numRoll; j++) {
					rollBackStack.pop();
				}
			}
			automata = analysisStack.peek();
			if (!automata.getReturnStatus().equals("RET")) {
				int num = nowNewStatusNums.pop();
				nowNewStatusNums.push(num + 1);
				addStack(automata.getReturnStatus());
				break;
			}
		}
	}

	/**
	 * 判断是否为关键字
	 * 
	 * @param word
	 * @return
	 */
	private boolean isKeyWord(String word) {
		switch (word) {
		case "function":
		case "end":
		case "global":
		case "return":
		case "while":
		case "if":
		case "for":
		case "main":
		case "true":
		case "false":
		case "else":
		case "elseif":
			return true;
		default:
			return false;
		}

	}

	/**
	 * 跳转当当前类型的下一个状态
	 * 
	 * @param statue
	 */
	private void addStack(String status) {
		List<Automata> statusAutomatas = Automata.automatas.get(status);
		analysisStack.push(statusAutomatas.get(0));
		for (int j = statusAutomatas.size() - 1; j > 0; j--) {
			rollBackStack.push(statusAutomatas.get(j));
		}
			nowStatusRollNums.push(statusAutomatas.size() - 1);
	}
 
	/**
	 * 获取状态的前缀,例如Func1获取Func
	 */
	public String getStatuePre() {
		Automata automata = analysisStack.peek();
		String startStatue = automata.getstartStatus();
		Pattern pattern = Pattern.compile("([a-zA-Z])+");
		Matcher matcher = pattern.matcher(startStatue);
		matcher.find();
		startStatue = matcher.group();
		return startStatue;
	}

	/**
	 * 拆分一行，解析出字符以及字符串
	 * 
	 * @param line
	 * @return
	 * @throws GrammarException
	 */
	private void splitLine(String line) throws GrammarException {
		nextIsPlain = 1;
		line = line.trim();
		int i = 0;
		while (i < line.length()) {
			int length = isKeyChar(line, i);
			if (length == 0) {
				if (line.charAt(i) == ' ') {
					allWords.add(line.substring(0, i));
					wordsPosition.add(row);
					line = line.substring(i + 1).trim();
					i = 0;
					nextIsPlain = 1;
				} else {
					i++;
					nextIsPlain = 0;
				}

			} else {
				if (i != 0) {
					allWords.add(line.substring(0, i));
					wordsPosition.add(row);
				}
				allWords.add(line.substring(i, i + length));
				wordsPosition.add(row);
				if (line.length() > i + length
						&& line.charAt(i + length) == ' ') {
					nextIsPlain = 1;
				} else {
					nextIsPlain = 0;
				}
				line = line.substring(i + length).trim();
				i = 0;
			}
			// if(isKeyChar(line.charAt(i), line.charAt(i+1))) {
			// if(i>0) {
			// ret.add(line.substring(0, i));
			// }
			// ret.add(line.substring(i, i+1));
			// line = line.substring(i+1).trim();
			// i = -1;
			// } else if (line.charAt(i) ==' ') {
			// line = line.substring(i+1).trim();
			// ret.add(line.substring(0, i));
			// i = -1;
			// }
		}
		if (i != 0) {
			allWords.add(line);
			wordsPosition.add(row);
		}
	}

	private int deleteNullNode(MatNode MatNode) {
		int ret = 0;
		for (int i = 0; i < MatNode.getChildNodes().size(); i++) {
			MatNode node = MatNode.getChildNodeAt(i);
			if ((node instanceof IdNode) || (node instanceof UIntLitNode)
					|| (node instanceof CharLitNode)
					|| (node instanceof StringLitNode)
					|| (node instanceof StringNode)
					|| (node instanceof UFloatLitNode)
					|| (node instanceof TrueLitNode)
					|| (node instanceof FalseLitNode)) {
				ret++;
			} else {
				int num = deleteNullNode(node);
				if (num == 0) {
					MatNode.removeChildNodeAt(i--);
				} else {
					ret += num;
				}
			}
		}
		return ret;
	}

	/**
	 * 判断是否整行都是注释
	 * 
	 * @param line
	 */
	private boolean judgeNote(String line) {
		line = line.trim();
		if (noteFlag == 0 && line.startsWith("%%")) {
			return true;
		} else if (noteFlag == 1) {
			if (line.endsWith("*/")) {
				noteFlag = 0;
				return true;
			} else if (!Pattern.matches("\\*/", line)) {
				return true;
			}
		} else if (line.startsWith("/*")) {
			line = line.substring(2);
			if (line.endsWith("*/")) {
				return true;
			} else {
				Pattern pattern = Pattern.compile("\\*/");
				Matcher matcher = pattern.matcher(line);
				if (!matcher.find()) {
					noteFlag = 1;
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 需要比较两个甚至三个字符，因为/-\等等之间不允许出现空格
	 * 
	 * @param line
	 * @param start
	 * @throws GrammarException
	 * @return：匹配的关键字符的个数，失败返回0，错误则跑出错误
	 */
	private int isKeyChar(String line, int start) throws GrammarException {
		Set<Character> keyHelp = new HashSet<Character>(Arrays.asList('{', '}',
				'(', ')',':', '*', '+', ';', ',', '~'));
		if (keyHelp.contains(line.charAt(start))) {
			return 1;
		} else if (line.charAt(start) == '#') {
			if (line.length() < start + 7) {
				throw new GrammarException(fileName, row
						+ "; After '#' need 'define'");
			} else if (line.substring(start + 1, start + 7).equals("define")) {
				return 7;
			} else {
				throw new GrammarException(fileName, row
						+ "; After '#' need 'define'");
			}
		} else if (line.charAt(start) == '=' || line.charAt(start) == '>'
				|| line.charAt(start) == '<') {
			if (line.length() > start + 1) {
				if (line.charAt(start + 1) == '=') {
					return 2;
				} else if (line.charAt(start) == '>'
						&& line.charAt(start + 1) == '>') {
					return 2;
				} else if (line.charAt(start) == '<'
						&& line.charAt(start + 1) == '<') {
					return 2;
				} else {
					return 1;
				}
			} else {
				return 1;
			}
		} else if (line.charAt(start) == '!') {
			if (line.length() > start + 1 && line.charAt(start + 1) == '=') {
				return 2;
			} else {
				throw new GrammarException(fileName, row
						+ "; After '!' need '='");
			}
		} else if (line.charAt(start) == '.') {
			if (nextIsPlain == 1
					|| (line.length() > start + 1 && line.charAt(start + 1) == ' ')
					|| line.length() == start + 1) {
				throw new GrammarException(fileName, row
						+ ";exception occurred in '.'");
			} else {
				return 1;
			}
		} else if (line.charAt(start) == '|') {
			if (line.length() > start + 1 && line.charAt(start + 1) == '|') {
				return 2;
			} else {
				return 1;
			}
		} else if (line.charAt(start) == '&') {
			if (line.length() > start + 1 && line.charAt(start + 1) == '&') {
				return 2;
			} else {
				return 1;
			}
		} else if (line.charAt(start) == '-') {
			if (line.length() > start + 1
					&& (line.charAt(start + 1) == '-' || line.charAt(start + 1) == '>')) {
				return 2;
			} else {
				return 1;
			}
		} else if (line.charAt(start) == '\'') {
			for (int i = start + 1; i < line.length(); i++) {
				if (line.charAt(i) == '\'' && line.charAt(i - 1) != '\\') {
					return i - start + 1;
				}
			}
			throw new GrammarException(fileName, row + ";String is not fit");
		} 
		return 0;
	}

	/**
	 * 删除一行中夹杂的注释
	 */
	private String removeNote(String line) throws Matlab2CException {
		if (noteFlag == 1) {
			Pattern pattern = Pattern.compile("\\*/");
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				noteFlag = 0;
				line = line.substring(matcher.end());
				if (!judgeNote(line)) {
					line = removeNote(line);
				} else {
					line = null;
				}
			}
		} else {
			Pattern patternSlashStar = Pattern.compile("/\\*");
			Pattern patternStarSlash = Pattern.compile("\\*/");
			Pattern patternDoubleSlash = Pattern.compile("//");
			Matcher matcherSlashStar = patternSlashStar.matcher(line);
			Matcher matcherStarSlash = patternStarSlash.matcher(line);
			Matcher matcherDoubleSlash = patternDoubleSlash.matcher(line);
			int startSlashStar = matcherSlashStar.find() ? matcherSlashStar
					.start() : Integer.MAX_VALUE;
			int startStarSlash = matcherStarSlash.find() ? matcherStarSlash
					.start() : Integer.MAX_VALUE;
			int startDoubleSlash = matcherDoubleSlash.find() ? matcherDoubleSlash
					.start() : Integer.MAX_VALUE;
			if (startDoubleSlash < startSlashStar) {
				if (startStarSlash < startDoubleSlash) { // */在//之前,这里就不考虑字符串中包含了
					throw new NoteException(fileName, row);
				} else {
					line = line.substring(0, startDoubleSlash);
				}
			} else if (startSlashStar < startDoubleSlash) {
				if (startStarSlash < startSlashStar) { // */在//之前,这里就不考虑字符串中包含了
					throw new Matlab2CException(
							"NoteException:exception occured in line" + row);
				} else if (startStarSlash == Integer.MAX_VALUE) {
					noteFlag = 1;
					line = line.substring(0, startSlashStar);
				} else if (startStarSlash == startSlashStar + 1) { // /*/情况
					if (matcherStarSlash.find()) {
						if (matcherStarSlash.end() != line.length()) {
							line = line.substring(0, startSlashStar)
									+ line.substring(matcherStarSlash.end());
							line = removeNote(line);
						} else {
							return line.substring(0, startSlashStar);
						}
					} else {
						noteFlag = 1;
						line = line.substring(0, startSlashStar);
					}
				} else {
					if (matcherStarSlash.end() != line.length()) {
						line = line.substring(0, startSlashStar)
								+ line.substring(matcherStarSlash.end());
						line = removeNote(line);
					} else {
						return line.substring(0, startSlashStar);
					}
				}
			}

		}
		return line;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Matlab2C matlab2C;
		File file = new File(baseRoot);
		File fileC = new File(baseRoot+"c");
		if(!fileC.exists()) {
			fileC.mkdir();
		}
		String[] fileNames = file.list();
		for (String fileName : fileNames) {
			if (fileName.endsWith(".m")) {
				matlab2C = new Matlab2C(fileName);
				// Date dateStart = new Date();
				matlab2C.compile();
				// Date dateEnd = new Date();
				System.out.println(fileName);
				// System.out.println(dateEnd.getTime() - dateStart.getTime());
				// SymbolTables.compiledSymT.clear();
			}
		}
//		bStar = new BStar("test.bs");
//		bStar.compile();
//		System.out.println("Mutex.bs");
	}

	private void printNode(MatNode MatNode, int n) {
		String plain = "";
		for (int i = 0; i < n; i++) {
			plain += " ";
		}
		System.out.println(plain + MatNode.getClass().getSimpleName() + "  " + MatNode.getLine());
		for (MatNode node : MatNode.getChildNodes()) {
			printNode(node, n + 4);
		}
	}

}
