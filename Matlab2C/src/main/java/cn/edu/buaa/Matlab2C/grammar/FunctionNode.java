package cn.edu.buaa.Matlab2C.grammar;

//import java.util.LinkedList;

import cn.edu.buaa.Matlab2C.c.FunctionC;
import cn.edu.buaa.Matlab2C.exception.SemanticsException;
import cn.edu.buaa.Matlab2C.symboltbl.SymbolTables;


public class FunctionNode extends MatNode {

	private String functionName;
	private String fileName;
	
	public String getfunctionName() {
		return functionName;
	}
	
	public void typeCheck() throws SemanticsException {
		this.gSymTab = new SymbolTables();
		this.incSymTab = new SymbolTables();
		setRootNode(this);
		functionName = ((IdNode) getChildNodeAt(1)).getId();
		for (int i = 2; i < getChildNodes().size() - 1; i++) {
			getChildNodeAt(i).typeCheck();
		}
		//检查所有的函数头是否在下面有具体的实现
		this.gSymTab.checkFuncAllDefined(fileName);  
		SymbolTables.compiledSymT.put(fileName, gSymTab);
	}
	
	/*public void toCCode() {
		// TODO Auto-generated method stub
		this.functionC = new FunctionC(fileName.substring(0,
				fileName.lastIndexOf('.')));
		LinkedList<String> includeFiles = SymbolTables.machineIncludes
				.get(fileName);
		if (includeFiles != null) {
			for (String name : includeFiles) {
				this.functionC.addhFile("#include \""
						+ name.substring(0, name.lastIndexOf('.'))
						+ ".h\"\n");
			}
		}
		for (int i = 2; i < getChildNodes().size() - 1; i++) {
			getChildNodeAt(i).toCCode();
		}
	}*/

	public void setfunctionName(String functionName) {
		this.functionName = functionName;
	}

	public FunctionNode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
		this.nodeKeyWord = "function";
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}
}
