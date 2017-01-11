/** 
 * @author  LiuCong  
 * @file    test.java
 * @date    Date：2015年12月20日 下午2:55:03 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.symboltbl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//TODO 转C，toString
public final class FunctionType {

	private final Type rType; // 返回值类型
	private final String funcName; // 函数名
	private final List<Type> subTypes; // 形参类型
	private final List<String> subNames;// 形参名
	private boolean isHeadDef; // 是否只是函数头的定义

	public FunctionType(Type rType, String funcName, List<Type> subTypes,
			List<String> subNames, boolean isHeadDef) {
		super();
		this.rType = rType;
		this.funcName = funcName;
		this.subTypes = subTypes;
		this.subNames = subNames;
		this.isHeadDef = isHeadDef;
	}

	public FunctionType(Type rType, String funcName, boolean isHeadDef) {
		super();
		this.rType = rType;
		subTypes = new LinkedList<Type>();
		subNames = new LinkedList<String>();
		this.funcName = funcName;
		this.isHeadDef = isHeadDef;
	}

	public Type getrType() {
		return this.rType;
	}

	public String getfuncName() {
		return this.funcName;
	}

	public boolean isHeadDef() {
		return this.isHeadDef;
	}
	
	public void setIsHeadDef(boolean isHeadDef) {
		this.isHeadDef = isHeadDef;
	}

	// public String toCString(ToC toC,SymbolTables sym) {
	// String r=rType==null?"void":rType.toCString(toC, false,sym);
	// String re = r + " " + image + "(";
	//
	// Iterator<Type> i = subTypes.iterator();
	// Iterator<String> j = subNames.iterator();
	// int index = 0;
	// while (i.hasNext()) {
	// String s = i.next().toCString(toC, false,sym);
	// String st = j.next();
	// if (index != 0) {
	// re += ",";
	// }
	// re += s + " " + st;
	// index++;
	// }
	// re += ")";
	//
	// //MachineC中是否要增加方法的声明
	// toC.addType("extern " +re+";");
	// return re;
	// }

	@Override
	public String toString() {
		String re = rType + " " + funcName + "(";
		Iterator<Type> i = subTypes.iterator();
		Iterator<String> j = subNames.iterator();
		int index = 0;
		while (i.hasNext()) {
			String s = i.next().toString();
			String st = j.next();
			if (index != 0) {
				re += ",";
			}
			re += s + " " + st;
			index++;
		}
		re += ")";
		return re;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rType == null) ? 0 : rType.hashCode());
		result = prime * result
				+ ((subTypes == null) ? 0 : subTypes.hashCode());
		result = prime * result + (isHeadDef ? 0 : 1);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionType other = (FunctionType) obj;
		if (!rType.equals(other.rType)) {
			return false;
		}
		if (subTypes.size() != other.subTypes.size()) {
			return false;
		} else {
			if (!subTypes.equals(other.subTypes)
					|| !subNames.equals(other.subNames)) {
				return false;
			}
		}
		if (!funcName.equals(other.funcName)) {
			return false;
		}
		return true;
	}

	// public static boolean typeCheck(FunctionType t1, FunctionType t2) {
	// if (t1 == t2) {
	// return true;
	// }
	// if (t1 == null || t2 == null) {
	// return false;
	// }
	// if ((t1.getrType() == null || t2.getrType() == null)
	// && t1.getrType() != t2.getrType()) {
	// return false;
	// }
	// if (!Type.typeCheck(t1.getrType(), t2.getrType())) {
	// return false;
	// }
	// if (t1.subTypes.size() != t2.subTypes.size()) {
	// return false;
	// }
	// for (Iterator<Type> i = t1.subTypes.iterator(), j = t2.subTypes
	// .iterator(); i.hasNext();) {
	// if (!Type.typeCheck(i.next(), j.next())) {
	// return false;
	// }
	// }
	//
	// return true;
	// }
	//
	// public boolean equals2(Object obj) {
	// if (this == obj)
	// return true;
	// if (!(obj instanceof FunctionType)) {
	// return false;
	// }
	// FunctionType other = (FunctionType) obj;
	// if (!(rType == other.rType || (rType != null && rType
	// .equals(other.rType)))) {
	// return false;
	// }
	// if (!(subTypes == other.subTypes || (subTypes != null && subTypes
	// .equals(other.subTypes)))) {
	// return false;
	// }
	// return true;
	// }

	public boolean addSubType(Type type) {
		throw new IllegalArgumentException("Type:" + this
				+ " of subTypes should have name.");
	}

	public boolean addSubType(Type type, String name) {
		this.subTypes.add(type);
		return this.subNames.add(name);
	}

	public int getSubSize() {
		return subTypes.size();
	}

	public Type getSubType(int index) {
		return subTypes.get(index);
	}

	public String getSubName(int index) {
		return subNames.get(index);
	}

	// @Override
	// public String toString() {
	// String re = rType + "(";
	// re += StringUtils.join(paraTypes.iterator(), ",");
	// // for (Type t : paraTypes) {
	// // re += t + ",";
	// // }
	// re += ")";
	// return re;
	// }

	public static void main(String[] args) {
		// Type t = new Type(TUPLE);
		// t.addSubType(new Type(INT));
		// Type ts = new Type(SET);
		// ts.addSubType(new Type(TUPLE));
		// t.addSubType(ts);
		// FunctionType ft = new FunctionType(t, "fdfd");
		// ft.addSubType(ts, "para1");
		// ft.addSubType(new Type(INT), "para2");
		// System.out.println(ft);
		//
		// Type t1 = new Type(TUPLE);
		// t1.addSubType(new Type(FLOAT));
		// Type ts1 = new Type(SET);
		// ts1.addSubType(new Type(TUPLE));
		// t1.addSubType(ts1);
		// System.out.println(t.equals(t1));
	}

}
