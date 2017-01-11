/** 
 * @author  LiuCong  
 * @file    test.java
 * @date    Date：2015年12月20日 下午2:55:03 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.symboltbl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cn.edu.buaa.Matlab2C.exception.SemanticsException;

public class SymbolTables {

	private final Map<String, Type> varaT; // 变量符号表
	private final Map<String, Type> typeT; // 类型符号表
	private final Map<String, FunctionType> funT; // 函数符号表
	private final Map<String, Type> defT; // define符号表
	private final Map<String, Type> enumT; // 枚举符号表
	private final Map<String, Type> conT; // 常量符号表

	public static final Map<String, SymbolTables> compiledSymT = new HashMap<String, SymbolTables>(); // 已经编译过的文件
	public static final Map<String, LinkedList<String>> machineIncludes = new HashMap<String, LinkedList<String>>(); // 每个文件include文件

	// TODO funT应该是全局的，但又须给每个节点生成局部的funT
	// TODO funT常量符号表

	public SymbolTables() {
		super();
		varaT = new HashMap<String, Type>();
		typeT = new HashMap<String, Type>();
		funT = new HashMap<String, FunctionType>();
		defT = new HashMap<String, Type>();
		enumT = new HashMap<String, Type>();
		conT = new HashMap<String, Type>();
	}

	private SymbolTables(Map<String, Type> varaT, Map<String, Type> typeT,
			Map<String, FunctionType> funT, Map<String, Type> enumSetT,
			Map<String, Type> conT) {
		super();
		this.varaT = varaT;
		this.typeT = typeT;
		this.funT = funT;
		this.enumT = enumSetT;
		this.defT = new HashMap<String, Type>();
		this.conT = conT;
	}

	public SymbolTables(SymbolTables other) {
		super();
		varaT = new HashMap<String, Type>();
		typeT = new HashMap<String, Type>();
		enumT = new HashMap<String, Type>();
		conT = new HashMap<String, Type>();
		funT = other.funT;
		defT = other.defT;
	}

	public Type addVaraT(String id, Type type) {
		return varaT.put(id, type);
	}

	public Type addTypeT(String id, Type type) {
		return typeT.put(id, type);
	}

	public Type addEnumT(String id, Type type) {
		return enumT.put(id, type);
	}

	public Type addConT(String id, Type type) {
		return conT.put(id, type);
	}

	public Type addDefT(String id, Type type) {
		return defT.put(id, type);
	}

	public FunctionType addFun(String id, FunctionType type) {
		return funT.put(id, type);
	}

	public Type removeVaraT(String id) {
		return varaT.remove(id);
	}

	public Type removeTypeT(String id) {
		return typeT.remove(id);
	}

	public FunctionType removeFun(String id) {
		return funT.remove(id);
	}

	public boolean containsVara(String id) {
		return varaT.containsKey(id) || enumT.containsKey(id)
				|| defT.containsKey(id) || conT.containsKey(id);
	}

	public boolean containsType(String id) {
		return typeT.containsKey(id);
	}

	public boolean containsFun(String id) {
		return funT.containsKey(id);
	}

	/**
	 * id是否存在符号表中，包括常量，变量，函数名称，类型，枚举
	 * 
	 * @param id
	 * @return
	 */
	public boolean containsId(String id) {
		return varaT.containsKey(id) || enumT.containsKey(id)
				|| defT.containsKey(id) || conT.containsKey(id)
				|| funT.containsKey(id) || typeT.containsKey(id);
	}

	public void addInclude(SymbolTables symTab, String fileName)
			throws SemanticsException {
		for (String key : symTab.conT.keySet()) {
			if (containsId(key)) {
				throw SemanticsException.redefineException(fileName, -1,
						"include " + key + "redefined!");
			}
		}
		for (String key : symTab.enumT.keySet()) {
			if (containsId(key)) {
				throw SemanticsException.redefineException(fileName, -1,
						"include " + key + "redefined!");
			}
		}
		for (String key : symTab.defT.keySet()) {
			if (containsId(key)) {
				throw SemanticsException.redefineException(fileName, -1,
						"include " + key + "redefined!");
			}
		}
		for (String key : symTab.varaT.keySet()) {
			if (containsId(key)) {
				throw SemanticsException.redefineException(fileName, -1,
						"include " + key + "redefined!");
			}
		}
		for (String key : symTab.funT.keySet()) {
			if (containsId(key)) {
				throw SemanticsException.redefineException(fileName, -1,
						"include " + key + "redefined!");
			}
		}
		for (String key : symTab.typeT.keySet()) {
			if (containsId(key)) {
				throw SemanticsException.redefineException(fileName, -1,
						"include " + key + "redefined!");
			}
		}
		this.defT.putAll(symTab.defT);
		this.enumT.putAll(symTab.enumT);
		this.conT.putAll(symTab.conT);
		this.varaT.putAll(symTab.varaT);
		this.funT.putAll(symTab.funT);
		this.typeT.putAll(symTab.typeT);
	}

	/**
	 * 取标识符的类型
	 * 
	 */
	public Type getVaraT(String id) {
		return varaT.containsKey(id) ? varaT.get(id)
				: (enumT.containsKey(id) ? enumT.get(id) : (conT
						.containsKey(id) ? conT.get(id) : defT.get(id)));
	}

	/**
	 * 是否是不可变的常量define、const
	 * 
	 * @param id
	 * @return
	 */
	public boolean isConst(String id) {
		return conT.containsKey(id) || defT.containsKey(id);
	}

	public Type getTypeT(String id) {
		return typeT.get(id);
	}

	/**
	 * 取函数的类型
	 * 
	 * @param id
	 *            函数名
	 * @return 函数的类型，若没有名为id的函数，则返回null
	 */
	public FunctionType getFuncT(String id) {
		FunctionType re = funT.get(id);
		return re;
	}

	/**
	 * 检查所有的函数是否已经定义，避免只定义了函数的头，而没有函数的具体实现
	 */
	public void checkFuncAllDefined(String fileName) throws SemanticsException {
		for (FunctionType funcType : funT.values()) {
			if (funcType.isHeadDef()) {
				throw SemanticsException.undefineException(fileName, 0,
						"functionHead " + funcType.getfuncName() + " unImplement!");
			}
		}
	}

	// public SymbolTables snapshot() {
	// return new SymbolTables(new HashMap<String, Type>(varaT),
	// new HashMap<String, Type>(quanT), new HashMap<String, Type>(
	// typeT), new HashMap<String, FunctionType>(funT));
	// }

	public SymbolTables combineWith(SymbolTables other) {
		SymbolTables re = new SymbolTables(new HashMap<String, Type>(
				other.varaT), new HashMap<String, Type>(other.typeT),
				new HashMap<String, FunctionType>(other.funT),
				new HashMap<String, Type>(other.enumT),
				new HashMap<String, Type>(other.conT));
		re.varaT.putAll(varaT);
		re.typeT.putAll(typeT);
		re.funT.putAll(funT);
		re.enumT.putAll(enumT);
		re.defT.putAll(defT);
		re.conT.putAll(conT);
		return re;
	}

	@Override
	public String toString() {
		return "SymbolTable [varaT=" + varaT + ", typeT=" + typeT + ", funT="
				+ funT + "]";
	}

	public static void main(String[] args) {
		SymbolTables sym = new SymbolTables();
		// sym.addFun("fe", new FunctionType(Type.INT, "fdfe"));
		SymbolTables sym1 = new SymbolTables();
		// sym1.addFun("fe", new FunctionType(Type.FLOAT, "fefe"));
		System.out.println(sym);
		System.out.println(sym1);
		System.out.println(sym1.combineWith(sym));
		System.out.println(sym.combineWith(sym1));
	}

}
