package cn.edu.buaa.Matlab2C.grammar;

import java.util.LinkedList;

import cn.edu.buaa.Matlab2C.c.FunctionC;
import cn.edu.buaa.Matlab2C.exception.SemanticsException;
import cn.edu.buaa.Matlab2C.symboltbl.SymbolTables;
import cn.edu.buaa.Matlab2C.symboltbl.Type;


public class MatNode {

	public static StringBuilder cStringBuilder = new StringBuilder(); //记录下当前转为c的代码
	public static StringBuilder cSetBuilder = new StringBuilder();    //记录下转化为集合需要的其他代码
	protected String nodeKeyWord;
	protected String fileName;
	protected LinkedList<MatNode>  childNodes   = new LinkedList<MatNode>();
	protected MatNode      fatherNode;
	protected SymbolTables pSymTab;             //局部符号表
	protected SymbolTables gSymTab;             //全局符号表
	protected SymbolTables incSymTab;           //include进来的符号表
	protected int line;
	protected Type retType;
	protected MatNode rootNode;
	protected FunctionC functionC;
	
	public MatNode(int line){
		this.line = line;
	}
	
	/**
	 * 返回对应状态的新的节点
	 * @param statue
	 * @return
	 */
	public static MatNode getNode(String statue, int line) {
		switch(statue) {
			case "Func"  : return new FunctionNode(line);
			case "Conf"  : return new ConfigNode(line);
			case "Clas"  : return new ClassDefineNode(line);
			case "GloD"  : return new GlobalDefineNode(line);
			case "RetP"  : return new ReturnParasNode(line);
			case "ParL"  : return new ParaListNode(line);
			case "WhiS"  : return new WhileSNode(line);
			case "Sta"   : return new StatementNode(line);
			case "FunC"  : return new FunctionCallNode(line);
			case "AssS"  : return new AssignmentSNode(line);
			case "RetS"  : return new ReturnSNode(line);
			case "Ifs"   : return new IfSNode(line);
			case "ParVL" : return new ParaValueListNode(line);
			case "Ele"   : return new EleNode(line);
			case "AndPE" : return new AndProENode(line);
			case "ConTE" : return new ConditionTermENode(line);
			case "Mse"   : return new MSENode(line);
			case "UnaE"  : return new UnaryENode(line);
			case "OneE"  : return new OneENode(line);
			case "Ter"   : return new TermNode(line);
			case "MseT"  : return new MSETNode(line);
			case "SetId"  : return new SetIdNode(line);
			case "Empt"  : return new EmptyNode(line);

			default      : return null;
			
		}
	}
	
	/**
	 * 设定当前节点的父节点
	 * @param fatherNode
	 */
	public void setFatherNode(MatNode fatherNode) {
		this.fatherNode = fatherNode;
	}
	
	/**
	 * 获取父节点
	 * @return
	 */
	public MatNode getFatherNode() {
		return this.fatherNode;
	}
	
	/**
	 * 加入子节点
	 * @param childNode
	 */
	public void addChildNode(MatNode childNode) {
		this.childNodes.add(childNode);
	}
	
	/**
	 * 获取所有的子节点
	 * @return
	 */
	public LinkedList<MatNode> getChildNodes() {
		return this.childNodes;
	}
	
	public void removeLastChildNode() {
		if(!this.childNodes.isEmpty()) {
			this.childNodes.removeLast();
		}
	}
	
	public void removeFirstChildNode() {
		if(!this.childNodes.isEmpty()) {
			this.childNodes.removeFirst();
		}
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}
	public MatNode getLastChildNode() {
		return this.childNodes.getLast();
	}
	
	public MatNode getChildNodeAt(int index) {
		if(this.childNodes.size() > index){
			return this.childNodes.get(index);
		} else {
			return null;
		}
	}
	
	public boolean removeChildNodeAt(int index) {
		if(this.childNodes.size() > index){
			this.childNodes.remove(index);
			return true;
		} else {
			return false;
		}
	}

	public void typeCheck() throws SemanticsException {
		//updateSymTab;
		this.gSymTab   = getFatherNode().gSymTab;
		this.pSymTab   = getFatherNode().pSymTab;
		this.incSymTab = getFatherNode().incSymTab;
		this.retType   = getFatherNode().retType;
		this.rootNode  = getFatherNode().getRootNode();
	}
	
	public void toCCode() {
		this.functionC  = getFatherNode().functionC;
	}

	/**
	 * 查找变量id的类型
	 * @param id
	 * @return
	 * @throws SemanticsException 
	 */
	public Type getIdType(String id) throws SemanticsException {
		if(pSymTab != null) {
			if(pSymTab.containsVara(id)) {
				return pSymTab.getVaraT(id);
			} 
		}
		if (gSymTab.containsVara(id)){
			return gSymTab.getVaraT(id);
		}  else if (incSymTab.containsVara(id)) {
			return incSymTab.getVaraT(id);
		} else {
			throw SemanticsException.undefineException(getRootNode().getFileName(), line, id + " undefined!");
		}
	}
	
	/**
	 * 查找id是否已经定义
	 * @param id
	 * @return
	 */
	public boolean containsId(String id) {
		if(pSymTab != null) {
			if(pSymTab.containsId(id)) {
				return true;
			}
		}
		if (gSymTab.containsId(id)){
			return true;
		}  else if (incSymTab.containsId(id)) {
			return true;
		} else {
			return false;
		}
	}
	
	public MatNode getRootNode() {
		return rootNode;
	}
	
	public void setRootNode(FunctionNode FunctionNode) {
		this.rootNode = FunctionNode;
	}
	
	public int getLine(){
		return this.line;
	}
	
	public static void cleanCStringBuilder() {
		cStringBuilder.delete(0, cStringBuilder.length());
	}
	
	public static void cleanCSetBuilder() {
		cSetBuilder.delete(0, cSetBuilder.length());
	}
	
	
}
